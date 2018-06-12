package com.example.jonathan.sheteanotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static List<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    static ListView listView;
    SharedPreferences sharedPreferences;
    static Set<String> stringSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.jonathan.sheteanotes", Context.MODE_PRIVATE);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        listView = findViewById(R.id.notesView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                noteIntent.putExtra("noteId", position);
                startActivity(noteIntent);
            }
        });

        stringSet = sharedPreferences.getStringSet("notes", null);

        notes.clear();

        if (stringSet != null){

            notes.addAll(stringSet);

        } else {
            notes.add("Example Note");
            stringSet = new HashSet<>();
            stringSet.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes",stringSet).apply();
            arrayAdapter.notifyDataSetChanged();
        }



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                final int positionIn = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete")
                        .setMessage("Delete note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(positionIn);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();


                return true;
            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == R.id.newNote){

            notes.add("");
            if (stringSet == null){
                stringSet = new HashSet<>();
            }else {

                stringSet.clear();
            }
            stringSet.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", stringSet).apply();

            Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
            noteIntent.putExtra("noteId", notes.size() -1);
            startActivity(noteIntent);


            return true;
        }

        return super.onOptionsItemSelected(item);

    }




}
