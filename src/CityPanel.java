import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class CityPanel extends JPanel {

	private City city;
	private JLabel popLabel;
	private JLabel soldierLabel;
	private HashMap<String, JEditorPane> productionLabels = new HashMap<String, JEditorPane>();
	private JPanel productionPanel;
	private HashMap<String, JEditorPane> stockpileLabels = new HashMap<String, JEditorPane>();
	private JPanel stockpilePanel;
	
	public CityPanel(City city) {
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
		
		ArrayList<String> stockTypes = city.getStockpileTypes();
		stockpilePanel = new JPanel();
		stockpilePanel.setLayout(new BoxLayout(stockpilePanel, BoxLayout.Y_AXIS));
		if(stockTypes.size() > 0){
			stockpilePanel.add(new JLabel("Stockpiled: "));
			for(String type: stockTypes){
				stockpilePanel.add(buildStockpilePanel(type));
			}
		}
		
		add(stockpilePanel);
		
		ArrayList<String> productTypes = city.getProductionTypes();
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
			for(String type: productTypes){
				productionPanel.add(buildProductionPanel(type));
			}
		}
		add(productionPanel);
	}
	
	private JPanel buildStockpilePanel(String type){
		JPanel stock = new JPanel();
		stock.add(new JLabel(type+": "));
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
	
	
	private JPanel buildProductionPanel(String type) {
		JPanel product = new JPanel();
		product.add(new JLabel(type+": "));
		JEditorPane productionLabel = new JEditorPane();
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
		JLabel fundingLabel = new JLabel("Funding: " + city.getFunding());
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
		ArrayList<String> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()){
			for(String s : types){
				if(stockpileLabels.get(s) == null){
					if(stockpileLabels.size() == 0)
						stockpilePanel.add(new JLabel("Stockpile: "));
					stockpilePanel.add(this.buildStockpilePanel(s));
				}
			}
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()){
			for(String s : types){
				if(productionLabels.get(s) == null){
					if(productionLabels.size() == 0)
						productionPanel.add(new JLabel("Production: "));
					productionPanel.add(this.buildProductionPanel(s));
				}
			}
		}
		
		for(Entry<String, JEditorPane> label: stockpileLabels.entrySet()){
			String type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getStockpile(type);
			pane.setText(type + ": " + amount + " (" + 
					city.getInstruction(type, "stockpile") + ")"); 
		}
		for(Entry<String, JEditorPane> label: productionLabels.entrySet()){
			String type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getProduction(type);
			String productionString = (Game.trim(amount*city.getProductionPower())) + " (" + amount + "%)";
			if(!pane.getText().equals(productionString))
				pane.setText(productionString); 
		}
	}

}
