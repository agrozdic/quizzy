package com.ftn.ac.rs.mobilne_2023.services;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.ftn.ac.rs.mobilne_2023.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserService {

    private static Map<String, User> users = new HashMap<>();

    static {
        UserService.getAll(result -> {
            for (int i = 1; i <= result.size(); i++) {
                Map<String, Object> userData = (Map<String, Object>) result.get(String.valueOf(i));
                int id = Integer.parseInt(userData.get("id").toString());
                String email = (String) userData.get("email");
                String username = (String) userData.get("username");
                String pass = (String) userData.get("password");

                User user = new User(id, email, username, pass);
                users.put(Integer.toString(user.getId()), user);
            }
        });
    }

    public static Map<String, Object> get(int id, final UserService.UserCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("user" + id)
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

    public static Map<String, Object> getAll(final UserService.UserCallback callback) {
        Map<String, Object> users = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists())
                                users.put(String.valueOf(document.getData().get("id")), document.getData());
                        }
                        callback.onSuccess(users);
                    } else {
                        Log.w(TAG, "Error getting documents", task.getException());
                    }
                });
        return users;
    }

    public static boolean addUser(int id, String email, String username, String password) {
        final boolean[] success = {false};
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("email", email);
        user.put("username", username);
        user.put("password", password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("user" + id)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written");
                    success[0] = true;
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        return success[0];
    }

    public boolean loginUser(String loginIdentifier, String password) {
        boolean success = false;
        boolean loginWithEmail = loginIdentifier.contains("@");

        for (int i = 1; i <= users.size(); i++) {
            User temp = (User) users.get(String.valueOf(i));
            Log.println(Log.INFO, "temp -> ", temp.getUsername() + " " + temp.getPassword());
            if ((loginWithEmail && temp.getEmail().equals(loginIdentifier) && temp.getPassword().equals(password))
                    || (temp.getUsername().equals(loginIdentifier) && temp.getPassword().equals(password))) {
                success = true;
            }
        }
        return success;
    }

    // callback interfejs zbog asinhronosti
    public interface UserCallback {
        void onSuccess(Map<String, Object> result);
    }
}
