package com.iit.testprovider.main.core;

import android.util.Log;

import com.iit.testprovider.main.wrapper.ListItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by slim on 17/11/15.
 */
public class RecordsHelper {

    private volatile static RecordsHelper mInstance = null;

    private LinkedHashMap<Long, ListItemWrapper> mRecordsHashMap;


    private RecordsHelper() {
        mRecordsHashMap = new LinkedHashMap<>();
    }

    public static synchronized RecordsHelper getInstance() {


        if (mInstance == null) {
            mInstance = new RecordsHelper();
        }


        return mInstance;
    }


    public void addRecord(ListItemWrapper itemWrapper) {
        mRecordsHashMap.put(itemWrapper.getId(), itemWrapper);
    }

    public int getSize() {
        return mRecordsHashMap.size();
    }

    public ListItemWrapper get(int position) {
        ListItemWrapper result = (ListItemWrapper) new ArrayList(mRecordsHashMap.values()).get(position);
        return result;
    }

    public void remove(int position) {
        ListItemWrapper listItemWrapper = get(position);
        mRecordsHashMap.remove(listItemWrapper.getId());
    }

    public int indexOf(ListItemWrapper item) {
        return new ArrayList(mRecordsHashMap.values()).indexOf(item);
    }

}
