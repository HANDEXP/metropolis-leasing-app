package org.hand.mas.metropolisleasing.models;

import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/4/3.
 */
public class FunctionListSvcModel extends AsHttpRequestModel {



    public FunctionListSvcModel(LMModelDelegate delegate) {
        super(delegate);
    }

    public void load(HashMap param){
        try {
            String url = "modules/app/function_query.svc";
            this.post(url,param);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
