package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.CheckBox;
import android.content.ContentValues;
import android.os.AsyncTask;

import java.util.jar.Attributes;

public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKID = "drinkId";

    int count = 0;


    // Modified
    private SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        //Get the drink from the intent
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);




        //Create a cursor
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},              // Modified "LIKES"
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //Get the drink details from the cursor
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                //Modified
               // int isLikes = cursor.getInt(4);


                //Populate the drink name
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);
                //Populate the drink description
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);
                //Populate the drink image
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                //Populate the favorite checkbox
                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);


                //Modified
                // Populate the button
               // Button buttonLikes = (Button) findViewById(R.id.buttonLikes);
                //final TextView textView  = (TextView) findViewById(R.id.textViewLike);
                // buttonLikes.isClickable();




            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }



        // Modified
       // setUpLikesTextView();

       // onLikesClicked();


    }


    //Update the database when the checkbox is clicked
    public void onFavoriteClicked(View view){
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        new UpdateDrinkTask().execute(drinkId);
    }


    /*

    // Modified
    // update the database when the button is clicked
    public void onLikesClicked(View view) {



        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        // Get the value of the button
        Button buttonLikes =(Button) findViewById(R.id.buttonLikes);

        final TextView textView  = (TextView) findViewById(R.id.textViewLike);

        buttonLikes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        count++;
                        textView.setText(count + "");
                    }
                });


        ContentValues drinkValues = new ContentValues();
        drinkValues.put("LIKES", buttonLikes.callOnClick());

        //Get a reference to the database and update the LIKES column
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            db.update("DRINK",
                    drinkValues,
                    "_id = ?",
                    new String[] {Integer.toString(drinkId)});
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

     */


    // Modified
    // update the database when the button is clicked
    public void onLikesClicked(View view) {



        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        // new UpdateDrinkTask().execute(drinkId);
        new UpdateLikeTask().execute(drinkId);


    }









    //Inner class to update the drink.
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues drinkValues;
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());




        }
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues,
                        "_id = ?", new String[] {Integer.toString(drinkId)});
                db.close();
                return true;
            } catch(SQLiteException e) {
                return false;
            }



        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }










    //Inner class to update the likes text.
    private class UpdateLikeTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues drinkValues;
        protected void onPreExecute() {

            Button buttonLikes = (Button) findViewById(R.id.buttonLikes);
            final TextView textView  = (TextView) findViewById(R.id.textViewLike);

            buttonLikes.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            count++;
                            textView.setText(count + "");
                        }
                    });
            drinkValues = new ContentValues();
            drinkValues.put("LIKES", buttonLikes.callOnClick());




        }
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues,
                        "LIKES = ?", new String[] {Integer.toString(drinkId)});
                db.close();
                return true;
            } catch(SQLiteException e) {
                return false;
            }



        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }









}
