package com.hfad.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class TopLevelActivity extends Activity {


    /*
     private variables so we can access them in both
     setUpFavoritesListView() and onDestroy()
     */
    private SQLiteDatabase db;
    private Cursor favoritesCursor;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);



        setupOptionsListView();
        setupFavoritesListView();

    }

    private void setupOptionsListView()
    {
        // Creates a listener (onItemClickListener) to listen for clicks
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
        {

            // When the user clicks on an item in the list view, the
            // OnItemClickListener's onItemClick() gets called.
            public void onItemClick(AdapterView<?> listView,
                                                    View itemView,
                                                        int position,
                                                            long id) {
                if (position == 0) {
                    // After the onItemClick() is called, it creates an intent to start the next Activity
                    Intent intent = new Intent(TopLevelActivity.this,                   // we are here
                                                                DrinkCategoryActivity.class);            // and we want to be here
                    startActivity(intent);
                }
            }
        };

        // Assign the listener to the list view
        // The ListView tells the Activity when an item gets clicked so the Activity can react.
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);             // itemClickListener is the listener we just created
    }


    private void setupFavoritesListView()
    {
        // Populate the list_favorites ListView from a cursor
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("DRINK",
                                            new String[] {"_id", "NAME"},
                                                "FAVORITE = 1",
                                                    null, null, null, null);

            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                                                                        android.R.layout.simple_list_item_1,
                                                                            favoritesCursor,
                                                                                new String[] {"NAME"},
                                                                                    new int[] {android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                                            "Database unavailable",
                                                Toast.LENGTH_SHORT);
            toast.show();
        }


        // Navigate to DrinkActivity if a drink is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id)
            {
                Intent intent = new Intent(TopLevelActivity.this,
                                                DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int)id);
                startActivity(intent);

            }
        });

    }












    @Override
    public void onRestart()
    {
        super.onRestart();
        Cursor newCursor = db.query("DRINK",
                                        new String[] {"_id", "NAME"},
                                            "FAVORITE = 1",
                                                    null, null, null, null);
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        favoritesCursor = newCursor;
    }


    // Close the cursor and database in the onDestroy()
    @Override
    public void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
}
