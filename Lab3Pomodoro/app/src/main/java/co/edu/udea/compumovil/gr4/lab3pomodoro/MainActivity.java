package co.edu.udea.compumovil.gr4.lab3pomodoro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_startPomodoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startPomodoro = (Button)findViewById(R.id.btn_startPomodoro);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startPomodoro:
                if(btn_startPomodoro.getText().toString().equals(getResources().getString(R.string.startPomodoro))) {
                    btn_startPomodoro.setText(R.string.stopPomodoro);
                } else {
                    btn_startPomodoro.setText(R.string.startPomodoro);
                }

                break;
        }
    }
}
