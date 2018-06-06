package com.miq.sms.controllers;

import com.jfoenix.controls.JFXRippler;
import com.miq.sms.models.dao.BuyDao;
import com.miq.sms.models.dao.ProductsDao;
import com.miq.sms.models.dao.SalesDao;
import com.miq.sms.models.vo.BuyVo;
import com.miq.sms.models.vo.ProductsVo;
import com.miq.sms.models.vo.SalesVo;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class OverviewController implements Initializable {

    @FXML
    private HBox groupHolder;

    @FXML
    private Label labMostWanted;
    @FXML
    private StackPane stackPaneExpireNotify;
    @FXML
    private AnchorPane anchPaneQtyNotify;
    @FXML
    private Label lblTotalQty;
    @FXML
    private TableView<ProductsVo> tableProductsQty;
    @FXML
    private TableColumn<ProductsVo, String> colProductsName;
    @FXML
    private TableColumn<ProductsVo, Integer> colQty;
    @FXML
    private TableView<ProductsVo> tableExpProducts;
    @FXML
    private TableColumn<ProductsVo, String> colExpProductsName;
    @FXML
    private TableColumn<ProductsVo, Date> colExpDate;
    @FXML
    private Label lblSalesBill;
    @FXML
    private Label lblPurchasesBill;
    @FXML
    private Label lblProductsQTYFinish;
    @FXML
    private StackPane stackPaneMostWanted1;
    @FXML
    private TableView<SalesVo> tableProductsMaxSales;
    @FXML
    private Group groupProductsInStore;
    @FXML
    private Group groupSalesBill;
    @FXML
    private Group groupBuyBill;
    @FXML
    private Group groupProductsEndQty;
    @FXML
    private TableColumn<SalesVo, String> colProductsNameMore;

    @FXML
    private TableColumn<SalesVo, Integer> colProductsQtyDay;
    @FXML
    private TableColumn<SalesVo, Float> colProductsPriceDay;
    @FXML
    private Label lblTotalPrice;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setRipples();
        fillProductsExp();
        fillProductsQty();
    }

    private void setRipples() {
        JFXRippler ripplerPinS = new JFXRippler(groupProductsInStore);
        JFXRippler ripplerSB = new JFXRippler(groupSalesBill);
        JFXRippler ripplerBB = new JFXRippler(groupBuyBill);
        JFXRippler ripplerPEQ = new JFXRippler(groupProductsEndQty);
        ripplerPinS.setMaskType(JFXRippler.RipplerMask.RECT);
        ripplerSB.setMaskType(JFXRippler.RipplerMask.RECT);
        ripplerBB.setMaskType(JFXRippler.RipplerMask.RECT);
        ripplerPEQ.setMaskType(JFXRippler.RipplerMask.RECT);

        ripplerPinS.setRipplerFill(Paint.valueOf("#1564C0"));
        ripplerSB.setRipplerFill(Paint.valueOf("#F87951"));
        ripplerBB.setRipplerFill(Paint.valueOf("#00AACF"));
        ripplerPEQ.setRipplerFill(Paint.valueOf("#00B3A0"));

        groupHolder.getChildren().addAll(ripplerPinS, ripplerSB, ripplerBB, ripplerPEQ);
    }

    public void fillProductsExp() {
        try {
            tableExpProducts.getItems().clear();
            ObservableList<ProductsVo> products = ProductsDao.getInstance().loadAll();
            ObservableList<SalesVo> sales = SalesDao.getInstance().loadAll();
            ObservableList<SalesVo> salesMonth = FXCollections.observableArrayList();
            ObservableList<SalesVo> salesDay = FXCollections.observableArrayList();
            ObservableList<BuyVo> buy = BuyDao.getInstance().loadAll();
            ObservableList<BuyVo> BuyMonth = FXCollections.observableArrayList();
            ObservableList<ProductsVo> productsExp = FXCollections.observableArrayList();
            products.forEach((t) -> {
                if (((Date) t.getExp_date()).before(Date.valueOf(LocalDate.now().plusMonths(2)))) {
                    productsExp.add(t);
                }
            });
            colExpProductsName.setCellValueFactory(c -> c.getValue().NameProperty());
            colExpDate.setCellValueFactory(c -> c.getValue().Exp_dateProperty());
            tableExpProducts.setItems(productsExp);
            lblTotalQty.setText(String.valueOf(products.size()));
            // load sales bill
            sales.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(30)))) {
                    salesMonth.add(t);
                }
            });

            lblSalesBill.setText(String.valueOf(salesMonth.size()));
            // load Buy bill
            buy.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(30)))) {
                    BuyMonth.add(t);
                }
            });
            lblPurchasesBill.setText(String.valueOf(BuyMonth.size()));

            // load more in this month
//            ObservableList<SalesVo> salesMonthMore = FXCollections.observableArrayList();
//            salesMonth.forEach((t) -> {
//                if(!salesMonth.contains(t)){
//                    salesMonthMore.add(t);
//                }
//            });
//load sale by day
            sales.forEach((t) -> {
                if (((Date) t.getDate()).after(Date.valueOf(LocalDate.now().minusDays(1)))) {
                    salesDay.add(t);
                }
            });
            colProductsNameMore.setCellValueFactory(c -> c.getValue().getProductsVo().NameProperty());
            colProductsQtyDay.setCellValueFactory(c -> c.getValue().QtyProperty().asObject());
            colProductsPriceDay.setCellValueFactory(c -> c.getValue().SalePriceProperty().asObject());
            tableProductsMaxSales.setItems(salesDay);
            tableChanged();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    public void fillProductsQty() {
        try {
            tableProductsQty.getItems().clear();
            ObservableList<ProductsVo> products = ProductsDao.getInstance().loadAll();
            ObservableList<ProductsVo> productsEndQty = FXCollections.observableArrayList();
            products.forEach((t) -> {
                if (t.getQty() <= t.getQtyMin()) {
                    productsEndQty.add(t);
                }
            });
            colProductsName.setCellValueFactory(c -> c.getValue().NameProperty());
            colQty.setCellValueFactory(c -> c.getValue().QtyProperty().asObject());
            tableProductsQty.setItems(productsEndQty);

            // load product End qty
            ObservableList<ProductsVo> productsQty = ProductsDao.getInstance().loadAll();
            ObservableList<ProductsVo> productsQtyFinish = FXCollections.observableArrayList();
            productsQty.forEach((t) -> {
                if (t.getQty() == 0) {
                    productsQtyFinish.add(t);
                }
            });
            lblProductsQTYFinish.setText(String.valueOf(productsQtyFinish.size()));
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

        if (tableProductsMaxSales.getItems() != null) {
            totalPrice = 0;
            tableProductsMaxSales.getItems().forEach((item) -> {
                totalPrice += item.getSalePrice();
            });
            lblTotalPrice.setText(String.valueOf(totalPrice));
        } else {
            lblTotalPrice.setText("");
        }
    }
}
