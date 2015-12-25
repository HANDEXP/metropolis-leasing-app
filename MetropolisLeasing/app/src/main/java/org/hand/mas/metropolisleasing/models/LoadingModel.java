package org.hand.mas.metropolisleasing.models;

import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUtl;

/**
 * Created by gonglixuan on 15/4/1.
 */
public class LoadingModel extends AsHttpRequestModel{
    public LoadingModel(LMModelDelegate delegate) {
        super(delegate);
    }
    public void load(){
        this.post(ConstantUtl.configFile,null);
    }
}
