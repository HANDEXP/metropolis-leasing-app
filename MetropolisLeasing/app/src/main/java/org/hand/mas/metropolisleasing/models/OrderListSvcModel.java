package org.hand.mas.metropolisleasing.models;

import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.utl.ConstantUrl;

import java.util.HashMap;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class OrderListSvcModel extends AsHttpRequestModel {
    public OrderListSvcModel(LMModelDelegate delegate) {
        super(delegate);
    }

    public void load() {
        try {
            String url = ConstantUrl.orderListUrl;
            this.post(url,null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(HashMap param) {
        try {
            String url = ConstantUrl.orderListUrl;
            this.post(url,param);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
