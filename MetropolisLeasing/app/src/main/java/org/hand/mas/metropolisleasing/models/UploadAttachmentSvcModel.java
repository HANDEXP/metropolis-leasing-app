package org.hand.mas.metropolisleasing.models;

import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/3/24.
 */
public class UploadAttachmentSvcModel extends AsHttpRequestModel {
    public UploadAttachmentSvcModel(LMModelDelegate delegate) {
        super(delegate);
    }

    public void upload(HashMap param,byte[] myBytes,String fileName){
        try {
            String url = ConstantUrl.uploadAttachmentUrl;
            uploadBytes(url,param,myBytes,fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
