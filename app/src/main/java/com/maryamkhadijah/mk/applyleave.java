package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class applyleave extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyleave);

        Button btnapplyleave;
        //  EditText editTextTextPersonName;

        btnapplyleave = findViewById(R.id.btnapplyleave);





        btnapplyleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applyleave.this, "Legal Info", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), Applyleaveform.class );
                // i.putExtra("Value1", "DFP50283");
                // i.putExtra("Value2", "MAD");

                startActivity(i);
            }
        });



















    }
}