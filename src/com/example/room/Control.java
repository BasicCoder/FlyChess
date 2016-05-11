/*
package com.example.flygame;

import java.util.Arrays;

public class Control {
	
	public class CommonData {
		public int[] pos = new int[4];
		
		public CommonData(){
			for(int i = 0; i < 4; i++){
				pos[i] = 0;
			}
		}
	}
	
	static CommonData[] pos = new CommonData[52];
	
	public void initial(){
		for(int i = 0; i < 52; i++){
			pos[i] = new CommonData();
		}
	}
	
	
	static int typeBack = 0;
	static int numBack = 0;
	
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
	
	static boolean gameOver = false;
	static int turn = 1;
	static boolean diceOrNot = false;
	static boolean complete = false;
	static int len = 0;
	static int count = 1;
	static int touchNum = 1;
	static int gameNum = 1;
	
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
*/


/*

package com.example.flygame;

import java.util.Arrays;

public class Control 
{
	
	final static float[] beginRedYellowX = {26,120}; // beginning (waiting) field coordination 
	final static float[] beginBlueGreenX = {638,732};
	final static float[] beginRedGreenY = {636,731};
	final static float[] beginYellowBlueY = {24,118};
	
	final static float[] startX = {190,2,567,755}; // first step cord
	final static float[] startY = {755,191,2,566};
	
	
	// final goal path's coordinations
	//*
	//*
	final static float pathRedBlueX = 378;	
	// for red, it's Y;  for green, it's X
	final static float[] pathRedGreenXY = {661,613,567,519,473,425};
	
	final static float pathYelowGreenY = 377;
	// for yellow, it's Y;  for blue, it's X
	final static float[] pathYellowBlueXY = {96,143,191,238,284,331};
	//*
	
	// the "circle" points
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
	
	
	//**
	//*
	// methods
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
	
	
	
	
	
	// combine all the array  
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
	
	public static float[] reverseArr(float[] sourse) {  
		int Length = sourse.length;  
		float[] reverseA = new float[Length];
		for(int i=0, j=Length-1; i!=Length; ++i,--j)
		{
			reverseA[j] = sourse[i];
		}		
		return reverseA;
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


*/





package com.example.room;

import java.util.Arrays;

import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.opengl.font.IFont;

import android.util.Log;

public class Control 
{
	
	final static float[] beginRedYellowX = {26,120}; // beginning (waiting) field coordination 
	final static float[] beginBlueGreenX = {638,732};
	final static float[] beginRedGreenY = {636,731};
	final static float[] beginYellowBlueY = {24,118};
	
	final static float[] readyX = {190,2,567,755}; // first step cord
	final static float[] readyY = {755,191,2,566};
	
	
	
	final static float pathRedBlueX = 378;	
	// for red, it's Y;  for green, it's X
	final static float[] pathRedGreenXY = {661,613,567,519,473,425};
	
	final static float pathYelowGreenY = 377;
	// for yellow, it's Y;  for blue, it's X
	final static float[] pathYellowBlueXY = {96,143,191,238,284,331};
	
	final static float[] beginRedX = {26,120,26,120};
	final static float[] beginRedY = {636,731,731,636};
	final static float[] beginYellowX = {26,120,26,120};
	final static float[] beginYellowY = {24,118,118,24};
	final static float[] beginBlueX = {638,732,638,732};
	final static float[] beginBlueY = {24,118,118,24};
	final static float[] beginGreenX = {638,732,638,732};
	final static float[] beginGreenY = {636,731,731,636};
	
	
	final static float pathredX[] = {378,378,378,378,378,378};
	final static float pathredY[] = {661,613,567,519,473,425};
	
	final static float pathblueX[] = {378,378,378,378,378,378};
	final static float pathblueY[] = {96,143,191,238,284,331};
	
	final static float pathgreenX[] = {661,613,567,519,473,425};
	final static float pathgreenY[] = {377,377,377,377,377,377};
	
