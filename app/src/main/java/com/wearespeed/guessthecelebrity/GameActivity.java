package com.wearespeed.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int score=0;
    int total=0;
    int timerDone=0;

    ArrayList<Integer> takenIndex = new ArrayList<>();

    Button playAgain;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    ImageView image;
    TextView scoreText;
    TextView timerText;

    public void setScore()
    {
        String temp = Integer.toString(score)+" / "+Integer.toString(total);
        scoreText.setText(temp);
    }

    public void setQuestion()
    {
        try {

            ArrayList<String> answers = new ArrayList<>();

            ArrayList<Integer> options = new ArrayList<>();

            if (timerDone == 1) {
                return;
            }

            Random rn = new Random();

            int ind = rn.nextInt(MainActivity.name.size());

            while (takenIndex.contains(ind)) {
                ind = rn.nextInt(MainActivity.name.size());
            }
            takenIndex.add(ind);

            options.add(ind);

            MainActivity.taken.set(ind, 1);

            int optionIndex = rn.nextInt(4);

            for (int i = 0; i < 4; i++) {
                if (i == optionIndex) {
                    answers.add(MainActivity.name.get(ind));
                } else {
                    int temp = rn.nextInt(MainActivity.name.size());

                    while (options.contains(temp)) {
                        temp = rn.nextInt(MainActivity.name.size());
                    }
                    options.add(temp);
                    answers.add(MainActivity.name.get(temp));
                }
            }



            option1.setText(answers.get(0));
            option2.setText(answers.get(1));
            option3.setText(answers.get(2));
            option4.setText(answers.get(3));
            image.setImageBitmap(MainActivity.images.get(ind));

            option1.setTag("Wrong!");
            option2.setTag("Wrong!");
            option3.setTag("Wrong!");
            option4.setTag("Wrong!");

            switch (optionIndex)
            {
                case 0: option1.setTag("Correct!");
                    break;
                    case 1: option2.setTag("Correct!");
                    break;
                    case 2: option3.setTag("Correct!");
                    break;
                    case 3: option4.setTag("Correct!");
                    break;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void startTimer()
    {
        CountDownTimer cdt = new CountDownTimer(30100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                int tempHehe = (int) (millisUntilFinished/1000);

                String text = Integer.toString(tempHehe)+"s";

                if(tempHehe<=9)
                {
                    text = "0"+text;
                }

                timerText.setText(text);
            }

            @Override
            public void onFinish() {
                timerDone=1;
                timerText.setText("00s");
                playAgain.setVisibility(View.VISIBLE);
            }
        };

        cdt.start();
    }


    public void onClickOption(View view)
    {
        if(timerDone==1)
        {
            return;
        }

        Button clicked = (Button) view;

        String response = (String) clicked.getTag();

        if(response=="Correct!")
        {
            score++;
        }
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        setQuestion();
        total++;
        setScore();
    }

    public void reset(View view)
    {
        takenIndex.clear();
        score=0;
        total=0;
        timerDone=0;
        setScore();
        startTimer();
        setQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        image = findViewById(R.id.imageView);
        scoreText = findViewById(R.id.scoreText);
        timerText = findViewById(R.id.timerText);
        playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(this::reset);
        playAgain.setVisibility(View.INVISIBLE);
        startTimer();

        Log.i("Names",MainActivity.images.toString());

        setQuestion();

    }
}