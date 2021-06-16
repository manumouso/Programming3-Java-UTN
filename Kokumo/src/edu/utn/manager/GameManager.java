package edu.utn.manager;

import edu.utn.connection.client.Client;
import edu.utn.connection.server.Server;
import edu.utn.controller.BoardController;
import edu.utn.enums.NetworkType;
import edu.utn.factory.ViewFactory;
import edu.utn.factory.NetworkFactory;
import edu.utn.message.Message;
import edu.utn.model.ninja.Ninja;
import edu.utn.model.ninja.NinjaPosition;
import edu.utn.validator.NetworkValidator;
import edu.utn.view.*;
import java.io.IOException;

public class GameManager {

    private boolean host;
    private ServiceManager serviceManager;
    private RuleManager ruleManager;
    private PlayerManager playerManager;
    private ViewFactory viewFactory;
    private BoardController boardController;
    private BoardPrinter boardPrinter;
    private Message message;
    private MessagePrinter messagePrinter;

    public GameManager(ServiceManager serviceManager, RuleManager ruleManager, PlayerManager playerManager) {
        this.serviceManager = serviceManager;
        this.ruleManager = ruleManager;
        this.playerManager = playerManager;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public RuleManager getRuleManager() {
        return ruleManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    private BoardController getBoardController() {
        if(boardController==null){
            boardController = new BoardController();
        }
        return boardController;
    }

    private BoardPrinter getBoardPrinter() {
        if(boardPrinter==null){
            boardPrinter= new BoardPrinter();
        }
        return boardPrinter;
    }

    private synchronized Message getMessage() {
        if(message==null){
            message=new Message();
        }
        return message;
    }
    private synchronized void addAll(){

        getMessage().getMessageList().addAll(getRuleManager().getMessage().getMessageList());
        getRuleManager().getMessage().getMessageList().clear();
    }

    private MessagePrinter getMessagePrinter() {
        if(messagePrinter==null){
            messagePrinter= new MessagePrinter();
        }
        return messagePrinter;
    }

    private ViewFactory getViewFactory() {
        if(viewFactory ==null){
            viewFactory = new ViewFactory();
        }
        return viewFactory;
    }

    public void startGame(){
        ViewFactory viewFactory = getViewFactory();
        Introduction intro = viewFactory.createIntro();
        intro.print();
        PrimaryStage primaryStage = viewFactory.createPrimaryStage();
        primaryStage.menu(this);
    }

    public void toServerRoom(){
        ServerRoom serverRoom = viewFactory.createServerRoom();
        serverRoom.menu(this);
    }
    public void toPlayerRoom(){
        PlayerRoom playerRoom= viewFactory.createPlayerRoom();
        playerRoom.menu(this);
    }
    public void toGameRoom(){
        GameRoom gameRoom = viewFactory.createGameRoom();
        gameRoom.menu(this);
    }
    public void setServer(int port){
        getServiceManager().setServer(createServer(port));
    }

    public Server createServer(int port){
        NetworkFactory networkFactory = getServiceManager().getNetworkFactory();

            if(validPort(port)){
                Server server =networkFactory.createServer(port);
                getMessage().getMessageList().add(NetworkType.SERVER.getMessage());
                return server;
            }

        return null;
    }

    public void setClient(){
        getServiceManager().setClient(createClient());
    }

    public Client createClient(){
        NetworkFactory networkFactory = getServiceManager().getNetworkFactory();
        Client client =networkFactory.createClient();
        getMessage().getMessageList().add(NetworkType.CLIENT.getMessage());
        return client;
    }

    public boolean sendJoin(String IP,int port){
        if(validIP(IP)){
            if(validPort(port)){
                int myPort= getServiceManager().getServer().getPort();
                return getServiceManager().joinGame(IP,port,"{\"port\":"+myPort+"}");
            }
        }
        return false;
    }
    public void sendAttack(NinjaPosition attackPosition, int attackPoints){
        String json="{\"position\":["+attackPosition.getI()+","+attackPosition.getJ()+"],\"attackPoints\":"+attackPoints+"}";
        getServiceManager().attack(attackPosition,json);
    }

    public void sendEndTurn(){
        getServiceManager().endTurn();
    }

    public void startConnection() throws IOException {
        getServiceManager().getServer().startConnection(getServiceManager(),getRuleManager(),getPlayerManager());
    }

    public void closeConnection() throws IOException {
        getServiceManager().getServer().closeConnection();
    }

    public void setServerState(){
        getServiceManager().setServerState(createServerState());
    }
    public ServerState createServerState(){
        return getServiceManager().getServerState();
    }
    public boolean serverWasCreated() {

        return getServiceManager().getServerState().serverWasCreated();
    }
    public void setServerWasCreated(boolean serverWasCreated) {

        getServiceManager().getServerState().setServerWasCreated(serverWasCreated);
    }

    public boolean isRunning() {
        return getServiceManager().getServerState().isRunning();
    }
    public void setRunning(boolean running) {
        getServiceManager().getServerState().setRunning(running);
    }
    public boolean connectedClient() {
        return getServiceManager().getServerState().connectedClient();
    }
    public void setConnectedClient(boolean connectedClient) {
        getServiceManager().getServerState().setConnectedClient(connectedClient);
    }

    public synchronized void setExternalMessage(boolean action){

        getServiceManager().setExternalMessage(action);
    }
    public synchronized boolean externalMessageReceived(){

        return getServiceManager().externalMessageArrived();
    }

    public Server getServer(){

        return getServiceManager().getServer();
    }

    public void printBoard(boolean playersBoard){
        BoardPrinter boardPrinter=getBoardPrinter();
        if(playersBoard){
            boardPrinter.printBoard(playerManager.getPlayer());
        }else{
            boardPrinter.printAttackBoard();
        }
    }
    public void clearBoards(boolean ninjasBoard){
        BoardController boardController = getBoardController();
        boardController.clearBoards(ninjasBoard);
    }
    public void printMessages(){
        MessagePrinter messagePrinter=getMessagePrinter();
        addAll();
        messagePrinter.printMessages(getMessage().getMessageList());
    }

    public void clearMessages(){
        MessagePrinter messagePrinter = getMessagePrinter();
        messagePrinter.clearMessages(getMessage().getMessageList());
    }

    private boolean validIP(String IP){
        if(NetworkValidator.validIP(IP)){
            return true;
        }
        getMessage().getMessageList().add(NetworkType.IP.getMessage());
        return false;
    }

    private boolean validPort(int port){
        if(NetworkValidator.validPort(port)){
            return true;
        }
        getMessage().getMessageList().add(NetworkType.PORT.getMessage());
        return false;
    }

    public boolean canMove(Ninja ninja){
        return ninja.getMovementCounter() == 0;
    }

    public synchronized void checkReceivedMessages(){
        if(getRuleManager().getMessageArrived()>0) {
            printMessages();
            clearMessages();
            getRuleManager().setMessageArrived(0);
        }
    }



}
