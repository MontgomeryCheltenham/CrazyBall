package crazyball;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class Nuisances extends JPanel{
	int stand, x, y, yy, h, w;
	String type;
	Image img, imgBird1, imgBird2, imgBird3, imgBird4;
	boolean goesRight;
	
	public Nuisances(String type){
		this.type = type;
		x = 0; yy = (int)(Math.random()*50)+41;
		imgBird1 = Toolkit.getDefaultToolkit().getImage("res/flappy-bird.png");
		imgBird2 = Toolkit.getDefaultToolkit().getImage("res/flappy2.png");
		imgBird3 = Toolkit.getDefaultToolkit().getImage("res/flappy3.png");
		imgBird4 = Toolkit.getDefaultToolkit().getImage("res/flappy4.png");
		if(type.equals("bird")){ img = imgBird1; goesRight = true; w=35; h=50;}
	}
	public void update(){
		y = CBGame.gameY-Rect.getH()*CBGame.rects1.size()-yy;
		if(goesRight){ x+=1; } 
		if(x>=CBGame.gameX){ goesRight = false; }
		if(!goesRight) {x-=1; }
		if(x<-80){ goesRight = true; }
		if(goesRight && Math.floorMod(x, 20)>10){ img = imgBird1; } 
		if(goesRight && Math.floorMod(x, 20)<=10){ img = imgBird2; }
		if(!goesRight && Math.floorMod(x, 20)>10){ img = imgBird3; } 
		if(!goesRight && Math.floorMod(x, 20)<=10){ img = imgBird4; }
	}
	public int getX() { return x; } public int getY() { return y; }
	public int getW() { return w; } public int getH() { return h; }
	
	public void paint(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.drawImage(img, x, y, this);
	}
}
