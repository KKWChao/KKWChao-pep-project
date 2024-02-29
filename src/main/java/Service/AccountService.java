package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
    

    /**
     * GET ALL ACCOUNTS
     * @return list of accounts
     */
    public List<Account> getAllAccounts() {
        try {
            return accountDAO.getAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET ACCOUNT BY ID
     * @param account_id
     * @return account 
     */
    public Account getAccountById(int account_id) {
        try {
            return accountDAO.getAccountById(account_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ADDING ACCOUNT
     * @param account
     * @return account
     */
    public Account addAccount(Account account) {
        try {
            return accountDAO.addAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * UPDATING ACCOUNT
     * @param account_id -> account id
     * @param account -> updated account info
     * @return updated account
     */
    public Account updateAccount(int account_id, Account account) {
        try {
            accountDAO.updateAccount(account_id, account);
            return accountDAO.getAccountById(account_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

    /**
     * LOGGING IN
     * @param login_name
     * @param login_password
     * @return account info if successful
     */

     public Account loginAccount(String login_name, String login_password) {
        try {
            return accountDAO.loginAccount(login_name, login_password);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
     }


}
