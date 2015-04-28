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
 * @Description: Ӧ��ҳ��
 * @author Felix
 * @date 2015��4��27�� ����9:00:00
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
		
		// �ٶ�����ģ��ʵ��
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
		
		// ֧����֧��ģ��ʵ��
		ModuleEntity aliPayModule = new ModuleEntity();
		aliPayModule.setModulename("֧����֧��");
		aliPayModule.setModuleurl("http://sinoserver.duapp.com/alipay.html");
		aliPayModule.setModulestatus("true");
		aliPayModule.setModuleid(R.drawable.alipay_icon);
		
		// ����ģ��ʵ��
		ModuleEntity addModule = new ModuleEntity();
		addModule.setModulename("���Ӧ��");
		addModule.setModuleurl("local");
		addModule.setModulestatus("true");
		addModule.setModuleid(R.drawable.item_add);
		
		allEntities.add(baiduPushModule);
		allEntities.add(wxPayModule);
		allEntities.add(aliPayModule);
		allEntities.add(addModule);
		
		//׼��Ҫ��ӵ�������Ŀ 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < allEntities.size(); i++) { 
        	if ("true".equals(allEntities.get(i).getModulestatus())) {
        		displayEntities.add(allEntities.get(i));
				Map<String, Object> item = new HashMap<String, Object>(); 
				item.put("imageItem", allEntities.get(i).getModuleid());	//���ͼ����Դ��ID   
				item.put("textItem", allEntities.get(i).getModulename());//��������ItemText   
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
