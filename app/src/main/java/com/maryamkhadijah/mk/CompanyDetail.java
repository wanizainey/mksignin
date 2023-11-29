package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompanyDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        Button btninfo,btnCback;
        //  EditText editTextTextPersonName;

        btninfo = findViewById(R.id.btninfo);

        //  btn2 = findViewById(R.id.btnprofile);


        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CompanyDetail.this, "Legal Info", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), legalinfo.class );
                // i.putExtra("Value1", "DFP50283");
                // i.putExtra("Value2", "MAD");

                startActivity(i);
            }
        });




    }
}