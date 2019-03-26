package swufe.cc.www.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity implements View.OnClickListener {

    TextView out;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello2);

        out = (TextView) findViewById(R.id.txtout);
        edit = (EditText) findViewById(R.id.inp);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("main","onClick msg....");
        String str = edit.getText().toString();
        /**获取edittext中的editable类型数据转换为String类型**/
        float str1 =Float.parseFloat(str);
        /**将从输入获取的string类型数据转换为float类型**/
        float d = str1*9/5+32;
        /**将摄氏度乘上9/5再加上32就等于相应的华氏度**/
        String str2 = getString(R.string.out);
        out.setText(str2+"："+d+"°F");
        /**在textview中输出结果**/
    }
}
