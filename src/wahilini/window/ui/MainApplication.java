package wahilini.window.ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    public static Stage stages;

    @Override
    public void start(Stage primaryStage) {
        stages = primaryStage;
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println("MainWindow Error \n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
