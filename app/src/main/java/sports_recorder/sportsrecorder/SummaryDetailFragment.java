package sports_recorder.sportsrecorder;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryDetailFragment extends Fragment {
    private GameManager gm = GameManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
     return inflater.inflate(R.layout.fragment_summary_detail, container, false);
    }

    // Tell the fragment which game's stats to display
    public void loadPosition(int position) {
        Game g = gm.get(position);
        Activity activity = getActivity();
        TextView tv = (TextView) activity.findViewById(R.id.editDateText);
        tv.setText(g.gameDateStr);
    }

    public void onClick(View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
    }

    public void sendEmail() {
//        Toast.makeText(getActivity().getApplicationContext(), R.string.action_email, Toast.LENGTH_SHORT).show();
    }

}
