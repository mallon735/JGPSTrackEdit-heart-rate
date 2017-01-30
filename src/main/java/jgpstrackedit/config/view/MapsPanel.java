package jgpstrackedit.config.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.international.International;
import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.util.MapExtractManager;


public class MapsPanel extends JPanel implements ConfigurationPanel {
	private JCheckBox chckbxShowMapOn;
	private JButton btnClearMemoryTile;
	private JTextField textFieldMaxTiles;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnCountrySpecificMap;
	private JRadioButton rdbtnMapExtract;
	private JComboBox comboBoxMapExtract;
	private JRadioButton rdbtnLastMapExtract;
	
	
	/**
	 * Create the panel.
	 */
	public MapsPanel() {
		

		initComponents();
	}
	private void initComponents() {
		
		chckbxShowMapOn = new JCheckBox(International.getText("dlgconfig.Show_map_on_startup"));
		chckbxShowMapOn.setBounds(7, 7, 255, 23);
		chckbxShowMapOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setDisplay(chckbxShowMapOn.isSelected());
			}
		});
		setLayout(null);
		add(chckbxShowMapOn);
		
		btnClearMemoryTile = new JButton(International.getText("dlgconfig.Clear_memory_tile_cache"));
		btnClearMemoryTile.setBounds(7, 115, 210, 23);
		btnClearMemoryTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TileManager.getCurrentTileManager().initializeTileCacheStructure();
			}
		});
		add(btnClearMemoryTile);
		
		textFieldMaxTiles = new JTextField();
		textFieldMaxTiles.setBounds(289, 142, 86, 20);
		add(textFieldMaxTiles);
		textFieldMaxTiles.setColumns(10);
		
		JLabel lblMaxNumberOf = new JLabel(International.getText("dlgconfig.Max_number_of_tiles_in_memory")+":");
		lblMaxNumberOf.setBounds(7, 145, 255, 14);
		add(lblMaxNumberOf);
		
		rdbtnCountrySpecificMap = new JRadioButton(International.getText("dlgconfig.Country_specific_map_extract"));
		rdbtnCountrySpecificMap.setBounds(33, 33, 398, 23);
		rdbtnCountrySpecificMap.setSelected(true);
		add(rdbtnCountrySpecificMap);
		
		rdbtnMapExtract = new JRadioButton(International.getText("dlgconfig.Map_extract"));
		rdbtnMapExtract.setBounds(33, 59, 184, 23);
		add(rdbtnMapExtract);
		
		rdbtnLastMapExtract = new JRadioButton(International.getText("dlgconfig.Last_map_extract"));
		rdbtnLastMapExtract.setBounds(33, 85, 210, 23);
		add(rdbtnLastMapExtract);
		
		comboBoxMapExtract = new JComboBox(MapExtractManager.mapExtractNames());
		comboBoxMapExtract.setBounds(227, 63, 208, 20);
		add(comboBoxMapExtract);
			
		buttonGroup.add(rdbtnCountrySpecificMap);
		buttonGroup.add(rdbtnMapExtract);
		buttonGroup.add(rdbtnLastMapExtract);
		
	}

	protected void setDisplay(boolean selected) {
		// TODO Auto-generated method stub
		rdbtnCountrySpecificMap.setEnabled(selected);
		rdbtnMapExtract.setEnabled(selected);
		rdbtnLastMapExtract.setEnabled(selected);
		comboBoxMapExtract.setEnabled(selected);
		
	}

	@Override
	public String getTabName() {
		// TODO Auto-generated method stub
		return International.getText("dlgconfig.Maps");
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		chckbxShowMapOn.setSelected(Configuration.getBooleanProperty("SHOW_MAP_ON_STARTUP"));
		rdbtnCountrySpecificMap.setSelected(Configuration.getBooleanProperty("COUNTRY_SPECIFIC_MAP"));
		rdbtnLastMapExtract.setSelected(Configuration.getBooleanProperty("LAST_MAP_EXTRACT"));
		comboBoxMapExtract.setSelectedItem(Configuration.getProperty("MAPEXTRACT"));
		textFieldMaxTiles.setText(""+Configuration.getIntProperty("MAX_TILES_IN_MEMORY"));
		setDisplay(chckbxShowMapOn.isSelected());
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		Configuration.setProperty("SHOW_MAP_ON_STARTUP",chckbxShowMapOn.isSelected()?"1":"0");
		Configuration.setProperty("COUNTRY_SPECIFIC_MAP",rdbtnCountrySpecificMap.isSelected()?"1":"0");
		Configuration.setProperty("MAPEXTRACT",comboBoxMapExtract.getSelectedItem().toString());
		Configuration.setProperty("MAX_TILES_IN_MEMORY",textFieldMaxTiles.getText());
		Configuration.setProperty("LAST_MAP_EXTRACT",rdbtnLastMapExtract.isSelected()?"1":"0");
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}
