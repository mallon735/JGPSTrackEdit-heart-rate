package jgpstrackedit.view;

import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.config.ConfigurationObserver;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.TrackObserver;
import jgpstrackedit.data.util.TourPlaner;
import jgpstrackedit.international.International;
import jgpstrackedit.util.Parser;

public class TrackDataPanel extends JPanel implements TrackObserver,ConfigurationObserver {

	private Track track = null;

	private JTextField textFieldName;
	private JTextField textFieldLength;
	private JTextField textFieldUp;
	private JTextField textFieldDown;
	private JTextField textFieldPoints;
	private JTextField textFieldDensity;
	private JTextField textFieldDuration;
	private JTextField textFieldDrivingTime;
	private JTextField textFieldMinElevation;
	private JTextField textFieldMaxElevation;

	/**
	 * @return the track
	 */
	public Track getTrack() {
		return track;
	}

	/**
	 * @param track
	 *            the track to set
	 */
	public void setTrack(Track track) {
		if (this.track != null) {
			this.track.removeTrackObserver(this);
		}
		this.track = track;
		
		if (this.track != null) {
			track.addTrackObserver(this);
		}
		initializeFields();
	}

	protected void initializeFields() {
		if (track != null) {
			DecimalFormat df = new DecimalFormat ("#####0.000");
			double length = track.getLength();
			int points = track.getNumberPoints();
			TourPlaner tourPlaner = new TourPlaner(track);
			textFieldName.setText(track.getName());
			textFieldLength.setText(df.format(length));
			textFieldUp.setText(df.format(track.getUpAltitudeDifference()));
			textFieldDown.setText(df.format(track.getDownAltitudeDifference()));
			textFieldMinElevation.setText(df.format(track.getLowestElevation()));
			textFieldMaxElevation.setText(df.format(track.getHighestElevation()));
			textFieldPoints.setText(Integer.toString(points));
			textFieldDensity.setText(df.format(points / length));
			textFieldDuration.setText(Parser.formatTimeHHMM(tourPlaner.tourTime()));
			textFieldDrivingTime.setText(Parser.formatTimeHHMM(tourPlaner
					.drivingTime()));
		} else {
			textFieldName.setText("");
			textFieldLength.setText("");
			textFieldUp.setText("");
			textFieldDown.setText("");
			textFieldMinElevation.setText("");
			textFieldMaxElevation.setText("");
			textFieldPoints.setText("");
			textFieldDensity.setText("");
			textFieldDuration.setText("");
			textFieldDrivingTime.setText("");
		}
	}

