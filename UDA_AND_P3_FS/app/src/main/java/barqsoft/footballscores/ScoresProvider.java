package barqsoft.footballscores;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresProvider extends ContentProvider {

    private static ScoresDBHelper mOpenHelper;
    private static final int MATCHES = 100;
    private static final int MATCHES_WITH_LEAGUE = 101;
    private static final int MATCHES_WITH_ID = 102;
    private static final int MATCHES_WITH_DATE = 103;
    private UriMatcher mUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder SCORE_QUERY = new SQLiteQueryBuilder();
    private static final String SCORES_BY_LEAGUE = DatabaseContract.ScoresTable.LEAGUE_COL + " = ?";
    private static final String SCORES_BY_DATE = DatabaseContract.ScoresTable.DATE_COL + " LIKE ?";
    private static final String SCORES_BY_ID = DatabaseContract.ScoresTable.MATCH_ID + " = ?";

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String AUTHORITY = DatabaseContract.BASE_CONTENT_URI.toString();
        matcher.addURI(AUTHORITY, null , MATCHES);
        matcher.addURI(AUTHORITY, "league" , MATCHES_WITH_LEAGUE);
        matcher.addURI(AUTHORITY, "id" , MATCHES_WITH_ID);
        matcher.addURI(AUTHORITY, "matchDate" , MATCHES_WITH_DATE);
        return matcher;
    }

    private int matchUri(Uri uri) {

        String link = uri.toString();
        {
           if(link.contentEquals(DatabaseContract.BASE_CONTENT_URI.toString())) {
               return MATCHES;
           }
           else if(link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithDate().toString())) {
               return MATCHES_WITH_DATE;
           }
           else if(link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithId().toString())) {
               return MATCHES_WITH_ID;
           }
           else if(link.contentEquals(DatabaseContract.ScoresTable.buildScoreWithLeague().toString()))   {
               return MATCHES_WITH_LEAGUE;
           }
        }
        return -1;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ScoresDBHelper(getContext());
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_LEAGUE:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return DatabaseContract.ScoresTable.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_DATE:
                return DatabaseContract.ScoresTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri );
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = matchUri(uri);

        switch (match) {
            case MATCHES: retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.SCORES_TABLE,
                    projection,null,null,null,null,sortOrder);
                    break;

            case MATCHES_WITH_DATE:
                    retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.SCORES_TABLE,
                    projection,SCORES_BY_DATE,selectionArgs,null,null,sortOrder);
                    break;

            case MATCHES_WITH_ID: retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.SCORES_TABLE,
                    projection,SCORES_BY_ID,selectionArgs,null,null,sortOrder);
                    break;

            case MATCHES_WITH_LEAGUE: retCursor = mOpenHelper.getReadableDatabase().query(
                    DatabaseContract.SCORES_TABLE,
                    projection,SCORES_BY_LEAGUE,selectionArgs,null,null,sortOrder);
                    break;

            default: throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (matchUri(uri)) {
            case MATCHES:
                db.beginTransaction();
                int count = 0;
                try {
                    for(ContentValues value : values) {
                        long id = db.insertWithOnConflict(DatabaseContract.SCORES_TABLE, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            count++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
