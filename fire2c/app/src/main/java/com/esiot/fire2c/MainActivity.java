package com.esiot.fire2c;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mydb;
    TextView temp,hum, led;
    String leddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        temp = (TextView)findViewById(R.id.suhu);
        hum = (TextView)findViewById(R.id.kelembapan);
        hum = (TextView)findViewById(R.id.led);
        mydb= FirebaseDatabase.getInstance().getReference().child("Sensor");
        try {
            mydb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String tempdata = Objects.requireNonNull(dataSnapshot.child("suhu").getValue()).toString();
                    String humdata  = Objects.requireNonNull(dataSnapshot.child("kelembapan").getValue()).toString();
                    leddata  = Objects.requireNonNull(dataSnapshot.child("led").getValue()).toString();

                    temp.setText(tempdata);
                    hum.setText(humdata);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } catch(Exception ignored)
        {
        }
    }

    public void ubahNilaiLED(View view) {
        // Ubah nilai LED di database Firebase
        if (leddata == "0"){
            mydb.child("led").setValue("1");
        }else{
            mydb.child("led").setValue("0");
        }

    }
}