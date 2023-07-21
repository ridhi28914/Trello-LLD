package com.example.flipkart.service;

import com.example.flipkart.models.Board;
import com.example.flipkart.models.Epic;
import com.example.flipkart.models.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {

    Map<String, Board> boardMap = new HashMap<>();

    private final LockService lockService;
    private final UserService userService;
    private final EpicService epicService;

    private  final CardService cardService;

    public BoardService(LockService lockService, UserService userService, EpicService epicService, CardService cardService) {
        this.lockService = lockService;
        this.userService = userService;
        this.epicService = epicService;
        this.cardService = cardService;
    }

    public Board createBoard(String name, String url) {

        String id = UUID.randomUUID().toString();

//        Todo: create a new URL
        Board board = new Board(id, name, url);

        boardMap.put(id, board);

        return board;
    }

    public Board getBoard(String boardId) {
        return boardMap.get(boardId);
    }

    public void deleteBoard(String boardId) {

        List<Epic> epics = boardMap.get(boardId).getEpics();
        if (epics == null)
            epics = new ArrayList<>();

        epicService.deleteEpic(epics.stream().map(epic -> epic.getId()).collect(Collectors.toList()));

        Board board = boardMap.get(boardId);
        lockService.lockId(boardId);
        board.setUsers(null);
        board.setEpics(null);
        boardMap.remove(boardId);
        lockService.unlockId(boardId);
    }

    public void addMembers(String boardId, List<String> userIds) {

        List<User> users = userIds.stream().map(userId -> userService.getUser(userId)).collect(Collectors.toList());

        List<User> userList = boardMap.get(boardId).getUsers();
        if (userList == null)
            userList = new ArrayList<>();

        userList.addAll(users);

        lockService.lockId(boardId);
        boardMap.get(boardId).setUsers(userList);
        lockService.unlockId(boardId);

    }

    public void removeMembers(String boardId, List<String> userIds) {

        List<User> users = userIds.stream().map(userId -> userService.getUser(userId)).collect(Collectors.toList());

        Board board = boardMap.get(boardId);
        List<User> userList = board.getUsers();
        if (userList == null)
            userList = new ArrayList<>();
        userList.removeAll(users);

//      find card which are with this userId and unassign them
        List<Epic> epics = board.getEpics();
        userIds.stream().forEach(userId -> {
            epics.forEach(epic -> {
                epic.getCards().stream().filter(card -> card.getUser()!=null && card.getUser().getUserId().equals(userId)).forEach(card -> {
                    cardService.unassignCard(card.getId());
                });
            });
        });

        lockService.lockId(boardId);
        boardMap.get(boardId).setUsers(userList);
        lockService.unlockId(boardId);

    }
}
