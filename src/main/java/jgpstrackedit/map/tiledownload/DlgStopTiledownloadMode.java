package jgpstrackedit.map.tiledownload;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.border.EtchedBorder;

import jgpstrackedit.config.SystemConfig;

public class DlgStopTiledownloadMode extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblDirectoryToStore;
	private JTextField textField;
	private JButton btnChooseDirectory;
	private JCheckBox chckbxAddExtensiontile;
	private JLabel lblPressOkButton;
	
	private boolean result = false;
	private JCheckBox chckbxDownloadTilesOf;
	private JList list;
	private JCheckBox chckbxAppendMapName;
	private JTextField textFieldMapName;

	
	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	public String getExtension() {
		if (chckbxAddExtensiontile.isSelected()) {
			return ".tile";
		} else {
			return "";
		}
	}
	
	public String getTargetDir() {
		return textField.getText()+(chckbxAppendMapName.isSelected()?SystemConfig.dirSeparator()+textFieldMapName.getText():"");
	}

	public List getAdditionalZoomLevels() {
		if (chckbxDownloadTilesOf.isSelected())
		  return list.getSelectedValuesList();
		else
			return new ArrayList();
	}
	/**
	 * Create the dialog.
	 */
	public DlgStopTiledownloadMode(JFrame frame,int zoomLevel, String mapName) {
		super(frame,true);
		initComponents(zoomLevel);
		textFieldMapName.setText(mapName);
	}
	
	private void initComponents(int zoomLevel) {
		setTitle("Stop Tile Download Mode");
		setBounds(100, 100, 659, 364);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblDirectoryToStore = new JLabel("Directory to store download tiles");
		lblDirectoryToStore.setBounds(10, 11, 188, 14);
		contentPanel.add(lblDirectoryToStore);
		
		textField = new JTextField();
		textField.setBounds(20, 36, 465, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnChooseDirectory = new JButton("Choose Directory");
		btnChooseDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnChooseDirectoryActionPerformed(e);
			}
		});
		btnChooseDirectory.setBounds(495, 35, 146, 23);
		contentPanel.add(btnChooseDirectory);
		
		chckbxAddExtensiontile = new JCheckBox("Add extension .tile to image files");
		chckbxAddExtensiontile.setBounds(6, 88, 416, 23);
		contentPanel.add(chckbxAddExtensiontile);
		
		lblPressOkButton = new JLabel("Press OK button to save all download tiles to the given directory. This action may take some time!");
		lblPressOkButton.setBounds(10, 272, 631, 14);
		contentPanel.add(lblPressOkButton);
		
		chckbxDownloadTilesOf = new JCheckBox("Additionally download tiles of lower zoom levels");
		chckbxDownloadTilesOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxDownloadTilesOfActionPerformed(arg0);
			}
		});
		chckbxDownloadTilesOf.setBounds(6, 114, 595, 23);
		contentPanel.add(chckbxDownloadTilesOf);
		
		list = new JList();
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		list.setBounds(20, 119, 51, 110);
		DefaultListModel listModel = new DefaultListModel();
		for (int i=zoomLevel-1; i>1;i--) {
			listModel.addElement(i);
		}
		list.setModel(listModel);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(20, 144, 51, 117);
		list.setEnabled(false);			
		contentPanel.add(scrollPane);
		
		chckbxAppendMapName = new JCheckBox("Append map name to target directory");
		chckbxAppendMapName.setBounds(6, 63, 288, 23);
		contentPanel.add(chckbxAppendMapName);
		
		textFieldMapName = new JTextField();
		textFieldMapName.setBounds(310, 67, 175, 20);
		contentPanel.add(textFieldMapName);
		textFieldMapName.setColumns(10);
		
		
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
	
	protected void btnChooseDirectoryActionPerformed(ActionEvent e) {
		JFileChooser fileOpenChooser = new JFileChooser();
        fileOpenChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileOpenChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileOpenChooser.getSelectedFile();
			textField.setText(file.getPath());
		}

	}
		
	protected void okButtonActionPerformed(ActionEvent e) {
		if (textField.getText().length()>0) {
		    result = true;
	     	setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this, "No target directory given!", "Missing values", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		setVisible(false);
	}
	protected void chckbxDownloadTilesOfActionPerformed(ActionEvent arg0) {
		if (chckbxDownloadTilesOf.isSelected()) {
			list.setEnabled(true);
		} else {
			list.setEnabled(false);			
		}
	}
}
