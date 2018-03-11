package csc380Project;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class QuestionPack {

    ArrayList<String> qArrayList;


    public QuestionPack() {
        qArrayList = new ArrayList<String>();

    }

    public boolean getQuestionPack(String aFileName) {
        File q = new File(aFileName);
        try {
            Scanner scanner = new Scanner(q);
            String line = "";
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                qArrayList.add(line);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("The file " + q.getPath() + " was not found.");
            return false;
        }
    }



    public QuestionPack addAllQuestions() {
        QuestionPack qp = new QuestionPack();
        qp.getQuestionPack("questions.txt");
        qp.getQuestionPack("questions2.txt");
        return qp;

    }

    public ArrayList<String> currentQuestions() {
        return qArrayList;
    }

}