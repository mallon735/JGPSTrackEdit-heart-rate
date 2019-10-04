package jgpstrackedit.view;

import jgpstrackedit.international.International;
import jgpstrackedit.util.Parser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DlgCompressOptions extends JDialog {

	public static enum CompressionOption {
		Cancel,
		RemoveInterval,
		MaxDistance,
		Interdistance,
		DouglasPeucker

	}
	
	private CompressionOption option;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldRemoveInterval;
	private JTextField textFieldMaxDistance;
	private JTextField textFieldTrackName;
	private ButtonGroup buttonGroup = new ButtonGroup();
	JCheckBox chckbxCopyCompressionResult;
	JRadioButton rdbtnRemoveEvery;
	JRadioButton rdbtnRemovePointsWithin;
	private JRadioButton rdbtnRemovePointsWhich;
	private JTextField textFieldInterdistance;
	private JLabel lblNewLabel;
	private JRadioButton rdbtnDouglasPeucker;
	private JTextField textFieldDPDistance;
	private JLabel label;


	/**
	 * Create the dialog.
	 */
	public DlgCompressOptions(String trackName) {
		setTitle(International.getText("dlgCompress.Compress_Options_For_Track")+" "+trackName);
		setBounds(100, 100, 519, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		rdbtnRemoveEvery = new JRadioButton(International.getText("dlgCompress.Remove_every"));
		rdbtnRemoveEvery.setSelected(true);
		rdbtnRemoveEvery.setBounds(6, 7, 109, 23);
		contentPanel.add(rdbtnRemoveEvery);
		buttonGroup.add(rdbtnRemoveEvery);
		
		textFieldRemoveInterval = new JTextField();
		textFieldRemoveInterval.setText("2");
		textFieldRemoveInterval.setBounds(121, 8, 21, 20);
		contentPanel.add(textFieldRemoveInterval);
		textFieldRemoveInterval.setColumns(10);
		
		JLabel lblThPoint = new JLabel(International.getText("dlgCompress.th_point"));
		lblThPoint.setBounds(146, 11, 335, 14);
		contentPanel.add(lblThPoint);
		
		rdbtnRemovePointsWithin = new JRadioButton(International.getText("dlgCompress.Remove_points_within"));
		rdbtnRemovePointsWithin.setBounds(6, 33, 150, 23);
		contentPanel.add(rdbtnRemovePointsWithin);
		buttonGroup.add(rdbtnRemovePointsWithin);
		
		textFieldMaxDistance = new JTextField();
		textFieldMaxDistance.setText("4.0");
		textFieldMaxDistance.setBounds(162, 34, 30, 20);
		contentPanel.add(textFieldMaxDistance);
		textFieldMaxDistance.setColumns(10);
		
		JLabel lblMetersOffTrack = new JLabel(International.getText("dlgCompress.meters_off_track_line"));
		lblMetersOffTrack.setBounds(202, 36, 279, 14);
		contentPanel.add(lblMetersOffTrack);
		
		chckbxCopyCompressionResult = new JCheckBox(International.getText("dlgCompress.Copy_compression_result"));
		chckbxCopyCompressionResult.setBounds(6, 117, 237, 23);
		contentPanel.add(chckbxCopyCompressionResult);
		
		textFieldTrackName = new JTextField();
		textFieldTrackName.setBounds(249, 118, 232, 20);
		contentPanel.add(textFieldTrackName);
		textFieldTrackName.setColumns(10);
		textFieldTrackName.setText(International.getText("dlgCompress.Compressed")+"_"+trackName);
		
		rdbtnRemovePointsWhich = new JRadioButton(International.getText("dlgCompress.Remove_points_which_are_nearer_than"));
		rdbtnRemovePointsWhich.setBounds(6, 59, 237, 23);
		contentPanel.add(rdbtnRemovePointsWhich);
		buttonGroup.add(rdbtnRemovePointsWhich);
		
		textFieldInterdistance = new JTextField();
		textFieldInterdistance.setText("4.0");
		textFieldInterdistance.setBounds(249, 60, 38, 20);
		contentPanel.add(textFieldInterdistance);
		textFieldInterdistance.setColumns(10);
		
		lblNewLabel = new JLabel("meters");
		lblNewLabel.setBounds(298, 61, 183, 14);
		contentPanel.add(lblNewLabel);
		
		rdbtnDouglasPeucker = new JRadioButton(International.getText("dlgCompress.douglaspeucker"));
		rdbtnDouglasPeucker.setBounds(6, 85, 237, 23);
		contentPanel.add(rdbtnDouglasPeucker);
		buttonGroup.add(rdbtnDouglasPeucker);
		
		textFieldDPDistance = new JTextField();
		textFieldDPDistance.setText("4.0");
		textFieldDPDistance.setColumns(10);
		textFieldDPDistance.setBounds(249, 91, 38, 20);
		contentPanel.add(textFieldDPDistance);
		
		label = new JLabel("meters");
		label.setBounds(298, 93, 183, 14);
		contentPanel.add(label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (rdbtnRemoveEvery.isSelected()) {
							option = CompressionOption.RemoveInterval;
						}
						if (rdbtnRemovePointsWithin.isSelected()) {
							option = CompressionOption.MaxDistance;
						}
						if (rdbtnRemovePointsWhich.isSelected()) {
							option = CompressionOption.Interdistance;
						}
						if (rdbtnDouglasPeucker.isSelected()) {
							option = CompressionOption.DouglasPeucker;
						}
						setVisible(false);
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
						option = CompressionOption.Cancel;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand(International.getText("Cancel"));
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String getTrackName() {
		return textFieldTrackName.getText();
	}
	
	public int getRemoveInterval() {
		return Parser.parseInt(textFieldRemoveInterval.getText());
	}
	
	public double getMaxDistance() {
		return Parser.parseDouble(textFieldMaxDistance.getText());
	}

	public double getDouglasPeuckerDistance() {
		return Parser.parseDouble(textFieldDPDistance.getText());
	}

	public double getInterdistance() {
		return Parser.parseDouble(textFieldInterdistance.getText());
	}

	public boolean copyToNewTrack() {
		return chckbxCopyCompressionResult.isSelected();
	}

	/**
	 * @return the option
	 */
	public CompressionOption getOption() {
		return option;
	}
}
