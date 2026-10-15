package messangerpoe;

import org.junit.Test;
import static org.junit.Assert.*;


public class MessageTest {

    // Test Case 1 data
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";

    // Test Case 2 data
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";


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

    //  Message length (250 characters)

    @Test
    public void testMessageLength_Success() {
        Message m = new Message(0, RECIPIENT_1, MESSAGE_1);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        String longText = "a".repeat(255);
        Message m = new Message(0, RECIPIENT_1, longText);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", m.checkMessageLength());
    }

    //  Message hash

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

    //  Message ID 

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
