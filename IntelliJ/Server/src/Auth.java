public class Auth {

    public Boolean[] login(String username, String password){
        DataController sql = new DataController();
        Boolean[] result = sql.login(username, password);
        return result;
    }

    public Boolean[] register(String username, String password){
        DataController sql = new DataController();
        Boolean[] sql_errors = {true, true};
        sql_errors = sql.register(username, password);
        return sql_errors;
    }
}
