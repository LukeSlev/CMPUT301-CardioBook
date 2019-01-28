package com.lslevins.lslevins_cardiobook;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class that takes care of all data storage. Data is stored in the filesystem in file
 *  cardiostats.txt
 *
 * Ideas and code structure was based off of the GSON tutorials on their github:
 *       https://github.com/google/gson/blob/master/UserGuide.md
 *
 * @author Luke Slevinsky
 */
public class DataStorage {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private Gson gson;

    /**
     * Parametrized Constructor
     *
     * @param context The current application context
     */
    public DataStorage(Context context) {
        this.mContext = context;
        this.gson = new Gson();
    }

    /**
     * Method to write a list of CardioStats to the file system
     *
     * @param cardioStats The new list to write to the filesystem
     */
    public void writeToFile(List<CardioStats> cardioStats) {
        String statsToWrite = gson.toJson(cardioStats);

        try {
            FileOutputStream fos = mContext.openFileOutput(mContext.getString(R.string.file_name), Context.MODE_PRIVATE);
            fos.write(statsToWrite.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        }
    }

    /**
     * Method to load data from the filesystem
     *
     * @return A list of CardioStats. It is an empty list if nothing is stored
     */
    public ArrayList<CardioStats> loadStats() {
        File f = new File(mContext.getFilesDir(),mContext.getString(R.string.file_name));
        byte[] statBytes = new byte[(int)f.length()];

        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(statBytes);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
            return new ArrayList<CardioStats>();
        } catch (IOException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
            throw new RuntimeException(e);
        }

        String cardioStats = new String(statBytes);
        // Deserialization
        Type collectionType = new TypeToken<ArrayList<CardioStats>>(){}.getType();
        ArrayList<CardioStats> cs = gson.fromJson(cardioStats, collectionType);
        Log.d(TAG, "loadStats: size:"+cs.size());

        if (cs.size() > 0) Log.d(TAG, "loadStats: last:"+cs.get(cs.size()-1).toString());
        return (cs == null) ? new ArrayList<CardioStats>() : cs;
    }

    /**
     * Method to delete and entry from the stored stats in the file system
     *
     * @param cardioStat The entry to be deleted
     */
    public void deleteStat(CardioStats cardioStat) {
        ArrayList<CardioStats> cs = loadStats();
        int idx = cs.indexOf(cardioStat);
        if (idx > -1) {
            cs.remove(idx);
        } else {
            Log.d(TAG, "deleteStat: ERROR IN DELETION");
        }
        writeToFile(cs);
    }

    /**
     * Debugging method to reset the stored entries to a blank slate
     */
    public void resetMemory(){
        ArrayList<CardioStats> cs = new ArrayList<CardioStats>();
        writeToFile(cs);
    }

}
