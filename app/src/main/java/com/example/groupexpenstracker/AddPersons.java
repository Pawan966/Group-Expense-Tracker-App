package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddPersons extends AppCompatActivity {

    TripDbHelper db;
    private String tripname,grpsize;
    private int Size;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_persons);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange));

        db = new TripDbHelper(this);
        tripname = getIntent().getStringExtra("tripname");
        grpsize = getIntent().getStringExtra("grpsize");
        Size = Integer.parseInt(grpsize);
        AddPerson(1);
    }

    public void AddPerson(int i){
        final Button msubmitPerson = findViewById(R.id.submitPerson);
        if(i==Size){
            msubmitPerson.setText("Submit");
        }
        if(i>Size){
            return;
        }
        final int id = i;
        final TextView mpersonname = findViewById(R.id.personname);
        mpersonname.setHint(id+". Member Name");
        msubmitPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mpersonname.getText().length()==0) {
                    Toast.makeText(AddPersons.this, "Enter valid person name", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isInserted = db.insertDataPersons(mpersonname.getText().toString(),tripname);
                if(isInserted)
                    Toast.makeText(AddPersons.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddPersons.this, "Adding failed", Toast.LENGTH_SHORT).show();

                if(msubmitPerson.getText().equals("Submit")){
                    Intent intent = new Intent(AddPersons.this,HomeActivity.class);
                    startActivity(intent);
                }
                mpersonname.setText("");
                AddPerson(id+1);
            }
        });
    }
}