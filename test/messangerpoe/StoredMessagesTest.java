package messangerpoe;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Unit tests for the Part 3 arrays and Stored Messages features, using test data messages 1 to 5.
public class StoredMessagesTest {

    // Every test starts with the arrays filled with the developer test data
    @Before
    public void loadTestData() {
        Message.addTestData();
    }

    // Turns a list of messages into a list of their texts so it can be compared.
    private ArrayList<String> getTexts(ArrayList<Message> messages) {
        ArrayList<String> texts = new ArrayList<>();
        for (Message m : messages) {
            texts.add(m.getMessageText());
        }
        return texts;
    }

    // Test (assertEquals) — arrays correctly populated

    @Test
    public void testSentMessagesArray_Populated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Did you get the cake?");
        expected.add("It is dinner time!");
        assertEquals(expected, getTexts(Message.getSentMessages()));
    }

    @Test
    public void testDisregardedMessagesArray_Populated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Yohoooo, I am at your gate.");
        assertEquals(expected, getTexts(Message.getDisregardedMessages()));
    }

    @Test
    public void testStoredMessagesArray_Populated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Where are you? You are late! I have asked you to be on time.");
        expected.add("Ok, I am leaving without you.");
        assertEquals(expected, getTexts(Message.getStoredMessages()));
    }

    @Test
    public void testMessageHashArray_Populated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("34:0:DIDCAKE");
        expected.add("77:1:WHERETIME");
        expected.add("55:2:YOHOOOOGATE");
        expected.add("08:3:ITTIME");
        expected.add("90:4:OKYOU");
        assertEquals(expected, Message.getMessageHashes());
    }

    @Test
    public void testMessageIDArray_Populated() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("3459871203");
        expected.add("7712345098");
        expected.add("5589012345");
        expected.add("0838884567");
        expected.add("9021345678");
        assertEquals(expected, Message.getMessageIDs());
    }

    // Test (assertEquals) — display the longest message

    @Test
    public void testLongestStoredMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage());
    }

    // Test (assertEquals) — search for message ID

    @Test
    public void testSearchForMessageID_Found() {
        Message found = Message.findMessageByID("0838884567");
        assertEquals("It is dinner time!", found.getMessageText());
        assertEquals("Developer", found.getRecipient());
    }

    @Test
    public void testSearchForMessageID_NotFound() {
        assertNull(Message.findMessageByID("1111111111"));
    }
}
