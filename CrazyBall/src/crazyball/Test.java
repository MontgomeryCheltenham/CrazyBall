package crazyball;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Test extends JPanel implements Runnable{
	static int gameX, gameY; int nofColors;
	public static ArrayList<Color> colors;
	public static ArrayList<Rect> rects1, rects2, rects3, rects4, rects5;
	public static ArrayList<ArrayList<Rect>> stands;
	Timer timer; TimerTask task;
	ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	static long startTime, elapsedTime;
	
	public Test(){
		
	    nofColors = 5;
		gameX=500; gameY=300;
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(gameX,gameY));
		colors = new ArrayList<Color>();
		for(int i=0; i<nofColors; i++){
			int rr, gg, bb;
			rr = (int)(Math.random()*255)+1;
			gg = (int)(Math.random()*255)+1;
			bb = (int)(Math.random()*255)+1;
			colors.add(new Color(rr,gg,bb));
		}
		
		rects1 = new ArrayList<Rect>(); rects2 = new ArrayList<Rect>(); rects3 = new ArrayList<Rect>();
		rects4 = new ArrayList<Rect>(); rects5 = new ArrayList<Rect>();
		stands = new ArrayList<ArrayList<Rect>>();
		
		startTime = System.nanoTime();
		run(); rectSetup();
	}
	public void rectSetup(){
		for(int i=0; i<5; i++){
//			rects1.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors))));
//			rects2.add(new Rect(1, i, colors.get((int)(Math.random()*nofColors)))); 
//			rects3.add(new Rect(2, i, colors.get((int)(Math.random()*nofColors))));
//			rects4.add(new Rect(3, i, colors.get((int)(Math.random()*nofColors))));
//			rects5.add(new Rect(4, i, colors.get((int)(Math.random()*nofColors))));
		}
	}
	public void run(){ 
		task = new TimerTask(){
			public void run(){
				try { for(int i=0; i<5; i++){ Thread.sleep(500);  rects1.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); }
				} catch (InterruptedException e) { e.printStackTrace(); }
				try { for(int i=0; i<5; i++){ Thread.sleep(500);  rects2.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); }
				} catch (InterruptedException e) { e.printStackTrace(); }
				try { for(int i=0; i<5; i++){ Thread.sleep(500);  rects3.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); }
				} catch (InterruptedException e) { e.printStackTrace(); }
				try { for(int i=0; i<5; i++){ Thread.sleep(500);  rects4.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); }
				} catch (InterruptedException e) { e.printStackTrace(); }
				try { for(int i=0; i<5; i++){ Thread.sleep(500);  rects5.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); }
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
		};
		timer = new Timer(10, new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				revalidate(); repaint();
			}
		}); timer.start();
		ses.schedule(task, 100, TimeUnit.MILLISECONDS);
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		for(int i=0; i<rects1.size(); i++){ rects1.get(i).paint(g); }
		for(int i=0; i<rects2.size(); i++){ rects2.get(i).paint(g); }
		for(int i=0; i<rects3.size(); i++){ rects3.get(i).paint(g); }
		for(int i=0; i<rects4.size(); i++){ rects4.get(i).paint(g); }
		for(int i=0; i<rects5.size(); i++){ rects5.get(i).paint(g); }
		for(int i=0; i<nofColors; i++){
			g.setColor(colors.get(i));
			g.fillRect(i*20 ,0, 20, 20);
		}
	}
	
}


