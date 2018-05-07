package csc380Project.game;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class QuestionPack {

    ArrayList<String> qArrayList;

    public ArrayList<String> getQArrayList(){
        return qArrayList;
    }


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
        //qp.getQuestionPack("questions2.txt");
        return qp;
    }

    public QuestionPack addFromFile(String fileName) {
        QuestionPack qp = new QuestionPack();
        qp.addAllQuestions();

        File q = new File(fileName);
        try {
            Scanner scanner = new Scanner(q);
            String line = "";
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                //if first line matches dont add the questions from the file because its probably a duplicated
                if (line.equals(qp.qArrayList.get(0))) {
                    break;
                }
                qp.qArrayList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("OOOOppppps imn sawwwwrryryyyy couldnt find the file");
            e.printStackTrace();
        }

        return qp;
    }

    public ArrayList<String> currentQuestions() {
        return qArrayList;
    }

}