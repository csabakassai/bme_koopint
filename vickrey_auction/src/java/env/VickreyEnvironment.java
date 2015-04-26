package env;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class VickreyEnvironment extends Environment {
	
	private final VickreyView view;
	
	private final Map<String, LinkedList<Double>> utilities = Maps.newHashMap();
	
	private final Map<String, Double> minimums = Maps.newHashMap();
	
	private final Map<String, Double> maximums = Maps.newHashMap();
	
	private Integer roundLimit = 1;
	
	public VickreyEnvironment() {
		super();
		LinkedList<Double> list1 = Lists.newLinkedList();
		list1.add( 0.0 );
		utilities.put( "bidder1", list1 );
		LinkedList<Double> list2 = Lists.newLinkedList();
		list2.add( 0.0 );
		utilities.put( "bidder2", list2 );
		LinkedList<Double> list3 = Lists.newLinkedList();
		list3.add( 0.0 );
		utilities.put( "bidder3", list3 );
		
		view = new VickreyView( this );
		
	}
	
	@Override
	public boolean executeAction( String agName, Structure act ) {
		
//		System.out.println( act + "[" + agName + "]" );
		
		String functor = act.getFunctor();
		if (functor.equals( "share" )) {
			
			Term utilityTerm = act.getTerm( 0 );
			double utility = JasonUtil.extractNumberFromTerm( utilityTerm );
			
			Set<String> agents = utilities.keySet();
			for (String agent : agents) {
				LinkedList<Double> utilityList = utilities.get( agent );
				if (utilityList == null) {
					utilityList = new LinkedList<Double>();
					utilityList.add( 0.0 );
					utilities.put( agent, utilityList );
				}
				if (agent.equals( agName )) {
					utilityList.add( utility );
				} else {
					
					utilityList.add( utilityList.getLast() );
				}
			}
			
			view.repaint();
			return true;
		} else {
			return super.executeAction( agName, act );
		}
	}
	
	public Map<String, LinkedList<Double>> getUtilities() {
		return utilities;
	}
	
	public Map<String, Double> getMinimums() {
		return minimums;
	}
	
	public Map<String, Double> getMaximums() {
		return maximums;
	}
	
	public void startAuction() {
		for (Entry<String, Double> entry : maximums.entrySet()) {
			addPercept( entry.getKey(), Literal.parseLiteral( "max(" + entry.getValue() + ")" ) );
			System.out.println( entry.getKey() + Literal.parseLiteral( "max(" + entry.getValue() + ")" ) );
		}
		
		for (Entry<String, Double> entry : minimums.entrySet()) {
			addPercept( entry.getKey(), Literal.parseLiteral( "min(" + entry.getValue() + ")" ) );
		}
		addPercept( "auctioneer", Literal.parseLiteral( "roundLimit(" + roundLimit + ")" ) );
		
		addPercept( "auctioneer", Literal.parseLiteral( "environmentReady" ) );
		
	}
	
	public void setRoundLimit( Integer value ) {
		this.roundLimit = value;
		
	}
	
}
