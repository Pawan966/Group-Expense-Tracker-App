package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyExpense extends AppCompatActivity {

    TripDbHelper db;
    private Button mupdateexpense;
    private Spinner mspinner1,mspinner2;
    private EditText mexpensevalue;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_expense);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey));

        db=new TripDbHelper(this);
        mupdateexpense=findViewById(R.id.updateexpense);
        mspinner1=findViewById(R.id.spinner1);
        mspinner2=findViewById(R.id.spinner2);
        mexpensevalue=findViewById(R.id.expensevalue);

        ArrayList<String> nameList = initializeSpinner1();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,nameList);
        mspinner1.setAdapter(adapter);

        mspinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String memSelected = mspinner1.getSelectedItem().toString();
                ArrayList<String> ExpenseList = initializeSpinner2(memSelected.charAt(0)+"");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyExpense.this,R.layout.spinner_layout,R.id.txt,ExpenseList);
                mspinner2.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayList<String> ExpenseList = initializeSpinner2("1");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyExpense.this,R.layout.spinner_layout,R.id.txt,ExpenseList);
                mspinner2.setAdapter(adapter);
            }
        });

        mspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String memSelected = mspinner1.getSelectedItem().toString();
                String expSelected = mspinner2.getSelectedItem().toString();
                Cursor cursor = db.getValueByExpenseNameAndId(expSelected,memSelected.charAt(0)+"");
                while(cursor.moveToNext()){
                    String Value = cursor.getString(2);
                    mexpensevalue.setHint("Current Value: Rs "+Value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        mupdateexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int items = mspinner2.getAdapter().getCount();
                String value = mexpensevalue.getText().toString();
                if(items==0){
                    Toast.makeText(ModifyExpense.this, "Data cannot be modified", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(value.length()==0){
                        Toast.makeText(ModifyExpense.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        String memSelected = mspinner1.getSelectedItem().toString();
                        String expSelected = mspinner2.getSelectedItem().toString();
                        boolean isUpdated = db.updateByIdAndExpense(expSelected,memSelected.charAt(0)+"",value);
                        if(isUpdated)
                            Toast.makeText(ModifyExpense.this, "Data modified", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ModifyExpense.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        mexpensevalue.setText("");
                        mexpensevalue.setHint("Current Value: Rs "+value);
                    }
                }
            }
        });
    }

    public ArrayList<String> initializeSpinner1(){
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

    public ArrayList<String> initializeSpinner2(String Id){
        ArrayList<String> list = new ArrayList<String>();
        try{
            Cursor res = db.getExpensebyId(Id);
            if(res.getCount()==0){
                mexpensevalue.setHint("Currently having no expense");
                return list;
            }else{
                while(res.moveToNext()){
                    String Expense = res.getString(1);
                    list.add(Expense);
                }
            }
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return list;
    }
}