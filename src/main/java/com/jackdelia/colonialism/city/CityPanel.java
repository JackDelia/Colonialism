package com.jackdelia.colonialism.city;

import com.jackdelia.colonialism.map.resource.Resource;
import com.jackdelia.colonialism.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 */
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
     */
	private CityPanel() {
        super();
        // initialize class attributes
        this.productionLabels = new HashMap<>();
        this.stockpileLabels = new HashMap<>();

    }

    /**
     * Factory Method to handle the creation
     * @param city the City to be associated with this Panel
     * @return constructed instance of a CityPanel
     */
	public static CityPanel create(final City city) {

	    CityPanel constructedCityPanel = new CityPanel();


        constructedCityPanel.setCity(city);

        constructedCityPanel.setLayout(new BoxLayout(constructedCityPanel, BoxLayout.PAGE_AXIS));
        constructedCityPanel.setMaximumSize(new Dimension(200,100000));


        // setup city status panel
        JLabel nameLabel = new JLabel(city.getName());
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 20));
        constructedCityPanel.add(nameLabel);
        constructedCityPanel.popLabel = new JLabel(String.format("Population: %d", city.getSize()));
        constructedCityPanel.soldierLabel = new JLabel(String.format("Soldiers: %d", city.getSoldiers()));
        constructedCityPanel.add(constructedCityPanel.popLabel);
        constructedCityPanel.add(constructedCityPanel.soldierLabel);
        constructedCityPanel.add(constructedCityPanel.fundingPanel());

        // setup stockpile panel
        ArrayList<Resource> stockTypes = city.getStockpileTypes();
        constructedCityPanel.stockpilePanel = new JPanel();
        constructedCityPanel.stockpilePanel.setLayout(new BoxLayout(constructedCityPanel.stockpilePanel, BoxLayout.Y_AXIS));

        if(stockTypes.size() > 0) {
            constructedCityPanel.stockpilePanel.add(new JLabel("Stockpiled: "));

            for(Resource type: stockTypes) {
                constructedCityPanel.stockpilePanel.add(constructedCityPanel.buildStockpilePanel(type));
            }

        }

        constructedCityPanel.add(constructedCityPanel.stockpilePanel);

        // setup production panel
        ArrayList<Resource> productTypes = city.getProductionTypes();
        constructedCityPanel.productionPanel = new JPanel();
        constructedCityPanel.productionPanel.setLayout(new BoxLayout(constructedCityPanel.productionPanel, BoxLayout.PAGE_AXIS));
        if(productTypes.size() > 0){
            JButton balanceButton = new JButton("Balance");

            balanceButton.addActionListener(e -> city.balanceProduction());

            constructedCityPanel.productionPanel.add(new JLabel("Production: "));
            for(Resource type: productTypes){
                constructedCityPanel.productionPanel.add(constructedCityPanel.buildProductionPanel(type));
            }
        }
        constructedCityPanel.add(constructedCityPanel.productionPanel);

        // setup export button
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> constructedCityPanel.showExportDialog());
        constructedCityPanel.add(exportButton);

        return constructedCityPanel;
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
		
		ArrayList<City> cityList = this.city.getPlayer().getCities();
		
		String[] cities = new String[cityList.size()-1];
		
		i = 0;
		for(City curCity : cityList) {
			if(curCity != this.city) {
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
            	City chosenCity = this.city.getPlayer().findCityByName(Objects.requireNonNull(cityChoice.getSelectedItem()).toString());
                this.city.addExport(chosenCity, Resource.valueOf(Objects.requireNonNull(resChoice.getSelectedItem()).toString()),
            			Double.parseDouble(field1.getText()));
            }  
	}
	
	private JPanel buildStockpilePanel(final Resource type){
		JPanel stock = new JPanel();
		stock.add(new JLabel(String.format("%s: ", type.toString())));
		JEditorPane stockLabel = new JEditorPane();
		stockLabel.setText(String.format("%s", this.city.getStockpile(type)));
		stockLabel.setMaximumSize(new Dimension(300,20));

        this.stockpileLabels.put(type, stockLabel);
		stockLabel.setBackground(Color.GRAY);

		// setup increase stock button
		JButton increaseStock= new JButton("^");
		increaseStock.addActionListener((ActionEvent e) ->
                city.setInstruction(type, "stockpile", city.getInstruction(type, "stockpile") + 1)
        );

		// setup decrease stock button
		JButton decreaseStock = new JButton("v");
		decreaseStock.addActionListener((ActionEvent e) ->
				city.setInstruction(type, "stockpile", city.getInstruction(type, "stockpile")-1)
        );
		
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
		increaseProduction.addActionListener((ActionEvent e) -> {
            city.incrementProduction(type, 1);
            productionLabel.setText(type + ": " + city.getProduction(type));
        });
		
		JButton decreaseProduction = new JButton("v");
		decreaseProduction.addActionListener((ActionEvent e) -> {
            city.incrementProduction(type,-1);
            productionLabel.setText(type + ": " + city.getProduction(type));
        });
		
		product.setLayout(new BoxLayout(product, BoxLayout.X_AXIS));
		product.add(productionLabel);
		product.add(increaseProduction);
		product.add(decreaseProduction);
		
		return product;
	}


	private JPanel fundingPanel(){
		JPanel funding = new JPanel();
		final JLabel fundingLabel = new JLabel(String.format("Funding: %s", city.getFunding()));
		funding.add(fundingLabel);
		JButton increaseFunding = new JButton("^");
		increaseFunding.addActionListener(e -> {
            city.incrementFunding(1);
            fundingLabel.setText(String.format("Funding: %s", city.getFunding()));
        });
		
		JButton decreaseFunding = new JButton("v");
		decreaseFunding.addActionListener((ActionEvent e) -> {
            city.incrementFunding(-1);
            fundingLabel.setText(String.format("Funding: %s", city.getFunding()));
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
		popLabel.setText(String.format("Population: %d", city.getSize()));
		soldierLabel.setText(String.format("Soldiers: %d", city.getSoldiers()));
		ArrayList<Resource> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()) {
            types.stream()
                    .filter(this::notNullStockpileResource)
                    .forEach(this::addResourceAsLabelToPanel);
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()) {
            types.stream()
                    .filter(this::notNullProductionLabel)
                    .forEach(this::addProductionAsLabelToPanel);
		}

        stockpileLabels.forEach(this::setStockpileQuantityOnPanel);

        productionLabels.forEach(this::setProductionQuantityOnPanel);
	}

    public void setCity(City city) {
        this.city = city;
    }

    private void addResourceAsLabelToPanel(Resource s) {
        if (stockpileLabels.size() == 0) {
            stockpilePanel.add(new JLabel("Stockpile: "));
        }
        stockpilePanel.add(this.buildStockpilePanel(s));
    }

    private boolean notNullStockpileResource(Resource s) {
        return stockpileLabels.get(s) == null;
    }

    private void addProductionAsLabelToPanel(Resource s) {
        if (productionLabels.size() == 0) {
            productionPanel.add(new JLabel("Production: "));
        }
        productionPanel.add(this.buildProductionPanel(s));
    }

    private boolean notNullProductionLabel(Resource s) {
        return productionLabels.get(s) == null;
    }

    private void setStockpileQuantityOnPanel(Resource type, JEditorPane pane) {
        double amount = city.getStockpile(type);
        pane.setText(String.format("%s (%s)", Game.trim(amount), Game.trim(city.getInstruction(type, "stockpile"))));
    }

    private void setProductionQuantityOnPanel(Resource type, JEditorPane pane) {
        double amount = city.getProduction(type);
        String productionString = String.format("%s (%s%%)", Game.trim(amount * city.getProductionPower()), Game.trim(amount));
        if (!pane.getText().equals(productionString)) {
            pane.setText(productionString);
        }
    }
}
