package com.example.timo.simplemeditationtimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    private Button startMeditationButton;
    EditText editTextLastOfMeditation;
    private long lastOfMeditation;
    private Spinner spinner;
    private ArrayAdapter<String> dataAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        startMeditationButton = (Button) findViewById(R.id.startMeditationButton);
        startMeditationButton.setOnClickListener((OnClickListener) this);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> numbers = new ArrayList<String>();
        numbers.add("1");
        numbers.add("4");
        numbers.add("5");


        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v){
        int ce = v.getId();

        if(ce == R.id.startMeditationButton){
            lastOfMeditation = 20000;

            Intent intent = new Intent(MainMenuActivity.this, MeditationActivity.class);
            intent.putExtra("lastOfMeditation", lastOfMeditation);
            intent.putExtra("numberOfPhases", setPhase());
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(12);
        //String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int setPhase(){
        int phase = spinner.getSelectedItemPosition();
        if(phase == 0){
            phase = 1;
        }else if(phase == 1){
            phase = 4;
        }else if (phase == 2){
            phase = 5;
        }

        return phase;
    }
}
