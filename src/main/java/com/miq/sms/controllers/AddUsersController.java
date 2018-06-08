
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.UsersDao;
import com.miq.sms.models.vo.UsersType;
import com.miq.sms.models.vo.UsersVo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class AddUsersController implements Initializable {

    @FXML
    private JFXTextField txtPassword;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXComboBox<?> comboUserType;
    @FXML
    private JFXTextField txtFullName;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnSave;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onClear(ActionEvent event) {
        clear();
    }

    @FXML
    private void onSave(ActionEvent event) {
        
        try {
            if(txtUsername.getText().trim().isEmpty()||txtPassword.getText().trim().isEmpty()|| txtFullName.getText().trim().isEmpty()||comboUserType.getSelectionModel().getSelectedIndex()<0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }
            UsersVo usersVo = new UsersVo();
            int id = UsersDao.getInstance().getLastUserId()+1;
            if(id<0){
                usersVo.setId(1);
            }else{
                usersVo.setId(id);
            }
            String userName = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String fullName =txtFullName.getText().trim();
            UsersType usersType = UsersType.getUsersTypeById(comboUserType.getSelectionModel().getSelectedIndex() + 1);
            
            
            usersVo.setUserName(userName);
            usersVo.setPassword(password);
            usersVo.setUserFullName(fullName);
            usersVo.setUsersType(usersType);
            int Count = UsersDao.getInstance().insert(usersVo);
            if (Count == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("اضافة");
                    alert.setHeaderText("تمت الاضافة بنجاح");
                    alert.showAndWait();
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                    String getuser = DashboardController.usersVo.getUserName();
                    LoginController lc = new LoginController();
                    lc.iniFile(getuser, "add new user : " + userName);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("خطأ");
                    alert.setHeaderText("لم يتم الاضافة");
                    alert.showAndWait();
            }
           
        } catch (Exception ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }
    private void clear(){
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        comboUserType.getSelectionModel().clearSelection();
    }
}
