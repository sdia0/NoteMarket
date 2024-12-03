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

public class AddActivity extends AppCompatActivity {

    private ArrayList<EditText> editTexts = new ArrayList<>();
    private DbHelper dbHelper;
    private SQLiteDatabase db;
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
        String tableName = getIntent().getStringExtra("tableName");
        ArrayList<String> headers = dbHelper.getColumnNames(db, tableName);

        // Находим родительский контейнер
        LinearLayout container = findViewById(R.id.container);

        if (headers != null) {
            headers.remove(0);
            for (int i = 0; i < headers.size(); i++) {
                EditText editText = new EditText(this);
                editText.setHint(headers.get(i)+"");
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
            for (int i = 0; i < headers.size(); i++) {
                String text = editTexts.get(i).getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_SHORT).show();
                    return;
                }
                values.put(headers.get(i), text);
            }

            if (tableName != null) dbHelper.insertData(tableName, values);
        });
    }
}