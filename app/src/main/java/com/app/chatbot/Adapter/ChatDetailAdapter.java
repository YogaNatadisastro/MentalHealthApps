package com.app.chatbot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.chatbot.Model.Chat;
import com.app.chatbot.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.MyViewHolder> {

    private List<Chat> chatList;
    private final Context context;

    public ChatDetailAdapter(Context context) {
        this.context = context;
        chatList = new ArrayList<>();
    }

    public void add(Chat chat){
        chatList.add(chat);
        notifyDataSetChanged();
    }

    public void clear(){
        chatList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailAdapter.MyViewHolder holder, int position) {
        Chat list = chatList.get(position);

        if (list.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.myChat.setText(list.getChat());
        } else {
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoChat.setText(list.getChat());
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout oppoLayout, myLayout;
        TextView oppoChat, myChat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoChat = itemView.findViewById(R.id.oppoChat);
            myChat = itemView.findViewById(R.id.myChat);
        }
    }
}
