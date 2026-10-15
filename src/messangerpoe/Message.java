package messangerpoe;

import java.util.Random;


public class Message {

    // Counts every message sent while the program is running.
    private static int totalMessagesSent = 0;

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

    // Reuses the cell number check from Login Class
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
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        // Remove punctuation so "tonight?" becomes "tonight"
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String firstTwo = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
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
