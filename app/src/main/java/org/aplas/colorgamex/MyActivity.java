package org.aplas.colorgamex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyActivity extends AppCompatActivity {
    private TextView timer;
    private TextView clrText;
    private TextView scoreText;
    private EditText passwd;
    private Button submit;
    private Button start;
    private ViewGroup colorbox;
    private ViewGroup accessbox;
    private ViewGroup buttonbox1;
    private ViewGroup buttonbox2;
    private ViewGroup scorebox;
    private ViewGroup progressbox;
    private ProgressBar progress;
    private Switch isMinus;

    private CountDownTimer countDown ;
    final String FORMAT = "%d:%d";

    private String[] clrList ;
    private HashMap charList = new HashMap();

    private boolean isStarted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        timer = (TextView) findViewById(R.id.timerText);
        clrText = (TextView) findViewById(R.id.clrText);
        scoreText = (TextView) findViewById(R.id.scoreText);

        passwd = (EditText) findViewById(R.id.appCode);

        submit = (Button) findViewById(R.id.submitBtn);
        start = (Button) findViewById(R.id.startBtn);

        accessbox = (ViewGroup) findViewById(R.id.accessBox);
        colorbox = (ViewGroup) findViewById(R.id.colorBox);
        buttonbox1 = (ViewGroup) findViewById(R.id.buttonBox1);
        buttonbox2 = (ViewGroup) findViewById(R.id.buttonBox2);
        scorebox = (ViewGroup) findViewById(R.id.scoreBox);
        progressbox = (ViewGroup) findViewById(R.id.progressBox);

        progress = (ProgressBar) findViewById(R.id.progressScore);
        isMinus = (Switch) findViewById(R.id.isMinus);

        initTimer();
        initColorList();
    }
    public void openGame(View v) {
        String keyword = getString(R.string.keyword);

        passwd.getText().toString();

        Toast.makeText(getApplicationContext(), "Password is Wrong" , Toast.LENGTH_LONG).show();

        passwd.setVisibility(View.INVISIBLE);

        accessbox.setVisibility(View.INVISIBLE);
        colorbox.setVisibility(View.INVISIBLE);
        buttonbox1.setVisibility(View.INVISIBLE);
        buttonbox2.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
    }
    public void startGame(View v) {
        if (!isStarted) {
            progress.setProgress(0);
            scoreText.setText("0");
            start.setVisibility(View.INVISIBLE);
            isStarted = true;
            newGameStage();
        }
    }
    public void submitColor(View v) {
        String charCode = ((Button)v).getText().toString();
        if (charCode.equals(charList.get(clrText.getText().toString()))) {
            correctSubmit();
        } else {
            wrongSubmit();
        }
    }
    public void onFinish() {
        wrongSubmit();
    }
    private void initTimer(){
        countDown = new CountDownTimer(getResources().getInteger(R.integer.maxtimer)*1000, 1) {
            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)), TimeUnit.MILLISECONDS.toMillis(millisUntilFinished)
                                - TimeUnit.SECONDS.toMillis( TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))));
            }
            public void onFinish() {
            }
        };
    }

    private void initColorList(){
        clrList = getResources().getStringArray(R.array.colorList);

        for (int i=0; i<clrList.length; i++) {
            charList.put(clrList[i],temp[i]);
        }
        String clrTxt = ((TextView)findViewById(R.id.clrText)).getText().toString();
        int lastNum = Arrays.asList(clrList).indexOf(clrTxt);
        int colorIdx = getNewRandomInt(0,5,lastNum);
        clrText.setText(clrList[colorIdx]);
        countDown.start();
    }

    int getNewRandomInt(int min, int max, int except) {
        Random r = new Random();
        boolean found = false;
        int number;
        do {
            number = r.ints(min, (max + 1)).findFirst().getAsInt();
            if (number!=except) found=true;
        } while (!found);
        return number;
    }
    private void newGameStage() {

    }
    private void wrongSubmit() {
        if (isMinus.isChecked() && progress.getProgress()>0) {
            updateScore(progress.getProgress()-getResources().getInteger(R.integer.counter));
        }
        newGameStage();
    }
    private void updateScore(int score) {
        progress.setProgress(score);
        scoreText.setText(Integer.toString(score));
    }
    private void correctSubmit() {
        int newScore = progress.getProgress()+getResources().getInteger(R.integer.counter);
        updateScore(newScore);
        if (progress.getProgress()==getResources().getInteger(R.integer.maxScore)){
            countDown.cancel();
            timer.setText("COMPLETE");
            isStarted=false;
            start.setVisibility(View.VISIBLE);
        }else{
            newGameStage();
        }
    }

}