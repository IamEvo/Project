import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation {

    private Pattern pattern;
    private Matcher matcher;

    private static final String password_pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]{8,})";

    private static final String password_length = "^\\w{8,}";
    private static final String password_Ucase = "^(?=.*[A-Z])";
    private static final String password_Lcase = "^(?=.*[a-z])";
    private static final String password_Specialchar = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]{8,})";
    private static final String password_digit = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]{8,})";

    public Boolean[] validate(String password, String password2){
        //1=password not matching, 2=no lowercase, 3=no uppercase, 4=no special, 5=numbers, 6=not 8 char long
        Boolean[] errors = {true, true, true, true, true};

        if(password != password2){
            errors[0] = false;
        }

        pattern = Pattern.compile(password_pattern);
        matcher = pattern.matcher(password);
        if(matcher.matches() == false){

            pattern = Pattern.compile(password_Lcase);
            matcher = pattern.matcher(password);
            if(matcher.matches() == false) {
                errors[1] = false;
            }

            pattern = Pattern.compile(password_Ucase);
            matcher = pattern.matcher(password);
            if(matcher.matches() == false) {
                errors[2] = false;
            }

            pattern = Pattern.compile(password_Specialchar);
            matcher = pattern.matcher(password);
            if(matcher.matches() == false) {
                errors[3] = false;
            }

            pattern = Pattern.compile(password_digit);
            matcher = pattern.matcher(password);
            if(matcher.matches() == false) {
                errors[4] = false;
            }

            pattern = Pattern.compile(password_length);
            matcher = pattern.matcher(password);
            if(matcher.matches() == false) {
                errors[5] = false;
            }
        }

        return errors;
    }
}
