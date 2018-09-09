// Copyright, Shivam Jha, 2018
package tk.comschool.shivam.braintrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private int score = 0;
    int gameDuration = 30;
    int firstNum, secondNum, operator;
    List<Button> optionButtonList = new ArrayList<>();
    private void startTimer(){
        new CountDownTimer(TimeUnit.SECONDS.toMillis(gameDuration), 1000) {
            @Override
            public void onTick(long l) {
                TextView timerTextView = findViewById(R.id.timer);
                timerTextView.setText(Long.toString(TimeUnit.MILLISECONDS.toSeconds(l)) + "s");
            }

            @Override
            public void onFinish() {
                TextView statusTextView = findViewById(R.id.statusTextView);
                statusTextView.setText("Time Over");
                findViewById(R.id.resetButton).setVisibility(View.VISIBLE);
                for (Button button : optionButtonList){
                    button.setClickable(false);
                }

            }
        }.start();

    }

    public void incrScore() {
        score++;
        updateScore();
    }

    protected void setQuestAnswer(){
        // Method to set the Question
        Random randomGenerator = new Random();
        firstNum = randomGenerator.nextInt(99) + 1;
        secondNum = randomGenerator.nextInt(99) + 1;
        operator = randomGenerator.nextInt(2) + 1;
        String text;
        switch (operator){
            case 1:
                text = Integer.toString(firstNum) + " + " + Integer.toString(secondNum);
                break;
            default:
                text = Integer.toString(firstNum) + " - " + Integer.toString(secondNum);
                break;
        }
        ((TextView) findViewById(R.id.questionTextView)).setText(text); // Setting text as Question
        Random random = new Random();
        int correctAnswerIndex = random.nextInt(optionButtonList.size()); // Randomly storing the index of optionButtonList at which the correct answer is to be set by Button#setText method
        for(int i = 0; i < optionButtonList.size(); i++){
            if (i == correctAnswerIndex){
                if (operator == 1)
                    optionButtonList.get(i).setText(String.valueOf(firstNum + secondNum));
                else optionButtonList.get(i).setText(String.valueOf(firstNum - secondNum));
            }
            else {
                optionButtonList.get(i).setText(String.valueOf(random.nextInt(99)));
            }
        }
    }


    protected void startGame(){
        startTimer();
        setQuestAnswer();
        for (Button button : optionButtonList){
            button.setClickable(true);
        }
    }

    private void showDialog(){
        new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
                .setMessage("Click GO button to start the game.")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setTitle("Start")
                .setPositiveButton("GO!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        startGame();
                    }
                }).show();
    }

    public void resetGame(View view){
        score = 0;
        updateScore();
        findViewById(R.id.resetButton).setVisibility(View.INVISIBLE);
        showDialog();

    }

    private void postAnswerCheckTask(int i){
        // Method which performs necessary tasks when answer is found to be correct/wrong
        if (i == 1) {
            incrScore();
            TextView statusText = findViewById(R.id.statusTextView);
            statusText.setText("Correct");
            statusText.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_light));
            updateScore();
        }
        else {
            TextView statusText = findViewById(R.id.statusTextView);
            statusText.setText("Wrong");
            statusText.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
        }
    }

    public void optionButtonClicked(View view){
        Button optionButton = (Button) view;
        if (operator == 1){
            if (firstNum + secondNum == Integer.parseInt(optionButton.getText().toString())){
                postAnswerCheckTask(1);
            } else postAnswerCheckTask(0);
        }
        else {
            if (firstNum - secondNum == Integer.parseInt(optionButton.getText().toString())){
                postAnswerCheckTask(1);
            } else postAnswerCheckTask(0);
        }
        setQuestAnswer();

    }


    private void updateScore(){
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Score :" + Integer.toString(score));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDialog();

        optionButtonList.add((Button) findViewById(R.id.option1));
        optionButtonList.add((Button) findViewById(R.id.option2));
        optionButtonList.add((Button) findViewById(R.id.option3));
        optionButtonList.add((Button) findViewById(R.id.option4));

    }
}
