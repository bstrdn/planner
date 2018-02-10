package randodeal.com.planner;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3;
    Button btnNewRecord;
    TextView currentDateTime;
    Calendar dateAndTime= Calendar.getInstance();
    ListView lvRecord;
    DBHelper dbHelper;    SQLiteDatabase db;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btnNewRecord = (Button) findViewById(R.id.btnNewRecord);
        lvRecord = (ListView) findViewById(R.id.lvRecord);
      //  btnNewRecord.setOnClickListener(this);
        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        arrayList = new ArrayList<>();
        setInitialDateTime();



        // создаем объект для создания и управления версиями БД / подключаемся к БД
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
                R.layout.my_item, new String[]{"Id","Date"},
                new int[]{R.id.text1, R.id.text2});
        lvRecord.setAdapter(adapter);

        Cursor c = db.query("record", null, null, null, null, null, null); // делаем запрос всех данных из таблицы mytable, получаем Cursor
        // ставим позицию курсора на первую строку выборки  // если в выборке нет строк, вернется false
        if (c.moveToFirst()) { // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("idClient");
            int dateColIndex = c.getColumnIndex("dateVisit");
          //  int phoneColIndex = c.getColumnIndex("cost");
            do {
                map = new HashMap<>();
                map.put("Id", c.getString(idColIndex));
                map.put("Date", c.getString(dateColIndex));
             //   map.put("Phone", c.getString(phoneColIndex));
                arrayList.add(map);
                // переход на следующую строку  // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");
        c.close();






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
                intent = new Intent(this, test.class);
                intent.addFlags(65536);
                startActivity(intent);

                break;
            case R.id.btn3:
                intent = new Intent(this, ReportActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;
//            case R.id.btnNewRecord:
//
//                break;
        }
    }

    public void setDate(View v) {


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                new TimePickerDialog(RecordActivity.this, t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
            }});
    datePickerDialog.show();}



    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(RecordActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
}
