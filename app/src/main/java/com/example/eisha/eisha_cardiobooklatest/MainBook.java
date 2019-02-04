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


public class MainBook extends AppCompatActivity {
    private static final String FILENAME = "file.sav"; // Create the file to save
    private static final int NEW_ENTRY = 1 ; //Create the request code
    private static final int EDIT_ENTRY = 2;// another request code
    private int pos; // the postion of the selected item
    private ListView entryDetails;
    private ArrayAdapter<importantEntry> adapter;
    private ArrayList<importantEntry> entryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book);
        loadFromFile();

        entryList = new ArrayList<importantEntry>(); // create the list view and the array
        entryDetails = (ListView) findViewById(R.id.oldEntries);

        loadFromFile(); // load the file to display the old data
        Button addButton = (Button) findViewById(R.id.addnew); // build buttons
        Button exitButton = (Button) findViewById(R.id.Exit);

        // set up the click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addIntent = new Intent(MainBook.this, NewEntry.class);
                //set up the intent if the user click the add button, it will be called
                startActivityForResult(addIntent, NEW_ENTRY);
            }
        });
        // set up the click listener for exit button
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);

            }
        });
        // set up the list view locator to locate the item user selected
        entryDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                pos = position; // get the item position
                Intent edit = new Intent(MainBook.this, Entry.class); // set up the new intent
                Bundle editbundle= new Bundle(); // set up the new bundle
                importantEntry entry = (importantEntry) adapter.getItem(pos);// get the counter user selected
                editbundle.putSerializable("edit", entry); // put the counter into bundle
                edit.putExtra("putedit", editbundle);
                startActivityForResult(edit,EDIT_ENTRY); // start the edit activity
            }
        });

    }
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();// load the old counters list
        adapter = new ArrayAdapter<importantEntry>(this, R.layout.list_item, entryList); // set up the adapter for the list view
        entryDetails.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get the result from activity
        if (requestCode == NEW_ENTRY && resultCode == RESULT_OK)  {
            // the result from the add activity and the result is ok
            importantEntry anEntry = (importantEntry) data.getBundleExtra("result").getSerializable("putresult");
            // get the new counter that passed from the add activity
            entryList.add(anEntry);  // add the new counter into the list
            saveInFile(); // save the list into the file
            adapter.notifyDataSetChanged();// chaneg the list view
        }
        else if (requestCode == EDIT_ENTRY && resultCode == DELETE)  {
            // the result from edit activity and the action is delete
            entryList.remove(pos); // remove the selected counter
            saveInFile();// save the list
            adapter.notifyDataSetChanged();// change the list view
        }
        else if (requestCode == EDIT_ENTRY && resultCode == 0){
            // the result from edit activity and the action is update
            importantEntry newEntry = (importantEntry) data.getBundleExtra("result").getSerializable("putresult");// get the new counter that passed from the add activity
            entryList.set(pos, newEntry);//  update the selected counter
            saveInFile(); // save the file
            adapter.notifyDataSetChanged(); // notice the list view to change

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