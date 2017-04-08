package DropThatFile.models;

import DropThatFile.engines.RSAEngine;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class Authenticator {
    private static final Map<String, String> users = new HashMap<String, String>();
    //1st parameter : Email address must respects email address' standard format.
    //2nd parameter : Password >= 8 characters and no whitespace, else error.
    static {
        users.put("test@test.com" , "testtest");
    }
    public static boolean validate(String user, String password) throws Exception {
        final String validUserPassword = users.get(user);

        final KeyPair pair = RSAEngine.generateKeyPair();

        final String cipheredText = RSAEngine.encrypt(password, pair.getPublic());

        final String decipheredMessage = RSAEngine.decrypt(cipheredText, pair.getPrivate());

        final String signature = RSAEngine.sign("opVGHjepyldnkuivD?uodtNVD8jdtyu", pair.getPrivate());

        boolean isCorrect = RSAEngine.verify("opVGHjepyldnkuivD?uodtNVD8jdtyu", signature, pair.getPublic());

        return validUserPassword != null && isCorrect == true && validUserPassword.equals(password);
    }
}