package jp.ac.ritsumei.ise.phy.exp2.is0554kx.temperaturecalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class settings extends AppCompatActivity {



    Button bY; //確認ボタンyes
    Button bN; //確認ボタンno
    TextView text; //確認のテキスト表示、その結果の表示テキスト


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        bY = findViewById(R.id.yes);
        bN = findViewById(R.id.no);
        text = findViewById(R.id.confirm);

        bY.setVisibility(View.INVISIBLE);
        bN.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
    }

    //yesを押したとき
    public void dataAllClear(View view){

        MainActivity.isClean = true;
        bY.setVisibility(View.INVISIBLE);
        bN.setVisibility(View.INVISIBLE);
        text.setText("データは削除されました");

    }
    //noを押したとき
    public void hoge(View view){
        bY.setVisibility(View.INVISIBLE);
        bN.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
    }

    //データをすべて消すを押したとき
    public void confirm(View view){
        bY.setVisibility(View.VISIBLE);
        bN.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
    }

    public void toCalendar(View view){
        finish();
    }


}