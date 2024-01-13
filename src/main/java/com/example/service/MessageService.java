package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.entity.Message;
import java.util.List;
import java.util.Optional;

import com.example.exception.InvalidMessageLengthException;
import com.example.exception.InvalidUserException;
import com.example.exception.MessageNotFoundException;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    private AccountService accountService;
    
    public MessageService(MessageRepository mr, AccountService accountService)
    {
        this.messageRepository = mr;
        this.accountService = accountService;
    }

    public Message addMessage(Message toAdd) throws InvalidUserException, InvalidMessageLengthException
    {
        Integer userID = toAdd.getPosted_by();
        if(accountService.getAccountById(userID).isEmpty())
        {
            throw new InvalidUserException();
        }
        String messageText = toAdd.getMessage_text();
        if(messageText.length() == 0 || messageText.length() > 255)
        {
            throw new InvalidMessageLengthException();
        }
        return messageRepository.save(toAdd);
    }

    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id)
    {
        return messageRepository.findById(id);     
    }

    public int deleteMessageById(Integer id)
    {
        //Make sure there's something to delete before trying to delete it!
        boolean exists = messageRepository.existsById(id);
        if(exists)
        {
            messageRepository.deleteById(id);
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public void updateMessageById(Integer id, String messageText) throws MessageNotFoundException, InvalidMessageLengthException
    {
        if(messageText.length() == 0 || messageText.length() > 255)
        {
            throw new InvalidMessageLengthException();
        }
        Message toUpdate = messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException());
        toUpdate.setMessage_text(messageText);
        messageRepository.save(toUpdate);
    }

    public List<Message> getMessagesByUser(Integer userID)
    {
        return messageRepository.findByPostedBy(userID);
    }
}
