package com.sinoservices.common.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinoservices.common.R;
import com.sinoservices.common.activity.ModuleWebActivity;
import com.sinoservices.common.entity.ModuleEntity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @ClassName: ApppCommonFragment
 * @Description: 应用页面
 * @author Felix
 * @date 2015年4月27日 上午9:00:00
 *
 */
public class AppCommonFragment extends Fragment {
	
	View view;
	GridView gv;
	
	List<ModuleEntity> allEntities = new ArrayList<ModuleEntity>();
	List<ModuleEntity> displayEntities = new ArrayList<ModuleEntity>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_app_common, container, false);
		
		// 百度推送模块实体
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
		
		// 支付宝支付模块实体
		ModuleEntity aliPayModule = new ModuleEntity();
		aliPayModule.setModulename("支付宝支付");
		aliPayModule.setModuleurl("http://sinoserver.duapp.com/alipay.html");
		aliPayModule.setModulestatus("true");
		aliPayModule.setModuleid(R.drawable.alipay_icon);
		
		// 增加模块实体
		ModuleEntity addModule = new ModuleEntity();
		addModule.setModulename("添加应用");
		addModule.setModuleurl("local");
		addModule.setModulestatus("true");
		addModule.setModuleid(R.drawable.item_add);
		
		allEntities.add(baiduPushModule);
		allEntities.add(wxPayModule);
		allEntities.add(aliPayModule);
		allEntities.add(addModule);
		
		//准备要添加的数据条目 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < allEntities.size(); i++) { 
        	if ("true".equals(allEntities.get(i).getModulestatus())) {
        		displayEntities.add(allEntities.get(i));
				Map<String, Object> item = new HashMap<String, Object>(); 
				item.put("imageItem", allEntities.get(i).getModuleid());	//添加图像资源的ID   
				item.put("textItem", allEntities.get(i).getModulename());//按序号添加ItemText   
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
        gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(getActivity(), ""+position, 0).show();
				Intent intent=new Intent(getActivity(), ModuleWebActivity.class);
//				Intent intent=new Intent(getActivity(), PayDemoActivity.class);
				intent.putExtra("ModuleEntity", displayEntities.get(position));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});		
		return view;
	}
	
}
