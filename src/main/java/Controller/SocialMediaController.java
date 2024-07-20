package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler); //post register
        app.post("/login", this::postLoginHandler); //post login
        app.post("/messages", this::postMessagesHandler); //post messages
        app.get("/messages", this::getAllMessagesHandler); //get messages
        app.get("/messages/{message_id}", this::getMessageByIdHandler); //get messages/{message_id}
        app.delete("/messages/{message_id}", this::deleteMessageHandler); //delete messages/{message_id}
        app.patch("/messages/{message_id}", this::patchMessageHandler); //patch messages/{message_id}
        app.get("/accounts/{account_id}/messages", this::getAccountMessagesHandler); //get accounts/{account_id}/messages
        //app.start(8080);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        if(registeredAccount==null){
            context.status(400);
        }else{
            context.status(200);
            context.json(mapper.writeValueAsString(registeredAccount));
        }
    }
    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedIn = accountService.loginAccount(account);
        if(loggedIn==null){
            context.status(401);
        }else{
            context.status(200);
            context.json(mapper.writeValueAsString(loggedIn));
        }
    }
    private void postMessagesHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(),Message.class);
        Message postedMessage = messageService.postMessage(message);
        if(postedMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(postedMessage));
        }
    }
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }
    private void getMessageByIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message == null){
            context.json("");
        }
        else{
            context.json(message);
        }
    }
    private void deleteMessageHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        if(deletedMessage == null){
            context.json("");
        }
        else{
            context.json(deletedMessage);
        }
        
    }
    private void patchMessageHandler(Context context) throws JsonProcessingException{
        //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        ObjectMapper mapper = new ObjectMapper();
        //String messageText = context.pathParam("message_text");
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        String messageText = context.body();
        int length = messageText.length()-3;
        messageText = messageText.substring(18, length);
        //String messageText = context.attribute("message_text");
        System.err.print("***MessageText:");
        System.err.println(messageText);
        //String messageText = context.req().getParameter("message_text");
        //String messageText = context.formParams("message_text").get(0);
        //String messageText = context.formParam("message_text");
        Message patchedMessage = messageService.patchMessage(message_id,messageText);
        if(patchedMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(patchedMessage));
        }
    }
    private void getAccountMessagesHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAccountMessages(account_id);
        context.json(messages);
    }


}