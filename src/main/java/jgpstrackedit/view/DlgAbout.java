package jgpstrackedit.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.jar.Manifest;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jgpstrackedit.international.International;

/**
 * About Dialog
 *
 */
public class DlgAbout extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private static final String BUILDSTRING = "Release 1.6.5 20190416";

	/**
	 * Create the dialog.
	 */
	public DlgAbout() {
		final Manifest manifest = getManifest();
		final String buildTime = 
				Optional.ofNullable(manifest.getMainAttributes().getValue("Build-Time")).orElse("");
		final String buildVersion =
				Optional.ofNullable(manifest.getMainAttributes().getValue("Build-Label")).orElse("");
		
		setTitle("JGPSTrackEdit - "+International.getText("dlgabout.About"));
		setBounds(100, 100, 530, 470);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10,10, 10,10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 1));
		{
			JLabel txtrJgpstrackeditc = new JLabel();
			txtrJgpstrackeditc.setText(
					"<html>JGPSTrackEdit (c) 2012-2019 by Hubert Lutnik (hubert.lutnik@htl-klu.at)<br/>"
					//+ String.format("Release %s (%s)<br/><br/>", buildVersion, buildTime )
					+ (buildVersion.equals("")?BUILDSTRING + "<br/><br/>":String.format("Release %s (%s)<br/><br/>", buildVersion, buildTime ))
					+ "Usage for non commercial purposes only.<br/>"
					+ "No guaranties!<br/><br/>"
					+ "Thanks to GPSPrune for some ideas:<br/>"
					+ "http://activityworkshop.net/software/gpsprune/index.html<br/><br/>"
					+ "Thanks to Mark James for licensing the icons<br/>"
					+ "http://www.famfamfam.com/lab/icons/silk/<br/><br/>"
					+ "See also:<br/>"
					+ "http://www.gpsies.com<br/>"
					+ "http://hikebikemap.de/<br/>"
					+ "http://www.openstreetmap.org/<br/>"
					+ "http://hikebikemap.de/<br/>"
					+ "http://www.mapquest.com/<br/>"
					+ "http://maps.google.com/<br/>"
					+ "http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames<br/>"
					+ "http://code.google.com/intl/de/apis/maps/documentation/staticmaps/<br/>"
					+ "http://code.google.com/intl/de/apis/maps/documentation/elevation/<br/>"
					+ "http://open.mapquestapi.com/directions/<br/>"
					+ "http://open.mapquestapi.com/staticmap/<br/>");
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
	
	/**
	 * Read the manifest file from the executable jar file.
	 * 
	 * @return Manifest-Object of the given META-INF/MANIFEST.MF file
	 */
	private Manifest getManifest() {
		String resource = "/" + this.getClass().getName().replace(".", "/") + ".class";
		String fullPath = this.getClass().getResource(resource).toString();
		String archivePath = fullPath.substring(0, fullPath.length() - resource.length());
		String manifestFileName = archivePath = archivePath	+ "/META-INF/MANIFEST.MF";

		try (InputStream input = new URL(manifestFileName).openStream()) {
			return new Manifest(input);
		} catch (Throwable t) {
			return new Manifest();
		}
	}
}
