package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

// Code from class contact example:
public class SummaryDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_detail);
        FragmentManager fragmentManager = getFragmentManager();
        SummaryDetailFragment fragment = (SummaryDetailFragment) fragmentManager.findFragmentById(R.id.fragment_summary_detail);
        if (fragment != null) {
            int position = 0;
            position = getIntent().getIntExtra("POSITION",0);
            fragment.loadPosition(position);
        }
    }
}
