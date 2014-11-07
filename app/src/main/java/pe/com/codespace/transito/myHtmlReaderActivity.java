package pe.com.codespace.transito;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;

public class myHtmlReaderActivity extends Activity {

    private static final String DIRECTORY= "file:///android_asset/html/";
    private static final String PAGE1= "Anexo3.htm";
    private static final String PAGE2= "Anexo4.htm";
    private static final String PAGE3= "Anexo5.htm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        try{
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setBuiltInZoomControls(true);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.setBackgroundColor(getResources().getColor(R.color.background_text));
            String path1 = DIRECTORY + PAGE1;
            myWebView.loadUrl(path1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_pdf_viewer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
