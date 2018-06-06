
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.miq.sms.models.dao.UsersDao;
import com.miq.sms.models.vo.UsersVo;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author danml
 */
public class LoginController implements Initializable {

    private Label label;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView imgProgress;

    UsersVo usersVo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleValidation();
        imgProgress.setVisible(false);
    }

    @FXML
    private void login(ActionEvent event) {

        imgProgress.setVisible(true);
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(3));
        pauseTransition.setOnFinished(ev -> {
            completeLogin();

        });
        pauseTransition.play();
    }

    private void handleValidation() {
        usersVo = new UsersVo();
        RequiredFieldValidator fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage(" يجب ادخال الاسم");
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtUsername.getValidators().add(fieldValidator);
        txtUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            if (!newVal) {
                txtUsername.validate();
                usersVo.setUserName(txtUsername.getText().trim());
            }
        });
        RequiredFieldValidator fieldValidator2 = new RequiredFieldValidator();
        fieldValidator2.setMessage("يجب ادخال كلمة المرور");
        fieldValidator2.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtPassword.getValidators().add(fieldValidator2);
        txtPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtPassword.validate();
                usersVo.setPassword(txtPassword.getText().trim());
            }
        });

    }

    private void completeLogin() {
        try {
            UsersVo uv = UsersDao.getInstance().getData(usersVo);
            if (uv == null) {
                Alert Deletalert = new Alert(Alert.AlertType.ERROR);
               Deletalert.setTitle("خطأ");
               Deletalert.setHeaderText("الاسم او كلمة المرور خطأ");
               Deletalert.setContentText("يجب ادخال الاسم وكلمة المرور بشكل صحيح");
               Deletalert.showAndWait();
                imgProgress.setVisible(false);
//                System.err.println("enter valid user name and password");
            } else {
                DashboardController dashboardController =new DashboardController();
                dashboardController.usersVo=uv;
        btnLogin.getScene().getWindow().hide();
        
            imgProgress.setVisible(false);
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("نظام ادارة سوبر ماركت");
            dashboardStage.getIcons().add(new Image("/icons/sms.png"));
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();
            }
        }catch (IOException ex) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        

    }   catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

}
