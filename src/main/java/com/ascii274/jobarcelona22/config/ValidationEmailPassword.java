package com.ascii274.jobarcelona22.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmailPassword {

    public static boolean validarEmail(String email){
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if(password == null || password == ""){
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

}
