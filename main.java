import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

        Login userAccount = new Login(username, password, cellNumber, firstName, lastName);
        String regStatus = userAccount.registerUser();
        System.out.println(regStatus);

        scanner.close();
    }
}