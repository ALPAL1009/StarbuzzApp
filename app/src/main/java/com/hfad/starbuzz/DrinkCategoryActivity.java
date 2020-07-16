package com.hfad.starbuzz;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;




public class DrinkCategoryActivity extends Activity
{
    // These private variables are added so we can close the database and cursor in out onDestroy().
    private SQLiteDatabase db;
    private Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);




        ListView listDrinks = (ListView) findViewById(R.id.list_drinks);
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            db = starbuzzDatabaseHelper.getReadableDatabase();  // Get a reference to the database
            cursor = db.query("DRINK",
                                        new String[] {"_id", "NAME"},
                                            null, null, null, null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                                                                        android.R.layout.simple_list_item_1,        // this line of code displays a single value for each row in the list view
                                                                            cursor,
                                                                                new String[] {"NAME"},
                                                                                    new int[] {android.R.id.text1},
                                                                                        0);
            listDrinks.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                                            "Database unavailable",
                                                    Toast.LENGTH_SHORT);
            toast.show();
        }

        // Create the listener
        // To get items in the list view to respond to clicks, you need to create an OnItemClickListener
        // and implement its onItemClick().
        /*
        OnItemClickListener - listens for when items are clicked
        onItemClick() - includes several parameters that you can use to find out which item was clicked
         */
        // A ListView is a subclass of AdapterView.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> listDrinks,            // The view that was clicked (in this case, the list view).
                                    // These three parameters give you info about which item was clicked in the list view.
                                    View itemView,
                                    int position,
                                    long id) {

                    //Pass the drink the user clicks on to DrinkActivity
                    Intent intent = new Intent(DrinkCategoryActivity.this,
                                                                DrinkActivity.class);
                    intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                    startActivity(intent);
            }
        };

        // Assign the listener to the list view
        listDrinks.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

}
