package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

public class FieldDots extends View implements View.OnTouchListener {
    public static int SMALL_RADIUS = 10;
    public static int MEDIUM_RADIUS = 20;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int dotRadius;
    private WeakHashMap pointerMap;

    private GestureDetector mDetector = new GestureDetector(getContext(), new MyGestureListener());

    public FieldDots(Context context) {
        super(context);
        initDotsView();
    }

    public FieldDots(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDotsView();
    }

    public FieldDots(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDotsView();
    }

//    public DotsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initDotsView();
//    }

    private void initDotsView() {
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        dotRadius = MEDIUM_RADIUS;
        pointerMap = new WeakHashMap();
        setOnTouchListener(this);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        drawAllDots();
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

//    public boolean onTouch(View v, MotionEvent event) {
//        // Log.d("DEBUG", "Receiving touch event");
//        int action = event.getActionMasked();
//        int index = event.getActionIndex();
//        int id = event.getPointerId(index);
//        float x = event.getX(index);
//        float y = event.getY(index);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//
//                // If the event type is uninitialized, skip it
//                if (MainActivity.getEventType() == R.string.event_type_null) {
//                    Toast.makeText(getContext(), "Select event type below", Toast.LENGTH_SHORT).show();
//
//                    break;
//                }
//
//                Dot d = new Dot();
//                if (isPortrait()) {
//                    d.x = x / mBitmap.getWidth();// *getResources().getDisplayMetrics().density;
//                    d.y = y / mBitmap.getHeight(); //*getResources().getDisplayMetrics().density;
//                } else {
//                    d.x = 1 - y / mBitmap.getHeight();
//                    d.y = x / mBitmap.getWidth();
//                }
//                MainActivity main = (MainActivity) getContext();
//                d.timestamp = main.getTimeOnClock();
//                d.half = main.getHalf();
//                System.out.println("d.half: " + d.half + ", main.half: " + main.getHalf());
////                System.out.println("timestamp: " + d.timestamp);
//                d.type = MainActivity.getEventType();
//                main.recordEvent(d.type);
//                MainActivity.Dots.add(d);
//                MainActivity.resetEventType();
//                drawDot(d);
////                mCanvas.drawCircle(x, y, dotRadius, mPaint);
////                System.out.println("Touch coordinates : " +
////                        String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
//                System.out.println("x: " + x +", y: " + y);
//                System.out.println("d.x: " + d.x +", d.y: " + d.y);
//                invalidate();
//                break;
//            default:
//                break;
//
//        }
//        return true;
//    }


    public void drawDot(Dot d) {
        MainActivity main = (MainActivity) getContext();
        if (d.half != main.getHalf())
            return;

        float drawx;
        float drawy;
        if (isPortrait()) {
            drawx = d.x*mBitmap.getWidth(); ///getResources().getDisplayMetrics().density;
            drawy = d.y*mBitmap.getHeight(); ///getResources().getDisplayMetrics().density;
        } else {
            drawx = d.y *mBitmap.getWidth();
            drawy = (1 - d.x)*mBitmap.getHeight();
        }
//        System.out.println();
        setColorByEvent(d.type);

        mCanvas.drawCircle(drawx, drawy, dotRadius, mPaint);
    }

    public void drawAllDots() {
        if (MainActivity.Dots != null) {
            for (Dot d : MainActivity.Dots) {
                drawDot(d);
            }
        } else {
            System.out.println("Dots is null!");
        }
    }

    public boolean isPortrait() {
//        boolean ret = mBitmap.getHeight() > mBitmap.getWidth();
//        if (ret) System.out.println("In portrait mode.");
//        else System.out.println("In landscape mode.");
//        return ret;
        return mBitmap.getHeight() > mBitmap.getWidth();
    }

    public void setColor(int c) {
        mPaint.setColor(c);
    }

    public void setColorByEvent(int event) {
        switch (event) {
            case R.string.event_type_goal:
//                setColor(Color.CYAN);
                setColor(getResources().getColor(R.color.gold));
                break;
            case R.string.event_type_shot_on_goal:
//                setColor(Color.BLUE);
//                setColor(getResources().getColor(R.color.magentaish));
                setColor(Color.CYAN);
                break;
            case R.string.event_type_shot:
                setColor(Color.LTGRAY);
                break;
            case R.string.event_type_penalty:
//                setColor(Color.RED);
                setColor(getResources().getColor(R.color.pink));
                break;
            case R.string.event_type_null:
                setColor(Color.BLACK);
                break;
        }
    }


    public void clear() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    //Gestures:
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
            //Toast.makeText(getContext(), "Long Pressed", Toast.LENGTH_SHORT).show();
            float userX = event.getX();
            float userY = event.getY();



            if (MainActivity.Dots != null) {
                System.out.println("longpress1");
                float minDiff = 2500;
                Dot minDot = null;
                for (Dot d : MainActivity.Dots) {
                    System.out.println("longpress2");
/*                    if ((d.x - userX) * (d.x - userX) + (d.y - userY) * (d.y - userY) <= dotRadius * dotRadius) {
                        System.out.println("longpress3");
                        Toast.makeText(getContext(), "Inside Circle", Toast.LENGTH_SHORT).show();
                        break;
                    }  */

                    float dotX;
                    float dotY;

                    if (isPortrait()) {
                        dotX = d.x * mBitmap.getWidth();
                        dotY = d.y * mBitmap.getHeight();
                    } else {
                        dotX = d.y * mBitmap.getWidth();
                        dotY = (1 - d.x) * mBitmap.getHeight();
                    }

//                    float diff = ((d.x - userX) * (d.x - userX) + (d.y - userY) * (d.y - userY));
                    float diff = ((dotX - userX) * (dotX - userX) + (dotY - userY) * (dotY - userY));

                    System.out.println("userX=" + userX + " userY=" + userY + " dotX=" + dotX + "dotY=" + dotY);
                    if (diff <= minDiff || minDot == null) {
                        System.out.println("minDiff=" + minDiff + " current diff=" + diff);
                        minDot = d;
                        minDiff = diff;
                    }
                }

                if (minDot != null) {
                    MainActivity main = (MainActivity) getContext();
                    if (minDot.half != main.getHalf()) {
                        System.out.println("longpress wrong half");
                        return;
                    }
                    System.out.println("longpress minDot not null");
                    //setColor(getResources().getColor(R.color.gold));
                    //mCanvas.drawCircle(minDot.x, minDot.y, dotRadius, mPaint);
                    float drawx;
                    float drawy;
                    if (isPortrait()) {
                        drawx = minDot.x*mBitmap.getWidth(); ///getResources().getDisplayMetrics().density;
                        drawy = minDot.y*mBitmap.getHeight(); ///getResources().getDisplayMetrics().density;
                    } else {
                        drawx = minDot.y *mBitmap.getWidth();
                        drawy = (1 - minDot.x)*mBitmap.getHeight();
                    }
//        System.out.println();
                    //setColorByEvent(minDot.type);
//                    setColor(getResources().getColor(R.color.gold));
                    setColor(Color.BLACK);

                    mCanvas.drawCircle(drawx, drawy, 30, mPaint);
                    System.out.println("longpress x, y " + drawx + " , " + drawy);
                    invalidate();
                }
            } else {
                System.out.println("Dots is null!");
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            System.out.println(DEBUG_TAG + "onSingleTapConfirmed: " + event.toString());
            mySingleTap(event);
            return true;
        }


    }

