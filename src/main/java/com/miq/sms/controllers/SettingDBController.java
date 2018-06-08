package com.miq.sms.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author AL SAFIR
 */
public class SettingDBController implements Initializable {

    @FXML
    private JFXButton btnBackup;
    @FXML
    private JFXButton btnRestore;
    private String user = DashboardController.usersVo.getUserName();
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onBackup(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            Path sourceDir = Paths.get("C:\\MarketSo");
//            Path sourceDir = Paths.get("classes\\MarketSo");
            Path targetDir = Paths.get(selectedDirectory.getAbsolutePath());
            try {
                Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
//                 Notifications notificatioAdd = Notifications.create();
//                    notificatioAdd.title("تصدير");
//                    notificatioAdd.text("تم تصدير قاعدة البيانات بنجاح");
//                    notificatioAdd.graphic(null);
//                    notificatioAdd.hideAfter(Duration.seconds(5));
//                    notificatioAdd.position(Pos.TOP_CENTER);
//                    notificatioAdd.showInformation();
                Alert Deletalert = new Alert(Alert.AlertType.INFORMATION);
                Deletalert.setTitle("تصدير");
                Deletalert.setHeaderText("تم تصدير قاعدة البيانات بنجاح");
                
//        Deletalert.setContentText(ex.getMessage());
                Deletalert.showAndWait();
                // logs file  
                LoginController lc = new LoginController();
                lc.iniFile(user, "Backup Database");
            } catch (IOException ex) {
                Alert Deletalert = new Alert(Alert.AlertType.ERROR);
                Deletalert.setTitle("خطأ");
                Deletalert.setHeaderText("حدث خطأ لم يتم تصدير قاعدة البيانات ");
                Deletalert.setContentText(ex.getMessage());
                Deletalert.showAndWait();
                //   Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void onRestore(ActionEvent event) {
         final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                Path sourceDir = Paths.get(selectedDirectory.getAbsolutePath());
               // Path sourceDir2 = Paths.get("C:\\Users\\AL SAFIR\\Desktop\\New folder (4)\\Database\\db\\BodyBuilding.accdb");
                Path sourceDir2 = Paths.get(selectedDirectory.getAbsolutePath().concat("\\sms.accdb"));
               // Path sourceDir3 = Paths.get(selectedDirectory.getAbsolutePath().concat("\\Images trainees"));
               Path targetDir = Paths.get("C:\\MarketSo\\db");
//                Path targetDir2 = Paths.get("C:\\Body Building\\db\\sms.accdb");
                Path targetDir2 = Paths.get("C:\\MarketSo\\db\\sms.accdb");
//                Path targetDir2 = Paths.get("C:\\Program Files (x86)\\MarketSo\\classes\\db\\sms.accdb");
              //  Path targetDir3 = Paths.get("C:\\Body Building\\Database\\Images trainees");
              //  Path targetDir4 = Paths.get("C:\\Body Building\\Database\\bdfReports");
            try {
             //   Files.deleteIfExists(targetDir3);
              //  Files.deleteIfExists(targetDir4);
                Files.createDirectories(targetDir);
                Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
                byte[] b=Files.readAllBytes(sourceDir2);
               // Files.write(targetDir2, b);
                Files.write(targetDir2, b);
  
//                 Notifications notificatioAdd = Notifications.create();
//                    notificatioAdd.title("استيراد");
//                    notificatioAdd.text("تم استيراد قاعدة البيانات بنجاح");
//                    notificatioAdd.graphic(null);
//                    notificatioAdd.hideAfter(Duration.seconds(5));
//                    notificatioAdd.position(Pos.TOP_CENTER);
//                    notificatioAdd.showInformation();
                Alert Deletalert = new Alert(Alert.AlertType.INFORMATION);
                Deletalert.setTitle("استيراد");
                Deletalert.setHeaderText("تم استيراد قاعدة البيانات بنجاح");
//                Deletalert.setContentText(ex.getMessage());
                Deletalert.showAndWait();
                // logs file  
                LoginController lc = new LoginController();
                lc.iniFile(user, "Restore Database");
            } catch (IOException ex) {
        Alert Deletalert = new Alert(Alert.AlertType.ERROR);
        Deletalert.setTitle("تحذير");
        Deletalert.setHeaderText("حدث خطأ لم يتم استيراد قاعدة البيانات ");
        Deletalert.setContentText(ex.getMessage());
        Deletalert.showAndWait();
             //   Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                // System.err.println(ex);
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
                //  System.err.println(ex);
            }

            return FileVisitResult.CONTINUE;
        }

    }
}