	/**
	 * Create the panel.
	 */
	public TrackDataPanel() {
		Configuration.addConfigurationObserver(this);
		setPreferredSize(new Dimension(504, 137));
		setLayout(null);

		JLabel lblName = new JLabel(International.getText("dlg_tdp.Name")+":");
		lblName.setBounds(10, 11, 46, 14);
		add(lblName);

		textFieldName = new JTextField();
		textFieldName.setEditable(false);
		textFieldName.setBounds(66, 8, 428, 20);
		add(textFieldName);
		textFieldName.setColumns(10);

		JLabel lblLength = new JLabel(International.getText("dlg_tdp.Length")+":");
		lblLength.setBounds(10, 36, 57, 14);
		add(lblLength);

		textFieldLength = new JTextField();
		textFieldLength.setEditable(false);
		textFieldLength.setBounds(66, 33, 86, 20);
		add(textFieldLength);
		textFieldLength.setColumns(10);

		JLabel lblKm = new JLabel("km");
		lblKm.setBounds(162, 36, 26, 14);
		add(lblKm);

		JLabel lblUp = new JLabel(International.getText("dlg_tdp.Up")+":");
		lblUp.setBounds(191, 36, 29, 14);
		add(lblUp);

		textFieldUp = new JTextField();
		textFieldUp.setEditable(false);
		textFieldUp.setBounds(220, 33, 86, 20);
		add(textFieldUp);
		textFieldUp.setColumns(10);

		JLabel lblDown = new JLabel(International.getText("dlg_tdp.Down")+":");
		lblDown.setBounds(316, 36, 36, 14);
		add(lblDown);

		textFieldDown = new JTextField();
		textFieldDown.setEditable(false);
		textFieldDown.setBounds(362, 33, 86, 20);
		add(textFieldDown);
		textFieldDown.setColumns(10);

		JLabel lblM = new JLabel("m");
		lblM.setBounds(459, 36, 26, 14);
		add(lblM);

		JLabel lblPoints = new JLabel(International.getText("dlg_tdp.Points")+":");
		lblPoints.setBounds(10, 86, 46, 14);
		add(lblPoints);

		textFieldPoints = new JTextField();
		textFieldPoints.setEditable(false);
		textFieldPoints.setBounds(66, 83, 86, 20);
		add(textFieldPoints);
		textFieldPoints.setColumns(10);

		JLabel lblDensity = new JLabel(International.getText("dlg_tdp.Density")+":");
		lblDensity.setBounds(162, 86, 58, 14);
		add(lblDensity);

		textFieldDensity = new JTextField();
		textFieldDensity.setEditable(false);
		textFieldDensity.setBounds(220, 83, 86, 20);
		add(textFieldDensity);
		textFieldDensity.setColumns(10);

		JLabel lblPointsKm = new JLabel(International.getText("dlg_tdp.points_km"));
		lblPointsKm.setBounds(316, 86, 86, 14);
		add(lblPointsKm);

		JLabel lblDuration = new JLabel(International.getText("dlg_tdp.Duration")+":");
		lblDuration.setBounds(10, 111, 57, 14);
		add(lblDuration);

		textFieldDuration = new JTextField();
		textFieldDuration.setEditable(false);
		textFieldDuration.setBounds(66, 108, 86, 20);
		add(textFieldDuration);
		textFieldDuration.setColumns(10);

		JLabel lblHhmm = new JLabel("hh:mm");
		lblHhmm.setBounds(162, 111, 57, 14);
		add(lblHhmm);

		JLabel lblDrivingTime = new JLabel(International.getText("dlg_tdp.Driving_time")+":");
		lblDrivingTime.setBounds(220, 111, 106, 14);
		add(lblDrivingTime);

		textFieldDrivingTime = new JTextField();
		textFieldDrivingTime.setEditable(false);
		textFieldDrivingTime.setBounds(362, 108, 86, 20);
		add(textFieldDrivingTime);
		textFieldDrivingTime.setColumns(10);

		JLabel lblHhmm_1 = new JLabel("hh:mm");
		lblHhmm_1.setBounds(458, 111, 46, 14);
		add(lblHhmm_1);
		
		JLabel lblElevation = new JLabel(International.getText("dlg_tdp.Elevation")+":");
		lblElevation.setBounds(10, 61, 71, 14);
		add(lblElevation);
		
		textFieldMinElevation = new JTextField();
		textFieldMinElevation.setEditable(false);
		textFieldMinElevation.setBounds(220, 58, 86, 20);
		add(textFieldMinElevation);
		textFieldMinElevation.setColumns(10);
		
		textFieldMaxElevation = new JTextField();
		textFieldMaxElevation.setEditable(false);
		textFieldMaxElevation.setBounds(362, 58, 86, 20);
		add(textFieldMaxElevation);
		textFieldMaxElevation.setColumns(10);
		
		JLabel lblMax = new JLabel(International.getText("dlg_tdp.max")+":");
		lblMax.setBounds(316, 61, 46, 14);
		add(lblMax);
		
		JLabel lblMin = new JLabel(International.getText("dlg_tdp.min")+":");
		lblMin.setBounds(162, 61, 48, 14);
		add(lblMin);

	}

	@Override
	public void trackModified(Track track) {
		// TODO Auto-generated method stub
		if (this.track.equals(track))
			initializeFields();

	}

	@Override
	public void configurationChanged() {
		// TODO Auto-generated method stub
		initializeFields();
		
	}
}
