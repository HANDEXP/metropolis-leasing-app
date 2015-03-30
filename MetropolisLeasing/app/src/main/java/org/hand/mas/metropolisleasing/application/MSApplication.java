package org.hand.mas.metropolisleasing.application;

import android.app.Activity;
import android.app.Application;

import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;

import org.hand.mas.utl.ConstantUrl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class MSApplication extends Application{
    private static MSApplication application;
    private List<Activity> mList = new LinkedList();

    public static MSApplication getApplication(){
        if (application == null){
            application = new MSApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        AsNetWorkUtl.application = this;
        AsHttpRequestModel.utl = AsNetWorkUtl.getAsNetWorkUtl(ConstantUrl.basicUrl);

    }

    public void addActivity(Activity activity){
        mList.add(activity);
    }

    public void exit(){
        for (Activity activity:mList){
            activity.finish();
        }
        System.exit(0);
    }
}
