package com.example.common.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * webView util
 */
public class WebViewUtil {

    private Handler handler=new Handler();
    /**
     * @param handler the handler to set
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public static void releaseResource(WebView webView) {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(WebView webView) {
        if (webView != null) {
            WebSettings ws = webView.getSettings();
            // 网页内容的宽度适配
            ws.setLoadWithOverviewMode(true);
            ws.setUseWideViewPort(true);
            // 保存表单数据
            ws.setSaveFormData(true);
            // 是否应该支持使用其屏幕缩放控件和手势缩放
            ws.setSupportZoom(true);
            ws.setBuiltInZoomControls(true);
            ws.setDisplayZoomControls(false);
            // 启动应用缓存
            ws.setAppCacheEnabled(true);
            // 设置缓存模式
            ws.setCacheMode(WebSettings.LOAD_DEFAULT);
            // 告诉WebView启用JavaScript执行。默认的是false。
            ws.setJavaScriptEnabled(true);
            //  页面加载好以后，再放开图片
            ws.setBlockNetworkImage(false);
            // 使用localStorage则必须打开
            ws.setDomStorageEnabled(true);
            // 排版适应屏幕
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            // WebView是否新窗口打开(加了后可能打不开网页)
            // ws.setSupportMultipleWindows(true);
            // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)
            ws.setTextZoom(100);
        }
    }

    //长按webView 如果是图片的话 可以弹框保存 需要设置webView.setLong
    public static boolean handleLongImage(WebView webView) {
        final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        return hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;
    }

    //同步cookie
    public static void syncCookie(String url, Context context) {
        if (!TextUtils.isEmpty(url) && url.contains("wanandroid")) {
            try {
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                String cookie = "你的本地cookie";
                if (!TextUtils.isEmpty(cookie)) {
                    String[] split = cookie.split(";");
                    for (int i = 0; i < split.length; i++) {
                        cookieManager.setCookie(url, split[i]);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                } else {
                    CookieSyncManager.getInstance().sync();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean handleThirdApp(Activity activity, String originalUrl, String backUrl) {
        //http开头直接跳过
        if (backUrl.startsWith("http")) {
            // 可能有提示下载Apk文件
            if (backUrl.contains(".apk")) {
                startActivity(activity, backUrl);
                return true;
            }
            return false;
        }

        boolean isJump = true;
        if (!TextUtils.isEmpty(originalUrl)) {
            if (backUrl.startsWith("openapp.jdmobile:")// 京东
                    || backUrl.startsWith("zhihu:")// 知乎
                    || backUrl.startsWith("tbopen:")// 淘宝
                    || backUrl.startsWith("kaola:")// 考拉
                    || backUrl.startsWith("vipshop:")//
                    || backUrl.startsWith("youku:")//优酷
                    || backUrl.startsWith("uclink:")// UC
                    || backUrl.startsWith("ucbrowser:")// UC
                    || backUrl.startsWith("alipay:")// 支付宝
                    || backUrl.startsWith("newsapp:")//
                    || backUrl.startsWith("sinaweibo:")// 新浪
                    || backUrl.startsWith("suning:")//
                    || backUrl.startsWith("pinduoduo:")// 拼多多
                    || backUrl.startsWith("jdmobile:")//京东
                    || backUrl.startsWith("baiduboxapp:")// 百度
                    || backUrl.startsWith("alipays:")//支付宝
                    || backUrl.startsWith("qtt:")//
                    || backUrl.startsWith("qqnews:")// 腾讯新闻
            ) {
                isJump = false;
            }
        }
        if (isJump) {
            startActivity(activity, backUrl);
        }
        return isJump;
    }

    private static void startActivity(Activity activity, String url) {
        try {
            Intent intent1 = new Intent();
            intent1.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent1.setData(uri);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调JS方法
     *
     * @param method
     * @param argsJsonStr
     */
    public void callJS(WebView webView ,String method,String argsJsonStr) {
        final String resp = "javascript:" + method + "('" + argsJsonStr + "')";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { //4.4以后用evaluateJavascript请求JS方法
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        try {
                            webView.loadUrl(resp);
                        } catch (Exception e) {
                        }
                    }
                }
            });
        } else {
            if (webView != null) {
                try {
                    webView.evaluateJavascript(resp, new ValueCallback<String>() { //4.4以后专门用来异步执行JavaScript代码的
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the handler
     */
    public Handler getHandler() {
        return handler;
    }
}
