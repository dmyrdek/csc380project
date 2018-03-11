package csc380Project;

public class Answer {

    private String answer;
    Player submitter;

    public Answer(String a, Player p) {
        answer = a;
        submitter = p;
    }

    public String getAnswer(){
        return this.answer;
    }



}
