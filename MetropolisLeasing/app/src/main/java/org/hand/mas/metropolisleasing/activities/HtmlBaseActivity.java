package org.hand.mas.metropolisleasing.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by gonglixuan on 15/4/2.
 */
public class HtmlBaseActivity extends ActionBarActivity {

    private WebView contentWebView;




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
                    Toast.makeText(HtmlBaseActivity.this, "没有找到邮件客户端", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //每次网络请求初始化 timer;

        }
    }
}
