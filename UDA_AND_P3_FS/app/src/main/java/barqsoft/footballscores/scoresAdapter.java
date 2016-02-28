package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class scoresAdapter extends CursorAdapter {

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double mDetailMatchId = 0;
    private static final String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public scoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View scoresView = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder scoresViewHolder = new ViewHolder(scoresView);
        scoresView.setTag(scoresViewHolder);
        return scoresView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.homeTeamName.setText(cursor.getString(COL_HOME));
        viewHolder.awayTeamName.setText(cursor.getString(COL_AWAY));
        viewHolder.matchDate.setText(cursor.getString(COL_MATCHTIME));
        viewHolder.matchScore.setText(Utilities.getScores(context, cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        viewHolder.matchId = cursor.getDouble(COL_ID);
        viewHolder.homeTeamCrest.setImageResource(Utilities.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
        viewHolder.awayTeamCrest.setImageResource(Utilities.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));

        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container2);

        if(viewHolder.matchId == mDetailMatchId) {
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView matchDayTextView = (TextView) v.findViewById(R.id.matchday_textview);
            matchDayTextView.setText(Utilities.getMatchDay(context, cursor.getInt(COL_MATCHDAY), cursor.getInt(COL_LEAGUE)));

            TextView leagueTextView = (TextView) v.findViewById(R.id.league_textview);
            leagueTextView.setText(Utilities.getLeague(context, cursor.getInt(COL_LEAGUE)));

            Button shareButton = (Button) v.findViewById(R.id.share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(viewHolder.homeTeamName.getText() + " "
                            + viewHolder.matchScore.getText() + " " + viewHolder.awayTeamName.getText() + " "));
                }
            });
        }
        else  {
            container.removeAllViews();
        }

    }
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}

