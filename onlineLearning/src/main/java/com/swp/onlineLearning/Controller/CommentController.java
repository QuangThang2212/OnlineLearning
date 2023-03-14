package com.swp.onlineLearning.Controller;


import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class CommentController {

    @OnMessage
    public void onOpen(String message, Session session) {
        System.out.println(message);
        System.out.println("WebSocket connection opened");
    }

}
