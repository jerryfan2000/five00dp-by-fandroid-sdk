package com.nyuen.five00dp.fragment;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.nyuen.five00dp.R;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;

public class WebDialogFragment extends SherlockDialogFragment {

    private WebView mWebView;
    private TextView mUrlView;
    private ProgressBar mProgressBar;
    private ImageButton mSwitchAgent;
    private String mUrl;
    private boolean mIsMobile = true;

    public WebDialogFragment(String url) {
        mUrl = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_web, container, false);

        mUrlView = (TextView) view.findViewById(R.id.tvUrl);
        mUrlView.setText(mUrl);
        mUrlView.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(mUrl));
                startActivity(intent);
            }
        });


        mWebView = (WebView) view.findViewById(R.id.webViewDialog); 
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) 
            {
                if(progress < 100 && mProgressBar.getVisibility() == ProgressBar.INVISIBLE){
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
                mProgressBar.setProgress(progress);
                if(progress == 100) {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }           
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mUrlView.setText(url);
                mUrl = url;
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        
        mProgressBar = (ProgressBar) view.findViewById(R.id.pbWeb);

        mSwitchAgent = (ImageButton) view.findViewById(R.id.btnSwitchAgent);
        mSwitchAgent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsMobile) {
                    mIsMobile = false;
                    ((ImageButton) v).setImageResource(R.drawable.ic_plus);
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19");
                } else {
                    mIsMobile = true;
                    ((ImageButton) v).setImageResource(R.drawable.ic_awesome);
                    mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.3.3; en-gb; Nexus S Build/GRI20) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                }
                mWebView.loadUrl(mUrl);    
            }
        });
        
        mWebView.loadUrl(mUrl);

        return view;
    }
}
