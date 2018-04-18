package csc380Project.controllers;

import com.jfoenix.controls.JFXTextArea;
import csc380Project.game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;



public class QuestionPromptController{
    @FXML
    private JFXTextArea question_prompt;


    @FXML
    public void initialize() {
        question_prompt.setMouseTransparent(true);
    }
}