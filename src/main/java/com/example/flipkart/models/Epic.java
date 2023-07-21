package com.example.flipkart.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;

@Getter
@Setter
public class Epic {

    String id;

    String name;

    Board board;

    PriorityQueue<Card> cards;

    Client client;

    public Epic(String id, String name, Board board) {
        this.id = id;
        this.name = name;
        this.board = board;
        this.cards = new PriorityQueue<>(Comparator.comparing(Card::getPriority));
    }
}
