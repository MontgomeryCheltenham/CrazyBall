package crazyball;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;



@SuppressWarnings("serial")
public class CBGame extends JPanel implements Runnable{
	static int gameX, gameY; int nofColors, level, score; int dy;
	Ball ball = new Ball();
	public static ArrayList<Color> colors;
	public static ArrayList<Rect> rects1, rects2, rects3, rects4, rects5;
	public static ArrayList<ArrayList<Rect>> stands;
	public static ArrayList<Nuisances> nuis;
	public static ArrayList<VertNuis> nuisVert;
	Timer timer; TimerTask taskAddRects, taskBird, taskLeg, taskRedBall;
	ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	static boolean running;
	private boolean levelUp, drawClown, drawGameOver, drawPlus100, drawPlus500;
	private boolean qq;
	static long startTime, elapsedTime;
	Image imgSadClown;
	Color bgColor;
		
	public CBGame(){
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "P");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Q");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), "Y");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "N");
		ActionMap am = getActionMap();
		am.put("Left", new KeyPressedAction("Left"));
	    am.put("Right", new KeyPressedAction("Right"));
	    am.put("Up", new KeyPressedAction("Up"));
	    am.put("Down", new KeyPressedAction("Down"));
	    am.put("P", new KeyPressedAction("P"));
	    am.put("Q", new KeyPressedAction("Q"));
	    am.put("Y", new KeyPressedAction("Y"));
	    am.put("N", new KeyPressedAction("N"));
		
	    nofColors = 5; level = 1;
		gameX=500; gameY=300;
		bgColor = Color.LIGHT_GRAY; setBackground(bgColor);
		setPreferredSize(new Dimension(gameX,gameY));
		drawGameOver = false;
		imgSadClown = Toolkit.getDefaultToolkit().getImage("res/sadclown.png");
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
		nuis = new ArrayList<Nuisances>();
		nuisVert = new ArrayList<VertNuis>();
		
