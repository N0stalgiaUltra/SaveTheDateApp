package com.example.savethedateapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.savethedateapp.R;
import com.google.api.services.calendar.model.TimePeriod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainScreen extends AppCompatActivity {

    private Button startButton;
    private TextView daysTextView;
    private TextView monthsTextView;
    private TextView yearsTextView;
    private TextView hoursTextView;
    private TextView minutesTextView;
    private TextView secondsTextView;
    private TextView headerTextView;
    private TextView placeHolderTextView;
    private CountDownTimer countDownTimer;
    private LinearLayout textsLayout;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog hourPickerDialog;
    private Button dateButton;
    private Button hourButton;

    private String selectedDate = "";
    private String selectedTime = "";
    int hour = 0;
    int minute = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //recupera o content view
        setContentView(R.layout.fragment_main_screen);

        startButton = findViewById(R.id.btn_start_count);
        textsLayout = findViewById(R.id.selected_texts);

        //Text Views
        yearsTextView = findViewById(R.id.tv_countdown_year);
        monthsTextView = findViewById(R.id.tv_countdown_month);
        daysTextView = findViewById(R.id.tv_countdown_days);
        hoursTextView = findViewById(R.id.tv_countdown_hours);
        minutesTextView = findViewById(R.id.tv_countdown_minutes);
        secondsTextView = findViewById(R.id.tv_countdown_seconds);
        headerTextView = findViewById(R.id.tv_header);
        placeHolderTextView = findViewById(R.id.tv_placeholder);


        textsLayout.setVisibility(View.INVISIBLE);
        headerTextView.setVisibility(View.INVISIBLE);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartCountdown();
            }
        });

        //Seletor de datas
        dateButton = findViewById(R.id.btn_pick_data);
        dateButton.setText(getTodayDate());
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        hourButton = findViewById(R.id.btn_pick_hour);
        hourButton.setText(getTodayHour());

        hourButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pickHour();
            }

        });
    }

    private void StartCountdown(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String targetDateString = "2023-07-27 22:35:00";
        String targetDateString = selectedDate+selectedTime;

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
                    yearsTextView.setText("Years: " + formatTime(l, 'y'));
                    monthsTextView.setText("Months: " +formatTime(l, 'M'));
                    daysTextView.setText("Days: " +formatTime(l, 'd'));
                    hoursTextView.setText("Hours: " +formatTime(l, 'h'));
                    minutesTextView.setText("Minutes: " + formatTime(l, 'm'));
                    secondsTextView.setText("Seconds: " +formatTime(l, 's'));

                    textsLayout.setVisibility(View.VISIBLE);
                    headerTextView.setVisibility(View.VISIBLE);
                    placeHolderTextView.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    textsLayout.setVisibility(View.INVISIBLE);
                    headerTextView.setVisibility(View.INVISIBLE);
                    placeHolderTextView.setVisibility(View.VISIBLE);
                }
            };
            countDownTimer.start();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
    /*TODO: Acertar o numero de zeros para as horas e minutos*/
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


    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                selectedDate = year+"-"+month+"-"+day+" ";
                dateButton.setText(day + "/" + month + "/" + year);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }
    private void openDatePicker(){
        datePickerDialog.show();
    }

    private String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + "/" + month + "/" + year;
    }

    private void pickHour(){

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selected_hour, int selected_minutes) {
                hour = selected_hour;
                minute = selected_minutes;
                selectedTime = String.format("%02d:%02d:00", hour, minute);
                hourButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }


    private String getTodayHour(){
        Calendar calendar = Calendar.getInstance();
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMin = calendar.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%02d:%02d", currHour, currMin);
    }
}