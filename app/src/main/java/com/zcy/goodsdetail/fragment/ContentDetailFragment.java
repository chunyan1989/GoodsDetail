package com.zcy.goodsdetail.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zcy.goodsdetail.R;


/**
 * 商品详情界面
 * A simple {@link Fragment} subclass.
 */
public class ContentDetailFragment extends Fragment {
    private WebView mWebView;
    private String mUrl = "http://www.baidu.com/";


    public ContentDetailFragment() {
        // Required empty public constructor
    }


    private static ContentDetailFragment fragment = null;

    public static ContentDetailFragment newInstance() {
        if (fragment == null) {
            fragment = new ContentDetailFragment();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mWebView = (WebView)view.findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);// 设置可以使用localStorage
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.loadUrl(mUrl);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String scheme = url.substring(0, 4).trim().toLowerCase();
                if (scheme.contains("https") || scheme.contains("http")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();
                if (title == null) {
                    title = "";
                }
                super.onPageFinished(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            // 加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 0) {
                } else if (newProgress == 100) {
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 设置标题
                if (title.length() > 16) {
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
