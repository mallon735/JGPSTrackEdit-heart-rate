package jgpstrackedit.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.OSMTileManager;
import jgpstrackedit.map.util.MapObserver;
import jgpstrackedit.map.util.TileNumber;

/*
 * 
http://tile.openstreetmap.org/15/17683/11576.png
http://tile.openstreetmap.org/15/17683/11577.png
http://tile.openstreetmap.org/15/17684/11576.png
http://tile.openstreetmap.org/15/17684/11577.png
http://tile.openstreetmap.org/15/17685/11576.png
http://tile.openstreetmap.org/15/17685/11577.png
 */


public class TileMangerTester extends JFrame {

	private JPanel contentPane;
	private TileManager tileManager;
	private TestMapPanel testMapPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TileMangerTester frame = new TileMangerTester();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TileMangerTester() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		contentPane.add(btnTest, BorderLayout.NORTH);
		tileManager = new OSMTileManager();
		tileManager.open();
		testMapPanel = new TestMapPanel(tileManager);
		contentPane.add(testMapPanel,BorderLayout.CENTER);

	}


}
