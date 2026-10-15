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

    // Makes a random ten-digit ID
    private String generateMessageID() {
        long number = (long) (new Random().nextDouble() * 10000000000L);
        return String.format("%010d", number);
    }

    // The ID must not be more than ten characters.
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
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
    
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}
