package jgpstrackedit.test;

import jgpstrackedit.map.OSMTileManager;
import jgpstrackedit.map.TileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private static final Logger logger = LoggerFactory.getLogger(TileMangerTester.class);

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
					logger.error("Exception while running TileMangerTester!", e);
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
