package org.hand.mas.metropolisleasing.models;

import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class CddGridSvcModel extends AsHttpRequestModel {

    private XmlConfigReader configReader;

    public CddGridSvcModel(LMModelDelegate delegate) {
        super(delegate);
        configReader = XmlConfigReader.getInstance();
    }

    public void load(HashMap param){
        try {
            String url = configReader
                    .getAttr(new Expression(
                            "/backend-config/url[@name='image_url']",
                            "value"));
            this.post(url,param);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
