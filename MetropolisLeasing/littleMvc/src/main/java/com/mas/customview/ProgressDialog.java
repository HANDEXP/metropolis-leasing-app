package com.mas.customview;

import com.littlemvc.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

public class ProgressDialog extends Dialog{

	public ProgressDialog(Context context, String strMessage){
		this(context, R.style.CustomDialog, strMessage);
		
	}
	
	public ProgressDialog(Context context, int theme,String strMessage){
		super(context, theme); 
		  this.setContentView(R.layout.customview_progressdialog);  
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);  
        if (tvMsg != null) {  
            tvMsg.setText(strMessage);  
        }  
    }  
}
