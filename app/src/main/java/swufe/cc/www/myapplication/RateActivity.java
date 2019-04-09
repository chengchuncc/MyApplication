package swufe.cc.www.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView show;
    float d,e,w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);


    }

    public void onClick(View btn){

        final Intent intent = getIntent();
        d = intent.getFloatExtra("dollar",6.7f);
        e = intent.getFloatExtra("euro",11f);
        w = intent.getFloatExtra("won",0.002f);

/*
//使用Bundle接收数据
        Bundle bundle = getIntent().getExtras();
        d = bundle.getFloat("dollar");
        e = bundle.getFloat("euro");
        w = bundle.getFloat("won");
*/

        //获取用户输入内容
        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        }else {
            //提示用户输入内容
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        float val = 0;
        if(btn.getId()==R.id.btn_dollar){
            val = r * (1/d);
        }else if(btn.getId()==R.id.btn_euro){
            val = r * (1/e);
        }else if(btn.getId()==R.id.btn_won){
            val = r * (1/w);
        }
        show.setText(String.format("%.2f", val));
    }

    public void openOne(View btn){
        //打开一个页面Activity
        Log.i("open","openOne:");
        Intent hello = new Intent(this,Rate2Activity.class);
/*        Intent web  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        Intent call  = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:10086"));*/
        startActivity(hello);
    }
}
