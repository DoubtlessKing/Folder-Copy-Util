
package Copy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class CopyUtility extends Application {
    CopyFileVisitor cfv;
    String destPath;
    String srcPath;
    String fileExt;
    @Override
    public void start(Stage primaryStage){
        final Label srcLabel = new Label("None Selected");
        final Label destLabel = new Label("None Selected");
        final TextField search = new TextField();
        Button destBtn = new Button();
        destBtn.setText("Set Dest");
        destBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser destChooser = new DirectoryChooser();
                File destFile = destChooser.showDialog(null);
                if(destFile!=null){
                    destPath = destFile.getAbsolutePath();
                    destLabel.setText(destPath);
                }
            }
        });
        Button srcBtn = new Button();
        srcBtn.setText("Set Src");
        srcBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser srcChooser = new DirectoryChooser();
                File srcFile = srcChooser.showDialog(null);
                if(srcFile!=null){
                    srcPath = srcFile.getAbsolutePath();
                    srcLabel.setText(srcPath);
                }
            }
        });
        Button runBtn = new Button("Copy");
        runBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                fileExt = search.getText();
                cfv = new CopyFileVisitor(srcPath, destPath, fileExt);
                try {
                    Files.walkFileTree(Paths.get(srcPath), cfv); 
                } catch (IOException ex) {
                    Logger.getLogger(CopyUtility.class.getName()).log(Level.SEVERE, null, ex);
                }
                cfv.publishLog();
            }
        });
        GridPane root = new GridPane();
        root.add(new Label("Source Directory:"),1,1);
        root.add(srcLabel, 3, 1);
        root.add(srcBtn, 3, 2);
        root.add(new Label("Destination Directory:"), 1, 3);
        root.add(destLabel, 3,3);
        root.add(destBtn,3,4);
        root.add(new Label("File Extension Filter:"),1,5);
        root.add(search,3,5);
        root.add(runBtn,3,6);
        
        Scene scene = new Scene(root, 500, 150);
        
        primaryStage.setTitle("Folder Copy Utility");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
