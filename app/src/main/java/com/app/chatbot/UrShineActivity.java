package com.app.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.chatbot.Adapter.UrShineAdapter;

import com.app.chatbot.Model.UserDetails;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UrShineActivity extends AppCompatActivity {

    ArrayList<UserDetails> list;
    private RecyclerView messagesRv;
    private UrShineAdapter urShineAdapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ur_shine);

        list = new ArrayList<>();

        // Set Up Recycler View
        urShineAdapter = new UrShineAdapter(this, list);
        messagesRv = findViewById(R.id.rvDoctor);
        messagesRv.setAdapter(urShineAdapter);
        messagesRv.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                urShineAdapter.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();

                    if (!uid.equals(FirebaseAuth.getInstance().getUid())) {
                        UserDetails uDetail = dataSnapshot.getValue(UserDetails.class);
                            list.add(uDetail);
                    }
                    urShineAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}