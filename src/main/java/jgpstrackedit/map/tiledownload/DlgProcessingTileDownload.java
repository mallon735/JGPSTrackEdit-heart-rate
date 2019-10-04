package jgpstrackedit.map.tiledownload;

import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.util.TileNumber;
import jgpstrackedit.util.ProgressHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

public class DlgProcessingTileDownload extends JDialog 
                                       implements ProgressHandler {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblDownloadStateLog;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JProgressBar progressBar;
	private boolean progress = true;


	/**
	 * Create the dialog.
	 */
	public DlgProcessingTileDownload() {
		initComponents();
	}
	
	private void initComponents() {
		setTitle("Tile Download");
		setBounds(100, 100, 457, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblDownloadStateLog = new JLabel("Download state log:");
		lblDownloadStateLog.setBounds(10, 11, 401, 14);
		contentPanel.add(lblDownloadStateLog);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 431, 163);
		contentPanel.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 205, 431, 19);
		contentPanel.add(progressBar);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
	
	protected void okButtonActionPerformed(ActionEvent arg0) {
		setVisible(false);
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		textArea.setText("Download cancelled.\n");
		setVisible(false);
		progress = false;
	}

	public void startDownload(TreeSet<TileNumber> downloadTiles, String targetDir,
			String extension) {
		textArea.setText("Start Download...\n");
		ArrayList<TileNumber> errorTiles = TileManager.getCurrentTileManager().downloadTiles(
				downloadTiles, targetDir, extension, this);
		if (errorTiles.size() > 0) {
			textArea.append("Error during tile download\n");
			textArea.append("Following tiles had not been downloaded:\n");
			for (TileNumber tile:errorTiles) {
				textArea.append(tile.toString()+"\n");
			}
			
		} else {
			textArea.append("Tile images downloaded sucessfully!");
			setVisible(false);
		}
	
	}
	
	@Override
	public boolean stepDone(int percentage) {
		// TODO Auto-generated method stub
		//logger.info("stepDone "+percentage);
		progressBar.setValue(percentage);
		return progress;
	}
}
