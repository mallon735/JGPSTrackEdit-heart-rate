package jgpstrackedit.view;

import jgpstrackedit.config.Constants;
import jgpstrackedit.international.International;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DlgSplit extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNumberTracks;
	private JTextField textNumberPoint;
	private JTextField textLength;
	private JTextField textTrackName;
	private JRadioButton rdbtnSelectedPoint;
	private JRadioButton rdbtnNumberOfTracks;
	private JRadioButton rdbtnNumberOfPoints;
	private JRadioButton rdbtnLengthOfTrack;

	private int splitOption = Constants.SPLIT_NONE;

	/**
	 * @return the splitOption
	 */
	public int getSplitOption() {
		return splitOption;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberTracks() {
		return Integer.parseInt(textNumberTracks.getText());
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberPoints() {
		return Integer.parseInt(textNumberPoint.getText());
	}
	
	public double getSplitLength() {
		return Double.parseDouble(textLength.getText());
	}


	public String getTrackName() {
		return textTrackName.getText();
	}
	/**
	 * Create the dialog.
	 */
	public DlgSplit(String trackName) {
		ButtonGroup group = new ButtonGroup();
		setTitle(International.getText("dlgSplit.Split_Options"));
		setBounds(100, 100, 350, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		{
			rdbtnSelectedPoint = new JRadioButton(International.getText("dlgSplit.selected_point"));
		    group.add(rdbtnSelectedPoint);
			rdbtnSelectedPoint.setSelected(true);
			contentPanel.add(rdbtnSelectedPoint, "cell 0 0");
		}
		{
			rdbtnNumberOfTracks = new JRadioButton(International.getText("dlgSplit.number_of_tracks"));
			group.add(rdbtnNumberOfTracks);
			contentPanel.add(rdbtnNumberOfTracks, "cell 0 1");
		}
		{
			textNumberTracks = new JTextField();
			textNumberTracks.setText("2");
			contentPanel.add(textNumberTracks, "cell 1 1,growx");
			textNumberTracks.setColumns(10);
		}
		{
			rdbtnNumberOfPoints = new JRadioButton(International.getText("dlgSplit.number_of_points"));
		    group.add(rdbtnNumberOfPoints);
			contentPanel.add(rdbtnNumberOfPoints, "cell 0 2");
		}
		{
			textNumberPoint = new JTextField();
			textNumberPoint.setText("250");
			contentPanel.add(textNumberPoint, "cell 1 2,growx");
			textNumberPoint.setColumns(10);
		}
		{
			rdbtnLengthOfTrack = new JRadioButton(International.getText("dlgSplit.length"));
			group.add(rdbtnLengthOfTrack);
			contentPanel.add(rdbtnLengthOfTrack, "cell 0 3");
		}
		{
			textLength = new JTextField();
			textLength.setText("80.000");
			contentPanel.add(textLength, "cell 1 3,growx");
			textLength.setColumns(10);
		}
		{
			JLabel lblName = new JLabel(International.getText("dlgSplit.name")+":");
			lblName.setHorizontalAlignment(SwingConstants.LEFT);
			lblName.setHorizontalTextPosition(SwingConstants.LEFT);
			contentPanel.add(lblName, "cell 0 4,alignx left");
		}
		{
			textTrackName = new JTextField();
			textTrackName.setText(trackName);
			contentPanel.add(textTrackName, "cell 1 4,growx");
			textTrackName.setColumns(15);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (rdbtnSelectedPoint.isSelected())
						  splitOption = Constants.SPLIT_SELECTED_POINT;
						if (rdbtnNumberOfTracks.isSelected())
							  splitOption = Constants.SPLIT_NUMBER_TRACKS;
						if (rdbtnNumberOfPoints.isSelected())
							  splitOption = Constants.SPLIT_NUMBER_POINTS;
						if (rdbtnLengthOfTrack.isSelected())
							  splitOption = Constants.SPLIT_LENGTH;
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
						splitOption = Constants.SPLIT_NONE;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand(International.getText("Cancel"));
				buttonPane.add(cancelButton);
			}
		}
	}
	

}
