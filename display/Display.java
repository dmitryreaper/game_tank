package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.awt.Dimension;
import javax.swing.JFrame;

import IO.Input;

public abstract class Display {
	
	private static boolean created = false;
	private static JFrame window;
	private static Canvas content;

	private static BufferedImage buffer;
	private static int[] bufferData;
	private static Graphics bufferGraphics;
	private static int clearColor;

	private static BufferStrategy bufferStrategy;

	private static float delta = 0;

	public static void create (int width, int height, String title, int _clearColor, int numBuffers) {
		if (created)
			return;

		window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = new Canvas();
		
		Dimension size = new Dimension(width, height);
		content.setPreferredSize(size);
		content.setBackground(Color.black);
		window.setResizable(false);
		window.getContentPane().add(content);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
		bufferGraphics = buffer.getGraphics();
		((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		clearColor = _clearColor;

		content.createBufferStrategy(numBuffers);
		bufferStrategy = content.getBufferStrategy();

		created = true;
	}

	public static void clear() {
		Arrays.fill(bufferData, clearColor);
	}

	public static void render() {
		bufferGraphics.setColor(new Color(0xff0000ff));
		bufferGraphics.fillOval((int)(350 + (Math.sin(delta) * 300)), 250, 100, 100);
		bufferGraphics.fillOval((int)(600 + (Math.sin(delta) * 200)), 250, 100, 100);

		delta += 0.02f;
	}

	public static void swapBuffers() {
		Graphics g = bufferStrategy.getDrawGraphics();
		g.drawImage(buffer, 0, 0, null);
		bufferStrategy.show();
	}

	public static Graphics2D getGraphics() {
		return (Graphics2D) bufferGraphics;
	}

	public static void destroy() {
		if(!created)
			return;

		window.dispose();
	}

	public static void setTitle(String title) {
		window.setTitle(title);
	}

	public static void addInputList(Input input) {
		window.add(input);
	}

}
