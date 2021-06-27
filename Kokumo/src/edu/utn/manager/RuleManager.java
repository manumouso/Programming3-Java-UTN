package edu.utn.manager;

import edu.utn.connection.client.Client;
import edu.utn.controller.AttackController;
import edu.utn.controller.MovementController;
import edu.utn.controller.NinjaController;
import edu.utn.enums.MessageType;
import edu.utn.factory.NinjaFactory;
import edu.utn.message.Message;
import edu.utn.model.AttackBoard;
import edu.utn.model.Player;
import edu.utn.model.ninja.Direction;
import edu.utn.model.ninja.Ninja;
import edu.utn.model.ninja.NinjaPosition;
import edu.utn.validator.RuleValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleManager {

    private NinjaFactory ninjaFactory;
    private MovementController movementController;
    private AttackController attackController;
    private NinjaController ninjaController;
    private Map<String, Direction> directionsMap;
    private Message message;
    public int messageArrived;
    private List<NinjaPosition> attackPositions;


    public NinjaFactory getNinjaFactory() {
        if(ninjaFactory==null){
            ninjaFactory = new NinjaFactory();
        }
        return ninjaFactory;
    }
    private MovementController getMovementController() {
        if(movementController==null){
            movementController=new MovementController();
        }
        return movementController;
    }
    public synchronized AttackController getAttackController() {
        if(attackController==null){
            attackController=new AttackController();
        }
        return attackController;
    }
    private NinjaController getNinjaController() {
        if(ninjaController==null){
            ninjaController= new NinjaController();
        }
        return ninjaController;
    }
    public Map<String, Direction> getDirectionsMap() {
        if(directionsMap== null){
            directionsMap=getNinjaFactory().createDirectionMap();
        }
        return directionsMap;
    }
    public synchronized Message getMessage() {
        if(message==null){
            message=new Message();
        }
        return message;
    }
    private synchronized void addAll(List<String> messages){

        getMessage().getMessageList().addAll(messages);
    }
    public synchronized int getMessageArrived() {
        return messageArrived;
    }
    public synchronized void setMessageArrived(int messageArrived) {
        this.messageArrived = messageArrived;
    }

    public List<NinjaPosition> getAttackPositions() {
        if(attackPositions==null){
            attackPositions=new ArrayList<>();
        }
        return attackPositions;
    }

    public void resetAttackPositions(){
        getAttackPositions().clear();
    }

    public Ninja createNinja(int i, int j, boolean commander, int m){
        if(withinLimits(i,j,true)){
            if(freeSquare(i,j)){
                NinjaFactory ninjaFactory = getNinjaFactory();
                Ninja ninja =ninjaFactory.createNinja(i,j,commander,m);
                MovementController movementController = getMovementController();
                movementController.ninjaStandsOn(ninja);
                addAll(movementController.getStandOnMessages());
                movementController.getStandOnMessages().clear();
                return ninja;
            }
        }

        return null;
    }
    public NinjaPosition createPosition(int i,int j){
        NinjaFactory ninjaFactory=getNinjaFactory();
        return ninjaFactory.createPosition(i,j);
    }

    public boolean move(Ninja ninja, Direction direction,ServiceManager manager){

            MovementController movementController = getMovementController();
            NinjaController ninjaController = getNinjaController();
            ninjaController.setDirection(ninja,direction);
            NinjaPosition current = ninjaController.getCurrentPosition(ninja);
            NinjaPosition next = movementController.getNextPosition(ninja);

            if(withinLimits(next.getI(), next.getJ())){
                if (squarePassable(next.getI(), next.getJ())){
                    if (freeSquare(next.getI(), next.getJ())){

                        movementController.move(ninja, current, next,manager);
                        addAll(movementController.getStandOnMessages());
                        movementController.getStandOnMessages().clear();
                        return true;
                    }
                }
            }
        return false;
    }

    public boolean commanderAlive(Player player){
        if(!RuleValidator.commanderDead(player)){
            return true;
        }
        getMessage().getMessageList().add(MessageType.DEADCOMMANDER.getMessage());
        return false;
    }
    private boolean withinLimits(int i,int j,boolean create){
        if(RuleValidator.withinLimitsBoard(i,j)){
            return true;
        }
        getMessage().getMessageList().add(MessageType.CREATE.getMessage());
        return false;
    }
    private boolean withinLimits(int i,int j){
        if(RuleValidator.withinLimitsBoard(i,j)){
            return true;
        }
        getMessage().getMessageList().add(MessageType.OUTBOUNDARY.getMessage());
        return false;
    }
    private boolean freeSquare(int i,int j){
        if(!RuleValidator.squareOccupied(i,j)){
            return true;
        }
        getMessage().getMessageList().add(MessageType.OCCUPIED.getMessage());
        return false;
    }
    public boolean isAlive(Ninja ninja){
        if(!ninja.isDead()){
            return true;
        }
        getMessage().getMessageList().add(MessageType.DEAD.getMessage());
        return false;
    }
    public boolean movementAllowed(Ninja ninja){
        if(ninja.isMovedPreviousTurn()){
            getMessage().getMessageList().add(MessageType.CONSECUTIVE.getMessage());
            return false;
        }
        return true;
    }
    private boolean squarePassable(int i, int j){
        if(!RuleValidator.squareDestroyed(i, j)){
            return true;
        }
        getMessage().getMessageList().add(MessageType.DESTROYED.getMessage());
        return false;
    }
    public boolean requiredNinjasQuantity(Player player){
        if(RuleValidator.requiredNinjasQuantity(player)){
            return true;
        }
        return false;
    }
    public boolean lessThanRequiredNinjasQuantity(Player player){
        if(RuleValidator.lessThanRequiredNinjasQuantity(player)){
            return true;
        }
        return false;
    }
    public boolean canMoveThisTurn(Ninja ninja){

        return RuleValidator.canMoveThisTurn(ninja);
    }

    public synchronized String attackReceived(Player player, NinjaPosition attackPosition,int attackPoints){
        String message;
        if(attackAllowed(player,attackPosition)){
            message = getAttackController().attackReceived(player, attackPosition,attackPoints);

            addAll(getAttackController().getAttackMessages());
            getAttackController().getAttackMessages().clear();
            setMessageArrived(getMessageArrived()+1);

        }else{
            message="WRONG ATTACK!!! Try again";
        }

        return message;
    }

    private boolean attackAllowed(Player player,NinjaPosition attackPosition){
        int i= attackPosition.getI();
        int j= attackPosition.getJ();
        if(RuleValidator.withinLimitsBoard(i,j)){
            if(!RuleValidator.squareDestroyed(i,j)){
                return !RuleValidator.ninjaDead(player, i, j);
            }
        }
        return false;
    }

    public synchronized void ninjaDiedByTrap(){
        getMessage().getMessageList().add("Enemy Ninja died standing on a trap");
        setMessageArrived(getMessageArrived()+1);
    }

    public boolean choseRepeatedPosition(NinjaPosition attack){
        for(NinjaPosition position:getAttackPositions()){
            if(attack.getI()== position.getI() && attack.getJ()== position.getJ()){
                return true;
            }
        }
        return false;
    }

    public synchronized String canMoveClient(boolean commanderDead,int attackCounter,int moveCounter, boolean movedPreviousTurn,boolean ninjaDead){
        if(!RuleValidator.ninjaDead(commanderDead)){
            if(!RuleValidator.ninjaDead(ninjaDead)){
                if(RuleValidator.canMoveThisTurn(attackCounter,moveCounter)){
                    if(!RuleValidator.movedPreviousTurn(movedPreviousTurn)){
                        return MessageType.ALLOWED.getMessage();
                    }else{
                        return MessageType.CONSECUTIVE.getMessage();
                    }
                }else{
                    return MessageType.ALREADY.getMessage();
                }
            }else{
                return MessageType.DEAD.getMessage();
            }
        }else{
            return MessageType.DEADCOMMANDER.getMessage();
        }
    }

    public synchronized String canAttackClient(int attackCounter, int moveCounter, boolean ninjaDead) {
        if (!RuleValidator.ninjaDead(ninjaDead)) {
            if (RuleValidator.canMoveThisTurn(attackCounter, moveCounter)) {
                return MessageType.ATTACK_ALLOWED.getMessage();
            } else {
                return MessageType.ALREADY.getMessage();
            }
        }else{
            return MessageType.DEAD.getMessage();
        }
    }


    public synchronized String validDirectionClient(int nextI,int nextJ,NinjaPosition pos1, NinjaPosition pos2,NinjaPosition pos3){
        if(RuleValidator.withinLimitsBoard(nextI,nextJ)){
            if(!RuleValidator.squareDestroyed(AttackBoard.getInstance().getSquares()[nextI][nextJ].name())){
                NinjaPosition next = new NinjaPosition(nextI,nextJ);
                if(!RuleValidator.squareOccupied(next,pos1,pos2,pos3)){
                    return MessageType.VALID_DIRECTION.getMessage();
                }else{
                    return MessageType.OCCUPIED.getMessage();
                }
            }else {
                return MessageType.DESTROYED.getMessage();
            }
        }else{
            return MessageType.OUTBOUNDARY.getMessage();
        }

    }

    public synchronized void moveClient(Ninja ninja, Direction direction, ServiceManager manager) {
        MovementController movementController = getMovementController();
        NinjaController ninjaController = getNinjaController();
        ninjaController.setDirection(ninja, direction);
        NinjaPosition current = ninjaController.getCurrentPosition(ninja);
        NinjaPosition next = movementController.getNextPosition(ninja);
        movementController.move(ninja, current, next, manager);
        addAll(movementController.getStandOnMessages());
        movementController.getStandOnMessages().clear();
    }
}
