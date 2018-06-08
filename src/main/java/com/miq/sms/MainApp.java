package com.miq.sms;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TestView.fxml"));
        
        Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.getIcons().add(new Image("/icons/sms.png"));
        stage.setTitle("تسجيل الدخول");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
