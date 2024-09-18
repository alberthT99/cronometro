package co.edu.cronometro;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStartStop, btnLap, btnReset;
    private ListView lvLaps;

    private Handler handler = new Handler();
    private long startTime = 0;
    private boolean isRunning = false;

    private ArrayList<String> lapTimes = new ArrayList<>();
    private ArrayAdapter<String> lapAdapter;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int millisRemaining = (int) (millis % 1000);

            tvTimer.setText(String.format("%02d:%02d:%03d", minutes, seconds, millisRemaining));

            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnLap = findViewById(R.id.btnLap);
        btnReset = findViewById(R.id.btnReset);
        lvLaps = findViewById(R.id.lvLaps);

        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapTimes);
        lvLaps.setAdapter(lapAdapter);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lapTimes.size() < 5) {
                    lapTimes.add(tvTimer.getText().toString());
                    lapAdapter.notifyDataSetChanged();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler.post(runnable);
        isRunning = true;
        btnStartStop.setText("Stop");
        btnLap.setEnabled(true);
        btnReset.setEnabled(false);
    }

    private void stopTimer() {
        handler.removeCallbacks(runnable);
        isRunning = false;
        btnStartStop.setText("Start");
        btnLap.setEnabled(false);
        btnReset.setEnabled(true);
    }

    private void resetTimer() {
        startTime = 0;
        tvTimer.setText("00:00:00");
        lapTimes.clear();
        lapAdapter.notifyDataSetChanged();
        btnReset.setEnabled(false);
    }
}