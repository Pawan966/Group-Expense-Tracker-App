package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseGraph extends AppCompatActivity {

    PieChart mpiechart;
    TripDbHelper db;
    Cursor initial_cursor,cursor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_graph);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey));

        mpiechart=findViewById(R.id.piechart);

        db=new TripDbHelper(this);
        initial_cursor=db.getExpenseType();

        Set<String> set = new HashSet<String>();
        if(initial_cursor.moveToFirst()){
            do{
                String ExpType = initial_cursor.getString(0);
                set.add(ExpType);
            }while (initial_cursor.moveToNext());
        }

        List<String> Expense_Type = new ArrayList<String>();
        List<Integer> Expense_Value = new ArrayList<Integer>();
        for(String ExpType : set){
            Cursor cursor = db.getExpenseTypeByName(ExpType);
            Expense_Type.add(ExpType);
            int sum = 0;
            if(cursor.moveToFirst()){
                do{
                    String expValue = cursor.getString(2);
                    sum = sum + Integer.parseInt(expValue);
                }while (cursor.moveToNext());
            }
            Expense_Value.add(sum);
        }

        String [] ExpenseType_Arr = new String[Expense_Type.size()];
        ExpenseType_Arr = Expense_Type.toArray(ExpenseType_Arr);
        Integer [] ExpenseValue_Arr = new Integer[Expense_Value.size()];
        ExpenseValue_Arr = Expense_Value.toArray(ExpenseValue_Arr);

        ArrayList<PieEntry> records=new ArrayList<>();
        for(int i=0;i<ExpenseType_Arr.length;i++){
            records.add(new PieEntry(ExpenseValue_Arr[i],ExpenseType_Arr[i]));
        }

        cursor = db.getAllPerson();
        cursor.moveToFirst();
        String tripName = cursor.getString(2);

        PieDataSet pieDataSet = new PieDataSet(records,"Value in Rupees");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(Color.YELLOW);

        mpiechart.setTransparentCircleRadius(61f);
        mpiechart.setData(pieData);
        Legend legend = mpiechart.getLegend();
        legend.setTextSize(15f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        Description description = new Description();
        description.setText("Expenses of: "+tripName);
        description.setTextSize(35);
        description.setPosition(800,1700);
        mpiechart.setDescription(description);
        mpiechart.animateY(1000, Easing.EaseInOutCubic);
        mpiechart.invalidate();
    }
}