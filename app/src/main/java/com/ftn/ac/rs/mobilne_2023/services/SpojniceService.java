package com.ftn.ac.rs.mobilne_2023.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SpojniceService {

    public static Map<String, Object> get(int id, final SpojniceService.SpojniceCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("spojnice").document("spojnice" + id)
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
    public interface SpojniceCallback {
        void onSuccess(Map<String, Object> result);
    }
}
