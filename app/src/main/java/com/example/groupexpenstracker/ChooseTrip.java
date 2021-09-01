package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseTrip extends AppCompatActivity {

    TripDbHelper db;
    Cursor cursor;
    private Button madd;
    private EditText mtripname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_trip);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange));

        db=new TripDbHelper(this);
        cursor=db.getAllPerson();

        if(cursor.moveToFirst()){
            String tripName = cursor.getString(2);
            Intent intent = new Intent(ChooseTrip.this,HomeActivity.class);
            startActivity(intent);
            return;
        }

        madd=findViewById(R.id.add);
        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtripname=findViewById(R.id.tripname);
                String name=mtripname.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(ChooseTrip.this, "Enter Valid trip name", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChooseTrip.this, name+" is On!!!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ChooseTrip.this,GroupFormation.class);
                    intent.putExtra("tripname",name);
                    startActivity(intent);
                }
            }
        });
    }
}