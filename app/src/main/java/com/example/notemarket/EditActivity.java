package com.example.notemarket;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Инициализация помощника базы данных
        dbHelper = new DbHelper(this);

        // Получаем доступ к записи в базу данных
        db = dbHelper.getWritableDatabase();

        // Получаем данные строки из Intent
        ArrayList<String> rowData = getIntent().getStringArrayListExtra("rowData");
        String tableName = getIntent().getStringExtra("tableName");
        ArrayList<String> headers = dbHelper.getColumnNames(db, tableName);
        ArrayList<Integer> ids = getIntent().getIntegerArrayListExtra("ids");
        String columnName = getIntent().getStringExtra("columnName");

        // Находим родительский контейнер
        LinearLayout container = findViewById(R.id.container);

        if (rowData != null) {
            // Создаем EditText для каждого элемента строки
            if (rowData != null && headers != null) {
                id = Integer.parseInt(rowData.get(0));
                for (int i = 1; i < headers.size(); i++) {
                    EditText editText = new EditText(this);
                    editText.setHint(headers.get(i) + "");
                    editText.setText(rowData.get(i)); // Устанавливаем данные в EditText
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Добавляем EditText в контейнер
                    container.addView(editText);
                    editTexts.add(editText);
                }
            }

            // Кнопка "Сохранить"
            Button saveButton = new Button(this);
            saveButton.setText("Сохранить");
            saveButton.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_button));
            saveButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            container.addView(saveButton);

            saveButton.setOnClickListener(v -> {
                ContentValues values = new ContentValues();
                for (int i = 0; i < headers.size() - 1; i++) {
                    String text = editTexts.get(i).getText().toString();
                    if (text.isEmpty()) {
                        Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    values.put(headers.get(i + 1), text);
                }

                DbHelper dbHelper = new DbHelper(this);
                if (tableName != null)
                    if (dbHelper.updateData(id, tableName, values))
                        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "Data has not updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        if (ids != null) {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            EditText editText = new EditText(this);
            editText.setHint(columnName + "");
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            container.addView(editText);

            // Кнопка "Сохранить"
            Button saveButton = new Button(this);
            saveButton.setText("Сохранить");
            saveButton.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_button));
            saveButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            container.addView(saveButton);

            saveButton.setOnClickListener(v -> {
                DbHelper dbHelper = new DbHelper(this);
                for (Integer id : ids) {
                    ContentValues values = new ContentValues();
                    values.put(columnName, editText.getText().toString());
                    if (dbHelper.updateData(id, tableName, values))
                        Log.d("CELL_UPDATED", "Data updated " + id);
                    else Log.d("CELL_UPDATED", "Data updated " + id);
                }
                finish();
            });
        }
    }
}