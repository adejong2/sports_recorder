package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener, ListView.OnItemClickListener {
    private GameManager gm = GameManager.getInstance();

    public int goals, shotsOnGoal, shots, penalties;
    int num_points;
    private Button goalButton, sogButton, shotButton, penaltyButton, halfButton, scoreButton1, scoreButton2;
    public static ArrayList<Dot> Dots;
    public static int timeOnClock;      // Time of player on field
    public static int eventType = R.string.event_type_null;
    public static int prevEventType = R.string.event_type_null;
    public boolean clockIsRunning;      // True if the clock should immediately start running
    public boolean dirtyClock = false;  // True if there are pending changes to timeOnClock
    public int scoreA, scoreB, half, halfScoreA, halfScoreB;
    public String gameDateStr;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private GestureDetector mDetector;

    private String items[] = new String[] { "Data Entry","Summary"};

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

        // Gestures detector:
        mDetector = new GestureDetector(this, new MyGestureListener());


        //Disable original title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Create new Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        getActionBar().setElevation(10);

        toolbar.setNavigationIcon(R.drawable.ic_menu_gray757575_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mDrawerListView);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.drawer_list_view);
        mDrawerListView.setOnItemClickListener(this);


        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.nav_drawer_list_item, items);
        mDrawerListView.setAdapter(listAdapter);

        // Set button listeners:
        goalButton = (Button) findViewById(R.id.goalButton);
        goalButton.setOnClickListener(this);
//        goalButton.setOnTouchListener(new MyGestureListener());

        sogButton = (Button) findViewById(R.id.SOGButton);
        sogButton.setOnClickListener(this);

        shotButton = (Button) findViewById(R.id.shotButton);
        shotButton.setOnClickListener(this);

        penaltyButton = (Button) findViewById(R.id.penaltyButton);
        penaltyButton.setOnClickListener(this);

        scoreButton1 = (Button) findViewById(R.id.score1);
//        scoreButton1.setOnClickListener(this);
        scoreButton1.setOnTouchListener(new GestureScore1(this));

        scoreButton2 = (Button) findViewById(R.id.score2);
//        scoreButton2.setOnClickListener(this);
        scoreButton2.setOnTouchListener(new GestureScore2(this));

        halfButton = (Button) findViewById(R.id.halfButton);
        halfButton.setOnClickListener(this);


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
            this.scoreA = savedInstanceState.getInt(getString(R.string.saved_scoreA), 0);
            this.scoreB = savedInstanceState.getInt(getString(R.string.saved_scoreB), 0);
            this.half = savedInstanceState.getInt(getString(R.string.saved_half), 1);
            this.halfScoreA = savedInstanceState.getInt(getString(R.string.saved_half_scoreA), 0);
            this.halfScoreB = savedInstanceState.getInt(getString(R.string.saved_scoreB), 0);
            this.goals = savedInstanceState.getInt(getString(R.string.saved_goals), 0);
            this.shotsOnGoal = savedInstanceState.getInt(getString(R.string.saved_shots_on_goal), 0);
            this.shots = savedInstanceState.getInt(getString(R.string.saved_shots), 0);
            this.penalties = savedInstanceState.getInt(getString(R.string.saved_penalties), 0);
            this.gameDateStr = savedInstanceState.getString(getString(R.string.saved_game_date), "");
            prevEventType = savedInstanceState.getInt(getString(R.string.saved_prev_event_type), R.string.event_type_null);
        } else {
            // Probably initialize members with default values for a new instance
            Dots = new ArrayList<>();
            timeOnClock = 0;
            this.clockIsRunning = false;
            scoreA = 0;
            scoreB = 0;
            half = 1;
            halfScoreA = 0;
            halfScoreB = 0;
            goals = 0;
            shotsOnGoal = 0;
            shots = 0;
            penalties = 0;

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            gameDateStr = formatter.format(today);
        }

