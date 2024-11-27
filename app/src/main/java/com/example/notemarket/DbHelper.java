package com.example.notemarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(@Nullable Context context) {
        super(context, "notemarket.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE goods(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "price INTEGER NOT NULL," +
                "quantity TEXT," +
                "deliveryDate TEXT NOT NULL)");

        db.execSQL("CREATE TABLE specifications(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "goods_id INTEGER," +
                "brand TEXT NOT NULL," +
                "processor TEXT NOT NULL," +
                "ram TEXT NOT NULL," +
                "storageCapacity TEXT," +
                "storageType TEXT NOT NULL," +
                "diagonal TEXT NOT NULL," +
                "screenResolution TEXT," +
                "videocard TEXT," +
                "battery TEXT," +
                "weight INTEGER," +
                "color TEXT," +
                "FOREIGN KEY (goods_id) REFERENCES goods (_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists goods");
        onCreate(db);
    }

    public List<List<String>> getTableAsMatrix(SQLiteDatabase db, String tableName) {
        List<List<String>> matrix = new ArrayList<>();

        // Выполнение запроса к таблице
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        try {
            int columnCount = cursor.getColumnCount(); // Количество колонок

            // Перемещение курсора на первую строку
            if (cursor.moveToFirst()) {
                do {
                    List<String> row = new ArrayList<>();
                    for (int i = 0; i < columnCount; i++) {
                        row.add(cursor.getString(i) != null ? cursor.getString(i) : ""); // Значение или пустая строка
                    }
                    matrix.add(row); // Добавление строки в матрицу
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close(); // Закрываем курсор
        }

        return matrix;
    }
    private void insertGoods(SQLiteDatabase db, String title, String description, int price, String quantity, String deliveryDate) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("deliveryDate", deliveryDate);

        db.insert("goods", null, values);
    }

    private void insertSpecifications(SQLiteDatabase db, String brand, String processor, String ram, String storageCapacity, String storageType,
                                      String diagonal, String screenResolution, String videocard, String battery, int weight, String color) {
        ContentValues values = new ContentValues();
        values.put("brand", brand);
        values.put("processor", processor);
        values.put("ram", ram);
        values.put("storageCapacity", storageCapacity);
        values.put("storageType", storageType);
        values.put("diagonal", diagonal);
        values.put("screenResolution", screenResolution);
        values.put("videocard", videocard);
        values.put("battery", battery);
        values.put("weight", weight);
        values.put("color", color);

        db.insert("specifications", null, values);
    }

    public List<String> getTableNames(SQLiteDatabase db) {
        List<String> tableNames = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                tableNames.add(cursor.getString(0)); // Первый столбец содержит имя таблицы
            }
            cursor.close();
        }
        return tableNames;
    }
    public List<String> getColumnNames(SQLiteDatabase db, String tableName) {
        List<String> columnNames = new ArrayList<>();

        // Выполняем запрос PRAGMA для получения метаданных столбцов
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

        // Проходим по результатам и извлекаем имена столбцов
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Получаем имя столбца
                int columnIndex = cursor.getColumnIndex("name");

                // Проверяем, что индекс столбца не -1
                if (columnIndex != -1) {
                    String columnName = cursor.getString(columnIndex);
                    columnNames.add(columnName);
                }
            }
            cursor.close();
        }

        return columnNames;
    }
}
