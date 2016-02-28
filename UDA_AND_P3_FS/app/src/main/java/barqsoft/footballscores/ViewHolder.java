package barqsoft.footballscores;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {

    public TextView homeTeamName;
    public TextView awayTeamName;
    public TextView matchScore;
    public TextView matchDate;
    public ImageView homeTeamCrest;
    public ImageView awayTeamCrest;
    public double matchId;

    public ViewHolder(View view) {
        homeTeamName = (TextView) view.findViewById(R.id.home_name);
        awayTeamName = (TextView) view.findViewById(R.id.away_name);
        matchScore = (TextView) view.findViewById(R.id.score_textview);
        matchDate = (TextView) view.findViewById(R.id.match_time_textview);
        homeTeamCrest = (ImageView) view.findViewById(R.id.home_crest);
        awayTeamCrest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
