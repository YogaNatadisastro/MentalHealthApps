package com.app.chatbot.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.chatbot.Login_user;
import com.app.chatbot.MainActivity;
import com.app.chatbot.Model.UserDetails;
import com.app.chatbot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    Button button;
    TextView tvName, tvEmail, tvDate;
    FirebaseUser user;
    FirebaseDatabase fData;
    DatabaseReference dReference;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        dReference = fData.getReference("Registered Users");

        button = viewRoot.findViewById(R.id.logout);
        tvName = viewRoot.findViewById(R.id.nameProfile);
        tvEmail = viewRoot.findViewById(R.id.emailProfile);
        tvDate = viewRoot.findViewById(R.id.dateOfBirth);
        user = auth.getCurrentUser();


        if (user == null) {

            Intent intent = new Intent(getActivity(), Login_user.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            tvEmail.setText(user.getEmail());
            showUserProfile(user);
        }

        button.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return viewRoot;
    }

    private void showUserProfile(FirebaseUser fUser) {
        String userId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                   String test = uDetail.getUserName();

                   tvName.setText(test);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}