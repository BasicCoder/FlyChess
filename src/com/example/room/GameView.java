package com.example.room;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import org.andengine.util.debug.Debug;
import com.example.room.PlaneSprite;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GameView extends SimpleBaseGameActivity {

	private OutputStream dos = null;
	private InputStream dis = null;

	private int roomNum = 10001;
	public static int localGameColor = 1;
	//private int countSeq = 1;

	private SendThread sendMsgThread;
	private GetThread getMsgThread;
	public static Handler mainHandler;
	public TimerTask timerTask;
	public Timer timer;
	ApplicationUtil appUtil;

	private int timeLast = 15;
	private Font mScoreFont;
	private static int CAMERA_WIDTH = 1600;
	private static int CAMERA_HEIGHT = 900;
	private ITextureRegion mBackgroundTextureRegion;
	private ITextureRegion mRedTextureRegion, mGreenTextureRegion, mBlueTextureRegion, mYellowTextureRegion;
	private TiledTextureRegion mDiceTextureRegion;
	private Font mFont;
	private Thread myThread;
	private static PlaneSprite[] redPlaneSprite;
	private static PlaneSprite[] yellowPlaneSprite;
	private static PlaneSprite[] bluePlaneSprite;
	private static PlaneSprite[] greenPlaneSprite;
	private Dice myDice;
	private ITextureRegion realBgTextureRegion;
	private ITextureRegion tuoguanRegion;
	private ITextureRegion tuichuRegion;
	private Font mTitleFont;
	private Font mPersonFont;
	private TimerHandler myTimer;
	private Font mRedFont;
	private Font mYellowFont;
	private Font mBlueFont;
	private Font mGreenFont;

	@Override
	public EngineOptions onCreateEngineOptions() {
		//final Camera camera = new Camera(-200, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Camera camera = new Camera(-575, -80, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new FillResolutionPolicy(), camera);
    	
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
          
            ITexture tuoguanTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("tuoguan.png");
                }
            });
            
            ITexture tuichuTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("tuichu.png");
                }
            });
            BitmapTexture realBgTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() { 
            	@Override 
            	public InputStream open() throws IOException {
            		return getAssets().open("timg.png");
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

			realBgTexture.load();
			tuoguanTexture.load();
            tuichuTexture.load();
			diceTexture.load();

			redPlane.load();
			greenPlane.load();
			bluePlane.load();
			yellowPlane.load();
			
			
			
			// new added myFont
			/*
			mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
			mFont.load();
			 */
			
			final ITexture scoreFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
					TextureOptions.BILINEAR);
			FontFactory.setAssetBasePath("font/");
			
			this.mScoreFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(),
					"LCD.ttf", 32, true, Color.WHITE);
			this.mScoreFont.load();
			
			this.mRedFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(),
					"LCD.ttf", 32, true, Color.RED);
			this.mRedFont.load();
			
			this.mYellowFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(),
					"LCD.ttf", 32, true, Color.YELLOW);
			this.mYellowFont.load();
			
			this.mBlueFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(),
					"LCD.ttf", 32, true, Color.BLUE);
			this.mBlueFont.load();
			
			this.mGreenFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(),
					"LCD.ttf", 32, true, Color.GREEN);
			this.mGreenFont.load();
			
			final ITexture textFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
					TextureOptions.BILINEAR);
			
			this.mTitleFont = FontFactory.createFromAsset(this.getFontManager(), textFontTexture, this.getAssets(),
					"huawen.ttf", 72, true, Color.BLACK);
			this.mTitleFont.load();

			final ITexture textFontTextureTemp = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
					TextureOptions.BILINEAR);
			this.mPersonFont = FontFactory.createFromAsset(this.getFontManager(), textFontTextureTemp, this.getAssets(),
					"huawen.ttf", 52, true, Color.WHITE);
			this.mPersonFont.load();
						
						
			
			
			
			
			
			
			
			
			this.realBgTextureRegion = TextureRegionFactory.extractFromTexture(realBgTexture);
            this.tuoguanRegion = TextureRegionFactory.extractFromTexture(tuoguanTexture);
            this.tuichuRegion = TextureRegionFactory.extractFromTexture(tuichuTexture);
			// 3 - Set up texture regions
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);

			this.mDiceTextureRegion = TextureRegionFactory.extractTiledFromTexture(diceTexture, 7, 1);

			this.mRedTextureRegion = TextureRegionFactory.extractFromTexture(redPlane);
			this.mGreenTextureRegion = TextureRegionFactory.extractFromTexture(greenPlane);
			this.mBlueTextureRegion = TextureRegionFactory.extractFromTexture(bluePlane);
			this.mYellowTextureRegion = TextureRegionFactory.extractFromTexture(yellowPlane);

			
			
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	protected Scene onCreateScene() {

		// 1 - Create new scene

		Control.initialColorWays();
		Control.progressCnt = 0;
		
		Bundle bundle = this.getIntent().getExtras();
		// 
		
		String Roomid = bundle.getString("roomid");
		String RoomStyle = bundle.getString("roomstyle");
		String UserSerial = bundle.getString("userserial");
		
		Log.e("Roomid", Roomid);
		Log.e("RoomStyle", RoomStyle);
		Log.e("UserSerial", UserSerial);
		
		roomNum = Integer.parseInt(Roomid);
		Control.playerNum = Integer.parseInt(RoomStyle);
		localGameColor = Integer.parseInt(UserSerial);
		/*
		 * appUtil = (ApplicationUtil) GameView.this.getApplication(); try {
		 * appUtil.init(); Socket socket = appUtil.getSocket(); dos =
		 * appUtil.getDos(); dis = appUtil.getDis();
		 * 
		 * } catch (IOException e) { e.printStackTrace(); } catch (Exception e)
		 * { e.printStackTrace(); }
		 */

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					appUtil = (ApplicationUtil) GameView.this.getApplication();
					Socket socket = appUtil.getSocket();
					dos = appUtil.getDos();
					dis = appUtil.getDis();

					String str = "hehehe";
					byte[] b = str.getBytes("utf-8");
					if (dos == null)
						Log.v("vvv", "xxxx");
					dos.write(b);
					dos.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		// background
		final Scene scene = new Scene();
		Sprite realBackground = new Sprite(-673, -105, realBgTextureRegion, this.getVertexBufferObjectManager()); 
		scene.attachChild(realBackground);
		
		Sprite gameGroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(gameGroundSprite);

		Sprite tuoguanSprite = new Sprite(780, 550, this.tuoguanRegion, getVertexBufferObjectManager());
		tuoguanSprite.setScale(0.7f);
		scene.attachChild(tuoguanSprite);
		
		Sprite tuichuSprite = new Sprite(780, 650, this.tuichuRegion, getVertexBufferObjectManager());
		tuichuSprite.setScale(0.7f);
		scene.attachChild(tuichuSprite);
		
		
		myDice = new Dice(850, 350, mDiceTextureRegion, this.getVertexBufferObjectManager());
		// myAnimatedSprite.animate(100);
		myDice.setScale(1.5f);
		scene.attachChild(myDice);
		scene.registerTouchArea(myDice);

		final Text textTitle = new Text(-559,124, this.mTitleFont, "Player List", 18, this.getVertexBufferObjectManager());
		scene.attachChild(textTitle);
		
		Font colorFont=null;
		String colorString=null;
		switch (localGameColor) {
		case 1:
			colorFont=mRedFont;
			colorString="RED";
			break;
		case 2:
			colorFont=mYellowFont;
			colorString="YELLOW";
			break;
		case 3:
			colorFont=mBlueFont;
			colorString="BLUE";
			break;
		case 4:
			colorFont=mGreenFont;
			colorString="GREEN";
			break;

		default:
			break;
		}
		
		final Text localColor = new Text(-550,80, colorFont, "I am "+ colorString, 18, this.getVertexBufferObjectManager());
		scene.attachChild(localColor);
		
		final Text personOne = new Text(-573,272, this.mPersonFont,   "Red:   ", 18, this.getVertexBufferObjectManager());
		scene.attachChild(personOne);
		
		final Text personTwo = new Text(-573,380, this.mPersonFont,   "Yellow:", 18, this.getVertexBufferObjectManager());
		scene.attachChild(personTwo);
		
		final Text personThree = new Text(-573,488, this.mPersonFont, "Blue:  ", 18, this.getVertexBufferObjectManager());
		scene.attachChild(personThree);
		final Text personFour = new Text(-573,596, this.mPersonFont,  "Green: ", 18, this.getVertexBufferObjectManager());
		scene.attachChild(personFour);
		
		final Text stateOne = new Text(-380,272, this.mPersonFont, "Wait", 18, this.getVertexBufferObjectManager());
		scene.attachChild(stateOne);
		final Text stateTwo = new Text(-380,380, this.mPersonFont, "Wait", 18, this.getVertexBufferObjectManager());
		scene.attachChild(stateTwo);
		final Text stateThree = new Text(-380,488, this.mPersonFont, "Wait", 18, this.getVertexBufferObjectManager());
		scene.attachChild(stateThree);
		final Text stateFour = new Text(-380,596, this.mPersonFont, "Wait", 18, this.getVertexBufferObjectManager());
		scene.attachChild(stateFour);
		
		
		
		redPlaneSprite = new PlaneSprite[4];
		for (int i = 0; i < 4; i++) {
			redPlaneSprite[i] = new PlaneSprite(myDice, 1, i + 1, Control.getStartX(1, i + 1),
					Control.getStartY(1, i + 1), this.mRedTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(redPlaneSprite[i]);
			scene.registerTouchArea(redPlaneSprite[i]);
		}

		yellowPlaneSprite = new PlaneSprite[4];
		for (int i = 0; i < 4; i++) {
			yellowPlaneSprite[i] = new PlaneSprite(myDice, 2, i + 1, Control.getStartX(2, i + 1),
					Control.getStartY(2, i + 1), this.mYellowTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(yellowPlaneSprite[i]);
			scene.registerTouchArea(yellowPlaneSprite[i]);
		}

		bluePlaneSprite = new PlaneSprite[4];
		for (int i = 0; i < 4; i++) {
			bluePlaneSprite[i] = new PlaneSprite(myDice, 3, i + 1, Control.getStartX(3, i + 1),
					Control.getStartY(3, i + 1), this.mBlueTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(bluePlaneSprite[i]);
			scene.registerTouchArea(bluePlaneSprite[i]);
		}

		greenPlaneSprite = new PlaneSprite[4];
		for (int i = 0; i < 4; i++) {
			greenPlaneSprite[i] = new PlaneSprite(myDice, 4, i + 1, Control.getStartX(4, i + 1),
					Control.getStartY(4, i + 1), this.mGreenTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(greenPlaneSprite[i]);
			scene.registerTouchArea(greenPlaneSprite[i]);
		}

		// texts for testing
		final Text colorText = new Text(-550, 30, mScoreFont, "R - Dice", this.getVertexBufferObjectManager());
		scene.attachChild(colorText);

		final Text cheatText = new Text(830, 520, mScoreFont, "N=" + Control.getDiceNum(),
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					Log.i("tmpText", "ACTION_UP " + Control.getDiceNum());
					
					int tmp = Control.getDiceNum() + 1;
					if (tmp == 7)
						tmp = 1;
					Control.setDiceNum(tmp);

					// this.setText("NUM=" + tmp);
					Log.i("tmpText", "setDiceNum " + tmp);
				}
				return true;
			}
		};
		scene.attachChild(cheatText);
		scene.registerTouchArea(cheatText);


		final Text timeCount = new Text(280, -60, this.mScoreFont, "Time Count: 0", 20, this.getVertexBufferObjectManager());
		timeCount.setScale(1.5f);
		timeLast = 15;
		timeCount.setText("Time Left: "+ timeLast);
		scene.attachChild(timeCount);
		

		myTimer = new TimerHandler(0.1f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO;
				String string = null;
				switch (Control.getColorTurn()) {
				case 1:
					string = "R";
					break;
				case 2:
					string = "Y";
					break;
				case 3:
					string = "B";
					break;
				case 4:
					string = "G";
					break;

				}
				if (Control.isDiceTurn()) {
					string += " - Dice";
				}
				else{
					string += " - Move";
				}
			
				colorText.setText(string);
				cheatText.setText("N=" + Control.getDiceNum());
			}
		});
		scene.registerUpdateHandler(myTimer);
		
		
		
		
		

		scene.setTouchAreaBindingOnActionDownEnabled(true);

		
		
		
		mainHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					
					break;
					
				case 1:// timer text
					   // local's turn 1 second pass
					if (timeLast == 0) {
						timeCount.setText("Time Out, T_T");
						timeLast = 15;
						if (timer != null) {
							timer.cancel();
							timer = null;
						}
						if (timerTask != null) {
							timerTask = null;
						}
					} else {
						timeLast--;
						timeCount.setText("Time Left: " + timeLast);
					}
					break;
					
				case 2:// local's turn begin
					{
						Log.i("timer2", "1");
						if( timer != null ){
							timer.cancel();
							Log.i("timer2", "2 1");
							timer = null;
							Log.i("timer2", "2 2");
						}
						timeLast = 15;
						Log.i("timer2", "3");
						timer = new Timer();
						Log.i("timer2", "4");
						
						timerTask = new TimerTask() {							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								mainHandler.sendEmptyMessage(1);
							}
						};
						
						timer.schedule(timerTask, 0, 1000); 
						Log.i("timer2", "5");
						
					}
					break;
					
				case 3:// other's turn begin
					timeCount.setText("Waiting Now..");
					timeLast = 15;
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (timerTask != null) {
						timerTask = null;
					}
					break;
					
				case 4:
				case 5:
					doGameLogic(msg);
					break;
					
				case 10:
					MsgInfo msgInfo = (MsgInfo)msg.obj;
					autoDotheMove(msgInfo);

					if (Control.getColorTurn() == localGameColor) {
						
						Log.i("autoDotheMove", "sendEmptyMessage(2222)");
						
						mainHandler.sendEmptyMessage(2);
						
					}
					
					Control.complete = true;
					break;
					
				default:
					break;
				}
			};
		};

		
		myThread = new Thread(new Runnable(){
			@Override
			public void run(){
				
				while(mainHandler== null);
				
				while(!Control.gameOver){
				
					while (Control.complete != true )
						; // out circle only when true
					
					Log.i("myThread", "Control.complete == true");
					
					Control.complete = false;
					
					if(Control.getColorTurn() != localGameColor){
						
						Log.i( "myThread","sendEmptyMessage(3333)");
						mainHandler.sendEmptyMessage(3);
						
						getMsgThread = new GetThread(appUtil, mainHandler);
						getMsgThread.start();
						
						try {
							Thread.sleep(2000);
							myDice.stopAnimation(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					else{
						
						Log.i( "myThread","fake sendEmptyMessage(2222)");
						
						//mainHandler.sendEmptyMessage(2);
					}
					
					
				}
			}
		});
		myThread.start();
		
		
		
		
		
		
		//mainHandler.sendEmptyMessage(2);
		Control.setDiceTurn();
		Control.complete = true;
		return scene;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	protected void doGameLogic(Message msg) {
		// TODO Auto-generated method stub
		Log.i("GameView", "doGameLogic");

		MsgInfo tempMsg = (MsgInfo) msg.obj;
		
		if(tempMsg.countNum != Control.progressCnt)
			return;
		
		
		if (msg.what == 4 && Control.isDiceTurn()) {

			myDice.animate(new long[] { 150, 150, 150, 150, 150, 150 }, 
					new int[] { 4, 2, 6, 5, 1, 3 },
					true);

			try {
				Thread.sleep(400 + new Random().nextInt(500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int diceNum = tempMsg.lenNum;// new Random().nextInt(6) + 1;

			myDice.stopAnimation(diceNum);
			Control.setDiceNum(diceNum);
			
			int myOnWayCnt = 0;
			switch (Control.getColorTurn()) {
			case 1:
				myOnWayCnt = Control.redOnWayCnt;
				break;
			case 2:
				myOnWayCnt = Control.yellowOnWayCnt;
				break;
			case 3:
				myOnWayCnt = Control.blueOnWayCnt;
				break;
			case 4:
				myOnWayCnt = Control.greenOnWayCnt;
				break;
			}
			
			Log.i("myOnWayCnt", ""+tempMsg.colorNum +" "+myOnWayCnt);
			
			if (myOnWayCnt == 0 ) {
				
				if(Control.isToReadyNum(diceNum)){	// no valid plane but able to get off
					
					switch (Control.getColorTurn()) {
					case 1:
						Control.redOnWayCnt+=1;
						break;
					case 2:
						Control.yellowOnWayCnt+=1;
						break;
					case 3:
						Control.blueOnWayCnt+=1;
						break;
					case 4:
						Control.greenOnWayCnt+=1;
						break;
					}
					Control.setFlyTurn();
				}
				else{	// can not get off
					Log.i("goNextColorTurn", "dice 1");
					
					// inform other that this this guy (me) is skipped
					// TODO
					Log.i("send empty", "pgs = "+Control.progressCnt+" "+localGameColor+" "+0+" "+diceNum);
					
					sendMsgThread = new SendThread(roomNum,Control.progressCnt,
							localGameColor,tempMsg.seqNum,diceNum,appUtil);sendMsgThread.start();

							
					Control.goNextColorTurn();
					Control.setDiceTurn();
					Control.progressCnt += 1;
					Control.complete = true;
				}
				
			}
			else{
				Control.setFlyTurn();
			}						
			
		}	//end of what == 4 
		
		else if (msg.what == 5 && Control.isFlyTurn()) {
			
			PlaneSprite planeSprite = null;
			switch (tempMsg.colorNum) {
			case 1:
				planeSprite = redPlaneSprite[tempMsg.seqNum - 1];
				break;
			case 2:
				planeSprite = yellowPlaneSprite[tempMsg.seqNum - 1];
				break;
			case 3:
				planeSprite = bluePlaneSprite[tempMsg.seqNum - 1];
				break;
			case 4:
				planeSprite = greenPlaneSprite[tempMsg.seqNum - 1];
				break;
			}
			int diceNum = tempMsg.lenNum;
			if (planeSprite.currentIndex == Control.wayLength-1) {
				return;
			}
			if (planeSprite.currentIndex == Control.startIndex) { // at the start point
				
				if (Control.isToReadyNum(diceNum)) { 	// valid planes, on start point
					planeSprite.moveByNum(1);	// move 1 step 
					
					
					Log.i("send move", "pgs = "+Control.progressCnt+" "+localGameColor+" "+tempMsg.seqNum+" "+diceNum);
					
					sendMsgThread = new SendThread(roomNum,Control.progressCnt,
							localGameColor,tempMsg.seqNum,diceNum,appUtil);
					sendMsgThread.start();
					
					Control.progressCnt += 1;
					
					
				} else { 	// touch a plane at start point, but the dice number
							// can't get it to ready point
					return;
				}
			} else { // not at start point

				planeSprite.moveByNum(diceNum);
				
				// inform other that this this guy (me) make the fly
				// TODO
				Log.i("send move", "pgs = "+Control.progressCnt+" "+localGameColor+" "+tempMsg.seqNum+" "+diceNum);
				
				sendMsgThread = new SendThread(roomNum,Control.progressCnt,
						localGameColor,tempMsg.seqNum,diceNum,appUtil);
				sendMsgThread.start();
				
				Control.progressCnt += 1;
			}
			if (!Control.isDiceAgainNum(diceNum)) {
				Control.goNextColorTurn();
				Control.complete = true;
				Log.i("goNextColorTurn", "fly 1");
			}
			Control.setDiceTurn();
			
		}//end of what == 5
		
	}

	
	
	
	
	
	
	private void autoDotheMove(MsgInfo tempMsg) {
		// TODO Auto-generated method stub
	
		// dice
		myDice.animate(new long[] { 150, 150, 150, 150, 150, 150 }, new int[] { 4, 2, 6, 5, 1, 3 }, true);
	
		try {
			Thread.sleep(400 + new Random().nextInt(500));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int msgLen = tempMsg.lenNum;	// new Random().nextInt(6) + 1;
		
		myDice.stopAnimation(msgLen);
		Control.setDiceNum(msgLen);
		
		int myOnWayCnt = 0;
		switch (tempMsg.colorNum) {
		case 1:
			myOnWayCnt = Control.redOnWayCnt;
			break;
		case 2:
			myOnWayCnt = Control.yellowOnWayCnt;
			break;
		case 3:
			myOnWayCnt = Control.blueOnWayCnt;
			break;
		case 4:
			myOnWayCnt = Control.greenOnWayCnt;
			break;
		}
		if (myOnWayCnt == 0 ) {
			if(Control.isToReadyNum(msgLen)){// no valid plane on way and new ones can't get off
				
				switch (Control.getColorTurn()) {
				case 1:
					Control.redOnWayCnt+=1;
					break;
				case 2:
					Control.yellowOnWayCnt+=1;
					break;
				case 3:
					Control.blueOnWayCnt+=1;
					break;
				case 4:
					Control.greenOnWayCnt+=1;
					break;
				}
				Control.setFlyTurn();
			}
			else{
				Control.goNextColorTurn();
				Log.i("goNextColorTurn", "dice 2");
				Control.setDiceTurn();
				
				// inform other that this this guy (me) is skipped
				// TODO
				Control.progressCnt++;
				return;
			}
			
		}
		else{
			Control.setFlyTurn();
		}	
		
		
		try {
			Thread.sleep(400 + new Random().nextInt(1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// fly
		PlaneSprite planeSprite = null;
		
		if(tempMsg.seqNum>4 || tempMsg.seqNum<0)
		{
			Log.i("index out", ">4? <0?");
			return;
		}
	
		switch (tempMsg.colorNum) {
		case 1:
			planeSprite = redPlaneSprite[tempMsg.seqNum - 1];
			break;
		case 2:
			planeSprite = yellowPlaneSprite[tempMsg.seqNum - 1];
			break;
		case 3:
			planeSprite = bluePlaneSprite[tempMsg.seqNum - 1];
			break;
		case 4:
			planeSprite = greenPlaneSprite[tempMsg.seqNum - 1];
			break;
		}
		
		int diceNum = tempMsg.lenNum;
		
		if (planeSprite.currentIndex == Control.wayLength-1) {
			return;
		}
		if (planeSprite.currentIndex == Control.startIndex) { // at the start point
			if (Control.isToReadyNum(diceNum)) { 	// valid planes, at start point
				
				planeSprite.moveByNum(1);	// move 1 step 
				
				Control.progressCnt++;
				
			} else { 	// touch a plane at start point, but the dice number
						// invalid to get it ready
				return;
			}
		} else { // not at start point

			planeSprite.moveByNum(diceNum);
			Control.progressCnt++;
			Log.i("goNextColorTurn", "fly 2");
			
		
		}
		if (!Control.isDiceAgainNum(diceNum)) {
			Control.goNextColorTurn();			
		}
		// if other player should again dice, still should wait for next MESSAGE
		Control.setDiceTurn();		
		
		
		
		
	}

	
	
	
	
	
	
	public static void checkCrushBackHome(int color,int idx) {
	
		Log.i("checkCrushBackHome", "start");
		int phyIdx = Control.getPhyIndex(idx, color);
		
		Log.i("checkCrushBackHome", "color= "+color+" "+phyIdx);
		
		for (PlaneSprite it : redPlaneSprite) {
			if (it.colorType != color && it.currentIndex >= 0) {
				if (phyIdx == Control.getPhyIndex(it)) {
					
					Log.i("redPlaneSprite", "back "+it.seqNum);
					
					it.moveBackHome();
					Control.redOnWayCnt--;
				}
			}
	
		}
	
		for (PlaneSprite it : yellowPlaneSprite) {
			if (it.colorType != color && it.currentIndex >= 0) {
				if (phyIdx == Control.getPhyIndex(it)) {
					
					Log.i("yellowPlaneSprite", "back "+it.seqNum);
					
					it.moveBackHome();
					Control.yellowOnWayCnt--;
				}
			}
	
		}
	
		for (PlaneSprite it : bluePlaneSprite) {
			if (it.colorType != color && it.currentIndex >= 0) {
				if (phyIdx == Control.getPhyIndex(it)) {
					
					Log.i("bluePlaneSprite", "back "+it.seqNum);
					
					it.moveBackHome();
					Control.blueOnWayCnt--;
				}
			}
	
		}
	
		for (PlaneSprite it : greenPlaneSprite) {
			if (it.colorType != color && it.currentIndex >= 0) {
				if (phyIdx == Control.getPhyIndex(it)) {
					
					Log.i("greenPlaneSprite", "back "+it.seqNum);
					
					it.moveBackHome();
					Control.greenOnWayCnt--;
				}
			}
	
		}
	
	}

}