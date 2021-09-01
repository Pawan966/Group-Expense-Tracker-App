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

public class GroupFormation extends AppCompatActivity {

    private TextView mtextview1;
    private EditText mgrpsize;
    private Button msubmit;
    private String tripname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_formation);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange));

        tripname=getIntent().getStringExtra("tripname");
        mtextview1=findViewById(R.id.textview1);
        mtextview1.setText("Trip Name : "+tripname);

        mgrpsize=findViewById(R.id.grpsize);
        msubmit=findViewById(R.id.submit);

        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer i = Integer.parseInt(mgrpsize.getText().toString());
                    if (i < 2) {
                        Toast.makeText(GroupFormation.this, "Enter Valid group size", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(GroupFormation.this, AddPersons.class);
                        intent.putExtra("tripname", tripname);
                        intent.putExtra("grpsize", i.toString());
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Toast.makeText(GroupFormation.this, "Enter Valid group size", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}