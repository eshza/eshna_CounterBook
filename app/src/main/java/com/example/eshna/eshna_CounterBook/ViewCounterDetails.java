package com.example.eshna.eshna_CounterBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by eshna on 9/25/17.
 */

public class ViewCounterDetails extends AppCompatActivity
{
    private TextView nameDisplay,dateDisplay, initialDisplay, currentDisplay,commentDisplay;

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

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!= null)
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

