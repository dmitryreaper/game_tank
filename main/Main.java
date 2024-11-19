package main;

import javax.swing.AbstractAction;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

import display.Display;

public class Main {
	public static void main(String[] args) {
		
		Display.create(800,600, "GAME");

		//fps
		Timer timer = new Timer( 1000 / 60, new AbstractAction() {
				public void actionPerformed(ActionEvent e ) {
					Display.render();
				}
			});
		timer.setRepeats(true);
		timer.start();
	}
		   
}
