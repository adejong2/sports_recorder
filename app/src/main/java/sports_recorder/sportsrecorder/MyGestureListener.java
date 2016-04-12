//package sports_recorder.sportsrecorder;

import android.view.GestureDetector;
import android.view.MotionEvent;

class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "Gestures ";

    @Override
    public boolean onDown(MotionEvent event) {
        System.out.println(DEBUG_TAG + "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        System.out.println(DEBUG_TAG + "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        System.out.println(DEBUG_TAG + "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        System.out.println(DEBUG_TAG + "onLongPress: " + event.toString());
    }

}
