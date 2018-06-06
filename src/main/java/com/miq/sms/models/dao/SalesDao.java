
package com.miq.sms.models.dao;

import com.miq.sms.models.vo.ProductsVo;
import javafx.collections.ObservableList;
import com.miq.sms.models.vo.SalesVo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

/**
 *
 * @author ali alshara
 */
public class SalesDao extends Dao implements DaoList<SalesVo> {

    private static SalesDao salesDao;

    private SalesDao() {

    }

    public static SalesDao getInstance() {
        if (salesDao == null) {
            salesDao = new SalesDao();
        }
        return salesDao;

    } 

    @Override
    public ObservableList<SalesVo> loadAll() throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductsVo productsVo = null;
        SalesVo salesVo=null;
        ObservableList<SalesVo> sales = FXCollections.observableArrayList();
        try {
            con = getConnection();
            String sql = "SELECT products.id, products.name, products.qty, products.qty_min, products.barcode,"
                    + " products.buy_price, products.sale_price_odd, products.sale_price_even, products.max_discount,"
                    + " products.exp_date, products.store_date, products.notes,"
                    + " sale_bill.id, sale_bill.qty, sale_bill.sale_price, sale_bill.discount, sale_bill.date,"
                    + " sale_bill.customer_name,sale_bill.user_name, sale_bill.notes, sale_bill.product_id"
                    + "  FROM products INNER JOIN sale_bill ON products.id = sale_bill.product_id";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //object FROM products
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
                //object from sales
                salesVo=new SalesVo();
                salesVo.setId(rs.getInt("sale_bill.id"));
                salesVo.setQty(rs.getInt("sale_bill.qty"));
                salesVo.setDate(rs.getDate("sale_bill.date"));
                salesVo.setSalePrice(rs.getFloat("sale_bill.sale_price"));
                salesVo.setDiscount(rs.getFloat("sale_bill.discount"));
                salesVo.setCustomerName(rs.getString("sale_bill.customer_name"));
                salesVo.setUserName(rs.getString("sale_bill.user_name"));
                salesVo.setNotes(rs.getString("sale_bill.notes"));
                salesVo.setProductsVo(productsVo);
                sales.add(salesVo);
//                System.err.println(salesVo.getId());
//                System.err.println(salesVo.getDiscount());
//                System.err.println(salesVo.getSalePrice());
//                System.err.println(" ...........");
//                System.err.println(salesVo.getProductsVo().getId());
//                System.err.println(salesVo.getProductsVo().getMaxDiscount());
//                System.err.println(salesVo.getProductsVo().getSalePriceEven());
                
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
//            e.printStackTrace();
        } finally {
            ps.close();
            rs.close();
            CloseConnection(con);
        }
        return sales;
    }

    @Override
    public int insert(SalesVo sv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isInsert = 0;
        try {
            con = getConnection();
            String sql = "INSERT INTO sale_bill ( sale_bill.id, sale_bill.qty, sale_bill.sale_price,sale_bill.discount, sale_bill.date,"
                    + " sale_bill.customer_name,sale_bill.user_name, sale_bill.notes, sale_bill.product_id) VALUES(?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, sv.getId());
            ps.setInt(2, sv.getQty());
            ps.setFloat(3, sv.getSalePrice());
            ps.setFloat(4, sv.getDiscount());
            ps.setDate(5, (Date) sv.getDate());
            ps.setString(6, sv.getCustomerName());
//            ps.setString(7, sv.getUserName());
            ps.setString(7, sv.getUser().getUserName());
//            ===================
            ps.setString(8, sv.getNotes());
            ps.setInt(9, sv.getProductsVo().getId());
            ps.executeUpdate();
            isInsert = 1;
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
        } finally {
            ps.close();
            CloseConnection(con);
        }
        return isInsert;
    }

    @Override
    public int update(SalesVo sv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isUpdate = 0;
        try {
            //sales.id,
            con = getConnection();
            String sql = "UPDATE sale_bill SET sale_bill.qty=?, sale_bill.sale_price=?, sale_bill.discount=?, sale_bill.date=?,"
                    + " sale_bill.customer_name=?,sale_bill.user_name=?, sale_bill.notes=?, sale_bill.product_id=? WHERE sale_bill.id=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, sv.getQty());
            ps.setFloat(2, sv.getSalePrice());
            ps.setFloat(3, sv.getDiscount());
            ps.setDate(4, (Date) sv.getDate());
            ps.setString(5, sv.getCustomerName());
            ps.setString(6, sv.getUser().getUserName());
            ps.setString(7, sv.getNotes());
            ps.setInt(8, sv.getProductsVo().getId());
            ps.setInt(9, sv.getId());
            ps.executeUpdate();
            isUpdate = 1;
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
        } finally {
            ps.close();
            CloseConnection(con);
        }
        return isUpdate;
    }

    @Override
    public int delete(SalesVo sv) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        int isDelete = 0;
        try {
            con = getConnection();
            String Sql = "DELETE FROM sale_bill WHERE id=?";
            ps = con.prepareStatement(Sql);
            ps.setInt(1, sv.getId());
            ps.executeUpdate();
            isDelete = 1;

        } catch (SQLException ex) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
        } finally {
            ps.close();
            CloseConnection(con);
        }
        return isDelete;
    }

    @Override
    public SalesVo getData(SalesVo t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SalesVo getDataById(int id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductsVo productsVo = null;
        SalesVo salesVo=null;
        try {
            con = getConnection();
            String sql = "SELECT products.id, products.name, products.qty, products.qty_min, products.barcode,"
                    + " products.buy_price, products.sale_price_odd, products.sale_price_even, products.max_discount,"
                    + " products.exp_date, products.store_date, products.notes,"
                    + " sale_bill.id, sale_bill.qty, sale_bill.sale_price, sale_bill.discount, sale_bill.date,"
                    + " sale_bill.customer_name,sale_bill.user_name, sale_bill.notes, sale_bill.product_id"
                    + "  FROM products INNER JOIN sale_bill ON products.id = sale_bill.product_id";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //object FROM products
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
                //object from sales
                salesVo=new SalesVo();
                salesVo.setId(rs.getInt("sale_bill.id"));
                salesVo.setQty(rs.getInt("sale_bill.qty"));
                salesVo.setDate(rs.getDate("sale_bill.date"));
                salesVo.setSalePrice(rs.getFloat("sale_bill.sale_price"));
                salesVo.setDiscount(rs.getFloat("sale_bill.discount"));
                salesVo.setCustomerName(rs.getString("sale_bill.customer_name"));
                salesVo.setUserName(rs.getString("sale_bill.user_name"));
                salesVo.setNotes(rs.getString("sale_bill.notes"));
                salesVo.setProductsVo(productsVo);
//                System.err.println(salesVo.getId());
//                System.err.println(" ...........");
//                System.err.println(salesVo.getProductsVo().getId());
                
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("خطأ");
                       alert.setHeaderText(ex.getMessage());
                       alert.setContentText(ex.toString());
                       alert.showAndWait();
//            e.printStackTrace();
        } finally {
            ps.close();
            rs.close();
            CloseConnection(con);
        }
        return salesVo;
    }

    @Override
    public SalesVo getDataByName(String name) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public int getLastSalesId(){
    ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        int id = 0;
        try {
            con = getConnection();
            String sql = "SELECT `id` FROM `sale_bill` ORDER BY `id` DESC LIMIT 1";
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
