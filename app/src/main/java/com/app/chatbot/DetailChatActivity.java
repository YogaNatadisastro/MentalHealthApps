package com.app.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.chatbot.Adapter.ChatDetailAdapter;
import com.app.chatbot.Model.Chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DetailChatActivity extends AppCompatActivity {

    TextView tvNameDoc;
    String namaDoc;
    EditText chatEditText;
    Button sendBtn;
    ImageView infoBtn;
    RecyclerView rvChat;
    ChatDetailAdapter detailChatAdapter;
    DatabaseReference referenceSender, referenceReceiver;
    private String senderRoom, receiverRoom, receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Get Button id
        sendBtn = findViewById(R.id.btSendDoc);
        infoBtn = findViewById(R.id.btnInfo);
        chatEditText = findViewById(R.id.EditChatDoc);

        receiverId = getIntent().getStringExtra("uid");
        // Get Room id
        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        // Nama Profile
        namaDoc = getIntent().getStringExtra("nama");
        tvNameDoc = findViewById(R.id.tvNameDokter);
        tvNameDoc.setText(namaDoc);

        // Set Revycler View
        rvChat = findViewById(R.id.rvChatDoc);
        rvChat.setHasFixedSize(true);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        detailChatAdapter = new ChatDetailAdapter(this);
        rvChat.setAdapter(detailChatAdapter);

        referenceSender = FirebaseDatabase.getInstance().getReference("chat").child(senderRoom);
        referenceReceiver = FirebaseDatabase.getInstance().getReference("chat").child(receiverRoom);

        referenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detailChatAdapter.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat  = dataSnapshot.getValue(Chat.class);
                    detailChatAdapter.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(view -> {
            final String getTxtChat = chatEditText.getText().toString();
            // get current timestamp
            final String chatId = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            Chat chat = new Chat(chatId, FirebaseAuth.getInstance().getUid(), getTxtChat);

            detailChatAdapter.add(chat);
            referenceSender.child(chatId).setValue(chat);
            referenceReceiver.child(chatId).setValue(chat);

        });

        infoBtn.setOnClickListener(view ->
                startActivity(new Intent(DetailChatActivity.this, ProfileDoctorActivity.class))
        );
    }
}