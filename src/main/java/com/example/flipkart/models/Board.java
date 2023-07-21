package com.example.flipkart.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Board {

    String id;

    String name;

    String url;

    List<User> users;

    List<Epic> epics;

    public Board(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

}
