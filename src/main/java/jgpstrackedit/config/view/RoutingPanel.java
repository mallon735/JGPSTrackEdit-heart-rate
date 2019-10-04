/**
 * 
 */
package jgpstrackedit.config.view;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.international.International;

import javax.swing.*;

/**
 * @author Hubert
 *
 */
public class RoutingPanel extends JPanel implements ConfigurationPanel {
	
	private JComboBox comboBoxRoutingType;
	private JTextField textFieldPointDistance;
	private JCheckBox chckbxAvoidLimitedAccess;
	private JCheckBox chckbxAvoidTollRoad;
		
	public RoutingPanel() {
		setLayout(null);
		
		JLabel lblRoutingType = new JLabel(International.getText("dlgconfig.Routing_type")+":");
		lblRoutingType.setBounds(10, 11, 145, 14);
		add(lblRoutingType);
		
		comboBoxRoutingType = new JComboBox();
		comboBoxRoutingType.setModel(new DefaultComboBoxModel(new String[] {"bicycle", "fastest", "shortest", "pedestrian", "multimodal"}));
		comboBoxRoutingType.setBounds(165, 8, 150, 20);
		add(comboBoxRoutingType);
		
		JLabel lblPointDistancem = new JLabel(International.getText("dlgconfig.Point_distance")+":");
		lblPointDistancem.setBounds(10, 36, 145, 14);
		add(lblPointDistancem);
		
		textFieldPointDistance = new JTextField();
		textFieldPointDistance.setBounds(165, 33, 86, 20);
		add(textFieldPointDistance);
		textFieldPointDistance.setColumns(10);
		
		JLabel lblM = new JLabel("m");
		lblM.setBounds(261, 36, 46, 14);
		add(lblM);
		
		chckbxAvoidLimitedAccess = new JCheckBox(International.getText("dlgconfig.Avoid_limited_access"));
		chckbxAvoidLimitedAccess.setBounds(10, 57, 305, 23);
		add(chckbxAvoidLimitedAccess);
		
		chckbxAvoidTollRoad = new JCheckBox(International.getText("dlgconfig.Avoid_toll_road"));
		chckbxAvoidTollRoad.setBounds(10, 83, 305, 23);
		add(chckbxAvoidTollRoad);
	}

	@Override
	public String getTabName() {
		return International.getText("dlgconfig.Routing");
	}

	@Override
	public void initialize() {
		comboBoxRoutingType.setSelectedItem(Configuration.getProperty("ROUTINGTYPE"));
		textFieldPointDistance.setText(Configuration.getProperty("ROUTINGPOINTDISTANCE"));
		chckbxAvoidLimitedAccess.setSelected(Configuration.getBooleanProperty("ROUTINGAVOIDLIMITEDACCESS"));
		chckbxAvoidTollRoad.setSelected(Configuration.getBooleanProperty("ROUTINGAVOIDTOLLROAD"));
		
	}

	@Override
	public void save() {
		Configuration.setProperty("ROUTINGTYPE",(String)comboBoxRoutingType.getSelectedItem());
		Configuration.setProperty("ROUTINGPOINTDISTANCE",textFieldPointDistance.getText());
		Configuration.setProperty("ROUTINGAVOIDLIMITEDACCESS",chckbxAvoidLimitedAccess.isSelected()?"1":"0");
		Configuration.setProperty("ROUTINGAVOIDTOLLROAD",chckbxAvoidTollRoad.isSelected()?"1":"0");
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}
