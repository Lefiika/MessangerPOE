package messangerpoe;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Login> registeredAccounts = new ArrayList<>();

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
                            quickChatMenu(scanner);
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
    public static void quickChatMenu(Scanner scanner) {
        System.out.println("Welcome to QuickChat.");

        boolean chatting = true;
        while (chatting) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
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
                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
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
