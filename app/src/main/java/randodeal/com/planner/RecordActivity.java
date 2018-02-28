package randodeal.com.planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.utils.Utils;

public class RecordActivity extends Activity implements View.OnClickListener {
    Button btn1,btn2,btn3;

    Button btnNewRecord;                //--------------новая запись
    TextView currentDateTime;           //--------------текствью (скрыт)
    Calendar dateAndTime = Calendar.getInstance(); //----текущая дата
    Calendar dateInCal = Calendar.getInstance();
    Calendar calendarNow = Calendar.getInstance();
    DBHelper dbHelper;    SQLiteDatabase db;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> arrayList;
    List<String> listClietn;           //---------------список клиентов
    List<Calendar> listEvents;         //--------------список дат для событий
    ListView lvRecord;
    //ArrayAdapter adapter;
    ArrayAdapter adapter2;
    AutoCompleteTextView autoCompleteTextView2;
    EditText etPhone2;
    Cursor c;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    AlertDialog.Builder ad;
    Context context;
    private HorizontalCalendar horizontalCalendar;
    long minDateToList;              //----------время для начальной инициации
    int userAlreadyExists;           //----------существует ли такой же пользователь



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
        etPhone2 = (EditText) findViewById(R.id.etPhone2);
        currentDateTime = (TextView) findViewById(R.id.currentDateTime);
        arrayList = new ArrayList<>();
        setInitialDateTime();
        listClietn = new ArrayList<>();
        listEvents = new ArrayList<>();
        //установка даты текущего дня 0:00
        Calendar minDate;
        minDate = Calendar.getInstance();
        System.out.println("----------------------------------------------------- " + minDate);
        minDate.set(Calendar.SECOND, 0);
        minDate.set(Calendar.MINUTE, 0);
        minDate.set(Calendar.HOUR_OF_DAY, 0);
        minDateToList = minDate.getTimeInMillis();





        // создаем объект для создания и управления версиями БД / подключаемся к БД
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        //заполняем листвью
        lvRecord ();

        //НАЖАТИЕ НА СПИСОК
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("позиция", "itemClick: position = " + position + ", id = " + id);

                final Dialog dialog = new Dialog(RecordActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.record2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnRecDel = (Button) dialog.findViewById(R.id.btnRecDel);
                Button btnAddCost = (Button) dialog.findViewById(R.id.btnAddCost);
                final TextView etCost = (TextView) dialog.findViewById(R.id.etCost);
                etCost.setText(1500 + "");
                final String IdRec = arrayList.get(position).get("IdRec");
            //удалить
                btnRecDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
            //закрытие диалога
                        dialog.dismiss();
            //удаление из базы по idRec
                        db.delete("record", "idRecord" + "='" + IdRec+"'", null);
                        arrayList.clear();
                        listEvents.clear();
                        lvRecord();
                        horizontalCalendar.refresh();
                    }
                });
                //добавление стоимости
                btnAddCost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues cv = new ContentValues();
//                        cv.put("coast", Integer.parseInt((String) etCost.getText()));
                       String etCostt = etCost.getText().toString();
                        cv.put("cost", etCostt);
                        //               System.out.println(dateMS);
//                cv.put("more", more);
                     //   db.insert("record", null, cv);
                        db.update("record", cv, "idRecord="+IdRec, null);
                       // db.delete("record", "idRecord" + "='" + arrayList.get(position).get("IdRec")+"'", null);
//                        arrayList.clear();
//                        listEvents.clear();
//                        lvRecord();
//                        horizontalCalendar.refresh();

                        dialog.dismiss();
                    }
                });

                dialog.show();
//                String value = adapter.getItem(position);
//                System.out.println(value);
            }
        });




        //////////////////////////
        //////////////////////////
        /////////КАЛЕНДАРЬ СВЕРХУ
        //////////////////////////
        /////////////////////////
           /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -4);
    /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 4);
