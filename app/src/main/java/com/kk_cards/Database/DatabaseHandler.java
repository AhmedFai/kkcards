package com.kk_cards.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by nvp on 11/29/2016.
 */
public class
DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Spy_Bazzar";

    // Contacts table name


    private static final String TABLE2 = "Add_to_cart";
    private static final String TABLE1 = "cart_quantity";
    private static final String TABLE3 = "online_cart";
    private static final String TABLE4 = "buy_now";
    private static final String TABLE5 = "go_to_cart";
    private static final String TABLE6 = "go_to_cart_logout";


    // Contacts Table Columns names


    private static final String Id = "id";
    private static final String Product_Id = "product_id";
    private static final String Product_Name = "product_name";
    private static final String Quantity = "quantity";
    private static final String Regular_Price = "regular_price";
    private static final String Sale_Price = "sale_price";
    private static final String Product_Image = "product_image";
    private static final String Discount = "discount";
    private static final String Delivery_charge = "del_charge";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String CREATE_CONTACTS_TABLE1 = "CREATE TABLE " + TABLE2 + "(" + Id + " INTEGER PRIMARY KEY," + Product_Id + " TEXT NOT NULL UNIQUE,"
                + Product_Name + " TEXT," + Quantity + " TEXT," + Regular_Price + " TEXT," + Sale_Price + " TEXT," +Discount + " TEXT,"+ Product_Image + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE1);
        String CREATE_CONTACTS_TABLE3 = "CREATE TABLE " + TABLE3 + "(" + Id + " INTEGER PRIMARY KEY," + Product_Id + " TEXT NOT NULL UNIQUE,"
                + Product_Name + " TEXT," + Quantity + " TEXT," + Regular_Price + " TEXT," + Sale_Price + " TEXT," +Discount + " TEXT,"+ Product_Image + " TEXT," + Delivery_charge + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE3);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE1 + "(" + Id + " INTEGER PRIMARY KEY," + Product_Id + " TEXT NOT NULL UNIQUE,"
                + Quantity + " TEXT"+ ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        String CREATE_CONTACTS_TABLE4 = "CREATE TABLE " + TABLE4 + "(" + Id + " INTEGER PRIMARY KEY," + Sale_Price + " TEXT,"
                + Quantity + " TEXT,"+Delivery_charge + " TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE4);
        String CREATE_CONTACTS_TABLE5 = "CREATE TABLE " + TABLE5 + "(" + Id + " INTEGER PRIMARY KEY," + Product_Id + " TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE5);


        String CREATE_CONTACTS_TABLE6 = "CREATE TABLE " + TABLE6 + "(" + Id + " INTEGER PRIMARY KEY," + Product_Id + " TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE6);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE5);


        // Create tables again
        onCreate(sqLiteDatabase);


        // Create tables again

    }

    // Table 1


    // Table2

    public void delete1() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE2);

        db.close();


    }
    public void delete_quantity_table() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE1);
        db.close();

    }
    public void delete_cart_table_server() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE3);

        db.close();


    }
    public void delete_buy_now() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE4);

        db.close();


    }


    public boolean delete_cart(String id)

    {

        SQLiteDatabase db = this.getWritableDatabase();


       int result= db.delete(TABLE2, Id + "=" + id, null);

       // Log.d("dhdjjddj", id);
        db.close();

        return true;
    }

    public boolean delete_for_go_cart(int id)

    {

        SQLiteDatabase db = this.getWritableDatabase();


        int result= db.delete(TABLE5, Id + "=" + id, null);

        // Log.d("dhdjjddj", id);
        db.close();

        return true;
    }

    public boolean delete_for_go_cart_log_out(int id)

    {

        SQLiteDatabase db = this.getWritableDatabase();


        int result= db.delete(TABLE6, Id + "=" + id, null);

        // Log.d("dhdjjddj", id);
        db.close();

        return true;
    }

    public int count_rows() {

        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE2, null);

        count = c.getCount();

        c.close();

        return count;

    }



    public boolean update_quantity(String quantity,String id)

    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();// Contact Name

        content_value.put(Quantity, quantity);

        int result = db.update(TABLE2, content_value, Id + "=" + id, null);

        Log.d("quanttttttt", String.valueOf(result));
        Log.d("quantityyyy_val", quantity);
        Log.d("iddd",id);

        db.close();

        if (result == 0)
            return false;
        else
            return true;

        // Closing database connection
    }

    public boolean update_quantity_server(String quantity,String id)

    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();// Contact Name

        content_value.put(Quantity, quantity);

        int result = db.update(TABLE1, content_value, Product_Id + "=" + id, null);

        Log.d("quanttttttt", String.valueOf(result));

        db.close();

        if (result == 0)
            return false;
        else
            return true;

        // Closing database connection
    }
    public boolean update_buy_now(String quantity)

    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();// Contact Name

        content_value.put(Quantity, quantity);

        int result = db.update(TABLE4, content_value, Id + "=" + 1, null);

        Log.d("quanttttttt", String.valueOf(result));

        db.close();

        if (result == 0)
            return false;
        else
            return true;

        // Closing database connection
    }



    public boolean update_server_table(String quantity,String id)

    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_value = new ContentValues();// Contact Name

        content_value.put(Quantity, quantity);

        int result = db.update(TABLE3, content_value, Product_Id + "=" + id, null);

        Log.d("quanttttttt", String.valueOf(result));

        db.close();

        if (result == 0)
            return false;
        else
            return true;

        // Closing database connection
    }


    public Cursor getall_data1() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2, null);

        return res;
    }
    public Cursor get_go_to_cart() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE5, null);

        return res;
    }

    public Cursor get_go_to_cart_log_out() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE6, null);

        return res;
    }

    public Cursor select_id_go_cart(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT "+ Id+" FROM "+TABLE5,null);

        return res;
    }

    public Cursor select_id_go_cart_log_out(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT "+ Id+" FROM "+TABLE6,null);

        return res;
    }
    public Cursor getall_data_server() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE3, null);

        return res;
    }

    public Cursor get_quantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1, null);

        return res;
    }

    public Cursor get_buy_now() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE4, null);

        return res;
    }


  public boolean insert(String name,String price1, String price2, String quantity,String discount,String id,String image)
    {
    SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Product_Id, id);
        content_value.put(Product_Name, name);
        content_value.put(Product_Image, image);
        content_value.put(Regular_Price, price1);
        content_value.put(Sale_Price, price2);
        content_value.put(Quantity, quantity);
        content_value.put(Discount, discount);
        long result = db.insert(TABLE2, null, content_value);

        Log.d("quantity",id);
        db.close();




        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }
    public boolean insert_server(String name,String price1, String price2, String quantity,String discount,String id,String image,String delivery_charge)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Product_Id, id);
        content_value.put(Product_Name, name);
        content_value.put(Product_Image, image);
        content_value.put(Regular_Price, price1);
        content_value.put(Sale_Price, price2);
        content_value.put(Quantity, quantity);
        content_value.put(Discount, discount);
        content_value.put(Delivery_charge, delivery_charge);

        long result = db.insert(TABLE3, null, content_value);


        db.close();


        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }


    public boolean insert_quantity(String quantity,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Product_Id, id);

        content_value.put(Quantity, quantity);
        long result = db.insert(TABLE1, null, content_value);


        db.close();


        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }
    public boolean insert_for_go_to_cart(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Product_Id, id);
        long result = db.insert(TABLE5, null, content_value);


        db.close();


        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }


    public boolean insert_for_go_to_cart_logout(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Product_Id, id);
        long result = db.insert(TABLE6, null, content_value);


        db.close();


        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }

    public boolean insert_buy_now_product(String sale_price,String quantity,String del)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_value = new ContentValues();// Contact Name
        content_value.put(Sale_Price, sale_price);
        content_value.put(Delivery_charge, del);
        content_value.put(Quantity, quantity);
        long result = db.insert(TABLE4, null, content_value);


        db.close();


        if (result == -1)
            return false;
        else
            return true;


        // Closing database connection
    }


    // Table3

}