
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.SalesDao;
import com.miq.sms.models.vo.SalesVo;
import com.miq.sms.models.vo.UsersVo;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ali alshara
 */
public class SalesReportViewController implements Initializable {

    @FXML
    private AnchorPane anchSalesReports;
    @FXML
    private JFXTextField txtSearchBarcode;
    @FXML
    private JFXButton btnDeleteProducts;
    @FXML
    private JFXButton btnEditProducts;
    @FXML
    private TableView<SalesVo> tableProducts;
    @FXML
    private TableColumn<SalesVo, Integer> colProductsNumber;
    @FXML
    private TableColumn<SalesVo, String> colBarcode;
    @FXML
    private TableColumn<SalesVo, String> colProductsName;
    @FXML
    private TableColumn<SalesVo, Integer> colQty;
    @FXML
    private TableColumn<SalesVo, Date> colDate;
    @FXML
    private TableColumn<SalesVo, Float> colsalePrice;
    @FXML
    private TableColumn<SalesVo, Float> colDiscount;
    @FXML
    private TableColumn<SalesVo, String> colCustomerName;
    @FXML
    private TableColumn<SalesVo, String> colUsername;
    @FXML
    private TableColumn<SalesVo, String> colNotes;
    @FXML
    private JFXComboBox<String> comboShow;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UsersVo usersVo = new UsersVo();
            usersVo  = DashboardController.usersVo;
            if(usersVo.getUsersType().getType()=="user"){
                btnEditProducts.setVisible(false);
                btnDeleteProducts.setVisible(false);
            }
        fillComboShow();
        fillTableProducts();
        textSearch();
        comboShow.getSelectionModel().selectedIndexProperty().addListener((newValue) -> {
            fillTableProducts();
            textSearch();
            txtSearchBarcode.setText("");
        });
       comboShow.getSelectionModel().selectFirst();
    }
    public void fillComboShow() {
        ObservableList<String> comboList = FXCollections.observableArrayList();
        comboList.addAll("الكل","يوم","اسبوع","شهر");
        comboShow.setItems(comboList);
    }

    public void fillTableProducts() {

        try {
                  
            tableProducts.getItems().clear();
            ObservableList<SalesVo> products = SalesDao.getInstance().loadAll();
            ObservableList<SalesVo> productsDay = FXCollections.observableArrayList();
            ObservableList<SalesVo> productsWeak = FXCollections.observableArrayList();
            ObservableList<SalesVo> productsMonth = FXCollections.observableArrayList();
            products.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(1)))) {
                    productsDay.add(t);
                }
            });
            products.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(7)))) {
                    productsWeak.add(t);
                }
            });
            products.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(30)))) {
                    productsMonth.add(t);
                }
            });
            colProductsNumber.setCellValueFactory(c -> c.getValue().IdProperty().asObject());
            colBarcode.setCellValueFactory(c -> c.getValue().getProductsVo().BarcodeProperty());
            colProductsName.setCellValueFactory(c -> c.getValue().getProductsVo().NameProperty());
            colQty.setCellValueFactory(c -> c.getValue().QtyProperty().asObject());
            colsalePrice.setCellValueFactory(c -> c.getValue().SalePriceProperty().asObject());
            colDiscount.setCellValueFactory(c -> c.getValue().DiscountProperty().asObject());
            colDate.setCellValueFactory(c -> c.getValue().DateProperty());
            colCustomerName.setCellValueFactory(c -> c.getValue().CustomerNameProperty());
            colUsername.setCellValueFactory(c -> c.getValue().UserNameProperty());
            colNotes.setCellValueFactory(c -> c.getValue().NotesProperty());
            if(comboShow.getSelectionModel().getSelectedIndex()==0){
                tableProducts.setItems(products);
            }
            else if(comboShow.getSelectionModel().getSelectedIndex()==1){
                tableProducts.setItems(productsDay);
            }
            else if(comboShow.getSelectionModel().getSelectedIndex()==2){
                tableProducts.setItems(productsWeak);
            }
            else if(comboShow.getSelectionModel().getSelectedIndex()==3){
                tableProducts.setItems(productsMonth);
            }else{
                tableProducts.setItems(products);
            }
            

            tableProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
            fillTableProducts();
            ObservableList data = tableProducts.getItems();
            txtSearchBarcode.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    tableProducts.setItems(data);
                    tableProducts.getSelectionModel().clearSelection();

                }
                String value = newValue.toLowerCase().trim();
                ObservableList<SalesVo> subentries = FXCollections.observableArrayList();

                long count = tableProducts.getColumns().stream().count();
                for (int i = 0; i < tableProducts.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String barcode = "" + tableProducts.getColumns().get(1).getCellData(i);
                        String Name = "" + tableProducts.getColumns().get(2).getCellData(i);
                        String customerName = "" + tableProducts.getColumns().get(7).getCellData(i);
                        if (Name.trim().startsWith(value)) {
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        } else if (customerName.trim().startsWith(value)) {
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        }  else if (barcode.trim().startsWith(value)) {
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        } else if (barcode.trim().equals(value)) {
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        }

                    }
                }
                tableProducts.setItems(subentries);
                tableProducts.getSelectionModel().clearSelection();

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
    private void onDeleteProducts(ActionEvent event) {
        if (tableProducts.getSelectionModel().getSelectedItem() != null) {
            Alert Deletalert = new Alert(Alert.AlertType.CONFIRMATION);
            Deletalert.setTitle("حذف");
            Deletalert.setHeaderText("هل انت متأكد من الحذف؟");
            Optional<ButtonType> result = Deletalert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    SalesVo sv = tableProducts.getSelectionModel().getSelectedItem();
                    int count = SalesDao.getInstance().delete(sv);
                    if (count == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("الحذف");
                        alert.setHeaderText("تم الحذف بنجاح");
                        alert.showAndWait();
                        textSearch();
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

    @FXML
    private void onEditProducts(ActionEvent event) {
        
        try {
            SalesVo salesVo = new SalesVo();
            salesVo = tableProducts.getSelectionModel().getSelectedItem();
            if (salesVo != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditSalesReportView.fxml"));
                Parent root = loader.load();
                EditSalesReportController controller = loader.<EditSalesReportController>getController();
                controller.initProduct(salesVo);
                Stage primaryStage = (Stage) btnEditProducts.getScene().getWindow();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setFullScreen(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(scene);
                int index = tableProducts.getSelectionModel().getSelectedIndex();
                stage.show();
                stage.setOnHidden((e) -> {
                    fillTableProducts();
                    textSearch();
                    tableProducts.requestFocus();
                    tableProducts.getSelectionModel().clearAndSelect(index);
                    tableProducts.getFocusModel().focusRightCell();
                    tableProducts.scrollTo(tableProducts.getSelectionModel().getFocusedIndex());
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

}
