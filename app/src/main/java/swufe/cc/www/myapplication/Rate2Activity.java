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

    }
    public void changeRate(View btn){
        String ed = editd.getText().toString();
        String ee = edite.getText().toString();
        String ew = editw.getText().toString();

        d = Float.parseFloat(ed);
        e = Float.parseFloat(ee);
        w = Float.parseFloat(ew);
        //打开一个页面Activity
        Log.i("open","openOne:");
        Intent intent = new Intent(this,RateActivity.class);
        intent.putExtra("dollar",d);
        intent.putExtra("euro",e);
        intent.putExtra("won",w);
        startActivity(intent);
    }
}
