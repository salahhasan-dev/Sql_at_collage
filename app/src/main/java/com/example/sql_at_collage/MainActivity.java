package com.example.sql_at_collage;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button add, delete, show, deleteAll, showFilter;
    ListView listview;
    EditText inputField;
    dbexample db;
    ArrayList<String> arrList;
    ArrayAdapter<String> arrAdapter;
    String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        add=findViewById(R.id.button);
        delete=findViewById(R.id.button2);
        show=findViewById(R.id.button3);
        inputField=findViewById(R.id.editTextText);
        listview=findViewById(R.id.ListView);
        db = new dbexample(MainActivity.this);
        deleteAll=findViewById(R.id.button4);
        showFilter = findViewById(R.id.button6);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insert(inputField.getText().toString());
                inputField.setText("");
                Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
            }
        });
        // if item is written or selected, then selected be that item
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = false;
                // if nothing is written and nothing is selected, then toast
                if(inputField.getText().toString().isEmpty() && selectedName==null){
                    Toast.makeText(MainActivity.this, "nothing to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if input field is not empty, then use that
                if(!inputField.getText().toString().isEmpty())
                {
                    check = db.delete(inputField.getText().toString());
                }
                // else if selected name is not empty, then use that
                else if(selectedName!=null)
                {
                    check = db.delete(selectedName);
                }

                Toast.makeText(MainActivity.this, "deleted " + check + " ", Toast.LENGTH_SHORT).show();
                selectedName = null;
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrList = new ArrayList<>();
                Cursor cursor = db.show();
                if(cursor==null){
                    Toast.makeText(MainActivity.this, "no data", Toast.LENGTH_SHORT).show();
                    arrAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrList);
                    listview.setAdapter(arrAdapter);
                    return;
                }

                while(cursor.moveToNext()){
                    arrList.add(cursor.getString(1));
                    arrAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrList);
                    listview.setAdapter(arrAdapter);
                }
                Toast.makeText(MainActivity.this, "shown", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAll();
                inputField.setText("");
                Toast.makeText(MainActivity.this, "deleted all data", Toast.LENGTH_SHORT).show();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = listview.getItemAtPosition(i).toString();
                selectedName = name;
                Toast.makeText(MainActivity.this, "selected: " + name, Toast.LENGTH_SHORT).show();
            }
        });

        showFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputField.getText().toString().isEmpty()){
                    return;
                }
                arrList = new ArrayList<>();
                String first_two_characters = inputField.getText().toString().substring(0, 2);
                Cursor cursor = db.show_filter(first_two_characters);

                while(cursor.moveToNext()){
                    arrList.add(cursor.getString(1));
                    arrAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrList);
                    listview.setAdapter(arrAdapter);
                }
                Toast.makeText(MainActivity.this, "shown", Toast.LENGTH_SHORT).show();
            }
        });
    }
}