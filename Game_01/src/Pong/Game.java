package Pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

	public static JFrame frame;
	public static int WIDTH = 160;
	public static int HEIGHT = 120;
	public static int SCALE = 3;
	private boolean isRunning;
	private Thread thread;
	
	public static Player player;
	public static Enemy enemy;
	public static Ball ball;
	
	public BufferedImage layer;
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		this.addKeyListener(this);
		layer = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		player = new Player(100,HEIGHT-10);
		enemy = new Enemy(100, 0);
		ball = new Ball(100, HEIGHT/2 -1);
	}
	
	
	public static void main(String[]args) {
		Game game = new Game();
		frame = new JFrame("Pong");
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();

	}
	
	public void initFrame() {
		frame = new JFrame("Pong");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	
	
	public void tick() {
		player.tick();
		enemy.tick();
		ball.tick();
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = layer.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH,HEIGHT);
		player.render(g);
		enemy.render(g);
		ball.render(g);
		g = bs.getDrawGraphics();
		
		g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	
	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		int frames =0;
		double timer = System.currentTimeMillis();
			while(isRunning) {
				long now = System.nanoTime();
				delta += (now - lastTime)/ns;
				lastTime = now;
				if(delta>=1) {
					tick();
					render();
					frames++;
					delta--;
				}
				if(System.currentTimeMillis() - timer >= 1000) {
					//System.out.println("FPS: " + frames);
					frames = 0;
					timer+=1000;
		
				}

			}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
		} else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
			player.left = true;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		} else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
			player.left = false;
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
