package org.hand.mas.metropolisleasing.models;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/4/1.
 */
public class RemoteModel extends AsHttpRequestModel {

    private XmlConfigReader configReader;

    public RemoteModel(LMModelDelegate delegate) {
        super(delegate);
        configReader = XmlConfigReader.getInstance();
        this.modelDelegate = delegate;
    }

    public void load(HashMap parm){
        try {
            String queryUrl = configReader.getAttr(new Expression(
                    "/backend-config/url[@name='synchronization_url']", "value"));

            this.post(queryUrl, parm);

        } catch (ParseExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
