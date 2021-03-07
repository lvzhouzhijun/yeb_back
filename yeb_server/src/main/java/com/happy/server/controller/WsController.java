package com.happy.server.controller;

import com.happy.server.pojo.Admin;
import com.happy.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/07/13:55
 * @Description:
 */
@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        Admin admin = (Admin) authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFormNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        // 发送消息
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),
                "/queue/chat",chatMsg);
    }

}
