package com.miq.sms.models.dao;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author Mustafa
 */
//calss database access object
public class Dao {

    public Connection getConnection() {
        Path sourceDir = Paths.get("classes\\db");
//        Path targetDir = Paths.get("classes\\MarketSo\\db");
        Path targetDir = Paths.get("C:\\MarketSo\\db");
        String msAccDB = "";
        if (!Files.exists(targetDir)) {
            try {
                Files.createDirectories(targetDir);
                Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
                // msAccDB = "classes\\db\\BodyBuilding.accdb";
            } catch (IOException ex) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText(ex.getMessage());
                alert.setContentText(ex.toString());
                alert.showAndWait();
            }

        }
        // variables
        Connection connection = null;
        // Step 1: Loading or registering Oracle JDBC driver class
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException cnfex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setHeaderText("Problem in loading or registering MS Access JDBC driver");
                alert.setContentText(cnfex.getMessage());
                alert.showAndWait();
//            System.out.println("Problem in loading or registering MS Access JDBC driver");
//            cnfex.printStackTrace();
        }
        // Step 2: Opening database connection
        try {
//            String msAccDB = "..\\marketso\\src\\main\\resources\\db\\sms.accdb";
            msAccDB = "C:\\MarketSo\\db\\sms.accdb";
//             msAccDB = "C:\\MarketSo\\db\\sms.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB;
//            String dbURL = "jdbc:ucanaccess://" + msAccDB+";jackcessOpener=com.miq.sms.models.dao.CryptCodecOpener";
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);
//            connection = DriverManager.getConnection(dbURL,"mustafa","11aa11aa@@");
            if (connection != null) {
//                JOptionPane.showMessageDialog(null, "connected");
                return connection;
            }

        } catch (SQLException sqlex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText("حدث خطأ في الاتصال بقاعدة البيانات");
            alert.setContentText(sqlex.getMessage());
            alert.showAndWait();
//            JOptionPane.showMessageDialog(null, "حدث خطأ في الاتصال بقاعدة البيانات");

        }

        return null;
    }

    public void CloseConnection(Connection con) throws Exception {

        // Step 3: Closing database connection
        try {
            if (con != null) {

                con.close();

            }
        } catch (SQLException sqlex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setHeaderText(sqlex.getMessage());
            alert.setContentText(sqlex.toString());
            alert.showAndWait();
        }
    }

    public class CopyDir extends SimpleFileVisitor<Path> {

        private Path sourceDir;
        private Path targetDir;

        public CopyDir(Path sourceDir, Path targetDir) {
            this.sourceDir = sourceDir;
            this.targetDir = targetDir;
        }

        @Override
        public FileVisitResult visitFile(Path file,
                BasicFileAttributes attributes) {

            try {
                Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                Files.copy(file, targetFile);
            } catch (IOException ex) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("خطأ");
//                alert.setHeaderText(ex.getMessage());
//                alert.setContentText(ex.toString());
//                alert.show();
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                BasicFileAttributes attributes) {
            try {
                Path newDir = targetDir.resolve(sourceDir.relativize(dir));
                Files.createDirectory(newDir);
            } catch (IOException ex) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("خطأ");
//                alert.setHeaderText(ex.getMessage());
//                alert.setContentText(ex.toString());
//                alert.show();
            }

            return FileVisitResult.CONTINUE;
        }

    }
}
