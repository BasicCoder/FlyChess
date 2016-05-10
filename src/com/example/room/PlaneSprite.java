package com.example.room;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseElasticInOut;
import org.andengine.util.modifier.ease.EaseSineInOut;
import org.andengine.util.modifier.ease.EaseStrongInOut;

import com.example.room.Control;
import com.example.room.GameView;

import android.os.Message;
import android.util.Log;
import android.widget.Toast; 

public class PlaneSprite extends Sprite{
	public PlaneSprite(Dice realDice, int typeT, int numT, float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			colorType = typeT;//1 red; 2 G; 3 B; 4 Y
			seqNum = numT;
			tempDice = realDice;
	}


	public int colorType;//1 red; 2 Y; 3 B; 4 G
	public int seqNum;//1,2,3,4
	public int currentIndex = -2;
	private Dice tempDice;
	
	public PlaneSprite getInstance()
	{
		return this;
	}
	
	public void moveByNum(final int step){
		
		Path path = null;
		try {
			Log.i("moveByNum", ""+currentIndex+' '+ step+' '+ colorType+' '+ seqNum);
			path = Control.getMovePath(currentIndex, step, colorType, seqNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final PathModifier tempModifier = new PathModifier(0.25f*path.getSize(), path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				//Debug.d(""+pPathModifier.);
				currentIndex += step;
				
				if(currentIndex >=0 
						&& currentIndex < Control.inCirclePathLength -1
						&& colorType == Control.getColorIdx(Control.getPhyIndex(currentIndex, colorType)))
					getInstance().moveExtraStep();
				
				else{
					if(currentIndex > Control.wayLength-1){
						currentIndex = ((Control.wayLength-1)*2-currentIndex);				
					}
					else if (currentIndex == Control.wayLength-1) { // finishing!!!
						getInstance().setScale(1.5f, 1.5f);
						switch (colorType) {
						case 1:
							Control.redOnWayCnt-=1;
							break;
						case 2:
							Control.yellowOnWayCnt-=1;
							break;
						case 3:
							Control.blueOnWayCnt-=1;
							break;
						case 4:
							Control.greenOnWayCnt-=1;
							break;
						}
					}
					else if (currentIndex >= 0)
						GameView.checkCrushBackHome(colorType,currentIndex);
				}
				Control.complete=true;
				// end of a round of movement
				
			}
		}, EaseSineInOut.getInstance());

		tempModifier.setAutoUnregisterWhenFinished(true);
		this.registerEntityModifier(tempModifier);
		
		
		//this.clearEntityModifiers();
	}
	
	private void moveExtraStep() {
		// TODO Auto-generated method stub
		if (currentIndex +1 >= Control.inCirclePathLength ||
				currentIndex <0 ) {
			return;
		}
		if(colorType == Control.getColorIdx(Control.getPhyIndex(currentIndex, colorType)))
		{
			Log.i("moveExtraStep","samecolor");
			
			final int jumpEnd = Control.getJumpIdx(currentIndex,colorType);

			Log.i("moveExtraStep","samecolor "+currentIndex + " -> "+jumpEnd);
			Log.i("moveExtraStep","samecolor "+Control.getPhyIndex(currentIndex, colorType) 
					+ " -> "+Control.getPhyIndex(jumpEnd,colorType));
			
			Path path = new Path(2)
					.to(Control.getPhyX(currentIndex, colorType), Control.getPhyY(currentIndex, colorType))
					.to(Control.getPhyX(jumpEnd, colorType), Control.getPhyY(jumpEnd, colorType));
			
			final PathModifier tempModifier = new PathModifier(0.8f, path, null, new IPathModifierListener() {
				@Override
				public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
					
				}

				@Override
				public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
					
				}

				@Override
				public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				}

				@Override
				public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
					//Debug.d(""+pPathModifier.);
					currentIndex = jumpEnd;
					if (currentIndex >= 0)
						GameView.checkCrushBackHome(colorType,currentIndex);
				}
			},EaseStrongInOut.getInstance());


			tempModifier.setAutoUnregisterWhenFinished(true);
			this.registerEntityModifier(tempModifier);
			
			
			Log.i("moveExtraStep","samecolor finished");
		}
	}

	public void moveBackHome() {
		// TODO Auto-generated method stub
		Path path = new Path(2)
				.to(Control.getPhyX(currentIndex, colorType), Control.getPhyY(currentIndex, colorType))
				.to(Control.getStartX(colorType, seqNum), Control.getStartY(colorType, seqNum));
		
		final PathModifier tempModifier = new PathModifier(1.5f, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				//Debug.d(""+pPathModifier.);
				currentIndex = Control.startIndex;
			}
		},EaseElasticInOut.getInstance());


		tempModifier.setAutoUnregisterWhenFinished(true);
		this.registerEntityModifier(tempModifier);
	}
	
