package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TripDetail extends AppCompatActivity {

    private TextView mtripname,mtotal;
    private Button mend;
    private ListView mmemberList;
    TripDbHelper db;
    Cursor cursor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey));

        mtripname=findViewById(R.id.tripname);
        mtotal=findViewById(R.id.total);
        mmemberList=findViewById(R.id.memberList);
        mend=findViewById(R.id.end);

        db=new TripDbHelper(this);
        cursor=db.getAllPerson();
        cursor.moveToFirst();
        String trip = cursor.getString(2);
        mtripname.setText(trip);

        List<String> lst = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String member = cursor.getString(1);
                String id = cursor.getString(0);
                lst.add(id+". "+member);
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lst);
        mmemberList.setAdapter(arrayAdapter);

        cursor = db.getAllExpense();
        int sum=0;
        if(cursor.moveToFirst()){
            do{
                Integer value = Integer.parseInt(cursor.getString(2));
                sum+=value;
            }while (cursor.moveToNext());
        }
        mtotal.setText("Total Trip Expense: Rs "+sum);

        mend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDropped = db.dropDatabase();
                if(isDropped){
                    Toast.makeText(TripDetail.this, "Trip ended successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT",true);
                    startActivity(intent);
                }else{
                    Toast.makeText(TripDetail.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}