package DAO;

import Model.Account;

public interface AccountDAO {
    Account registerAccount(Account account);
    Account loginAccount(String username, String password);
    boolean userExistsById(int userId);
}