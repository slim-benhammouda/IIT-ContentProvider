package com.iit.testprovider.main;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import com.iit.testprovider.main.adapter.CustomAdapter;
import com.iit.testprovider.main.core.RecordsHelper;
import com.iit.testprovider.main.database.TestContentProvider;
import com.iit.testprovider.main.database.tables.RecordsTable;
import com.iit.testprovider.main.ui.AddDialog;
import com.iit.testprovider.main.wrapper.ListItemWrapper;


public class MainFragment extends Fragment implements AddDialog.OnAddListener, LoaderManager.LoaderCallbacks<Cursor>, CustomAdapter.OnItemClickedListener {


    private static final String LIST_CONTENT_KEY = "list_content_key";
    private static final int RECORD_TABLE_ID = 1;

    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private android.support.v4.app.LoaderManager mLoaderManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        mLoaderManager = getLoaderManager();

        if (savedInstanceState == null) {
            mLoaderManager.initLoader(RECORD_TABLE_ID, null, this);
//            mLoaderManager.initLoader(LIST_TABLE_ID, null, this);
//            mLoaderManager.initLoader(ITEM_TABLE_ID, null, this);


        } else {
            //TODO mObjectList = (ArrayList<ListItemWrapper>) savedInstanceState.getSerializable(LIST_CONTENT_KEY);
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CustomAdapter(this);
        recyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        //TODO outState.putSerializable(LIST_CONTENT_KEY, mObjectList);

        super.onSaveInstanceState(outState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            AddDialog addDialog = AddDialog.newInstance(this);
            addDialog.show(getActivity().getSupportFragmentManager(), "");
            return true;
        } else if (id == R.id.action_linear) {

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            return true;
        } else if (id == R.id.action_grid) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
            recyclerView.setLayoutManager(mLayoutManager);

            return true;
        } else if (id == R.id.action_horizontal_stagged) {
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);

            recyclerView.setLayoutManager(mLayoutManager);

            return true;
        } else if (id == R.id.action_vertical_stagged) {
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(mLayoutManager);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkClicked(ListItemWrapper listItemWrapper) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordsTable.LABEL, listItemWrapper.getTitle());
        contentValues.put(RecordsTable.DESCRIPTION, listItemWrapper.getDescription());

        Uri uri = getActivity().getContentResolver().insert(
                TestContentProvider.RECORDS_CONTENT_URI,
                contentValues);

        listItemWrapper.setId(Long.valueOf(uri.getLastPathSegment()));
        RecordsHelper.getInstance().addRecord(listItemWrapper);
//        mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemInserted(RecordsHelper.getInstance().getSize() - 1);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = null;
        if (id == RECORD_TABLE_ID) {
            cursorLoader = new CursorLoader(getActivity().getApplicationContext(), TestContentProvider.RECORDS_CONTENT_URI,
                    RecordsTable.PROJECTION_ALL, null, null, null);
        }
        //else if (id == LIST_TABLE_ID) {
//
//        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("slim", "onLoadFinished called");
        if (loader.getId() == RECORD_TABLE_ID) {
            Log.v("slim", "onLoadFinished: RecordTable");

            if (data.moveToFirst()) {
                while (!data.isAfterLast()) {
                    RecordsHelper.getInstance().addRecord(createListItem(data));
                    if (!data.isClosed()) {
                        data.moveToNext();
                    }
                }

                mAdapter.notifyDataSetChanged();
            }

            //Do this only if you no longer need the loader
            data.close();
            mLoaderManager.destroyLoader(RECORD_TABLE_ID);
        }


    }

    @Override
    public void onLoaderReset(Loader loader) {
//do nothing
    }


    private ListItemWrapper createListItem(Cursor data) {

        ListItemWrapper listItemWrapper = new ListItemWrapper();

        listItemWrapper.setId(data.getLong(data.getColumnIndex(RecordsTable._ID)));
        listItemWrapper.setTitle(data.getString(data.getColumnIndex(RecordsTable.LABEL)));
        listItemWrapper.setDescription(data.getString(data.getColumnIndex(RecordsTable.DESCRIPTION)));

        return listItemWrapper;
    }

    @Override
    public void onItemClicked(int position) {


        //delete the clicked item in our case, can be modified


        getActivity().getContentResolver().delete(
                ContentUris.withAppendedId(
                        TestContentProvider.RECORDS_CONTENT_URI, RecordsHelper.getInstance().get(position).getId()),
                null, null);

        RecordsHelper.getInstance().remove(position);
        mAdapter.notifyItemRemoved(position);

    }
}