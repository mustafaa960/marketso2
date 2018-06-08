package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.ProductsDao;
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
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class EditProductsController implements Initializable {

    @FXML
    private JFXTextField txtBarcode;
    @FXML
    private JFXTextField txtProductsQty;
    @FXML
    private JFXTextField txtDiscount;
    @FXML
    private JFXTextField txtBuyPrice;
    @FXML
    private JFXTextField txtSalePriceOdd;
    @FXML
    private JFXTextField txtSalePriceEven;
    @FXML
    private JFXDatePicker dataPickExpire;
    @FXML
    private JFXTextArea txtAreaNote;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtProductName;
    @FXML
    private JFXTextField txtProductsQtyMin;

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
            //make txtProductsQtyMin is only number
            txtProductsQtyMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtProductsQtyMin.setText(oldValue);
            }
            });
            //make txtDiscount is only number
//            txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                txtDiscount.setText(oldValue);
//            }
//            });  
    }
    ProductsVo productsVo = null;

    public void initProduct(ProductsVo pv) {
//        this.productsVo=new ProductsVo();
        this.productsVo = pv;
        txtBarcode.setText(productsVo.getBarcode());
        txtProductName.setText(productsVo.getName());
        txtProductsQty.setText(String.valueOf(productsVo.getQty()));
        txtProductsQtyMin.setText(String.valueOf(productsVo.getQtyMin()));
        txtDiscount.setText(String.valueOf(productsVo.getMaxDiscount()));
        txtBuyPrice.setText(String.valueOf(productsVo.getBuyPrice()));
        txtSalePriceOdd.setText(String.valueOf(productsVo.getSalePriceOdd()));
        txtSalePriceEven.setText(String.valueOf(productsVo.getSalePriceEven()));
        Date expDate = (Date) productsVo.getExp_date();
        dataPickExpire.setValue(expDate.toLocalDate());
        txtAreaNote.setText(productsVo.getNotes());
        
    }

    @FXML
    private void onSave(ActionEvent event) {
        try {
            if (txtBarcode.getText().trim().isEmpty() || txtProductName.getText().trim().isEmpty() || txtProductsQty.getText().trim().isEmpty()|| txtProductsQtyMin.getText().trim().isEmpty()
                    || txtDiscount.getText().trim().isEmpty() || txtBuyPrice.getText().trim().isEmpty()
                    || txtSalePriceOdd.getText().trim().isEmpty() || txtSalePriceEven.getText().trim().isEmpty()
                    || dataPickExpire.getEditor().getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("لا يمكن ترك حقل فارغ");
                alert.setContentText("يرجى ملئ جميع الحقول ");
                alert.showAndWait();
                return;
            }
            int id = this.productsVo.getId();
            String name = txtProductName.getText().trim();
            int qty = Integer.valueOf(txtProductsQty.getText().trim());
            int qtyMin = Integer.valueOf(txtProductsQtyMin.getText().trim());
            String barcode = txtBarcode.getText().trim();
            float buyPrice = Float.valueOf(txtBuyPrice.getText().trim());
            float salePriceOdd = Float.valueOf(txtSalePriceOdd.getText().trim());
            float salePriceEven = Float.valueOf(txtSalePriceEven.getText().trim());
            float maxDiscount = Float.valueOf(txtDiscount.getText().trim());
            Date expDate = Date.valueOf(dataPickExpire.getValue());
            Date storeDate = (Date) this.productsVo.getStore_date();
            String notes = txtAreaNote.getText().trim();
            if ((qty <= 0) ||(qtyMin <= 0)|| (buyPrice <= 0.0) || (salePriceOdd <= 0.0) || (salePriceEven <= 0.0) || (maxDiscount < 0)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }
            ProductsVo pv = new ProductsVo();
            pv.setId(id);
            pv.setName(name);
            pv.setQty(qty);
            pv.setQtyMin(qtyMin);
            pv.setBarcode(barcode);
            pv.setBuyPrice(buyPrice);
            pv.setSalePriceOdd(salePriceOdd);
            pv.setSalePriceEven(salePriceEven);
            pv.setMaxDiscount(maxDiscount);
            pv.setExp_date(expDate);
            pv.setStore_date(storeDate);
            pv.setNotes(notes);
            int count = ProductsDao.getInstance().update(pv);
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
                lc.iniFile(getuser, "edit Product : " + pv.getName());
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
