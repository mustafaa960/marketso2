package com.miq.sms.models.dao;

import javafx.collections.ObservableList;
import com.miq.sms.models.vo.ProductsVo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author ali alshara
 */
public class ProductsDao extends Dao implements DaoList<ProductsVo> {

    private static ProductsDao productsDao;

    private ProductsDao() {

    }

    public static ProductsDao getInstance() {
        if (productsDao == null) {
            productsDao = new ProductsDao();
        }
        return productsDao;

    }

    @Override
    public ObservableList<ProductsVo> loadAll() throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductsVo productsVo = null;
        ObservableList<ProductsVo> products = FXCollections.observableArrayList();
        try {
            con = getConnection();
            String sql = "SELECT products.[id], products.[name], products.[qty], products.[qty_min], products.[barcode], "
                    + "products.[buy_price], products.[sale_price_odd], products.[sale_price_even], products.[max_discount], products.[exp_date],"
                    + " products.[store_date], products.[notes] FROM products";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                productsVo = new ProductsVo();
                productsVo.setId(rs.getInt("id"));
                productsVo.setName(rs.getString("name"));
                productsVo.setQty(rs.getInt("qty"));
                productsVo.setQtyMin(rs.getInt("qty_min"));
                productsVo.setBarcode(rs.getString("barcode"));
                productsVo.setBuyPrice(rs.getFloat("buy_price"));
                productsVo.setSalePriceOdd(rs.getFloat("sale_price_odd"));
                productsVo.setSalePriceEven(rs.getFloat("sale_price_even"));
                productsVo.setMaxDiscount(rs.getFloat("max_discount"));
                productsVo.setExp_date(rs.getDate("exp_date"));
                productsVo.setStore_date(rs.getDate("store_date"));
                productsVo.setNotes(rs.getString("notes"));
                products.add(productsVo);
//                System.err.println(productsVo.getUserName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
//            e.printStackTrace();
        } finally {
            ps.close();
            rs.close();
            CloseConnection(con);
        }
        return products;
    }

    @Override
    public int insert(ProductsVo pv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isInsert = 0;
        try {
            con = getConnection();
            String sql = "INSERT INTO products ( id, name, qty, qty_min, barcode, buy_price, sale_price_odd, sale_price_even,"
                    + "max_discount, exp_date, store_date,notes ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, pv.getId());
            ps.setString(2, pv.getName());
            ps.setInt(3, pv.getQty());
            ps.setInt(4, pv.getQtyMin());
            ps.setString(5, pv.getBarcode());
            ps.setFloat(6, pv.getBuyPrice());
            ps.setFloat(7, pv.getSalePriceOdd());
            ps.setFloat(8, pv.getSalePriceEven());
            ps.setFloat(9, pv.getMaxDiscount());
            ps.setDate(10, (Date) pv.getExp_date());
            ps.setDate(11, (Date) pv.getStore_date());
            ps.setString(12, pv.getNotes());
            ps.executeUpdate();
            isInsert = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            ps.close();
            CloseConnection(con);
        }
        return isInsert;
    }

    @Override
    public int update(ProductsVo pv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isUpdate = 0;
        try {
            con = getConnection();
            String Sql = "UPDATE products SET name=?, qty=?, qty_min=?, barcode=?, buy_price=?, sale_price_odd=?,"
                    + " sale_price_even=?,max_discount=?, exp_date=?, store_date=?,notes=?  WHERE id=? ";
            ps = con.prepareStatement(Sql);
            ps.setString(1, pv.getName());
            ps.setInt(2, pv.getQty());
            ps.setInt(3, pv.getQtyMin());
            ps.setString(4, pv.getBarcode());
            ps.setFloat(5, pv.getBuyPrice());
            ps.setFloat(6, pv.getSalePriceOdd());
            ps.setFloat(7, pv.getSalePriceEven());
            ps.setFloat(8, pv.getMaxDiscount());
            ps.setDate(9, (Date) pv.getExp_date());
            ps.setDate(10, (Date) pv.getStore_date());
            ps.setString(11, pv.getNotes());
            ps.setInt(12, pv.getId());
            ps.executeUpdate();
            isUpdate = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            ps.close();
            CloseConnection(con);
        }

        return isUpdate;
    }

    @Override
    public int delete(ProductsVo pv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isDelete = 0;
        try {
            con = getConnection();
            String Sql = "DELETE FROM products WHERE id=?";
            ps = con.prepareStatement(Sql);
            ps.setInt(1, pv.getId());
            ps.executeUpdate();
            isDelete = 1;

        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            ps.close();
            CloseConnection(con);
        }
        return isDelete;
    }

    @Override
    public ProductsVo getData(ProductsVo t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductsVo getDataById(int id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductsVo productsVo = null;
        try {
            con = getConnection();
            String sql = "SELECT products.[id], products.[name], products.[qty], products.[qty_min], products.[barcode], "
                    + "products.[buy_price], products.[sale_price_odd], products.[sale_price_even], products.[max_discount],"
                    + " products.[exp_date], products.[store_date], products.[notes] FROM products WHERE products.[id]=? ";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                productsVo = new ProductsVo();
                productsVo.setId(rs.getInt("id"));
                productsVo.setName(rs.getString("name"));
                productsVo.setQty(rs.getInt("qty"));
                productsVo.setQtyMin(rs.getInt("qty_min"));
                productsVo.setBarcode(rs.getString("barcode"));
                productsVo.setBuyPrice(rs.getFloat("buy_price"));
                productsVo.setSalePriceOdd(rs.getFloat("sale_price_odd"));
                productsVo.setSalePriceEven(rs.getFloat("sale_price_even"));
                productsVo.setMaxDiscount(rs.getFloat("max_discount"));
                productsVo.setExp_date(rs.getDate("exp_date"));
                productsVo.setStore_date(rs.getDate("store_date"));
                productsVo.setNotes(rs.getString("notes"));
                System.err.println(productsVo.getId());
                System.err.println(productsVo.getName());
                System.err.println(productsVo.getSalePriceOdd());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
//            e.printStackTrace();
        } finally {
            ps.close();
            rs.close();
            CloseConnection(con);
        }
//        System.err.println(productsVo.getName());
        return productsVo;
    }

    @Override
    public ProductsVo getDataByName(String name) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getLastProductId(){
    ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        int id = 0;
        try {
            con = getConnection();
            String sql = "SELECT `id` FROM `products` ORDER BY `id` DESC LIMIT 1";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
//JOptionPane.showMessageDialog(null, ex.getMessage());   
        }
        return id;
    }
}
