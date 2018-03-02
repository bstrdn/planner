package randodeal.com.planner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by dntst on 07.02.2018.
 */

class DBHelper extends SQLiteOpenHelper {


    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table client ("
                + "idClient integer primary key autoincrement,"
                + "name text,"
                + "phone integer,"
                + "dateBirthday integer,"
                + "more text" + ");");
        db.execSQL("create table record ("
                + "idRecord integer primary key autoincrement,"
                + "idClient text,"
                + "name text,"        //временно - id
                + "dateVisit integer,"
                + "cost integer" + ");");

        db.execSQL("INSERT INTO client (name, phone) VALUES ('Том Смит', 89211234567)");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public static void delClient (String tableName, String keyName, String keyValue) {
//
//                db.delete(tableName, keyName + " = " + keyValue, null);
//        System.out.println(clientName);
//    }

}
