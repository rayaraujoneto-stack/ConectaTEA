package br.com.conectatea.app;
import android.app.Activity;import android.os.Bundle;import android.speech.tts.TextToSpeech;import android.webkit.WebView;import android.webkit.WebSettings;import android.webkit.JavascriptInterface;import java.util.Locale;import java.util.UUID;
public class MainActivity extends Activity {
private TextToSpeech tts;private boolean ready=false;private String pending;
@Override public void onCreate(Bundle b){super.onCreate(b);
tts=new TextToSpeech(this,status->{if(status==TextToSpeech.SUCCESS&&tts!=null){int l=tts.setLanguage(new Locale("pt","BR"));ready=l!=TextToSpeech.LANG_MISSING_DATA&&l!=TextToSpeech.LANG_NOT_SUPPORTED;if(ready&&pending!=null){String p=pending;pending=null;say(p);}}});
WebView w=new WebView(this);WebSettings s=w.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(true);s.setAllowFileAccessFromFileURLs(false);s.setAllowUniversalAccessFromFileURLs(false);
w.addJavascriptInterface(new Object(){@JavascriptInterface public void speak(String t){runOnUiThread(()->say(t));}},"AndroidSpeech");
setContentView(w);w.loadUrl("file:///android_asset/index.html");}
private void say(String t){if(tts==null||t==null||t.trim().isEmpty())return;if(!ready){pending=t;return;}tts.stop();tts.speak(t,TextToSpeech.QUEUE_FLUSH,null,UUID.randomUUID().toString());}
@Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}