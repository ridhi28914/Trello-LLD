package com.example.flipkart.service;

import com.example.flipkart.models.Board;
import com.example.flipkart.models.Card;
import com.example.flipkart.models.Epic;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EpicService {

    Map<String, Epic> epicMap = new HashMap<>();

    private  final CardService cardService;
    private final LockService lockService;

    public EpicService(CardService cardService, LockService lockService) {
        this.cardService = cardService;
        this.lockService = lockService;
    }

    public Epic createEpic(String name, Board board) {

        String id = UUID.randomUUID().toString();

        Epic epic = new Epic(id, name, board);

        epicMap.put(epic.getId(), epic);

        if(board.getEpics() != null) {
            board.getEpics().add(epic);
        }
        else {
            List<Epic> epics = new ArrayList<>();
            epics.add(epic);
            board.setEpics(epics);
        }
        return epic;
    }

    public void deleteEpic(List<String> epicIdList) {

        epicIdList.forEach(epicId -> {
            PriorityQueue<Card> cardList = epicMap.get(epicId).getCards();

            if (cardList != null)
                cardService.deleteCard(cardList.stream().map(card -> card.getId()).collect(Collectors.toList()));

            lockService.lockId(epicId);
            epicMap.remove(epicId);
            lockService.unlockId(epicId);

        });
    }

//    public void assignUser(String epicId, String cardId, String userId) {
//
//        Epic epic = epicMap.get(epicId);
//
//        epic.getCards().add(userId);
//    }
}