/*	public void doTheMove(int len) {
		// TODO Auto-generated method stub
		if (currentIndex + 1 == Control.wayLength) { // finished plane, no moving
			return ;
		}
		else{											// valid planes, on road
			moveByNum(len);		
		}	
	}
*/	
	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
    		float pTouchAreaLocalX, float pTouchAreaLocalY) 
	{		
		// TODO Auto-generated method stub
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP
				&& Control.isFlyTurn()) 
		{
			if(		Control.getColorTurn() != colorType 
					//|| Control.getColorTurn()!=GameView.localGameColor
					){
				return true;
			}
			Log.i("PlaneSprite Touched", ""+colorType+" " +seqNum+" "+ Control.getDiceNum());
			
			MsgInfo moveInfo = new MsgInfo(Control.progressCnt, colorType, seqNum, Control.getDiceNum());
			Message msg = new Message();
			msg.obj = moveInfo;
			msg.what=5;
			GameView.mainHandler.sendMessage(msg);
				
		}	
		return true;
    }

	
	
	/*
	
	public void flyToColor(){
		Debug.d("11");
		float[] coordinateX = Control.getCutX(currentIndex, 4, colorType);
		float[] coordinateY = Control.getCutY(currentIndex, 4, colorType);
		Path path = new Path(2).to(coordinateX[0], coordinateY[0])
					.to(coordinateX[4], coordinateY[4]);
		Debug.d("22");
		PathModifier tempModifier = new PathModifier(3, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				//Debug.d("bbbb");
				//tempDice.stopAnimation(0);
				
			}
		}, EaseSineInOut.getInstance());
		Debug.d("33");
		this.registerEntityModifier(tempModifier);
		tempModifier.setAutoUnregisterWhenFinished(true);
		currentIndex += 4;
		Debug.d(""+currentIndex);
	}
	
	public void moveByNum(int step){
		
		Path path;
		if(current == -1){
			path = new Path(2).to(Control.getStartX(type, num), Control.getStartY(type, num))
					.to(Control.getFirstStepX(type), Control.getFirstStepY(type));
		}
		else if(current+step <= 56){
			float[] coordinateX = Control.getCutX(current, step, type);
			float[] coordinateY = Control.getCutY(current, step, type);
			path = new Path(coordinateX, coordinateY);
		}
		else{
			float[] tempX = new float[1];
			tempX[0] = Control.getStartX(type, num);
			float[] tempY = new float[1];
			tempY[0] = Control.getStartY(type, num);
			
			float[] coordinateX = Control.concatAll(Control.getCutX(current, step-(current+step-56), type),tempX);
			float[] coordinateY = Control.concatAll(Control.getCutY(current, step-(current+step-56), type),tempY);
			
			path = new Path(coordinateX, coordinateY);
			//path.to(Control.getStartX(type, num), Control.getStartY(type, num));
		}
	
		PathModifier tempModifier = new PathModifier((float)step/3, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				//Control.pos[changeNum(current)].pos[type-1] = 0;
				
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				//Debug.d(""+pPathModifier.);
				tempDice.stopAnimation(0);
			
			}
		}, EaseSineInOut.getInstance());
		
		this.registerEntityModifier(tempModifier);
		tempModifier.setAutoUnregisterWhenFinished(true);
		if(current == -1){
			current = 0;
		}
		else{
			current += step;
			if(current > 56)
				current = -1;
		}
		//test = true;
		//this.clearEntityModifiers();
	}
	
	
	public void backHome(){
		Path path = new Path(2).to(getX(), getY()).to(Control.getStartX(type, num), Control.getStartY(type, num));
		PathModifier tempModifier = new PathModifier((float)1, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				
			}
		}, EaseSineInOut.getInstance());
		
		this.registerEntityModifier(tempModifier);
		tempModifier.setAutoUnregisterWhenFinished(true);
		Control.pos[changeNum(current)].pos[type-1] = 0;
		Control.typeBack = 0;
		Control.numBack = 0;
		current = -1;
		
	}
	
	
	
	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if(Control.turn == type ){
				if(current > 0 && current <= 50)
					Control.pos[changeNum(current)].pos[type-1] = 0;
				moveByNum(Control.len);
				Control.count = num;
				Control.complete = true;
				if(current <= 50 && (current + 2) % 4 == 0 ){
					
					
					flyToColor();
					Debug.d("cc");
				}
				Control.turn++;
				if(Control.turn == 5)
					Control.turn = 1;
				Control.diceOrNot = false;
				
				if(current <= 0 || current > 50)
					return true;
				Control.pos[changeNum(current)].pos[type-1] = num;
				for(int i =0 ; i < 4; i++){
					if(i != type-1 && Control.pos[changeNum(current)].pos[i] != 0){
						Control.typeBack = i+1;
						Control.numBack = Control.pos[changeNum(current)].pos[i];
					}
				}
			}
		}
		//this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
    }
    */
}
