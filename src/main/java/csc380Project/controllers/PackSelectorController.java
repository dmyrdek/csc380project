package csc380Project.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;

public class PackSelectorController {

    @FXML
    JFXTextField new_pack_field;

    @FXML
    JFXComboBox existing_pack_combo_box;

    private static String createPackName = "";
    private static String existingPackName = "";
    private ArrayList<String> allExistingPacks = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        File f = new File("src/main/resources/QuestionPacks");

        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };
    
        File[] packs = f.listFiles(textFilter);
        for (File pack : packs) {
            allExistingPacks.add(pack.getName().substring(0, pack.getName().indexOf(".txt")));
        }

        for(String p : allExistingPacks){
            existing_pack_combo_box.getItems().add(p);
        }

    }

    public static String getCreatePackName(){
        return createPackName;
    }

    public static String getExistingPackName(){
        return existingPackName;
    }

    @FXML
    public void getCreatePackName(KeyEvent event) {
        createPackName = new_pack_field.getText();
        existingPackName = "";
        existing_pack_combo_box.getSelectionModel().clearSelection();
    }

    public void selectPack(ActionEvent event){
        new_pack_field.clear();
        createPackName = "";
    }

    public void nextButtonPress(ActionEvent event) throws IOException{
        if (!existing_pack_combo_box.getSelectionModel().isEmpty()){
            existingPackName = (String) existing_pack_combo_box.getSelectionModel().getSelectedItem();
            Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("PackCreator.fxml"));
            Scene homePage = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePage);
            appStage.show();
            appStage.requestFocus();
        } else if (!createPackName.equals("")){
            Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("PackCreator.fxml"));
            Scene homePage = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePage);
            appStage.show();
            appStage.requestFocus();
        }
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }
}