//        final Calendar defaultSelectedDate = Calendar.getInstance();

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .addEvents(new CalendarEventsPredicate() {
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        for (Calendar i : listEvents) {
//                            System.out.println("ВЫВОД ДАТЫ " + i.getTimeInMillis());
                            if (Utils.isSameDate(date, i)) {
                                events.add(new CalendarEvent(0xFFFFFFFF, "событие"));
                            }
                        }
                        return events;
                    }
                })
                .build();



        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
//                String selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
//                Toast.makeText(RecordActivity.this, selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
//                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
//                System.out.println("ДАТА" + date.getTimeInMillis());
//                System.out.println("ПОЗИЦИЯ" + position);
                minDateToList = date.getTimeInMillis();
                calendarNow = date;
                arrayList.clear();
                listEvents.clear();
                lvRecord();
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
        //////////////////////////
        //////////////////////////
        /////////КАЛЕНДАРЬ СВЕРХУ
        //////////////////////////
        /////////////////////////

        //выпадающий список пациентов
        autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        adapter2 = new ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line, listClietn);
        autoCompleteTextView2.setAdapter(adapter2);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ////////обработчик нажатия
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(RecordActivity.this,
                        adapter2.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
           //     String[] at4 = {adapter2.getItem(position).toString()};
                Cursor c = null;
                String[] argsName = {adapter2.getItem(position).toString()};
                c = db.query("client", null,"name = ?", argsName, null, null,null );
                c.moveToFirst();
             //   int idClient = c.getColumnIndex("idClient");
 //               System.out.println(c.getString(idClient));
            }
        });




