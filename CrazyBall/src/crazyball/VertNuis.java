package crazyball;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class VertNuis extends JPanel{
	private int stand, x, y, w, h; String type;
	Image img, imgLeg, imgEarth;
	boolean moveUp, goOut;
	
	public VertNuis(String type, int stand){
		y = -h;
		this.type = type;
		this.stand = stand;
		imgLeg = Toolkit.getDefaultToolkit().getImage("res/leg.png");
		imgEarth = Toolkit.getDefaultToolkit().getImage("res/earth.jpg");
	}
	public void update(){
		x = stand*Rect.getW()-Rect.getW()/2-w/2;
		if(type.equals("leg")){ img = imgLeg; w=50; h=50;}
		if(type.equals("redBall")){ img = imgEarth; w=20; h=20; } 
		if(y<-h){goOut=true;}
		if(y+h>=CBGame.gameY-Rect.getH()*CBGame.rects1.size()){
			if(type.equals("redBall")) {goOut=true; } else {moveUp=true; }}
		if(!moveUp){y++;} if(moveUp){y--;}
	}
	public int getStand() { return stand; } public int getY() { return y; }
	public int getW() { return w; } public int getH() { return h; }
	public String getType() { return type; }
	public boolean isOut() { return goOut; }
	
	public void paint(Graphics g){
		super.paintComponent(g);
		if(type.equals("leg")){ g.drawImage(imgLeg, x, y, this); }
		if(type.equals("redBall")){ g.setColor(Color.RED);
			g.fillOval(x, y, w, h); }
	}
}
