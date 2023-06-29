package com.ftn.ac.rs.mobilne_2023.model;

import java.util.ArrayList;
import java.util.Map;

public class Spojnice {

    private String prompt;

    private String pair1a;
    private String pair2a;
    private String pair3a;
    private String pair4a;
    private String pair5a;

    private String pair1b;
    private String pair2b;
    private String pair3b;
    private String pair4b;
    private String pair5b;

    private String key;
    private ArrayList<String> solutions = new ArrayList<>();

    public Spojnice(Map<String, Object> firestoreResult) {
        this.prompt = (String) firestoreResult.get("prompt");

        this.pair1a = (String) firestoreResult.get("pair1a");
        this.pair2a = (String) firestoreResult.get("pair2a");
        this.pair3a = (String) firestoreResult.get("pair3a");
        this.pair4a = (String) firestoreResult.get("pair4a");
        this.pair5a = (String) firestoreResult.get("pair5a");

        this.pair1b = (String) firestoreResult.get("pair1b");
        this.pair2b = (String) firestoreResult.get("pair2b");
        this.pair3b = (String) firestoreResult.get("pair3b");
        this.pair4b = (String) firestoreResult.get("pair4b");
        this.pair5b = (String) firestoreResult.get("pair5b");

        this.key = (String) firestoreResult.get("key");

        String[] temp = this.key.split(";");

        for (String tempString: temp) {
            String pairA = tempString.substring(0, 2);
            String pairB = tempString.substring(2);
            String solution = pairA + "->" + pairB;
            solutions.add(solution);
        }
    }

    public String getPrompt() {
        return prompt;
    }

    public String getPair1a() {
        return pair1a;
    }

    public String getPair2a() {
        return pair2a;
    }

    public String getPair3a() {
        return pair3a;
    }

    public String getPair4a() {
        return pair4a;
    }

    public String getPair5a() {
        return pair5a;
    }

    public String getPair1b() {
        return pair1b;
    }

    public String getPair2b() {
        return pair2b;
    }

    public String getPair3b() {
        return pair3b;
    }

    public String getPair4b() {
        return pair4b;
    }

    public String getPair5b() {
        return pair5b;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<String> getSolutions() {
        return solutions;
    }
}
