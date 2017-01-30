package jgpstrackedit.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import jgpstrackedit.config.Constants;
import jgpstrackedit.data.Track;
import jgpstrackedit.international.International;
import javax.swing.JCheckBox;

public class DlgMerge extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JComboBox comboBox;
	private JCheckBox chckbxMergeDirectwithout;

	private int mergeOption = Constants.MERGE_NONE;
	private Track mergeTrack;
	/**
	 * @return the mergeTrack
	 */
	public Track getMergeTrack() {
		return mergeTrack;
	}

	private ArrayList<Track> tracks;
	private String trackName;

	/**
	 * Create the dialog.
	 */
	public DlgMerge(String trackNameM, ArrayList<Track> tracksList) {
		tracks = tracksList;
		trackName = trackNameM;
		setTitle(International.getText("dlgMerge.Merge_Options"));
		setBounds(100, 100, 500, 206);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(new MigLayout("", "[80.00][276.00,grow]",
				"[][][]"));
		JLabel lblMergeWith = new JLabel(
				International.getText("dlgMerge.merge_with") + ":");
		contentPanel.add(lblMergeWith, "cell 0 0,alignx left");
		comboBox = new JComboBox();
		contentPanel.add(comboBox, "cell 1 0,growx");
		for (Track track : tracks) {
			comboBox.addItem(track.getName());
		}

		JLabel lblTrackName = new JLabel(
				International.getText("dlgMerge.track_name") + ":");
		contentPanel.add(lblTrackName, "cell 0 1,alignx left");
		textField = new JTextField();
		textField.setText(trackName);
		contentPanel.add(textField, "cell 1 1,growx");
		textField.setColumns(10);
		chckbxMergeDirectwithout = new JCheckBox(
				"Merge direct (without considering track directions)");
		contentPanel.add(chckbxMergeDirectwithout, "cell 1 2");
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);
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
		setVisible(false);
		JButton cancelButton = new JButton(International.getText("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				mergeOption = Constants.MERGE_NONE;
			}
		});
		cancelButton.setActionCommand(International.getText("Cancel"));
		buttonPane.add(cancelButton);
	}

	public int getMergeOption() {
		return mergeOption;
	}

	public String getTrackName() {
		return textField.getText();
	}

}
