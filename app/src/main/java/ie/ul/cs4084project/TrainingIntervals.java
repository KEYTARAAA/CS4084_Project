package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class TrainingIntervals extends AppCompatActivity {
    public static final String SPRINT_SECONDS = "SPRINT_SECONDS";
    public static final  String SPRINT_MINUTES = "SPRINT_MINUTES";
    public static final  String REST_SECONDS = "REST_SECONDS";
    public static final  String REST_MINUTES = "REST_MINUTES";
    public static final  String ROUNDS = "ROUNDS";
    private String name, id, email;
    private NumberPicker numberPickerSprintSeconds, numberPickerSprintMinutes, numberPickerRestSeconds , numberPickerRestMinutes;
    private EditText rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
        setContentView(R.layout.activity_training_intervals);
        numberPickerSprintSeconds = (NumberPicker) findViewById(R.id.numberPickerSprintSeconds);
        numberPickerSprintSeconds.setMinValue(0);
        numberPickerSprintSeconds.setMaxValue(59);
        numberPickerSprintMinutes = (NumberPicker) findViewById(R.id.numberPickerSprintMinutes);
        numberPickerSprintMinutes.setMinValue(0);
        numberPickerSprintMinutes.setMaxValue(59);
        numberPickerRestSeconds = (NumberPicker) findViewById(R.id.numberPickerRestSeconds);
        numberPickerRestSeconds.setMinValue(0);
        numberPickerRestSeconds.setMaxValue(59);
        numberPickerRestMinutes = (NumberPicker) findViewById(R.id.numberPickerRestMinutes);
        numberPickerRestMinutes.setMinValue(0);
        numberPickerRestMinutes.setMaxValue(59);
        rounds = (EditText) findViewById(R.id.editTextNumberRoundsEnter);
    }


    public void ready(View view){
        Intent intent = new Intent(this, IntervalTraining.class);
        intent.putExtra(MainActivity.ID, id);
        intent.putExtra(MainActivity.EMAIL, email);
        intent.putExtra(MainActivity.NAME, name);
        intent.putExtra(SPRINT_SECONDS, numberPickerSprintSeconds.getValue());
        intent.putExtra(SPRINT_MINUTES, numberPickerSprintMinutes.getValue());
        intent.putExtra(REST_SECONDS, numberPickerRestSeconds.getValue());
        intent.putExtra(REST_MINUTES, numberPickerRestMinutes.getValue());
        int r = 3;
        if(!rounds.getText().toString().isEmpty()) {
             r = Integer.parseInt(rounds.getText().toString());
        }
        intent.putExtra(ROUNDS, r);

        startActivity(intent);

    }
}