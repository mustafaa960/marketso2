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
//class products value object
public class ProductsVo {

    //initialize
    private IntegerProperty id;
    private StringProperty name;
    private IntegerProperty qty;
    private IntegerProperty qtyMin;
    private StringProperty barcode;
    private FloatProperty buyPrice;
    private FloatProperty salePriceOdd;
    private FloatProperty salePriceEven;
    private FloatProperty maxDiscount;
    private SimpleObjectProperty<Date> exp_date;
    private SimpleObjectProperty<Date> store_date;
    private StringProperty notes;
//    private StringProperty userName;

    //constructor
    public ProductsVo() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.qty = new SimpleIntegerProperty();
        this.qtyMin = new SimpleIntegerProperty();
        this.barcode = new SimpleStringProperty();
        this.buyPrice = new SimpleFloatProperty();
        this.salePriceOdd = new SimpleFloatProperty();
        this.salePriceEven = new SimpleFloatProperty();
        this.maxDiscount = new SimpleFloatProperty();
        this.exp_date = new SimpleObjectProperty<>();
        this.store_date = new SimpleObjectProperty<>();
        this.notes = new SimpleStringProperty();
//        this.userName = new SimpleStringProperty();
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty NameProperty() {
        return name;
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
    public IntegerProperty QtyMinProperty() {
        return qtyMin;
    }

    public int getQtyMin() {
        return qtyMin.get();
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin.set(qtyMin);
    }

    public StringProperty BarcodeProperty() {
        return barcode;
    }

    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public FloatProperty BuyPriceProperty() {
        return buyPrice;
    }

    public float getBuyPrice() {
        return buyPrice.get();
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice.set(buyPrice);
    }

    public FloatProperty SalePriceOddProperty() {
        return salePriceOdd;
    }

    public float getSalePriceOdd() {
        return salePriceOdd.get();
    }

    public void setSalePriceOdd(float salePriceOdd) {
        this.salePriceOdd.set(salePriceOdd);
    }

    public FloatProperty SalePriceEvenProperty() {
        return salePriceEven;
    }

    public float getSalePriceEven() {
        return salePriceEven.get();
    }

    public void setSalePriceEven(float salePriceEven) {
        this.salePriceEven.set(salePriceEven);
    }

    public float getMaxDiscount() {
        return maxDiscount.get();
    }

    public void setMaxDiscount(float maxDiscount) {
        this.maxDiscount.set(maxDiscount);
    }

    public FloatProperty MaxDiscountProperty() {
        return maxDiscount;
    }

    public Object getExp_date() {
        return exp_date.get();
    }

    public void setExp_date(Date exp_date) {
        this.exp_date.set(exp_date);
    }

    public SimpleObjectProperty<Date> Exp_dateProperty() {
        return exp_date;
    }

    public Object getStore_date() {
        return store_date.get();
    }

    public void setStore_date(Date store_date) {
        this.store_date.set(store_date);
    }

    public SimpleObjectProperty<Date> Store_dateProperty() {
        return store_date;
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
//    public String getUserName() {
//        return userName.get();
//    }
//    
//    public void setUserName(String userName) {
//        this.userName.set(userName);
//    }
//    
//    public StringProperty UserNameProperty() {
//        return userName;
//    }

}
