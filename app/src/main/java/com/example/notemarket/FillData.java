package com.example.notemarket;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class FillData {
    public static void fillWarehouses(DbHelper db) {
        ArrayList<ArrayList<String>> warehouses = new ArrayList<>();

        // Пример добавления данных, где Warehouse_ID будет автоматически присвоен
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse A", "Moscow", "50", "1"))); // "1001" - ID продукта
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse B", "Saint Petersburg", "30", "12")));
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse C", "Novosibirsk", "20", "13")));
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse D", "Ekaterinburg", "40", "12")));
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse E", "Kazan", "35", "15")));
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse F", "Vladivostok", "15", "6")));
        warehouses.add(new ArrayList<>(Arrays.asList("Warehouse G", "Sochi", "25", "7")));

        // Вставляем данные в таблицу "Warehouses"
        for (ArrayList<String> warehouse : warehouses) {
            ContentValues values = new ContentValues();
            values.put("Warehouse_Name", warehouse.get(0)); // Название склада
            values.put("Location", warehouse.get(1));      // Местоположение
            values.put("Quantity", warehouse.get(2));      // Количество
            values.put("Product_ID", warehouse.get(3));    // ID товара (ссылаемся на таблицу Products)

            // Вставляем данные в таблицу "Warehouses"
            boolean success = db.insertData("Warehouses", values);
            if (success) {
                Log.d("Database", "Warehouse inserted successfully: " + warehouse.get(0));
            } else {
                Log.e("Database", "Failed to insert warehouse: " + warehouse.get(0));
            }
        }
    }

    static public void fillProducts(DbHelper db) {
        ArrayList<ArrayList<String>> products = new ArrayList<>();

        // Заполнение списка продуктов, где Product_ID будет автоматически присвоен
        products.add(new ArrayList<>(Arrays.asList("Laptop X", "High-performance laptop", "2024-12-01", "1")));
        products.add(new ArrayList<>(Arrays.asList("Laptop Y", "Lightweight laptop", "2024-11-20", "2")));
        products.add(new ArrayList<>(Arrays.asList("Tablet Pro", "Professional tablet", "2024-11-15", "4")));
        products.add(new ArrayList<>(Arrays.asList("Desktop Z", "Powerful desktop", "2024-10-25", "2")));
        products.add(new ArrayList<>(Arrays.asList("Gaming Laptop", "Gaming optimized laptop", "2024-12-02", "2")));
        products.add(new ArrayList<>(Arrays.asList("Ultrabook 15", "Slim and portable ultrabook", "2024-11-30", "6")));
        products.add(new ArrayList<>(Arrays.asList("2-in-1 Laptop", "Convertible laptop", "2024-11-05", "7")));

        for (ArrayList<String> product : products) {
            ContentValues values = new ContentValues();
            values.put("Product_Name", product.get(0)); // Название продукта
            values.put("Description", product.get(1)); // Описание
            values.put("Date_Added", product.get(2)); // Дата добавления
            values.put("Brand_ID", product.get(3)); // ID бренда

            // Вставляем данные в таблицу "Products"
            boolean success = db.insertData("Products", values);
            if (success) {
                Log.d("Database", "Product inserted successfully: " + product.get(0));
            } else {
                Log.e("Database", "Failed to insert product: " + product.get(0));
            }
        }
    }

    public static void fillBrands(DbHelper db) {
        ArrayList<ArrayList<String>> brands = new ArrayList<>();

        brands.add(new ArrayList<>(Arrays.asList("Dell")));
        brands.add(new ArrayList<>(Arrays.asList("HP")));
        brands.add(new ArrayList<>(Arrays.asList("Lenovo")));
        brands.add(new ArrayList<>(Arrays.asList("Apple")));
        brands.add(new ArrayList<>(Arrays.asList("Asus")));
        brands.add(new ArrayList<>(Arrays.asList("Acer")));
        brands.add(new ArrayList<>(Arrays.asList("Microsoft")));

        for (ArrayList<String> brand : brands) {
            ContentValues values = new ContentValues();
            values.put("Brand_Name", brand.get(0)); // Название бренда

            // Вставляем данные в таблицу "Brands"
            boolean success = db.insertData("Brands", values);
            if (success) {
                Log.d("Database", "Brand inserted successfully: " + brand.get(0));
            } else {
                Log.e("Database", "Failed to insert brand: " + brand.get(0));
            }
        }
    }

    public static void fillSuppliers(DbHelper db) {
        ArrayList<ArrayList<String>> suppliers = new ArrayList<>();

        suppliers.add(new ArrayList<>(Arrays.asList("Supplier A", "contact@supplierA.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier B", "contact@supplierB.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier C", "contact@supplierC.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier D", "contact@supplierD.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier E", "contact@supplierE.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier F", "contact@supplierF.com")));
        suppliers.add(new ArrayList<>(Arrays.asList("Supplier G", "contact@supplierG.com")));

        for (ArrayList<String> supplier : suppliers) {
            ContentValues values = new ContentValues();
            values.put("Supplier_Name", supplier.get(0)); // Название поставщика
            values.put("Contact_Info", supplier.get(1)); // Контактный email

            // Вставляем данные в таблицу "Suppliers"
            boolean success = db.insertData("Suppliers", values);
            if (success) {
                Log.d("Database", "Supplier inserted successfully: " + supplier.get(1));
            } else {
                Log.e("Database", "Failed to insert supplier: " + supplier.get(1));
            }
        }
    }

    static public void fillDeliveries(DbHelper db) {
        ArrayList<ArrayList<String>> deliveries = new ArrayList<>();

        // Добавление данных для поставок (без Delivery_ID, который будет автоинкрементироваться)
        deliveries.add(new ArrayList<>(Arrays.asList("2024-12-01", "20", "750.0", "1", "1", "1")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-12-02", "30", "720.0", "2", "2", "2")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-11-29", "10", "800.0", "3", "1", "13")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-12-03", "25", "700.0", "4", "4", "4")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-11-30", "15", "680.0", "3", "5", "5")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-12-04", "40", "650.0", "3", "1", "6")));
        deliveries.add(new ArrayList<>(Arrays.asList("2024-12-05", "12", "780.0", "3", "1", "17")));

        for (ArrayList<String> delivery : deliveries) {
            ContentValues values = new ContentValues();
            values.put("Date", delivery.get(0)); // Дата поставки
            values.put("Quantity", delivery.get(1)); // Количество
            values.put("Price", delivery.get(2)); // Цена
            values.put("Supplier_ID", delivery.get(3)); // ID поставщика
            values.put("Warehouse_ID", delivery.get(4)); // ID склада
            values.put("Product_ID", delivery.get(5)); // ID продукта

            // Вставляем данные в таблицу "Deliveries"
            boolean success = db.insertData("Deliveries", values);
            if (success) {
                Log.d("Database", "Delivery inserted successfully: " + delivery.get(0));
            } else {
                Log.e("Database", "Failed to insert delivery: " + delivery.get(0));
            }
        }
    }

    public static void fillOrders(DbHelper db) {
        ArrayList<ArrayList<String>> orders = new ArrayList<>();

        orders.add(new ArrayList<>(Arrays.asList("2024-12-01", "5", "800.0", "Completed", "4000.0", "1", "1")));
        orders.add(new ArrayList<>(Arrays.asList("2024-12-02", "3", "720.0", "Pending", "2160.0", "2", "12")));
        orders.add(new ArrayList<>(Arrays.asList("2024-11-30", "10", "850.0", "Completed", "8500.0", "3", "13")));
        orders.add(new ArrayList<>(Arrays.asList("2024-12-03", "2", "900.0", "Shipped", "1800.0", "4", "4")));
        orders.add(new ArrayList<>(Arrays.asList("2024-11-28", "8", "750.0", "Cancelled", "6000.0", "5", "15")));
        orders.add(new ArrayList<>(Arrays.asList("2024-12-05", "6", "790.0", "Completed", "4740.0", "6", "6")));
        orders.add(new ArrayList<>(Arrays.asList("2024-12-04", "1", "1000.0", "Pending", "1000.0", "1", "7")));

        for (ArrayList<String> order : orders) {
            ContentValues values = new ContentValues();
            values.put("Order_Date", order.get(0)); // Дата заказа
            values.put("Quantity", order.get(1)); // Количество
            values.put("Price_Per_Unit", order.get(2)); // Цена за единицу
            values.put("Order_Status", order.get(3)); // Статус заказа
            values.put("Total_Price", order.get(4)); // Общая сумма
            values.put("Warehouse_ID", order.get(5)); // ID склада
            values.put("Product_ID", order.get(6)); // ID продукта

            // Вставляем данные в таблицу "Orders"
            boolean success = db.insertData("Orders", values);
            if (success) {
                Log.d("Database", "Order inserted successfully: " + order.get(0));
            } else {
                Log.e("Database", "Failed to insert order: " + order.get(0));
            }
        }
    }

    public static void fillWriteOffs(DbHelper db) {
        ArrayList<ArrayList<String>> writeOffs = new ArrayList<>();

        writeOffs.add(new ArrayList<>(Arrays.asList("101", "2", "Defective", "2024-12-01", "1", "1")));
        writeOffs.add(new ArrayList<>(Arrays.asList("102", "3", "Expired", "2024-11-28", "12", "2")));
        writeOffs.add(new ArrayList<>(Arrays.asList("103", "1", "Damaged", "2024-12-02", "13", "4")));
        writeOffs.add(new ArrayList<>(Arrays.asList("104", "4", "Lost", "2024-11-30", "14", "3")));
        writeOffs.add(new ArrayList<>(Arrays.asList("105", "2", "Defective", "2024-12-03", "15", "5")));
        writeOffs.add(new ArrayList<>(Arrays.asList("106", "5", "Expired", "2024-12-05", "16", "6")));
        writeOffs.add(new ArrayList<>(Arrays.asList("107", "3", "Damaged", "2024-12-04", "17", "7")));

        for (ArrayList<String> writeOff : writeOffs) {
            ContentValues values = new ContentValues();
            values.put("Warehouse_ID", writeOff.get(0)); // ID склада
            values.put("Quantity", writeOff.get(1)); // Количество
            values.put("Reason", writeOff.get(2)); // Причина списания
            values.put("WriteOff_Date", writeOff.get(3)); // Дата списания
            values.put("Product_ID", writeOff.get(4)); // ID продукта
            values.put("Brand_ID", writeOff.get(5)); // ID бренда

            // Вставляем данные в таблицу "WriteOffs"
            boolean success = db.insertData("WriteOffs", values);
            if (success) {
                Log.d("Database", "WriteOff inserted successfully: " + writeOff.get(0));
            } else {
                Log.e("Database", "Failed to insert WriteOff: " + writeOff.get(0));
            }
        }
    }

    public static void fillCharacteristics(DbHelper db) {
        ArrayList<ArrayList<String>> characteristics = new ArrayList<>();

        characteristics.add(new ArrayList<>(Arrays.asList("Laptop X", "Intel i7", "16GB", "512GB", "SSD", "15.6\"",
                "1920x1080", "NVIDIA RTX 3050", "5000mAh", "2kg", "Black", "1")));
        characteristics.add(new ArrayList<>(Arrays.asList("Laptop Y", "AMD Ryzen 5", "8GB", "256GB", "SSD", "14\"",
                "1920x1080", "Integrated Graphics", "4000mAh", "1.5kg", "Silver", "2")));
        characteristics.add(new ArrayList<>(Arrays.asList("Tablet Pro", "Apple A15", "6GB", "128GB", "Flash", "10.5\"",
                "2560x1600", "Integrated", "7000mAh", "0.7kg", "Space Gray", "3")));
        characteristics.add(new ArrayList<>(Arrays.asList("Desktop Z", "Intel i9", "32GB", "1TB", "HDD", "27\"",
                "3840x2160", "NVIDIA RTX 4090", "NA", "15kg", "Black", "4")));
        characteristics.add(new ArrayList<>(Arrays.asList("Gaming Laptop", "Intel i7", "16GB", "1TB", "SSD", "17\"",
                "2560x1440", "NVIDIA GTX 3060", "6000mAh", "2.5kg", "Red", "5")));
        characteristics.add(new ArrayList<>(Arrays.asList("Ultrabook 15", "Intel i5", "8GB", "512GB", "SSD", "15\"",
                "1920x1080", "Integrated Graphics", "4500mAh", "1.2kg", "Silver", "6")));
        characteristics.add(new ArrayList<>(Arrays.asList("2-in-1 Laptop", "AMD Ryzen 7", "16GB", "512GB", "SSD", "13.5\"",
                "2256x1504", "Integrated Graphics", "5000mAh", "1.6kg", "Blue", "7")));

        for (ArrayList<String> characteristic : characteristics) {
            ContentValues values = new ContentValues();
            values.put("Name", characteristic.get(0)); // Название продукта
            values.put("Processor", characteristic.get(1)); // Процессор
            values.put("RAM", characteristic.get(2)); // Оперативная память
            values.put("Storage_Type", characteristic.get(4)); // Тип накопителя
            values.put("Screen_Size", characteristic.get(5)); // Размер экрана
            values.put("Screen_Resolution", characteristic.get(6)); // Разрешение экрана
            values.put("GPU", characteristic.get(7)); // Видеокарта
            values.put("Battery_Capacity", characteristic.get(8)); // Емкость батареи
            values.put("Weight", characteristic.get(9)); // Вес
            values.put("Color", characteristic.get(10)); // Цвет
            values.put("Product_ID", characteristic.get(11)); // ID продукта

            // Вставляем данные в таблицу "Characteristics"
            boolean success = db.insertData("Characteristics", values);
            if (success) {
                Log.d("Database", "Characteristic inserted successfully: " + characteristic.get(1));
            } else {
                Log.e("Database", "Failed to insert characteristic: " + characteristic.get(1));
            }
        }
    }

    public static void fillSupplies(DbHelper db) {
        ArrayList<ArrayList<String>> supplies = new ArrayList<>();

        // Добавляем примерные данные для таблицы Supplies
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-01", "100", "12.5", "1", "1", "1")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-02", "200", "15.0", "2", "2", "2")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-03", "150", "10.0", "3", "3", "3")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-04", "300", "8.0", "4", "4", "4")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-05", "250", "20.0", "5", "5", "5")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-06", "400", "25.0", "6", "6", "6")));
        supplies.add(new ArrayList<>(Arrays.asList("2024-01-07", "500", "30.0", "7", "7", "7")));

        for (ArrayList<String> supply : supplies) {
            ContentValues values = new ContentValues();
            values.put("Supply_Date", supply.get(0)); // Supply date
            values.put("Quantity", supply.get(1)); // Quantity
            values.put("Unit_Price", supply.get(2)); // Unit price
            values.put("Supplier_ID", supply.get(3)); // Supplier ID
            values.put("Warehouse_ID", supply.get(4)); // Warehouse ID
            values.put("Product_ID", supply.get(5)); // Product ID

            // Вставляем данные в таблицу "Supplies"
            boolean success = db.insertData("Supplies", values);
            if (success) {
                Log.d("Database", "Supply inserted successfully: " + supply.get(0));
            } else {
                Log.e("Database", "Failed to insert supply: " + supply.get(0));
            }
        }
    }

}
