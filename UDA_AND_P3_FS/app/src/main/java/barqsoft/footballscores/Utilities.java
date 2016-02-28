package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {

    public static final int BUNDESLIGA = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int EREDIVISIE= 404;
    public static final int PRIMEIRA_LIGA = 402;
    public static final int BUNDESLIGA3 = 403;
    public static final int CHAMPIONS_LEAGUE = 405;

    public static String getLeague(Context c, int league_num) {
        Resources r = c.getResources();
        switch (league_num)
        {
            case SERIE_A : return r.getString(R.string.seriaa);
            case PREMIER_LEAGUE : return r.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE : return r.getString(R.string.champions_league);
            case PRIMERA_DIVISION : return r.getString(R.string.primeradivison);
            case SEGUNDA_DIVISION : return r.getString(R.string.segundaadivison);
            case BUNDESLIGA : return r.getString(R.string.bundesliga);
            case BUNDESLIGA2 : return r.getString(R.string.bundesliga2);
            case BUNDESLIGA3 : return r.getString(R.string.bundesliga3);
            case LIGUE1 : return r.getString(R.string.ligue1);
            case LIGUE2 : return r.getString(R.string.ligue2);
            case EREDIVISIE : return r.getString(R.string.eredivisie);
            case PRIMEIRA_LIGA : return r.getString(R.string.primeiraliga);
            default: return r.getString(R.string.unknown_league);
        }
    }

    public static String getMatchDay(Context c, int match_day, int league_num) {
        Resources r = c.getResources();
        if(league_num == CHAMPIONS_LEAGUE) {
            if (match_day > 6) {
                if (match_day == 7 || match_day == 8) {
                    return r.getString(R.string.first_knockout_round);
                } else if (match_day == 9 || match_day == 10) {
                    return r.getString(R.string.quarter_final);
                } else if (match_day == 11 || match_day == 12) {
                    return r.getString(R.string.semi_final);
                } else {
                    return r.getString(R.string.final_text);
                }
            } else {
                return r.getString(R.string.group_stage_text) + String.valueOf(match_day);
            }
        }
        return r.getString(R.string.matchday_text) + " " + String.valueOf(match_day);
    }

    public static String getScores(Context c, int home_goals,int awaygoals) {
        Resources r = c.getResources();
        if(home_goals < 0 || awaygoals < 0) {
            return r.getString(R.string.score_divider);
        }
        else {
            return String.valueOf(home_goals) + r.getString(R.string.score_divider) + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname) {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname) {
            case "Arsenal FC" : return R.drawable.arsenal;
            case "Aston Villa" : return R.drawable.aston_villa;
            case "Chelsea FC" : return R.drawable.chelsea;
            case "Crystal Palace FC" : return R.drawable.crystal_palace_fc;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "Leicester City FC" : return R.drawable.leicester_city_fc_hd_logo;
            case "Liverpool FC" : return R.drawable.liverpool;
            case "Manchester City FC" : return R.drawable.manchester_city;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Newcastle United FC" : return R.drawable.newcastle_united;
            case "Southampton FC" : return R.drawable.southampton_fc;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Swansea City FC" : return R.drawable.swansea_city_afc;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion FC" : return R.drawable.west_bromwich_albion_hd_logo;
            case "West Ham United FC" : return R.drawable.west_ham;
            default: return R.drawable.football;
        }
    }
}
