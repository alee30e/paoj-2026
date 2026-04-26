package com.pao.project.service;

import com.pao.project.exception.InvalidPasswordException;
import com.pao.project.exception.InvalidUsernameException;
import com.pao.project.exception.UserNotFoundException;
import com.pao.project.model.Client;
import com.pao.project.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private static UserService instance;
    private UserService(){
        users = new ArrayList<>();
    }
    public static UserService getInstance(){
        if (instance == null)
            instance = new UserService();
        return instance;
    }
    public User addUser(String username, String password,
                        String role, Client client){
        for (User u1 : users){
            if (u1.getUsername().equalsIgnoreCase(username)){
                throw new InvalidUsernameException("Exista un user cu acelasi nume");
            }
        }
        User u = new User(username, password, role, client);
        users.add(u);
        return u;
    }
    public User findByUsername(String username) {
        if (username == null || username.isEmpty()) throw new InvalidUsernameException("Username invalid");
        for (User u : users)
            if (u.getUsername().equalsIgnoreCase(username))
                return u;

        throw new UserNotFoundException("Userul nu exista");
    }
    public void changePassword(String username, String oldPassword, String newPassword){
        User u = findByUsername(username);
        if (!u.getPassword().equals(oldPassword)) {
            throw new InvalidPasswordException("Parola veche este incorecta.");
        }
        u.setPassword(newPassword);
    }
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    public void deleteUser(String username){
        User u = findByUsername(username);
        users.remove(u);
    }
    public User login(String username, String password){
        User u = findByUsername(username);
        if (u.getPassword().equals(password))
            return u;
        throw new InvalidPasswordException("Parola incorecta!");
    }
    
}
