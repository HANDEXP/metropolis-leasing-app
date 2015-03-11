package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.test.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;


public class MainActivity extends Activity {

    @Autowired
    private URL serviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
//        RoboSpring.autowire(this);
//        Resource a =   new FileSystemResource("/data/data/org.hand.mas.metropolisleasing/untitled.xml");
//        XmlBeanFactory factory = new XmlBeanFactory(a);
//
//        Person one = new Person();
//        try {
//            Method method = factory.getBean("kelly").getClass().getMethod("getHello",null);
//            String test = String.valueOf(method.invoke(one));
//            Log.d("Hello",test);
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        Log.d("TEST", factory.getBean("kelly").toString());
//        Log.d("ServiceUrl",serviceUrl.toString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