//ЗАКРЫТИЕ onCreate
    }

    //ЛИСТВЬЮ ЗАПОЛНЕНИЕ
    private void lvRecord() {
        //получаем список записей
        c = db.query("record", null, null, null, null, null, "dateVisit"); // делаем запрос всех данных из таблицы mytable, получаем Cursor
        // ставим позицию курсора на первую строку выборки  // если в выборке нет строк, вернется false
        if (c.moveToFirst()) { // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("idClient");
            int  dateColIndex = c.getColumnIndex("dateVisit");
            int  idRecColIndex = c.getColumnIndex("idRecord");

            //  int phoneColIndex = c.getColumnIndex("cost");
            do {
                map = new HashMap<>();
                map.put("Id", c.getString(idColIndex));
                map.put("IdRec", c.getString(idRecColIndex));

               // map.put("Date", c.getString(dateColIndex));
                String dateMS = DateUtils.formatDateTime(this, c.getLong(dateColIndex), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
                map.put("Date", dateMS);
                    ///////запись даты событий для горизонтального колендаря
                Calendar calendarDate = Calendar.getInstance(); //--------дата, для горизонтального календаря
                calendarDate.setTimeInMillis(c.getLong(dateColIndex));
//                System.out.println("КАЛЕНДАРЬ -------- дата для ГК " + calendarDate.getTimeInMillis());
                listEvents.add(calendarDate);

                        ///проверка даты, чтобы она была на текущий день
                if (c.getLong(dateColIndex) > minDateToList && c.getLong(dateColIndex) < minDateToList + 1000 * 60 * 60 * 24) {
                    arrayList.add(map);
                }
                // переход на следующую строку  // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");
        c.close();
//        dbHelper = new DBHelper(this);
//        db = dbHelper.getWritableDatabase();

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
                R.layout.my_item, new String[]{"Id","Date"},
                new int[]{R.id.text1, R.id.text2});
        lvRecord.setAdapter(adapter);

        ///получаем список клиентов
        c = db.query("client", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idClient = c.getColumnIndex("name");
            do {
                listClietn.add(c.getString(idClient));
            } while (c.moveToNext());
        } else
            Log.d("ошибка", "0 rows");
        c.close();
    }

    ///КНОПКИ
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

//                intent = new Intent(this, Record2Activity.class);
//                intent.addFlags(65536);
//                startActivity(intent);

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


    //устанавливаем дату
    public void setDate(View v) {

        boolean a = false;
        final String man = autoCompleteTextView2.getText().toString();
        //если не пустое имя
        if (!man.equals("")) {
        //проверка есть ли в базе этот человек
            for (String ctlst : listClietn) {
                if (man.equals(ctlst)) {
                    a = true;
                }
            }
        //если такого клиента нет в базе а = false
            if (!a) {
                context = RecordActivity.this;
                String title = "Клиента " + man + " нет в базе!";
                String message = "Добавить?";
                String button1String = "Добавить в базу";
                String button2String = "Отмена";
                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);  // заголовок
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        datePickerDialog = new DatePickerDialog(context, d,
                                calendarNow.get(Calendar.YEAR),
                                calendarNow.get(Calendar.MONTH),
                                calendarNow.get(Calendar.DAY_OF_MONTH)

                        );
                        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                            //добавляем нового клиента
                                ContentValues cv = new ContentValues();
                                cv.put("name", man);
                                if (!etPhone2.getText().toString().equals("")) {
                                    cv.put("phone", etPhone2.getText().toString());
                                }
                                db.insert("client", null, cv);
                                //после - выбор времени setTime();
                                setTime();
                            }

                        });


                        //диалог выбора даты
                        datePickerDialog.show();

                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //отмена добавления
                    }
                });
                ad.setCancelable(true);
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        //отмена кнопкой назад
                    }
                });
                ad.show();

            } else {
                datePickerDialog = new DatePickerDialog(this, d,
                        calendarNow.get(Calendar.YEAR),
                        calendarNow.get(Calendar.MONTH),
                        calendarNow.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    //после - выбор времени setTime();
                    public void onDismiss(DialogInterface dialog) {
                        setTime();
                    }

                });
                //диалог выбора даты
                datePickerDialog.show();
            }

        }
    }



    // отображаем диалоговое окно для выбора времени
    public void setTime() {
        userAlreadyExists = 0;
        timePickerDialog = new TimePickerDialog(RecordActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true);
        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
//!!!СОХРАНЕНИЕ В БАЗЕ
                ContentValues cv = new ContentValues();
                String name = autoCompleteTextView2.getText().toString();
                String date = currentDateTime.getText().toString();
                long dateMS = dateAndTime.getTimeInMillis();
//                String more = etMore.getText().toString();
                //проверка даты, есть ли уже такая
                for (Map<String, String> hashMap : arrayList)
                {     // For each hashmap, iterate over it
                    for (Map.Entry<String, String> entry  : hashMap.entrySet())
                    { // Do something with your entrySet, for example get the key.
                        String sListName = entry.getValue();
                      //  String sdateMS = DateUtils.formatDateTime(this, entry., DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
                       //         DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
//                        System.out.println("выбор времени =============" +sListName);
//                        System.out.println("ВРЕМЯ ДЛЯ СРАВНЕНИЯ =======" +date);
                        if (sListName.equals(date)) {
                            userAlreadyExists = 1;
                        }
                    }
                }
if (userAlreadyExists == 0) {
    cv.put("idClient", name);
    cv.put("dateVisit", dateMS);
    //               System.out.println(dateMS);
//                cv.put("more", more);
    db.insert("record", null, cv);

//                Toast toast = Toast.makeText(getApplicationContext(),
//                        name + " добавлен!", Toast.LENGTH_SHORT);
//                toast.show();

    //очиста списка, перестроение
    autoCompleteTextView2.setText("");
    etPhone2.setText("");
    arrayList.clear();
    listEvents.clear();
    lvRecord();
    horizontalCalendar.refresh();
}
else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Это время уже занято!", Toast.LENGTH_SHORT);
                toast.show();
}





            }
            public void setCancelable(boolean flag) {

            }


        });
        timePickerDialog.show();

//        new TimePickerDialog(RecordActivity.this, t,
//                dateAndTime.get(Calendar.HOUR_OF_DAY),
//                dateAndTime.get(Calendar.MINUTE), true)
//                .show();

    }



    // установка начальных даты и времени
    private void setInitialDateTime() {
        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY));
       // currentDateTime.setText((CharSequence) dateAndTime.getTime());
//        System.out.println(dateAndTime.getTimeInMillis());
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
