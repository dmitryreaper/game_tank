package game;

import java.awt.Color;
import java.awt.Graphics2D;

import IO.Input;
import Texture.*;
import display.Display;
import utils.Time;

public class Game implements Runnable {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String TITLE = "WINDOW";
	public static final int CLEAR_COLOR = 0xff000000;
	public static final int NUM_BUFFERS = 3;

	public static final float UPDATE_RATE = 60.0f;
	public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
	public static final long IDLE_TIME = 1;

	public static final String ATLAS_FILE_NAME = "texture.png";

	private boolean running;
	private Thread gameThread;

	private Graphics2D graphics;
	private Input input;
	private Texture atlas;
	private Player player;

	//private SpriteSheet sheet;
	//private Sprite sprite;

	//temp
	//float x = 350;
	//float y = 250;
	//float delta = 0;
	//float radius = 10;
	//float speed = 3;
	
	public Game(){
		running = false;
		Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
		graphics = Display.getGraphics();
		input = new Input();
		Display.addInputList(input);
		atlas = new Texture(ATLAS_FILE_NAME);
		player = new Player(300, 300, 1, 3, atlas);
		

		//sheet = new SpriteSheet(atlas.cut(2, 10, 125, 150), 1, 125); // sprite coordinate
		//sprite = new Sprite(sheet, 1);
				
	}

	public synchronized void start(){

		if(running)
			return;

		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	public synchronized void stop(){

		if(!running)
			return;

		running = false;
		try {
			gameThread.join();			
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		cleanUp();
	}

	private void update(){
		player.update(input);
	}

	private void render(){

		Display.clear();
		//graphics.setColor(Color.red); 
		//graphics.fillOval((int) (x + (Math.sin(delta) * 200)),(int)y, (int)radius * 2, (int)radius * 2);
		player.render(graphics);
		//graphics.drawImage(atlas.cut(0, 0, 100, 120), 300, 300, null);

		//sprite.render(graphics, x, y);
		Display.swapBuffers();
		
		
	}
	
	public void run(){

		int fps = 0;
		int upd = 0;
		int updl = 0;

		long count = 0;

		float delta = 0;

		long lastTime = Time.get();
		while(running){
			long now = Time.get();
			long elapsedTime = now - lastTime;
			lastTime = now;
			count += elapsedTime;
			boolean render = false;
			delta += (elapsedTime  / UPDATE_INTERVAL);

			while(delta > 1){
				update();
				upd++;
				
				delta--;
				if(render){
					updl++;
				}else{
					render = true;
				}
				render = true;
			}

			
			if(render){
				render();
				fps++;
				
			}else{
				try{
					Thread.sleep(IDLE_TIME);
				} catch (InterruptedException e){
					e.printStackTrace();
				}

			}

			if(count >= Time.SECOND){
				Display.setTitle(TITLE + " || Fps" + fps + " | Update:" + upd + " | Updl: " + updl);
				upd = 0;
				fps = 0;
				updl = 0;
				count = 0;
			}

		}
		
	}

	private void cleanUp(){
		Display.destroy();
	}
}
