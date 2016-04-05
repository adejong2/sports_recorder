package sports_recorder.sportsrecorder;

import android.content.Context;
import android.os.Bundle;
import java.util.Date;


public class Game {
    public String gameDateStr;
    public Date date;
    public int goals, shotsOnGoal, shots, penalties, timeOnClock, half, scoreA, scoreB, halfScoreA, halfScoreB;

    // Constructor
    public Game(Bundle bundle, Context context) {
        if (bundle != null) {
            this.gameDateStr = bundle.getString(context.getString(R.string.saved_game_date), "");
            this.goals = bundle.getInt(context.getString(R.string.saved_goals), 0);
            this.shotsOnGoal = bundle.getInt(context.getString(R.string.saved_shots_on_goal), 0);
            this.shots = bundle.getInt(context.getString(R.string.saved_shots), 0);
            this.penalties = bundle.getInt(context.getString((R.string.saved_penalties)), 0);
            this.timeOnClock = bundle.getInt(context.getString((R.string.saved_time_on_clock)), 0);
            this.half = bundle.getInt(context.getString((R.string.saved_half)), 0);
            this.scoreA = bundle.getInt(context.getString((R.string.saved_scoreA)), 0);
            this.scoreB = bundle.getInt(context.getString((R.string.saved_scoreB)), 0);
            this.halfScoreA = bundle.getInt(context.getString((R.string.saved_half_scoreA)), 0);
            this.halfScoreB = bundle.getInt(context.getString((R.string.saved_half_scoreB)), 0);
        }


    }

    public String toString() {
        return gameDateStr;
    }

}
