package pe.com.codespace.transito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread start_splash = new Thread() {
            @Override
            public void run() {
                try{
                    sleep(SPLASH_SCREEN_DELAY);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashScreenActivity.this,ActivityMain.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        start_splash.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
