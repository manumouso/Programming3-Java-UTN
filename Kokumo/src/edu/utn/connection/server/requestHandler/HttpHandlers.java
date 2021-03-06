package edu.utn.connection.server.requestHandler;

import com.sun.net.httpserver.HttpHandler;
import edu.utn.manager.PlayerManager;
import edu.utn.manager.RuleManager;
import edu.utn.manager.ServiceManager;

public abstract class HttpHandlers implements HttpHandler {

    protected ServiceManager serviceManager;
    protected PlayerManager playerManager;
    protected RuleManager ruleManager;

    public HttpHandlers() {
    }

    public HttpHandlers(RuleManager ruleManager, PlayerManager playerManager) {
        this.ruleManager = ruleManager;
        this.playerManager=playerManager;
    }

    public HttpHandlers(ServiceManager serviceManager, RuleManager ruleManager) {
        this.serviceManager = serviceManager;
        this.ruleManager = ruleManager;
    }

    public HttpHandlers(RuleManager ruleManager) {
        this.ruleManager = ruleManager;
    }

    public HttpHandlers(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public HttpHandlers(PlayerManager playerManager){
        this.playerManager=playerManager;
    }

}
