package env;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import com.google.common.collect.Maps;

public class VickreyView extends JFrame {
	
	private final VickreyEnvironment environment;
	
	private DefaultTableModel utilitiesTableModel;
	
	private Map<String, BidderPanel> bidderPanels = Maps.newHashMap();
	
	private final JSpinner roundNumber;
	
	public VickreyView(VickreyEnvironment environment) {
		super();
		this.environment = environment;
		setSize( 800, 200 );
		getContentPane().setLayout( new GridLayout( 6, 1 ) );
		
		add( new HeaderPanel() );
		
		Map<String, LinkedList<Double>> utilities = environment.getUtilities();
		Set<Entry<String, LinkedList<Double>>> entrySet = utilities.entrySet();
		for (Entry<String, LinkedList<Double>> entry : entrySet) {
			Double utility = entry.getValue().getLast();
			String agentName = entry.getKey();
			BidderPanel bidderPanel = new BidderPanel( agentName, utility );
			add( bidderPanel );
			bidderPanels.put( agentName, bidderPanel );
		}
		
		SpinnerModel roundModel = new SpinnerNumberModel( 1000, 1, 5000, 1 );
		roundNumber = new JSpinner( roundModel );
		RoundLimitPanel roundLimitPanel = new RoundLimitPanel();
		roundLimitPanel.add( roundNumber );
		add( roundLimitPanel );
		JButton startButton = new JButton( "Start" );
		startButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent e ) {
				Map<String, Double> maximums = VickreyView.this.environment.getMaximums();
				Map<String, Double> minimums = VickreyView.this.environment.getMinimums();
				
				for (BidderPanel bidderPanel : VickreyView.this.bidderPanels.values()) {
					maximums.put( bidderPanel.agentName, (Double) bidderPanel.max.getValue() );
					minimums.put( bidderPanel.agentName, (Double) bidderPanel.min.getValue() );
					
				}
				VickreyView.this.environment.setRoundLimit( (Integer) roundNumber.getValue() );
				VickreyView.this.environment.startAuction();
				
			}
		} );
		
		add( startButton );
		setVisible( true );
		repaint();
		
	}
	
	@Override
	public void repaint() {
		Map<String, LinkedList<Double>> utilities = environment.getUtilities();
		Set<Entry<String, LinkedList<Double>>> entrySet = utilities.entrySet();
		for (Entry<String, LinkedList<Double>> entry : entrySet) {
			String agentName = entry.getKey();
			Double utility = entry.getValue().getLast();
			BidderPanel bidderPanel = bidderPanels.get( agentName );
			bidderPanel.setUtility( utility );
		}
		super.repaint();
	}
	
	private class RoundLimitPanel extends JPanel {
		
		public RoundLimitPanel() {
			this.setLayout( new GridLayout( 1, 2 ) );
			add( new JLabel( "Round limit: " ) );
			
		}
		
	}
	
	private class HeaderPanel extends JPanel {
		
		public HeaderPanel() {
			this.setLayout( new GridLayout( 1, 4 ) );
			add( new JLabel( "Agent" ) );
			add( new JLabel( "Utility" ) );
			add( new JLabel( "Min" ) );
			add( new JLabel( "Max" ) );
			
		}
		
	}
	
	private class BidderPanel extends JPanel {
		
		private final String agentName;
		
		private double utility;
		
		private JLabel utitlityLabel;
		
		private JSpinner min;
		
		private JSpinner max;
		
		public BidderPanel(String agentName, double utility) {
			super();
			this.agentName = agentName;
			this.utility = utility;
			this.setLayout( new GridLayout( 1, 4 ) );
			JLabel jLabel = new JLabel( agentName );
			add( jLabel );
			utitlityLabel = new JLabel( "" + utility );
			add( utitlityLabel );
			
			SpinnerModel minModel = new SpinnerNumberModel( 0.0, 0.0, 1.0, 0.05 );
			min = new JSpinner( minModel );
			add( min );
			
			SpinnerModel maxModel = new SpinnerNumberModel( 1.0, 0.0, 1.0, 0.05 );
			max = new JSpinner( maxModel );
			add( max );
		}
		
		public void setUtility( double utility ) {
			this.utility = utility;
			utitlityLabel.setText( "" + utility );
		}
		
	}
	
}
