package com.ftn.ac.rs.mobilne_2023.model;

import java.util.Map;

public class KorakPoKorak {

    private String korak1;
    private String korak2;
    private String korak3;
    private String korak4;
    private String korak5;
    private String korak6;
    private String korak7;
    private String resenje;

    public KorakPoKorak(Map<String, Object> firestoreResult){
        this.korak1 = (String) firestoreResult.get("korak1");
        this.korak2 = (String) firestoreResult.get("korak2");
        this.korak3 = (String) firestoreResult.get("korak3");
        this.korak4 = (String) firestoreResult.get("korak4");
        this.korak5 = (String) firestoreResult.get("korak5");
        this.korak6 = (String) firestoreResult.get("korak6");
        this.korak7 = (String) firestoreResult.get("korak7");
        this.resenje = (String) firestoreResult.get("resenje");
    }


    public String getKorak1() {
        return korak1;
    }

    public void setKorak1(String korak1) {
        this.korak1 = korak1;
    }

    public String getKorak2() {
        return korak2;
    }

    public void setKorak2(String korak2) {
        this.korak2 = korak2;
    }

    public String getKorak3() {
        return korak3;
    }

    public void setKorak3(String korak3) {
        this.korak3 = korak3;
    }

    public String getKorak4() {
        return korak4;
    }

    public void setKorak4(String korak4) {
        this.korak4 = korak4;
    }

    public String getKorak5() {
        return korak5;
    }

    public void setKorak5(String korak5) {
        this.korak5 = korak5;
    }

    public String getKorak6() {
        return korak6;
    }

    public void setKorak6(String korak6) {
        this.korak6 = korak6;
    }

    public String getKorak7() {
        return korak7;
    }

    public void setKorak7(String korak7) {
        this.korak7 = korak7;
    }

    public String getResenje() {
        return resenje;
    }

    public void setResenje(String resenje) {
        this.resenje = resenje;
    }
}
