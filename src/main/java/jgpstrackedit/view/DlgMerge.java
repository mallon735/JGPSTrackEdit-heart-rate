package jgpstrackedit.view;

import jgpstrackedit.config.Constants;
import jgpstrackedit.data.Track;
import jgpstrackedit.international.International;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DlgMerge extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboBox;
	private JCheckBox chckbxMergeDirectwithout;

	private int mergeOption = Constants.MERGE_NONE;
	private Track mergeTrack;

	private List<Track> tracks;
	private String trackName;

	/**
	 * Create the dialog.
	 */
	public DlgMerge(String trackNameM, List<Track> tracksList) {
		tracks = tracksList;
		trackName = trackNameM;
		
		setTitle(International.getText("dlgMerge.Merge_Options"));
		
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(new MigLayout("", "[][]",
				"[][][][]"));
		
		JLabel lblTrackNameLabel = new JLabel(
				International.getText("dlgMerge.track_name") + ":");
		contentPanel.add(lblTrackNameLabel, "cell 0 0,alignx left");
		JLabel lblTrackName = new JLabel(trackName);
		contentPanel.add(lblTrackName, "cell 1 0,alignx left");
		
		JLabel lblMergeWith = new JLabel(
				International.getText("dlgMerge.merge_with") + ":");
		contentPanel.add(lblMergeWith, "cell 0 1,alignx left");
		comboBox = new JComboBox<String>();
		addItemsToComboBox(trackName);
		comboBox.setMinimumSize(new Dimension(240, comboBox.getSize().height));
		contentPanel.add(comboBox, "cell 1 1,alignx left");
		
		chckbxMergeDirectwithout = new JCheckBox(
				International.getText("dlgMerge.merge_no_direction"));
		contentPanel.add(chckbxMergeDirectwithout, "cell 1 2");
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, "cell 1 3, alignx right");
		
		JButton okButton = new JButton(International.getText("OK"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Track track : tracks) {
					if (((String) comboBox.getSelectedItem()).equals(track
							.getName())) {
						mergeTrack = track;
					}
				}
				if (mergeTrack.getName().equals(trackName)) {
					JOptionPane.showMessageDialog(null, International
							.getText("dlgMerge.Merge_to_same_track"),
							International.getText("dlgMerge.Merge_Error"),
							JOptionPane.ERROR_MESSAGE);
				} else {
					setVisible(false);
					if (chckbxMergeDirectwithout.isSelected()) {
						mergeOption = Constants.MERGE_DIRECT;
					} else {
						mergeOption = Constants.MERGE_TRACK;
					}
				}
			}
		});
		okButton.setActionCommand(International.getText("OK"));
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton(International.getText("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				mergeOption = Constants.MERGE_NONE;
			}
		});
		cancelButton.setActionCommand(International.getText("Cancel"));
		buttonPane.add(cancelButton);
		
		this.pack();
		setVisible(false);
	}

	private void addItemsToComboBox(String avoidTrack) {
		for (Track track : tracks) {
			if(track.getName().equals(avoidTrack)) {
				continue;
			}
			comboBox.addItem(track.getName());
		}
	}

	public int getMergeOption() {
		return mergeOption;
	}

	public String getTrackName() {
		return trackName;
	}
	
	/**
	 * @return the mergeTrack
	 */
	public Track getMergeTrack() {
		return mergeTrack;
	}
}