		startTime = System.nanoTime();
		adjustColors(); rectSetup(); 
		run();
	}
	public void rectSetup(){
		for(int i=0; i<5; i++){
			rects1.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors)))); 
			rects2.add(new Rect(1, i, colors.get((int)(Math.random()*nofColors)))); 
			rects3.add(new Rect(2, i, colors.get((int)(Math.random()*nofColors))));
			rects4.add(new Rect(3, i, colors.get((int)(Math.random()*nofColors))));
			rects5.add(new Rect(4, i, colors.get((int)(Math.random()*nofColors))));
		}
		for(int i=0; i<rects1.size(); i++){ //no same color rects next to each other
			for(int j=0; j<rects2.size(); j++){
				if((rects1.get(i).getLayer()==rects2.get(j).getLayer())){
					while(rects1.get(i).getColor()==rects2.get(j).getColor()){
						rects2.get(j).setColor(colors.get((int)(Math.random()*nofColors)));}
				}
			}
		}
		for(int i=0; i<rects2.size(); i++){
			for(int j=0; j<rects3.size(); j++){
				if((rects2.get(i).getLayer()==rects3.get(j).getLayer())){
					while(rects2.get(i).getColor()==rects3.get(j).getColor()){
						rects3.get(j).setColor(colors.get((int)(Math.random()*nofColors)));}
				}
			}
		}
		for(int i=0; i<rects3.size(); i++){
			for(int j=0; j<rects4.size(); j++){
				if((rects3.get(i).getLayer()==rects4.get(j).getLayer())){
					while(rects3.get(i).getColor()==rects4.get(j).getColor()){
						rects4.get(j).setColor(colors.get((int)(Math.random()*nofColors)));}
				}
			}
		}
		for(int i=0; i<rects4.size(); i++){
			for(int j=0; j<rects5.size(); j++){
				if((rects4.get(i).getLayer()==rects5.get(j).getLayer())){
					while(rects4.get(i).getColor()==rects5.get(j).getColor()){
						rects5.get(j).setColor(colors.get((int)(Math.random()*nofColors)));}
				}
			}
		}
	}
	public void run(){ 
		protectBall(2);
		taskAddRects = new TimerTask(){
			public void run(){
				try { drawPlus500=true;
					while(rects5.size()<5){
						for(int i=0; i<5; i++){ 
							Thread.sleep(100);  rects1.add(new Rect(0, i, colors.get((int)(Math.random()*nofColors))));
							Thread.sleep(100);  rects2.add(new Rect(1, i, colors.get((int)(Math.random()*nofColors)))); 
							Thread.sleep(100);  rects3.add(new Rect(2, i, colors.get((int)(Math.random()*nofColors))));
							Thread.sleep(100);  rects4.add(new Rect(3, i, colors.get((int)(Math.random()*nofColors))));
							Thread.sleep(100);  rects5.add(new Rect(4, i, colors.get((int)(Math.random()*nofColors))));}
					}
					score+=500; drawPlus500=false; //scores - all rows
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
		};
		taskBird = new TimerTask(){
			public void run(){ nuis.add(new Nuisances("bird")); }
		};
		taskLeg = new TimerTask(){
			public void run(){ nuisVert.add(new VertNuis("leg", (int)(Math.random()*5)+1)); }
		};
		taskRedBall = new TimerTask(){
			public void run(){ nuisVert.add(new VertNuis("redBall", (int)(Math.random()*5)+1)); }
		};
		timer = new Timer(10, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				elapsedTime = (System.nanoTime() - startTime)/(long)Math.pow((long)10,(long)9);
				running = true; 
				ball.update();
				Iterator<Nuisances> iter = nuis.iterator();
				while(iter.hasNext()){
					Nuisances n = iter.next();
					n.update();
				}
				Iterator<VertNuis> iter1 = nuisVert.iterator();
				while(iter1.hasNext()){
					VertNuis vn = iter1.next();
					vn.update();
					if(vn.isOut()) { iter1.remove(); }
				}
				if(drawPlus100||drawPlus500) { dy++; } else { dy=0; }
				if(ball.getLives()==0){ gameOver(); }
				contact(); compare(); collision();
				repaint();
			}
		}); timer.start();
		ses.schedule(taskBird, 500, TimeUnit.MILLISECONDS); 
		ses.scheduleAtFixedRate(taskLeg, 5, 7, TimeUnit.SECONDS); 
		ses.scheduleAtFixedRate(taskRedBall, 10, 30, TimeUnit.SECONDS);
		
	}
	public void contact(){
		if(rects1.size()>0){
		int s1 = rects1.size()-1;
		if(ball.getStand()==1 && rects1.get(s1).getY()<=ball.getY()+ball.r*2){
			Color c = rects1.get(s1).getColor();
			if(c==colors.get(nofColors-1)){ rects1.get(s1).setColor(colors.get(0));}
			else {rects1.get(s1).setColor(colors.get(colors.indexOf(c)+1));}
		}}if(rects2.size()>0){
		int s2 = rects2.size()-1;
		if(ball.getStand()==2 && rects2.get(s2).getY()<=ball.getY()+ball.r*2){
			Color c = rects2.get(s2).getColor();
			if(c==colors.get(nofColors-1)){ rects2.get(s2).setColor(colors.get(0));}
			else {rects2.get(s2).setColor(colors.get(colors.indexOf(c)+1));}
		}}if(rects3.size()>0){
		int s3 = rects3.size()-1; if(s3<0){s3=0;}
		if(ball.getStand()==3 && rects3.get(s3).getY()<=ball.getY()+ball.r*2){
			Color c = rects3.get(s3).getColor();
			if(c==colors.get(nofColors-1)){ rects3.get(s3).setColor(colors.get(0));}
			else {rects3.get(s3).setColor(colors.get(colors.indexOf(c)+1));}
		}}if(rects4.size()>0){
		int s4 = rects4.size()-1; if(s4<0){s4=0;}
		if(ball.getStand()==4 && rects4.get(s4).getY()<=ball.getY()+ball.r*2){
			Color c = rects4.get(s4).getColor();
			if(c==colors.get(nofColors-1)){ rects4.get(s4).setColor(colors.get(0));}
			else {rects4.get(s4).setColor(colors.get(colors.indexOf(c)+1));}
		}}if(rects5.size()>0){
		int s5 = rects5.size()-1; if(s5<0){s5=0;}
		if(ball.getStand()==5 && rects5.get(s5).getY()<=ball.getY()+ball.r*2){
			Color c = rects5.get(s5).getColor();
			if(c==colors.get(nofColors-1)){ rects5.get(s5).setColor(colors.get(0));}
			else {rects5.get(s5).setColor(colors.get(colors.indexOf(c)+1));}
		}}
		// redball - rect ->chg color
		for(int i =0; i<nuisVert.size(); i++){
			if(nuisVert.get(i).getType().equals("redBall")){ 
				if(rects1.size()>0){
					int s1 = rects1.size()-1; if(s1<0){s1=0;}
					if(nuisVert.get(i).getStand()==1 && rects1.get(s1).getY()<=nuisVert.get(i).getY()+nuisVert.get(i).getH()){
						Color c = rects1.get(s1).getColor();
						if(c==colors.get(nofColors-1)){ rects1.get(s1).setColor(colors.get(0));}
						else {rects1.get(s1).setColor(colors.get(colors.indexOf(c)+1));}
					}}if(rects2.size()>0){
					int s2 = rects2.size()-1; if(s2<0){s2=0;}
					if(nuisVert.get(i).getStand()==2 && rects2.get(s2).getY()<=nuisVert.get(i).getY()+nuisVert.get(i).getH()){
						Color c = rects2.get(s2).getColor();
						if(c==colors.get(nofColors-1)){ rects2.get(s2).setColor(colors.get(0));}
						else {rects2.get(s2).setColor(colors.get(colors.indexOf(c)+1));}
					}}if(rects3.size()>0){
					int s3 = rects3.size()-1; if(s3<0){s3=0;}
					if(nuisVert.get(i).getStand()==3 && rects3.get(s3).getY()<=nuisVert.get(i).getY()+nuisVert.get(i).getH()){
						Color c = rects3.get(s3).getColor();
						if(c==colors.get(nofColors-1)){ rects3.get(s3).setColor(colors.get(0));}
						else {rects3.get(s3).setColor(colors.get(colors.indexOf(c)+1));}
					}}if(rects4.size()>0){
					int s4 = rects4.size()-1; if(s4<0){s4=0;}
					if(nuisVert.get(i).getStand()==4 && rects4.get(s4).getY()<=nuisVert.get(i).getY()+nuisVert.get(i).getH()){
						Color c = rects4.get(s4).getColor();
						if(c==colors.get(nofColors-1)){ rects4.get(s4).setColor(colors.get(0));}
						else {rects4.get(s4).setColor(colors.get(colors.indexOf(c)+1));}
					}}if(rects5.size()>0){
					int s5 = rects5.size()-1; if(s5<0){s5=0;}
					if(nuisVert.get(i).getStand()==5 && rects5.get(s5).getY()<=nuisVert.get(i).getY()+nuisVert.get(i).getH()){
						Color c = rects5.get(s5).getColor();
						if(c==colors.get(nofColors-1)){ rects5.get(s5).setColor(colors.get(0));}
						else {rects5.get(s5).setColor(colors.get(colors.indexOf(c)+1));}
					}}
			}
		}
	}
	public void adjustColors(){
		for (int i = 0; i < colors.size(); i++) { //eliminate similar colours
			for (int j = i+1; j < colors.size(); j++) {
//				System.out.println(i+" "+colors.get(i).getRed()+" "
//	    				+" "+colors.get(i).getGreen()+" "+colors.get(i).getBlue()+" "+j+" "+colors.get(j).getRed()+" "+colors.get(j).getGreen()+" "+colors.get(j).getBlue());
				int redI = colors.get(i).getRed(); int redJ = colors.get(j).getRed();
				int greenI = colors.get(i).getGreen(); int greenJ = colors.get(j).getGreen();
				int blueI = colors.get(i).getBlue(); int blueJ = colors.get(j).getBlue();
			    if((Math.abs(redI-redJ)<=20 && Math.abs(greenI-greenJ)<=20 && Math.abs(blueI-blueJ)<=40)
			    	||(Math.abs(redI-redJ)<=20 && Math.abs(blueI-blueJ)<=20 && Math.abs(greenI-greenJ)<=40)
			    	||(Math.abs(blueI-blueJ)<=20 && Math.abs(greenI-greenJ)<=20 && Math.abs(redI-redJ)<=40)
			    		){
			    	System.out.println(i+" "+colors.get(i).getRed()+" "
		    				+" "+colors.get(i).getGreen()+" "+colors.get(i).getBlue()+" "+j+" "+colors.get(j).getRed()+" "+colors.get(j).getGreen()+" "+colors.get(j).getBlue());	
			    	colors.set(j, new Color((int)(Math.random()*255)+1,(int)(Math.random()*255)+1,(int)(Math.random()*255)+1));
			    	System.out.println("kala chgd");
			    	System.out.println(i+" "+colors.get(i).getRed()+" "
		    				+" "+colors.get(i).getGreen()+" "+colors.get(i).getBlue()+" "+j+" "+colors.get(j).getRed()+" "+colors.get(j).getGreen()+" "+colors.get(j).getBlue());
			    }
			}
		}
	}
	public void compare(){
		if(rects5.size()>0){
			if(rects1.get(rects1.size()-1).getColor()==rects2.get(rects2.size()-1).getColor()
					&& rects2.get(rects2.size()-1).getColor()==	rects3.get(rects3.size()-1).getColor()
					&& rects3.get(rects3.size()-1).getColor()==	rects4.get(rects4.size()-1).getColor()
					&& rects4.get(rects4.size()-1).getColor()==	rects5.get(rects5.size()-1).getColor()){
				Iterator<Rect> iter1 = rects1.iterator(); Rect r1 = iter1.next();
				Iterator<Rect> iter2 = rects2.iterator(); Rect r2 = iter2.next();
				Iterator<Rect> iter3 = rects3.iterator(); Rect r3 = iter3.next();
				Iterator<Rect> iter4 = rects4.iterator(); Rect r4 = iter4.next();
				Iterator<Rect> iter5 = rects5.iterator(); Rect r5 = iter5.next();
				rects1.remove(rects1.size()-1);
				rects2.remove(rects2.size()-1);
				rects3.remove(rects3.size()-1);
				rects4.remove(rects4.size()-1);
				rects5.remove(rects5.size()-1);
				score+=100; //scores - row
				Thread localThread = new Thread(new Runnable(){
					public void run(){ drawPlus100=true;
						try { Thread.sleep(2500); drawPlus100=false; 
						}catch (InterruptedException e) { e.printStackTrace(); }
					}
				});
				localThread.start(); 
			}
			levelUp=false;
		}else if(rects1.size()==0){ // level up 
			levelUp();
		}
	}
	public void collision(){
		if(!ball.isDeadProof()){
			int x = ball.getX(); int y = ball.getY(); int r = ball.getR();
			for(int i =0;i<nuis.size();i++){
				int nx = nuis.get(i).getX(); int ny = nuis.get(i).getY();
				int nw = nuis.get(i).getW(); int nh = nuis.get(i).getH();
				if(x+2*r>nx && x<nx+nw && y+2*r>ny && y<ny+nh){ 
					ball.updateLives(-1); protectBall(2); troll();
				}
			}
			for(int i =0;i<nuisVert.size(); i++){
				int ns = nuisVert.get(i).getStand(); int ny = nuisVert.get(i).getY();
				int nh = nuisVert.get(i).getH();
				if(ns==ball.getStand() && y+2*r>ny && y<ny+nh && !nuisVert.get(i).getType().equals("redBall")){
					ball.updateLives(-1); protectBall(2); troll();
				}
			}
		}
	}
	public void levelUp(){
		if(!levelUp){
			nofColors++; level++;
			colors.add(new Color((int)(Math.random()*255)+1,(int)(Math.random()*255)+1,(int)(Math.random()*255)+1));
			removeAllNuis(); 
			adjustColors();
			ses.schedule(taskAddRects, 100, TimeUnit.MILLISECONDS);
			taskAddRects.cancel();
			ball.updateLives(1); protectBall(4);
			ball.setStand(3); ball.setY(110);
			ses.scheduleAtFixedRate(taskLeg, 3, 10*level, TimeUnit.SECONDS);
			if(Math.floorMod(level, 5)==0){
				ses.schedule(taskBird, 1200, TimeUnit.MILLISECONDS);
			}
			levelUp=true;
		}
	}
	public void gameOver(){
		running = false; timer.stop(); drawGameOver = true; 
		taskLeg.cancel(); taskRedBall.cancel(); taskBird.cancel();
		removeAllNuis(); 
	}
	public void removeAllNuis(){
		Iterator<VertNuis> iter = nuisVert.iterator();
		while(iter.hasNext()){ VertNuis vn = iter.next(); vn.removeAll(); }
		Iterator<Nuisances> iter1 = nuis.iterator();
		while(iter1.hasNext()){ Nuisances n = iter1.next(); n.removeAll(); }
	}
	public void troll(){
		timer.stop();
		drawClown = true; 
		ses.schedule(new TimerTask(){
			public void run(){ drawClown = false; timer.start(); }
		}, 150, TimeUnit.MILLISECONDS);
	}
	public void protectBall(int time){
		if(ball.getLives()>0) {ball.setDeadProof(true); }
		ses.schedule(new TimerTask(){
			public void run(){ ball.setDeadProof(false); }
		}, time, TimeUnit.SECONDS);
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		for(int i=0; i<rects1.size(); i++){ rects1.get(i).paint(g); }
		for(int i=0; i<rects2.size(); i++){ rects2.get(i).paint(g); }
		for(int i=0; i<rects3.size(); i++){ rects3.get(i).paint(g); }
		for(int i=0; i<rects4.size(); i++){ rects4.get(i).paint(g); }
		for(int i=0; i<rects5.size(); i++){ rects5.get(i).paint(g); }
		ball.paint(g);
		for(int i=0; i<nofColors; i++){
			g.setColor(colors.get(i));
			g.fillRect(i*20 ,0, 20, 20);
		}
		for(int i=0; i<nuis.size(); i++){
			nuis.get(i).paint(g);
		}
		for(int i=0; i<nuisVert.size(); i++){
			nuisVert.get(i).paint(g);
		}
		for(int i=0; i<ball.getLives()-1; i++){
			g.setColor(Color.YELLOW);
			g.fillOval(gameX-i*20-ball.getR()-5, 5, ball.getR(), ball.getR());
		}
		g.drawString("Level: "+Integer.toString(level), gameX-50, 35);
		g.drawString("Score: "+Integer.toString(score), gameX-70, 55);
		if(drawPlus100){
			g.setColor(colors.get(1));
			g.drawString("+100", gameX*9/10, gameY-100-dy);}
		if(drawPlus500){
			g.setColor(colors.get(2));
			g.drawString("+500", gameX*19/20, gameY-90-dy);}
		if(drawClown){ g.drawImage(imgSadClown, 100, 0, this); }
		if(qq){
			g.setColor(colors.get((int)(Math.random()*nofColors)));
			g.fillRect(gameX*2/5, gameY/2-14, gameX/5, 20);
			g.setColor(Color.BLACK);
			g.drawString("Quit game? Y / N", gameX*2/5+2, gameY/2); 
		}
		if(drawGameOver){ 
			g.setColor(colors.get((int)(Math.random()*nofColors)));
			g.fillRect(gameX*2/5, gameY/2-14, gameX/5, 20);
			g.drawString("Score: "+Integer.toString(score), gameX*2/5+12, gameY/2+20);
			g.setColor(Color.BLACK);
			g.drawString("G A M E   O V E R", gameX*2/5+2, gameY/2); }
	}
	public class KeyPressedAction extends AbstractAction{
		private String cmd;
		public KeyPressedAction(String cmd){ this.cmd = cmd; }		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(cmd.equals("Left")){
				if(ball.getStand()!=1){ball.setStand(ball.getStand()-1);}
			}
			if(cmd.equals("Right")){
				if(ball.getStand()!=5){ball.setStand(ball.getStand()+1);}
			}
			if(cmd.equals("Up")){
				ball.jump();
			}
			if(cmd.equals("Down")){
				ball.duck();
			}
			if(cmd.equals("P")&&!qq){
				if(running) { timer.stop(); running = false;}
				else if(!running){ timer.start(); running=true; } 
			}
			if(cmd.equals("Q")){
				if(running) { qq=true; repaint(); timer.stop(); running = false; }
				else if(!running&&qq){ timer.start(); running=true; qq=false; }
				else if(!running&&!qq){ 
					CBMain.p.removeAll(); CBMain.p.revalidate(); CBMain.p.repaint();
					gameOver(); new CBMain();
				}
			}
			if(cmd.equals("Y")&&qq){
				CBMain.p.removeAll(); CBMain.p.revalidate(); CBMain.p.repaint();
				gameOver(); 
				new CBMain();
			}
			if(cmd.equals("N")&&qq){
				timer.start(); running=true; qq=false;
			}
		}
	}
}
