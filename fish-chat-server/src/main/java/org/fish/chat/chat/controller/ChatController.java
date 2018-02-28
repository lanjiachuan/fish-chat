package org.fish.chat.chat.controller;

import org.fish.chat.chat.service.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author adre
 * @version 创建时间：2018/2/28 下午4:17
 */
@RestController
public class ChatController {

    @Autowired
    private UserChatService userChatService;
}
