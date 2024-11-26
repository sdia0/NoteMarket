package com.example.notemarket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView ivEdit, ivDelete, ivInsert, editTools, editFew;
    TableLayout tableLayout;
    int selectedRowIndex = -1;
    boolean isEditFew;
    @SuppressLint({"RtlHardcoded", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FrameLayout rootLayout = findViewById(R.id.main); // Корневой контейнер

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Проверяем, было ли нажатие за пределами таблицы
                if (!isTouchInsideTable(event)) {
                    resetRowHighlight(); // Сбрасываем выделение всех ячеек
                }
                return false; // Позволяем другим событиям продолжать обработку
            }
        });

        ivEdit = findViewById(R.id.ivEdit);
        ivDelete = findViewById(R.id.ivDelete);
        ivInsert = findViewById(R.id.ivInsert);
        editTools = findViewById(R.id.editTools);
        editFew = findViewById(R.id.editFew);

        editTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показать иконки с анимацией
                toggleIconsWithAnimation();
            }
        });

        editFew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRowHighlight();
                if (isEditFew) {
                    isEditFew = false;
                    @SuppressLint("UseCompatLoadingForDrawables")
                    Drawable drawable = getResources().getDrawable(R.drawable.edit_few, null);  // Получаем Drawable объект
                    editFew.setImageDrawable(drawable);
                }
                else {
                    isEditFew = true;
                    @SuppressLint("UseCompatLoadingForDrawables")
                    Drawable drawable = getResources().getDrawable(R.drawable.close, null);  // Получаем Drawable объект
                    editFew.setImageDrawable(drawable);
                }
            }
        });

        // Получаем ссылку на TableLayout
        tableLayout = findViewById(R.id.tableLayout);

        // Пример данных для таблицы
        List<List<String>> data = new ArrayList<>();

        // Добавляем строки данных
        List<String> row1 = new ArrayList<>();
        row1.add("Имя");
        row1.add("Возраст");
        row1.add("Город");
        row1.add("Компания");
        row1.add("shfsgjfgjsfgsdjfg");
        data.add(row1);

        List<String> row2 = new ArrayList<>();
        row2.add("Иван");
        row2.add("25");
        row2.add("Москва");
        row2.add("ТехноГрупп");
        row2.add("shfsgjfgjsfgsdjfg");
        data.add(row2);

        List<String> row3 = new ArrayList<>();
        row3.add("Мария");
        row3.add("30");
        row3.add("Санкт-Петербург");
        row3.add("IT Solutions");
        row3.add("shfsgjfgjsfgsdjfg");
        data.add(row3);

        List<String> row4 = new ArrayList<>();
        row4.add("Алексей");
        row4.add("22");
        row4.add("Новосибирск");
        row4.add("Web Studio");
        row4.add("shfsgjfgjsfgsdjfg");
        data.add(row4);

        // Создаем заголовок таблицы (первая строка данных)
        List<String> headers = data.get(0);
        TableRow headerRow = new TableRow(this);
        headerRow.setGravity(Gravity.CENTER);

        // Уменьшаем отступы для заголовков
        for (int i = 0; i < headers.size(); i++) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(headers.get(i));
            headerTextView.setPadding(36, 36, 36, 36);  // Уменьшаем паддинг для заголовков
            headerTextView.setMaxWidth(600);  // Ограничиваем ширину столбца
            headerTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);  // Обрезаем текст многоточием
            headerTextView.setSingleLine(true);  // Убедимся, что текст не переносится
            headerTextView.setGravity(Gravity.LEFT);
            int finalI = i;
            headerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // При нажатии на ячейку выделяем столбец
                    highlightColumn(finalI); // j - это индекс столбца
                }
            });
            headerRow.addView(headerTextView);
        }

        // Добавляем строку заголовков в таблицу
        tableLayout.addView(headerRow);

        // Динамически добавляем остальные строки
        for (int i = 1; i < data.size(); i++) { // начинаем с 1, так как 0 - это заголовки
            List<String> rowData = data.get(i);
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.LEFT);

            // Добавляем столбцы (значения данных)
            for (String value : rowData) {
                TextView textView = new TextView(this);
                textView.setText(value);
                textView.setPadding(36, 36, 36, 36);  // Уменьшаем паддинг для данных
                textView.setMaxWidth(600);  // Ограничиваем ширину столбца
                textView.setEllipsize(android.text.TextUtils.TruncateAt.END);  // Обрезаем текст многоточием
                textView.setSingleLine(true);  // Убедимся, что текст не переносится
                textView.setGravity(Gravity.LEFT);
                int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // При нажатии на ячейку выделяем строку
                        selectedRowIndex = finalI;
                        if (isEditFew) highlightCell(textView);
                        else highlightRow(tableRow); // row - это объект строки
                    }
                });
                tableRow.addView(textView);
            }

            // Добавляем строку в таблицу
            tableLayout.addView(tableRow);

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditFew) {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                        return;
                    }
                    if (selectedRowIndex == -1) { // Проверяем, что строка выбрана
                        Toast.makeText(MainActivity.this, "Сначала выберите строку", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Получаем данные из выбранной строки
                    TableRow selectedRow = (TableRow) tableLayout.getChildAt(selectedRowIndex);
                    ArrayList<String> rowData = new ArrayList<>();

                    for (int i = 0; i < selectedRow.getChildCount(); i++) {
                        TextView cell = (TextView) selectedRow.getChildAt(i);
                        rowData.add(cell.getText().toString());
                    }

                    // Передаем данные в новую активность
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putStringArrayListExtra("rowData", rowData);
                    startActivity(intent);
                }
            });

        }
    }
    private boolean isTouchInsideTable(MotionEvent event) {
        int[] location = new int[2];
        tableLayout.getLocationOnScreen(location); // Получаем координаты таблицы

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        // Проверяем, если клик был внутри границ таблицы
        return x >= location[0] && x <= location[0] + tableLayout.getWidth() &&
                y >= location[1] && y <= location[1] + tableLayout.getHeight();
    }
    private void toggleIconsWithAnimation() {
        // Если иконки скрыты, показываем их с анимацией
        if (ivEdit.getVisibility() == View.GONE) {
            showWithAnimation(ivEdit);
            showWithAnimation(ivDelete);
            showWithAnimation(ivInsert);
            showWithAnimation(editFew);
        } else {
            // Если иконки уже видны, скрываем их с анимацией
            hideWithAnimation(ivEdit);
            hideWithAnimation(ivDelete);
            hideWithAnimation(ivInsert);
            hideWithAnimation(editFew);
        }
    }

    private void highlightRow(TableRow row) {
        // Очищаем выделение предыдущей строки
        resetRowHighlight();

        // Добавляем выделение для текущей строки
        for (int i = 0; i < row.getChildCount(); i++) {
            TextView cell = (TextView) row.getChildAt(i);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(3, getResources().getColor(R.color.blue)); // Голубая рамка
            cell.setBackground(drawable);
        }
    }
    private void highlightCell(TextView textView) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(3, getResources().getColor(R.color.blue)); // Голубая рамка
        textView.setBackground(drawable);
    }
    private void highlightColumn(int columnIndex) {
        resetRowHighlight();
        // Обновляем стиль всех ячеек в столбце
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            if (row.getChildCount() > columnIndex) {
                TextView cell = (TextView) row.getChildAt(columnIndex);
                // Применяем рамку для выделения
                GradientDrawable drawable = new GradientDrawable();
                drawable.setStroke(3, getResources().getColor(R.color.blue));
                cell.setBackground(drawable);
            }
        }
    }
    private void resetRowHighlight() {
        selectedRowIndex = -1;
        // Сбросить выделение всех строк
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView cell = (TextView) row.getChildAt(j);
                cell.setBackground(null); // Убираем выделение
            }
        }
    }

    // Функция для отображения иконки с анимацией
    private void showWithAnimation(final ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f); // Анимация появления
        fadeIn.setDuration(500); // Длительность анимации
        imageView.startAnimation(fadeIn);
    }

    // Функция для скрытия иконки с анимацией
    private void hideWithAnimation(final ImageView imageView) {
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f); // Анимация исчезновения
        fadeOut.setDuration(500); // Длительность анимации
        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {}

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });
        imageView.startAnimation(fadeOut);
    }
}