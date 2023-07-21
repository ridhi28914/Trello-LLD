package com.example.flipkart.service;

import com.example.flipkart.models.Card;
import com.example.flipkart.models.Epic;
import com.example.flipkart.models.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CardService {

    Map<String, Card> cardMap = new HashMap<>();

    private final LockService lockService;

    public CardService(LockService lockService) {
        this.lockService = lockService;
    }

    public Card getCard(String cardId) {
        return cardMap.get(cardId);
    }

    public Card createCard(String name, String description, Integer priority, Epic epic) {

        String id = UUID.randomUUID().toString();

        Card card = new Card(id, name, description, priority, epic, epic.getBoard());

        cardMap.put(id, card);
        epic.getCards().add(card);

        return card;
    }

    public void deleteCard(List<String> cardIdList) {

        cardIdList.forEach(cardId -> {

            Card card = cardMap.get(cardId);

            PriorityQueue<Card> cardList = card.getEpic().getCards();
            if (cardList != null)
            {
                lockService.lockId(card.getEpic().getId());
                cardList.remove(card);
                lockService.unlockId(card.getEpic().getId());
            }

            lockService.lockId(cardId);
            cardMap.remove(cardId);
            lockService.unlockId(cardId);
        });

    }

    public void updateCardEpic(String cardId, Epic epic) {

        Card card = cardMap.get(cardId);

        lockService.lockId(card.getEpic().getId());
        card.getEpic().getCards().remove(card);
        lockService.unlockId(card.getEpic().getId());

        lockService.lockId(cardId);
        card.setEpic(epic);
        lockService.unlockId(cardId);

        lockService.lockId(epic.getId());
        epic.getCards().add(card);
        lockService.unlockId(epic.getId());

    }

    public void assignCard(User user, String cardId) {

        Card card = cardMap.get(cardId);

        Integer count = 0;

        for(Card card1: cardMap.values()) {
            if(card1.getUser() != null && card1.getUser().equals(user)) {
                count++;
            }
        }

        if(count < 5 ) {
            lockService.lockId(cardId);
            card.setUser(user);
            lockService.unlockId(cardId);

            if(user.getCardList() != null) {
                user.getCardList().add(card);
            }
            else {
                List<Card> cardList = new ArrayList<>();
                cardList.add(card);
                user.setCardList(cardList);
            }
        }
        else
            throw new RuntimeException("User cannot be assigned more than 5 cards");
    }

    public void unassignCard(String cardId) {
        Card card = cardMap.get(cardId);

        User user = card.getUser();

        lockService.lockId(user.getUserId());
        user.getCardList().remove(card);
        lockService.unlockId(user.getUserId());

        lockService.lockId(cardId);
        card.setUser(null);
        lockService.unlockId(cardId);

    }
}
