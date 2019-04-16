package swufe.cc.www.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "Rate";
    private float d = 0.0f,e = 0.0f,w = 0.0f;
    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        d = sharedPreferences.getFloat("dolla_rate",0.0f);
        e = sharedPreferences.getFloat("euro_rate",0.0f);
        w = sharedPreferences.getFloat("won_rate",0.0f);

        Log.i(TAG, "onCreate: sp dollarRate=" + d);
        Log.i(TAG, "onCreate: sp euroRate=" + e);
        Log.i(TAG, "onCreate: sp wonRate=" + w);

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        handler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String)msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg =" + str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };//匿名类定义对象

    }

    public void onClick(View btn){


        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        }else {
            //提示用户输入内容
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
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
        openConfig();
    }

    private void openConfig() {
        Intent intent = new Intent(this, Rate2Activity.class);
        intent.putExtra("dollar", d);
        intent.putExtra("euro", e);
        intent.putExtra("won", w);

        Log.i(TAG,"dollarRate="+d);
        Log.i(TAG,"euroRate="+e);
        Log.i(TAG,"wonRate="+w);

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
            openConfig();

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

            Log.i(TAG,"get newDollar2="+d);
            Log.i(TAG,"get newEuro2="+e);
            Log.i(TAG,"get newWon2="+w);

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dolla_rate",d);
            editor.putFloat("euro_rate",e);
            editor.putFloat("won_rate",w);
            editor.commit();
            Log.i(TAG, "onActivityResult:数据已保存到sharedPreferences");

        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run()....");
        for(int i=1 ;i<3;i++){
            Log.i(TAG, "run: i="+i);
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        //获取Msg对象用于返回主线程
        Message msg = handler.obtainMessage(5);
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html" + html);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
