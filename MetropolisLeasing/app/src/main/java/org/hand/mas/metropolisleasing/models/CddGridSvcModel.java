package org.hand.mas.metropolisleasing.models;

import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class CddGridSvcModel extends AsHttpRequestModel {

    public CddGridSvcModel(LMModelDelegate delegate) {
        super(delegate);
    }

    public void load(HashMap param){
        try {
            String url = ConstantUrl.cddGridUrl;
            this.post(url,param);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
