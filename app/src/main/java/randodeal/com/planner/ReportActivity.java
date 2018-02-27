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
import android.widget.TextView;
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
    TextView tvReportAll;
    int costAll = 0;

    AutoCompleteTextView autoCompleteTextView;

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
        tvReportAll = (TextView) findViewById(R.id.tvReportAll);


        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();


catList = new ArrayList<>();






        // делаем запрос всех данных из таблицы, получаем Cursor
        Cursor c = db.query("record", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idClietn = c.getColumnIndex("cost");
            do { // получаем значения по номерам столбцов и пишем все в лог
//                costAll += Integer.parseInt(c.getString(idClietn));
                int cost = c.getInt(idClietn);
                costAll += cost;
                System.out.println(cost);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("ошибка", "0 rows");
        c.close();


        tvReportAll.setText(costAll + "");
        System.out.println(costAll);























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
