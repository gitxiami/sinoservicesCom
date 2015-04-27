package com.sinoservices.common.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinoservices.common.R;
import com.sinoservices.common.entity.ModuleEntity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * @ClassName: ApppFragment
 * @Description: 应用页面
 * @author Felix
 * @date 2015年4月27日 上午9:00:00
 *
 */
public class AppCommonFragment extends Fragment {
	
	View view;
	GridView gv;
	
	List<ModuleEntity> moduleEntities = new ArrayList<ModuleEntity>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_app_common, container, false);
		
		// 百度推模块实体
		ModuleEntity baiduPushModule = new ModuleEntity();
		baiduPushModule.setModulename("百度推送");
		baiduPushModule.setModuleurl("http://commonserver.duapp.com/push.jsp");
		baiduPushModule.setModulestatus("true");
		baiduPushModule.setModuleid(R.drawable.baidu_push_icon);
		
		// 微信支付模块实体
		ModuleEntity wxPayModule = new ModuleEntity();
		wxPayModule.setModulename("微信支付");
		wxPayModule.setModuleurl("http://commonserver.duapp.com/wxpay.jsp");
		wxPayModule.setModulestatus("false");
		wxPayModule.setModuleid(R.drawable.wxpay_icon);
		
		// 增加模块实体
		ModuleEntity addModule = new ModuleEntity();
		addModule.setModulename("");
		addModule.setModuleurl(null);
		addModule.setModulestatus("false");
		addModule.setModuleid(R.drawable.item_add);
		
		moduleEntities.add(baiduPushModule);
		moduleEntities.add(wxPayModule);
		moduleEntities.add(addModule);
		
/*	    //图片ID数组 
	    int[] images = new int[]{        
	            R.drawable.baidu_push_icon, R.drawable.wxpay_icon, R.drawable.item_add
	    }; 
	    
	    //图片的文字标题 
	    String[] titles = new String[] 
	    { "百度推送", "微信支付", ""}; 
*/	   
		//准备要添加的数据条目 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < moduleEntities.size(); i++) { 
        	if ("true".equals(moduleEntities.get(i).getModulestatus())) {
				Map<String, Object> item = new HashMap<String, Object>(); 
				item.put("imageItem", moduleEntities.get(i).getModuleid());	//添加图像资源的ID   
				item.put("textItem", moduleEntities.get(i).getModulename());//按序号添加ItemText   
				items.add(item); 
			}
        } 

       //实例化一个适配器 
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),  
                                                    items,  
                                                    R.layout.grid_item,  
                                                    new String[]{"imageItem", "textItem"},  
                                                    new int[]{R.id.image_item, R.id.text_item}); 

        //获得GridView实例 
        gv = (GridView)view.findViewById(R.id.gv_app); 
        //为GridView设置适配器 
        gv.setAdapter(adapter);		
				
		return view;
	}
	
}
