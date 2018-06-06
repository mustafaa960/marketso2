
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.BuyDao;
import com.miq.sms.models.vo.BuyVo;
import com.miq.sms.models.vo.ProductsVo;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author AL SAFIR
 */
public class EditPurchasesController implements Initializable {
    @FXML
    private JFXTextField txtProductsQty;
    @FXML
    private JFXTextField txtBuyPrice;
    @FXML
    private JFXDatePicker dataPickExpire;
    @FXML
    private JFXTextArea txtAreaNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXDatePicker dataPickBuy;
    @FXML
    private JFXTextField txtProductName;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       //make txtProductsQty is only number 
            txtProductsQty.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtProductsQty.setText(oldValue);
            }
            });
    }
    BuyVo buyVo = null;

    public void initProduct(BuyVo bv) {
//        this.productsVo=new ProductsVo();
        this.buyVo = bv;
        txtProductsQty.setText(String.valueOf(buyVo.getQty()));
        txtProductName.setText(buyVo.getProductsVo().getName());
        txtBuyPrice.setText(String.valueOf(buyVo.getBuyPrice()));
        txtUserName.setText(buyVo.getUserName());
        Date expDate = (Date) buyVo.getExpDate();
        dataPickExpire.setValue(expDate.toLocalDate());
        Date date = (Date) buyVo.getDate();
        dataPickBuy.setValue(date.toLocalDate());
        txtAreaNote.setText(buyVo.getNotes());
    }
    @FXML
    private void onSave(ActionEvent event) {
        try {
            if ( txtUserName.getText().trim().isEmpty() || txtProductsQty.getText().trim().isEmpty()
                    || txtBuyPrice.getText().trim().isEmpty()
                    || dataPickExpire.getEditor().getText().trim().isEmpty()
                    || dataPickBuy.getEditor().getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("لا يمكن ترك حقل فارغ");
                alert.setContentText("يرجى ملئ جميع الحقول ");
                alert.showAndWait();
                return;
            }
            int id = this.buyVo.getId();
            String username = txtUserName.getText().trim();
            int qty = Integer.valueOf(txtProductsQty.getText().trim());
            float buyPrice = Float.valueOf(txtBuyPrice.getText().trim());
            Date expDate = Date.valueOf(dataPickExpire.getValue());
            Date date = Date.valueOf(dataPickBuy.getValue());
            String notes = txtAreaNote.getText().trim();
            ProductsVo productsVo=buyVo.getProductsVo();
            if ((qty <= 0) || (buyPrice <= 0.0)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }
            BuyVo bv = new BuyVo();
            bv.setId(id);
            //==============create instance of userVo
//                UsersVo usersVo = new UsersVo();
//            String user = DashboardController.usersVo.getUserName();
//            usersVo.setUserName(user);
//            buyVo.setUser(usersVo);
            //========================
            bv.setUserName(username);
            bv.setQty(qty);
            bv.setBuyPrice(buyPrice);
            bv.setExpDate(expDate);
            bv.setDate(date);
            bv.setNotes(notes);
            bv.setProductsVo(productsVo);
            int count = BuyDao.getInstance().update(bv);
            if (count == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("التعديل");
                alert.setHeaderText("تم التعديل بنجاح");
                alert.showAndWait();
                Stage stage = (Stage) btnSave.getScene().getWindow();
                stage.close();
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
    }
    

