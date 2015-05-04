package com.sinoservices.common.ring.protocol;

import com.sinoservices.common.ring.log.Loger;


//|START 0x40 1 byte|frame length 1 byte|recv/send type 1 byte|indicate type 1 byte |data 1~253 bytes|crc 1 byte|End 0x2A 1 byte|
final public class DataFrame{
	private static String TAG="DataFrame";
	
    static public final byte FRAME_START=0x40;
//    private byte FRAME_LENGTH_SEND=0; //>2 ~ 255
//    private byte FRAME_LENGTH_RECV=0; //>2 ~ 255
//    static private byte FRAME_DIRECTION='N';//R/S
    static private final byte FRAME_TYPE_SEND='S';
    static private final byte FRAME_TYPE_RECV='R';
    private byte FRAME_INDICATE_SEND=0;
    private byte FRAME_INDICATE_RECV=0;
    
    static private final byte FRAME_INDICATE_TYPE_BAR=1;
    static private final byte FRAME_INDICATE_TYPE_KEY=2;
    static private final byte FRAME_INDICATE_TYPE_SCAN=3;
    static private final byte FRAME_INDICATE_TYPE_LED=4;
    
    private byte[] mSendData;
	private byte[] mRecvData;	
    static public final byte FRAME_END=0x2A;
    private boolean mSendValid=false;
    private boolean mRecvValid=false;
    private boolean mIsConfirmType=false;
    
    public boolean isValid(int type){
    	boolean ret=false;
    	 
    	if (0 == type){
    		ret=mSendValid;
    	}else if (1 == type){
    		ret=mRecvValid;
    	}else{
    		Loger.getLogger(TAG).e( "parameter type is error!");
    	}
    		
    	if (!ret){
    		
    	    log();	
    	}
    	
    	return ret;
    }
    
    public boolean codeData(byte[] bytes, int indType){
    	
    	int frameLenght=2;
		byte crc=0;		
		int sum=0;
		int cursor=0;
		
		
		mSendValid=false;
		mSendData = new byte[bytes.length+6];
				
		frameLenght += bytes.length;		
						
		if (indType > 4){
			Loger.getLogger(TAG).e( "indType is error:"+indType);
			return false;
		}
		
		mSendData[cursor++]=FRAME_START;
		mSendData[cursor++]=(byte)(frameLenght&0xff);
		mSendData[cursor++]=FRAME_TYPE_SEND;
		mSendData[cursor++]=(byte)(indType&0xff);
		
		sum += frameLenght;
		sum += FRAME_TYPE_SEND;
		sum += indType;
		for (int i=0; i<bytes.length; i++){
			sum += bytes[i];
			mSendData[cursor++]=bytes[i];
		}	
		
		crc = (byte)(sum&0xFF);
		
		mSendData[cursor++]=crc;
		mSendData[cursor++]=FRAME_END;
		
		mSendValid=true;
		return true;		
    }
    
    public byte[] getSendDataFrame(){
    	return mSendData;
    }
    
    public int decodeData(byte[] bytes,int length){
		int frameLength=0;
		int sum=0;
		int cursor=0;	
		int ret=0;
		byte sendrecv=0;
		
		mRecvValid=false;
		
		if (length<7){
			Loger.getLogger(TAG).e( "frame is so short"+bytes);
			return ret;
		}
		
		mRecvData = new byte[length-6];
		
		//0 check start flag
		if (FRAME_START != bytes[cursor++]){
			Loger.getLogger(TAG).e( "start flag is error:"+bytes);
			return ret;
		}
		
		//1 check frame length
		frameLength = bytes[cursor++]-2;
		if (frameLength < 1 || frameLength>253){
			Loger.getLogger(TAG).e("frameLength is error:"+frameLength);
			return ret;
		}
		
		//2 check recv byte
		sendrecv = bytes[cursor++];
		if (FRAME_TYPE_RECV != sendrecv && FRAME_TYPE_SEND != sendrecv){
			Loger.getLogger(TAG).e("direction is error:"+bytes[cursor-1]);
			return ret;
		}
		
		if (FRAME_TYPE_RECV == sendrecv){
			mIsConfirmType=true;
		}else{
			mIsConfirmType=false;
		}
		
		//3 check recv indicate
		FRAME_INDICATE_RECV=bytes[cursor++];
		if (FRAME_INDICATE_RECV < 1 || FRAME_INDICATE_RECV > 4){
			Loger.getLogger(TAG).e( "direction is error:"+FRAME_INDICATE_RECV);
			return ret;
		}
		
		//4 copy data and check sum
		for (; cursor<frameLength+4; cursor++){
			sum += bytes[cursor]&0xff;
			mRecvData[cursor-4]=bytes[cursor];
		}	
		sum += bytes[1]&0xff;
		sum += bytes[2]&0xff;
		sum += bytes[3]&0xff;
		sum &=0xff;
		
		if (sum != (bytes[cursor++]&0xff)){
			Loger.getLogger(TAG).e( "crc is error,sum:"+sum+",byte:"+bytes[cursor-1]);
			return ret;
		}
		
		if (FRAME_END != bytes[cursor]){
			Loger.getLogger(TAG).e("end flag is error:"+bytes[cursor]);
			return ret;
		}
		
		mRecvValid=true;
		return frameLength;
	}	
    
    public byte[] getRecvData(){
    	return mRecvData;
    }
    
    public int getRecvIndicateType(){
    	return FRAME_INDICATE_RECV;
    }
    
    private void log(){
    	Loger.getLogger(TAG).e( "indicate send:"+FRAME_INDICATE_SEND+",indicate recv:"+FRAME_INDICATE_SEND);
    }
    
}

