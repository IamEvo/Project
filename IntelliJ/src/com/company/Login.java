package com.company;

//TODO https://security.stackexchange.com/questions/7075/authentication-between-multiple-systems-platforms-within-the-same-web-applicatio/7076#7076
import Protocols.Posting;

import java.security.SecureRandom;

public class Login {

    static private SecureRandom secureRandom;

    public Boolean[] register(Client client, String username, String password, String password2){

        //1=password not matching, 2=no lowercase, 3=no uppercase, 4=no special, 5=numbers, 6=not 8 char long
        //7 username exists, 8 username not long enough
        Boolean[] errors = {true, true, true, true, true, true, true, true};
        Boolean[] pw_validation_errors = {true, true, true, true, true};

        try {

            PasswordValidator login_session = new PasswordValidator();

            //TODO add username validation
            pw_validation_errors = login_session.validate(password, password2);

            for(int i=0; i<5; i++){
                errors[i] = pw_validation_errors[i];
            }

            for (Boolean temp: errors){
                if(temp == false){
                    return errors;
                }
            }

            Posting posting = new Posting(1,username, password);
            client.newPosting(posting);

            return errors;


        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

        return errors;
    }



    public Boolean login(Client client, String username, String password){

        Posting posting = new Posting(2,username, password);
        client.newPosting(posting);

        return false;

    }

    public static void register_errors(Boolean[] errors){
        try{
            //1=password not matching, 2=no lowercase, 3=no uppercase, 4=no special, 5=numbers, 6=not 8 char long
            //7 username exists, 8 username not long enough

            if(errors[0] == false){
                System.out.println("Passwords do not match");
            }

            if(errors[1] == false){
                System.out.println("Password does not contain lower case characters");
            }

            if(errors[2] == false){
                System.out.println("Password does not contain upper case characters");
            }

            if(errors[3] == false){
                System.out.println("Password does not contain special characters");
            }

            if(errors[4] == false){
                System.out.println("Password does not contain numbers");
            }

            if(errors[5] == false){
                System.out.println("Password is not 8 char long");
            }

            for(int i=0; i<6; i++){
                if(errors[i] == false){
                    return;
                }
            }

            if(errors[6] == false) {
                System.out.println("Username already in use");
            }

            if(errors[7] == false){
                System.out.println("Unable to connect to database");
            }

        }catch (Exception e){
            System.out.println("ERROR: " + e);
        }

    }
}
