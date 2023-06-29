package com.ftn.ac.rs.mobilne_2023.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class KorakPoKorakService {

    public static Map<String, Object> get(int id, final KorakPoKorakService.KorakPoKorakCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("korakpokorak").document("korakpokorak" + id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> retVal = document.getData();
                            try {
                                callback.onSuccess(retVal);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Map<String, Object> retVal = new HashMap<>();
                            retVal.put("error", null);
                            Log.println(Log.ERROR, "FIRESTORE", "Document doesn't exist");
                            try {
                                callback.onSuccess(retVal);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        Map<String, Object> retVal = new HashMap<>();
                        retVal.put("error", null);
                        Log.println(Log.ERROR, "FIRESTORE", "Task failed");
                        try {
                            callback.onSuccess(retVal);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        return null;
    }

    // callback interfejs zbog asinhronosti
    public interface KorakPoKorakCallback {
        void onSuccess(Map<String, Object> result) throws InterruptedException;
    }

}
