package messangerpoe;

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

    // Reuses the cell number check from Login class.
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
            addToTotal();
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage("messages.json");
            return "Message successfully stored.";
        }
        return "Invalid option.";
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

    // Total number of messages sent so far.
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Called whenever a message is sent.
    public static void addToTotal() {
        totalMessagesSent++;
    }

    public String getMessageID() { return messageID; }
    // Lets unit tests set a known ID.
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}
