/*
 *
 * Class Name: ActivityCounter
 *
 * Version : Version 1.0
 *
 * Date: October 2, 2017
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
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Allows user to enter and edit details for a counter
 *
 * @author eshna
 * @version 1.0
 * @see MainActivity
 * @see ViewCounterDetails
 * @see CounterInfo
 * @since 1.0
 */

public class ActivityCounter extends AppCompatActivity {
    private TextView counterText, dateText;
    private int counterValue;
    private EditText nameInput, initValue, currValue, commentInput;
    private CounterInfo counterInfo = null;
    private ArrayList<CounterInfo> counterInfoArrayList;
    private int edit_mode  = 0;
    private int pos;
    private int oldCurrVal;

    /**
     * Called on activity creation
     *
     * @param savedInstanceState saves state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        counterText = (TextView)findViewById(R.id.value_of_counter);
        nameInput = (EditText) findViewById(R.id.name_display);
        initValue = (EditText) findViewById(R.id.initial_display);
        currValue = (EditText) findViewById(R.id.current_display);
        commentInput = (EditText)findViewById(R.id.comment_display);



    }

    /**
     * called when activity starts up
     */

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        edit_mode = 0; //not in edit mode, new counter is created

        if(bundle != null) //counter already exists and has some information
        {
            edit_mode = 1; //edit mode turned on

            pos = bundle.getInt("current_pos");
            nameInput.setText(bundle.getString("name"));
            if(Integer.parseInt(bundle.getString("initial_value"))>=0)
                initValue.setText(bundle.getString("initial_value"));
            if(Integer.parseInt(bundle.getString("current_value"))>=0)
            {
                currValue.setText(bundle.getString("current_value"));
                counterText.setText(bundle.getString("current_value"));
                oldCurrVal = Integer.parseInt(currValue.getText().toString());
            }

            commentInput.setText(bundle.getString("comment"));
        }
        //current value takes value of initial value
        initValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edit_mode!= 1)  //when new counter created
                {
                    currValue.setText(initValue.getText().toString());
                    counterText.setText(initValue.getText().toString());
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //counter textbox takes the value of the current value
        currValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    counterText.setText(currValue.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Increments the current value when plus button is clicked.
     *
     * @param view the view that was clicked
     */
    public void plusButtonClicked(View view)
    {
        if(!TextUtils.isEmpty(counterText.getText().toString())) //textbox must have value
        {
            counterValue = Integer.parseInt(counterText.getText().toString());
            counterValue++;
            counterText.setText(String.valueOf(counterValue));
            currValue.setText(String.valueOf(counterValue));
        }


    }

    /**
     * Decrements the current value when minus button is clicked.
     * @param view the view that was clicked
     */

    public void minusButtonClicked(View view)
    {
        if(!TextUtils.isEmpty(counterText.getText().toString()))
        {
            counterValue = Integer.parseInt(counterText.getText().toString());
            counterValue--;
            if(counterValue >= 0 ) {    //cannot decrement below 0
                counterText.setText(String.valueOf(counterValue));
                currValue.setText(String.valueOf(counterValue));
            }
        }

    }

    /**
     * Resets the current value to initial value when plus button is clicked
     * @param view the view that was clicked
     */
    public void resetButtonClicked(View view)
    {
        if(!TextUtils.isEmpty(counterText.getText().toString()))
        {
            counterValue = Integer.parseInt(initValue.getText().toString());
            counterText.setText(String.valueOf(counterValue));
            currValue.setText(String.valueOf(counterValue));
        }
    }

    /**
     * called when user clicks the submit button
     * @param view the view that was clicked
     * @throws InputNumberException when number entered is negative
     */

    public void onSubmit(View view) throws InputNumberException {
        try {
            Context context = getApplicationContext();

            String name = nameInput.getText().toString();

            String initial = initValue.getText().toString();

            if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(initial)) {
                Toast.makeText(context, "Must enter name and initial value of counter", Toast.LENGTH_LONG).show();

            }
            else if (!TextUtils.isEmpty(currValue.getText().toString()) && Integer.parseInt(currValue.getText().toString()) < 0)
                Toast.makeText(context, "Invalid input for current value", Toast.LENGTH_LONG).show();
            else if (Integer.parseInt(initial) < 0 )
                Toast.makeText(context, "Invalid input for initial value", Toast.LENGTH_LONG).show();

            else
            {
                counterInfo = new CounterInfo(name);

                if(!TextUtils.isEmpty(commentInput.getText().toString()))
                    counterInfo.setComment(commentInput.getText().toString());

                counterInfo.setInitial(initial);


                if(!TextUtils.isEmpty(currValue.getText().toString()))
                    counterInfo.setCurrent(currValue.getText().toString());
                else
                    counterInfo.setCurrent(initial);



                Toast.makeText(context, "Entry successful!", Toast.LENGTH_LONG).show();

                loadFromFile();
                if(edit_mode == 0){
                    counterInfo.setDate();
                    counterInfoArrayList.add(counterInfo);
                }

                else{

                    if(oldCurrVal != Integer.parseInt(currValue.getText().toString()))
                        counterInfo.setDate();
                    counterInfoArrayList.set(pos, counterInfo);
                }


                saveInFile();
            }

        }catch (InputNumberException e){
            e.printStackTrace();
        }
        finish(); //exits out of activity
    }

    /**
     * saves information to file using gson
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

    /**
     * loads information from file using gson
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

}

