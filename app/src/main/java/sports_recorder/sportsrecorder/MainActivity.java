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
    private Button goalButton;
    public static ArrayList<Dot> Dots;
    public static int timeOnClock;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    // Timer
    long startTime = 0;
    Button timer;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long milliseconds = System.currentTimeMillis() - startTime;
            int minutes = (int) Math.floor((double) (milliseconds / 1000 / 60));

            timer.setText(String.format("%d:%02d", minutes, milliseconds/1000));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set button listeners:
        goalButton = (Button) findViewById(R.id.button1);
        goalButton.setOnClickListener(this);


        // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE); //For fragments
        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
//        Gson gson = new Gson();

        // Load the data values for the app:
//        int defaultValue = getResources().getInteger(R.string.saved_goals_default);
        int defaultValue = 1;
        goals = sharedPref.getInt(getString(R.string.saved_goals), defaultValue);
//        goalButton.setText("" + goals);

        num_points = sharedPref.getInt(getString(R.string.saved_goals), 0);


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Dots = (ArrayList<Dot>) savedInstanceState.getSerializable(getString(R.string.saved_dots_arraylist));
        } else {
            // Probably initialize members with default values for a new instance
            Dots = new ArrayList<>();
        }

        //Timer
        timer = (Button) findViewById(R.id.timerText);
        Button timerButton = (Button) findViewById(R.id.timerButton);
        timerButton.setText("start");
        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button timerButton = (Button) v;
                if (timerButton.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    timerButton.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    timerButton.setText("stop");
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
            case R.id.button1:
                Toast.makeText(this, "Goal", Toast.LENGTH_SHORT).show();
                field.setColor(Color.BLUE);
//              goals++;
//              goalButton.setText("" + goals);
//              // Save data for the app:
//              editor.putInt(getString(R.string.saved_goals), goals);
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

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


}


