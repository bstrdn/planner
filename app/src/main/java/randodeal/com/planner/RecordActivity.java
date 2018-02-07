package randodeal.com.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3;

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

                break;
            case R.id.btn3:
                intent = new Intent(this, ReportActivity.class);
                intent.addFlags(65536);
                startActivity(intent);
                break;
        }
    }
}