        public boolean mySingleTap(MotionEvent event) {
        // Log.d("DEBUG", "Receiving touch event");
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        float x = event.getX(index);
        float y = event.getY(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                // If the event type is uninitialized, skip it
                if (MainActivity.getEventType() == R.string.event_type_null) {
                    Toast.makeText(getContext(), "Select event type below", Toast.LENGTH_SHORT).show();

                    break;
                }

                Dot d = new Dot();
                if (isPortrait()) {
                    d.x = x / mBitmap.getWidth();// *getResources().getDisplayMetrics().density;
                    d.y = y / mBitmap.getHeight(); //*getResources().getDisplayMetrics().density;
                } else {
                    d.x = 1 - y / mBitmap.getHeight();
                    d.y = x / mBitmap.getWidth();
                }
                MainActivity main = (MainActivity) getContext();
                d.timestamp = main.getTimeOnClock();
                d.half = main.getHalf();
                System.out.println("d.half: " + d.half + ", main.half: " + main.getHalf());
//                System.out.println("timestamp: " + d.timestamp);
                d.type = MainActivity.getEventType();
                main.recordEvent(d.type);
                MainActivity.Dots.add(d);
                MainActivity.resetEventType();
                drawDot(d);
//                mCanvas.drawCircle(x, y, dotRadius, mPaint);
//                System.out.println("Touch coordinates : " +
//                        String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                System.out.println("x: " + x +", y: " + y);
                System.out.println("d.x: " + d.x +", d.y: " + d.y);
                invalidate();
                break;
            default:
                break;

        }
        return true;
    }



}