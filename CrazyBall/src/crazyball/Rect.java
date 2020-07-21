package crazyball;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class Rect extends JPanel{
	static int rectW, rectH;
	int stand, layer; Color color;
	
	public Rect(int stand, int layer, Color color){
		this.stand = stand; this.layer = layer; this.color=color;
		rectW = 100; rectH = 20;
	}
	public int getX(){ return stand*rectW; } 
	public int getY(){ return CBGame.gameY - rectH - layer*rectH; }
	public static int getW(){ return rectW; } public static int getH(){ return rectH; }
	public void setStand(int stand){ this.stand = stand; } 
	public void setLayer(int layer){ this.stand = layer; }
	public int getStand(){return stand; } public int getLayer(){return layer; }
	public Color getColor(){return color; }
	public void setColor(Color color){ this.color=color; }
	
	public void paint(Graphics g){
		super.paintComponent(g);
		g.setColor(color);
//		g.fillRect(stand*rectW, CBGame.gameY - rectH - layer*rectH, rectW, rectH);
		g.fillRect(stand*rectW, 300 - rectH - layer*rectH, rectW, rectH);
	}
}
