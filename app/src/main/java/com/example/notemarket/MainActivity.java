package com.example.notemarket;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView ivEdit, ivDelete, ivInsert, editTools, editFew, deleteFew;
    TableLayout tableLayout, headerLayout;
    int selectedRowIndex = -1;
    boolean isEditFew = false, isDeleteFew = false;
    ImageView logout;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    ArrayList<Integer> selectedToEdit = new ArrayList<>();
    String columnName;
    List<Integer> selectedToDelete = new ArrayList<>();
    ArrayList<String> headers;
    EditText etSearch;
    ImageView ivSearch;
    Button btnClear, btnShowPanel;
    boolean isShown = true;
    LinearLayout panel;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences1 = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String savedTable = preferences1.getString("savedTable", "");
        if (!savedTable.isEmpty()) {
            setAdapter(savedTable);
            drawHeaders(savedTable);
            drawTable(savedTable, "");
        }
    }

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

        // Инициализация помощника базы данных
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        // fillData(dbHelper); // Вставка данных в таблицы

        FrameLayout rootLayout = findViewById(R.id.main); // Корневой контейнер

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Проверяем, было ли нажатие за пределами таблицы
                if (!isTouchInsideTable(event)) {
                    selectedRowIndex = -1;
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
        deleteFew = findViewById(R.id.deleteFew);
        logout = findViewById(R.id.logout);

        etSearch = findViewById(R.id.etSearch);
        ivSearch = findViewById(R.id.ivSearch);
        btnClear = findViewById(R.id.btnClear);

        btnShowPanel = findViewById(R.id.showPanel);
        panel = findViewById(R.id.panel);

        headerLayout = findViewById(R.id.headerLayout);

        btnShowPanel.setOnClickListener(v -> {
            if (isShown) {
                panel.setVisibility(View.GONE);
                btnShowPanel.setText("Показать панель");
            }
            else {
                panel.setVisibility(View.VISIBLE);
                btnShowPanel.setText("Скрыть панель");
            }
            isShown = !isShown;
        });

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        SharedPreferences preferences1 = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String savedTable = preferences1.getString("savedTable", "");
        if (!savedTable.isEmpty()) {
            // Устанавливаю адаптер заново из-за переворота экрана
            setAdapter(savedTable);
            drawHeaders(savedTable);
        }

        setAdapter(savedTable);

        // Обработка выбора элемента
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTable = parent.getItemAtPosition(position).toString();
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("savedTable", selectedTable);
            editor.apply();
            drawHeaders(selectedTable);
            drawTable(selectedTable, "");
        });

        btnClear.setOnClickListener(v -> {
            query = "";
            column = -1;
            etSearch.setText("");
            btnClear.setVisibility(View.GONE);
            drawTable(savedTable, "");
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("mode", 0);
                editor.apply();
                // Перенаправление на WelcomeActivity
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish(); // Завершить текущую активность
            }
        });

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
                selectedRowIndex = -1;
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

        deleteFew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRowIndex = -1;
                resetRowHighlight();
                if (isDeleteFew) {
                    isDeleteFew = false;
                    @SuppressLint("UseCompatLoadingForDrawables")
                    Drawable drawable = getResources().getDrawable(R.drawable.delete_few, null);  // Получаем Drawable объект
                    deleteFew.setImageDrawable(drawable);
                }
                else {
                    isDeleteFew = true;
                    @SuppressLint("UseCompatLoadingForDrawables")
                    Drawable drawable = getResources().getDrawable(R.drawable.close, null);  // Получаем Drawable объект
                    deleteFew.setImageDrawable(drawable);
                }
            }
        });

        // Получаем ссылку на TableLayout
        tableLayout = findViewById(R.id.tableLayout);
    }
    public void setAdapter(String tableName) {
        autoCompleteTextView.setText(tableName, false);
        // Настройка адаптера для AutoCompleteTextView
        List<String> tableNames = dbHelper.getTableNames(db);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, // Стиль элемента выпадающего списка
                tableNames
        );

        // Привязка адаптера к AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);
    }
    public void drawHeaders(String tableName) {
        headerLayout.removeAllViews();
        headerLayout.removeAllViews();
        // Создаем заголовок таблицы (первая строка данных)
        headers = dbHelper.getColumnNames(db,tableName);
        TableRow headerRow = new TableRow(this);
        headerRow.setGravity(Gravity.CENTER);

        // Уменьшаем отступы для заголовков
        for (int i = 0; i < headers.size(); i++) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(i + "");
            headerTextView.setPadding(36, 36, 36, 36);  // Уменьшаем паддинг для заголовков
            headerTextView.setWidth(300);  // Ограничиваем ширину столбца
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
        headerLayout.addView(headerRow);
        headerRow = new TableRow(this);

        // Уменьшаем отступы для заголовков
        for (int i = 0; i < headers.size(); i++) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(headers.get(i));
            headerTextView.setPadding(36, 36, 36, 36);  // Уменьшаем паддинг для заголовков
            headerTextView.setWidth(300);  // Ограничиваем ширину столбца
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
        headerLayout.addView(headerRow);
    }
    int column = -1;
    String query = "";
    public boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
    public boolean validatePrompt(String prompt, String tableName) {
        int index = prompt.indexOf(':');

        if (index != -1) {
            String column = prompt.substring(0, index);
            String query = prompt.substring(index + 1);
            DbHelper dbHelper = new DbHelper(this);
            ArrayList<String> columnNames = dbHelper.getColumnNames(dbHelper.getWritableDatabase(), tableName);
            // Если пользователь введ номер столбца
            if (isNumeric(column)) {
                if (Integer.parseInt(column) <= columnNames.size()) {
                    this.column = Integer.parseInt(column);
                    this.query = query;
                    return true;
                }
                else return false;
            }
            else {
                Toast.makeText(this, "Введите номер столбца", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            query = prompt;
            return true;
        }
    }
    public void drawTable(String tableName, String prompt) {
        if (!prompt.isEmpty()) {
            if (!validatePrompt(prompt, tableName)) {
                Toast.makeText(this, "Неверный формат для поиска", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // Местная переменная, которая будет хранить данные
        List<List<String>> data = dbHelper.getTableAsMatrix(db, tableName);
        tableLayout.removeAllViews();

        final boolean[] isFound = {false};

        // Динамически добавляем остальные строки
        for (int i = 0; i < data.size(); i++) { // начинаем с 1, так как 0 - это заголовки
            List<String> rowData = data.get(i);
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.LEFT);

            // Добавляем столбцы (значения данных)
            for (String value : rowData) {
                // Отрисовка ячейки
                TextView textView = new TextView(this);
                textView.setText(value);
                textView.setPadding(36, 36, 36, 36);  // Уменьшаем паддинг для данных
                textView.setWidth(300);  // Ограничиваем ширину столбца
                textView.setEllipsize(android.text.TextUtils.TruncateAt.END);  // Обрезаем текст многоточием
                textView.setSingleLine(true);  // Убедимся, что текст не переносится
                textView.setGravity(Gravity.LEFT);

                // Поиск
                int currentColumn = rowData.indexOf(value);
                if (column == -1) {
                    if (value.contains(query)) isFound[0] = true;
                }
                else if (column == currentColumn) {
                    if (value.contains(query)) isFound[0] = true;
                }

                // Нажатие на ячейку
                int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // При нажатии на ячейку выделяем строку
                        selectedRowIndex = finalI;
                        if (isDeleteFew) {
                            selectedToDelete.add(Integer.valueOf(data.get(finalI).get(0)));
                            for (int i = 0; i < tableRow.getChildCount(); i++) {
                                TextView cell = (TextView) tableRow.getChildAt(i);
                                GradientDrawable drawable = new GradientDrawable();
                                drawable.setStroke(3, getResources().getColor(R.color.blue)); // Голубая рамка
                                cell.setBackground(drawable);
                            }
                        } else {
                            if (isEditFew) {
                                columnName = headers.get(tableRow.indexOfChild(textView));
                                selectedToEdit.add(Integer.valueOf(rowData.get(0)));
                                highlightCell(textView);
                            } else highlightRow(tableRow); // row - это объект строки
                        }
                    }
                });
                tableRow.addView(textView);
            }

            // Добавляем строку в таблицу
            if (isFound[0]) {
                // Проверяем, есть ли у tableRow родитель
                if (tableRow.getParent() != null) {
                    // Удаляем tableRow из его текущего родителя
                    ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                }
                tableLayout.addView(tableRow);
                isFound[0] = false;
            }

            if (query.isEmpty()) {
                // Проверяем, есть ли у tableRow родитель
                if (tableRow.getParent() != null) {
                    // Удаляем tableRow из его текущего родителя
                    ((ViewGroup) tableRow.getParent()).removeView(tableRow);
                }

                // Добавляем строку в tableLayout
                tableLayout.addView(tableRow);
            }

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditFew) {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        if (columnName.isEmpty() || selectedToEdit.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Выберите ячейки для редактирования!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("ids", selectedToEdit);
                        intent.putExtra("tableName", tableName);
                        intent.putExtra("columnName", columnName);
                        selectedToEdit = new ArrayList<>();
                        columnName = "";
                        startActivity(intent);
                        return;
                    }
                    if (selectedRowIndex == -1) { // Проверяем, что строка выбрана
                        Toast.makeText(MainActivity.this, "Сначала выберите строку", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Получаем данные из выбранной строки
                    TableRow selectedRow = (TableRow) tableLayout.getChildAt(selectedRowIndex + 1);
                    ArrayList<String> rowData = new ArrayList<>();

                    for (int i = 0; i < selectedRow.getChildCount(); i++) {
                        TextView cell = (TextView) selectedRow.getChildAt(i);
                        rowData.add(cell.getText().toString());
                    }

                    // Передаем данные в новую активность
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putStringArrayListExtra("rowData", rowData);
                    intent.putExtra("tableName", tableName);
                    startActivity(intent);
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDeleteFew) {
                        if (selectedRowIndex == -1 || selectedToDelete.isEmpty())
                            Toast.makeText(MainActivity.this, "Сначала выберите строку", Toast.LENGTH_SHORT).show();
                        else {
                            for (Integer id : selectedToDelete) {
                                if (dbHelper.deleteData(id, tableName))
                                    Log.d("DELETED", "Успешно удалено " + id);
                                else Log.d("DELETED", "Ошибка... " + id);
                            }
                            drawTable(tableName, "");
                        }
                    } else {
                        int rowCount = tableLayout.getChildCount();
                        Log.d("TableInfo", "Number of rows: " + rowCount);
                        // Получаем данные из выбранной строки
                        if (selectedRowIndex < tableLayout.getChildCount()) {
                            TableRow selectedRow = (TableRow) tableLayout.getChildAt(selectedRowIndex);
                            TextView cell = (TextView) selectedRow.getChildAt(0);
                            int id = Integer.parseInt(cell.getText().toString());
                            if (dbHelper.deleteData(id, tableName)) Log.d("DELETED", "Успех " + id);
                            else Log.d("DELETED", "Ошибка... " + id);
                            drawTable(tableName, "");
                        } else {
                            // Индекс вне допустимого диапазона
                            Log.e("TableError", "Invalid row index " + (selectedRowIndex + 1));
                        }
                    }
                }
            });

            ivSearch.setOnClickListener(v -> {
                String text = etSearch.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(this, "Введите запрос для поиска", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnClear.setVisibility(View.VISIBLE);
                drawTable(tableName, text);
            });

            ivInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    intent.putExtra("tableName", tableName);
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
            showWithAnimation(deleteFew);
        } else {
            // Если иконки уже видны, скрываем их с анимацией
            hideWithAnimation(ivEdit);
            hideWithAnimation(ivDelete);
            hideWithAnimation(ivInsert);
            hideWithAnimation(editFew);
            hideWithAnimation(deleteFew);
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
    public void fillData(DbHelper db) {
        FillData.fillProducts(db);      // Заполняем таблицу "Products"
        FillData.fillWarehouses(db);    // Заполняем таблицу "Warehouses"
        FillData.fillBrands(db);        // Заполняем таблицу "Brands"
        FillData.fillSuppliers(db);     // Заполняем таблицу "Suppliers"
        FillData.fillDeliveries(db);    // Заполняем таблицу "Deliveries"
        FillData.fillOrders(db);       // Заполняем таблицу "Orders"
        FillData.fillWriteOffs(db);    // Заполняем таблицу "WriteOffs"
        FillData.fillCharacteristics(db); // Заполняем таблицу "Characteristics"
        FillData.fillSupplies(db);
    }

    private long insertGoods(SQLiteDatabase db, String title, String description, int price, String quantity, String deliveryDate) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("deliveryDate", deliveryDate);

        return db.insert("goods", null, values); // Возвращаем id вставленной записи
    }

    private void insertSpecifications(SQLiteDatabase db, long goodsId, String brand, String processor, String ram, String storageCapacity, String storageType,
                                      String diagonal, String screenResolution, String videocard, String battery, int weight, String color) {
        ContentValues values = new ContentValues();
        values.put("goods_id", goodsId); // Связываем с товаром по id
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
}