	final static float pathyellowX[] = {96,143,191,238,284,331};
	final static float pathyellowY[] = {377,377,377,377,377,377};
	
	
	// the "circle" points
	final static float[] coordinateX = {236,212,212,237,190,141,94,48,24,24,
			24,24,24,47,95,142,190,236,213,213,
			237,284,331,377,424,472,519,542,542,518,
			566,613,660,707,730,730,730,730,730,707,
			659,612,566,519,542,542,519,472,425,378,
			331,283,378,378,378,378,378,378,96,143,
			191,238,284,331,378,378,378,378,378,378,
			661,613,567,519,473,425};
	final static float[] coordinateY = {708,660,614,567,520,543,543,520,473,426,
			378,332,284,237,214,214,237,190,143,96,
			49,25,25,25,25,25,49,96,143,190,
			237,214,214,237,284,331,379,425,472,519,
			542,542,519,566,613,661,708,731,731,731,
			731,731,661,613,567,519,473,425,377,377,
			377,377,377,377,96,143,191,238,284,331,
			377,377,377,377,377,377};

	final static int[] circleOffset={0,13,26,39};
	final static int[] pathOffset={2,8,14,20};
	
	final static int [] colorIdx=
		{
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		4,1,2,3,
		1,1,1,1,1,1,
		2,2,2,2,2,2,
		3,3,3,3,3,3,
		4,4,4,4,4,4  };
	
	final static int [] jumpStartLogicIdx={
		17
	};
	final static int [] jumpEndLogicIdx={
		29
	};

	final static int [] toReadyNum={
		5,6
	};
	final static int [] diceAgainNum={
		6
	};
	
	public static int getPhyIndex(int cur, int type)
	{
		Log.i("getPhyIndex", ""+cur+" "+type);
		int[]way = null;
		switch (type) {
		case 1:
			way=redWay;
			break;
		case 2:
			way=yellowWay;
			break;
		case 3:
			way=blueWay;
			break;
		case 4:
			way=greenWay;
			break;
		default:
			break;
		}
		return way[cur];
	}
	public static int getPhyIndex(PlaneSprite pSprite)
	{
		int[]way = null;
		switch (pSprite.colorType) {
		case 1:
			way=redWay;
			break;
		case 2:
			way=yellowWay;
			break;
		case 3:
			way=blueWay;
			break;
		case 4:
			way=greenWay;
			break;
		default:
			break;
		}
		return way[pSprite.currentIndex];
	}
	
