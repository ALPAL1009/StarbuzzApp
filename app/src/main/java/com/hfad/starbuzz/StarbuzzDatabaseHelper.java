package com.hfad.starbuzz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz";   // the name of our database
    private static final int DB_VERSION = 2;            // the version of the database

    StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateMyDatabase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabase(db, 0, newVersion);

    }

    private static void insertDrink(SQLiteDatabase db, String name, String description, int resourceId) {

        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("DRINK", null, drinkValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) {           //modified oldVersion < 1
            db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");

            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
            insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam", R.drawable.cappuccino);
            insertDrink(db,"Filter", "Our best drip coffee", R.drawable.filter);
            insertDrink(db, "Macchiato", "Add a splash of milk to a shot of espresso", R.drawable.macchiato);
        }
        // Modified from (oldVersion < 2)
        // Code to add extra column
        //db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        // Modified
        // Add a numeric LIKES column to the DRINK table
        if (oldVersion < 2)
            db.execSQL("ALTER TABLE DRINK ADD COLUMN LIKES NUMERIC");           // The column I want to add to DB

    }

}


















