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
	private HashMap<String, JEditorPane> productionLabels = new HashMap<String, JEditorPane>();
	private JPanel productionPanel;
	private ArrayList<JEditorPane> stockpileLabels = new ArrayList<JEditorPane>();
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
		stockpilePanel.setLayout(new BoxLayout(stockpilePanel, BoxLayout.Y_AXIS));
		if(stockTypes.size() > 0){
			stockpilePanel.add(new JLabel("Stockpiled: "));
			for(String type: stockTypes){
				JEditorPane stockLabel = new JEditorPane();
				stockLabel.setText(type + ": " + city.getStockpile(type));
				stockLabel.setMaximumSize(new Dimension(3000, 20));
				stockpileLabels.add(stockLabel);
				stockpilePanel.add(stockLabel);
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
		ArrayList<String> types = city.getStockpileTypes();
		if(types.size() > stockpileLabels.size()){
			ArrayList<String> labelTypes = new ArrayList<String>();
			for(int i = 0; i < stockpileLabels.size(); i++){
				labelTypes.add(stockpileLabels.get(i).getText().split(":")[0]);
			}
			if(labelTypes.size() == 0)
				stockpilePanel.add(new JLabel("Stockpiled: "));
			for(String s : types){
				if(!labelTypes.contains(s)){
					JEditorPane stockLabel = new JEditorPane();
					stockLabel.setText(s + ": " + city.getStockpile(s));
					stockLabel.setMaximumSize(new Dimension(3000, 20));
					stockpileLabels.add(stockLabel);
					stockpilePanel.add(stockLabel);
				}
			}
		}
		
		types = city.getProductionTypes();
		if(types.size() > productionLabels.size()){
//			ArrayList<String> labelTypes = new ArrayList<String>();
//			for(int i = 0; i < productionLabels.size(); i++){
//				labelTypes.add(productionLabels.get(i).getText().split(":")[0]);
//			}
			for(String s : types){
				if(productionLabels.get(s) == null){
					if(productionLabels.size() == 0)
						productionPanel.add(new JLabel("Production: "));
					productionPanel.add(this.buildProductionPanel(s));
				}
			}
		}
		
		for(JEditorPane label: stockpileLabels){
			String type = label.getText().split(":")[0];
			double amount = city.getStockpile(type);
			label.setText(type + ": " + amount); 
		}
		for(Entry<String, JEditorPane> label: productionLabels.entrySet()){
			String type = label.getKey();
			JEditorPane pane = label.getValue();
			double amount = city.getProduction(type);
			if(!pane.getText().equals(""+amount))
				pane.setText(""+amount); 
		}
	}

}
