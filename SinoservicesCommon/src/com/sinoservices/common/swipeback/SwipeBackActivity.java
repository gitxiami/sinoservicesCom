
package com.sinoservices.common.swipeback;


import com.sinoservices.common.activity.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
/**
 * @ClassName: SwipeBackActivity 
 * @Description: TODO
 * @author Jerry 
 *
 */
public class SwipeBackActivity extends BaseActivity implements SwipeBackActivityBase{
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }
    
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
