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
    private Game game = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_detail, container, false);
    }

    // Tell the fragment which game's stats to display
    public void loadPosition(int position) {
        Game g = gm.get(position);
        game = g;

        Activity activity = getActivity();
        TextView tv = (TextView) activity.findViewById(R.id.editDateText);
        tv.setText(g.gameDateStr);

        tv = (TextView) activity.findViewById(R.id.editScoreText);
        String s = g.scoreA + " to " + g.scoreB;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editHalfScoreText);
        s = g.halfScoreA + " to " + g.halfScoreB;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editTimeText);
        s = "" + g.timeOnClock;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editGoalText);
        s = "" + g.goals;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editSOGText);
        s = "" + g.shotsOnGoal;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editShotsText);
        s = "" + g.shots;
        tv.setText(s);

        tv = (TextView) activity.findViewById(R.id.editPenaltiesText);
        s = "" + g.penalties;
        tv.setText(s);
    }

    public void onClick(View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
    }

    public void sendEmail() {
        if (game == null) {
            System.out.println("Fragment's game is null!");
            return;
        }

        Intent intent = new Intent();
        intent.setType("text/plain");
        String subject = "Sports Recorder: Game on " + game.gameDateStr.substring(0, game.gameDateStr.length() - 5);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getString(R.string.game_date));
//        stringBuilder.append(' ');
        stringBuilder.append(game.gameDateStr);
        stringBuilder.append('\n');

        stringBuilder.append(getString(R.string.game_score));
        stringBuilder.append((game.scoreA + " to " + game.scoreB + '\n'));

        String body = stringBuilder.toString();
        intent.putExtra(Intent.EXTRA_TEXT, body);

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

}
