package com.example.flipkart.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    String userId;

    String name;

    String email;

    List<Card> cardList;

    Client client;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}
