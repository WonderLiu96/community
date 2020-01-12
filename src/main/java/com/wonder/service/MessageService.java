package com.wonder.service;

import com.wonder.dao.MessageDAO;
import com.wonder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/11
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getConversationCount(int userId,String conversationId){
        return messageDAO.getConversationCount(userId,conversationId);
    }
}
