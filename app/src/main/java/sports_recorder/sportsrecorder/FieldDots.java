package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

public class FieldDots extends View implements View.OnTouchListener {
    public static int SMALL_RADIUS = 10;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int dotRadius;
    private WeakHashMap pointerMap;


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
        dotRadius = SMALL_RADIUS;
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
        mCanvas.drawCircle(200, 200, dotRadius, mPaint);

        drawAllDots();
    }

    public boolean onTouch(View v, MotionEvent event) {
        // Log.d("DEBUG", "Receiving touch event");
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        float x = event.getX(index);
        float y = event.getY(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                // If the event type is uninitialized, skip it
                if (MainActivity.getEventType() == R.string.event_type_null)
                    break;

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
                setColor(Color.CYAN);
                break;
            case R.string.event_type_shot_on_goal:
                setColor(Color.BLUE);
                break;
            case R.string.event_type_shot:
                setColor(Color.LTGRAY);
                break;
            case R.string.event_type_penalty:
                setColor(Color.RED);
                break;
            case R.string.event_type_null:
                setColor(Color.BLACK);
                break;
        }
    }


    public void clear() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

}