package com.example.auth1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turn extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText tFullName, tEmail, tPhone, tLocation, tCi;
    Spinner tHour;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    String userId;
    DatabaseReference fDatabase;
    String selectedI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("Email");
        String phone = data.getStringExtra("Phone");


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user= fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        loadHours();

        tFullName = findViewById(R.id.profileFullName);
        tEmail = findViewById(R.id.profileEmailAddress);
        tPhone = findViewById(R.id.profilePhoneNo);
        tLocation = findViewById(R.id.profileLocation);
        tCi = findViewById(R.id.profileDni);
        tHour =findViewById(R.id.spinnerTime);
        saveBtn = findViewById(R.id.saveTicket);


      saveBtn.setOnClickListener((v) -> {
          String Ci = tCi.getText().toString().trim();
          String Loc = tLocation.getText().toString();
          String Hour = selectedI;

          //FirebaseUser fuser = fAuth.getCurrentUser();
          userId = fAuth.getCurrentUser().getUid();
          DocumentReference documentReference = fStore.collection("tickets").document(userId);
          Map<String, Object> ticket = new HashMap<>();
          ticket.put("CI", Ci);
          ticket.put("Location", Loc);
          ticket.put("Hour", Hour);
          documentReference.set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  Log.d(TAG, "onSuccess: user ticket is created for "+ userId);
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.d(TAG, "onFailure: " + e.toString());
              }
          });
          startActivity(new Intent(getApplicationContext(),MainActivity.class));


      });




    }


    public void loadHours(){
        final List<Horario> horas = new ArrayList<>();
        fDatabase.child("Horarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey();
                        String hour = ds.child("hora").getValue().toString();
                        String state = ds.child("estado").getValue().toString();
                        horas.add(new Horario(id, hour, state));
                    }
                    ArrayAdapter<Horario> arrayAdapter  = new ArrayAdapter<>(Turn.this, android.R.layout.simple_dropdown_item_1line, horas);
                    tHour.setAdapter(arrayAdapter);
                    tHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedI = parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}