package com.example.room;


import java.io.IOException;


import java.io.InputStream;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;


import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseSineInOut;
import com.example.room.PlaneSprite;

import android.widget.Toast;


public class GameView extends SimpleBaseGameActivity{
	private static int CAMERA_WIDTH = 900;
	private static int CAMERA_HEIGHT = 800;
	private ITextureRegion mBackgroundTextureRegion;
	private ITextureRegion mRedTextureRegion, mGreenTextureRegion, mBlueTextureRegion, mYellowTextureRegion;
	private TiledTextureRegion  mDiceTextureRegion;
	@Override
	public EngineOptions onCreateEngineOptions() {
    	final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    	return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
        try {
        	// 1 - Set up bitmap textures
            ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("game_bg_800.jpg");
                }
            });
          
            
            ITexture diceTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("dices.png");
                }
            });
            
            ITexture redPlane = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("plane_red.png");
                }
            });
            ITexture greenPlane = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("plane_green.png");
                }
            });
            ITexture bluePlane = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("plane_blue.png");
                }
            });
            ITexture yellowPlane = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("plane_yellow.png");
                }
            });
            // 2 - Load bitmap textures into VRAM
            backgroundTexture.load();
            
            diceTexture.load();
            
            redPlane.load();
            greenPlane.load();
            bluePlane.load();
            yellowPlane.load();
            // 3 - Set up texture regions
            this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
            
            this.mDiceTextureRegion = TextureRegionFactory.extractTiledFromTexture(diceTexture, 7, 1);
            
            this.mRedTextureRegion = TextureRegionFactory.extractFromTexture(redPlane);
            this.mGreenTextureRegion = TextureRegionFactory.extractFromTexture(greenPlane);
            this.mBlueTextureRegion = TextureRegionFactory.extractFromTexture(bluePlane);
            this.mYellowTextureRegion = TextureRegionFactory.extractFromTexture(yellowPlane);
            
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	@Override
	protected Scene onCreateScene() {
		// 1 - Create new scene
		final Scene scene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(backgroundSprite);
		
		Dice myAnimatedSprite= new Dice(800, 350, mDiceTextureRegion, this.getVertexBufferObjectManager());
		//myAnimatedSprite.animate(100);
		scene.attachChild(myAnimatedSprite);
		scene.registerTouchArea(myAnimatedSprite);
				
				
				
		PlaneSprite[] redPlaneSprite = new PlaneSprite[4]; 
		for(int i = 0; i < 4; i++){
			redPlaneSprite[i] = new PlaneSprite(myAnimatedSprite, 1, i+1, Control.getStartX(1, i+1), Control.getStartY(1, i+1), this.mRedTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(redPlaneSprite[i]);
			scene.registerTouchArea(redPlaneSprite[i]);
		}
		
		PlaneSprite[] yellowPlaneSprite = new PlaneSprite[4]; 
		for(int i = 0; i < 4; i++){
			yellowPlaneSprite[i] = new PlaneSprite(myAnimatedSprite, 2, i+1, Control.getStartX(2, i+1), Control.getStartY(2, i+1), this.mYellowTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(yellowPlaneSprite[i]);
			scene.registerTouchArea(yellowPlaneSprite[i]);
		}
		
		PlaneSprite[] bluePlaneSprite = new PlaneSprite[4]; 
		for(int i = 0; i < 4; i++){
			bluePlaneSprite[i] = new PlaneSprite(myAnimatedSprite, 3, i+1, Control.getStartX(3, i+1), Control.getStartY(3, i+1), this.mBlueTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(bluePlaneSprite[i]);
			scene.registerTouchArea(bluePlaneSprite[i]);
		}
		
		PlaneSprite[] greenPlaneSprite = new PlaneSprite[4]; 
		for(int i = 0; i < 4; i++){
			greenPlaneSprite[i] = new PlaneSprite(myAnimatedSprite, 4, i+1, Control.getStartX(4, i+1), Control.getStartY(4, i+1), this.mGreenTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(greenPlaneSprite[i]);
			scene.registerTouchArea(greenPlaneSprite[i]);
		}
		
		/*
		Path 
			path = new Path(2).to(Control.getStartX(1, 1), Control.getStartY(1, 1))
					.to(Control.getFirstStepX(1), Control.getFirstStepY(1));
		
		PathModifier tempModifier = new PathModifier((float)2, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				//current++;
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				Debug.d("finish");
			}
		}, EaseSineInOut.getInstance());
		
		redPlaneSprite[0].registerEntityModifier(tempModifier);
		*/
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		return scene;
	}
	



}