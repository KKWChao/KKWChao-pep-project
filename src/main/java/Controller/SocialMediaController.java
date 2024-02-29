package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessagesService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    /* 
     * Need to Create 
     * 1. Path for Registering
     *      POST: "/register" - register url
     * 
     * 2. Paths for Account
     *      POST: "/account" - login url
     * 
     *      GET: "/account/{user_id}/messages" - get all messages for the specific user
     *      
     * 3. Paths for Message
     *      GET: "/messages"
     *      GET: "/messages/{message_id}"
     *      POST: "/messages"
     *      PUT: "/messages/{message_id}"
     *      DELETE: "/messages/{message_id}"
     * 
     * 
     */
    AccountService accountService;
    MessagesService messagesService;

    public SocialMediaController() {
        accountService = new AccountService();
        messagesService = new MessagesService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // register
        app.post("/register", this::postSocialMediaRegisterHandler);

        // login
        app.post("/login", this::postSocialMediaLoginHandler);

        // accounts
        app.post("/account", this::postSocialMediaLoginHandler); // login route
        app.get("/accounts/{account_id}/messages", this::getSocialMediaAccountMessagesHandler); // get all messages from specific account

        // messages
        app.get("/messages", this::getSocialMediaMessagesHandler); 
        app.get("/messages/{message_id}", this::getSocialMediaMessageByIdHandler);
        app.post("/messages", this::postSocialMediaMessageHandler);
        app.patch("/messages/{message_id}", this::patchSocialMediaMessageHandler);
        app.delete("/messages/{message_id}", this::deleteSocialMediaMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // ---------- Account Routes ---------- //

    /**
     * REGISTER USER
     *      logic so that username is not blank and pw > 4 chars 
     * @param context
     * @throws JsonProcessingException
     * @return Fail -> status 400
     * @return Success -> json of account w/ account_id
     */
    private void postSocialMediaRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
            
        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    /**
     * LOGIN HANDLER
     * @param context
     * @throws JsonProcessingException
     * @return Fail -> status 401 Unauthorized
     * @return Success -> json of account in response + account_id
     */
    private void postSocialMediaLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    
        // getting login from post
        Account account = mapper.readValue(context.body(), Account.class);

        String username = account.getUsername();
        String password = account.getPassword();

        Account loginAccount = accountService.loginAccount(username, password);
        

        if (loginAccount != null) {
            context.json(loginAccount);
        } else {
            context.status(401);
        }
    }

    // ---------- Message Routes ---------- //
    
    /**
     * GET ALL MESSAGES
     * @param context
     * @return List of messages
     */
    private void getSocialMediaMessagesHandler(Context context) {
        context.json(messagesService.getAllMessages());

    }

    /**
     * GET MESSAGE BY ID
     * @param context
     * @return empty response
     * @return Success -> message
     */
    private void getSocialMediaMessageByIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messagesService.getMessageById(message_id);
        
        if (message != null) {
            context.json(message);
        } else {
            context.status(200);
        }
    }

    /**
     * RETURN ALL MESSAGES FROM SPECIFIC ACCOUNT
     * @param context
     * @return Fail -> empty body
     * @return Success -> list of messages by specific user id
     */
    private void getSocialMediaAccountMessagesHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        
        context.json(messagesService.getMessagesByUserId(account_id));
    }
    
    /**
     * CREATE MESSAGE
     * @param context
     * @throws JsonProcessingException
     * @return Fail -> status 400
     * @return Success -> updated message
     */
    private void postSocialMediaMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messagesService.createMessage(message);

        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
        } else {
            context.status(400);
        }
    }
    

    /**
     * UPDATE MESSAGE
     * @param context
     * @throws JsonProcessingException
     * @return Fail -> status 400
     * @return Success -> updated message
     */
    private void patchSocialMediaMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message updatedMessage = messagesService.updateMessage(message_id, message);

        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * DELETE MESSAGE
     * @param context
     * @return Success -> return deleted message, if no message, return empty message 
     */
    private void deleteSocialMediaMessageHandler(Context context) {

        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messagesService.deleteMessage(message_id);
        
        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.status(200);
        }

    }


}