package com.example.dumbbellsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    Database database;
    ListView lvTask;
    ArrayList<Task> arrTask;
    TaskAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lvTask = (ListView) findViewById(R.id.listViewTask);
        arrTask = new ArrayList<>();

        adapter = new TaskAdapter(this, R.layout.task_line, arrTask);
        lvTask.setAdapter(adapter);



        //initialize database todo
        database = new Database(this, "todo.sqlite", null, 1);
        //initialize table
        database.QueryData("CREATE TABLE IF NOT EXISTS Task(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50))");
        getData();


    }

    public void tickIt(final String name, final int id){
        database.QueryData("DELETE FROM task WHERE id = '"+id+"'");
        Toast.makeText(MainActivity2.this, "You ticked " + name +"!", Toast.LENGTH_SHORT).show();
        getData();
    }



    public void dialogEdit(String name, int id){
        Dialog dlgE = new Dialog(this);
        dlgE.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgE.setContentView(R.layout.dialog_edit_task);
        EditText editTask = (EditText) dlgE.findViewById(R.id.editTextEditTask);
        Button buttonConfirm = (Button) dlgE.findViewById(R.id.buttonConfirm);
        Button buttonCancelEdit = (Button) dlgE.findViewById(R.id.buttonCancelEdit);

        editTask.setText(name);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edited = editTask.getText().toString().trim();
                database.QueryData("UPDATE Task SET name = '"+ edited +"' WHERE id = '"+ id +"'");
                Toast.makeText(MainActivity2.this, "Task updated!", Toast.LENGTH_SHORT).show();
                dlgE.dismiss();
                getData();
            }
        });

        buttonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgE.dismiss();
            }
        });

        dlgE.show();
    }

    private void getData(){
        Cursor dataTask = database.GetData("SELECT * FROM Task");
        //Clear old data to avoid duplication
        arrTask.clear();
        while(dataTask.moveToNext()){
            String name = dataTask.getString(1); // 0 is Id
            //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            int id = dataTask.getInt(0);
            arrTask.add(new Task(id, name));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAddButton){
            dialogAdd();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogAdd(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_task);

        EditText editText = dialog.findViewById(R.id.addText);
        Button buttonAdd = (Button) dialog.findViewById(R.id.buttonAdd);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        dialog.setCanceledOnTouchOutside(false);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name.equals("")){
                    Toast.makeText(MainActivity2.this, "Empty task!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.QueryData("INSERT INTO Task VALUES(null, '"+name+"')");
                    Toast.makeText(MainActivity2.this, "Task added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getData();

                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}