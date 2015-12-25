package org.hand.mas.custom_view;

import android.view.View;

/**
 * Created by gonglixuan on 15/5/20.
 */
public interface OnProgressingListener {
    /**
     * 开始时触发事件
     */
    void onStart(View view);

    /**
     * 过程中
     */
    void onProgress(View view);

    /**
     * 结束时触发事件
     */
    void onComplete(View view);
}
