package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToolbar;
import com.miq.sms.models.vo.UsersVo;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Mustafa
 */
public class DashboardController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane holderPane;
    @FXML
    private AnchorPane sideAnchor;
    @FXML
    private JFXToolbar toolBar;
    private AnchorPane home, sales, addProduct, store, purchases, salesReport, users, setting;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXButton btnViewSales;
    @FXML
    private JFXButton btnAddProduct;
    @FXML
    private JFXButton btnPurchases;
    @FXML
    private JFXButton btnSalesReport;
    @FXML
    private JFXButton btnUsers;
    @FXML
    private JFXButton btnSetting;

    public static UsersVo usersVo;
    @FXML
    private JFXButton btnStore;

    /**
     * Creates new form Home
     *
     * @param uv
     */
    public DashboardController(UsersVo uv) {
//        initComponents();
//        this.setLocationRelativeTo(null);
        usersVo = uv;
//        getUserLevel();

    }

    public DashboardController() {
//      ;

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        createPages();
        if (usersVo.getUsersType().getType() == "user") {
            btnAddProduct.setDisable(true);
            btnStore.setDisable(true);
            btnPurchases.setDisable(true);
//            btnSalesReport.setDisable(true);
            btnUsers.setDisable(true);
            btnSetting.setDisable(true);
        }
    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    FXMLLoader homeloader = new FXMLLoader(getClass().getResource("/fxml/Overview.fxml"));
    FXMLLoader salesloader = new FXMLLoader(getClass().getResource("/fxml/SalesView.fxml"));
    FXMLLoader addProductloader = new FXMLLoader(getClass().getResource("/fxml/addProductsView.fxml"));
    FXMLLoader storeloader = new FXMLLoader(getClass().getResource("/fxml/Store.fxml"));
    FXMLLoader purchaseLoder = new FXMLLoader(getClass().getResource("/fxml/PurchasesView.fxml"));
    FXMLLoader salesReportLoder = new FXMLLoader(getClass().getResource("/fxml/salesReportView.fxml"));
    FXMLLoader usersLoder = new FXMLLoader(getClass().getResource("/fxml/UsersView.fxml"));
    FXMLLoader settingLoder = new FXMLLoader(getClass().getResource("/fxml/SettingDBView.fxml"));

    //Load all fxml files to a cahce for swapping
    private void createPages() {
        try {
            home = homeloader.load();
            sales = salesloader.load();
            addProduct = addProductloader.load();
            store = storeloader.load();
            purchases = purchaseLoder.load();
            salesReport = salesReportLoder.load();
            users = usersLoder.load();
            setting = settingLoder.load();

            //set up default node on page load
            setNode(home);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    @FXML
    private void openHome(ActionEvent event) {
        setNode(home);
        OverviewController controller = homeloader.<OverviewController>getController();
        controller.fillProductsExp();
        controller.fillProductsQty();
    }

    @FXML
    private void onViewSales(ActionEvent event) {
        setNode(sales);
        SalesViewController controller = salesloader.<SalesViewController>getController();
        controller.barcodeSearch();
    }

    @FXML
    private void onAddProduct(ActionEvent event) {
        setNode(addProduct);

        AddProductsViewController controller = addProductloader.<AddProductsViewController>getController();
        controller.barcodeSearch();
    }

    @FXML
    private void onStore(ActionEvent event) {
        setNode(store);

        StoreController controller = storeloader.<StoreController>getController();
        controller.textSearch();
    }

    @FXML
    private void exit(ActionEvent event) {
        try {
            //        Platform.exit();
            btnClose.getScene().getWindow().hide();
            Stage mainStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void onPurchases(ActionEvent event) {
        setNode(purchases);
        PurchasesViewController controller = purchaseLoder.<PurchasesViewController>getController();
        controller.textSearch();
    }

    @FXML
    private void onSalesReport(ActionEvent event) {
        setNode(salesReport);
        SalesReportViewController controller = salesReportLoder.<SalesReportViewController>getController();
        controller.textSearch();
    }

    @FXML
    private void onUsers(ActionEvent event) {
        setNode(users);
        UsersController controller = usersLoder.<UsersController>getController();
        controller.textSearch();
    }

    @FXML
    private void onSetting(ActionEvent event) {
        setNode(setting);
    }

}
