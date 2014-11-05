package miner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GamePiece extends JLabel
{
	public GamePiece(String imageloc)
	{
		super(new ImageIcon(imageloc));
		//this.setIcon(new ImageIcon(imageloc));
	}
}
