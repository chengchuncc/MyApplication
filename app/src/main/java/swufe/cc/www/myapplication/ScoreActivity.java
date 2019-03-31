package swufe.cc.www.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (TextView) findViewById(R.id.score);
    }

    public void btnAdd1(View btn) {
        showScore(1);
    }
    public void btnAdd2(View btn) {
        showScore(2);
    }
    public void btnAdd3(View btn) {
        showScore(3);
    }
    public void btnReset(View btn) {
        score.setText("0");
    }
    private void showScore(int inc){
        Log.i("show","int="+inc);
        String oldScore = (String)score.getText();
        int newScore = Integer.parseInt(oldScore)+inc;
        score.setText(""+newScore);
    }
}
