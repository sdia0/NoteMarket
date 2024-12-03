package com.example.notemarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    String TAG = "DBHELPER";
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
    public Boolean deleteData(int id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (tableExists(db, tableName)) {
            long result = db.delete(tableName, "id=?", new String[]{id+""});
            return result == -1 ? false : true;
        }
        else {
            Log.e("Database", "Table does not exist: " + tableName);
            return false;
        }
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

    public void insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Проверяем, существует ли таблица с заданным именем
        if (tableExists(db, tableName)) {
            // Если таблица существует, вставляем данные
            db.insert(tableName, null, values);
        } else {
            // Если таблица не существует, выводим ошибку
            Log.e("Database", "Table does not exist: " + tableName);
        }
    }

    public Boolean updateData(int id, String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (tableExists(db, tableName)) {
            long result = db.update(tableName, values, "_id=?", new String[]{id + ""});
            return result == -1 ? false : true;
        }
        else {
            Log.i(TAG, "Такой таблицы не существует");
            return false;
        }
    }

    // Метод для проверки существования таблицы
    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            // Запрос PRAGMA table_info для проверки существования таблицы
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            return cursor.getCount() > 0;  // Если таблица существует, будет хотя бы 1 строка
        } catch (Exception e) {
            // Если произошла ошибка (например, таблица не существует), возвращаем false
            Log.e("Database", "Error checking table existence: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();  // Закрываем курсор
            }
        }
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
    public ArrayList<String> getColumnNames(SQLiteDatabase db, String tableName) {
        ArrayList<String> columnNames = new ArrayList<>();
        // Подготовим запрос для получения всех данных из таблицы
        String query = "SELECT * FROM " + tableName + " LIMIT 1"; // Оператор LIMIT 1 для быстрого получения только первой строки
        Cursor cursor = null;

        try {
            // Выполняем запрос
            cursor = db.rawQuery(query, null);

            // Получаем названия столбцов
            if (cursor != null) {
                String[] columns = cursor.getColumnNames(); // Массив с названиями столбцов
                // Добавляем столбец в ArrayList
                columnNames.addAll(Arrays.asList(columns));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return columnNames; // Возвращаем ArrayList с именами столбцов
    }
}
