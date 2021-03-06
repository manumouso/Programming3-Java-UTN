package controller;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.ConfirmBox;


public class AccessController {

    public Button createButton,joinButton,quitButton;
    private Scene createScene,joinScene;

    public void setCreateScene(Scene scene) {
        createScene = scene;
    }

    public void openCreateScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(createScene);
    }

    public void setJoinScene(Scene scene) {
        joinScene = scene;
    }

    public void openJoinScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(joinScene);
    }

    public void quitGame(){
        Boolean answer= ConfirmBox.display("Quit","Are you sure you want to exit?");
        if(answer){
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.close();
        }
    }
}
