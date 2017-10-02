/*
 * Class Name: ViewCounterDetails
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
/**
 * Displays details of a counter
 *
 * @author eshna
 * @version 1.0
 * @see ActivityCounter
 * @see MainActivity
 * @see CounterInfo
 * @since 1.0
 */




public class ViewCounterDetails extends AppCompatActivity
{
    private TextView nameDisplay,dateDisplay, initialDisplay, currentDisplay,commentDisplay;

    /**
     * method is called on activity creation
     *
     * @param savedInstanceState saves state of application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_counter_details);

        nameDisplay = (TextView) findViewById(R.id.name_display);
        dateDisplay = (TextView) findViewById(R.id.date_display);
        initialDisplay = (TextView) findViewById(R.id.initial_display);
        currentDisplay = (TextView) findViewById(R.id.current_display);
        commentDisplay = (TextView) findViewById(R.id.comment_display);
    }

    /**
     * called when the activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!= null) //counter already exists and has info
        {
            nameDisplay.setText(bundle.getString("name"));
            dateDisplay.setText(bundle.getString("current_date"));
            commentDisplay.setText(bundle.getString("comment"));


            if(Integer.parseInt(bundle.getString("current_value"))>=0)
            {
                currentDisplay.setText(bundle.getString("current_value"));
            }
            if(Integer.parseInt(bundle.getString("initial_value"))>=0)
            {
                initialDisplay.setText(bundle.getString("initial_value"));
            }


        }
    }
}

