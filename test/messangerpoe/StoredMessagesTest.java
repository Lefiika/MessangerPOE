package messangerpoe;

import java.io.File;
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

    // Test (assertEquals) — search all messages sent or stored for a particular recipient

    @Test
    public void testSearchByRecipient_Found() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Where are you? You are late! I have asked you to be on time.");
        expected.add("Ok, I am leaving without you.");
        assertEquals(expected, Message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testSearchByRecipient_NotFound() {
        assertEquals(0, Message.searchByRecipient("+27000000000").size());
    }

    // Test (assertEquals) — delete a message using a message hash

    @Test
    public void testDeleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteMessageByHash("77:1:WHERETIME"));
    }

    @Test
    public void testDeleteMessageRemovesFromArrays() {
        Message.deleteMessageByHash("77:1:WHERETIME");
        assertEquals(1, Message.getStoredMessages().size());
        assertEquals(4, Message.getMessageHashes().size());
        assertEquals(4, Message.getMessageIDs().size());
        assertFalse(Message.getMessageHashes().contains("77:1:WHERETIME"));
        assertFalse(Message.getMessageIDs().contains("7712345098"));
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        assertEquals("Message hash not found.", Message.deleteMessageByHash("00:0:NOTHING"));
    }

    // Test (assertEquals) — display report

    @Test
    public void testDisplayReport_SentMessages() {
        String expected = "Message ID: 3459871203\nMessage Hash: 34:0:DIDCAKE\nRecipient: +27834557896\nMessage: Did you get the cake?\n\n"
                + "Message ID: 0838884567\nMessage Hash: 08:3:ITTIME\nRecipient: Developer\nMessage: It is dinner time!\n\n";
        assertEquals(expected, Message.printMessages());
    }

    @Test
    public void testDisplayReport_StoredMessages() {
        String expected = "--- Stored Messages Report ---\n\n"
                + "Message ID: 7712345098\nMessage Hash: 77:1:WHERETIME\nRecipient: +27838884567\nMessage: Where are you? You are late! I have asked you to be on time.\n\n"
                + "Message ID: 9021345678\nMessage Hash: 90:4:OKYOU\nRecipient: +27838884567\nMessage: Ok, I am leaving without you.\n\n";
        assertEquals(expected, Message.displayStoredReport());
    }

    // Test (assertEquals) — read the JSON file into the stored messages array

    @Test
    public void testReadJsonFileIntoArray() throws Exception {
        File file = File.createTempFile("messages_test", ".json");
        Message.writeStoredMessagesToFile(file.getPath());
        Message.clearAll();

        assertEquals(2, Message.loadStoredMessages(file.getPath()));

        ArrayList<String> expectedTexts = new ArrayList<>();
        expectedTexts.add("Where are you? You are late! I have asked you to be on time.");
        expectedTexts.add("Ok, I am leaving without you.");
        assertEquals(expectedTexts, getTexts(Message.getStoredMessages()));

        ArrayList<String> expectedHashes = new ArrayList<>();
        expectedHashes.add("77:1:WHERETIME");
        expectedHashes.add("90:4:OKYOU");
        assertEquals(expectedHashes, Message.getMessageHashes());
        file.delete();
    }

    @Test
    public void testReadJsonFile_KeepsQuotesAndCommas() throws Exception {
        File file = File.createTempFile("messages_test", ".json");
        Message.clearAll();
        Message m = new Message(0, "+27718693002", "He said \"hi\", then left");
        m.storeMessage(file.getPath());
        Message.clearAll();

        Message.loadStoredMessages(file.getPath());
        assertEquals("He said \"hi\", then left", Message.getStoredMessages().get(0).getMessageText());
        file.delete();
    }

    @Test
    public void testReadJsonFile_MissingFile() {
        Message.clearAll();
        assertEquals(0, Message.loadStoredMessages("no_such_file.json"));
    }
}
