import java.sql.*;


public class DataController {

    //TODO create database account with lower permisions
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/messaging_system?autoReconnect=true&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public DataController() {
    }

    public Boolean[] login(String username, String password) {

        Hashing hashing = new Hashing();

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM user_info WHERE username = ?";

        Boolean[] sql_errors = {true, true};

        try {

            if(username_exists(username) == false){
                sql_errors[0] = false;
                return sql_errors;
            }

            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);

            // execute select SQL statement
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();

            //create the hash of the password using the salt stored in the database
            String salt_string = rs.getString(3);
            byte[] salt = toByteArray(salt_string);
            //creates the hash using the password entered and the salt stored in the database
            String hash[] = hashing.createHash(password.toCharArray(), salt);
            //gets the password hash stored in the database
            String database_hash_string = rs.getString(2);
            String pass_hash_string = hash[1];

            dbConnection.close();

            if (pass_hash_string.equals(database_hash_string)) {
                return sql_errors;
            } else {
                sql_errors[0] = false;
                return sql_errors;
            }

        } catch (Exception e) {
            System.out.println(e);
            sql_errors[1] = false;
            return sql_errors;
        }
    }

    public Boolean[] register(String username, String password) {
        Hashing hashing = new Hashing();

        String salt;
        String password_hash;
        //0=username exists, 2=sql connect failed
        Boolean[] sql_errors = {true, true};

        try {

            String hash[] = hashing.createNewHash(password);

            salt = hash[0];
            password_hash = hash[1];

            //System.out.println(hash[0] + hash[1]);

            Boolean user_exists = username_exists(username);
            if (user_exists == true) {
                sql_errors[0] = false;
                System.out.println(user_exists);
                return sql_errors;
            }

            // create a mysql database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/messaging_system?autoReconnect=true&useSSL=false", "root", "");

            // the mysql insert statement
            PreparedStatement stmt = con.prepareStatement("INSERT INTO user_info(username, Password, Salt) VALUES (?, ?, ?)");

            stmt.setString(1, username);
            stmt.setString(2, password_hash);
            stmt.setString(3, salt);

            stmt.executeUpdate();
            con.close();

            return sql_errors;
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.out.println(e);
            sql_errors[1] = false;
            return sql_errors;
        }

    }

    public Boolean username_exists(String username) {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT USERNAME FROM user_info WHERE username = ?";

        try {

            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);

            // execute select SQL statement
            ResultSet rs = preparedStatement.executeQuery();

            int size = 0;
            if (rs != null) {
                rs.last();
                size = rs.getRow();
                rs.beforeFirst();
            }

            dbConnection.close();

            if (size > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
        System.out.println(e);
        return false;
        }

    }

    public Boolean salt_exists(String salt) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/messaging_system?autoReconnect=true&useSSL=false", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `user_info` WHERE Salt= " + "'" + salt + "'");

            int size = 0;
            if (rs != null) {
                rs.beforeFirst();
                rs.last();
                size = rs.getRow();
            }

            con.close();

            if (size > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public Boolean delete_user(String username) {


        return true;
    }

    public static byte[] toByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }
}

