/*
 *
 * Class Name: MainActivity
 *
 * Version : Version 1.0
 *
 * Date: October 2, 2017
 *
 * Copyright 2017 Eshna Sengupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eshna.eshna_CounterBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Displays a list of counters along with a summary of their details
 *
 * @author eshna
 * @version 1.0
 * @see ActivityCounter
 * @see ViewCounterDetails
 * @see CounterInfo
 * @since 1.0
 */


public class MainActivity extends AppCompatActivity {
    private ListView counterList;
    private ArrayList<CounterInfo> counterInfoArrayList;
    private ArrayAdapter<CounterInfo> adapter;

    /**
     * method is called on activity creation
     *
     * @param savedInstanceState saves state of application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterList =(ListView) findViewById(R.id.counter_list);
        registerForContextMenu(counterList);

        //When item in a list is clicked
        counterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //sending information from MainActivity to ViewCounterDetails
                Intent intent = new Intent(view.getContext(),ViewCounterDetails.class);
                CounterInfo selection = counterInfoArrayList.get(position);
                Bundle bundle = selection.sendBundle();

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     *Creates a context menu
     *
     * @param menu The context menu that is being built
     * @param v The view for which the context menu is being built
     * @param menuInfo  Extra information about the item for which the context menu should be shown
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     *Called when a context menu item is selected
     *
     * @param item The context menu item that was selected.
     * @return boolean Return false to allow normal context menu processing to proceed,
     *         true to consume it here.
     */

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            //dit selected counter
            case R.id.edit_id:
                Intent intent = new Intent(this, ActivityCounter.class);
                CounterInfo selection = counterInfoArrayList.get(info.position);
                Bundle bundle = selection.sendBundle();
                bundle.putInt("current_pos",info.position);

                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            //remove selected counter
            case R.id.delete_id:
                counterInfoArrayList.remove(info.position);
                adapter.notifyDataSetChanged();
                saveInFile();
                //update count
                TextView num_count = (TextView) findViewById(R.id.num_count_val);
                Integer size = counterInfoArrayList.size();
                num_count.setText(String.format ("%d", size));
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }


    /**
     * Called when the activity starts up
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile(); //getting previously saved data

        TextView num_count = (TextView) findViewById(R.id.num_count_val);
        Integer size = counterInfoArrayList.size();
        num_count.setText(String.format("%d",size)); //no. of counters in the list
        adapter = new ArrayAdapter<CounterInfo>(this, R.layout.custom_list,counterInfoArrayList);
        counterList.setAdapter(adapter);

    }

    /**
     * Called when user clicks the add counter button
     *
     * @param view the view that was clicked
     */
    public void addCounter(View view)
    {
        Intent intent = new Intent(this, ActivityCounter.class); //starts new activity
        startActivity(intent);
    }


    /**
     * loads data from file using gson
     */
    private void loadFromFile()
    {
        try{
            FileInputStream fis = openFileInput("file.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<CounterInfo>>(){}.getType();

            counterInfoArrayList = gson.fromJson(in, listType);
        }catch (FileNotFoundException e){
            Log.d("filetag","FileNotFoundException ocurred");
            counterInfoArrayList = new ArrayList<CounterInfo>();
        }
    }

    /**
     * stores data to file using gson
     */
    private void saveInFile()
    {
        try
        {
            FileOutputStream fos = openFileOutput("file.sav", Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(counterInfoArrayList, out);
            out.flush();
            Log.d("filetag","Done saveInFile");
            fos.close();
        }catch (FileNotFoundException e){
            throw new RuntimeException();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
