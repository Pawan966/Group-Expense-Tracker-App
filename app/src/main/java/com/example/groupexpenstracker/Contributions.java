package com.example.groupexpenstracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

import java.util.ArrayList;

public class Contributions extends AppCompatActivity {

    TripDbHelper db;
    Cursor cursor,mailcursor;
    ExpandingList expandingList;
    int totalSum=0;
    private TextView divAmt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributions);

        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey));

        expandingList = findViewById(R.id.calculateList);
        db = new TripDbHelper(getApplicationContext());
        cursor = db.getAllPerson();
        cursor.moveToLast();

        mailcursor=db.getAllPerson();
        mailcursor.moveToFirst();
        String mailtripname=mailcursor.getString(2);

        String memNum = cursor.getString(0);
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        cursor = db.getAllExpense();
        if(cursor.moveToFirst()){
            do{
                String value = cursor.getString(2);
                totalSum += Integer.parseInt(value);
            }while(cursor.moveToNext());
        }
        int eachToPay = totalSum/Integer.parseInt(memNum);
        divAmt = findViewById(R.id.div_amt);
        divAmt.setText("CONTRIBUTION: Rs "+eachToPay);

        cursor = db.getAllPerson();
        ArrayList<ArrayList<String>> Receiver = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> Doner = new ArrayList<ArrayList<String>>();

        if(cursor.moveToFirst()){
            do{
                ArrayList<String> dataItem = new ArrayList<String>();
                ArrayList<String> toWhom = new ArrayList<String>();
                String memId = cursor.getString(0);
                String memName = cursor.getString(1);
                dataItem.add(memId+"."+memName);
                toWhom.add(memId+"."+memName);
                Cursor innerCursor = db.getExpensebyId(memId);
                int amtPaid = 0;
                if(innerCursor.moveToFirst()){
                    do{
                        String value = innerCursor.getString(2);
                        amtPaid += Integer.parseInt(value);
                    }while (innerCursor.moveToNext());
                    dataItem.add(amtPaid+"");
                }else{
                    dataItem.add(0+"");
                }
                if(amtPaid>=eachToPay){
                    dataItem.add(0+"");
                    dataItem.add((amtPaid-eachToPay)+"");
                    toWhom.add((amtPaid-eachToPay)+"");
                    Receiver.add(toWhom);
                }else{
                    dataItem.add((eachToPay-amtPaid)+"");
                    dataItem.add(0+"");
                    toWhom.add((eachToPay-amtPaid)+"");
                    Doner.add(toWhom);
                }
                dataList.add(dataItem);
            }while (cursor.moveToNext());

            ArrayList<ArrayList<String>> toWhomList = new ArrayList<ArrayList<String>>();
            for(int j=0;j<Doner.size();j++){
                if(Integer.parseInt(Doner.get(j).get(1))==0)
                    continue;
                ArrayList<String> list = new ArrayList<String>();
                for(int k=0;k<Receiver.size();k++){
                    if(Integer.parseInt(Receiver.get(k).get(1))==0)
                        continue;
                    if(Integer.parseInt(Doner.get(j).get(1))==0)
                        break;
                    if(Integer.parseInt(Doner.get(j).get(1))>=Integer.parseInt(Receiver.get(k).get(1))){
                        int value = Integer.parseInt(Doner.get(j).get(1))-Integer.parseInt(Receiver.get(k).get(1));
                        Doner.get(j).set(1,value+"");
                        String str = "Pay Rs "+Receiver.get(k).get(1)+" to "+Receiver.get(k).get(0).substring(2);
                        Receiver.get(k).set(1,"0");
                        list.add(str);
                    }else{
                        int value = Integer.parseInt(Receiver.get(k).get(1))-Integer.parseInt(Doner.get(j).get(1));
                        String str = "Pay Rs "+Doner.get(j).get(1)+" to "+Receiver.get(k).get(0).substring(2);
                        Doner.get(j).set(1,"0");
                        Receiver.get(k).set(1,value+"");
                        list.add(str);
                    }
                }
                toWhomList.add(list);
            }
            ArrayList<ArrayList<String>> payList = new ArrayList<ArrayList<String>>();
            int k=0;
            for(int j=0;j<Integer.parseInt(memNum);j++){
                if(Integer.parseInt(dataList.get(j).get(2))==0){
                    ArrayList<String> demoList = new ArrayList<String>();
                    demoList.add("Don't have to pay anything");
                    payList.add(demoList);
                }else{
                    payList.add(toWhomList.get(k));
                    k++;
                }
            }
            for(int j=0;j<Integer.parseInt(memNum);j++){
                ExpandingItem item = expandingList.createNewItem(R.layout.expandable_calculate_list);
                ((TextView)item.findViewById(R.id.memName)).setText(dataList.get(j).get(0).substring(0,2)+"Member Name: "+dataList.get(j).get(0).substring(2));

                item.createSubItems(payList.get(j).size()+3);
                ((TextView)item.getSubItemView(0).findViewById(R.id.child_item)).setText("Amount Paid: Rs "+dataList.get(j).get(1));
                ((TextView)item.getSubItemView(1).findViewById(R.id.child_item)).setText("Amount To Pay: Rs "+dataList.get(j).get(2));
                ((TextView)item.getSubItemView(2).findViewById(R.id.child_item)).setText("Amount To Receive: Rs "+dataList.get(j).get(3));

                ArrayList<String> mailitems = new ArrayList<String>();
                for(int m=3;m<payList.get(j).size()+3;m++){
                    ((TextView)item.getSubItemView(m).findViewById(R.id.child_item)).setText(payList.get(j).get(m-3));
                    mailitems.add(payList.get(j).get(m-3));
                }

                String mailName=dataList.get(j).get(0).substring(2);
                String paidAmount=dataList.get(j).get(1);
                String amountToPay=dataList.get(j).get(2);
                String amountToReceive=dataList.get(j).get(3);
                String each=Integer.toString(eachToPay);
                String total=Integer.toString(totalSum);

                String message="Hey "+mailName +",";
                message+="\n" + "Hope Your " + mailtripname + " trip went very well.";
                message+="\n\n" + "Below is the summary of the trip expenditures :";
                message+="\n\n" + "Total Expenditure of the whole trip: Rs " + total;
                message+="\n" + "Individual Contribution: Rs " + each;
                message+= "\n\n" + "You Paid: Rs " + paidAmount;
                message+= "\n" + "You have to pay: Rs " + amountToPay + " more";
                message+= "\n" + "You have to receive: Rs " + amountToReceive;
                message+="\n";

                for(int i=0;i<mailitems.size();i++)
                    message+="\n" + mailitems.get(i);

                String finalMessage = message;
                item.findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Trip Report of " + mailtripname);
                        intent.putExtra(Intent.EXTRA_TEXT, finalMessage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });

                item.setIndicatorIconRes(R.drawable.emoji_people);
                switch (j%10){
                    case 0:
                        item.setIndicatorColorRes(R.color.pink);
                        break;
                    case 1:
                        item.setIndicatorColorRes(R.color.orange);
                        break;
                    case 2:
                        item.setIndicatorColorRes(R.color.yellow);
                        break;
                    case 3:
                        item.setIndicatorColorRes(R.color.lightBlue);
                        break;
                    case 4:
                        item.setIndicatorColorRes(R.color.violet);
                        break;
                    case 5:
                        item.setIndicatorColorRes(R.color.lightGreen);
                        break;
                    case 6:
                        item.setIndicatorColorRes(R.color.red);
                        break;
                    case 7:
                        item.setIndicatorColorRes(R.color.darkBlue);
                        break;
                    case 8:
                        item.setIndicatorColorRes(R.color.darkGreen);
                        break;
                    case 9:
                        item.setIndicatorColorRes(R.color.cyan);
                        break;
                }
            }

        }
    }
}