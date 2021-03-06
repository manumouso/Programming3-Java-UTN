package edu.utn.connection.server;

import com.sun.net.httpserver.HttpServer;
import edu.utn.connection.server.requestHandler.*;
import edu.utn.manager.PlayerManager;
import edu.utn.manager.RuleManager;
import edu.utn.manager.ServiceManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {

    private String IP;
    private int port;
    private HttpServer server;

    public Server(int port) {
        this.port = port;
    }

    private HttpServer getServer() throws IOException {
        if(server==null){
            server=HttpServer.create(new InetSocketAddress(port),0);
        }
        return server;
    }

    public String getIP() {
        return IP;
    }
    public int getPort() {
        return port;
    }

    public void startConnection(ServiceManager serviceManager, RuleManager ruleManager, PlayerManager playerManager) throws IOException {
        try {
            getServer().createContext("/attack",new Attack(ruleManager,playerManager));
            getServer().createContext("/canAttack",new CanAttack(ruleManager));
            getServer().createContext("/canMove",new CanMove(ruleManager));
            getServer().createContext("/deadByTrap",new DeadByTrap(serviceManager,ruleManager));
            getServer().createContext("/invite",new Invite(serviceManager));
            getServer().createContext("/join",new Join(serviceManager));
            getServer().createContext("/serverAttack",new ServerAttack(ruleManager,playerManager));
            getServer().createContext("/validDirection",new ValidDirection(ruleManager));
            getServer().createContext("/yourTurn",new YourTurn(playerManager));
            getServer().setExecutor(null);
            getServer().start();

        }catch (IOException e){
            System.out.println("\t\t\tIO Exception: "+e.getMessage());
        }catch (Exception e){
            System.out.println("\t\t\tException: "+e.getMessage());
        }
    }
    public void closeConnection() throws IOException {
        if(getServer()!=null){
            getServer().stop(0);
        }
    }

}
