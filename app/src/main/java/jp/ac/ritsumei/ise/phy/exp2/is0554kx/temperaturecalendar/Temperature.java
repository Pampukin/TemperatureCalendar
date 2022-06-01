package jp.ac.ritsumei.ise.phy.exp2.is0554kx.temperaturecalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Temperature extends AppCompatActivity {

    double tem = 36.5;
    public static double TEM = 0; //どこからでもアクセスできる体温
    TextView text; //入力する日付のテキスト
    TextView temText; //体温が表示されるテキスト

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        tem = 36.5;
        TEM = 0;

        text = findViewById(R.id.date);

        //送られてきたものを受け取る
        Intent intent = getIntent();
        int year = intent.getIntExtra("year",0);
        int month = intent.getIntExtra("month", 0);
        int date = intent.getIntExtra("date",0);
        text.setText(year + "年" +month +"月" + date + "日");
        double tem = intent.getDoubleExtra("tem", 0);

        //既に体温が入力されていた時に入力されていた体温を表示する
        if(tem != 0){
            this.tem = tem;
        }

        this.temText = findViewById(R.id.textView);
        this.temText.setText(this.tem + "℃");
    }

    //体温をボタンで変化させる
    public void chageTem(View view){
        if(view.getId() == R.id.up){
            tem += 0.1;
            if(tem > 50){
                tem = 50;
            }
            tem = (Math.floor(tem * 10)) / 10;
        }else if(view.getId() == R.id.down){
            tem -=0.1;
            if(tem < 0){
                tem = 0;
            }
            tem = (Math.floor(tem * 10)) / 10;
        }

        temText.setText(tem + "℃");
    }

    //カレンダーへ戻る
    public void toCalendar(View view){
        TEM = tem;
        finish();
    }

}