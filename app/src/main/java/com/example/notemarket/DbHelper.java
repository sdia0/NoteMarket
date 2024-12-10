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
        super(context, "notemarket4.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Таблица "Brands"
        db.execSQL("CREATE TABLE Brands (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Brand_Name TEXT NOT NULL);");

        db.execSQL("CREATE TABLE Deliveries (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Date TEXT, " +
                "Quantity INTEGER, " +
                "Price REAL, " +
                "Supplier_ID INTEGER, " +
                "Warehouse_ID INTEGER, " +
                "Product_ID INTEGER, " +
                "FOREIGN KEY (Supplier_ID) REFERENCES Suppliers(Supplier_ID), " +
                "FOREIGN KEY (Warehouse_ID) REFERENCES Warehouses(Warehouse_ID), " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID));");

        // Таблица "Characteristics"
        db.execSQL("CREATE TABLE Characteristics (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, " +
                "Processor TEXT, " +
                "RAM TEXT, " +
                "Storage_Type TEXT, " +
                "Screen_Size TEXT, " +
                "Screen_Resolution TEXT, " +
                "GPU TEXT, " +
                "Battery_Capacity TEXT, " +
                "Weight TEXT, " +
                "Color TEXT, " +
                "Product_ID INTEGER, " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID));");

        // Таблица "Products"
        db.execSQL("CREATE TABLE Products (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Product_Name TEXT NOT NULL, " +
                "Description TEXT, " +
                "Date_Added TEXT, " +
                "Brand_ID INTEGER, " +
                "FOREIGN KEY (Brand_ID) REFERENCES Brands(Brand_ID));");

        // Таблица "Warehouses"
        db.execSQL("CREATE TABLE Warehouses (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Warehouse_Name TEXT NOT NULL, " +
                "Location TEXT, " +
                "Quantity INTEGER, " +
                "Product_ID INTEGER, " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID));");

        // Таблица "Suppliers"
        db.execSQL("CREATE TABLE Suppliers (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Supplier_Name TEXT NOT NULL, " +
                "Contact_Info TEXT);");

        // Таблица "Supplies"
        db.execSQL("CREATE TABLE Supplies (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Supply_Date TEXT, " +
                "Quantity INTEGER, " +
                "Unit_Price REAL, " +
                "Supplier_ID INTEGER, " +
                "Warehouse_ID INTEGER, " +
                "Product_ID INTEGER, " +
                "FOREIGN KEY (Supplier_ID) REFERENCES Suppliers(Supplier_ID), " +
                "FOREIGN KEY (Warehouse_ID) REFERENCES Warehouses(Warehouse_ID), " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID));");

        // Таблица "Orders"
        db.execSQL("CREATE TABLE Orders (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Order_Date TEXT, " +
                "Quantity INTEGER, " +
                "Price_Per_Unit REAL, " +
                "Order_Status TEXT, " +
                "Total_Price REAL, " +
                "Warehouse_ID INTEGER, " +
                "Product_ID INTEGER, " +
                "FOREIGN KEY (Warehouse_ID) REFERENCES Warehouses(Warehouse_ID), " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID));");

        // Таблица "WriteOffs"
        db.execSQL("CREATE TABLE WriteOffs (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Warehouse_ID INTEGER, " +
                "Quantity INTEGER, " +
                "Reason TEXT, " +
                "WriteOff_Date TEXT, " +
                "Product_ID INTEGER, " +
                "Brand_ID INTEGER, " +
                "FOREIGN KEY (Warehouse_ID) REFERENCES Warehouses(Warehouse_ID), " +
                "FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID), " +
                "FOREIGN KEY (Brand_ID) REFERENCES Brands(Brand_ID));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Выполняем действия только если версия базы данных изменена
        if (oldVersion < newVersion) {
            // Пример удаления старых таблиц (если необходимо)
            // В данном случае мы будем сбрасывать всю базу данных, удаляя старые таблицы
            db.execSQL("DROP TABLE IF EXISTS Products");
            db.execSQL("DROP TABLE IF EXISTS Deliveries");
            db.execSQL("DROP TABLE IF EXISTS Warehouses");
            db.execSQL("DROP TABLE IF EXISTS Brands");
            db.execSQL("DROP TABLE IF EXISTS Suppliers");
            db.execSQL("DROP TABLE IF EXISTS Deliveries");
            db.execSQL("DROP TABLE IF EXISTS Orders");
            db.execSQL("DROP TABLE IF EXISTS WriteOffs");
            db.execSQL("DROP TABLE IF EXISTS Characteristics");

            // После удаления старых таблиц пересоздаем их с помощью метода onCreate
            onCreate(db);
        }
    }

    public List<List<String>> getTableAsMatrix(SQLiteDatabase db, String tableName) {
        List<List<String>> matrix = new ArrayList<>();

        // Выполнение запроса к таблице
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        List<Integer> foreignKeys = new ArrayList<>();
        List<String> columnNames = getColumnNames(db, tableName);
        for (int i = 1; i < columnNames.size(); i++)
            if (columnNames.get(i).contains("_ID"))
                foreignKeys.add(i);

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

        for (Integer key : foreignKeys) {
            for (List<String> row : matrix) {
                String column = columnNames.get(key);
                int index = column.indexOf('_');
                String table = column.substring(0, index);
                // Значение внешнего ключа из текущей строки
                String foreignKeyValue = row.get(key);

                // Запрос к таблице, чтобы получить значение связанного столбца
                String query = "SELECT " + table + "_Name FROM " + table + "s" + " WHERE " + "ID = ?";
                Log.d("QUERY", query + " " + foreignKeyValue);
                Cursor cursor1 = db.rawQuery(query, new String[]{foreignKeyValue});

                try {
                    if (cursor1.moveToFirst()) {
                        // Получаем значение связанного столбца, например, warehouse_name
                        String foreignValue = cursor1.getString(0);
                        row.set(key, foreignValue != null ? foreignValue : foreignKeyValue + "");
                    } else {
                        row.set(key, foreignKeyValue + ""); // Если значение не найдено в связанной таблице
                    }
                } finally {
                    cursor1.close(); // Закрываем курсор
                }
            }
        }

        return matrix;
    }
    public Boolean deleteData(int id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (tableExists(db, tableName)) {
            long result = db.delete(tableName, "ID=?", new String[]{id+""});
            return result != -1;
        }
        else {
            Log.e("Database", "Table does not exist: " + tableName);
            return false;
        }
    }

    public boolean insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Проверяем, существует ли таблица с заданным именем
        if (tableExists(db, tableName)) {
            // Если таблица существует, вставляем данные
            long result = db.insert(tableName, null, values);
            return result != -1; // Если результат не -1, значит вставка успешна
        } else {
            // Если таблица не существует, выводим ошибку
            Log.e("Database", "Table does not exist: " + tableName);
            return false;
        }
    }

    public Boolean updateData(int id, String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (tableExists(db, tableName)) {
            long result = db.update(tableName, values, "ID=?", new String[]{id + ""});
            return result == -1 ? false : true;
        }
        else {
            Log.i(TAG, "Такой таблицы не существует");
            return false;
        }
    }

    // Метод для проверки существования таблицы
    public boolean tableExists(SQLiteDatabase db, String tableName) {
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
        tableNames.remove(0);
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
