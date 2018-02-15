package randodeal.com.planner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {

    DBHelper dbHelper;    SQLiteDatabase db;
    List<String> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        catList = new ArrayList<>();
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // подключаемся к БД
        db = dbHelper.getWritableDatabase();
        String[] args = {"петр викторович"};
        // делаем запрос всех данных из таблицы, получаем Cursor
        Cursor c = db.query("client", null, "name = ?", args, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idClient = c.getColumnIndex("idClient");
//            int dateVisit = c.getColumnIndex("dateVisit");
//            int cost = c.getColumnIndex("cost");
            do { // получаем значения по номерам столбцов и пишем все в лог
                System.out.println(c.getString(idClient));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("ошибка", "0 rows");
        c.close();
    }
}
