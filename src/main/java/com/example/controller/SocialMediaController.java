package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.service.*;
import java.util.List;
import com.example.entity.*;
import com.example.exception.AuthenticationException;
import com.example.exception.InvalidMessageLengthException;
import com.example.exception.InvalidRegistrationException;
import com.example.exception.InvalidUserException;
import com.example.exception.MessageNotFoundException;
import com.example.exception.UserAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accServ, MessageService messServ)
    {
        this.accountService = accServ;
        this.messageService = messServ;
    }

    @PostMapping(path = "/register")
    public @ResponseBody ResponseEntity<Account> register(@RequestBody Account account)
    {
        Account toRet = accountService.registerUser(account);
        return ResponseEntity.status(HttpStatus.OK).body(toRet);
    }

    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<Account> login(@RequestBody Account account)
    {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.login(account));
    }

    @PostMapping(path = "/messages")
    public @ResponseBody ResponseEntity<Message> addMessage(@RequestBody Message message)
    {
        Message toRet = messageService.addMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(toRet);
    }

    @GetMapping(path = "/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages()
    {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
    }

    @GetMapping(path = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable int message_id)
    {
        Optional<Message> toRet = messageService.getMessageById(message_id);
        if(toRet.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(toRet.get());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    } 

    @DeleteMapping(path = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id)
    {
        if(messageService.deleteMessageById(message_id) == 1)
        {
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

    }

    @PatchMapping(path = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> updateMessageById(@PathVariable int message_id, @RequestBody Message newMessage)
    {
        String message_text = newMessage.getMessage_text();
        messageService.updateMessageById(message_id, message_text);
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    @GetMapping(path = "/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer account_id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessagesByUser(account_id));
    }

    @ExceptionHandler(InvalidRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleInvalidRegistration(InvalidRegistrationException e)
    {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidMessageLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleInvalidMessageLength(InvalidMessageLengthException e)
    {
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody String handleUserAlreadyExists(UserAlreadyExistsException e)
    {
        return e.getMessage();
    }
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleMessageNotFound(MessageNotFoundException e)
    {
        return e.getMessage();
    }
    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleInvalidUser(InvalidUserException e)
    {
        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleInvalidAuthentication(AuthenticationException e)
    {
        return e.getMessage();
    }
}
