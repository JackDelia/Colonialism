import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CityPanel extends JPanel {

	private City city;
	private JLabel popLabel;
	private ArrayList<JLabel> productionLabels = new ArrayList<JLabel>();
	private JPanel productionPanel;
	private ArrayList<JLabel> stockpileLabels = new ArrayList<JLabel>();
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
		add(popLabel);
		add(fundingPanel());
		
		ArrayList<String> stockTypes = city.getStockpileTypes();
		stockpilePanel = new JPanel();
		stockpilePanel.setLayout(new BoxLayout(stockpilePanel, BoxLayout.PAGE_AXIS));
		if(stockTypes.size() > 0){
			stockpilePanel.add(new JLabel("Stockpiled: "));
			for(String type: stockTypes){
				JLabel stockLabel = new JLabel(type + ": " + city.getStockpile(type));
				stockpileLabels.add(stockLabel);
				stockpilePanel.add(stockLabel);
			}
			add(stockpilePanel);
		}
		
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
	
	
	private JPanel buildProductionPanel(String type) {
		JPanel product = new JPanel();
		JLabel productionLabel = new JLabel(type + ": " + city.getProduction(type));
		productionLabels.add(productionLabel);
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
		ArrayList<String> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()){
			ArrayList<String> labelTypes = new ArrayList<String>();
			for(int i = 0; i < stockpileLabels.size(); i++){
				labelTypes.add(stockpileLabels.get(i).getText().split(":")[0]);
			}
			for(String s : types){
				if(!labelTypes.contains(s)){
					JLabel stockLabel = new JLabel(s + ": " + city.getStockpile(s));
					stockpileLabels.add(stockLabel);
					stockpilePanel.add(stockLabel);
				}
			}
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()){
			ArrayList<String> labelTypes = new ArrayList<String>();
			for(int i = 0; i < productionLabels.size(); i++){
				labelTypes.add(productionLabels.get(i).getText().split(":")[0]);
			}
			for(String s : types){
				if(!labelTypes.contains(s)){
					productionPanel.add(this.buildProductionPanel(s));
				}
			}
		}
		
		for(JLabel label: stockpileLabels){
			String type = label.getText().split(":")[0];
			double amount = city.getStockpile(type);
			label.setText(type + ": " + amount); 
		}
		for(JLabel label: productionLabels){
			String type = label.getText().split(":")[0];
			double amount = city.getProduction(type);
			label.setText(type + ": " + amount); 
		}
	}

}
