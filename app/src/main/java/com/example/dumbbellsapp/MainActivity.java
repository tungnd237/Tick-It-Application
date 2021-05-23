package com.example.dumbbellsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SQLDBHandler sqlHander;
    private CalendarView calendar;
    private EditText textEdit;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;
    private Button todoList;
    private Button reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEdit = findViewById(R.id.EventText);
        calendar = findViewById(R.id.calendarView);
        todoList = findViewById(R.id.button2);
        todoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTodo();
            }
        });






        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                ShowData(view);
            }
        });

        try{
            sqlHander = new SQLDBHandler(this, "CalendarDatabase", null, 1);
            sqLiteDatabase = sqlHander.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EventCalendar(Date TEXT, Event TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void openTodo() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }


    public void InsertDatabase(View view){

        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", selectedDate);
        contentValues.put("Event", textEdit.getText().toString());

        sqLiteDatabase.insert("EventCalendar", null, contentValues);
    }

    public void ShowData(View view){

        String query = "Select Event from EventCalendar where Date =" + selectedDate;
        try{
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToLast();
            textEdit.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            textEdit.setText("");
        }
    }

}