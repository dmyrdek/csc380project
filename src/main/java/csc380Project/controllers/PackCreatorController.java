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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class PackCreatorController {

    @FXML
    JFXTextField new_pack_field;

    @FXML
    JFXComboBox existing_pack_combo_box;

    @FXML
    ListView questions_list;

    private static String createPackName;
    private static String existingPackName;
    private ArrayList<String> allExistingPacks = new ArrayList<>();
    private ObservableList questions = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws IOException {
        createPackName = PackSelectorController.getCreatePackName();
        existingPackName = PackSelectorController.getExistingPackName();

        if (!createPackName.equals("")){


        } else if (!existingPackName.equals("")){
            File existingPack = new File("src/main/resources/QuestionPacks/" + existingPackName + ".txt");
            
            try (BufferedReader reader = new BufferedReader(new FileReader(existingPack))) {

                String line;
                while ((line = reader.readLine()) != null)
                    questions.add(line);
        
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        questions_list.setItems(questions);

    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("PackSelector.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }
}