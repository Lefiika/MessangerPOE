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
}
