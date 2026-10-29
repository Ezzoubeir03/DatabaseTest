package com.example.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Version 1 for now. Change to 2 (and 3) in the upgrade steps later.
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        // Create database
        Button createDatabase = findViewById(R.id.create_database);
        createDatabase.setOnClickListener(v -> dbHelper.getWritableDatabase());

        // Add data
        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            // First record
            values.put("name", "Android programming");
            values.put("author", "Tom Smith");
            values.put("pages", 500);
            values.put("price", 60.99);
            db.insert("Book", null, values);
            values.clear();

            // Second record
            values.put("name", "Java programming");
            values.put("author", "Jack Tayor");
            values.put("pages", 300);
            values.put("price", 20.99);
            db.insert("Book", null, values);
        });

        // Update data
        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("price", 20.00);
            db.update("Book", values, "name = ?",
                    new String[]{"Java programming"});
        });

        // Delete data
        Button deleteData = findViewById(R.id.delete_data);
        deleteData.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("Book", "pages > ?", new String[]{"300"});
        });

        // Query data
        Button queryData = findViewById(R.id.query_data);
        queryData.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("Book", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    int pages = cursor.getInt(cursor.getColumnIndexOrThrow("pages"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                    Log.d("Main", "Book name: " + name);
                    Log.d("Main", "Book author: " + author);
                    Log.d("Main", "Book pages: " + pages);
                    Log.d("Main", "Book price: " + price);
                } while (cursor.moveToNext());
            }
            cursor.close();
        });
    }
}