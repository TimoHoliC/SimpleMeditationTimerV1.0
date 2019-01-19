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
    private Spinner phasesSpinner;
    private Spinner durationSpinner;
    private ArrayAdapter<String> phasesAdapter;
    private ArrayAdapter<String> durationsAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        startMeditationButton = (Button) findViewById(R.id.startMeditationButton);
        startMeditationButton.setOnClickListener((OnClickListener) this);
        phasesSpinner = (Spinner) findViewById(R.id.phasesSpinner);
        phasesSpinner.setOnItemSelectedListener(this);
        durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        durationSpinner.setOnItemSelectedListener(this);

        List<String> numbers = new ArrayList<String>();
        numbers.add("1");
        numbers.add("4");
        numbers.add("5");

        phasesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        phasesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        phasesSpinner.setAdapter(phasesAdapter);

        List<String> durations = new ArrayList<>();
        durations.add("20:00");
        durations.add("30:00");
        durations.add("40:00");

        durationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
        durationsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        durationSpinner.setAdapter(durationsAdapter);

    }

    @Override
    public void onClick(View v){
        int ce = v.getId();

        if(ce == R.id.startMeditationButton){


            Intent intent = new Intent(MainMenuActivity.this, MeditationActivity.class);
            intent.putExtra("lastOfMeditation", getDuration());
            intent.putExtra("numberOfPhases", getPhase());

            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(12);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getPhase(){
        int phase = phasesSpinner.getSelectedItemPosition();
        if(phase == 0){
            phase = 1;
        }else if(phase == 1){
            phase = 4;
        }else if (phase == 2){
            phase = 5;
        }

        return phase;
    }

    public long getDuration(){
        int durationPosition = durationSpinner.getSelectedItemPosition();
        long lDuration = 20 * 1000 * 60;
        if(durationPosition == 0){
            lDuration = 20 * 10 * 60;
        }else if(durationPosition == 1){
            lDuration = 30 * 1000 * 60;
        }else if (durationPosition == 2){
            lDuration = 40 * 1000 * 60;
        }

        return lDuration;

    }
}
