package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.ProductsDao;
import com.miq.sms.models.dao.SalesDao;
import com.miq.sms.models.vo.ProductsVo;
import com.miq.sms.models.vo.SalesVo;
import com.miq.sms.models.vo.UsersVo;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Mustafa
 */
public class SalesViewController implements Initializable {

    @FXML
    private Label labQTYremining;

    @FXML
    private Label labTotalPrice;

    @FXML
    private JFXComboBox<String> comboQTY;

    @FXML
    private TableColumn<SalesVo, Integer> colQTY;

    @FXML
    private JFXTextField txtBarcode;

    @FXML
    private TableView<SalesVo> TableSales;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnAddToSale;

    @FXML
    private TableColumn<SalesVo, Float> colPrice;

    @FXML
    private AnchorPane parentPane;

    @FXML
    private JFXComboBox<ProductsVo> comboProductsName;

    @FXML
    private JFXTextField txtProductsNumber;

    @FXML
    private TableColumn<SalesVo, String> colProductName;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXComboBox<Float> comboDiscount;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextArea txtAreaNote;
    @FXML
    private TableColumn<SalesVo, String> colNote;
    @FXML
    private JFXButton btnDeleteFromTable;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        barcodeSearch();
        fillComboQty();
        comboQtyChanged();
        comboDiscountChanged();
        
    }

    @FXML
    private void onSave(ActionEvent event) {
        ObservableList<SalesVo> data = TableSales.getItems();
        if (data.size() > 0) {
            data.forEach((sv) -> {
                try {
                    int count = ProductsDao.getInstance().update(sv.getProductsVo());
                    int count2 = SalesDao.getInstance().insert(sv);
                    if (count == 0 && count2 == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("خطأ");
                        alert.setHeaderText("لم يتم اضافة الفاتورة  ");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("خطأ");
                    alert.setHeaderText(ex.getMessage());
                    alert.setContentText(ex.toString());
                    alert.showAndWait();
                }
            });
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("فاتورة جديدة");
            alert.setHeaderText(" تمت اضافة فاتورة جديدة  : ");
            alert.setContentText("الحساب الكلي للفاتورة : " + labTotalPrice.getText().trim());
            alert.showAndWait();
            barcodeSearch();
            TableSales.getItems().clear();
            tableChanged();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText("لم يتم اضافة اي فاتورة في قائمة المبيعات  ");
            alert.showAndWait();
        }

    }

    @FXML
    private void onClear(ActionEvent event) {
        clear();
    }

    @FXML
    private void onAdd(ActionEvent event) {
        if (txtBarcode.getText().trim().isEmpty() || txtProductsNumber.getText().trim().isEmpty() || txtPrice.getText().trim().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText("لا يمكن ترك حقل فارغ");
            alert.setContentText("يرجى ملئ كل الحقول");
            alert.showAndWait();
            return;
        }

        ProductsVo pvOld = comboProductsName.getSelectionModel().getSelectedItem();
        if (pvOld != null) {
            ProductsVo pv = new ProductsVo();
            pv.setId(pvOld.getId());
            pv.setBarcode(pvOld.getBarcode());
            pv.setName(pvOld.getName());
            pv.setMaxDiscount(pvOld.getMaxDiscount());
            pv.setQty(pvOld.getQty() - Integer.valueOf(txtProductsNumber.getText().trim()));
            pv.setBuyPrice(pvOld.getBuyPrice());
            pv.setExp_date((Date) pvOld.getExp_date());
            pv.setSalePriceEven(pvOld.getSalePriceEven());
            pv.setSalePriceOdd(pvOld.getSalePriceOdd());
            pv.setStore_date((Date) pvOld.getStore_date());
            pv.setNotes(pvOld.getNotes());
            SalesVo sv = new SalesVo();
            int id;
            if (TableSales.getItems().size() > 0) {
                id = TableSales.getItems().get(TableSales.getItems().size() - 1).getId();
            } else {
                id = SalesDao.getInstance().getLastSalesId();
            }
            sv.setId(id + 1);
            sv.setQty(Integer.valueOf(txtProductsNumber.getText().trim()));
            sv.setDate(Date.valueOf(LocalDate.now()));
            sv.setSalePrice(Float.valueOf(txtPrice.getText().trim()) * Integer.valueOf(txtProductsNumber.getText().trim()));
            sv.setDiscount(comboDiscount.getSelectionModel().getSelectedItem());
            if(txtCustomerName.getText().trim().isEmpty()){
                sv.setCustomerName("زبون");
            }else{
                sv.setCustomerName(txtCustomerName.getText().trim());
            }
            
//            ============= create instance of userVo
            UsersVo usersVo = new UsersVo();
            String user = DashboardController.usersVo.getUserName();
            usersVo.setUserName(user);
            sv.setUser(usersVo);
//           ==========

            sv.setNotes(txtAreaNote.getText().trim());

            if (pv != null) {
                sv.setProductsVo(pv);
            }
            if (sv != null) {
                colProductName.setCellValueFactory(c -> c.getValue().getProductsVo().NameProperty());
                colQTY.setCellValueFactory(c -> c.getValue().QtyProperty().asObject());
                colPrice.setCellValueFactory(c -> c.getValue().SalePriceProperty().asObject());
                colNote.setCellValueFactory(c -> c.getValue().NotesProperty());
                TableSales.getItems().add(sv);
                clear();
                tableChanged();
            }
        }
    }

    public void barcodeSearch() {
        try {
            FillComboProductsName();
            comboProductsName.getSelectionModel().clearSelection();
            ObservableList<ProductsVo> dataOld = comboProductsName.getItems();
            txtBarcode.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    ObservableList<ProductsVo> data = ProductsDao.getInstance().loadAll();
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
                        comboProductsName.getSelectionModel().selectFirst();
                        fillComboDiscount();
                        labQTYremining.setText(String.valueOf(comboProductsName.getSelectionModel().getSelectedItem().getQty()));
                        if (comboQTY.getSelectionModel().getSelectedIndex() == 0) {
                            float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceOdd();
                            float discount = comboDiscount.getSelectionModel().getSelectedItem();
                            float total = price -  discount;
                            txtPrice.setText(String.valueOf(total));
                        } else {
                            float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceEven();
                            float discount = comboDiscount.getSelectionModel().getSelectedItem();
                            float total = price -  discount;
                            txtPrice.setText(String.valueOf(total));
                        }
                    } else {
                        txtPrice.setText("");
                        comboDiscount.getItems().clear();
                        labQTYremining.setText("");
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("خطأ");
                    alert.setHeaderText(ex.getMessage());
                    alert.setContentText(ex.toString());
                    alert.showAndWait();
                }

            });
            //make txtProductsNumber is only number ||newValue.matches("[a-zA-Z]*")
            txtProductsNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtProductsNumber.setText(oldValue);
            }
        });
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
        txtBarcode.requestFocus();
    }

    private void FillComboProductsName() {
        try {
//            comboProductsName.setButtonCell(
//                    new ListCell<ProductsVo>() {
//                @Override
//                protected void updateItem(ProductsVo pv, boolean bln) {
//                    super.updateItem(pv, bln);
//                    if (bln) {
//                        setText("");
//                    } else {
//                        setText(pv.getName());
//                    }
//                }
//            });
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

//                @Override
//                public Object fromString(String string) {
//                    if ((!string.trim().equals("")) && !map.containsKey(string.trim())) {
//                        ProductsVo pv = new ProductsVo();
//                        pv.setName(string.trim());
//                        return pv;
//                    }
//                    return map.get(string.trim());
//                }
                @Override
                public Object fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private void fillComboQty() {
        ObservableList<String> qtyType = FXCollections.observableArrayList();
        qtyType.add("مفرد");
        qtyType.add("جملة");
        comboQTY.setItems(qtyType);
        comboQTY.getSelectionModel().selectFirst();
    }

    private void fillComboDiscount() {
        float maxDiscount = 0;
        if (comboProductsName.getSelectionModel().getSelectedItem() != null) {
            maxDiscount = comboProductsName.getSelectionModel().getSelectedItem().getMaxDiscount();
            comboDiscount.getItems().clear();
            for (int i = 0; i <= maxDiscount; i++) {
                comboDiscount.getItems().add(Float.valueOf(i));
                i+=249;
            }
            comboDiscount.getSelectionModel().selectFirst();
        }
    }

    private void comboQtyChanged() {
        try {

            comboQTY.getSelectionModel().selectedIndexProperty().addListener((newValue) -> {
                if (comboProductsName.getSelectionModel().getSelectedItem() != null && comboDiscount.getSelectionModel().getSelectedItem() >= 0) {
                    txtPrice.setText("");
                    if (comboQTY.getSelectionModel().getSelectedIndex() == 0) {
                        float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceOdd();
                        float discount = comboDiscount.getSelectionModel().getSelectedItem();
                        float total = price -  discount;
                        txtPrice.setText(String.valueOf(total));
                    } else if (comboQTY.getSelectionModel().getSelectedIndex() == 1) {
                        float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceEven();
                        float discount = comboDiscount.getSelectionModel().getSelectedItem();
                        float total =  price -  discount;
                        txtPrice.setText(String.valueOf(total));
                    }
                }
            });
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    private void comboDiscountChanged() {
        try {
            comboDiscount.getSelectionModel().selectedIndexProperty().addListener((newValue) -> {
                if (comboDiscount.getSelectionModel().getSelectedItem() != null && newValue != null) {
                    if (comboQTY.getSelectionModel().getSelectedItem() != null) {
                        txtPrice.setText("");
                        if (comboQTY.getSelectionModel().getSelectedIndex() == 0) {
                            float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceOdd();
                            float discount = comboDiscount.getSelectionModel().getSelectedItem();
                            float total = price  - discount;
                            txtPrice.setText(String.valueOf(total));
                        } else {
                            float price = comboProductsName.getSelectionModel().getSelectedItem().getSalePriceEven();
                            float discount = comboDiscount.getSelectionModel().getSelectedItem();
                            float total = price - discount;
                            txtPrice.setText(String.valueOf(total));
                        }

                    }
                }
            });
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    float totalPrice = 0;

    private void tableChanged() {

        if (TableSales.getItems() != null) {
            totalPrice = 0;
            TableSales.getItems().forEach((item) -> {
                totalPrice += item.getSalePrice();
            });
            labTotalPrice.setText(String.valueOf(totalPrice));
        } else {
            labTotalPrice.setText("");
        }
    }

    private void clear() {
        txtBarcode.setText("");
        comboProductsName.getItems().clear();
        comboProductsName.getSelectionModel().clearSelection();
        comboQTY.getSelectionModel().selectFirst();
        comboDiscount.getItems().clear();
        comboDiscount.getSelectionModel().clearSelection();
        txtProductsNumber.setText("");
        labQTYremining.setText("");
        txtPrice.setText("");
        txtCustomerName.setText("");
        txtAreaNote.setText("");
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (TableSales.getSelectionModel().getSelectedItem() != null) {
            Alert Deletalert = new Alert(Alert.AlertType.CONFIRMATION);

            Deletalert.setTitle("حذف");
            Deletalert.setHeaderText("هل انت متأكد من الحذف؟");
            Optional<ButtonType> result = Deletalert.showAndWait();
            if (result.get() == ButtonType.OK) {
                SalesVo sv = TableSales.getSelectionModel().getSelectedItem();
                TableSales.getItems().remove(sv);
                tableChanged();

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
