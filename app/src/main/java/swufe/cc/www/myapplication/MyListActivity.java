package swufe.cc.www.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "MyList";
    Handler handler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        listView = (ListView) findViewById(R.id.mylist);
        String data[]={"waiting"};

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==7){
                    List<String> list2 = (List<String>)msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(MyListActivity.this,android.R.layout.simple_list_item_1,list2);
                    listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void run() {
        //获取网络数据，放入list带回主线程
        List<String> retList = new ArrayList<String>();
        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(1);
            Elements tds = table1.getElementsByTag("td");
            for(int i = 0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();

                Log.i(TAG, "run:"+td1.text()+"==>"+td2.text());
                retList.add(str1+"==>"+val);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(7);
        //msg.obj = "Hello from run()";
        msg.obj = retList;
        handler.sendMessage(msg);

    }
}
