package sports_recorder.sportsrecorder;


import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager gm = null;
    public ArrayList<Game> games;

    // Initialize the list
    public GameManager() {
        games = new ArrayList<>();
    }

    public static GameManager getInstance() {
        if (gm == null) gm = new GameManager();
//        Game game = new Game(null, context);
        return gm;
    }

    public Game get(int position) {
//        if (games.size() > position) {
            return games.get(position);
//        } else {
//            System.out.println("Warning: Games is not large enough!");
//            return null;
//        }
    }

    public void setGame(int position, Bundle bundle, Context context) {
        Game game = new Game(bundle, context);
        if (games.size() == 0)
            games.add(game);
        else
            games.set(position, game);
    }
}
