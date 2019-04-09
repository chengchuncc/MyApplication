package swufe.cc.www.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Rate2Activity extends AppCompatActivity {
    EditText editd;
    EditText edite;
    EditText editw;
    float d=0,e=0,w=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate2);

        editd = (EditText) findViewById(R.id.edit_dollar);
        edite = (EditText) findViewById(R.id.edit_euro);
        editw = (EditText) findViewById(R.id.edit_won);

        final Intent intent = getIntent();
        d = intent.getFloatExtra("dollar",0.0f);
        e = intent.getFloatExtra("euro",0.0f);
        w = intent.getFloatExtra("won",0.0f);

        editd.setText(""+d);
        edite.setText(""+e);
        editw.setText(""+w);
    }
    public void changeRate(View btn){
        //获取新的值
        String ed = editd.getText().toString();
        String ee = edite.getText().toString();
        String ew = editw.getText().toString();

        d = Float.parseFloat(ed);
        e = Float.parseFloat(ee);
        w = Float.parseFloat(ew);

        Log.i("changeRate:","获取到新的值");
        Log.i("onCreate:","newDollar="+d);
        Log.i("onCreate:","newEuro="+e);
        Log.i("onCreate:","newWon="+w);

        //保存到bundle或放到extra中
        Log.i("open","changeRate:");
        Intent intent2 = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar2",d);
        bdl.putFloat("euro2",e);
        bdl.putFloat("won2",w);
        intent2.putExtras(bdl);
        setResult(2,intent2);
        //返回到调用页面
        finish();
    }
}
