package sports_recorder.sportsrecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class FieldDots extends View implements View.OnTouchListener {
    public static int SMALL_RADIUS = 10;
    public static int MEDIUM_RADIUS = 20;
    public static int LARGE_RADIUS = 40;
    public static int AREA_RADIUS = 0;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int dotRadius;
    private HashMap pointerMap;


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
        pointerMap = new HashMap();
        setOnTouchListener(this);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void setDotRadius(int r) {
        dotRadius = r;
    }

    public void setColor(int c) {
        mPaint.setColor(c);
    }

    public void eraseCanvas() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
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
                mCanvas.drawCircle(x, y, dotRadius, mPaint);
//                System.out.println("Touch coordinates : " +
//                        String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                System.out.println("x: " + x +", y: " + y);
                invalidate();
                break;
            default:
                break;

        }
        return true;
    }
}