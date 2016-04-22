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
import org.andengine.util.modifier.ease.EaseSineInOut;
import com.example.room.Control;
import com.example.room.GameView;
import android.widget.Toast; 

public class PlaneSprite extends Sprite{
	public PlaneSprite(Dice realDice, int typeT, int numT, float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			type = typeT;//1 red; 2 G; 3 B; 4 Y
			num = numT;
			tempDice = realDice;
	}


	public int type;//1 red; 2 Y; 3 B; 4 G
	public int num;//1,2,3,4
	public int current = -1;
	private Dice tempDice;
	
	private boolean test = false;
	
	private int changeNum(int step){
		if(type == 1){
			return step-1;
		}
		else if(type == 2){
			if(step <= 39)
				return step+12;
			else{
				return step-40;
			}
		}
		else if(type == 3){
			if(step <= 26)
				return step+25;
			else{
				return step-27;
			}
		}
		else{
			if(step <= 13)
				return step+38;
			else{
				return step-14;
			}
		}
	}
	
	public void flyToColor(){
		Debug.d("11");
		float[] coordinateX = Control.getCutX(current, 4, type);
		float[] coordinateY = Control.getCutY(current, 4, type);
		Path path = new Path(2).to(coordinateX[0], coordinateY[0])
					.to(coordinateX[4], coordinateY[4]);
		Debug.d("22");
		PathModifier tempModifier = new PathModifier(2, path, null, new IPathModifierListener() {
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
		current += 4;
		Debug.d(""+current);
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
		test = false;
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
				test = true;
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

	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if(Control.turn == type && Control.diceOrNot == 1){
				moveByNum(Control.len);
				if(current <= 50 && (current + 2) % 4 == 0 ){
					/*while(!test){
						Debug.d("xxxx");
					}
					*/
					
					flyToColor();
					Debug.d("cc");
				}
				Control.turn++;
				if(Control.turn == 5)
					Control.turn = 1;
				Control.diceOrNot = 0;
				/*
				Control.pos[changeNum(current)].pos[type-1] = num;
				for(int i =0 ; i < 4; i++){
					if(Control.pos[changeNum(current)].pos[i] != 0){
						
					}
				}
				*/
			}
		}
		//this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
    }

}