//        System.out.println("Date: " + gameDateStr);

        // Scores and Half:
        if (half==1) {
            scoreButton1.setText("" + scoreA);
            scoreButton2.setText("" + scoreB);
        } else {
            halfButton.setText("2nd");
            scoreButton1.setText("" + scoreB);
            int color = scoreButton1.getCurrentTextColor();
            scoreButton1.setTextColor(scoreButton2.getCurrentTextColor());
            scoreButton2.setText("" + scoreA);
            scoreButton2.setTextColor(color);
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
            timerButton.setText("Stop");
            dirtyClock = true;
        } else {timerButton.setText("Start");}

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button timerButton = (Button) v;
                if (timerButton.getText().equals("Stop")) { // Stop the clock
                    timerHandler.removeCallbacks(timerRunnable);
                    timerButton.setText("Start");
                    updateTimeOnClock();
                    clockIsRunning = false;
                } else {    // Start the clock:
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    timerButton.setText("Stop");
                    dirtyClock = true;
                    clockIsRunning = true;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            getActionBar().setTitle("buttonclicked");
//            mDrawerLayout.openDrawer(mDrawerListView);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
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
            case R.id.halfButton:
//                Toast.makeText(this, "Half Time", Toast.LENGTH_SHORT).show();
//                setHalfTime();
                halfTimeAlert();
                break;
            case R.id.score1:
                if ((half % 2) == 1) {
                    scoreA++;
                    scoreButton1.setText("" + scoreA);
                } else {
                    scoreB++;
                    scoreButton1.setText("" + scoreB);
                }
                break;
            case R.id.score2:
                if ((half % 2) == 0) {
                    scoreA++;
                    scoreButton2.setText("" + scoreA);
                } else {
                    scoreB++;
                    scoreButton2.setText("" + scoreB);
                }
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
        saveGameState(savedInstanceState);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    // Save the game state to a bundle for onSaveInstanceState and for summary view
    public void saveGameState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(getResources().getString(R.string.saved_dots_arraylist), Dots);
        updateTimeOnClock();
        savedInstanceState.putInt(getString(R.string.saved_time_on_clock), timeOnClock);
        savedInstanceState.putLong(getString(R.string.saved_start_time), this.startTime);
        savedInstanceState.putBoolean(getString(R.string.saved_clock_is_running), this.clockIsRunning);
        savedInstanceState.putInt(getString(R.string.saved_scoreA), scoreA);
        savedInstanceState.putInt(getString(R.string.saved_scoreB), scoreB);
        savedInstanceState.putInt(getString(R.string.saved_half), half);
        savedInstanceState.putInt(getString(R.string.saved_half_scoreA), halfScoreA);
        savedInstanceState.putInt(getString(R.string.saved_half_scoreB), halfScoreB);
        savedInstanceState.putInt(getString(R.string.saved_goals), goals);
        savedInstanceState.putInt(getString(R.string.saved_shots_on_goal), shotsOnGoal);
        savedInstanceState.putInt(getString(R.string.saved_shots), shots);
        savedInstanceState.putInt(getString(R.string.saved_penalties), penalties);
        savedInstanceState.putString(getString(R.string.saved_game_date), gameDateStr);
        savedInstanceState.putInt(getString(R.string.saved_prev_event_type), prevEventType);

        // Update the game at position 0 to this game:
        gm.setGame(0, savedInstanceState, getApplicationContext());
    }


    public static int getEventType() {
        return eventType;
    }

    public static void setPrevEventType() {
        eventType = prevEventType;
    }

    public static void resetEventType() {
        if (eventType != R.string.event_type_null)
            prevEventType = eventType;
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

    public int getTimeOnClock() {
        long milliseconds = 0;
        if (clockIsRunning)
            milliseconds = System.currentTimeMillis() - this.startTime;
        return timeOnClock + (int) milliseconds / 1000;
    }

    // Process the switch to the second half
    public void setHalfTime() {
        if (half > 1)
            return;;

        Toast.makeText(this, "Half Time", Toast.LENGTH_SHORT).show();



        half = (half % 2) + 1;

        if (half==1)
            halfButton.setText("1st");
        else if (half==2) {
            halfButton.setText("2nd");
            halfScoreA = scoreA;
            halfScoreB = scoreB;

            // Teams switch home fields
            int color = scoreButton1.getCurrentTextColor();
            scoreButton1.setTextColor(scoreButton2.getCurrentTextColor());
            scoreButton2.setTextColor(color);

            scoreButton1.setText("" + scoreB);
            scoreButton2.setText("" + scoreA);
        }

        FieldDots fieldDots = (FieldDots) findViewById(R.id.dots_view);
        fieldDots.clear();
        fieldDots.invalidate();
    }

    public void halfTimeAlert() {

        if (half == 2) {
            Toast.makeText(this, "2nd Half", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(getString(R.string.half_time_alert_title));

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.half_time_alert_dialog))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.confirm),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, change halves
                        setHalfTime();
                    }
                })
                .setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public int getHalf() {
        return this.half;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;

//        Toast.makeText(this, items[position], Toast.LENGTH_SHORT).show();

        if (items[position].equals("Summary")) {

            Intent intent = new Intent(this, SummaryDetailActivity.class);
            intent.putExtra("POSITION", 0); // Game position, not drawer menu position
//            Bundle extras = intent.getExtras();
//            saveGameState(extras); // Pass game info bundle at start. Then need a way need to pass it to the fragment
            Bundle bundle = new Bundle(); // Try just setting game 0:
            saveGameState(bundle);
            gm.setGame(0, bundle, getApplicationContext()); // Sets game 0
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void recordEvent(int eventType) {
        switch (eventType) {
            case R.string.event_type_goal:
                this.goals++;
                this.shotsOnGoal++;
                this.shots++;
                break;
            case R.string.event_type_shot_on_goal:
                this.shotsOnGoal++;
                this.shots++;
                break;
            case R.string.event_type_shot:
                this.shots++;
                break;
            case R.string.event_type_penalty:
                this.penalties++;
                break;
            default:
                System.out.println("Unrecognized event type in MainActivity.recordEvent()");
        }
    }

    // Gestures:
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures ";

//        Button goalButton = (Button) findViewById(R.id.goalButton);
//        goalButton.setOnTouchListener(this);
//        goalButton.setText("Hello");


        @Override
        public boolean onDown(MotionEvent event) {
            System.out.println(DEBUG_TAG + "MainActivity " + "onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            System.out.println(DEBUG_TAG + "MainActivity " + "onFling: " + event1.toString() + event2.toString());
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            System.out.println(DEBUG_TAG + "MainActivity " + "onDoubleTap: " + event.toString());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            System.out.println(DEBUG_TAG + "MainActivity " + "onLongPress: " + event.toString());
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            System.out.println(DEBUG_TAG + "MainActivity " + "onSingleTapConfirmed: " + event.toString());
            return true;
        }

    }

    public boolean onTouch(final View v, final MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    public void changeScore(int score, int change) {
        System.out.println("Gesture changing score");
        switch (score) {
            case 1:
                if ((half % 2) == 1) {
                    scoreA += change;
                    scoreButton1.setText("" + scoreA);
                } else {
                    scoreB += change;
                    scoreButton1.setText("" + scoreB);
                }
                break;
            case 2:
                if ((half % 2) == 1) {
                    scoreB += change;
                    scoreButton2.setText("" + scoreB);
                } else {
                    scoreA += change;
                    scoreButton2.setText("" + scoreA);
                }
                break;
            }
        }

}


