package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.resource.Resource;
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
import java.util.Objects;

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
	private HashMap<Resource, JEditorPane> productionLabels;
	private JPanel productionPanel;
	private HashMap<Resource, JEditorPane> stockpileLabels;
	private JPanel stockpilePanel;

    /**
     * Default Constructor
     * View for a City
     *
     * @param city the city model to be displayed
     */
	public CityPanel(final City city) {
        super();

        // initialize class attributes
        this.productionLabels = new HashMap<>();
        this.stockpileLabels = new HashMap<>();

		this.city = city;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setMaximumSize(new Dimension(200,100000));
		
		
		// setup city status panel
		JLabel nameLabel = new JLabel(city.getName());
		nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 20));
		add(nameLabel);
        this.popLabel = new JLabel("Population: " + city.getSize());
        this.soldierLabel = new JLabel("Soldiers: " + city.getSoldiers());
		add(this.popLabel);
		add(this.soldierLabel);
		add(fundingPanel());

		// setup stockpile panel
		ArrayList<Resource> stockTypes = city.getStockpileTypes();
        this.stockpilePanel = new JPanel();
        this.stockpilePanel.setLayout(new BoxLayout(this.stockpilePanel, BoxLayout.Y_AXIS));

		if(stockTypes.size() > 0) {
            this.stockpilePanel.add(new JLabel("Stockpiled: "));

			for(Resource type: stockTypes) {
                this.stockpilePanel.add(buildStockpilePanel(type));
			}

		}

		add(this.stockpilePanel);

		// setup production panel
		ArrayList<Resource> productTypes = city.getProductionTypes();
        this.productionPanel = new JPanel();
        this.productionPanel.setLayout(new BoxLayout(this.productionPanel, BoxLayout.PAGE_AXIS));
		if(productTypes.size() > 0){
			JButton balanceButton = new JButton("Balance");

			balanceButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					city.balanceProduction();
				}
			});

            this.productionPanel.add(new JLabel("Production: "));
			for(Resource type: productTypes){
                this.productionPanel.add(buildProductionPanel(type));
			}
		}
		add(this.productionPanel);

		// setup export button
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showExportDialog();
			}
		});
		add(exportButton);

	}
	
	private void showExportDialog() {
		ArrayList<Resource> resourceTypes = this.city.getProductionTypes();
		String[] resources = new String[resourceTypes.size()];
		int i = 0;
		for(Resource r : resourceTypes){
			resources[i] = r.toString();
			i++;
		}
		
		JComboBox<String> resChoice = new JComboBox<>(resources);
		
		ArrayList<City> cityList = this.city.getController().getCities();
		
		String[] cities = new String[cityList.size()-1];
		
		i = 0;
		for(City curCity : cityList){
			if(curCity != this.city){
				cities[i] = curCity.getName();
				i++;
			}
		}
		
		JComboBox<String> cityChoice = new JComboBox<>(cities);
		
		JTextField field1 = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(resChoice);
        panel.add(cityChoice);
        panel.add(new JLabel("Percent of excess to be exported:"));
        panel.add(field1);
        
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
            	City chosenCity = this.city.getController().findCityByName(Objects.requireNonNull(cityChoice.getSelectedItem()).toString());
                this.city.addExport(chosenCity, Resource.valueOf(Objects.requireNonNull(resChoice.getSelectedItem()).toString()),
            			Double.parseDouble(field1.getText()));
            }  
	}
	
	private JPanel buildStockpilePanel(final Resource type){
		JPanel stock = new JPanel();
		stock.add(new JLabel(type.toString() + ": "));
		JEditorPane stockLabel = new JEditorPane();
		stockLabel.setText("" + this.city.getStockpile(type));
		stockLabel.setMaximumSize(new Dimension(300,20));

        this.stockpileLabels.put(type, stockLabel);
		stockLabel.setBackground(Color.GRAY);

		// setup increase stock button
		JButton increaseStock= new JButton("^");
		increaseStock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				city.setInstruction(type, "stockpile", city.getInstruction(type, "stockpile")+1);
			}
		});

		// setup decrease stock button
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
		product.add(new JLabel(type + ": "));
		final JEditorPane productionLabel = new JEditorPane();
		productionLabel.setText("" + city.getProduction(type));
		productionLabel.setMaximumSize(new Dimension(300,20));
		productionLabel.setEditable(true);

        this.productionLabels.put(type, productionLabel);
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
	
	private void update(){
		popLabel.setText("Population: " + city.getSize());
		soldierLabel.setText("Soldiers: " + city.getSoldiers());
		ArrayList<Resource> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()) {
			for(Resource s : types) {
				if(stockpileLabels.get(s) == null) {
					if(stockpileLabels.size() == 0) {
                        stockpilePanel.add(new JLabel("Stockpile: "));
                    }
					stockpilePanel.add(this.buildStockpilePanel(s));
				}
			}
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()) {
			for(Resource s : types) {
				if(productionLabels.get(s) == null) {
					if(productionLabels.size() == 0) {
                        productionPanel.add(new JLabel("Production: "));
                    }
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
		for(Entry<Resource, JEditorPane> label: productionLabels.entrySet()) {
			Resource type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getProduction(type);
			String productionString = (Game.trim(amount*city.getProductionPower())) + " (" + Game.trim(amount) + "%)";
			if(!pane.getText().equals(productionString)) {
                pane.setText(productionString);
            }
		}
	}

}
