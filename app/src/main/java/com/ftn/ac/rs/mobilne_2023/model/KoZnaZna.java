package com.ftn.ac.rs.mobilne_2023.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class KoZnaZna {

    private String q1;
    private String q1a1;
    private String q1a2;
    private String q1a3;
    private String q1a4;

    private String q2;
    private String q2a1;
    private String q2a2;
    private String q2a3;
    private String q2a4;

    private String q3;
    private String q3a1;
    private String q3a2;
    private String q3a3;
    private String q3a4;

    private String q4;
    private String q4a1;
    private String q4a2;
    private String q4a3;
    private String q4a4;

    private String q5;
    private String q5a1;
    private String q5a2;
    private String q5a3;
    private String q5a4;

    private ArrayList<String> answers;

    public KoZnaZna(Map<String, Object> firestoreResult) {
        this.q1 = (String) firestoreResult.get("q1");
        this.q1a1 = (String) firestoreResult.get("q1a1");
        this.q1a2 = (String) firestoreResult.get("q1a2");
        this.q1a3 = (String) firestoreResult.get("q1a3");
        this.q1a4 = (String) firestoreResult.get("q1a4");

        this.q2 = (String) firestoreResult.get("q2");
        this.q2a1 = (String) firestoreResult.get("q2a1");
        this.q2a2 = (String) firestoreResult.get("q2a2");
        this.q2a3 = (String) firestoreResult.get("q2a3");
        this.q2a4 = (String) firestoreResult.get("q2a4");

        this.q3 = (String) firestoreResult.get("q3");
        this.q3a1 = (String) firestoreResult.get("q3a1");
        this.q3a2 = (String) firestoreResult.get("q3a2");
        this.q3a3 = (String) firestoreResult.get("q3a3");
        this.q3a4 = (String) firestoreResult.get("q3a4");

        this.q4 = (String) firestoreResult.get("q4");
        this.q4a1 = (String) firestoreResult.get("q4a1");
        this.q4a2 = (String) firestoreResult.get("q4a2");
        this.q4a3 = (String) firestoreResult.get("q4a3");
        this.q4a4 = (String) firestoreResult.get("q4a4");

        this.q5 = (String) firestoreResult.get("q5");
        this.q5a1 = (String) firestoreResult.get("q5a1");
        this.q5a2 = (String) firestoreResult.get("q5a2");
        this.q5a3 = (String) firestoreResult.get("q5a3");
        this.q5a4 = (String) firestoreResult.get("q5a4");

        String answersString = (String) firestoreResult.get("answers");
        this.answers = new ArrayList<>(Arrays.asList(answersString.split(";")));
    }

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public String getQ4() {
        return q4;
    }

    public void setQ4(String q4) {
        this.q4 = q4;
    }

    public String getQ5() {
        return q5;
    }

    public void setQ5(String q5) {
        this.q5 = q5;
    }

    public String getQ1a1() {
        return q1a1;
    }

    public void setQ1a1(String q1a1) {
        this.q1a1 = q1a1;
    }

    public String getQ1a2() {
        return q1a2;
    }

    public void setQ1a2(String q1a2) {
        this.q1a2 = q1a2;
    }

    public String getQ1a3() {
        return q1a3;
    }

    public void setQ1a3(String q1a3) {
        this.q1a3 = q1a3;
    }

    public String getQ1a4() {
        return q1a4;
    }

    public void setQ1a4(String q1a4) {
        this.q1a4 = q1a4;
    }

    public String getQ2a1() {
        return q2a1;
    }

    public void setQ2a1(String q2a1) {
        this.q2a1 = q2a1;
    }

    public String getQ2a2() {
        return q2a2;
    }

    public void setQ2a2(String q2a2) {
        this.q2a2 = q2a2;
    }

    public String getQ2a3() {
        return q2a3;
    }

    public void setQ2a3(String q2a3) {
        this.q2a3 = q2a3;
    }

    public String getQ2a4() {
        return q2a4;
    }

    public void setQ2a4(String q2a4) {
        this.q2a4 = q2a4;
    }

    public String getQ3a1() {
        return q3a1;
    }

    public void setQ3a1(String q3a1) {
        this.q3a1 = q3a1;
    }

    public String getQ3a2() {
        return q3a2;
    }

    public void setQ3a2(String q3a2) {
        this.q3a2 = q3a2;
    }

    public String getQ3a3() {
        return q3a3;
    }

    public void setQ3a3(String q3a3) {
        this.q3a3 = q3a3;
    }

    public String getQ3a4() {
        return q3a4;
    }

    public void setQ3a4(String q3a4) {
        this.q3a4 = q3a4;
    }

    public String getQ4a1() {
        return q4a1;
    }

    public void setQ4a1(String q4a1) {
        this.q4a1 = q4a1;
    }

    public String getQ4a2() {
        return q4a2;
    }

    public void setQ4a2(String q4a2) {
        this.q4a2 = q4a2;
    }

    public String getQ4a3() {
        return q4a3;
    }

    public void setQ4a3(String q4a3) {
        this.q4a3 = q4a3;
    }

    public String getQ4a4() {
        return q4a4;
    }

    public void setQ4a4(String q4a4) {
        this.q4a4 = q4a4;
    }

    public String getQ5a1() {
        return q5a1;
    }

    public void setQ5a1(String q5a1) {
        this.q5a1 = q5a1;
    }

    public String getQ5a2() {
        return q5a2;
    }

    public void setQ5a2(String q5a2) {
        this.q5a2 = q5a2;
    }

    public String getQ5a3() {
        return q5a3;
    }

    public void setQ5a3(String q5a3) {
        this.q5a3 = q5a3;
    }

    public String getQ5a4() {
        return q5a4;
    }

    public void setQ5a4(String q5a4) {
        this.q5a4 = q5a4;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
