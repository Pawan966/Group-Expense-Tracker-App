package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddExpense extends AppCompatActivity {

    TripDbHelper db;
    private Button maddexpense;
    private Spinner mspinner;
    private EditText mexpensetype,mamount;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey));

        maddexpense=findViewById(R.id.addexpense);
        mspinner=findViewById(R.id.spinner);
        mexpensetype=findViewById(R.id.expensetype);
        mamount=findViewById(R.id.amount);
        db=new TripDbHelper(this);

        ArrayList<String> nameList = initializeSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,nameList);
        mspinner.setAdapter(adapter);

        maddexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memSelected = mspinner.getSelectedItem().toString();
                String ExpenseType = mexpensetype.getText().toString();
                String amount = mamount.getText().toString();
                if(memSelected.length()==0 || ExpenseType.length()==0 || amount.length()==0){
                    Toast.makeText(AddExpense.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isInserted = db.insertDataExpense(ExpenseType,amount,memSelected.charAt(0)+"");
                if(isInserted)
                    Toast.makeText(AddExpense.this, "Expense Added", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddExpense.this, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public ArrayList<String> initializeSpinner(){
        ArrayList<String> list = new ArrayList<String>();
        try{
            Cursor res = db.getAllPerson();
            if(res.getCount()==0){
                return list;
            }else{
                while(res.moveToNext()){
                    String memId = res.getString(0);
                    String memName = res.getString(1);
                    list.add(memId+"."+memName);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}