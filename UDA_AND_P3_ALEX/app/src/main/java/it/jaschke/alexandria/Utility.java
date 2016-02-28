package it.jaschke.alexandria;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import it.jaschke.alexandria.services.BookService;

/**
 * Created by R.Pendlebury on 10/01/2016.
 */

public class Utility {

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    // Need to suppress warnings because the int returned could be out of the range of our IntDef options
    @SuppressWarnings("ResourceType")
    static public @BookService.NetworkStatus
    int getNetworkStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_network_status_key), BookService.NETWORK_STATUS_UNKNOWN);
    }

    /**
     * Resets the location status.  (Sets it to BookService.NETWORK_STATUS_UNKNOWN)
     * @param c Context used to get the SharedPreferences
     */
    static public void resetNetworkStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_network_status_key), BookService.NETWORK_STATUS_UNKNOWN);
        spe.apply(); // use apply() rather than commit() since this will be called from the UI thread
    }

}
