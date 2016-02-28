package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.text.format.Time;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by R.Pendlebury on 13/01/2016.
 */
public class ListWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor mCursor = null;
            private int mCount;
            private String[] mSearchDate = new String[NUM_DAYS];
            private List<String> mMatchList;

            // The number of days/pages we will be loading
            private static final int NUM_DAYS = 5;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (mCursor != null) {
                    mCursor.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // mCursor. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission

                final long identityToken = Binder.clearCallingIdentity();
                mMatchList = queryData();
                mCount = mMatchList.size();
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (mCursor != null) {
                    mCursor.close();
                    mCursor = null;
                }
                mMatchList.clear();
            }

            @Override
            public int getCount() {
                return mCount;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.stack_widget_item);
                views.setTextViewText(R.id.widget_item, mMatchList.get(position));
                Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                views.setOnClickFillInIntent(R.id.widget_item, launchIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.stack_widget_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            private List<String> queryData() {
                List<String> matchList = new ArrayList<>();
                for (int i = 0;i < NUM_DAYS;i++)
                {
                    Date tmpDate = new Date(System.currentTimeMillis()+((i-2)*86400000));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    mSearchDate[i] = simpleDateFormat.format(tmpDate);
                }

                for (int i = mSearchDate.length-1 ; i > -1 ; i--){
                    String[] tmpDate = new String[1];
                    tmpDate[0] = mSearchDate[i];
                    mCursor = getContentResolver().query(
                            DatabaseContract.ScoresTable.buildScoreWithDate(),
                            null,
                            null,
                            tmpDate,
                            null);

                    String dayName = getDayName(getApplicationContext(), System.currentTimeMillis()+((i-2)*86400000));
                    matchList.add(dayName);

                    if (mCursor != null && mCursor.getCount() > 0) {
                        if (mCursor.moveToFirst()) {
                            do {
                                mCount++;
                                String midString = "";
                                if (mCursor.getString(6).equals("-1") && mCursor.getString(7).equals("-1")){
                                    midString = "v";
                                } else {

                                    midString =  mCursor.getString(6) + " - " + mCursor.getString(7);
                                }
                                String s = mCursor.getString(3) +
                                        " " +
                                        midString +
                                        " " + mCursor.getString(4);
                                matchList.add(s);
                            } while (mCursor.moveToNext());
                        }
                    }
                    mCursor.close();
                }
                return matchList;
            }

            public String getDayName(Context context, long dateInMillis) {
                // If the matchDate is today, return the localized version of "Today" instead of the actual
                // day name.
                Time t = new Time();
                t.setToNow();
                int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
                int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
                if (julianDay == currentJulianDay) {
                    return context.getString(R.string.today);
                } else if ( julianDay == currentJulianDay +1 ) {
                    return context.getString(R.string.tomorrow);
                }
                else if ( julianDay == currentJulianDay -1) {
                    return context.getString(R.string.yesterday);
                }
                else {
                    Time time = new Time();
                    time.setToNow();
                    // Otherwise, the format is just the day of the week (e.g "Wednesday".
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                    return dayFormat.format(dateInMillis);
                }
            }
        };


    }
}


