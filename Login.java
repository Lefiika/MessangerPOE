
public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public Login() {
        this.username = "";
        this.password = "";
        this.cellPhoneNumber = "";
        this.firstName = "";
        this.lastName = "";
    }

    public Login(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Username must contain an underscore and be no more than 5 characters long.
    public boolean checkUserName() {
        return checkUserName(this.username);
    }

    public boolean checkUserName(String username) {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    // Password must be at least 8 characters, with a capital letter, a number, and a special character.
    public boolean checkPasswordComplexity() {
        return checkPasswordComplexity(this.password);
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        return hasCapital && hasNumber && hasSpecial;
    }


    // Reference: regular expression syntax researched from the official
    // Java Pattern class documentation and the "^\\+" anchor/escaping
    // approach for matching a leading international dialling code.
    // Oracle. (n.d.). Class Pattern. Java Platform SE Documentation.
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html

    public boolean checkCellPhoneNumber() {
        return checkCellPhoneNumber(this.cellPhoneNumber);
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) return false;
        String sanitizedNumber = cellPhoneNumber.replaceAll("[\\s-]", "");
        String regex = "^\\+27\\d{9}$";
        return sanitizedNumber.matches(regex);
    }
    

    // Returns a message indicating the result of the registration attempt, based on the validation of the username, password, and cell phone number.
    public String registerUser() {
        if (!checkUserName(this.username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(this.password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(this.cellPhoneNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
        return "Username, password, and cell phone number successfully captured.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (this.username == null || this.password == null) {
            return false;
        }
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public void setCellPhoneNumber(String cellPhoneNumber) { this.cellPhoneNumber = cellPhoneNumber; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
