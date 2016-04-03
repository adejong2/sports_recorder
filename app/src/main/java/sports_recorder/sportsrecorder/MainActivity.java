package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    int goals; // Change this to zero later
    int num_points;
    private Button goalButton, sogButton, shotButton, penaltyButton;
    public static ArrayList<Dot> Dots;
    public static int timeOnClock;      // Time of player on field
    public static int eventType = R.string.event_type_null;
    public boolean clockIsRunning;      // True if the clock should immediately start running
    public boolean dirtyClock = false;  // True if there are pending changes to timeOnClock

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    // Timer
    long startTime = 0;
    Button timer;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long milliseconds = currentTime - startTime;
            int seconds = (int) milliseconds / 1000;
            if (seconds > 0) {
                seconds += timeOnClock;
                int minutes = (int) Math.floor((double) (seconds / 60));

                timer.setText(String.format("%d:%02d", minutes, seconds % 60));
            }
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set button listeners:
        goalButton = (Button) findViewById(R.id.goalButton);
        goalButton.setOnClickListener(this);

        sogButton = (Button) findViewById(R.id.SOGButton);
        sogButton.setOnClickListener(this);

        shotButton = (Button) findViewById(R.id.shotButton);
        shotButton.setOnClickListener(this);

        penaltyButton = (Button) findViewById(R.id.penaltyButton);
        penaltyButton.setOnClickListener(this);


        // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE); //For fragments
        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
//        Gson gson = new Gson();

        // Load the data values for the app:
//        int defaultValue = getResources().getInteger(R.string.saved_goals_default);
        int defaultValue = 0;
        goals = sharedPref.getInt(getString(R.string.saved_goals), defaultValue);
//        goalButton.setText("" + goals);
        num_points = sharedPref.getInt(getString(R.string.saved_goals), 0);



        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Dots = (ArrayList<Dot>) savedInstanceState.getSerializable(getString(R.string.saved_dots_arraylist));
            timeOnClock = savedInstanceState.getInt(getString(R.string.saved_time_on_clock), 0);
            this.startTime = savedInstanceState.getLong(getString(R.string.saved_start_time), System.currentTimeMillis());
            this.clockIsRunning = savedInstanceState.getBoolean(getString(R.string.saved_clock_is_running), false);
        } else {
            // Probably initialize members with default values for a new instance
            Dots = new ArrayList<>();
            timeOnClock = 0;
            this.clockIsRunning = false;
        }

        //Timer
        timer = (Button) findViewById(R.id.timerText);
        if (timeOnClock > 0) {
            timer.setText(String.format("%d:%02d", timeOnClock / 60, timeOnClock % 60));
        }
        Button timerButton = (Button) findViewById(R.id.timerButton);


        if (clockIsRunning) {
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            timerButton.setText("stop");
            dirtyClock = true;
        } else {timerButton.setText("start");}

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button timerButton = (Button) v;
                if (timerButton.getText().equals("stop")) { // Stop the clock
                    timerHandler.removeCallbacks(timerRunnable);
                    timerButton.setText("start");
                    updateTimeOnClock();
                    clockIsRunning = false;
                } else {    // Start the clock:
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    timerButton.setText("stop");
                    dirtyClock = true;
                    clockIsRunning = true;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        // Fix from stack overflow for crashing on rotation:
        //http://stackoverflow.com/questions/7009086/recycle-imageviews-bitmap/7009362#7009362
        //
//        FieldDots imageView = (FieldDots)findViewById(R.id.dots_view);
//        Drawable drawable = imageView.getDrawableState();
//        if (drawable instanceof BitmapDrawable) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            bitmap.recycle();
//        }

        FieldDots imageView = (FieldDots) findViewById(R.id.dots_view);

        if (imageView.getBackground() != null &&
                BitmapDrawable.class.isInstance(imageView.getBackground())) {
            ((BitmapDrawable) imageView.getBackground()).getBitmap().recycle();
        }

        super.onDestroy();
    }

    public void onClick(View view) {
        int id = view.getId();
        FieldDots field = (FieldDots) findViewById(R.id.dots_view);
        switch (id) {
            case R.id.goalButton:
                Toast.makeText(this, getString(R.string.event_type_goal), Toast.LENGTH_SHORT).show();
                eventType = R.string.event_type_goal;
//                field.setColor(Color.CYAN);
//              goals++;
//              goalButton.setText("" + goals);
//              // Save data for the app:
//              editor.putInt(getString(R.string.saved_goals), goals);
                break;
            case R.id.SOGButton:
                Toast.makeText(this, getString(R.string.event_type_shot_on_goal), Toast.LENGTH_SHORT).show();
                eventType = R.string.event_type_shot_on_goal;
//                field.setColor(Color.BLUE);
                break;
            case R.id.shotButton:
                Toast.makeText(this, getString(R.string.event_type_shot), Toast.LENGTH_SHORT).show();
                eventType = R.string.event_type_shot;
//                field.setColor(Color.LTGRAY);
                break;
            case R.id.penaltyButton:
                Toast.makeText(this, getString(R.string.event_type_penalty), Toast.LENGTH_SHORT).show();
                eventType = R.string.event_type_penalty;
//                field.setColor(Color.RED);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();    // Call the superclass method first.

        //timerHandler.removeCallbacks((timerRunnable));
        //Button timerButton = (Button)findViewById(R.id.timerButton);
        //timerButton.setText("start");

        editor.commit();    // Save data for the app:
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current game state
        savedInstanceState.putSerializable(getResources().getString(R.string.saved_dots_arraylist), Dots);
        updateTimeOnClock();
        savedInstanceState.putInt(getString(R.string.saved_time_on_clock), timeOnClock);
        savedInstanceState.putLong(getString(R.string.saved_start_time), this.startTime);
        savedInstanceState.putBoolean(getString(R.string.saved_clock_is_running), this.clockIsRunning);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    public static int getEventType() {
        return eventType;
    }

    public static void resetEventType() {
        eventType = R.string.event_type_null;
    }

    public void updateTimeOnClock() {
        if (!dirtyClock)
            return;

        dirtyClock = false;
        long current = System.currentTimeMillis();
        long milliseconds = current - this.startTime;
        this.startTime = current;
        timeOnClock += (int) milliseconds / 1000;
//        System.out.println(timeOnClock);
    }

}


