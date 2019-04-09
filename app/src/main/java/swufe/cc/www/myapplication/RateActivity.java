package swufe.cc.www.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView show;
    float d = 6.7f,e = 11f,w = 0.002f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);


    }

    public void onClick(View btn){


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
        OpenConfig();

    }

    private void OpenConfig() {
        Intent intent = new Intent(this, Rate2Activity.class);
        intent.putExtra("dollar", d);
        intent.putExtra("euro", e);
        intent.putExtra("won", w);

        Log.i("openOne:","dollarRate="+d);
        Log.i("openOne:","euroRate="+e);
        Log.i("openOne:","wonRate="+w);

        startActivityForResult(intent, 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            OpenConfig();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            d = bundle.getFloat("dollar2",0.1f);
            e = bundle.getFloat("euro2",0.1f);
            w = bundle.getFloat("won2",0.1f);

            Log.i("onActivityResult","get newDollar2="+d);
            Log.i("onActivityResult","get newEuro2="+e);
            Log.i("onActivityResult","get newWon2="+w);

        }
    }
}
