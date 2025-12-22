package com.path.tech.auth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class TestJwtFactory {

    private TestJwtFactory(){

    }
    public static JwtUtil jwtUtil(){
        try{
            KeyPair keyPair = generateRsaKeyPair();
            return new JwtUtil(keyPair.getPrivate(), keyPair.getPublic(),
                    "auth-service",15*60, 7*24*60*60);

        } catch(Exception e){
            throw new RuntimeException("Failed to create test jwtUtil",e);
        }
    }

    private static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
