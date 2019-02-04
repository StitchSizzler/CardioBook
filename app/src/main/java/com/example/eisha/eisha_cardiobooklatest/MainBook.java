package com.example.eisha.eisha_cardiobooklatest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.os.FileObserver.DELETE;

/**
 * Displays the entries
 * Can add more or exit the app
 * Select entry to edit
 */

public class MainBook extends AppCompatActivity {
    private static final String FILENAME = "file.sav"; // file to store entries
    private static final int NEW_ENTRY = 1 ; // decide what action
    private static final int EDIT_ENTRY = 2;// decide what action
    private int pos; // for adapter
    private ListView entryDetails;
    private ArrayAdapter<importantEntry> adapter;
    private ArrayList<importantEntry> entryList;


    /** when activity first created
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book);
        loadFromFile();

        entryList = new ArrayList<importantEntry>();
        entryDetails = (ListView) findViewById(R.id.oldEntries);

        loadFromFile();
        Button addButton = (Button) findViewById(R.id.addnew);
        Button exitButton = (Button) findViewById(R.id.Exit);

        // go to add new entry
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addIntent = new Intent(MainBook.this, NewEntry.class);
                startActivityForResult(addIntent, NEW_ENTRY);
            }
        });
        // exit app
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);

            }
        });
        // go to edit entry on click
        entryDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                pos = position;
                Intent edit = new Intent(MainBook.this, Entry.class);
                Bundle editbundle= new Bundle();
                importantEntry entry = (importantEntry) adapter.getItem(pos);
                editbundle.putSerializable("edit", entry);
                edit.putExtra("putedit", editbundle);
                startActivityForResult(edit,EDIT_ENTRY);
            }
        });

    }

    /**
     * when activity starts
     */
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<importantEntry>(this, R.layout.list_item, entryList);
        entryDetails.setAdapter(adapter);

    }

    /**
     * if result from previous activity
     * are fine, add the new entry and save the file
     * or delete the entry, or edit entry
     * depending on what is requested
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_ENTRY && resultCode == RESULT_OK)  {
            importantEntry anEntry = (importantEntry) data.getBundleExtra("result").getSerializable("putresult");
            entryList.add(anEntry);
            saveInFile();
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == EDIT_ENTRY && resultCode == DELETE)  {
            // delete entry
            entryList.remove(pos);
            saveInFile();
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == EDIT_ENTRY && resultCode == 0){
            importantEntry newEntry = (importantEntry) data.getBundleExtra("result").getSerializable("putresult");
            entryList.set(pos, newEntry);
            saveInFile();
            adapter.notifyDataSetChanged();

        }

    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(entryList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-09-19
            Type listType = new TypeToken<ArrayList<importantEntry>>() {
            }.getType();
            entryList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            entryList = new ArrayList<importantEntry>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }


    }
}