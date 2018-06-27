package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.BuyDao;
import com.miq.sms.models.dao.ProductsDao;
import com.miq.sms.models.vo.BuyVo;
import com.miq.sms.models.vo.ProductsVo;
import com.miq.sms.models.vo.UsersVo;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author ali alshara
 */
public class AddProductsViewController implements Initializable {

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtBarcode;

    @FXML
    private DatePicker dataPickExpire;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXTextField txtBuyPrice;

    @FXML
    private JFXComboBox<ProductsVo> comboProductsName;
    @FXML
    private JFXTextField txtProductsQty;
    @FXML
    private JFXTextField txtDiscount;
    @FXML
    private JFXTextField txtSalePriceOdd;
    @FXML
    private JFXTextField txtSalePriceEven;
    @FXML
    private JFXTextArea txtAreaNote;
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
//        FillComboProductsName();
        barcodeSearch();

    }

    public void barcodeSearch() {
        try {
            FillComboProductsName();

            ObservableList<ProductsVo> data = ProductsDao.getInstance().loadAll();
            ObservableList<ProductsVo> dataOld = comboProductsName.getItems();
            txtBarcode.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    comboProductsName.setItems(dataOld);
                }
                String value = newValue.trim();
                ObservableList<ProductsVo> subentries = FXCollections.observableArrayList();
                data.forEach((product) -> {
                    if (value.equals(product.getBarcode())) {
                        subentries.add(product);
                    }
                });

                comboProductsName.setItems(subentries);
                if (comboProductsName.getItems().size() > 0) {
                    comboProductsName.setEditable(false);
                    comboProductsName.getSelectionModel().selectFirst();
                    txtProductsQtyMin.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getQtyMin()));
                    txtDiscount.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getMaxDiscount()));
                    txtBuyPrice.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getBuyPrice()));
                    txtSalePriceOdd.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getSalePriceOdd()));
                    txtSalePriceEven.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getSalePriceEven()));
                    Date date = (Date) comboProductsName.getSelectionModel().getSelectedItem().getExp_date();
                     dataPickExpire.setValue(date.toLocalDate());
                } else {
                    comboProductsName.setEditable(true);
                    clearTexts2();
                }

            });
            //make txtProductsQty is only number ||newValue.matches("[a-zA-Z]*")
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
//            //make txtDiscount is only number
//            txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                txtDiscount.setText(oldValue);
//            }
//            });
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
        txtBarcode.requestFocus();
    }

    @FXML
    private void onClear(ActionEvent event) {
        clearTexts();
    }

    @FXML
    private void onSave(ActionEvent event) {
        try {
            if(txtProductsQty.getText().trim().isEmpty()||txtProductsQtyMin.getText().trim().isEmpty()|| txtBarcode.getText().trim().isEmpty()||txtBuyPrice.getText().trim().isEmpty()||txtSalePriceOdd.getText().trim().isEmpty()||txtSalePriceEven.getText().trim().isEmpty()||txtDiscount.getText().trim().isEmpty()||dataPickExpire.getEditor().getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }
            int qty = Integer.valueOf(txtProductsQty.getText().trim());
            int qtyMin = Integer.valueOf(txtProductsQtyMin.getText().trim());
            String barcode = txtBarcode.getText().trim();
            float buyPrice = Float.valueOf(txtBuyPrice.getText().trim());
            float salePriceOdd = Float.valueOf(txtSalePriceOdd.getText().trim());
            float salePriceEven = Float.valueOf(txtSalePriceEven.getText().trim());
            float maxDiscount = Float.valueOf(txtDiscount.getText().trim());
            Date expDate = Date.valueOf(dataPickExpire.getValue());
            Date storeDate = Date.valueOf(LocalDate.now());
            String notes = txtAreaNote.getText().trim();
            if ((qty <= 0)||(qtyMin <= 0) || (buyPrice <= 0.0) || (salePriceOdd <= 0.0) || (salePriceEven <= 0.0) || (maxDiscount < 0) ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("احد الحقول غير صحيح");
                alert.setContentText("يرجى التأكد من اضافه جميع الحقول بشكل صحيح");
                alert.showAndWait();
                return;
            }

            if (comboProductsName.getItems().size() > 0) {

                int id = comboProductsName.getItems().get(0).getId();
                String name = comboProductsName.getItems().get(0).getName();

                ProductsVo productsVo = new ProductsVo();
                productsVo.setId(id);
                productsVo.setName(name);
                productsVo.setQty(comboProductsName.getItems().get(0).getQty() + qty);
                productsVo.setQtyMin(qtyMin);
                productsVo.setBarcode(barcode);
                if (buyPrice > comboProductsName.getItems().get(0).getBuyPrice()) {
                    productsVo.setBuyPrice(buyPrice);
                } else {
                    productsVo.setBuyPrice(comboProductsName.getItems().get(0).getBuyPrice());
                }

                productsVo.setSalePriceOdd(salePriceOdd);
                productsVo.setSalePriceEven(salePriceEven);
                productsVo.setMaxDiscount(maxDiscount);
                productsVo.setExp_date(expDate);
                productsVo.setStore_date(storeDate);
                productsVo.setNotes(notes);
                BuyVo buyVo = new BuyVo();
                buyVo.setProductsVo(productsVo);
                buyVo.setId(BuyDao.getInstance().getLastBuyId() + 1);
                buyVo.setQty(qty);
                buyVo.setDate(storeDate);
                buyVo.setExpDate(expDate);
                buyVo.setBuyPrice(buyPrice);
                //==============create instance of userVo
                UsersVo usersVo = new UsersVo();
            String user = DashboardController.usersVo.getUserName();
            usersVo.setUserName(user);
            buyVo.setUser(usersVo);
            //========================
//                buyVo.setUserName("admin");
                buyVo.setNotes(notes);
                int count = ProductsDao.getInstance().update(productsVo);
                int count2 = BuyDao.getInstance().insert(buyVo);
                if (count == 1 && count2 == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("تعديل");
                    alert.setHeaderText("تم التعديل بنجاح");
                    alert.showAndWait();
//                    JOptionPane.showMessageDialog(null, "تم التعديل بنجاح");
                    clearTexts();
                    barcodeSearch();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("خطأ");
                    alert.setHeaderText("لم يتم التعديل  ");
                    alert.showAndWait();
//                    JOptionPane.showMessageDialog(null, "لم يتم التعديل  ");
                }

            } else {
                int id = ProductsDao.getInstance().getLastProductId() + 1;
                String name = comboProductsName.getEditor().getText().trim();

                ProductsVo productsVo = new ProductsVo();
                productsVo.setId(id);
                productsVo.setName(name);
                productsVo.setQty(qty);
                productsVo.setQtyMin(qtyMin);
                productsVo.setBarcode(barcode);
                productsVo.setBuyPrice(buyPrice);
                productsVo.setSalePriceOdd(salePriceOdd);
                productsVo.setSalePriceEven(salePriceEven);
                productsVo.setMaxDiscount(maxDiscount);
                productsVo.setExp_date(expDate);
                productsVo.setStore_date(storeDate);
                productsVo.setNotes(notes);
                BuyVo buyVo = new BuyVo();
                buyVo.setProductsVo(productsVo);
                buyVo.setId(BuyDao.getInstance().getLastBuyId() + 1);
                buyVo.setQty(qty);
                buyVo.setDate(storeDate);
                buyVo.setExpDate(expDate);
                buyVo.setBuyPrice(buyPrice);
                //==============create instance of userVo
                UsersVo usersVo = new UsersVo();
            String user = DashboardController.usersVo.getUserName();
            usersVo.setUserName(user);
            buyVo.setUser(usersVo);
            //========================
//                buyVo.setUserName("admin");
                buyVo.setNotes(notes);
                int count = ProductsDao.getInstance().insert(productsVo);
                int count2 = BuyDao.getInstance().insert(buyVo);
                if (count == 1 && count2 == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("الاضافة");
                    alert.setHeaderText("تمت الاضافه بنجاح");
                    alert.showAndWait();
//                    JOptionPane.showMessageDialog(null, "تمت الاضافه بنجاح");
                    clearTexts();
                    barcodeSearch();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("خطأ");
                    alert.setHeaderText("لم تتم الاضافه  ");
                    alert.showAndWait();
//                    JOptionPane.showMessageDialog(null,"لم تتم الاضافه  "); 
                }
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

//method return products name from products object and set products name in the ComboBox  
    private void FillComboProductsName() {

        try {

            comboProductsName.setButtonCell(
                    new ListCell<ProductsVo>() {
                @Override
                protected void updateItem(ProductsVo pv, boolean bln) {
                    super.updateItem(pv, bln);
                    if (bln) {
                        setText("");
                    } else {
                        setText(pv.getName());
                    }
                }
            });
            comboProductsName.setConverter(
                    new StringConverter() {
                private final Map<String, ProductsVo> map = new HashMap<>();

                @Override
                public String toString(Object t) {
                    if (t != null) {
                        String str = ((ProductsVo) t).getName();
                        map.put(str, (ProductsVo) t);
                        return str;
                    } else {
                        return "";
                    }
                }

                @Override
                public Object fromString(String string) {
                    if ((!string.trim().equals("")) && !map.containsKey(string.trim())) {

                        ProductsVo pv = new ProductsVo();
                        pv.setName(string.trim());
                        return pv;
                    }

                    return map.get(string.trim());
                }

            });

            comboProductsName.setCellFactory((ListView<ProductsVo> p) -> {
                ListCell cell = new ListCell<ProductsVo>() {
                    @Override
                    protected void updateItem(ProductsVo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            });

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }

    }

    private void clearTexts() {
        txtBarcode.setText("");
        comboProductsName.getItems().clear();
        comboProductsName.getEditor().setText("");
        txtProductsQty.setText("");
        txtProductsQtyMin.setText("");
        txtDiscount.setText("");
        txtBuyPrice.setText("");
        txtSalePriceOdd.setText("");
        txtSalePriceEven.setText("");
        dataPickExpire.getEditor().setText("");
        dataPickExpire.setValue(null);
        txtAreaNote.setText("");
    }
    private void clearTexts2() {
        comboProductsName.getItems().clear();
        comboProductsName.getEditor().setText("");
        txtProductsQtyMin.setText("");
        txtDiscount.setText("");
        txtBuyPrice.setText("");
        txtSalePriceOdd.setText("");
        txtSalePriceEven.setText("");
        dataPickExpire.getEditor().setText("");
        dataPickExpire.setValue(null);
        txtAreaNote.setText("");
    }

}
