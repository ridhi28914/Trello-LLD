package com.example.flipkart;


import com.example.flipkart.models.Board;
import com.example.flipkart.models.Card;
import com.example.flipkart.models.Epic;
import com.example.flipkart.models.User;
import com.example.flipkart.service.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BoardServiceTest {


    LockService lockService = new LockService();
    CardService cardService = new CardService(lockService);
    UserService userService = new UserService(cardService);

    EpicService epicService = new EpicService(cardService, lockService);

    BoardService boardService = new BoardService(lockService, userService, epicService, cardService);


    @Test
    public void test1() {

        Board board = boardService.createBoard("board", "board.url");
        assert board.getName().equals("board");

        User user1 = userService.createUser("user1", "dghd.com");
        User user2 = userService.createUser("user2", "sjsk.com");

        boardService.addMembers(board.getId(), List.of(user1.getUserId()));

        Epic epic1 = epicService.createEpic("epic1", board);
//        Epic epic2 = epicService.createEpic("epic2", board);

        Card card = cardService.createCard("card1", "desc11", 0, epic1);
        Card card2 = cardService.createCard("card2", "desc12", 1, epic1);

        cardService.assignCard(user1, card.getId());
//        cardService.assignCard(user1, card3.getId());

//        cardService.assignCard(user2, card2.getId());
//        cardService.assignCard(user2, card4.getId());

        Board board1 = boardService.getBoard(board.getId());

//        assert board1.getEpics().size() == 2;

        Board board2 = boardService.createBoard("board2", "board2.url");
        boardService.addMembers(board2.getId(), List.of(user2.getUserId()));
        Epic epic2 = epicService.createEpic("epic2", board2);
        Card card4 = cardService.createCard("card4", "desc14", 1, epic2);
        Card card3 = cardService.createCard("card3", "desc13", 0, epic2);

        cardService.assignCard(user2, card3.getId());
        cardService.assignCard(user2, card4.getId());

//        boardService.deleteBoard(board2.getId());

        boardService.removeMembers(board2.getId(), List.of(user2.getUserId()));

//        Card card5 = cardService.createCard("card3", "desc13", 0, epic2);
//        Card card6 = cardService.createCard("card4", "desc14", 1, epic2);
//        Card card6 = cardService.createCard("card4", "desc14", 1, epic2);
//        Card card6 = cardService.createCard("card4", "desc14", 1, epic2);






    }

}
