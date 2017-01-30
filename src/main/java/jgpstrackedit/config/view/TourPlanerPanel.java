package jgpstrackedit.config.view;

import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.data.util.TourPlaner;
import jgpstrackedit.util.Parser;
import jgpstrackedit.international.International;

public class TourPlanerPanel extends JPanel implements ConfigurationPanel {
	private JTextField textFieldAverageSpeed;
	private JTextField textFieldInclineTime;
	private JTextField textFieldBreakRatio;
	private JTextField textFieldMaxTourTime;
	
	private DecimalFormat df = new DecimalFormat ("#####0.000");


	/**
	 * Create the panel.
	 */
	public TourPlanerPanel() {
		setLayout(null);
		
		JLabel lblAverageDrivingSpeed = new JLabel(International.getText("dlgconfig.Average_driving_speed")+":");
		lblAverageDrivingSpeed.setToolTipText(International.getText("dlgconfig.Average_speed"));
		lblAverageDrivingSpeed.setBounds(10, 11, 278, 14);
		add(lblAverageDrivingSpeed);
		
		textFieldAverageSpeed = new JTextField();
		textFieldAverageSpeed.setBounds(298, 8, 86, 20);
		add(textFieldAverageSpeed);
		textFieldAverageSpeed.setColumns(10);
		
		JLabel lblKmh = new JLabel("km/h");
		lblKmh.setBounds(394, 11, 46, 14);
		add(lblKmh);
		
		JLabel lblInclineTimeFor = new JLabel(International.getText("dlgconfig.Incline_time")+":");
		lblInclineTimeFor.setToolTipText(International.getText("dlgconfig.Additional_time_to_climb"));
		lblInclineTimeFor.setBounds(10, 36, 278, 14);
		add(lblInclineTimeFor);
		
		textFieldInclineTime = new JTextField();
		textFieldInclineTime.setBounds(298, 33, 86, 20);
		add(textFieldInclineTime);
		textFieldInclineTime.setColumns(10);
		
		JLabel lblMin = new JLabel("min");
		lblMin.setBounds(394, 36, 46, 14);
		add(lblMin);
		
		JLabel lblBreakRatio = new JLabel(International.getText("dlgconfig.Break_ratio")+":");
		lblBreakRatio.setToolTipText(International.getText("dlgconfig.Defines_the_ratio"));
		lblBreakRatio.setBounds(10, 61, 278, 14);
		add(lblBreakRatio);
		
		textFieldBreakRatio = new JTextField();
		textFieldBreakRatio.setBounds(298, 58, 86, 20);
		add(textFieldBreakRatio);
		textFieldBreakRatio.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(International.getText("dlgconfig.Maximum_duration_of_an")+":");
		lblNewLabel.setToolTipText(International.getText("dlgconfig.Value_used_for_multiple"));
		lblNewLabel.setBounds(10, 86, 278, 14);
		add(lblNewLabel);
		
		textFieldMaxTourTime = new JTextField();
		textFieldMaxTourTime.setBounds(298, 83, 86, 20);
		add(textFieldMaxTourTime);
		textFieldMaxTourTime.setColumns(10);
		
		JLabel lblH = new JLabel("hh:mm");
		lblH.setBounds(394, 86, 46, 14);
		add(lblH);

	}

	@Override
	public String getTabName() {
		// TODO Auto-generated method stub
		return International.getText("dlgconfig.Tourplanner");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		TourPlaner.setAverageSpeed(Parser.parseDouble(textFieldAverageSpeed.getText()));
		TourPlaner.setInclineTime100Meters(Parser.parseDouble(textFieldInclineTime.getText()));
		TourPlaner.setBreakRatio(Parser.parseDouble(textFieldBreakRatio.getText()));
		TourPlaner.setMaxTourTime(Parser.parseTime(textFieldMaxTourTime.getText()));
		Configuration.setProperty("AVERAGESPEED",textFieldAverageSpeed.getText());
		Configuration.setProperty("INCLINETIME100METERS",textFieldInclineTime.getText()); // min
		Configuration.setProperty("BREAKRATIO",textFieldBreakRatio.getText()); //
		Configuration.setProperty("MAXTOURTIME",textFieldMaxTourTime.getText()); // h
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		//System.out.println("Incline="+TourPlaner.getInclineTime100Meters());
		textFieldAverageSpeed.setText(df.format(TourPlaner.getAverageSpeed()));
		textFieldInclineTime.setText(df.format(TourPlaner.getInclineTime100Meters()));
		textFieldBreakRatio.setText(df.format(TourPlaner.getBreakRatio()));
		textFieldMaxTourTime.setText(Parser.formatTimeHHMM(TourPlaner.getMaxTourTime()));
		
	}
}
