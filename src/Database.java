import java.sql.*;

public class Database {
    String fileName;
    public Database(String fileName){
        this.fileName = fileName;
        createTable();
    }
    public Connection connect(String fileName){
        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void createTable(){
        String url = "jdbc:sqlite:" + fileName;

        String sql = "CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0);";

        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void insert( String number, String pin, int balance){
        String sql = "INSERT INTO card (number,pin,balance) VALUES (?,?,?)";

        try(Connection connection = this.connect(fileName);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);
            statement.setInt(3,balance);
            statement.executeUpdate();
        }catch (SQLException | NullPointerException e){
            System.out.println(e.getMessage());
        }
    }
    public String select(String number){
        String sql = "SELECT pin FROM card WHERE number = ?";
        String result = "0";

        try(Connection conn = this.connect(fileName);
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, number);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                result = set.getString("pin");
                //System.out.println(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateBalance(int balance, String number){
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect(fileName);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, balance);
            statement.setString(2, number);
            statement.executeUpdate();
        }catch (SQLException e){
            e.getMessage();
        }
    }
    public int getBalance(String number){
        String sql = "SELECT balance FROM card WHERE number = ?";
        int balance = 0;

        try(Connection conn = this.connect(fileName);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                balance = set.getInt("balance");
            }
        }catch (SQLException e){
            e.getMessage();
        }
        return balance;
    }
    public void deleteAccount(String number){
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection conn = this.connect(fileName);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
