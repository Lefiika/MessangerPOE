package messangerpoe;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Login> registeredAccounts = new ArrayList<>();

        // Part 3: load the stored messages from the JSON file when the program starts
        Message.loadStoredMessages("messages.json");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": {
                    System.out.print("Enter First Name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter Last Name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();

                    System.out.print("Enter Cell Phone Number: ");
                    String cellNumber = scanner.nextLine();

                    Login newAccount = new Login(username, password, cellNumber, firstName, lastName);
                    String regStatus = newAccount.registerUser();
                    System.out.println(regStatus);

                    if (regStatus.contains("successfully captured")) {
                        registeredAccounts.add(newAccount);
                    }
                    break;
                }
                case "2": {
                    System.out.print("Enter Username to Login: ");
                    String loginUser = scanner.nextLine();

                    System.out.print("Enter Password to Login: ");
                    String loginPass = scanner.nextLine();

                    Login matchedAccount = null;
                    for (Login account : registeredAccounts) {
                        if (account.getUsername().equals(loginUser)) {
                            matchedAccount = account;
                            break;
                        }
                    }

                    if (matchedAccount != null) {
                        boolean loginSuccess = matchedAccount.loginUser(loginUser, loginPass);
                        System.out.println(matchedAccount.returnLoginStatus(loginSuccess));

                        // Part 2: only logged in users can use QuickChat
                        if (loginSuccess) {
                            quickChatMenu(scanner, matchedAccount);
                            running = false;   // the user chose Quit, so the program ends
                        }
                    } else {
                        // No stored account has this username, so it's a failed login.
                        System.out.println(new Login().returnLoginStatus(false));
                    }
                    break;
                }
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }

        scanner.close();
    }

    // Part 2: welcome message and numeric menu. Runs until the user chooses Quit.
    public static void quickChatMenu(Scanner scanner, Login user) {
        System.out.println("Welcome to QuickChat.");

        boolean chatting = true;
        while (chatting) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit\n4) Stored Messages");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    chatting = false;
                    break;
                case "4":
                    storedMessagesMenu(scanner, user);
                    break;
                default:
                    System.out.println("Invalid option, please enter 1, 2, 3 or 4.");
            }
        }
    }

    // Part 3: the Stored Messages menu. Runs until the user chooses Back.
    public static void storedMessagesMenu(Scanner scanner, Login user) {
        String sender = user.getFirstName() + " " + user.getLastName() + " (" + user.getCellPhoneNumber() + ")";

        boolean viewing = true;
        while (viewing) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for messages by recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println(Message.displaySenderAndRecipient(sender));
                    break;
                case "2":
                    System.out.println("Longest stored message: " + Message.getLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter the message ID: ");
                    Message found = Message.findMessageByID(scanner.nextLine().trim());
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getMessageText());
                    }
                    break;
                case "4":
                    System.out.print("Enter the recipient cell number: ");
                    ArrayList<String> results = Message.searchByRecipient(scanner.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for this recipient.");
                    } else {
                        for (String text : results) {
                            System.out.println("Message: " + text);
                        }
                    }
                    break;
                case "5":
                    System.out.print("Enter the message hash: ");
                    System.out.println(Message.deleteMessageByHash(scanner.nextLine().trim()));
                    Message.writeStoredMessagesToFile("messages.json");
                    break;
                case "6":
                    System.out.println("Coming Soon.");
                    break;
                case "7":
                    viewing = false;
                    break;
                default:
                    System.out.println("Invalid option, please enter a number from 1 to 7.");
            }
        }
    }

    // Part 2: the user says how many messages to enter, then a for loop captures each one.
    public static void sendMessages(Scanner scanner) {
        // Ask for the number of messages (must be a whole number above 0)
        int numberOfMessages = 0;
        while (numberOfMessages < 1) {
            System.out.print("How many messages do you want to enter? ");
            String input = scanner.nextLine();
            try {
                numberOfMessages = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                numberOfMessages = 0;
            }
            if (numberOfMessages < 1) {
                System.out.println("Please enter a whole number of 1 or more.");
            }
        }

        // The loop counter is used as the message number (0, 1, 2, ...)
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numberOfMessages + " ---");

            // Recipient: keep asking until the number is valid
            String recipient = "";
            Message check = null;
            boolean validRecipient = false;
            while (!validRecipient) {
                System.out.print("Enter the recipient cell number (with international code): ");
                recipient = scanner.nextLine();
                check = new Message(i, recipient, "");
                String result = check.checkRecipientCell();
                System.out.println(result);
                validRecipient = result.equals("Cell phone number successfully captured.");
            }

            // Message text: keep asking until it is 250 characters or less
            Message message = null;
            boolean validLength = false;
            while (!validLength) {
                System.out.print("Enter your message (max 250 characters): ");
                String text = scanner.nextLine();
                message = new Message(i, recipient, text);
                String result = message.checkMessageLength();
                System.out.println(result);
                validLength = result.equals("Message ready to send.");
            }

            System.out.println(message.getMessageIDStatus());

            // Send, disregard or store
            int choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.println("1) Send Message\n2) Disregard Message\n3) Store Message to send later");
                System.out.print("Choose an option: ");
                String input = scanner.nextLine().trim();
                if (input.equals("1") || input.equals("2") || input.equals("3")) {
                    choice = Integer.parseInt(input);
                } else {
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
                }
            }

            System.out.println(message.sentMessage(choice));

            if (choice == 1) {
                // Show the full details of the sent message
                System.out.println(message.printMessageDetails());
            } else if (choice == 2) {
                String answer = scanner.nextLine().trim();
                if (answer.equals("0")) {
                    System.out.println("Message deleted.");
                } else {
                    System.out.println("Message was not sent.");
                }
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
    }
}
