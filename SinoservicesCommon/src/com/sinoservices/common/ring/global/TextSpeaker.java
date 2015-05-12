package com.sinoservices.common.ring.global;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class TextSpeaker {

	private TextToSpeech tts;
    private Context context;
    public TextSpeaker(final Context context) {
    	this.context=context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                    	ToastUtils.makeText(context, "该设备智能语音没开放.",Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }
     
    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    public void speak(int resId){
    	
    	 tts.speak(this.context.getString(resId), TextToSpeech.QUEUE_FLUSH, null);
    }

}
