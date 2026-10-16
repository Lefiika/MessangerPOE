package messangerpoe;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;
import static org.junit.Assert.*;

// Unit tests for the Message class , using the task's Test Case 1 and 2 data.
public class MessageTest {

    // Test Case 1 data
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";

    // Test Case 2 data
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";

    // Recipient number (reuses the cell number logic)

    @Test
    public void testRecipientCell_Success() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        Message m = new Message(1, RECIPIENT_2, MESSAGE_2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                m.checkRecipientCell());
    }

    // Message length (250 characters)

    @Test
    public void testMessageLength_Success() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        // 255 characters, so it is 5 over the limit
        String longText = "a".repeat(255);
        Message m = new Message(0, RECIPIENT_1, longText);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", m.checkMessageLength());
    }

    // Message hash

    @Test
    public void testMessageHash_TestCase1() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.setMessageID("0012345678");
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    public void testMessageHash_TestCase2() {
        Message m = new Message(1, RECIPIENT_2, MESSAGE_2);
        m.setMessageID("9912345678");
        assertEquals("99:1:HIPAYMENT", m.createMessageHash());
    }

    // Send / disregard / store

    @Test
    public void testSentMessage_Send() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message m = new Message(1, RECIPIENT_2, MESSAGE_2);
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        Message m = new Message(1, RECIPIENT_2, MESSAGE_2);
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    // Storing messages as JSON

    @Test
    public void testStoreMessage_WritesJsonFile() throws Exception {
        File file = File.createTempFile("messages_test", ".json");
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.setMessageID("0012345678");

        assertTrue(m.storeMessage(file.getPath()));

        String json = new String(Files.readAllBytes(file.toPath()));
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("\"recipient\": \"+27718693002\""));
        assertTrue(json.contains("\"message\": \"Hi Mike, can you join us for dinner tonight?\""));
        file.delete();
    }

    @Test
    public void testStoreMessage_EscapesQuotes() throws Exception {
        File file = File.createTempFile("messages_test", ".json");
        Message m = new Message(0, RECIPIENT_1, "He said \"hi\" today");

        m.storeMessage(file.getPath());

        String json = new String(Files.readAllBytes(file.toPath()));
        assertTrue(json.contains("He said \\\"hi\\\" today"));
        file.delete();
    }

    // Total messages and printing 

    @Test
    public void testTotalMessagesIncreasesWhenSent() {
        int before = Message.returnTotalMessages();
        new Message(0, RECIPIENT_1, MESSAGE_1).sentMessage(1);
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testDisregardedMessageIsNotCounted() {
        int before = Message.returnTotalMessages();
        new Message(1, RECIPIENT_2, MESSAGE_2).sentMessage(2);
        assertEquals(before, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessageDetails_Order() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.setMessageID("0012345678");
        assertEquals("Message ID: 0012345678\nMessage Hash: 00:0:HITONIGHT\nRecipient: +27718693002\nMessage: " + MESSAGE_1,
                m.printMessageDetails());
    }

    @Test
    public void testPrintMessagesContainsSentMessage() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.sentMessage(1);
        assertTrue(Message.printMessages().contains(m.getMessageID()));
    }

    // Message ID

    @Test
    public void testMessageIDIsCreatedAndIsTenCharacters() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        assertTrue(m.checkMessageID());
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.getMessageID().matches("\\d{10}"));
    }

    @Test
    public void testMessageIDTooLongFails() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.setMessageID("12345678901");
        assertFalse(m.checkMessageID());
    }

    @Test
    public void testMessageIDAtLimitPasses() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        m.setMessageID("1234567890");
        assertTrue(m.checkMessageID());
    }
}
