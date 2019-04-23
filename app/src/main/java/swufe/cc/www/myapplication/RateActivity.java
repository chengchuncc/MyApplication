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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
                    Bundle bdl = (Bundle) msg.obj;
                    d = bdl.getFloat("d");
                    e = bdl.getFloat("e");
                    w = bdl.getFloat("w");

                    Log.i(TAG, "handleMessage: dollar:"+d);
                    Log.i(TAG, "handleMessage: euro:"+e);
                    Log.i(TAG, "handleMessage: won:"+w);

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();

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
            val = r * d;
        }else if(btn.getId()==R.id.btn_euro){
            val = r * e;
        }else if(btn.getId()==R.id.btn_won){
            val = r * w;
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
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            //用于保存获取的汇率
            Bundle bundle = new Bundle();



        //获取网络数据
        /*URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html" + html);
            Document doc = Jsoup.parse(html);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/

        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            /*int i =1;
            for(Element table : tables){
                Log.i(TAG,"run:table["+i+"]="+table);
                i++;
            }*/

            Element table1 = tables.get(0);
            //Log.i(TAG,"run:table6="+table1);
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for(int i =0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG, "run:"+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("d",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("e",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("w",100f/Float.parseFloat(val));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //bundle中保存所获取的汇率

        //获取Msg对象用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.obj = "Hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);

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
