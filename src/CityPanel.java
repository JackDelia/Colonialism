import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CityPanel extends JPanel {

	private City city;
	private JTextArea cityText;
	
	public CityPanel(City city) {
		super();
		this.city = city;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setMaximumSize(new Dimension(200,100000));
		
		
		JSlider fundingSlider = new JSlider(1, 100);
		fundingSlider.setMaximumSize(new Dimension(170,20));
		fundingSlider.setValue((int)city.funding);
		fundingSlider.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		        	city.funding = source.getValue();
		        }
		        update();
			}
			
		});
		cityText = new JTextArea(city.toString());
		add(new JLabel("Funding:"));
		add(fundingSlider);
		add(cityText);
	}
	
	public void paintComponent(Graphics g){
		update();
	}
	
	public void update(){
		cityText.setText(String.valueOf(city.toString()));
	}

}
