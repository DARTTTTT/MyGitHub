package wrg.com.mygithub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private WebView webView;

    private ImageView floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        floatingActionButton= (ImageView) findViewById(R.id.float_button);
        //floatingActionButton.setAlpha(0.5f);
        webView.loadUrl("https://github.com/WENGRUIGAN");
        new WebviewSelfAdaption(this).getWebviewAdaption(webView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("https://github.com/WENGRUIGAN");

            }
        });

        webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.d("print", "onPageStarted: " + "--------------onPageStarted--------");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        int w = View.MeasureSpec.makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
                        int contentHeight = webView.getContentHeight();
                        Log.d("print", "onPageFinished:getContentHeight----------------> " + contentHeight);
                        if (webView.getContentHeight() * webView.getScale() - (webView.getHeight() + webView.getScrollY()) == 0) {
                            //XXX
                            int webviewheight = (int) (webView.getContentHeight() * webView
                                    .getScale());
                            view.measure(w, webviewheight);
                            Log.d("print", "onPageFinished: " + "--------------onPageFinished--------" + "--onPageFinished--------> webviewheight = " + webviewheight);

                        }
                     /*   int webviewheight = (int) (webView.getContentHeight() * webView
                                .getScale());
                        view.measure(w,webviewheight);*/

                        //Log.d("print", "onPageFinished: "+"--------------onPageFinished--------"+"--onPageFinished--------> webviewheight = "+webviewheight);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode,
                                                String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        Log.d("print", "onPageFinished: " + "--------------onReceivedError--------");

                    }

                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url); // 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
                        //addImageClickListener();
                        new WebviewSelfAdaption(getApplicationContext()).getWebviewAdaption(view);


                    }
                });
            }
        }, 1000);


        //webView.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");


    }




    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }


    }

    /**
     * 返回是上一个html
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
