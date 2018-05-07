package csc380Project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class PackCreatorController {

    @FXML
    JFXTextArea question_editor;

    @FXML
    JFXComboBox existing_pack_combo_box;

    @FXML
    ListView questions_list;

    @FXML
    JFXButton save_button;

    private static String createPackName;
    private static String existingPackName;
    private ArrayList<String> allExistingPacks = new ArrayList<>();
    private ObservableList questions = FXCollections.observableArrayList();
    private String questionToEdit;
    private int quesitionToEditIndex;
    private boolean editingExistingQuestion = false;
    private String directoryAndName = "";

    @FXML
    public void initialize() throws IOException {
        createPackName = PackSelectorController.getCreatePackName();
        existingPackName = PackSelectorController.getExistingPackName();

        if (!createPackName.equals("")){
            directoryAndName = "src/main/resources/QuestionPacks/" + createPackName + ".txt";

        } else if (!existingPackName.equals("")){
            directoryAndName = "src/main/resources/QuestionPacks/" + existingPackName + ".txt";
            File existingPack = new File(directoryAndName);
            
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

    public void selectQuestion(MouseEvent event){
        editingExistingQuestion = true;
        questionToEdit = (String) questions_list.getSelectionModel().getSelectedItem();
        quesitionToEditIndex = questions_list.getSelectionModel().getSelectedIndex();
        question_editor.setText(questionToEdit);
    }

    public void submitQuestion(ActionEvent event){
        if (editingExistingQuestion){
            questions.set(quesitionToEditIndex, question_editor.getText() + "\n");
            question_editor.selectAll();
            question_editor.setText("");
            editingExistingQuestion = false;
        } else {
            questions.add(question_editor.getText() + "\n");
            question_editor.selectAll();
            question_editor.setText("");
        }
        save_button.setDisable(false);
    }

    public void saveQuestionPack(ActionEvent event) throws IOException{
        FileWriter writer = new FileWriter(directoryAndName); 
        for(String question : new ArrayList<String>(questions)) {
            writer.write(question);
        }
        writer.close();
        save_button.setDisable(true);
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