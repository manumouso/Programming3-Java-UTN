package view;

import controller.AccessController;
import controller.CreateController;
import controller.JoinController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    Stage window;
    @Override
    public void start(Stage primaryStage) {
        try{
            window=primaryStage;
            // getting loader and a pane for the first scene.
            // loader will then give a possibility to get related controller
            FXMLLoader accessPageLoader = new FXMLLoader(getClass().getResource("Access.fxml"));
            Parent accessPane = accessPageLoader.load();
            Scene accessScene = new Scene(accessPane, 510, 388);

            // getting loader and a pane for the second scene
            FXMLLoader createPageLoader = new FXMLLoader(getClass().getResource("Create.fxml"));
            Parent createPane = createPageLoader.load();
            Scene createScene = new Scene(createPane, 810, 610);

            //getting loader and a pane for the third scene
            FXMLLoader joinPageLoader = new FXMLLoader(getClass().getResource("Join.fxml"));
            Parent joinPane = joinPageLoader.load();
            Scene joinScene= new Scene(joinPane, 883, 665);

            // injecting second scene into the controller of the first scene
            AccessController firstPaneController = (AccessController) accessPageLoader.getController();
            firstPaneController.setCreateScene(createScene);

            //injecting the third scene into the controller of the first scene
            firstPaneController.setJoinScene(joinScene);

            // injecting first scene into the controller of the second scene
            CreateController secondPaneController = (CreateController) createPageLoader.getController();
            secondPaneController.setAccessScene(accessScene);

            //injecting first scene into the controller of the third scene
            JoinController thirdPaneController= (JoinController) joinPageLoader.getController();
            thirdPaneController.setAccessScene(accessScene);

            window.setTitle("Kokumo No Monogatari");
            window.getIcons().add(new Image("resource/image/kokumo.jpg"));
            window.setOnCloseRequest(e->{
                e.consume();
                quitGame();
            });
            accessScene.getStylesheets().add("/resource/style/Ninjax.css");
            window.setScene(accessScene);
            window.setResizable(false);
            window.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void quitGame(){
        Boolean answer= ConfirmBox.display("Quit","Are you sure you want to exit?");
        if(answer){
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
