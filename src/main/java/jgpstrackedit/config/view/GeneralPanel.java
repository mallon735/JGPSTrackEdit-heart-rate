package jgpstrackedit.config.view;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import jgpstrackedit.config.Configuration;
import jgpstrackedit.international.International;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Locale;

import jgpstrackedit.international.International;

public class GeneralPanel extends JPanel implements ConfigurationPanel {

	private JCheckBox chckbxShowHelpDialog;
	
	private JComboBox comboBoxLanguage;
	private JComboBox comboBoxCountry;
	private JComboBox comboBoxLF;
	/**
	 * Create the panel.
	 */
	public GeneralPanel() {
		setLayout(null);
		
		chckbxShowHelpDialog = new JCheckBox(International.getText("dlgconfig.Show_Help_Dialog_on_startup"));
		chckbxShowHelpDialog.setBounds(6, 7, 331, 23);
		add(chckbxShowHelpDialog);
		
		JLabel lblLanguage = new JLabel(International.getText("dlgconfig.Language")+":");
		lblLanguage.setBounds(6, 33, 117, 14);
		add(lblLanguage);
		
		comboBoxLanguage = new JComboBox();
		comboBoxLanguage.setModel(new DefaultComboBoxModel(International.languages));
		comboBoxLanguage.setBounds(133, 30, 119, 20);
		add(comboBoxLanguage);
		
		JLabel lblCountry = new JLabel(International.getText("dlgconfig.Country")+":");
		lblCountry.setBounds(6, 58, 117, 14);
		add(lblCountry);
		
		comboBoxCountry = new JComboBox();
		comboBoxCountry.setModel(new DefaultComboBoxModel(International.getCountries()));
		comboBoxCountry.setBounds(133, 55, 119, 20);
		add(comboBoxCountry);
		
		JButton btnRestoreDefault = new JButton(International.getText("dlgconfig.Restore_default"));
		btnRestoreDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxLanguage.setSelectedItem(Locale.getDefault().getLanguage());
				comboBoxCountry.setSelectedItem(Locale.getDefault().getCountry());
				comboBoxLF.setSelectedItem("System");
			}
		});
		btnRestoreDefault.setBounds(6, 113, 246, 23);
		add(btnRestoreDefault);
		
		JLabel lblGuiLookfeel = new JLabel(International.getText("dlgconfig.GUI_LookFeel")+":");
		lblGuiLookfeel.setBounds(6, 83, 117, 14);
		add(lblGuiLookfeel);
		
		comboBoxLF = new JComboBox();
		comboBoxLF.setModel(new DefaultComboBoxModel(new String[] {"System", "Cross-Platform"}));
		comboBoxLF.setBounds(133, 80, 119, 20);
		add(comboBoxLF);

	}

	@Override
	public String getTabName() {
		// TODO Auto-generated method stub
		return International.getText("dlgconfig.General");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		Configuration.setProperty("SHOW_HELP_ON_STARTUP",chckbxShowHelpDialog.isSelected()?"1":"0");
		International.setCurrentLocale(new Locale(comboBoxLanguage.getSelectedItem().toString(),comboBoxCountry.getSelectedItem().toString()));
        Configuration.setProperty("LOCALE",International.getCurrentLocale().toString());
		Configuration.setProperty("GUILOOKFEEL",comboBoxLF.getSelectedItem().toString());
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		chckbxShowHelpDialog.setSelected(Configuration.getBooleanProperty("SHOW_HELP_ON_STARTUP"));
		comboBoxLanguage.setSelectedItem(International.getCurrentLanguage());
		comboBoxCountry.setSelectedItem(International.getCurrentCountry());
		comboBoxLF.setSelectedItem(Configuration.getProperty("GUILOOKFEEL"));
		
	}
}
