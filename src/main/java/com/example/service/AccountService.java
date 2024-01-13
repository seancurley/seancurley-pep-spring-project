package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.entity.Account;
import java.util.List;
import java.util.Optional;

import com.example.exception.*;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository ar)
    {
        this.accountRepository = ar;
    }

    /*
     * Requirements for successful registration:
     * Username does not already exist
     * Username is not blank
     * Password is at least 4 characters long
     */
    public Account registerUser(Account account) throws InvalidRegistrationException, UserAlreadyExistsException
    {
        String username = account.getUsername();
        String password = account.getPassword();
        if(username == "" || password.length() < 4)
        {
            throw new InvalidRegistrationException();
        }
        else if(accountRepository.findByUsername(username).isPresent())
        {
            throw new UserAlreadyExistsException();
        }
        else
        {
            return accountRepository.save(account);
        }
    }

    public Account login(Account account) throws AuthenticationException
    {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
            .orElseThrow(() -> new AuthenticationException());
    }

    public List<Account> getAllAccounts()
    {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Integer id)
    {
        return accountRepository.findById(id);
    }

    public void deleteAccount(Integer id)
    {
        accountRepository.deleteById(id);
    }


}
