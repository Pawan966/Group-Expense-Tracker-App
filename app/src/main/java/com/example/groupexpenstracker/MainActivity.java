package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {

    TextView mappname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getBooleanExtra("EXIT",false)){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.teal_700));

        mappname=findViewById(R.id.appname);
        YoYo.with(Techniques.FadeInRight).duration(3000).repeat(0).playOn(mappname);

        Thread t=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    Intent intent=new Intent(MainActivity.this,ChooseTrip.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        t.start();
    }
}