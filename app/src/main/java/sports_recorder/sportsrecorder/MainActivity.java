package sports_recorder.sportsrecorder;

import android.app.Activity;
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


}
