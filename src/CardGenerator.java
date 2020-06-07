import java.util.Random;

public class CardGenerator {
    private static final String BEGIN_CARD_NUMBER = "400000";
    private String password;
    private String cardNumber;

    public void generateCard(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        builder.append(BEGIN_CARD_NUMBER);
        for (int i =0;i<10; i++){
            int num = random.nextInt(10);
            builder.append(num);
        }
        boolean cardIsValid = checkValidity(builder.toString());
        if (cardIsValid){
            cardNumber = builder.toString();
        }else {
            generateCard();
        }

    }
    public void generatePin(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<4; i++){
            int num = random.nextInt(10);
            builder.append(num);
        }
        password = builder.toString();
    }
    public boolean checkValidity(String cardNumber){
        boolean valid = false;
        StringBuilder builder = new StringBuilder(cardNumber);
        String lastDigit = String.valueOf(cardNumber.charAt(cardNumber.length()-1));
        builder.deleteCharAt(cardNumber.length()-1);
        String[] arr = builder.toString().split("");
        for (int i = 0; i<arr.length; i++){
            int num = Integer.parseInt(arr[i]);
            if (i == 0){
                arr[i] = String.valueOf(num * 2);
            }
            if (i % 2 == 0){
                arr[i] = String.valueOf(num * 2);
            }
        }
        for (int i = 0; i<arr.length; i++){
            int num = Integer.parseInt(arr[i]);
            if (num > 9){
                arr[i] = String.valueOf(num - 9);
            }
        }
        int sum = 0;
        for (String s : arr){
            sum += Integer.parseInt(s);
        }
        sum += Integer.parseInt(lastDigit);
        if (sum % 10 == 0){
            valid = true;
        }
        return valid;
    }

    public String getCardNumber(){
        return this.cardNumber;
    }
    public String getPassword(){
        return this.password;
    }
}
