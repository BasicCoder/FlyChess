package com.example.room;

import java.util.Random;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.os.Message;
import android.util.Log;

public class Dice extends AnimatedSprite{

	public Dice(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		//this.setCurrentTileIndex(0);
		//this.animate(100);
		this.stopAnimation(0);
		//this.animate(100);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			
			// TODO
			
			if(Control.getColorTurn() != GameView.localGameColor)
				return true;	
					
			if(! Control.isDiceTurn()){
				stopAnimation(0);
			}
			
			// then ensure it is dice's turn, and is my color
			
			Log.i("Dice Touched", "");
			
			int diceResult = new Random().nextInt(6) + 1;
			MsgInfo moveInfo = new MsgInfo(Control.progressCnt, 5, 0, diceResult);
			Message msg = new Message();
			msg.obj = moveInfo;
			msg.what= 4;
			GameView.mainHandler.sendMessage(msg);
			
			
			//this.stopAnimation(0);
		}
		//this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
    }
}
