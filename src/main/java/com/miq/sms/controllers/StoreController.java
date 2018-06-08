
package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.miq.sms.models.dao.ProductsDao;
import com.miq.sms.models.vo.ProductsVo;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class StoreController implements Initializable {

    
     @FXML
    private TableColumn<ProductsVo, Integer> colProductsNumber;
    @FXML
    private TableColumn<ProductsVo, String> colProductsName;
    @FXML
    private JFXButton btnDeleteProducts;
    @FXML
    private JFXButton btnEditProducts;
    @FXML
    private JFXTextField txtSearchBarcode;
    @FXML
    private TableView<ProductsVo> tableProducts;
    @FXML
    private TableColumn<ProductsVo, String> colBarcode;
    @FXML
    private TableColumn<ProductsVo, Integer> colQty;
    @FXML
    private TableColumn<ProductsVo, Float> colBuyPrice;
    @FXML
    private TableColumn<ProductsVo, Float> colSalePriceOdd;
    @FXML
    private TableColumn<ProductsVo, Float> colSalePriceEven;
    @FXML
    private TableColumn<ProductsVo, Float> colMaxDiscount;
    @FXML
    private TableColumn<ProductsVo, Date> colExpDate;
    @FXML
    private TableColumn<ProductsVo, Date> colStoreDate;
    @FXML
    private TableColumn<ProductsVo, String> colNotes;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillTableProducts();
        textSearch();
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
                    ProductsVo pv = tableProducts.getSelectionModel().getSelectedItem();
                    int count =ProductsDao.getInstance().delete(pv);
                    if(count ==1){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("الحذف");
                        alert.setHeaderText("تم الحذف بنجاح");
                        alert.showAndWait();
                        textSearch();
                        String getuser = DashboardController.usersVo.getUserName();
                        LoginController lc = new LoginController();
                        lc.iniFile(getuser, "delete Product : " + pv.getName());
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
            ProductsVo productsVo = new ProductsVo();
            productsVo = tableProducts.getSelectionModel().getSelectedItem();
            if (productsVo != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditProductsView.fxml"));
                Parent root = loader.load();
                EditProductsController controller = loader.<EditProductsController>getController();
                controller.initProduct(productsVo);
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
     
    
    public void textSearch() {
        try {
            fillTableProducts();
            ObservableList data = tableProducts.getItems();
            txtSearchBarcode.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null  && (newValue.length() < oldValue.length())) {
                    tableProducts.setItems(data);
                    tableProducts.getSelectionModel().clearSelection();

                }
                String value = newValue.toLowerCase().trim();
                ObservableList<ProductsVo> subentries = FXCollections.observableArrayList();

                long count = tableProducts.getColumns().stream().count();
                for (int i = 0; i < tableProducts.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String barcode = "" + tableProducts.getColumns().get(1).getCellData(i);
                        String Name = "" + tableProducts.getColumns().get(2).getCellData(i);
                        if(Name.trim().startsWith(value)){
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        }
                        else if (barcode.trim().startsWith(value)) {
                            subentries.add(tableProducts.getItems().get(i));
                            break;
                        }
                        else if (barcode.trim().equals(value)) {
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
    
    public void fillTableProducts(){
        
         try {
             
             tableProducts.getItems().clear();
             ObservableList<ProductsVo> products = ProductsDao.getInstance().loadAll();
             
             colProductsNumber.setCellValueFactory(c -> c.getValue().IdProperty().asObject());
             colBarcode.setCellValueFactory(c -> c.getValue().BarcodeProperty());
             colProductsName.setCellValueFactory(c -> c.getValue().NameProperty());
             colQty.setCellValueFactory(c -> c.getValue().QtyProperty().asObject());
             colBuyPrice.setCellValueFactory(c -> c.getValue().BuyPriceProperty().asObject());
             colSalePriceOdd.setCellValueFactory(c -> c.getValue().SalePriceOddProperty().asObject());
             colSalePriceEven.setCellValueFactory(c -> c.getValue().SalePriceEvenProperty().asObject());
             colMaxDiscount.setCellValueFactory(c -> c.getValue().MaxDiscountProperty().asObject());
             colExpDate.setCellValueFactory(c -> c.getValue().Exp_dateProperty());
             colStoreDate.setCellValueFactory(c -> c.getValue().Store_dateProperty());
             colNotes.setCellValueFactory(c -> c.getValue().NotesProperty());
             tableProducts.setItems(products);
             
             tableProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
             
         } catch (Exception ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
         }
        
    }
    
}
