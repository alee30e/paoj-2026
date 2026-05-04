package com.pao.project.model;

import com.pao.project.exception.InvalidPasswordException;
import com.pao.project.exception.InvalidUsernameException;

public class User {
    private String id, username, password, role;
    private Client client;
    private static int nextId = 1;

    public User( String username, String password, String role, Client client) {
        validateUsername(username);
        validatePassword(password);
        this.id = genereateId();
        this.username = username;
        this.password = password;
        this.role = role;
        this.client = client;
    }
    public String genereateId(){
        return "user_"+ nextId ++;
    }
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Client getClient() {
        return client;
    }
    public void validatePassword(String password){
        if (password == null || password.length() < 8){
            throw new InvalidPasswordException("Parola trebuie sa aiba minim 8 caractere!");
        }
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String special = "@./*^&";
        for (char c : password.toCharArray()){
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (special.contains(String.valueOf(c))) hasSpecial = true;
        }

        if (!hasSpecial || !hasLower || !hasDigit || !hasUpper){
            throw new InvalidPasswordException("Parola trebuie sa contina cel putin o litera mare, una mica, o cifra si un caracter special '@./*^&'");
        }
    }
    private void validateUsername(String username){
        if (username == null || username.isBlank()) {
            throw new InvalidUsernameException("Username invalid!");
        }

        if (username.length() < 4 || username.length() > 20) {
            throw new InvalidUsernameException("Username-ul trebuie sa aiba intre 4 si 20 de caractere!");
        }

        String regex = "^[a-zA-Z0-9._]+$";
        if (!username.matches(regex)) {
            throw new InvalidUsernameException("Username-ul poate contine doar litere, cifre, . si _");
        }
    }
    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", client=" + client +
                '}';
    }
}
