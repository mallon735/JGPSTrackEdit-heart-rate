package jgpstrackedit.gpsies;

import jgpstrackedit.data.Track;
import jgpstrackedit.international.International;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GPSiesSaveDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(GPSiesSaveDlg.class);
	
	private final JPanel contentPanel = new JPanel();
	private JLabel lblTrackName;
	private JTextField textFieldTrackName;
	private JLabel lblTrackType;
	private JComboBox<String> comboBoxTrackTypes;
	private Track saveTrack;
	private JLabel lblErrorlog;
	private JTextArea textAreaErrorLog;
	private GPSiesUpLoad gpsUpLoad;
	private JLabel lblUsername;
	private JTextField textFieldUsername;
	private JLabel lblPassword;
	private JPasswordField passwordField;


	/**
	 * Create the dialog.
	 */
	public GPSiesSaveDlg(Track saveTrack) {
		this.saveTrack = saveTrack;
		initComponents();
		gpsUpLoad = new GPSiesUpLoad();
		if (!gpsUpLoad.checkUploadable(saveTrack)) {
			textAreaErrorLog.setText(International.getText("dlgGPSies.Error_length"));
		}
		
	}
	
	private void initComponents() {
		setTitle(International.getText("dlgGPSies.Save_Track"));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblTrackName = new JLabel(International.getText("dlgGPSies.Track_name")+":");
		lblTrackName.setBounds(10, 11, 94, 14);
		contentPanel.add(lblTrackName);
		
		textFieldTrackName = new JTextField();
		textFieldTrackName.setText(saveTrack.getName());
		textFieldTrackName.setBounds(114, 8, 318, 20);
		contentPanel.add(textFieldTrackName);
		textFieldTrackName.setColumns(10);
		
		lblTrackType = new JLabel(International.getText("dlgGPSies.Track_types")+":");
		lblTrackType.setBounds(10, 36, 94, 14);
		contentPanel.add(lblTrackType);
		
		comboBoxTrackTypes = new JComboBox<String>();
		comboBoxTrackTypes.setModel(new DefaultComboBoxModel<String>(new String[] {
				"biking", "boating", "canoeing", "car", "climbing",
				"crossskating", "flying", "geocaching", "jogging",
				"miscellaneous", "motorbiking", "mountainbiking", "racingbike",
				"riding", "skating", "skiingAlpine", "skiingNordic", "train",
				"trekking", "walking", "wintersports" }));
		comboBoxTrackTypes.setBounds(114, 33, 118, 20);
		contentPanel.add(comboBoxTrackTypes);
		
		lblErrorlog = new JLabel(International.getText("dlgGPSies.Errorlog")+":");
		lblErrorlog.setBounds(10, 111, 222, 14);
		contentPanel.add(lblErrorlog);
		
		textAreaErrorLog = new JTextArea();
		textAreaErrorLog.setBounds(10, 129, 422, 93);
		contentPanel.add(textAreaErrorLog);
		
		lblUsername = new JLabel(International.getText("dlgGPSies.User_name")+":");
		lblUsername.setBounds(10, 61, 94, 14);
		contentPanel.add(lblUsername);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(114, 58, 118, 20);
		contentPanel.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		lblPassword = new JLabel(International.getText("dlgGPSies.Password")+":");
		lblPassword.setBounds(10, 86, 94, 14);
		contentPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(114, 83, 118, 20);
		contentPanel.add(passwordField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("dlgGPSies.Save"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						okButtonActionPerformed(arg0);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(International.getText("Cancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	protected void okButtonActionPerformed(ActionEvent arg0) {
		if (gpsUpLoad.checkUploadable(saveTrack)) {
			gpsUpLoad.saveTempKML(textFieldTrackName.getText(), saveTrack);
			File importFile = new File(gpsUpLoad.getFileName(textFieldTrackName.getText()));
			try {
				int result = gpsUpLoad.uploadFile(textFieldTrackName.getText(), 
						(String) comboBoxTrackTypes.getSelectedItem(),
						importFile, 
						textFieldUsername.getText(),
						new String(passwordField.getPassword()));
				textAreaErrorLog.setText("Response status code: " + result);
				logger.info("Response status code: " + result);
				if (result == 200) {
				    this.setVisible(false);
				}
			} catch (Exception e) {
				textAreaErrorLog.setText(e.getMessage());
				logger.error("Exception during upload track!", e);
			}
		}
		
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
