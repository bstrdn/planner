package randodeal.com.planner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends Activity implements View.OnClickListener {

    DBHelper dbHelper;    SQLiteDatabase db;
    HashMap<String, String> map;
    Button btn1,btn2,btn3;
    MultiAutoCompleteTextView textView;
    AutoCompleteTextView autoCompleteTextView;
    String[] mContacts;
    List<String> catList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);


        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();

//        ContentValues cv = new ContentValues();
//        cv.put("idClient", 3333);
//        cv.put("dateVisit", 123213213);
//        cv.put("cost", 1500);
//        // вставляем запись
//        db.insert("record", null, cv);
catList = new ArrayList<>();






        // делаем запрос всех данных из таблицы, получаем Cursor
        Cursor c = db.query("client", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idClietn = c.getColumnIndex("name");
//            int dateVisit = c.getColumnIndex("dateVisit");
//            int cost = c.getColumnIndex("cost");
            do { // получаем значения по номерам столбцов и пишем все в лог
            catList.add(c.getString(idClietn));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("ошибка", "0 rows");
        c.close();

























        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        List<String> catList = Arrays.asList(mCats);

        adapter = new ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line, catList);
        autoCompleteTextView.setAdapter(adapter);



        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ReportActivity.this,
                        adapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
String[] at4 = {adapter.getItem(position).toString()};

//                Cursor  c = db.query("client", null, "name", at4, null, null, null);
////        c.getString(c.getColumnIndex("name"));
//                c.moveToFirst();
                Cursor c = null;
//                c = db.rawQuery("select 1 from mytable where name='Name'", null );
//                Log.d("mylogs", " recordid = " + c.getInt(0));

                String[] argsName = {adapter.getItem(position).toString()};
                c = db.query("client", null,"name = ?", argsName, null, null,null );
                c.moveToFirst();
                int idClient = c.getColumnIndex("idClient");
                System.out.println(c.getString(idClient));
             //   System.out.println(c.getString(c.getColumnIndex("idClient")));
                System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
                System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
                System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
            }
        });
//
//        c = db.query("client", null, null, null, null, null, null);
////        c.getString(c.getColumnIndex("name"));
//        c.moveToFirst();
//     //   int idClietn = c.getColumnIndex("idClient");
//        System.out.println(c.getString(c.getColumnIndex("idClient")));
//        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
//        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
//        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                intent = new Intent(this, ClientActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;
            case R.id.btn2:
                intent = new Intent(this, RecordActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;
            case R.id.btn3:

                break;
        }
    }
}
