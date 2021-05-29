package edu.utn.entity;


import edu.utn.entity.square.Empty;
import edu.utn.entity.square.Square;

import java.util.ArrayList;
import java.util.List;

//SINGLETON
public class AttackBoard implements Size {

    private static AttackBoard attackBoardInstance;
    private Square[][] squares;
    private List<String> messages;

    public Square[][] getSquares() {
        return squares;
    }

    public List<String> getMessages() {
        if(messages== null){
            messages=new ArrayList<>();
        }
        return messages;
    }

    private AttackBoard(){
        squares=new Square[MAX_ROW][MAX_COLUMN];
        generateBoard();

    }

    public static AttackBoard getInstance(){
        if(attackBoardInstance==null){
            attackBoardInstance = new AttackBoard();
        }
        return attackBoardInstance;
    }

    private void generateBoard(){

        for(int i=0;i<MAX_ROW;i++){
            for(int j=0;j<MAX_COLUMN;j++){
                squares[i][j]=new Empty();
            }
        }

    }


}
