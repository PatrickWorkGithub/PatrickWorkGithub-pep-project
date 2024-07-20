package Service;

import Model.Account;
import Model.Message;

import java.util.List;

import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account registerAccount(Account account){
        Account registered = accountDAO.registerAccount(account);
        return registered;
    }
    public Account loginAccount(Account account){
        return accountDAO.loginAccount(account);

    }

}
