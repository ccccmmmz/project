package project.common.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.test.commonporject.R;


public class WebViewActivity extends AppCompatActivity implements IWebPageView {


    // 进度条
    private ProgressBar mProgressBar;
    private WebView webView;
    // 全屏时视频加载view
    private FrameLayout videoFullView;
    private Toolbar mTitleToolBar;
    // 加载视频相关
    //private MyWebChromeClient mWebChromeClient;
    // title
    private String mTitle;
    // 网页链接
    private String mUrl;
    // 可滚动的title 使用简单 没有渐变效果，文字两旁有阴影
    private TextView tvGunTitle;
    private boolean isTitleFix;
    private CusWebClient mWebChromeClient;

    /**
     * 打开网页:
     *
     * @param mContext 上下文
     * @param mUrl     要加载的网页url
     * @param mTitle   title
     */
    public static void loadUrl(Context mContext, String mUrl, String mTitle) {
        loadUrl(mContext, mUrl, mTitle, false);
    }

    /**
     * 打开网页:
     *
     * @param mContext     上下文
     * @param mUrl         要加载的网页url
     * @param mTitle       title
     * @param isTitleFixed title是否固定
     */
    public static void loadUrl(Context mContext, String mUrl, String mTitle, boolean isTitleFixed) {
        if (isNetworkConnected(mContext)) {
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("mUrl", mUrl);
            intent.putExtra("isTitleFix", isTitleFixed);
            intent.putExtra("mTitle", mTitle == null ? "" : mTitle);
            mContext.startActivity(intent);
        } else {
            //当前网络不可用
            System.out.println("网络不可用");
        }
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getIntentData();
        initTitle();
        initWebView();
        syncCookie(mUrl);
        webView.loadUrl(mUrl);
        getDataFromBrowser(getIntent());
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("mTitle");
            mUrl = getIntent().getStringExtra("mUrl");
            isTitleFix = getIntent().getBooleanExtra("isTitleFix", false);
        }
    }

    private void initTitle() {
        //StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0);
        webView = findViewById(R.id.webview_detail);
        mTitleToolBar = findViewById(R.id.title_tool_bar);
        tvGunTitle = findViewById(R.id.tv_gun_title);
        //mProgressBar = findViewById(R.id.pb_progress);
        //mProgressBar.setColor(CommonUtils.getColor(R.color.colorRateRed));
        //mProgressBar.show();

        initToolBar();
    }

    private void initToolBar() {
        //setSupportActionBar(mTitleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //mTitleToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        tvGunTitle.postDelayed(() -> tvGunTitle.setSelected(true), 1900);
        tvGunTitle.setText(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void setTitle(String mTitle) {
        if (!isTitleFix) {
            tvGunTitle.setText(mTitle);
            this.mTitle = mTitle;
        }
    }

    /**
     * 唤起其他app处理
     */
    @Override
    public boolean handleOverrideUrl(String url) {
        return WebViewUtil.handleThirdApp(this, mUrl, url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebViewUtil.initWebView(webView);

        mWebChromeClient = new CusWebClient(this);
        webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        //webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.setOnLongClickListener(v -> handleLongImage());
    }

    @Override
    public void hindProgressBar() {
        //mProgressBar.hide();
    }

    @Override
    public void showWebView() {
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        videoFullView = new FullscreenHolder(WebViewActivity.this);
//        videoFullView.addView(view);
//        decor.addView(videoFullView);
    }

    @Override
    public void showVideoFullView() {
        videoFullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
        videoFullView.setVisibility(View.GONE);
    }

    @Override
    public void startProgress(int newProgress) {
//        mProgressBar.setWebProgress(newProgress);
    }

    @Override
    public void addImageClickListener() {
//        loadImageClickJS();
//        loadTextClickJS();
    }

    private void loadImageClickJS() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");
    }

    private void loadTextClickJS() {
        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        webView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    /**
     * 同步cookie
     */
    private void syncCookie(String url) {
        WebViewUtil.syncCookie(url, this);
    }

    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CusWebClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(intent, resultCode);
        } else if (requestCode == CusWebClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    /**
     * 作为三方浏览器打开
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 长按图片事件处理
     */
    private boolean handleLongImage() {
        boolean consume = WebViewUtil.handleLongImage(webView);
        if (consume) {
            System.out.println("长按图片事件处理");
        }
        return consume;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (mWebChromeClient.inCustomView()) {
                hideCustomView();
                return true;

                //返回网页上一页
            } else if (webView.canGoBack()) {
                webView.goBack();
                return true;

                //退出网页
            } else {
                handleFinish();
            }
        }
        return false;
    }

    /**
     * 直接通过三方浏览器打开时，回退到首页
     */
    public void handleFinish() {
        supportFinishAfterTransition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        webView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    protected void onDestroy() {
        if (videoFullView != null) {
            videoFullView.clearAnimation();
            videoFullView.removeAllViews();
        }
        if (webView != null) {
            WebViewUtil.releaseResource(webView);
            //mProgressBar.reset();
            tvGunTitle.clearAnimation();
            tvGunTitle.clearFocus();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    /**
     * 禁止改变字体大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
