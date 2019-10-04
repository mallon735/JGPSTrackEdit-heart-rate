package jgpstrackedit.view;

import jgpstrackedit.international.International;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DlgSelectMapExtract extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JList list;
	
	private String selectedMapExtractName = null;

	/**
	 * @return the selectedMapExtractName
	 */
	public String getSelectedMapExtractName() {
		return selectedMapExtractName;
	}

	/**
	 * Create the dialog.
	 */
	public DlgSelectMapExtract(String[] mapExtractNames) {
		setTitle(International.getText("dlgSelectMapExtract.Select_map_extract"));
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				list = new JList(mapExtractNames);
				scrollPane.setViewportView(list);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						selectedMapExtractName = (String)list.getSelectedValue();
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
						selectedMapExtractName = null;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand(International.getText("Cancel"));
				buttonPane.add(cancelButton);
			}
		}
	}

}
