package com.android.memoriser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), noteeditor.class);
            startActivity(intent);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listviewmain = (ListView) findViewById(R.id.listviewmain);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.android.memoriser", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("ex note");
        } else{
            notes = new ArrayList(set);
        }

        notes.add("Note 1");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listviewmain.setAdapter(arrayAdapter);

        listviewmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), noteeditor.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });

        listviewmain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Delete this Note?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               notes.remove(position);
                               arrayAdapter.notifyDataSetChanged();
                               SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.android.memoriser", Context.MODE_PRIVATE);
                               HashSet<String> set = new HashSet(MainActivity.notes);
                               sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


    }
}