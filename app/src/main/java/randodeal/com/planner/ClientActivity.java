package randodeal.com.planner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ClientActivity extends Activity implements View.OnClickListener {
    Button btn1,btn2,btn3;
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button btnPush;
    EditText etName, etPhone, etMore;
    ListView lv1;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        Log.d("Старт", "создано");

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etMore = (EditText) findViewById(R.id.etMore);

        btnPush = (Button) findViewById(R.id.btnPush);
        btnPush.setOnClickListener(this);

        lv1 = (ListView) findViewById(R.id.lv1);

        listItems = new ArrayList<String>();

        arrayList = new ArrayList<>();


 //       adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listItems);
        SimpleAdapter  adapter = new SimpleAdapter(this, arrayList,
                R.layout.my_item, new String[]{"Name","Phone"},
                new int[]{R.id.text1, R.id.text2});
        lv1.setAdapter(adapter);

      //  adapter.setNotifyOnChange(true);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();






        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("client", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("idClient");
            int nameColIndex = c.getColumnIndex("name");
            int phoneColIndex = c.getColumnIndex("phone");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "idClient = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", phone = " + c.getString(phoneColIndex));




                map = new HashMap<>();
                map.put("Name", c.getString(nameColIndex));
                map.put("Phone", c.getString(phoneColIndex));
                arrayList.add(map);
                //adapter.add("Клиент " + c.getString(nameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, names);
//        // присваиваем адаптер списку
//        lv1.setAdapter(adapter);











    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:

                break;
            case R.id.btn2:
                intent = new Intent(this, RecordActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;
            case R.id.btn3:
                intent = new Intent(this, ReportActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;

            case R.id.btnPush:
                ContentValues cv = new ContentValues();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String more = etMore.getText().toString();
                cv.put("name", name);
                cv.put("phone", phone);
                cv.put("more", more);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("client", null, cv);

                Toast toast = Toast.makeText(getApplicationContext(),
                        name + " добавлен!", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }






    }
}
