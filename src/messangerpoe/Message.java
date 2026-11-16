package messangerpoe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Message {

    // Counts every message sent while the program is running.
    private static int totalMessagesSent = 0;

    // Every message sent while the program is running.
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // Messages the user chose to store for later (these are written to the JSON file).
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    // Messages the user chose to disregard.
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();

    // The hash and ID of every message. Index 0 of one array matches index 0 of the other.
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    private String messageID;     // random ten-digit number
    private int messageNumber;    // the loop counter for this message
    private String recipient;     // cell number with international code
    private String messageText;   // the actual message

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
    }

    // Makes a random ten-digit ID (leading zeros are kept, e.g. 0012345678).
    private String generateMessageID() {
        long number = (long) (new Random().nextDouble() * 10000000000L);
        return String.format("%010d", number);
    }

    // The ID must not be more than ten characters.
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Tells the user which ID was created for the message.
    public String getMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    // Reuses the cell number check from Part 1 (Login class).
    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // The message must not be more than 250 characters.
    public String checkMessageLength() {
        int length = messageText.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
    }

    // Hash format: first two digits of ID : message number : FIRSTWORDLASTWORD (all caps)
    // Example: 00:0:HITONIGHT
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        // Remove punctuation so "tonight?" becomes "tonight"
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String firstTwo = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Choice: 1 = send, 2 = disregard, 3 = store for later. Returns the message to show the user.
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            addHashAndID();
            addToTotal();
            return "Message successfully sent.";
        } else if (choice == 2) {
            disregardedMessages.add(this);
            addHashAndID();
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage("messages.json");
            return "Message successfully stored.";
        }
        return "Invalid option.";
    }

    // Adds this message's hash and ID to the two parallel arrays.
    private void addHashAndID() {
        messageHashes.add(createMessageHash());
        messageIDs.add(messageID);
    }

    // The details of this message in the required order: ID, Hash, Recipient, Message.
    public String printMessageDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Returns the details of every message sent while the program is running.
    public static String printMessages() {
        String all = "";
        for (Message m : sentMessages) {
            all = all + m.printMessageDetails() + "\n\n";
        }
        return all;
    }

    // Adds this message to the stored list and saves every stored message to a JSON file.
    // Returns true if the file was written.
    // Reference: JSON format (arrays, objects and string escaping) follows RFC 8259:
    // Bray, T. (2017). The JavaScript Object Notation (JSON) Data Interchange Format.
    // https://www.rfc-editor.org/rfc/rfc8259
    // Writing a file with FileWriter: Oracle. (n.d.). FileWriter (Java Platform SE 8).
    // https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
    public boolean storeMessage(String fileName) {
        storedMessages.add(this);
        addHashAndID();

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json = json + "  {\n"
                    + "    \"messageID\": \"" + escapeJson(m.messageID) + "\",\n"
                    + "    \"messageHash\": \"" + escapeJson(m.createMessageHash()) + "\",\n"
                    + "    \"recipient\": \"" + escapeJson(m.recipient) + "\",\n"
                    + "    \"message\": \"" + escapeJson(m.messageText) + "\"\n"
                    + "  }";
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        json = json + "]\n";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            return true;
        } catch (IOException e) {
            System.out.println("Could not save the message: " + e.getMessage());
            return false;
        }
    }

    // Makes text safe to put inside a JSON string (backslashes, quotes, new lines).
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    // Reads the JSON file into the stored messages array. Returns how many messages were loaded.
    // Reference: reading a text file line by line with BufferedReader and FileReader:
    // Oracle. (n.d.). BufferedReader (Java Platform SE 8).
    // https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
    public static int loadStoredMessages(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return 0;
        }

        int loaded = 0;
        String id = "";
        String hash = "";
        String recipient = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"messageID\"")) {
                    id = readJsonValue(line);
                } else if (line.startsWith("\"messageHash\"")) {
                    hash = readJsonValue(line);
                } else if (line.startsWith("\"recipient\"")) {
                    recipient = readJsonValue(line);
                } else if (line.startsWith("\"message\"")) {
                    String text = readJsonValue(line);
                    // The message number is the middle part of the hash, e.g. 00:0:HITONIGHT
                    int number = Integer.parseInt(hash.split(":")[1]);
                    Message m = new Message(number, recipient, text);
                    m.setMessageID(id);
                    storedMessages.add(m);
                    m.addHashAndID();
                    loaded++;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read the stored messages: " + e.getMessage());
        }
        return loaded;
    }

    // Gets the text inside the quotes after the colon and turns escaped characters back to normal.
    private static String readJsonValue(String line) {
        if (line.endsWith(",")) {
            line = line.substring(0, line.length() - 1);
        }
        String value = line.substring(line.indexOf(": \"") + 3, line.length() - 1);

        String result = "";
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                i++;
                char next = value.charAt(i);
                if (next == 'n') {
                    result = result + "\n";
                } else {
                    result = result + next;
                }
            } else {
                result = result + c;
            }
        }
        return result;
    }

    // Lists the sender and recipient of every stored message.
    public static String displaySenderAndRecipient(String sender) {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        String all = "";
        for (Message m : storedMessages) {
            all = all + "Sender: " + sender + "\nRecipient: " + m.recipient + "\n\n";
        }
        return all;
    }

    // Total number of messages sent so far.
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Called whenever a message is sent.
    public static void addToTotal() {
        totalMessagesSent++;
    }

    public static ArrayList<Message> getSentMessages() { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }
    public String getMessageID() { return messageID; }
    // Lets unit tests set a known ID.
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}
