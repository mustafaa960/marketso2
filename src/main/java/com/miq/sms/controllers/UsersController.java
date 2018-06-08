
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.UsersDao;
import com.miq.sms.models.vo.UsersVo;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class UsersController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private TableView<UsersVo> TableUsers;
    @FXML
    private TableColumn<UsersVo, Integer> colID;
    @FXML
    private TableColumn<UsersVo, String> colUsername;
    @FXML
    private TableColumn<UsersVo, String> colPassword;
    @FXML
    private TableColumn<UsersVo, String> colUserType;
    @FXML
    private TableColumn<UsersVo, String> colFullName;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnDelete;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       LoadALL();
       textSearch();
    }    
     //vo داله تقوم بملىء التيبل من اليوزر
    public void LoadALL() {
        try {

            ObservableList<UsersVo> users = UsersDao.getInstance().loadAll();
            colID.setCellValueFactory(c -> c.getValue().IdProperty().asObject());
            colUsername.setCellValueFactory(c -> c.getValue().UserNameProperty());
            colPassword.setCellValueFactory(c -> c.getValue().PasswordProperty());
            colFullName.setCellValueFactory(c -> c.getValue().UserFullNameProperty());
            colUserType.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getUsersType().getType()));
            TableUsers.setItems(users);
            TableUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
        } catch (Exception ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }
    public void textSearch() {
        try {
             LoadALL();
            ObservableList data = TableUsers.getItems();
            txtSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                     TableUsers.setItems(data);
                     TableUsers.getSelectionModel().clearSelection();
                }
                String value = newValue.toLowerCase().trim();
                ObservableList<UsersVo> subentries = FXCollections.observableArrayList();

                long count = TableUsers.getColumns().stream().count();
                for (int i = 0; i < TableUsers.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String userName = "" + TableUsers.getColumns().get(1).getCellData(i);
                       
                         if (userName.toLowerCase().startsWith(value)) {
                            subentries.add(TableUsers.getItems().get(i));
                            break;
                        } 
                    }
                }
                TableUsers.setItems(subentries);
            });
        } catch (Exception ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }
    @FXML
    private void onEdit(ActionEvent event) {
        try {
            UsersVo usersVo = new UsersVo();
            usersVo = TableUsers.getSelectionModel().getSelectedItem();
            if (usersVo != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditUsersView.fxml"));
                Parent root = loader.load();
                EditUsersController controller = loader.<EditUsersController>getController();
                controller.initUser(usersVo);
                Stage primaryStage = (Stage) btnEdit.getScene().getWindow();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("اضافة مستخدم");
  //            stage.getIcons().add(new Image("/icons/BodyBuilding.png"));
                stage.setResizable(false);
                stage.setFullScreen(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(scene);
                int index = TableUsers.getSelectionModel().getSelectedIndex();
                stage.show();
                stage.setOnHidden((e) -> {
                    LoadALL();
                    textSearch();
                    TableUsers.requestFocus();
                    TableUsers.getSelectionModel().clearAndSelect(index);
                    TableUsers.getFocusModel().focusRightCell();
                    TableUsers.scrollTo(TableUsers.getSelectionModel().getFocusedIndex());
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("تحذير");
            alert.setHeaderText("لم يتم تحديد اي عنصر");
            alert.setContentText("يرجى اختيار عنصر لتعديله ");
            alert.showAndWait();
            }
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            Stage Primarystage = (Stage) btnAdd.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddUsersView.fxml"));
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("اضافة مستخدم");
//            stage.getIcons().add(new Image("/icons/BodyBuilding.png"));
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Primarystage);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
            stage.setOnHidden((WindowEvent we) -> {
               
                LoadALL();
                textSearch();
                TableUsers.requestFocus();
                TableUsers.getSelectionModel().selectLast();
                TableUsers.scrollTo(TableUsers.getSelectionModel().getFocusedIndex());
                
            });
        } catch (IOException ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (TableUsers.getSelectionModel().getSelectedItem() != null) {
            Alert Deletalert = new Alert(Alert.AlertType.CONFIRMATION);

            Deletalert.setTitle("حذف");
            Deletalert.setHeaderText("هل انت متأكد من الحذف؟");
            Optional<ButtonType> result = Deletalert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    UsersVo uv = TableUsers.getSelectionModel().getSelectedItem();
                    int count =UsersDao.getInstance().delete(uv);
                    if(count ==1){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("الحذف");
                        alert.setHeaderText("تم الحذف بنجاح");
                        alert.showAndWait();
                        textSearch();
                        // instance of user

                        String getuser = DashboardController.usersVo.getUserName();
                        LoginController lc = new LoginController();
                        lc.iniFile(getuser, "delete user : " + uv.getUserName());
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
                }

            } else {
                // ... user chose CANCEL or closed the dialog
                Deletalert.close();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("تحذير");
            alert.setHeaderText("لم يتم تحديد اي عنصر");
            alert.setContentText("يرجى اختيار عنصر لحذفه من القائمة ");
            alert.showAndWait();
        }
    }
    
}
