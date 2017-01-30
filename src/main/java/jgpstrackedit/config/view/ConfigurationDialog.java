package jgpstrackedit.config.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jgpstrackedit.view.JGPSTrackEdit;
import jgpstrackedit.config.Configuration;
import jgpstrackedit.international.International;

public class ConfigurationDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	// New configuration panels must be added here
	private ConfigurationPanel[] configurationPanels = {new GeneralPanel(),
			                                            new ViewingTabPanel(),
			                                            new MapsPanel(),
			                                            new TourPlanerPanel(),
			                                            new RoutingPanel()};


	/**
	 * Create the dialog.
	 */
	public ConfigurationDialog(final JGPSTrackEdit parent) {
		setTitle(International.getText("dlgconfig.Configuration"));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane);
			for (int i=0;i<configurationPanels.length;i++) {
				tabbedPane.addTab(configurationPanels[i].getTabName(),(JPanel)configurationPanels[i]);
				configurationPanels[i].initialize();

			}

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						for (int i=0;i<configurationPanels.length;i++) {
							configurationPanels[i].save();
						}
						Configuration.saveProperties();
						setVisible(false);
						parent.repaint();
					}
				});
				okButton.setActionCommand(International.getText("OK"));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(International.getText("Cancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						for (int i=0;i<configurationPanels.length;i++) {
							configurationPanels[i].cancel();
						}
						setVisible(false);
					}
				});
				cancelButton.setActionCommand(International.getText("Cancel"));
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/** Initializes the configuration data.
	 * 
	 */
	public void initialize() {
		for (int i=0;i<configurationPanels.length;i++) {
			configurationPanels[i].initialize();
		}
		
	}

}
