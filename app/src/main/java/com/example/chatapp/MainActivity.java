package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    private static int MAX_MESSAGE_LENGTH = 150;

    Button buttonSend;
    EditText editTextMessenger;
    RecyclerView messagesRecycler;

    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = findViewById(R.id.send_message);
        editTextMessenger = findViewById(R.id.message_input);
        messagesRecycler = findViewById(R.id.messages_recycler);

        messagesRecycler.setLayoutManager(new LinearLayoutManager(this));


        DataAdapter dataAdapter = new DataAdapter(this,messages);
        messagesRecycler.setAdapter(dataAdapter);

        buttonSend.setOnClickListener(view -> {
            String message = editTextMessenger.getText().toString();
            if (message.equals("")) {
                Toast.makeText(getApplicationContext(), "Введіть повідомлення!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (message.length() > MAX_MESSAGE_LENGTH) {
                Toast.makeText(getApplicationContext(), "Надто довге повідомлення!", Toast.LENGTH_SHORT).show();
                return;
            }
            myRef.push().setValue(message);
            editTextMessenger.setText("");
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg = snapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                messagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}