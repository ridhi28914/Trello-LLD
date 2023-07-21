package com.example.flipkart.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {

    String id;

    String name;

    String description;

    Integer priority;

    Epic epic;

    Board board;

    User user;

    Client client;


    public Card(String id, String name, String description, Integer priority, Epic epic, Board board) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.epic = epic;
        this.board = board;
        this.user = null;
    }
}