package edu.utn.connection.server.requestHandler;

import com.sun.net.httpserver.HttpExchange;
import edu.utn.json.Constants;
import edu.utn.json.JsonController;
import edu.utn.manager.RuleManager;
import edu.utn.manager.ServiceManager;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeadByTrap extends HttpHandlers{
    public DeadByTrap(ServiceManager serviceManager, RuleManager ruleManager) {
        super(serviceManager,ruleManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase(Constants.POST)) {


            serviceManager.setKilledNinjasCounter(serviceManager.getKilledNinjasCounter()+1);
            ruleManager.ninjaDiedByTrap();
            JsonObject res;

            try {
                res= Json.createObjectBuilder()
                        .add("message","Ninja died standing on a trap informed to the other player")
                        .build();

            }
            catch (Exception ex) {
                res = Json.createObjectBuilder()
                        .add("message", ex.getMessage())
                        .build();
            }

            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            exchange.sendResponseHeaders(200, 0);

            try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
                byte [] data = res.toString().getBytes();
                try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
                    byte [] buffer = new byte [data.length];
                    int count ;
                    while ((count = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                }
            }

        } else {
            new JsonController.NotImplementedHandler().handle(exchange);
        }

    }
}
