package jgpstrackedit.map.tiledownload;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DlgStartTiledownloadMode extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnUseLoadedTracks;
	private JLabel lblDefineTileDownload;
	private JRadioButton rdbtnLoadExistingTile;
	private JTextField textField;
	private JButton btnSearchFile;
	private JRadioButton rdbtnStartWithEmpty;
	private JLabel lblZoomLevel;
	private JRadioButton rdbtnUseCurrentZoom;
	private JRadioButton rdbtnChooseZoomLevel;
	private JComboBox comboBox;
	private ButtonGroup definition;
	private ButtonGroup zoom;
	
	public final static int RESULT_CANCEL = 0;
	public final static int TRACKS = 1;
	public final static int FILE = 2;
	public final static int EMPTY = 3;

	private int result = TRACKS;


	/**
	 * Create the dialog.
	 */
	public DlgStartTiledownloadMode(JFrame frame, int currentZoom, int maxZoom) {
		super(frame,true);
		initComponents(currentZoom, maxZoom);
	}
	
	public int getResult() {
		return result;
	}
	
	public int getDownloadZoom() {
		return Integer.parseInt(comboBox.getSelectedItem().toString());
	}
	
	public String getFilePath() {
		return textField.getText();
	}

	private void initComponents(int currentZoom, int maxZoom) {
		setTitle("Start Tile Download Mode");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		rdbtnUseLoadedTracks = new JRadioButton("Use loaded tracks");
		rdbtnUseLoadedTracks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnUseLoadedTracksActionPerformed(e);
			}
		});
		rdbtnUseLoadedTracks.setSelected(true);
		rdbtnUseLoadedTracks.setBounds(20, 34, 327, 23);
		contentPanel.add(rdbtnUseLoadedTracks);
		
		lblDefineTileDownload = new JLabel("Define tile download definition");
		lblDefineTileDownload.setBounds(10, 11, 373, 14);
		contentPanel.add(lblDefineTileDownload);
		
		rdbtnLoadExistingTile = new JRadioButton("Load existing tile download definition");
		rdbtnLoadExistingTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnLoadExistingTileActionPerformed(arg0);
			}
		});
		rdbtnLoadExistingTile.setBounds(20, 60, 304, 23);
		contentPanel.add(rdbtnLoadExistingTile);
		
		textField = new JTextField();
		textField.setBounds(42, 90, 268, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnSearchFile = new JButton("Search File");
		btnSearchFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSearchFileActionPerformed(arg0);
			}
		});
		btnSearchFile.setBounds(320, 90, 112, 23);
		contentPanel.add(btnSearchFile);
		
		rdbtnStartWithEmpty = new JRadioButton("Start with empty tile download definition");
		rdbtnStartWithEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnStartWithEmptyActionPerformed(e);
			}
		});
		rdbtnStartWithEmpty.setBounds(20, 117, 290, 23);
		contentPanel.add(rdbtnStartWithEmpty);
		
		lblZoomLevel = new JLabel("Zoom level");
		lblZoomLevel.setBounds(10, 147, 235, 14);
		contentPanel.add(lblZoomLevel);
		
		rdbtnUseCurrentZoom = new JRadioButton("Use current zoom level");
		rdbtnUseCurrentZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnUseCurrentZoomActionPerformed(e);
			}
		});
		rdbtnUseCurrentZoom.setSelected(true);
		rdbtnUseCurrentZoom.setBounds(20, 168, 290, 23);
		contentPanel.add(rdbtnUseCurrentZoom);
		
		rdbtnChooseZoomLevel = new JRadioButton("Choose zoom level");
		rdbtnChooseZoomLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnChooseZoomLevelActionPerformed(e);
			}
		});
		rdbtnChooseZoomLevel.setBounds(20, 194, 173, 23);
		contentPanel.add(rdbtnChooseZoomLevel);
		
		comboBox = new JComboBox();
		comboBox.setBounds(199, 195, 111, 20);
		for (int i=1; i<=maxZoom;i++) {
			comboBox.addItem(i);
		}
		comboBox.setSelectedItem(currentZoom);
		contentPanel.add(comboBox);
		
		definition = new ButtonGroup();
		definition.add(rdbtnUseLoadedTracks);
		definition.add(rdbtnLoadExistingTile);
		definition.add(rdbtnStartWithEmpty);
		
		zoom = new ButtonGroup();
		zoom.add(rdbtnUseCurrentZoom);
		zoom.add(rdbtnChooseZoomLevel);

		ableFile(false);
		ableZoom(false);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
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
	
	private void ableFile(boolean value) {
		textField.setEditable(value);
		btnSearchFile.setEnabled(value);
	}
	
	
	protected void rdbtnLoadExistingTileActionPerformed(ActionEvent arg0) {
		ableFile(true);
		result = FILE;
	}
	
	protected void rdbtnUseLoadedTracksActionPerformed(ActionEvent e) {
		ableFile(false);
		result = TRACKS;
	}
	
	protected void rdbtnStartWithEmptyActionPerformed(ActionEvent e) {
		ableFile(false);
		result = EMPTY;
	}
	
	private void ableZoom(boolean value) {
		comboBox.setEditable(value);
		comboBox.setEnabled(value);
	}
	
	protected void rdbtnUseCurrentZoomActionPerformed(ActionEvent e) {
		ableZoom(false);
	}
	
	protected void rdbtnChooseZoomLevelActionPerformed(ActionEvent e) {
		ableZoom(true);
	}
	
	protected void okButtonActionPerformed(ActionEvent e) {
		setVisible(false);
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		setVisible(false);
		result = RESULT_CANCEL;
	}
	
	protected void btnSearchFileActionPerformed(ActionEvent arg0) {
		JFileChooser fileOpenChooser = new JFileChooser();

		int returnVal = fileOpenChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileOpenChooser.getSelectedFile();
			textField.setText(file.getPath());
		}

	}
}
