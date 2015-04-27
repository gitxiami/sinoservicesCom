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
 * @Description: Ӧ��ҳ��
 * @author Felix
 * @date 2015��4��27�� ����9:00:00
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
		
		// �ٶ���ģ��ʵ��
		ModuleEntity baiduPushModule = new ModuleEntity();
		baiduPushModule.setModulename("�ٶ�����");
		baiduPushModule.setModuleurl("http://commonserver.duapp.com/push.jsp");
		baiduPushModule.setModulestatus("true");
		baiduPushModule.setModuleid(R.drawable.baidu_push_icon);
		
		// ΢��֧��ģ��ʵ��
		ModuleEntity wxPayModule = new ModuleEntity();
		wxPayModule.setModulename("΢��֧��");
		wxPayModule.setModuleurl("http://commonserver.duapp.com/wxpay.jsp");
		wxPayModule.setModulestatus("false");
		wxPayModule.setModuleid(R.drawable.wxpay_icon);
		
		// ����ģ��ʵ��
		ModuleEntity addModule = new ModuleEntity();
		addModule.setModulename("");
		addModule.setModuleurl(null);
		addModule.setModulestatus("false");
		addModule.setModuleid(R.drawable.item_add);
		
		moduleEntities.add(baiduPushModule);
		moduleEntities.add(wxPayModule);
		moduleEntities.add(addModule);
		
/*	    //ͼƬID���� 
	    int[] images = new int[]{        
	            R.drawable.baidu_push_icon, R.drawable.wxpay_icon, R.drawable.item_add
	    }; 
	    
	    //ͼƬ�����ֱ��� 
	    String[] titles = new String[] 
	    { "�ٶ�����", "΢��֧��", ""}; 
*/	   
		//׼��Ҫ��ӵ�������Ŀ 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < moduleEntities.size(); i++) { 
        	if ("true".equals(moduleEntities.get(i).getModulestatus())) {
				Map<String, Object> item = new HashMap<String, Object>(); 
				item.put("imageItem", moduleEntities.get(i).getModuleid());	//���ͼ����Դ��ID   
				item.put("textItem", moduleEntities.get(i).getModulename());//��������ItemText   
				items.add(item); 
			}
        } 

       //ʵ����һ�������� 
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),  
                                                    items,  
                                                    R.layout.grid_item,  
                                                    new String[]{"imageItem", "textItem"},  
                                                    new int[]{R.id.image_item, R.id.text_item}); 

        //���GridViewʵ�� 
        gv = (GridView)view.findViewById(R.id.gv_app); 
        //ΪGridView���������� 
        gv.setAdapter(adapter);		
				
		return view;
	}
	
}
