package com.example.room;

import java.util.Arrays;

public class Control {
	static CommonData[] pos = new CommonData[50];
	
	final static float[] startX = {190,2,567,755};
	final static float[] startY = {755,191,2,566};
	
	final static float[] beginRedYellowX = {26,120};
	final static float[] beginBlueGreenX = {638,732};
	final static float[] beginRedGreenY = {636,731};
	final static float[] beginYellowBlueY = {24,118};
	
	final static float pathRedBlueX = 378;
	final static float[] pathRedGreenXY = {661,613,567,519,473,425};
	
	final static float pathYelowGreenY = 377;
	final static float[] pathYellowBlueXY = {96,143,191,238,284,331};
	
	final static float[] coordinateX = {236,212,212,237,190,141,94,48,24,24,
			24,24,24,47,95,142,190,236,213,213,
			237,284,331,377,424,472,519,542,542,518,
			566,613,660,707,730,730,730,730,730,707,
			659,612,566,519,542,542,519,472,425,378,
			331,283};
	final static float[] coordinateY = {708,660,614,567,520,543,543,520,473,426,
			378,332,284,237,214,214,237,190,143,96,
			49,25,25,25,25,25,49,96,143,190,
			237,214,214,237,284,331,379,425,472,519,
			542,542,519,566,613,661,708,731,731,731,
			731,731};
	
	static int turn = 1;
	static int diceOrNot = 0;
	static int len = 0;
	
	static public float getStartX(int type, int num){
		switch(type){
			case 1:
			case 2:
				return beginRedYellowX[1-num%2];
			case 3:
			case 4:
				return beginBlueGreenX[1-num%2];
		}
		return 0;
	}
	
	static public float getStartY(int type, int num){
		switch(type){
			case 1:
			case 4:
				return beginRedGreenY[(num-1)/2];
			case 2:
			case 3:
				return beginYellowBlueY[(num-1)/2];
		}
		return 0;
		
	}
	
	static public float getFirstStepX(int type){
		return startX[type-1];
	}
	
	static public float getFirstStepY(int type){
		return startY[type-1];
	}
	
	public static float[] concatAll(float[] first, float[] ... rest) {  
		int totalLength = first.length;  
		for (float[] array : rest) {  
			totalLength += array.length;  
		}  
		float[] result = Arrays.copyOf(first, totalLength);  
		int offset = first.length;  
		for (float[] array : rest) {  
		   System.arraycopy(array, 0, result, offset, array.length);  
		   offset += array.length;  
		}  
		return result;  
	} 

	static public float[] getCutXRed(int current, int len){
		float[] tempX;
		if(current == 0){
			tempX = new float[1];
			tempX[0] = getFirstStepX(1);
			return concatAll(tempX, Arrays.copyOfRange(coordinateX, 0, len));
		}
		if(current + len <= 50){
			return Arrays.copyOfRange(coordinateX, current-1, current+len);
		}
		else if(current > 50 && current + len <= 56){
			tempX = new float[len+1];
			Arrays.fill(tempX,0, len+1, pathRedBlueX); 
			return tempX;
		}
		else{
			tempX = new float[current+len-50];
			Arrays.fill(tempX,0, current+len-50, pathRedBlueX);
			return concatAll(Arrays.copyOfRange(coordinateX, current-1, 50),tempX);
		}
	}
	
