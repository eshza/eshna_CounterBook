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


public class MainActivity extends AppCompatActivity {
    private ListView counterList;
    private ArrayList<CounterInfo> counterInfoArrayList;
    private ArrayAdapter<CounterInfo> adapter;

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
                Intent intent = new Intent(view.getContext(),ViewCounterDetails.class);
                CounterInfo selection = counterInfoArrayList.get(position);
                Bundle bundle = selection.sendBundle();

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){

            case R.id.edit_id:
                Intent intent = new Intent(this, ActivityCounter.class);
                CounterInfo selection = counterInfoArrayList.get(info.position);
                Bundle bundle = selection.sendBundle();
                bundle.putInt("current_pos",info.position);

                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            //remove selected person
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



    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();

        TextView num_count = (TextView) findViewById(R.id.num_count_val);
        Integer size = counterInfoArrayList.size();
        num_count.setText(String.format("%d",size));
        adapter = new ArrayAdapter<CounterInfo>(this, R.layout.custom_list,counterInfoArrayList);
        counterList.setAdapter(adapter);

    }
    public void addCounter(View view)
    {
        Intent intent = new Intent(this, ActivityCounter.class);
        startActivity(intent);
    }




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
