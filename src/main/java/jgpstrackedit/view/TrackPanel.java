package jgpstrackedit.view;

import javax.swing.*;
import java.awt.*;

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
