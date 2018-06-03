package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.Resource;
import com.jackdelia.colonialism.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CityPanel extends JPanel {

	private City city;
	private JLabel popLabel;
	private JLabel soldierLabel;
	private HashMap<Resource, JEditorPane> productionLabels = new HashMap<Resource, JEditorPane>();
	private JPanel productionPanel;
	private HashMap<Resource, JEditorPane> stockpileLabels = new HashMap<Resource, JEditorPane>();
	private JPanel stockpilePanel;
	
	public CityPanel(final City city) {
		super();
		this.city = city;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setMaximumSize(new Dimension(200,100000));
		
		
		
		JLabel nameLabel = new JLabel(city.getName());
		nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 20));
		
		add(nameLabel);
		popLabel = new JLabel("Population: " + city.getSize());
		soldierLabel = new JLabel("Soldiers: " + city.getSoldiers());
		add(popLabel);
		add(soldierLabel);
		add(fundingPanel());
		
		ArrayList<Resource> stockTypes = city.getStockpileTypes();
		stockpilePanel = new JPanel();
		stockpilePanel.setLayout(new BoxLayout(stockpilePanel, BoxLayout.Y_AXIS));
		if(stockTypes.size() > 0){
			stockpilePanel.add(new JLabel("Stockpiled: "));
			for(Resource type: stockTypes){
				stockpilePanel.add(buildStockpilePanel(type));
			}
		}
		
		add(stockpilePanel);
		
		ArrayList<Resource> productTypes = city.getProductionTypes();
		productionPanel = new JPanel();
		productionPanel.setLayout(new BoxLayout(productionPanel, BoxLayout.PAGE_AXIS));
		if(productTypes.size() > 0){
			JButton balanceButton = new JButton("Balance");
			balanceButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					city.balanceProduction();
				}
			});
			productionPanel.add(new JLabel("Production: "));
			for(Resource type: productTypes){
				productionPanel.add(buildProductionPanel(type));
			}
		}
		add(productionPanel);
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showExportDialog();
			}
		});
		add(exportButton);
	}
	
	private void showExportDialog(){
		ArrayList<Resource> resourceTypes = city.getProductionTypes();
		String[] resources = new String[resourceTypes.size()];
		int i = 0;
		for(Resource r : resourceTypes){
			resources[i] = r.toString();
			i++;
		}
		
		JComboBox resChoice = new JComboBox(resources);
		
		ArrayList<City> cityList = city.getController().getCities();
		
		String[] cities = new String[cityList.size()-1];
		
		i = 0;
		for(City c : cityList){
			if(c != city){
				cities[i] = c.getName();
				i++;
			}
		}
		
		JComboBox cityChoice = new JComboBox(cities);
		
		JTextField field1 = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(resChoice);
        panel.add(cityChoice);
        panel.add(new JLabel("Percent of excess to be exported:"));
        panel.add(field1);
        
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
            	City chosenCity = city.getController().findCityByName(cityChoice.getSelectedItem().toString());
            	city.addExport(chosenCity, Resource.valueOf(resChoice.getSelectedItem().toString()), 
            			Double.parseDouble(field1.getText()));
            }  
	}
	
	private JPanel buildStockpilePanel(final Resource type){
		JPanel stock = new JPanel();
		stock.add(new JLabel(type.toString()+": "));
		JEditorPane stockLabel = new JEditorPane();
		stockLabel.setText(""+city.getStockpile(type));
		stockLabel.setMaximumSize(new Dimension(300,20));
		
		stockpileLabels.put(type, stockLabel);
		stockLabel.setBackground(Color.GRAY);
		JButton increaseStock= new JButton("^");
		increaseStock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.setInstruction(type, "stockpile", city.getInstruction(type, "stockpile")+1);
			}
		});
		
		JButton decreaseStock = new JButton("v");
		decreaseStock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.setInstruction(type, "stockpile", city.getInstruction(type, "stockpile")-1);
			}
		});
		
		stock.setLayout(new BoxLayout(stock, BoxLayout.X_AXIS));
		stock.add(stockLabel);
		stock.add(increaseStock);
		stock.add(decreaseStock);
		
		return stock;
	}
	
	
	private JPanel buildProductionPanel(final Resource type) {
		JPanel product = new JPanel();
		product.add(new JLabel(type+": "));
		final JEditorPane productionLabel = new JEditorPane();
		productionLabel.setText(""+city.getProduction(type));
		productionLabel.setMaximumSize(new Dimension(300,20));
		productionLabel.setEditable(true);
		
		productionLabels.put(type, productionLabel);
		productionLabel.setBackground(Color.GRAY);
		JButton increaseProduction = new JButton("^");
		increaseProduction.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.incrementProduction(type, 1);
				productionLabel.setText(type + ": " + city.getProduction(type));
			}
		});
		
		JButton decreaseProduction = new JButton("v");
		decreaseProduction.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.incrementProduction(type,-1);
				productionLabel.setText(type + ": " + city.getProduction(type));
			}
		});
		
		product.setLayout(new BoxLayout(product, BoxLayout.X_AXIS));
		product.add(productionLabel);
		product.add(increaseProduction);
		product.add(decreaseProduction);
		
		return product;
	}


	private JPanel fundingPanel(){
		JPanel funding = new JPanel();
		final JLabel fundingLabel = new JLabel("Funding: " + city.getFunding());
		funding.add(fundingLabel);
		JButton increaseFunding = new JButton("^");
		increaseFunding.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.incrementFunding(1);
				fundingLabel.setText("Funding: " + city.getFunding());
			}
		});
		
		JButton decreaseFunding = new JButton("v");
		decreaseFunding.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.incrementFunding(-1);
				fundingLabel.setText("Funding: " + city.getFunding());
			}
		});
		
		funding.setLayout(new BoxLayout(funding, BoxLayout.X_AXIS));
		funding.add(increaseFunding);
		funding.add(decreaseFunding);
		
		return funding;
	}
	
	public void paintComponent(Graphics g){
		update();
	}
	
	public void update(){
		popLabel.setText("Population: " + city.getSize());
		soldierLabel.setText("Soldiers: " + city.getSoldiers());
		ArrayList<Resource> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()){
			for(Resource s : types){
				if(stockpileLabels.get(s) == null){
					if(stockpileLabels.size() == 0)
						stockpilePanel.add(new JLabel("Stockpile: "));
					stockpilePanel.add(this.buildStockpilePanel(s));
				}
			}
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()){
			for(Resource s : types){
				if(productionLabels.get(s) == null){
					if(productionLabels.size() == 0)
						productionPanel.add(new JLabel("Production: "));
					productionPanel.add(this.buildProductionPanel(s));
				}
			}
		}
		
		for(Entry<Resource, JEditorPane> label: stockpileLabels.entrySet()){
			Resource type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getStockpile(type);
			pane.setText(Game.trim(amount) + " (" + 
					Game.trim(city.getInstruction(type, "stockpile")) + ")"); 
		}
		for(Entry<Resource, JEditorPane> label: productionLabels.entrySet()){
			Resource type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getProduction(type);
			String productionString = (Game.trim(amount*city.getProductionPower())) + " (" + Game.trim(amount) + "%)";
			if(!pane.getText().equals(productionString))
				pane.setText(productionString); 
		}
	}

}
