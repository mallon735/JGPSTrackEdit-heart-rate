package jgpstrackedit.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import jgpstrackedit.international.International;

public class DlgAbout extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public DlgAbout() {
		setTitle("JGPSTrackEdit - "+International.getText("dlgabout.About"));
		setBounds(100, 100, 498, 470);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTextArea txtrJgpstrackeditc = new JTextArea();
			txtrJgpstrackeditc.setBounds(0, 0, 490, 403);
			txtrJgpstrackeditc.setBackground(new Color(211, 211, 211));
			txtrJgpstrackeditc.setFont(new Font("Arial", Font.PLAIN, 12));
			txtrJgpstrackeditc.setEditable(false);
			txtrJgpstrackeditc.setText("JGPSTrackEdit (c) 2012...2016 by Hubert Lutnik   (hubert.lutnik@htl-klu.at)\r\n\r\nRelease 1.6.1   15.02.2016\r\n\r\nUsage for non commercial purposes only.\r\nNo guaranties!\r\n\r\nThanks to GPSPrune for some ideas: \r\nhttp://activityworkshop.net/software/gpsprune/index.html\r\n\r\nThanks to Mark James for licensing the icons\r\nhttp://www.famfamfam.com/lab/icons/silk/\r\n\r\nSee also:\r\nhttp://www.gpsies.com\r\nhttp://hikebikemap.de/\r\nhttp://www.openstreetmap.org/\r\nhttp://hikebikemap.de/\r\nhttp://www.mapquest.com/\r\nhttp://maps.google.com/\r\nhttp://wiki.openstreetmap.org/wiki/Slippy_map_tilenames\r\nhttp://code.google.com/intl/de/apis/maps/documentation/staticmaps/\r\nhttp://code.google.com/intl/de/apis/maps/documentation/elevation/\r\nhttp://open.mapquestapi.com/directions/\r\nhttp://open.mapquestapi.com/staticmap/\r\n\r\n\r\n\r\n");
			contentPanel.add(txtrJgpstrackeditc);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				okButton.setActionCommand(International.getText("OK"));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
