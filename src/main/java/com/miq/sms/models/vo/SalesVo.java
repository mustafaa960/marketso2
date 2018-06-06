package com.miq.sms.models.vo;

import java.sql.Date;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Mustafa
 */
//class Items value object
public class SalesVo {

    //initialize
    private IntegerProperty id;
    private IntegerProperty qty;
//    private StringProperty barcode;
    private FloatProperty salePrice;
    private FloatProperty discount;
    private SimpleObjectProperty<Date> date;
    private StringProperty notes;
    private StringProperty customerName;
    private StringProperty userName;
    //initilaze object from Items
    private ProductsVo productsVo;

    private UsersVo user;

    public UsersVo getUser() {
        return user;
    }

    public void setUser(UsersVo user) {
        this.user = user;
    }
    //constructor
    public SalesVo() {
        this.id = new SimpleIntegerProperty();
        this.qty = new SimpleIntegerProperty();
//        this.barcode = new SimpleStringProperty();
        this.salePrice = new SimpleFloatProperty();
        this.discount = new SimpleFloatProperty();
        this.date = new SimpleObjectProperty<>();
        this.notes = new SimpleStringProperty();
        this.customerName = new SimpleStringProperty();
        this.userName = new SimpleStringProperty();
    }

    //getter and setter and property methods
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty IdProperty() {
        return id;
    }
    
    public IntegerProperty QtyProperty() {
        return qty;
    }

    public int getQty() {
        return qty.get();
    }

    public void setQty(int qty) {
        this.qty.set(qty);
    }

//    public StringProperty BarcodeProperty() {
//        return barcode;
//    }
//
//    public String getBarcode() {
//        return barcode.get();
//    }
//
//    public void setBarcode(String barcode) {
//        this.barcode.set(barcode);
//    }

    public FloatProperty SalePriceProperty() {
        return salePrice;
    }

    public float getSalePrice() {
        return salePrice.get();
    }

    public void setSalePrice(float salePrice) {
        this.salePrice.set(salePrice);
    }
    
    public float getDiscount() {
        return discount.get();
    }

    public void setDiscount(float discount) {
        this.discount.set(discount);
    }

    public FloatProperty DiscountProperty() {
        return discount;
    }

    public Object getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<Date> DateProperty() {
        return date;
    }

    public StringProperty NotesProperty() {
        return notes;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public StringProperty CustomerNameProperty() {
        return customerName;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty UserNameProperty() {
        return userName;
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public ProductsVo getProductsVo() {
        return productsVo;
    }

    public void setProductsVo(ProductsVo productsVo) {
        this.productsVo = productsVo;
    }

}
