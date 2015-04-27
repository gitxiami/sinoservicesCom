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
 * @Description: 应用页面
 * @author Felix
 * @date 2015年4月27日 上午9:00:00
 *
 */
public class AppCommonFragment extends Fragment {
	
	View view;
	GridView gv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_app_common, container, false);
		
	    //图片ID数组 
	    int[] images = new int[]{        
	            R.drawable.item_hr_self, R.drawable.item_todo, R.drawable.item_document,  
	            R.drawable.item_schedule, R.drawable.item_email, R.drawable.item_it5000,  
	            R.drawable.item_business, R.drawable.item_mall,R.drawable.item_add  
	    }; 
	    
	    //图片的文字标题 
	    String[] titles = new String[] 
	    { "HR自助", "待办", "公文", "日程", "邮件", "IT5000", "外事商务", "员工商城", ""}; 
	   
		//准备要添加的数据条目 
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
        for (int i = 0; i < 9; i++) { 
          Map<String, Object> item = new HashMap<String, Object>(); 
          item.put("imageItem", images[i]);//添加图像资源的ID   
          item.put("textItem", titles[i]);//按序号添加ItemText   
          items.add(item); 
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
