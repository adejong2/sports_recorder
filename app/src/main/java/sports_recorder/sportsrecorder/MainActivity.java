package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    int goals = 1; // Change this to zero later
    private Button goalButton;


    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


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
        Gson gson = new Gson();

        // Load the data values for the app:
//        int defaultValue = getResources().getInteger(R.string.saved_goals_default);
        int defaultValue = 1;
        goals = sharedPref.getInt(getString(R.string.saved_goals), defaultValue);
        goalButton.setText("" + goals);

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

        FieldDots imageView = (FieldDots)findViewById(R.id.dots_view);

        if(imageView.getBackground() != null &&
                BitmapDrawable.class.isInstance(imageView.getBackground()))
        {
            ((BitmapDrawable)imageView.getBackground()).getBitmap().recycle();
        }

        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            Toast.makeText(this, "GOAL!!!", Toast.LENGTH_SHORT).show();
            goals++;
            goalButton.setText("" + goals);
            // Save data for the app:
            editor.putInt(getString(R.string.saved_goals), goals);
        }
    }

    @Override
    public void onPause() {
        super.onPause();    // Call the superclass method first.

        editor.commit();    // Save data for the app:
    }

}


