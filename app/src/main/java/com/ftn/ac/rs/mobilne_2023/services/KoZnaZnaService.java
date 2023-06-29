package com.ftn.ac.rs.mobilne_2023.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class KoZnaZnaService {

    public static Map<String, Object> get(int id, final KoZnaZnaService.KoZnaZnaCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("koznazna").document("koznazna" + id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> retVal = document.getData();
                            callback.onSuccess(retVal);
                        } else {
                            Map<String, Object> retVal = new HashMap<>();
                            retVal.put("error", null);
                            Log.println(Log.ERROR, "FIRESTORE", "Document doesn't exist");
                            callback.onSuccess(retVal);
                        }
                    } else {
                        Map<String, Object> retVal = new HashMap<>();
                        retVal.put("error", null);
                        Log.println(Log.ERROR, "FIRESTORE", "Task failed");
                        callback.onSuccess(retVal);
                    }
                });
        return null;
    }

    // callback interfejs zbog asinhronosti
    public interface KoZnaZnaCallback {
        void onSuccess(Map<String, Object> result);
    }
}
