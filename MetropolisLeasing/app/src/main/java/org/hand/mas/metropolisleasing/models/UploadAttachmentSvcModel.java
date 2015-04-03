package org.hand.mas.metropolisleasing.models;

import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/3/24.
 */
public class UploadAttachmentSvcModel extends AsHttpRequestModel {

    private XmlConfigReader configReader;

    public UploadAttachmentSvcModel(LMModelDelegate delegate) {

        super(delegate);
        configReader = XmlConfigReader.getInstance();
    }

    public void upload(HashMap param,byte[] myBytes,String fileName){
        try {
            String url = configReader
                    .getAttr(new Expression(
                            "/backend-config/url[@name='uploadAttachment']",
                            "value"));
            uploadBytes(url,param,myBytes,fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
