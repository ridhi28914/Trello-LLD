package com.example.flipkart.service;

import com.example.flipkart.models.Card;
import com.example.flipkart.models.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    Map<String, User> userMap = new HashMap<>();

    private final CardService cardService;

    public UserService(CardService cardService) {
        this.cardService = cardService;
    }

    public User createUser(String name, String email) {

        String id = UUID.randomUUID().toString();

        User user = new User(id, name, email);

        userMap.put(user.getUserId(), user);

        return user;
    }

    public User getUser(String userId) {
        return userMap.get(userId);
    }

    public void assignCardToUser(String userId, String cardId) {

            User user = userMap.get(userId);
            cardService.assignCard(user, cardId);
    }

    public void deleteUser(String userId) {

        User user = userMap.get(userId);
        List<Card> cardList = user.getCardList();
        if (cardList == null)
            cardList = new ArrayList<>();

        cardList.forEach(card -> cardService.unassignCard(card.getId()));

        userMap.remove(userId);
    }

}
