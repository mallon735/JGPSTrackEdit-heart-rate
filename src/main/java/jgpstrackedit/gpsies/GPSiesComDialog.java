package jgpstrackedit.gpsies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jgpstrackedit.control.UIController;
import jgpstrackedit.data.Point;
import jgpstrackedit.international.International;
import jgpstrackedit.util.Parser;
import jgpstrackedit.view.Transform;

public class GPSiesComDialog extends JDialog implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(GPSiesComDialog.class);
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUser;
	private JTextField textFieldPerimeter;
	private JTextField textFieldZIP;
	private JTextField textFieldCity;
	private JTextField textFieldLimit;
	private JTable tableGPSiesResult;
	private GPSiesResult gpsiesResult;
	private ButtonGroup buttonGroup;
	private JRadioButton rdbtnUserName;
	private JRadioButton rdbtnCurrentMapView;
	private JRadioButton rdbtnCenterCurrentView;
	private JRadioButton rdbtnZip;
	private JRadioButton rdbtnCity;
	private JCheckBox chckbxCountry;
	private JComboBox<String> comboBoxCountry;
	private JCheckBox chckbxTrackProperty;
	private JComboBox<String> comboBoxTrackProperty;
	private JCheckBox chckbxTrackTypes;
	private JComboBox<String> comboBoxTrackTypes;
	private JButton btnStoppLoading;

	private boolean stoppLoading = false;
	private UIController uiController;
	private JProgressBar progressBarLoading;
	
	private String lastSearchUrl;
	private int loadedPage = 1;

	/**
	 * Create the dialog.
	 */
	public GPSiesComDialog(UIController uiController) {
		this.uiController = uiController;
		setTitle(International.getText("dlgGPSies.GPSies"));
		setBounds(100, 100, 800, 338);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		JSplitPane splitPaneTable = new JSplitPane();
		buttonGroup = new ButtonGroup();
		JPanel panelParameter = new JPanel();
		panelParameter.setMinimumSize(new Dimension(250, 200));
		panelParameter.setPreferredSize(new Dimension(150, 200));
		contentPanel.add(splitPaneTable, BorderLayout.CENTER);
		panelParameter.setLayout(null);

		rdbtnUserName = new JRadioButton(International.getText("dlgGPSies.User_name")+":");
		rdbtnUserName.setSelected(true);
		rdbtnUserName.setBounds(6, 7, 109, 23);
		panelParameter.add(rdbtnUserName);
		buttonGroup.add(rdbtnUserName);

		textFieldUser = new JTextField();
		textFieldUser.setBounds(122, 8, 118, 20);
		panelParameter.add(textFieldUser);
		textFieldUser.setColumns(10);

		rdbtnCurrentMapView = new JRadioButton(International.getText("dlgGPSies.Current_map_extract"));
		rdbtnCurrentMapView.setBounds(6, 33, 234, 23);
		panelParameter.add(rdbtnCurrentMapView);
		buttonGroup.add(rdbtnCurrentMapView);

		rdbtnCenterCurrentView = new JRadioButton(International.getText("dlgGPSies.Center_of"));
		rdbtnCenterCurrentView.setBounds(6, 59, 234, 23);
		panelParameter.add(rdbtnCenterCurrentView);
		buttonGroup.add(rdbtnCenterCurrentView);

		textFieldPerimeter = new JTextField();
		textFieldPerimeter.setBounds(122, 82, 86, 20);
		panelParameter.add(textFieldPerimeter);
		textFieldPerimeter.setColumns(10);

		rdbtnZip = new JRadioButton(International.getText("dlgGPSies.ZIP")+":");
		rdbtnZip.setBounds(6, 112, 109, 23);
		panelParameter.add(rdbtnZip);
		buttonGroup.add(rdbtnZip);

		textFieldZIP = new JTextField();
		textFieldZIP.setBounds(122, 110, 86, 20);
		panelParameter.add(textFieldZIP);
		textFieldZIP.setColumns(10);

		rdbtnCity = new JRadioButton(International.getText("dlgGPSies.City"));
		rdbtnCity.setBounds(6, 138, 109, 23);
		panelParameter.add(rdbtnCity);
		buttonGroup.add(rdbtnCity);

		textFieldCity = new JTextField();
		textFieldCity.setBounds(122, 139, 118, 20);
		panelParameter.add(textFieldCity);
		textFieldCity.setColumns(10);

		chckbxCountry = new JCheckBox(International.getText("dlgGPSies.Country")+":");
		chckbxCountry.setBounds(6, 164, 109, 23);
		panelParameter.add(chckbxCountry);

		comboBoxCountry = new JComboBox<>();
		comboBoxCountry.setModel(new DefaultComboBoxModel<String>(International.getCountries()));
		comboBoxCountry.setBounds(122, 165, 118, 20);
		panelParameter.add(comboBoxCountry);

		chckbxTrackProperty = new JCheckBox(International.getText("dlgGPSies.Track_property")+":");
		chckbxTrackProperty.setBounds(6, 190, 116, 23);
		panelParameter.add(chckbxTrackProperty);

		comboBoxTrackProperty = new JComboBox<>();
		comboBoxTrackProperty.setModel(new DefaultComboBoxModel<>(new String[] {
				"onewaytrip", "roundtrip" }));
		comboBoxTrackProperty.setBounds(122, 191, 118, 20);
		panelParameter.add(comboBoxTrackProperty);

		chckbxTrackTypes = new JCheckBox(International.getText("dlgGPSies.Track_types")+":");
		chckbxTrackTypes.setBounds(6, 216, 109, 23);
		panelParameter.add(chckbxTrackTypes);

		comboBoxTrackTypes = new JComboBox<>();
		comboBoxTrackTypes.setModel(new DefaultComboBoxModel<>(new String[] {
				"biking", "boating", "canoeing", "car", "climbing",
				"crossskating", "flying", "geocaching", "jogging",
				"miscellaneous", "motorbiking", "mountainbiking", "racingbike",
				"riding", "skating", "skiingAlpine", "skiingNordic", "train",
				"trekking", "walking", "wintersports" }));
		comboBoxTrackTypes.setBounds(122, 217, 118, 20);
		panelParameter.add(comboBoxTrackTypes);

		splitPaneTable.setBounds(379, 99, 1, 1);
		// panelParameter.add(splitPaneTable);

		JScrollPane scrollPaneTable = new JScrollPane();
		splitPaneTable.setLeftComponent(panelParameter);

		JLabel lblKm = new JLabel("km");
		lblKm.setBounds(214, 85, 46, 14);
		panelParameter.add(lblKm);

		JLabel lblPerimeter = new JLabel(International.getText("dlgGPSies.Perimeter")+":");
		lblPerimeter.setBounds(16, 85, 99, 14);
		panelParameter.add(lblPerimeter);
		splitPaneTable.setRightComponent(scrollPaneTable);

		
		tableGPSiesResult = new JTable() {    
			private static final long serialVersionUID = 1L;

			//Implement table cell tool tips.
		    public String getToolTipText(MouseEvent e) {
		        String tip = null;
		        java.awt.Point p = e.getPoint();
		        int rowIndex = rowAtPoint(p);
		        int colIndex = columnAtPoint(p);
		        int realColumnIndex = convertColumnIndexToModel(colIndex);

		        if (realColumnIndex == 1) { 
		            tip = (String) getValueAt(rowIndex, colIndex);

		        } else { //another column
		            //You can omit this part if you know you don't 
		            //have any renderers that supply their own tool 
		            //tips.
		            tip = super.getToolTipText(e);
		        }
		        return tip;
		    }
		};
		tableGPSiesResult
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableGPSiesResult.setDefaultEditor(String.class, new DefaultCellEditor(
				new JTextField()));
        
		scrollPaneTable.setViewportView(tableGPSiesResult);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JLabel lblSearchResultLimit = new JLabel(International.getText("dlgGPSies.Search_limit")+":");
			lblSearchResultLimit.setHorizontalAlignment(SwingConstants.LEFT);
			buttonPane.add(lblSearchResultLimit);

			textFieldLimit = new JTextField();
			textFieldLimit.setText("20");
			buttonPane.add(textFieldLimit);
			textFieldLimit.setColumns(4);
			{
				JButton prevButton = new JButton("<");
				prevButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchPrevTracks();
					}
				});
				buttonPane.add(prevButton);
				
				JButton nextButton = new JButton(">");
				nextButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchNextTracks();
					}
				});
				buttonPane.add(nextButton);
				
				
				JButton okButton = new JButton(International.getText("dlgGPSies.Search_Tracks"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchTracks();
					}
				});
				okButton.setActionCommand(International.getText("OK"));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}

			JButton btnLoadSelectedTracks = new JButton(International.getText("dlgGPSies.Load_selected_Tracks"));
			btnLoadSelectedTracks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startLoading();
				}
			});
			buttonPane.add(btnLoadSelectedTracks);
			{
				JButton cancelButton = new JButton(International.getText("Close"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});

				btnStoppLoading = new JButton(International.getText("dlgGPSies.Stop_loading"));
				btnStoppLoading.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setStoppLoading(true);
					}
				});

				progressBarLoading = new JProgressBar();
				progressBarLoading.setPreferredSize(new Dimension(146, 26));
				progressBarLoading.setStringPainted(true);
				progressBarLoading.setToolTipText(International.getText("dlgGPSies.Progress_loading_selected_tracks"));
				buttonPane.add(progressBarLoading);
				buttonPane.add(btnStoppLoading);
				cancelButton.setActionCommand(International.getText("Cancel"));
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void startLoading() {
		progressBarLoading.setValue(0);
		new Thread(this).start();

	}

	protected void searchTracks() {
		// IMPORTANT!
		// The java source file of class GPSiesKey (which contains the api key) is not available.
		// Using the GPSies API requires a key. The key used in JGPSTrackEdit was
		// registered at gpsies.com by the author. You may not use that key
		// for other applications. If you want to reuse the source code of
		// package jgpstrackedit.gpsies, obtain your own key from www.gpsies.com
		StringBuilder urlString = new StringBuilder(
				"http://ws.gpsies.com/api.do?key="+GPSiesKey.GPSIESKEY);
		//
		if (rdbtnUserName.isSelected()) {
			urlString.append("&username=");
			urlString.append(textFieldUser.getText());
			appendCountry(urlString);
		}
		if (rdbtnCurrentMapView.isSelected()) {
			urlString.append("&BBOX=");
			Point getUpperLeftBoundary = Transform.getUpperLeftBoundary();
			Point getLowerRightBoundary = Transform.getLowerRightBoundary();
			urlString.append(getUpperLeftBoundary.getLongitudeAsString());
			urlString.append(",");
			urlString.append(getLowerRightBoundary.getLatitudeAsString());
			urlString.append(",");
			urlString.append(getLowerRightBoundary.getLongitudeAsString());
			urlString.append(",");
			urlString.append(getUpperLeftBoundary.getLatitudeAsString());
		}
		if (rdbtnCenterCurrentView.isSelected()) {
			Point getUpperLeftBoundary = Transform.getUpperLeftBoundary();
			Point getLowerRightBoundary = Transform.getLowerRightBoundary();
			urlString.append("&lat=");
			urlString
					.append(Parser.formatLatitude((getUpperLeftBoundary
							.getLatitude() + getLowerRightBoundary
							.getLatitude()) / 2.0));
			urlString.append("&lon=");
			urlString
					.append(Parser.formatLongitude((getUpperLeftBoundary
							.getLongitude() + getLowerRightBoundary
							.getLongitude()) / 2.0));
			urlString.append("&perimeter=");
			urlString.append(Parser.parseDouble(textFieldPerimeter.getText()));
		}
		if (rdbtnZip.isSelected()) {
			urlString.append("&zip=");
			urlString.append(textFieldZIP.getText());
			appendCountry(urlString);
		}
		if (rdbtnCity.isSelected()) {
			urlString.append("&city=");
			urlString.append(textFieldCity.getText());
			appendCountry(urlString);
		}
		if (chckbxTrackProperty.isSelected()) {
			urlString.append("&trackProperty=");
			urlString.append(comboBoxTrackProperty.getSelectedItem());
		}
		if (chckbxTrackTypes.isSelected()) {
			urlString.append("&trackTypes=");
			urlString.append(comboBoxTrackTypes.getSelectedItem());
		}
		urlString.append("&limit=");
		urlString.append(Parser.parseInt(textFieldLimit.getText()));
		urlString.append("&filetype=kml");
		
		this.lastSearchUrl = urlString.toString();
		this.loadedPage = 1;
		logger.info("GPSIES: " + this.lastSearchUrl);
		gpsiesResult = gpsiesGetResults(this.lastSearchUrl);
		this.setTableModel(tableGPSiesResult, gpsiesResult);
	}
	
	/**
	 * Set the the table columns content. Preserve the column width.
	 *  
	 * @param table
	 * @param newTableModel
	 */
	private void setTableModel(JTable table, AbstractTableModel newTableModel) {
		List<Integer> columnWidthList = new ArrayList<>(table.getColumnCount());
		
		for(int i=0; i<table.getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			columnWidthList.add(column.getWidth());
		}
		
		table.setModel(newTableModel);
		
		if(columnWidthList.size() == table.getColumnCount()) {
			for(int i=0; i<table.getColumnCount(); i++) {
				TableColumn column = table.getColumnModel().getColumn(i);
				column.setWidth(columnWidthList.get(i));
				table.getTableHeader().setResizingColumn(column);
			}
		}
	}
	
	/**
	 * Get the previous result page.
	 */
	protected void searchPrevTracks() {
		if(this.lastSearchUrl != null) {
			if(this.loadedPage > 1) {
				this.loadedPage -= 1;
				gpsiesResult = gpsiesGetResults(this.lastSearchUrl + String.format("&resultPage=%d", this.loadedPage));
				this.setTableModel(tableGPSiesResult, gpsiesResult);
			}
		}
	}
	
	/**
	 * Get the next result page.
	 */
	protected void searchNextTracks() {
		if(this.lastSearchUrl != null) {
			GPSiesResult gpsiesResultNextPage = gpsiesGetResults(this.lastSearchUrl + String.format("&resultPage=%d", this.loadedPage+1));
			if(gpsiesResultNextPage.getRowCount() > 0) {
				this.loadedPage += 1;
				gpsiesResult = gpsiesResultNextPage;
				this.setTableModel(tableGPSiesResult, gpsiesResult);
			}
		}
	}

	/**
	 * Get the GPSies results an parse.
	 * 
	 * @param urlString GPSies-API URL
	 * @return Results og the query
	 */
	protected GPSiesResult gpsiesGetResults(String urlString) {
		GPSiesResultHandlerImpl handler = new GPSiesResultHandlerImpl();
		GPSiesResultParser parser = new GPSiesResultParser(handler, null);
		URL url;
		try {
			url = new URL(urlString);
			parser.parse(url);
		} catch (Throwable e) {
			logger.error("Error while getting gpsies results!", e);
		}
		return handler.getResult();
	}

	protected void appendCountry(StringBuilder urlString) {
		if (chckbxCountry.isSelected()) {
			urlString.append("&country=");
			urlString.append(comboBoxCountry.getSelectedItem());
		}
	}

	/**
	 * @param stoppLoading
	 *            the stoppLoading to set
	 */
	protected void setStoppLoading(boolean stoppLoading) {
		this.stoppLoading = stoppLoading;
	}

	@Override
	public void run() {
		int[] selectedRows = tableGPSiesResult.getSelectedRows();
		for (int i = 0; !stoppLoading && i < selectedRows.length; i++) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				logger.warn("Thread interrupted!");
			}
			String downloadURL = gpsiesResult.get(selectedRows[i])
					.getDownloadlink();
			logger.info("GPSIES-Download: " + downloadURL);
			uiController.openTrack(downloadURL);
			progressBarLoading.setValue((int)((i+1)*100/selectedRows.length));
		}

	}
}
