package randodeal.com.planner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> alHistory;
    HashMap<String,String> mapHistory;
    ListView lvHistory;
    TextView tvHistory1;
    DBHelper dbHelper;
    SQLiteDatabase db;

    String idClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Intent intent = getIntent();
        idClient = intent.getStringExtra("idClient");

        tvHistory1 = (TextView) findViewById(R.id.tvHistory1);


        lvHistory = (ListView) findViewById(R.id.lvHistory);
        alHistory = new ArrayList<>();

//        mapHistory = new HashMap<>();
//        mapHistory.put("Date", "12 января");
//        mapHistory.put("Cost", "20000" + " рублей");
//        alHistory.add(mapHistory);
//
//        mapHistory = new HashMap<>();
//        mapHistory.put("Date", "13 января");
//        mapHistory.put("Cost", "20000" + " рублей");
//        alHistory.add(mapHistory);
//
//        mapHistory = new HashMap<>();
//        mapHistory.put("Date", "14 января");
//        mapHistory.put("Cost", "20000" + " рублей");
//        alHistory.add(mapHistory);


        tvHistory1.setText("клиент №" + idClient);

        lvHistory();
    }

    void lvHistory () {
        SimpleAdapter adapter = new SimpleAdapter(this, alHistory, R.layout.my_item_history,
                new String[]{"dateVisit", "cost"}, new int[]{R.id.text1, R.id.text2});
        lvHistory.setAdapter(adapter);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query("record", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                String dateVisit = c.getString(c.getColumnIndex("dateVisit"));
                String cost = c.getString(c.getColumnIndex("cost"));
                mapHistory = new HashMap<>();
                mapHistory.put("dateVisit", dateVisit);
                mapHistory.put("cost", cost);
                alHistory.add(mapHistory);
            } while (c.moveToNext());
        }
            c.close();
    }






}
