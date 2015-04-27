package com.sinoservices.common.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinoservices.common.R;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_app_common, container, false);
		
	    //ͼƬID���� 
	    int[] images = new int[]{        
	            R.drawable.item_hr_self, R.drawable.item_todo, R.drawable.item_document,  
	            R.drawable.item_schedule, R.drawable.item_email, R.drawable.item_it5000,  
	            R.drawable.item_business, R.drawable.item_mall,R.drawable.item_add  
	    }; 
	    
	    //ͼƬ�����ֱ��� 
	    String[] titles = new String[] 
	    { "HR����", "����", "����", "�ճ�", "�ʼ�", "IT5000", "��������", "Ա���̳�", ""}; 
	   
		//׼��Ҫ��ӵ�������Ŀ 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < 9; i++) { 
          Map<String, Object> item = new HashMap<String, Object>(); 
          item.put("imageItem", images[i]);//���ͼ����Դ��ID   
          item.put("textItem", titles[i]);//��������ItemText   
          items.add(item); 
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
