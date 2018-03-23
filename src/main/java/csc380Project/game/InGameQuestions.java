package csc380Project.game;

import java.util.ArrayList;

public class InGameQuestions {

    private String[] questions;

    public String[] getQuestions(){
        return questions;
    }

    public InGameQuestions(int numQuestions){

        questions = new String [numQuestions];
    }

}