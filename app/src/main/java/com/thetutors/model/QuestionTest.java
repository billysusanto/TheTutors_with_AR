package com.thetutors.model;

/**
 * Created by billysusanto on 1/23/2016.
 */
public class QuestionTest {
    int no;
    String question;
    String answer;
    String multipleChoice;

    public QuestionTest(){
    }

    public QuestionTest(String question, String answer, String multipleChoice){
        this.question = question;
        this.answer = answer;
        this.multipleChoice = multipleChoice;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getQuestion(){
        return this.question;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return this.answer;
    }

    public void setMultipleChoice(String multipleChoice){
        this.multipleChoice = multipleChoice;
    }

    public String getMultipleChoice(){
        return this.multipleChoice;
    }

    public int getNo(){ return this.no;}

    public void setNo(int no) {
        this.no = no;
    }
}
