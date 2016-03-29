package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.football_pitch);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    protected void onDestroy() {
        // Fix from stackoverflow for crashing on rotation.
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }

        super.onDestroy();
    }


}
