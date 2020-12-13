package jyu.ties4560.demo3_4_recipedb.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import jyu.ties4560.demo3_4_recipedb.domain.Account;
import jyu.ties4560.demo3_4_recipedb.persistence.Dao;
import jyu.ties4560.demo3_4_recipedb.persistence.GenericDao;
import jyu.ties4560.demo3_4_recipedb.persistence.SimplePersistenceProvider;

public class AccountService {
    private Dao<Long, Account> accountDao;

    public AccountService() {
        System.out.println("Creating AccountService...");
        SimplePersistenceProvider simplePersistenceProvider = SimplePersistenceProvider.getInstance();
        System.out.println("AccountService: Got an instance of SimplePersistenceProvider");
        Map<Long, Account> accountDatabase = simplePersistenceProvider.getAccountDatabase();
        System.out.println("AccountService: Got accountDatabase");
        this.accountDao = new GenericDao<>(accountDatabase);
        System.out.println("AccountService: Created accountDao");
        System.out.println("AccountService created!");
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAll();
    }

    public Account getOne(Long key) {
        Account account = accountDao.getByKey(key);
        
        if (account == null) {
            throw new NotFoundException("Account not found!");
        }
        
        return account;
    }
    
    public Account getByName(String username) {
        Account account = accountDao.getByName(username);
        
        if (account == null) {
            throw new NotFoundException("Account not found!");
        }
        
        return account;
    }

    public Account add(Account account) {
        return accountDao.add(account);
    }

    public Account update(Account account) {
        Account accountToUpdate = accountDao.getByKey(account.getId());
        
        if (accountToUpdate == null) {
            throw new NotFoundException("Account not found!");
        }
        
        return accountDao.update(account);
    }

    public Account delete(Long key) {
        Account account = accountDao.getByKey(key);
        
        if (account == null) {
            throw new NotFoundException("Account not found!");
        }
        
        return accountDao.delete(key);
    }
}


