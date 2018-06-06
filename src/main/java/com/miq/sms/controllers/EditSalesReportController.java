
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.SalesDao;
import com.miq.sms.models.vo.ProductsVo;
import com.miq.sms.models.vo.SalesVo;
import com.miq.sms.models.vo.UsersVo;
import java.net.URL;
import java.sql.Date;
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
public class EditSalesReportController implements Initializable {

    @FXML
    private JFXTextField txtProductsQty;
    @FXML
    private JFXTextField txtDiacount;
    @FXML
    private JFXTextArea txtAreaNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXTextField txtProductName;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtSalePrice;
    @FXML
    private JFXDatePicker dataPickSale;

    /**
     * Initializes the controller class.
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
//        //make txtDiacount is only number 
//            txtDiacount.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                txtDiacount.setText(oldValue);
//            }
//        });
    }    
     SalesVo salesVo = null;

    public void initProduct(SalesVo sv) {
//        this.productsVo=new ProductsVo();
        this.salesVo = sv;
        txtProductsQty.setText(String.valueOf(salesVo.getQty()));
        txtDiacount.setText(String.valueOf(salesVo.getDiscount()));
        txtProductName.setText(salesVo.getProductsVo().getName());
        txtSalePrice.setText(String.valueOf(salesVo.getSalePrice()));
        txtUserName.setText(salesVo.getUserName());
        txtCustomerName.setText(salesVo.getCustomerName());
        Date date = (Date) salesVo.getDate();
        dataPickSale.setValue(date.toLocalDate());
        txtAreaNote.setText(salesVo.getNotes());
    }
    @FXML
    private void onSave(ActionEvent event) {
        try {
            if ( txtUserName.getText().trim().isEmpty() || txtProductsQty.getText().trim().isEmpty()
                    || txtSalePrice.getText().trim().isEmpty()|| txtDiacount.getText().trim().isEmpty()
                    || txtCustomerName.getText().trim().isEmpty()
                    || dataPickSale.getEditor().getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("لا يمكن ترك حقل فارغ");
                alert.setContentText("يرجى ملئ جميع الحقول ");
                alert.showAndWait();
                return;
            }
            int id = this.salesVo.getId();
//            String username = txtUserName.getText().trim();
            String customername = txtCustomerName.getText().trim();
            int qty = Integer.valueOf(txtProductsQty.getText().trim());
            float discount = Float.valueOf(txtDiacount.getText().trim());
            float salePrice = Float.valueOf(txtSalePrice.getText().trim());
            Date date = Date.valueOf(dataPickSale.getValue());
            String notes = txtAreaNote.getText().trim();
            ProductsVo productsVo=this.salesVo.getProductsVo();
            if ((discount < 0 ) ||(qty <= 0) || (salePrice <= 0.0)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }
            
            SalesVo sv = new SalesVo();
            sv.setId(id);
            //==============create instance of userVo
                UsersVo usersVo = new UsersVo();
            String user = DashboardController.usersVo.getUserName();
            usersVo.setUserName(user);
            sv.setUser(usersVo);
            //========================
//            sv.setUserName(username);
            sv.setQty(qty);
            sv.setDiscount(discount);
            if(customername.isEmpty()){
                sv.setCustomerName("زبون");
            }else{
                sv.setCustomerName(customername);
            }
            sv.setSalePrice(salePrice);
            sv.setDate(date);
            sv.setNotes(notes);
            sv.setProductsVo(productsVo);
            int count = SalesDao.getInstance().update(sv);
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
