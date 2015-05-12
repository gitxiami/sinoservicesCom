package com.sinoservices.common.ring.global;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public final class ToastUtils {

	/**
	 *  Show some message for user.<br>
	 *  
	 *  we guarantee that It is shown one time in its duration .<br>
	 *  
	 *  If the message by msgResId  do not exist , we do not show anything .<br>
	 * 
	 * @param context
	 * @param msgResId
	 * @param durationType How long to display the message.  Either LENGTH_SHORT or  LENGTH_LONG
	 */
	public final static void makeText(Context context, int msgResId, final int durationType){
			makeText(context, context.getApplicationContext().getResources().getString(msgResId), durationType);
	}
	
	/**
	 * Show some message for user.<br>
	 * 
	 * we guarantee that It is shown one time in its duration .<br>
	 * 
	 *  If the message is empty ( null or empty string) , we do not show anything .<br>
	 * 
	 * @param context
	 * @param message
	 * @param duration How long to display the message.  Either LENGTH_SHORT or  LENGTH_LONG
	 */
	public  final static  void makeText(Context context, String message, final int durationType){
		
		
		// make the duration 
		long duration;
		if(durationType == Toast.LENGTH_LONG){
			duration = 3500;// this is a  default long time in framework 
		}else{
			duration = 2000;// this is a  default short  time in framework 
		}
		
		// whether or not show 
		if(isLastMessage(message)  ){
			long now = System.currentTimeMillis();
			long fff = (now-lastTime);
			if(fff>duration){
			   Toast toast = Toast.makeText(context, message, durationType);
			   toast.setGravity(Gravity.CENTER, 30, 30); 
			   toast.show();
				lastTime = now;
			}/*else{
			}*/
		}else{
			Toast toast = Toast.makeText(context, message, durationType);
			toast.setGravity(Gravity.CENTER, 30, 30); 
			toast.show();
			lastTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * Whether or not current message is the same with the last message .<br>
	 * 
	 * If not , we will set the current message as new message , and put into SoftReference
	 * 
	 * 
	 * @param lastMessage It must be not null . we have fliter it .
	 * @return
	 */
	private final static boolean isLastMessage(String lastMessage){
		boolean isSame = false;
		if(softRef_LastMessage==null || softRef_LastMessage.get()==null){
			// do nothing. the result has been "false"
		}else{
			isSame = softRef_LastMessage.get().equalsIgnoreCase(lastMessage) ;
		}
		
		if(!isSame){
			softRef_LastMessage = new SoftReference<String>(lastMessage);
		}
		
		return isSame;
	}
	
	// the last time for show 
	private static long lastTime ;
	
	// Maybe , the softReference is not meanigful
	private static SoftReference<String> softRef_LastMessage  ;

	// we only need no any instance
	private ToastUtils() {
		// TODO Auto-generated constructor stub
	}

}
