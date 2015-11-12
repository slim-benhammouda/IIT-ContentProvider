package com.iit.testcontentprovider.second;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    private static final String[] PROJECTION_ALL = {BaseColumns._ID, "title", "description"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_read) {
            queryContentProviderWithResolver();
            //queryContentProvider();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryContentProvider() {
        CustomAsyncQueryHandler queryHandler = new CustomAsyncQueryHandler(getContentResolver());


        // Construct query and execute
        queryHandler.startQuery(
                1, null,
                Uri.parse("content://com.iit.testprovider.main.provider/records"),
                PROJECTION_ALL,
                null,
                null,
                null
        );
    }

    private void queryContentProviderWithResolver() {
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://com.iit.testprovider.main.provider/records"), PROJECTION_ALL,
                null,
                null,
                null);

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(BaseColumns._ID);
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");


            while (cursor.moveToNext()) {
                long id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);

                Log.d("iit", "ID: " + id + " Title: " + title + " Description: " + description);
            }
            cursor.close();

        }
    }

    private static class CustomAsyncQueryHandler extends AsyncQueryHandler {

        public CustomAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(BaseColumns._ID);
                int titleIndex = cursor.getColumnIndex("title");
                int descriptionIndex = cursor.getColumnIndex("description");


                while (cursor.moveToNext()) {
                    long id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);

                    Log.d("iit", "ID: " + id + " Title: " + title + " Description: " + description);
                }
                cursor.close();
            }
        }
    }

}
