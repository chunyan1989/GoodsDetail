package com.zcy.goodsdetail.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zcy.goodsdetail.R;
import com.zcy.goodsdetail.widget.PageTwoWebView;
import com.zcy.goodsdetail.widget.SlidingDetailsLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 商品界面
 * A simple {@link Fragment} subclass.
 */
public class GoodsDetailFragment extends Fragment {
    private static final String TAG = "GoodsDetailFragment";
    @BindView(R.id.slidingDetailsLayout)
    SlidingDetailsLayout slidingDetailsLayout;
    @BindView(R.id.tishi)
    TextView tishi;
    @BindView(R.id.webview)
    PageTwoWebView webview;

    private String mUrl = "http://www.baidu.com/";


    private static GoodsDetailFragment fragment = null;

    public static GoodsDetailFragment newInstance() {
        if (fragment == null) {
            fragment = new GoodsDetailFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        ButterKnife.bind(this, view);
        initView();
        initWebView();
        return view;
    }

    private void initWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);// 设置可以使用localStorage
        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.loadUrl(mUrl);
        webview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //这句话说的意思告诉父View我自己的事件我自己处理
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {

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
                super.onPageFinished(view, url);
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                webview.measure(w, h);
            }
        });
    }

    private void initView() {
        tishi.setText("上拉查看商品详情");
        slidingDetailsLayout.setPositionChangListener(new SlidingDetailsLayout.PositionChangListener() {
            @Override
            public void position(int positon) {
                if (positon == 0) {
                    tishi.setText("上拉查看商品详情");
                } else {
                    tishi.setText("下拉返回商品简介");

                }
            }

            @Override
            public void onBottom() {
                tishi.setText("松开，马上加载商品详情");
            }

            @Override
            public void backBottom() {
                tishi.setText("上拉查看商品详情");
            }

            @Override
            public void onTop() {
                tishi.setText("松开，返回商品简介");
            }

            @Override
            public void backTop() {
                tishi.setText("下拉返回商品简介");
            }
        });
    }


}
