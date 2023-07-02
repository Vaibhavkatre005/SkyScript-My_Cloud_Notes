package com.example.mynotes;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static CollectionReference getCollectionReferenceForNote(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");

    }
    static String TimeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/YYYY").format(timestamp.toDate());
    }
}
