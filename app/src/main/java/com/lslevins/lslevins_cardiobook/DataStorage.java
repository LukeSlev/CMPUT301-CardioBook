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
 * Created by Luke Slevinsky on 2019-01-13.
 */
public class DataStorage {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private Gson gson;

    // Constructor
    public DataStorage(Context context) {
        this.mContext = context;
        this.gson = new Gson();
    }

    public void writeToFile(List<CardioStats> cardioStats) {
        String statsToWrite = gson.toJson(cardioStats);
        File f = new File(mContext.getFilesDir(),mContext.getString(R.string.file_name));

        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(statsToWrite.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        }
    }

    public ArrayList<CardioStats> loadStats() {
        File f = new File(mContext.getFilesDir(),mContext.getString(R.string.file_name));
        byte[] statBytes = new byte[(int)f.length()];

        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(statBytes);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "writeToFile: "+e.getMessage());
        }

        String cardioStats = new String(statBytes);
        // Deserialization
        Type collectionType = new TypeToken<ArrayList<CardioStats>>(){}.getType();
        ArrayList<CardioStats> cs = gson.fromJson(cardioStats, collectionType);
        Log.d(TAG, "loadStats: size:"+cs.size());

        if (cs.size() > 0) Log.d(TAG, "loadStats: last:"+cs.get(cs.size()-1).toString());
        return (cs == null) ? new ArrayList<CardioStats>() : cs;
    }

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

    public void updateStat(CardioStats cardioStats) {

    }

    public void addStat(CardioStats cardioStat) {
        ArrayList<CardioStats> cs = loadStats();
        cs.add(cardioStat);
        writeToFile(cs);
    }

    public void resetMemory(){
        ArrayList<CardioStats> cs = new ArrayList<CardioStats>();
        writeToFile(cs);
    }

}
