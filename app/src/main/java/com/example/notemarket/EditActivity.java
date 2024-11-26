package com.example.notemarket;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private ArrayList<EditText> editTexts = new ArrayList<>();

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

        // Получаем данные строки из Intent
        ArrayList<String> rowData = getIntent().getStringArrayListExtra("rowData");

        // Находим родительский контейнер
        LinearLayout container = findViewById(R.id.container);

        // Создаем EditText для каждого элемента строки
        if (rowData != null) {
            for (String data : rowData) {
                EditText editText = new EditText(this);
                editText.setText(data); // Устанавливаем данные в EditText
                editText.setHint("Введите данные");
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Добавляем EditText в контейнер
                container.addView(editText);
                editTexts.add(editText);
            }
        }
        else {
            EditText editText = new EditText(this);
            editText.setText(""); // Устанавливаем данные в EditText
            editText.setHint("Введите данные");
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Добавляем EditText в контейнер
            container.addView(editText);
            editTexts.add(editText);
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
            ArrayList<String> updatedRowData = new ArrayList<>();
            for (EditText editText : editTexts) {
                updatedRowData.add(editText.getText().toString());
            }

            // Передаем измененные данные обратно
            Intent resultIntent = new Intent(this, MainActivity.class);
            resultIntent.putStringArrayListExtra("updatedRowData", updatedRowData);
            startActivity(resultIntent);
            finish();
        });
    }
}