	static public float[] getCutYRed(int current, int len){
		float[] tempY;
		if(current == 0){
			tempY = new float[1];
			tempY[0] = getFirstStepY(1);
			return concatAll(tempY, Arrays.copyOfRange(coordinateY, 0, len));
		}
		if(current + len <= 50){
			return Arrays.copyOfRange(coordinateY, current-1, current+len);
		}
		else if(current > 50 && current + len <= 56){
			return Arrays.copyOfRange(pathRedGreenXY, current-51, current+len-50);
		}
		else{
			tempY = Arrays.copyOfRange(pathRedGreenXY, 0, current+len-50);			
			return concatAll( Arrays.copyOfRange(coordinateY, current-1, 50), tempY);
		}
	}
	
	
	static public float[] getCutXYellow(int current, int len){
		float[] tempX;
		int offset = 13;
		if(current == 0){
			tempX = new float[1];
			tempX[0] = getFirstStepX(2);
			return concatAll(tempX, Arrays.copyOfRange(coordinateX, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 39){
				return Arrays.copyOfRange(coordinateX, current-1+offset, current+len+offset);
			}
			else if(current > 39){
				return Arrays.copyOfRange(coordinateX, current-40, current-40+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateX, current-1+offset, 52),
						Arrays.copyOfRange(coordinateX, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			return Arrays.copyOfRange(pathYellowBlueXY, current-51, current+len-50);
		}
		else{
			tempX = Arrays.copyOfRange(pathYellowBlueXY, 0, current+len-50);
			return concatAll(Arrays.copyOfRange(coordinateX, current-40, 11),tempX);
			
		}
	}
	
	static public float[] getCutYYellow(int current, int len){
		float[] tempY;
		int offset = 13;
		if(current == 0){
			tempY = new float[1];
			tempY[0] = getFirstStepY(2);
			return concatAll(tempY, Arrays.copyOfRange(coordinateY, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 39){
				return Arrays.copyOfRange(coordinateY, current-1+offset, current+len+offset);
			}
			else if(current > 39){
				return Arrays.copyOfRange(coordinateY, current-40, current-40+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateY, current-1+offset, 52),
						Arrays.copyOfRange(coordinateY, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			tempY = new float[len+1];
			Arrays.fill(tempY,0, len+1, pathYelowGreenY); 
			return tempY;
		}
		else{
			tempY = new float[current+len-50];
			Arrays.fill(tempY,0, current+len-50, pathYelowGreenY);
			return concatAll(Arrays.copyOfRange(coordinateY, current-40, 11),tempY);
		}
	}
	
	static public float[] getCutXBlue(int current, int len){
		float[] tempX;
		int offset = 26;
		if(current == 0){
			tempX = new float[1];
			tempX[0] = getFirstStepX(3);
			return concatAll(tempX, Arrays.copyOfRange(coordinateX, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 26){
				return Arrays.copyOfRange(coordinateX, current-1+offset, current+len+offset);
			}
			else if(current > 26){
				return Arrays.copyOfRange(coordinateX, current-27, current-27+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateX, current-1+offset, 52),
						Arrays.copyOfRange(coordinateX, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			tempX = new float[len+1];
			Arrays.fill(tempX,0, len+1, pathRedBlueX); 
			return tempX;
			//return Arrays.copyOfRange(pathYellowBlueXY, current-51, current+len-50);
		}
		else{
			tempX = new float[current+len-50];
			Arrays.fill(tempX,0, current+len-50, pathRedBlueX);
			return concatAll(Arrays.copyOfRange(coordinateX, current-27, 24),tempX);
			
		}
	}
	
	static public float[] getCutYBlue(int current, int len){
		float[] tempY;
		int offset = 26;
		if(current == 0){
			tempY = new float[1];
			tempY[0] = getFirstStepY(3);
			return concatAll(tempY, Arrays.copyOfRange(coordinateY, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 26){
				return Arrays.copyOfRange(coordinateY, current-1+offset, current+len+offset);
			}
			else if(current > 26){
				return Arrays.copyOfRange(coordinateY, current-27, current-27+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateY, current-1+offset, 52),
						Arrays.copyOfRange(coordinateY, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			return Arrays.copyOfRange(pathYellowBlueXY, current-51, current+len-50);
		}
		else{
			tempY = Arrays.copyOfRange(pathYellowBlueXY, 0, current+len-50);
			return concatAll(Arrays.copyOfRange(coordinateY, current-27, 24),tempY);
		}
	}
	
	static public float[] getCutXGreen(int current, int len){
		float[] tempX;
		int offset = 39;
		if(current == 0){
			tempX = new float[1];
			tempX[0] = getFirstStepX(4);
			return concatAll(tempX, Arrays.copyOfRange(coordinateX, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 13){
				return Arrays.copyOfRange(coordinateX, current-1+offset, current+len+offset);
			}
			else if(current > 13){
				return Arrays.copyOfRange(coordinateX, current-14, current-14+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateX, current-1+offset, 52),
						Arrays.copyOfRange(coordinateX, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			return Arrays.copyOfRange(pathRedGreenXY, current-51, current+len-50);
		}
		else{
			tempX = Arrays.copyOfRange(pathRedGreenXY, 0, current+len-50);
			return concatAll(Arrays.copyOfRange(coordinateX, current-14, 37),tempX);
			
		}
	}
	
	static public float[] getCutYGreen(int current, int len){
		float[] tempY;
		int offset = 39;
		if(current == 0){
			tempY = new float[1];
			tempY[0] = getFirstStepY(4);
			return concatAll(tempY, Arrays.copyOfRange(coordinateY, 0+offset, len+offset));
		}
		if(current + len <= 50){
			if(current+len <= 13){
				return Arrays.copyOfRange(coordinateY, current-1+offset, current+len+offset);
			}
			else if(current > 13){
				return Arrays.copyOfRange(coordinateY, current-14, current-14+len+1);
			}
			else{
				return concatAll(Arrays.copyOfRange(coordinateY, current-1+offset, 52),
						Arrays.copyOfRange(coordinateY, 0, len-(52-current-offset)));
			}
		}
		else if(current > 50 && current + len <= 56){
			tempY = new float[len+1];
			Arrays.fill(tempY,0, len+1, pathYelowGreenY); 
			return tempY;
		}
		else{
			tempY = new float[current+len-50];
			Arrays.fill(tempY,0, current+len-50, pathYelowGreenY);
			return concatAll(Arrays.copyOfRange(coordinateY, current-14, 37),tempY);
		}
	}
	
	
	
	static public float[] getCutX(int current, int len, int type){
//		float[] tempX;
//		int offset = 0;
//		if(type == 2){
//			offset = 13;
//		}
//		if(type == 3){
//			offset = 26;
//		}
//		if(current == 0){
//			tempX = new float[1];
//			tempX[0] = getFirstStepX(type);
//			return concatAll(tempX, Arrays.copyOfRange(coordinateX, 0+offset, len+offset));
//		}
//		if(current + len <= 50){
//			if(type == 1){
//				return Arrays.copyOfRange(coordinateX, current-1, current+len);
//			}
//			else if(type == 2){
//				if(current+len <= 39){
//					return Arrays.copyOfRange(coordinateX, current-1+offset, current+len+offset);
//				}
//				else if(current > 39){
//					return Arrays.copyOfRange(coordinateX, current-40, current-40+len+1);
//				}
//				else{
//					return concatAll(Arrays.copyOfRange(coordinateX, current-1+offset, 52),
//							Arrays.copyOfRange(coordinateX, 0, len-(52-current-offset)));
//				}
//			}
//		}
//		else if(current > 50 && current + len <= 56){
//			if(type == 1){
//				tempX = new float[len+1];
//				Arrays.fill(tempX,0, len+1, pathRedBlueX); 
//				return tempX;
//			}
//			else if(type == 2){
//				return Arrays.copyOfRange(pathYellowBlueXY, current-51, current+len-50);
//			}
//		}
//		else{
//			if(type == 1){
//				tempX = new float[current+len-50];
//				Arrays.fill(tempX,0, current+len-50, pathRedBlueX);
//				return concatAll(Arrays.copyOfRange(coordinateX, current-1, 50),tempX);
//			}
//			else if(type == 2){
//				tempX = Arrays.copyOfRange(pathYellowBlueXY, 0, current+len-50);
//				return concatAll(Arrays.copyOfRange(coordinateX, current-40, 11),tempX);
//			}
//		}
//		return null;
		if(type == 1)
			return getCutXRed(current, len);
		else if(type == 2)
			return getCutXYellow(current, len);
		else if(type == 3)
			return getCutXBlue(current, len);
		else if(type == 4)
			return getCutXGreen(current, len);
		return null;
	}
	
	
	static public float[] getCutY(int current, int len, int type){
//		float[] tempY;
//		int offset = 0;
//		if(type == 2){
//			offset = 13;
//		}
//		if(current == 0){
//			tempY = new float[1];
//			tempY[0] = getFirstStepY(type);
//			return concatAll(tempY, Arrays.copyOfRange(coordinateY, 0+offset, len+offset));
//		}
//		if(current + len <= 50){
//			if(type == 1){
//				return Arrays.copyOfRange(coordinateY, current-1, current+len);
//			}
//			else if(type == 2){
//				if(current+len <= 39){
//					return Arrays.copyOfRange(coordinateY, current-1+offset, current+len+offset);
//				}
//				else if(current > 39){
//					return Arrays.copyOfRange(coordinateY, current-40, current-40+len+1);
//				}
//				else{
//					return concatAll(Arrays.copyOfRange(coordinateY, current-1+offset, 52),
//							Arrays.copyOfRange(coordinateY, 0, len-(52-current-offset)));
//				}
//			}
//		}
//		else if(current > 50 && current + len <= 56){
//			if(type == 1){
//				return Arrays.copyOfRange(pathRedGreenXY, current-51, current+len-50);
//			}
//			else if(type == 2){
//				tempY = new float[len+1];
//				Arrays.fill(tempY,0, len+1, pathYelowGreenY); 
//				return tempY;
//			}
//		}
//		else{
//			if(type == 1){
//				tempY = Arrays.copyOfRange(pathRedGreenXY, 0, current+len-50);			
//				return concatAll( Arrays.copyOfRange(coordinateY, current-1, 50), tempY);
//			}
//			else if(type == 2){
//				tempY = new float[current+len-50];
//				Arrays.fill(tempY,0, current+len-50, pathYelowGreenY);
//				return concatAll(Arrays.copyOfRange(coordinateY, current-40, 11),tempY);
//			}
//		}
//		return null;
		if(type == 1)
			return getCutYRed(current, len);
		else if(type == 2)
			return getCutYYellow(current, len);
		else if(type == 3)
			return getCutYBlue(current, len);
		else if(type == 4)
			return getCutYGreen(current, len);
		return null;
	}
}
