
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
public class EditUsersController implements Initializable {

    @FXML
    private JFXTextField txtPassword;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXComboBox comboUserType;
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
    UsersVo usersVo = null;

    public void initUser(UsersVo uv) {
        this.usersVo=uv;
        txtUsername.setText(this.usersVo.getUserName());
        txtPassword.setText(this.usersVo.getPassword());
        txtFullName.setText(this.usersVo.getUserFullName());
        comboUserType.setValue(this.usersVo.getUsersType().getType());
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
            int id = this.usersVo.getId();
            String userName = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String fullName =txtFullName.getText().trim();
            UsersType usersType = UsersType.getUsersTypeById(comboUserType.getSelectionModel().getSelectedIndex() + 1);
            UsersVo uv = new UsersVo();
            uv.setId(id);
            uv.setUserName(userName);
            uv.setPassword(password);
            uv.setUserFullName(fullName);
            uv.setUsersType(usersType);
            int count = UsersDao.getInstance().update(uv);
            if (count == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("خطأ");
                alert.setHeaderText("تم التعديل بنجاح");
                alert.showAndWait();
                Stage stage = (Stage) btnSave.getScene().getWindow();
                stage.close();
                // instance of user
                String getuser = DashboardController.usersVo.getUserName();
                LoginController lc = new LoginController();
                lc.iniFile(getuser, "edit user : " + uv.getUserName());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("لم يتم التعديل  ");
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
