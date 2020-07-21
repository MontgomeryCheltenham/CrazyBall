package crazyball;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class CBMain {
	static JFrame frm = new JFrame("CrazyBall");
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	static int W = dim.width; static int H = dim.height;
	JPanel pnlMenu = new JPanel(); static JPanel p = new JPanel();
	JButton btnStart = new JButton("start");
	JButton btnQuit = new JButton("quit"); 
	//JPanel pnlGame = new CBGame();
	
	public static void main(String[] args) {
		new CBMain(); 
	}
	public CBMain(){
		@SuppressWarnings("static-access")
		InputMap im = p.getInputMap(p.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Esc");
		ActionMap am = p.getActionMap();
	    am.put("Esc", new KeyPressedAction("Esc"));
		
		p.setPreferredSize(new Dimension(CBGame.gameX+100,CBGame.gameY+100));
		pnlMenu.add(btnStart); pnlMenu.add(btnQuit); p.add(pnlMenu);
		frm.add(p); frm.pack();
		frm.getRootPane().setDefaultButton(btnStart);
		btnStart.requestFocus();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocation(W/8, H/8);
		frm.setResizable(false); frm.setFocusable(true); frm.requestFocus();
		frm.setSize(600, 400);
		frm.setVisible(true);
		
		btnStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				p.remove(pnlMenu); p.add(new CBGame()); 
				p.validate(); p.repaint();
			}
		});
		btnQuit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
	}
	public class KeyPressedAction extends AbstractAction{
		private String cmd;
		public KeyPressedAction(String cmd){ this.cmd = cmd; }		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(cmd.equals("Enter")&&!CBGame.running){
				p.removeAll(); p.revalidate(); p.repaint();
				p.add(new CBGame());
			}
			if(cmd.equals("Esc")&&!CBGame.running){ System.exit(0); }
		}
	}
}
