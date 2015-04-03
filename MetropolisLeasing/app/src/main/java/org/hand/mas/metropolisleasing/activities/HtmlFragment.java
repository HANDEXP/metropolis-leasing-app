package org.hand.mas.metropolisleasing.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.littlemvc.utl.AsNetWorkUtl;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/4/3.
 */
public class HtmlFragment extends Fragment {
    private  WebView contentWebView;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_html_fragment, container,false);
        bindAllViews(root);
        return root;
    }

    private void bindAllViews(View root) {
        contentWebView = (WebView) root.findViewById(R.id.activity_html_base_webview);
        contentWebView.setWebChromeClient(new AlertWebChromeClient());
        contentWebView.setWebViewClient(new ContentWebClient());
        WebSettings webSettings = contentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getArguments().getString("url");
        String title =  getArguments().getString("title");
        load(url);
    }

    /**
     * @param url
     */
    protected void load(String url) {
        //System.out.println(AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", "")));
        String  _url   = AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", ""));

        contentWebView.loadUrl(AsNetWorkUtl.getAsNetWorkUtl(null).getAbsoluteUrl(url.replace("${base_url}", "")));
    }

    private class AlertWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
        {
            new AlertDialog.Builder(HtmlFragment.this.getActivity())
                    .setTitle("")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.confirm();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();

            return true;
        };

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(HtmlFragment.this.getActivity())
                    .setTitle("")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.cancel();
                                }
                            })
                    .create()
                    .show();

            return true;
        }

    }


    private class ContentWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



            Uri query_string = Uri.parse(url);
            String query_scheme = query_string.getScheme();
            String query_host = query_string.getHost();

            if ((query_scheme.equalsIgnoreCase("https") || query_scheme.equalsIgnoreCase("http")) && query_host != null
                    && query_string.getQueryParameter("new_window") == null) {
                return false;// handle the load by webview
            }

            if (query_scheme.equalsIgnoreCase("tel")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, query_string);
                startActivity(intent);
                return true;
            }

            if (query_scheme.equalsIgnoreCase("mailto")) {

                android.net.MailTo mailTo = android.net.MailTo.parse(url);

                // Create a new Intent to send messages
                // 系统邮件系统的动作为Android.content.Intent.ACTION_SEND

                Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                sendIntent.setType("plain/text");

                // 设置邮件默认地址
                sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { mailTo.getTo() });
                // 调用系统的邮件系统
                try {
                    startActivity(sendIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HtmlFragment.this.getActivity(), "没有找到邮件客户端", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }


        @Override
        public void onPageFinished	(WebView view, String url) {
            super.onPageFinished(view, url);
            //每次网络请求初始化 timer;

        }
    }
}
