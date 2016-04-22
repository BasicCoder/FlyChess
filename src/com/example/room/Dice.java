package com.example.room;

import java.util.Random;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
			if(Control.diceOrNot == 1)
				return true;
			int result = new Random().nextInt(6) + 1;
			this.animate(new long[]{100,100,100,100,100,100,100}, new int[]{4,2,6,5,1,3,result}, false);
			Control.diceOrNot = 1;
			Control.len = result;
			//this.stopAnimation(0);
		}
		//this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
    }
}
