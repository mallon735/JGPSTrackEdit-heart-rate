package jgpstrackedit.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class TrackPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public TrackPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelData = new JPanel();
		add(panelData, BorderLayout.NORTH);
		
		JPanel panelAttitudeProfile = new JPanel();
		add(panelAttitudeProfile, BorderLayout.CENTER);

	}

}
