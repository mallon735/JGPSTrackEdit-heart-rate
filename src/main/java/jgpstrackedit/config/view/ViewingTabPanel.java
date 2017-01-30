package jgpstrackedit.config.view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import jgpstrackedit.config.Configuration;
import javax.swing.JCheckBox;
import jgpstrackedit.international.International;

public class ViewingTabPanel extends JPanel implements ConfigurationPanel {

	private JComboBox comboBoxUnselectedLine;
	private JComboBox comboBoxSelectedLine;
	private JComboBox comboBoxDiameter;	
	private JCheckBox chckbxShowDirectionButtons;
	
	/**
	 * Create the panel.
	 */
	public ViewingTabPanel() {
		setLayout(null);
		
		JLabel lblUnselectedTrackLine = new JLabel(International.getText("dlgconfig.Unselected_track_line_width")+":");
		lblUnselectedTrackLine.setBounds(10, 11, 222, 14);
		add(lblUnselectedTrackLine);
		
		JLabel lblSelectedTrackLine = new JLabel(International.getText("dlgconfig.Selected_track_line_width")+":");
		lblSelectedTrackLine.setBounds(10, 36, 222, 14);
		add(lblSelectedTrackLine);
		
		String[] lineValues = {"1","2","3","4"};
		comboBoxSelectedLine = new JComboBox(lineValues);
		comboBoxSelectedLine.setBounds(242, 33, 64, 20);
		add(comboBoxSelectedLine);
		
		comboBoxUnselectedLine = new JComboBox(lineValues);
		comboBoxUnselectedLine.setBounds(242, 8, 64, 20);
		add(comboBoxUnselectedLine);
		
		JLabel lblPointDiameter = new JLabel(International.getText("dlgconfig.Point_diameter")+":");
		lblPointDiameter.setBounds(10, 61, 222, 14);
		add(lblPointDiameter);

		String[] diameterValues = {"5","6","7","8","9","10","11"};
		comboBoxDiameter = new JComboBox(diameterValues);
		comboBoxDiameter.setBounds(242, 58, 64, 20);
		add(comboBoxDiameter);
		
		chckbxShowDirectionButtons = new JCheckBox(International.getText("dlgconfig.Show_direction_buttons"));
		chckbxShowDirectionButtons.setBounds(10, 82, 296, 23);
		add(chckbxShowDirectionButtons);

	}

	@Override
	public String getTabName() {
		// TODO Auto-generated method stub
		return International.getText("dlgconfig.Viewing");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		Configuration.setProperty("SELECTED_LINE_WIDTH",(String)comboBoxSelectedLine.getSelectedItem());
		Configuration.setProperty("UNSELECTED_LINE_WIDTH",(String)comboBoxUnselectedLine.getSelectedItem());
		Configuration.setProperty("POINT_DIAMETER",(String)comboBoxDiameter.getSelectedItem());
		Configuration.setProperty("SHOW_DIRECTION_BUTTONS",chckbxShowDirectionButtons.isSelected()?"1":"0");
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		comboBoxSelectedLine.setSelectedItem(Configuration.getProperty("SELECTED_LINE_WIDTH"));
		comboBoxUnselectedLine.setSelectedItem(Configuration.getProperty("UNSELECTED_LINE_WIDTH"));
		comboBoxDiameter.setSelectedItem(Configuration.getProperty("POINT_DIAMETER"));
		chckbxShowDirectionButtons.setSelected(Configuration.getBooleanProperty("SHOW_DIRECTION_BUTTONS"));
		
	}
}
