package jp.ac.ritsumei.ise.phy.exp2.is0554kx.temperaturecalendar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String Ny = "-1"; //今年
    String Nm = "-1"; //今月
    String Nd; //今日の日にち
    int year = -1; //カレンダーの年
    int month = -1; //カレンダーの月
    int[] tDx = new int[43]; //テキストを配列に入れたもの（日にち）
    int[] bDx = new int[43]; //ボタンを配列に入れたもの（体温）
    int bId = 0; //ボタンの位置の番号
    int todayButtonid = 1; //今日のボタンの位置
    int dId = 1; //日にちの位置の番号
    int[] ArrayDate = new int[43]; //カレンダーの日付の位置
    int lastDay = 0; //月の最終日
    TextView text; //対面チェックのテキスト
    double safeTem = 37.5; //対面申請の基準となる体温

    int cp = 0; //カレンダーに含まれる先月の日の個数
    int ct = 0; //カレンダーに含まれる今月の日の個数
    int cn = 0; //カレンダーに含まれる先月の日の個数

    View view = null;

    SharedPreferences data; //体温のデータを入れるもの
    SharedPreferences.Editor editor;

    public static boolean isClean = false;
    SimpleDateFormat sdfM; //MM
    SimpleDateFormat sdfY; //yyyy
    SimpleDateFormat sdfD; //dd

    Calendar c; //カレンダー

    TextView m; //activityのmonth表示
    TextView y; //activityのyear表示


    @Override
    //クリエイト時
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //配列の初期化
        for(int i = 0; i < ArrayDate.length; i++){
            ArrayDate[i] = 0;
        }
        //年月日のフォーマットを作る
        this.c = Calendar.getInstance();
        this.sdfM = new SimpleDateFormat("MM");
        this.sdfY = new SimpleDateFormat("yyyy");
        this.sdfD = new SimpleDateFormat("dd");

        this.Nm = this.sdfM.format(this.c.getTime());
        this.Ny = this.sdfY.format(this.c.getTime());
        this.Nd = this.sdfD.format(this.c.getTime());

        this.m = findViewById(R.id.month);
        this.y = findViewById(R.id.yaer);
        this.m.setText(this.Nm + "月");
        this.y.setText(this.Ny);
        this.year = Integer.parseInt(this.Ny);
        this.month = Integer.parseInt((this.Nm));

        makeButtonIndex();//ボタンのIDの配列を作る
        makeTextIndex();//テキストのIDの配列を作る

        // データを保管する
        if(this.data == null){
            this.data = getSharedPreferences("tem", MODE_PRIVATE);
            this.editor = this.data.edit();
        }

        makeCalendar();

        //bIdを今日の日付の位置にする
        for(int n = 0; n < ArrayDate.length-1; n++){

            if(n == Integer.parseInt(this.Nd)){
                break;
            }
            this.dId++;
            this.todayButtonid++;
        }

        this.text = findViewById(R.id.check);

    }


    @Override
    //適宜行われる
    protected void onResume(){
        super.onResume();

        this.editor.putString("string",String.valueOf(Temperature.TEM));
        this.editor.commit();


        if(this.view != null){
            Button b = findViewById(this.view.getId());

            this.editor.putFloat(makeKey(),(float)Temperature.TEM);
            this.editor.apply();

            b.setText(String.valueOf(this.data.getFloat((makeKey()),0)));
        }

        makeCalendar();
        //全てのデータを消すとsettingsで押したとき
        if(this.isClean){

            this.editor.clear().commit();
            this.year = Integer.parseInt(this.Ny);
            this.month = Integer.parseInt(this.Nm);
            this.m = findViewById(R.id.month);
            this.y = findViewById(R.id.yaer);
            this.y.setText(String.valueOf(year) + "年");
            this.m.setText(String.valueOf(month) + "月");

        }
        this.isClean = false;

        setAllTem();

        start();

        check();

    }

    //月を変える
    public void changeMonth(View view){

        makeTextClean();

        if(view.getId() == R.id.next){
            this.month++;
            if(this.month == 13){
                this.month = 1;
                this.year++;
            }
        }
        if(view.getId() == R.id.pre){
            this.month--;
            if(this.month == 0){
                this.month = 12;
                this.year--;
            }
        }

        this.m.setText(month + "月");
        this.y.setText(year + "年");
        makeCalendar();
        setAllTem();


    }

    //カレンダーを作る
    private void makeCalendar() {

        //配列の初期化
        for(int i = 0; i < ArrayDate.length; i++){
            this.ArrayDate[i] = 0;
        }

        this.c.set(year, month-1, 1);
        this.lastDay = c.getActualMaximum(Calendar.DATE);

        TextView t = null; //日付を入力するテキスト
        int id = 0; //テキストのid
        int firstDay = 0;

        // 日付から曜日を取得する
        switch (this.c.get(Calendar.DAY_OF_WEEK)) {
            //日曜日
            case Calendar.SUNDAY:     // Calendar.SUNDAY:1
                id = 1;
                firstDay = 1;
                t = findViewById(R.id.t1);
                t.setText("(" + 1 + ")");
                break;

            //月曜日
            case Calendar.MONDAY:     // Calendar.MONDAY:2
                id = 2;
                firstDay = 2;
                t = findViewById(R.id.t2);
                t.setText("(" + 1 + ")");
                break;

            //火曜日
            case Calendar.TUESDAY:    // Calendar.TUESDAY:3
                id = 3;
                firstDay = 3;
                t = findViewById((R.id.t3));
                t.setText("(" + 1 + ")");
                break;

            //水曜日
            case Calendar.WEDNESDAY:  // Calendar.WEDNESDAY:4
                id = 4;
                firstDay = 4;
                t = findViewById(R.id.t4);
                t.setText("(" + 1 + ")");
                break;

            //木曜日
            case Calendar.THURSDAY:   // Calendar.THURSDAY:5
                id = 5;
                firstDay = 5;
                t = findViewById(R.id.t5);
                t.setText("(" + 1 + ")");
                break;

            //金曜日
            case Calendar.FRIDAY:     // Calendar.FRIDAY:6
                id = 6;
                firstDay = 6;
                t = findViewById(R.id.t6);
                t.setText("(" + 1 + ")");
                break;

            //土曜日
            case Calendar.SATURDAY:   // Calendar.SATURDAY:7
                id = 7;
                firstDay = 7;
                t = findViewById(R.id.t7);
                t.setText("(" + 1 + ")");
                break;
        }

        makeTextClean();

        this.ct = 0;
        //今月のカレンダー
        for(int i = 1; i < this.lastDay+1; i++){
            this.ct++;
            this.ArrayDate[id] = i;
            t = findViewById(this.tDx[id++]);
            t.setText("(" + i + ")");
            t.setAlpha(1.0f);
        }

        this.cn = 0;
        //今月のカレンダーに写る来月のカレンダー
        int n = 1;
        for(int i = id; i<this.tDx.length; i++){
            this.cn++;
            t = findViewById(this.tDx[i]);
            t.setText("(" + n++ + ")");
            t.setAlpha(0.3f);
        }

        //今月のカレンダーに写る先月のカレンダー
        int preMonth = this.month-1;
        int preYear = this.year;
        if(preMonth==0){
            preMonth = 12;
            preYear--;
        }
        Calendar preCal = Calendar.getInstance();
        preCal.set(preYear,preMonth-1,1);
        int preMonthLastDay = preCal.getActualMaximum(Calendar.DATE); //先月の最終日

        this.cp = 0;
        for(int i = firstDay-1; i>0; i--){
            this.cp++;
            t = findViewById(this.tDx[i]);
            t.setText("(" + preMonthLastDay-- + ")");
            t.setAlpha(0.3f);
        }

        allVisibleButton();
        imvisibleButton();

    }

    //日付のテキストを初期化をする
    public void makeTextClean(){
        for(int i = 1; i<this.tDx.length; i++){
            TextView t = findViewById(this.tDx[i]);
            t.setText(" ");
        }
    }

    //値を保存しておく個別の名前を作る
    private String makeKey(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        this.c.set(this.year,this.month-1,1);
        String key = "";
        if(this.view != null){
            key = sdf.format(this.c.getTime()) + "-" + this.view.getId();
        }else if(this.bId != 0 && view == null){
            key = sdf.format(this.c.getTime()) + "-" + this.bDx[this.bId];
        }

        return key;

    }

    //登録されている体温を適切な場所に表示する
    private void setAllTem(){

        String preId = this.year + "-"+this.month + "-";

        for(int i = 1; i<this.bDx.length; i++){
            String bId = String.valueOf(this.bDx[i]);
            String key = preId + bId;
            Button b = findViewById(this.bDx[i]);

            String tem = String.valueOf(this.data.getFloat(key,0));
            int num = (int)this.data.getFloat(key,0); //keyのデータがあるかどうか調べ無かったら0を代入する
            if(num == 0){
                tem = ""; //0の場合表示されないようにする
            }
            b.setText(tem);
        }

    }

    //体温設定画面に行く
    public void toTemScene(View view){

        this.view = view;
        int dateId = 0;
        for(int i = 0; i< this.bDx.length;i++){
            if(this.view.getId() == this.bDx[i]){
                break;
            }
            dateId++;
        }
        int date = this.ArrayDate[dateId];
        Intent intent = new Intent(this, Temperature.class);
        intent.putExtra("year", this.year);
        intent.putExtra("month", this.month);
        intent.putExtra("date", date );

        //既に体温が登録されていた場所をクリックしたときその体温を持ってTemperatureに行く
        double tem = 0;
        Button usingButton =findViewById(view.getId());
        if(usingButton.getText() != ""){
            tem = Double.parseDouble(usingButton.getText().toString());
        }
        intent.putExtra("tem", tem);

        startActivity(intent);

    }

    //設定画面に行く
    public void toSettings(View view){

        Intent intent = new Intent(this, settings.class);
        startActivity(intent);

    }

    //テキストを配列に入れる
    private void makeTextIndex(){

        tDx[0] = 0;
        tDx[1] = R.id.t1;
        tDx[2] = R.id.t2;
        tDx[3] = R.id.t3;
        tDx[4] = R.id.t4;
        tDx[5] = R.id.t5;
        tDx[6] = R.id.t6;
        tDx[7] = R.id.t7;
        tDx[8] = R.id.t8;
        tDx[9] = R.id.t9;
        tDx[10] = R.id.t10;
        tDx[11] = R.id.t11;
        tDx[12] = R.id.t12;
        tDx[13] = R.id.t13;
        tDx[14] = R.id.t14;
        tDx[15] = R.id.t15;
        tDx[16] = R.id.t16;
        tDx[17] = R.id.t17;
        tDx[18] = R.id.t18;
        tDx[19] = R.id.t19;
        tDx[20] = R.id.t20;
        tDx[21] = R.id.t21;
        tDx[22] = R.id.t22;
        tDx[23] = R.id.t23;
        tDx[24] = R.id.t24;
        tDx[25] = R.id.t25;
        tDx[26] = R.id.t26;
        tDx[27] = R.id.t27;
        tDx[28] = R.id.t28;
        tDx[29] = R.id.t29;
        tDx[30] = R.id.t30;
        tDx[31] = R.id.t31;
        tDx[32] = R.id.t32;
        tDx[33] = R.id.t33;
        tDx[34] = R.id.t34;
        tDx[35] = R.id.t35;
        tDx[36] = R.id.t36;
        tDx[37] = R.id.t37;
        tDx[38] = R.id.t38;
        tDx[39] = R.id.t39;
        tDx[40] = R.id.t40;
        tDx[41] = R.id.t41;
        tDx[42] = R.id.t42;


    }

    //ボタンを配列に入れる
    private void makeButtonIndex(){
        bDx[0] = 0;
        bDx[1] = R.id.c1;
        bDx[2] = R.id.c2;
        bDx[3] = R.id.c3;
        bDx[4] = R.id.c4;
        bDx[5] = R.id.c5;
        bDx[6] = R.id.c6;
        bDx[7] = R.id.c7;
        bDx[8] = R.id.c8;
        bDx[9] = R.id.c9;
        bDx[10] = R.id.c10;
        bDx[11] = R.id.c11;
        bDx[12] = R.id.c12;
        bDx[13] = R.id.c13;
        bDx[14] = R.id.c14;
        bDx[15] = R.id.c15;
        bDx[16] = R.id.c16;
        bDx[17] = R.id.c17;
        bDx[18] = R.id.c18;
        bDx[19] = R.id.c19;
        bDx[20] = R.id.c20;
        bDx[21] = R.id.c21;
        bDx[22] = R.id.c22;
        bDx[23] = R.id.c23;
        bDx[24] = R.id.c24;
        bDx[25] = R.id.c25;
        bDx[26] = R.id.c26;
        bDx[27] = R.id.c27;
        bDx[28] = R.id.c28;
        bDx[29] = R.id.c29;
        bDx[30] = R.id.c30;
        bDx[31] = R.id.c31;
        bDx[32] = R.id.c32;
        bDx[33] = R.id.c33;
        bDx[34] = R.id.c34;
        bDx[35] = R.id.c35;
        bDx[36] = R.id.c36;
        bDx[37] = R.id.c37;
        bDx[38] = R.id.c38;
        bDx[39] = R.id.c39;
        bDx[40] = R.id.c40;
        bDx[41] = R.id.c41;
        bDx[42] = R.id.c42;
    }

    //先月と来月の分のカレンダーのボタンを非表示にする
    private void imvisibleButton(){
        for(int i = 1; i < cp+1; i++){
            Button b = findViewById(bDx[i]);
            View view = b;
            int year = this.year;
            int month = this.month;
            String id = year + "-" + month + "-" + view.getId();
            editor.putFloat(id,-1);
            editor.apply();
            b.setVisibility(View.INVISIBLE);
        }
        for(int i = 1 + cp + ct; i < bDx.length; i++){
            Button b = findViewById(bDx[i]);
            View view = b;
            int year = this.year;
            int month = this.month;
            String id = year + "-" + month + "-" + view.getId();
            editor.putFloat(id,-1);
            editor.apply();
            b.setVisibility(View.INVISIBLE);
        }
    }

    //全てのボタンを表示する
    private void allVisibleButton(){
        for(int i = 1; i < bDx.length; i++){
            Button b = findViewById(bDx[i]);
            b.setVisibility(View.VISIBLE);
        }
    }

    //今日の体温が入力されていなかったら今日の体温の入力画面に移行する
    private void start(){
        //今日のボタンの位置を設定
        Button todayButton = findViewById(this.bDx[this.todayButtonid]);
        View view = todayButton;
        int year = Integer.parseInt(this.Ny);
        int month = Integer.parseInt(this.Nm);

        String todayId = year + "-" + month + "-" + view.getId();
        float tem = this.data.getFloat(todayId,0);

        if(tem == 0){
            //this.view = (View)todayButton;
            Intent intent = new Intent(this, Temperature.class);
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("date", Integer.parseInt(this.Nd) );
            this.view = todayButton;
            startActivity(intent);
        }

    }

    //対面活動可能かどうか表示する
    private void check(){
        if(isCheck()){
            this.text.setText("対面活動: 可能");
        }else{
            this.text.setText("対面活動: 不可" );
        }

    }

    private boolean isCheck(){
        int safeNum = 0;
        //今日を取得
        int checkId = this.todayButtonid;
        int year = Integer.parseInt(this.Ny);
        int month = Integer.parseInt(this.Nm);

        for(int i = 0; i < 14; i++){
            if(checkId == 0){
                checkId = 42;
                month--;
                if(month == 0){
                    year--;
                    month = 12;
                }
            }
            Button todayButton = findViewById(this.bDx[checkId--]);
            View view = todayButton;

            //体温が入っているidを制作
            String todayId = year + "-" + month + "-" + view.getId();
            //idの体温
            float tem = this.data.getFloat(todayId,64);

            //何も入力できない場所であったらループを戻す
            if(tem == 64){

            } else if(tem == -1){
                i--;
            }else if(tem <= this.safeTem) {
                safeNum++;
            }else if(tem > this.safeTem){
                break;//軽くするための処理
            }
        }

        //14日間連続で体温がsafeTemよりも低かったら
        if(safeNum >= 14){
            return true;
        }else{
            return false;
        }

    }

}