	public static float getPhyX(int cur, int type)
	{
		int[]way = null;
		switch (type) {
		case 1:
			way=redWay;
			break;
		case 2:
			way=yellowWay;
			break;
		case 3:
			way=blueWay;
			break;
		case 4:
			way=greenWay;
			break;
		default:
			break;
		}
		return coordinateX[way[cur]];
	}
	public static float getPhyY(int cur, int type)
	{
		int[]way = null;
		switch (type) {
		case 1:
			way=redWay;
			break;
		case 2:
			way=yellowWay;
			break;
		case 3:
			way=blueWay;
			break;
		case 4:
			way=greenWay;
			break;
		default:
			break;
		}
		return coordinateY[way[cur]];
	}
	
	
	public static int getJumpIdx(int cur,int type){
		for (int j = 0; j < jumpStartLogicIdx.length; j++) {
			if(cur==jumpStartLogicIdx[j])
				return jumpEndLogicIdx[j];
		}
		for (int i = cur+1; i < wayLength ; i++) {
			if (getColorIdx(getPhyIndex(i, type)) == type) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getColorIdx(int i){
		return colorIdx[i];
	}

	final static int circleLength=52;
	final static int inCirclePathLength=50;
	final static int terminalPathLength=6;
	final static int wayLength=56;
	
	final static int startIndex=-2;
	final static int readyIndex=-1;
	
	
	
	final static int [] redWay=new int[wayLength];
	final static int [] yellowWay=new int[wayLength];
	final static int [] blueWay=new int[wayLength];
	final static int [] greenWay=new int[wayLength];

	static public void initialColorWays()
	{
		for(int i=0; i!=inCirclePathLength; ++i){
			redWay[i]	=(i+circleOffset[0])%circleLength;
			yellowWay[i]=(i+circleOffset[1])%circleLength;
			blueWay[i]	=(i+circleOffset[2])%circleLength;
			greenWay[i]	=(i+circleOffset[3])%circleLength;
		}
		
		for (int i = inCirclePathLength; i < wayLength; ++i) {
			redWay[i]	=(i+pathOffset[0]);
			yellowWay[i]=(i+pathOffset[1]);
			blueWay[i]	=(i+pathOffset[2]);
			greenWay[i]	=(i+pathOffset[3]);
		}
		String string1="";
		String string2="";
		String string3="";
		String string4="";
		for (int j = 0; j < wayLength; j++) {
			string1+=(" "+redWay[j]);
			string2+=(" "+yellowWay[j]);
			string3+=(" "+blueWay[j]);
			string4+=(" "+greenWay[j]);
		}

		Log.i("redWay", string1);
		Log.i("yellowWay", string2);
		Log.i("blueWay", string3);
		Log.i("greenWay", string4);
	}
	

	/*
	 *  dynamic informations 
	 */
	
	public static int playerNum=4;
	private static int colorTurn = 1;
	private static boolean diceOrNot = true;
	private static int diceNum=1;
	private static int myGameNum=1;
	protected static boolean gameOver = false;
	protected static boolean complete = true; // flag: operation done
	protected static int progressCnt;
	protected static int selectSeq;

	protected static int redOnWayCnt=0;
	protected static int yellowOnWayCnt=0;
	protected static int blueOnWayCnt=0;
	protected static int greenOnWayCnt=0;
	
	public static void goNextColorTurn() {
		colorTurn = (colorTurn+1);
		if (colorTurn>playerNum) {
			colorTurn=1;
		}
	}
	public static int getColorTurn(){
		return colorTurn;
	}
	public static boolean isDiceTurn(){
		return diceOrNot==true;
	}
	public static boolean isFlyTurn(){
		return diceOrNot==false;
	}	
	public static void setDiceTurn(){
		diceOrNot = true;
	}
	public static void setFlyTurn()	{
		diceOrNot = false;
	}
	public static int getDiceNum() {
		return diceNum;
	}
	public static void setDiceNum(int s) {
		diceNum=s;
	}
	public static boolean isToReadyNum(int n) {
		// TODO Auto-generated method stub
		for (int i = 0; i < toReadyNum.length; i++) {
			if(n==toReadyNum[i])
				return true;
		}		
		return false;
	}
	public static boolean isDiceAgainNum(int n) {
		// TODO Auto-generated method stub
		for (int i = 0; i < diceAgainNum.length; i++) {
			if(n==diceAgainNum[i])
				return true;
		}		
		return false;
	}
	
	//**
	//*
	// methods
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
	static public float getReadyStepX(int type){
		return readyX[type-1];
	}	
	static public float getReadyStepY(int type){
		return readyY[type-1];
	}
	
	// currentIndex should be >= 0 
	// return the point index sequence of movement, starts at currentIdx, length is len
	public static int[] getWayIndexCut(int currentIndex, int len, int type)throws Exception
	{
		// TODO Auto-generated method stub
		
		Log.i("getWayIndexCut", ""+currentIndex+' '+ len+' '+ type);
		
		int[]cut =new int[len+1];
		if (currentIndex < 0) {
			throw new IndexOutOfBoundsException("currentIndex should be lager than 0.");
		}
		if (currentIndex+len >= wayLength) {
			throw new IndexOutOfBoundsException("currentIndex+len should be less than wayLength.");
		}
		
		int[]way = null;
		switch (type) {
		case 1:
			way=redWay;
			break;
		case 2:
			way=yellowWay;
			break;
		case 3:
			way=blueWay;
			break;
		case 4:
			way=greenWay;
			break;
		default:
			break;
		}
		
		for (int i = 0; i <= len; i++) {
			cut[i]=way[i+currentIndex];
			Log.i("cut",""+cut[i]);
		}		
		return cut;
	}
	
	// input logic index, step length, color type, sequence number
	// return path index of physics points
	public static Path getMovePath(int currentIndex, int len, int type, int seqnum) throws Exception
	{
		// TODO Auto-generated method stub
		
		float [] Xcut = new float[len+1];
		float [] Ycut = new float[len+1];
		int i=0;
		Log.i("c,l-1",""+currentIndex+" "+len);
		if (currentIndex == startIndex && len>=0) {
			Xcut[i] = getStartX(type, seqnum);
			Ycut[i] = getStartY(type, seqnum);
			++i;
			++currentIndex;
			--len;
		}
		Log.i("c,l-2",""+currentIndex+" "+len);
		
		if (currentIndex == readyIndex && len>=0 ) {
			Xcut[i] = getReadyStepX(type);
			Ycut[i] = getReadyStepY(type);
			++i;
			++currentIndex;		
			--len;	
			Log.i("ready","");
		}
		Log.i("c,l-3",""+currentIndex+" "+len);
		
		
		if(len >= 0)
		{
			if (currentIndex+len < wayLength) {	// need no turn back
				int[] cut=getWayIndexCut(currentIndex, len, type);	

				for (int idx=0; idx!=cut.length; ++idx) {
					Xcut[i]=coordinateX[cut[idx]];
					Ycut[i]=coordinateY[cut[idx]];
					++i;
				}
			}
			else {
				// currentIndex+len > wayLength
				// will pass over the terminal and go back
				Log.i("11111", "currentIndex="+currentIndex);
				
				Log.i("11111", "currentIndex="+currentIndex+", len ="+(wayLength-1-currentIndex));
				
				int[] cut=getWayIndexCut(currentIndex, wayLength-1-currentIndex, type);					
				
				for (int idx=0; idx!=cut.length; ++idx) {
					Xcut[i]=coordinateX[cut[idx]];
					Ycut[i]=coordinateY[cut[idx]];
					++i;
				}
				Log.i("11111", "");
				int backCnt=currentIndex+len-wayLength+1;
				
				Log.i("11111", "currentIndex="+(wayLength-1-backCnt)+", len ="+(backCnt)+", i="+(i));
				
				cut=reverseArr(getWayIndexCut(wayLength-1-backCnt, backCnt-1, type));				
				for (int idx=0; idx!=cut.length; ++idx) {
					Xcut[i]=coordinateX[cut[idx]];
					Ycut[i]=coordinateY[cut[idx]];
					++i;
				}		

				Log.i("22222", "");
			}
			
		}

		Path path = new Path(Xcut,Ycut);
		return path;				
	}
	
	
	// combine all the array  
	public static float[] concatAll(float[] first, float[] ... rest) {  
		// TODO Auto-generated method stub
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
	
	public static float[] reverseArr(float[] sourse) {  
		// TODO Auto-generated method stub
		int Length = sourse.length;  
		float[] reverseA = new float[Length];
		for(int i=0, j=Length-1; i!=Length; ++i,--j)
		{
			reverseA[j] = sourse[i];
		}		
		return reverseA;
	} 
	
	public static int[] reverseArr(int[] sourse) {  
		// TODO Auto-generated method stub
		int Length = sourse.length;  
		int[] reverseA = new int[Length];
		for(int i=0, j=Length-1; i!=Length; ++i,--j)
		{
			reverseA[j] = sourse[i];
		}		
		return reverseA;
	}

	
	
	
	
/*	
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * static public float[] getCutXRed(int current, int len){
		float[] tempX;
		if(current == 0){
			tempX = new float[1];
			tempX[0] = getReadyStepX(1);
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
			tempY[0] = getReadyStepY(1);
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
			tempX[0] = getReadyStepX(2);
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
			tempY[0] = getReadyStepY(2);
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
			tempX[0] = getReadyStepX(3);
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
			tempY[0] = getReadyStepY(3);
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
			tempX[0] = getReadyStepX(4);
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
			tempY[0] = getReadyStepY(4);
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
	*/
	
}

	
	
	
	
	
	
	
	
	
	
	

