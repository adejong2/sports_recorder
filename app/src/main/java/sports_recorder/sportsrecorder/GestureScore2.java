package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class GestureScore2 extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private Button score2Button;
    private MainActivity activity;
    private GestureDetector mDetector;
    private final int SCORE = 2;
    private final String SOURCE = "GestureScore" + SCORE + " ";
    private final int PLUS_ONE = 1;
    private final int MINUS_ONE = -1;
    private final int PLUS_TWO = 2;

    public GestureScore2(Activity activity) {
        this.activity = (MainActivity) activity;
        score2Button = (Button) activity.findViewById(R.id.score2);
        mDetector = new GestureDetector(activity, this);
        score2Button.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        System.out.println(SOURCE + "onSingleTapConfirmed: " + event.toString());
        activity.changeScore(SCORE, PLUS_ONE);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        System.out.println(SOURCE + "onDoubleTap: " + event.toString());
        activity.changeScore(SCORE, PLUS_TWO);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        boolean useX =  (Math.abs(velocityX) >= Math.abs(velocityY));
        float absmax;
        if (useX)
            absmax = velocityX;
        else
            absmax = -velocityY;

        if (absmax >= 0)
            activity.changeScore(SCORE, PLUS_ONE);
        else
            activity.changeScore(SCORE, MINUS_ONE);

        return true;
    }

}
