import java.util.Scanner;

public class User {
    public Database database;
    private String cardNumber;
    private boolean loggedIn = false;
    private CardGenerator card;
    private double balance = 0;
    final Scanner scanner = new Scanner(System.in);
    public User(String fileName){
        database = new Database(fileName);
        mainMenu();

    }

    private void mainMenu(){
        char op;
        do {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            op = scanner.nextLine().charAt(0);
            System.out.println();
            switch (op){
                case '1':
                    createAccount();
                    break;
                case '2':
                    login();
                    break;
                case '0':
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }while (true);
    }
    private void createAccount(){
        card = new CardGenerator();
        card.generateCard();
        card.generatePin();

        String cardNumber = card.getCardNumber();
        String cardPin = card.getPassword();

        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + cardNumber);
        System.out.println("Your card PIN:\n" + cardPin);
        System.out.println();
        database.insert(cardNumber, cardPin, (int)balance);
    }

    private void login(){
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String cardPin = scanner.nextLine();
        System.out.println();

        if (database.select(cardNumber).equals(cardPin)){
            System.out.println("You have successfully logged in!");
            loggedIn = true;
            this.balance = database.getBalance(cardNumber);
            this.cardNumber = cardNumber;
            System.out.println();
            loggedInMenu();
        } else {
            System.out.println("Wrong card number or PIN!");
            System.out.println();
        }
    }

    private void loggedInMenu(){
        char op;
        do {
            System.out.println("1. Balance\n" +
                    "2. Deposit\n" +
                    "3. Withdraw\n" +
                    "4. Transfer funds\n" +
                    "5. Close account\n" +
                    "6. Log out\n" +
                    "0. Exit");

            op = scanner.nextLine().charAt(0);
            System.out.println();
            switch (op) {
                case '1':
                    viewBalance();
                    break;
                case '2':
                    addIncome();
                    break;
                case '3':
                    withdraw();
                    break;
                case '4':
                    doTransfer();
                    break;
                case '5':
                    closeAccount();
                    break;
                case '6':
                    logout();
                    break;
                case '0':
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }while (true);
    }

    private void withdraw() {
        System.out.println("Enter amount:");
        int amount = Integer.parseInt(scanner.nextLine());
        if (amount <= this.balance){
            database.updateBalance((-1 * amount), this.cardNumber);
            System.out.println("Successful");
        }else {
            System.out.println("Insufficient Balance!");
        }
        System.out.println();
    }

    private void closeAccount() {
        database.deleteAccount(this.cardNumber);
        System.out.println("Your account has been closed!");
        loggedIn = false;
        System.out.println();
        mainMenu();
    }

    private void doTransfer() {
        System.out.println("Transfer\nEnter card number:");
        String recipientNumber = scanner.nextLine();
        boolean validCard = card.checkValidity(recipientNumber);
        if (recipientNumber.equals(cardNumber)){
            System.out.println("You can't transfer money to the same account!");
        }else if (!validCard){
            System.out.println("Probably you made mistake in card number. Please try again!");
            System.out.println();
        }else {
            String pin = database.select(recipientNumber);
            if (pin.equals("")){
                System.out.println("Such card does not exist.");
            }else {
                System.out.println("Enter how much money you want to transfer:");
                int amount = Integer.parseInt(scanner.nextLine());
                if (amount > this.balance){
                    System.out.println("Insufficient Balance!");
                }else {
                    database.updateBalance((-1 * amount), this.cardNumber);
                    database.updateBalance(amount, recipientNumber);
                    System.out.println("Transaction Successful!");
                }

            }
            System.out.println();
        }
    }

    private void addIncome() {
        System.out.println("Enter amount:");
        int income = Integer.parseInt(scanner.nextLine());
        database.updateBalance(income, this.cardNumber);
        System.out.println("Transaction Successful!");
        System.out.println();
    }

    private void logout() {
        loggedIn = false;
        mainMenu();
    }

    private void viewBalance() {
        System.out.println("Balance: " + this.balance);
        System.out.println();
    }
}
