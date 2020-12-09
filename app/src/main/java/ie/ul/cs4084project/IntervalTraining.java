package ie.ul.cs4084project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntervalTraining extends AppCompatActivity {

    private String name, id, email;
    private int sprintSeconds, sprintMinutes, restSeconds, restMinutes, rounds, sprintRounds, roundNumber;
    private boolean start;
    private Button buttonStartPause, buttonFinish;
    private CountDownTimer countDownTimerSprint, countDownTimerRest, countDownTimerCurrent;
    private long timeLeftSprint, timeLeftRest, sprintTime, restTime, timeLeftCurrent;
    private TextView mode, countDown, roundCount;
    private ConstraintLayout constraintLayout;
    private Vibrator vibrator;
    private static final long[] vibrationPatternSprintOver = {1,800,200,800,200,800};
    private static final long[] vibrationPatternRestOver = {1,400,100,400,100,400};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_training);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
        sprintSeconds = intent.getIntExtra(TrainingIntervals.SPRINT_SECONDS, 0);
        sprintMinutes = intent.getIntExtra(TrainingIntervals.SPRINT_MINUTES, 0);
        restSeconds = intent.getIntExtra(TrainingIntervals.REST_SECONDS, 0);
        restMinutes = intent.getIntExtra(TrainingIntervals.REST_MINUTES, 0);
        roundCount = findViewById(R.id.textViewIntervalTrainingRounds);
        roundCount.setText("ROUND 0/"+rounds);
        sprintRounds = intent.getIntExtra(TrainingIntervals.ROUNDS, 3);
        roundCount.setText("ROUND 0/"+sprintRounds);
        roundNumber = 0;

        rounds = sprintRounds + (sprintRounds-1);
        start = true;
        countDown = findViewById(R.id.textViewCountdown);
        mode = findViewById(R.id.textViewMode);
        buttonFinish = findViewById(R.id.buttonFinishIntervalTraining);
        buttonStartPause = findViewById(R.id.buttonStartPauseIntervalTraining);
        mode.setText("Ready");
        int mins = (int) sprintTime/60000;
        int secs = (int) (sprintTime%60000)/1000;
        String timeLeftText = "";
        if(mins<10){
            timeLeftText += "0"+mins;
        }else{
            timeLeftText += Integer.toString(mins);
        }
        timeLeftText +=  " : ";
        if(secs<10){
            timeLeftText += "0"+secs;
        }else{
            timeLeftText += secs;
        }
        countDown.setText(timeLeftText);

        sprintTime = ((sprintMinutes * 60) + sprintSeconds)*1000;
        timeLeftSprint = sprintTime;
        restTime = ((restMinutes * 60) + restSeconds)*1000;
        timeLeftRest = restTime;
        constraintLayout = findViewById(R.id.constraintLayoutIntervalTraining);
        constraintLayout.setBackgroundColor(Color.BLUE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }


    public void startPause(View view){
        if(start){
            startTimer();
        }else{
            pauseTimer();
        }
    }

    private void pauseTimer() {
        countDownTimerCurrent.cancel();
        buttonStartPause.setText("Start");
        start = false;
    }

    private void startTimer() {
        start = true;
        buttonStartPause.setText("Pause");
        mode.setText("GO!");
        roundNumber++;
        constraintLayout.setBackgroundColor(Color.GREEN);
        roundCount.setText("ROUND "+roundNumber+"/"+sprintRounds);
        countDownTimerSprint = new CountDownTimer(timeLeftSprint, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftCurrent = millisUntilFinished;
                updateTime();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                rounds--;
                if(rounds>0) {
                    vibrator.vibrate(VibrationEffect.createWaveform(vibrationPatternSprintOver , -1));
                    mode.setText("REST");
                    constraintLayout.setBackgroundColor(Color.RED);
                    countDownTimerRest = new CountDownTimer(timeLeftRest, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timeLeftCurrent = millisUntilFinished;
                            updateTime();
                        }

                        @Override
                        public void onFinish() {
                            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPatternRestOver , -1));
                            rounds--;
                            startTimer();
                        }
                    };
                    countDownTimerCurrent = countDownTimerRest;
                    countDownTimerRest.start();
                }else {
                    vibrator.vibrate(3000);
                    mode.setText("FINISHED!!!");
                    constraintLayout.setBackgroundColor(Color.YELLOW);
                }
            }
        };
        countDownTimerCurrent = countDownTimerSprint;
        countDownTimerSprint.start();
    }

    private void updateTime() {
        int mins = (int) timeLeftCurrent/60000;
        int secs = (int) (timeLeftCurrent%60000)/1000;
        String timeLeftText = "";
        if(mins<10){
            timeLeftText += "0"+mins;
        }else{
            timeLeftText += mins;
        }
        timeLeftText +=  " : ";
        if(secs<10){
            timeLeftText += "0"+secs;
        }else{
            timeLeftText += Integer.toString(secs);
        }
        countDown.setText(timeLeftText);
    }


    public void finish(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(MainActivity.ID, id);
        intent.putExtra(MainActivity.EMAIL, email);
        intent.putExtra(MainActivity.NAME, name);
        if(countDownTimerCurrent!=null) {
            countDownTimerCurrent.cancel();
        }
        if(vibrator!=null) {
            vibrator.cancel();
        }
        startActivity(intent);
    }
}