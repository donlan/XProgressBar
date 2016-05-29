package dong.lan.xprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import dong.lan.xprogressbar.view.CircleProgressBar;
import dong.lan.xprogressbar.view.TextProgressBar;

public class MainActivity extends AppCompatActivity {

    TextProgressBar progressBar;
    CircleProgressBar circleProgressBar;
    MyHandler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (TextProgressBar) findViewById(R.id.progress);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_pro);
        handle = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int p = 0;
                while (p < 100) {
                    handle.sendEmptyMessage(++p);
                    try {
                        Thread.sleep((long) (Math.random() * 500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setProgress(msg.what);
            circleProgressBar.setProgress(msg.what);
        }
    }
}
