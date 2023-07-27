package com.example.savethedateapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.savethedateapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainScreen extends AppCompatActivity {

    private Button startButton;
    private TextView daysTextView;
    private TextView monthsTextView;
    private TextView yearsTextView;
    private TextView hoursTextView;
    private TextView minutesTextView;
    private TextView secondsTextView;
    private CountDownTimer countDownTimer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //recupera o content view
        setContentView(R.layout.fragment_main_screen);

        startButton = findViewById(R.id.btn_start_count);
        yearsTextView = findViewById(R.id.tv_countdown_year);
        monthsTextView = findViewById(R.id.tv_countdown_month);
        daysTextView = findViewById(R.id.tv_countdown_days);
        hoursTextView = findViewById(R.id.tv_countdown_hours);
        minutesTextView = findViewById(R.id.tv_countdown_minutes);
        secondsTextView = findViewById(R.id.tv_countdown_seconds);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartCountdown();
            }
        });
    }

    private void StartCountdown(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDateString = "2023-08-13 22:15:00";

        try {
            Date targetDateTime = dateFormat.parse(targetDateString);
            long targetDateTimeInMilis = targetDateTime.getTime();

            long currDateTimeInMilis = System.currentTimeMillis();
            long countdownMilis = targetDateTimeInMilis - currDateTimeInMilis;

            if(countDownTimer != null){
                countDownTimer.cancel();
            }

            countDownTimer = new CountDownTimer(countdownMilis, 1000) {
                @Override
                public void onTick(long l) {
                    yearsTextView.setText(formatTime(l, 'y'));
                    monthsTextView.setText(formatTime(l, 'M'));
                    daysTextView.setText(formatTime(l, 'd'));
                    hoursTextView.setText(formatTime(l, 'h'));
                    minutesTextView.setText(formatTime(l, 'm'));
                    secondsTextView.setText(formatTime(l, 's'));
                    //setar os textos
                    //setar a visibility
                }

                @Override
                public void onFinish() {
                    //setar o header text view para contagem encerrada!
                    //tirar a visibility dos textviews
                }
            };
            countDownTimer.start();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private String formatTime(long milis, char type){

        switch(type){
            case 'y': return String.format("%d", TimeUnit.MILLISECONDS.toDays(milis)/ 365);
            case 'M': return String.format("%d", TimeUnit.MILLISECONDS.toDays(milis) / 30);
            case 'd': return String.format("%d", TimeUnit.MILLISECONDS.toDays(milis) % 30);
            case 'h': return String.format("%02d", TimeUnit.MILLISECONDS.toHours(milis) % 24);
            case 'm': return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(milis) % 60);
            case 's': return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milis) % 60);
            default: return "";
        }

    }
}