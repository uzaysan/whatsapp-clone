package com.uzaysan.whatsappclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements EventListener<QuerySnapshot> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollectionReference chatRef = FirebaseFirestore.getInstance().collection("Chats");
        chatRef.addSnapshotListener(this);

    }


    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            Log.e("Firebase Error: ",error.getMessage());
            return;
        }
        Toast.makeText(this, "Update recieved", Toast.LENGTH_SHORT).show();
        for (QueryDocumentSnapshot qds : value) {
            Log.e("Firebase Object: ", qds.toString());
        }

    }
}