package crazyball;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class Ball extends JPanel{
	int stand, bx, by, yy, r, lives;
	boolean moveUp, jump, deadProof;
	
	public Ball(){
		lives = 4; stand = 3; by=110; yy=100; r=10;
	}
	public int getStand() { return stand; }
	public void setStand(int stand) { this.stand = stand; }
	public int getX() { return bx; } public int getY() { return by; }
	public void setY(int y) { this.by = y; }
	public int getR() { return r; }
	public int getLives() { if(lives<0){return 0;} else {return lives; }}
	public void updateLives(int l) { lives+=l; }
	public boolean isDeadProof() { return deadProof; }
	public void setDeadProof(boolean proof){ this.deadProof = proof; }

	public void update(){
		bx = stand*Rect.getW()-Rect.getW()/2-r;  // rects1 -> all rects.size() equal
		if(by+r*2>=CBGame.gameY-Rect.getH()*CBGame.rects1.size()) { moveUp=true; }
		if(by<=CBGame.gameY-Rect.getH()*CBGame.rects1.size()-yy){ moveUp=false; yy=100; }
		if(moveUp){ by-=3; } if(!moveUp){ by+=3; }
		if(jump){ moveUp=true; yy=180; jump=!jump; }
	}
	public void jump(){ jump=true; }
	public void duck(){ 
		if(by+r*2>=CBGame.gameY-Rect.getH()*CBGame.rects1.size()) {
			by=CBGame.gameY-Rect.getH()*CBGame.rects1.size()-r*2;
		} else { by+=20; }
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		if(deadProof){ g.setColor(Color.GREEN); } else { g.setColor(Color.YELLOW); }
		g.fillOval(bx, by, r*2, r*2);
	}
}
