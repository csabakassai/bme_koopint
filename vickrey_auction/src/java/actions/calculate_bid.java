// Internal action code for project vickrey_auction

package actions;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import java.util.Random;
import java.util.logging.Logger;

import env.JasonUtil;

public class calculate_bid extends DefaultInternalAction {
	
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
		Agent agent = ts.getAg();
		Logger logger = agent.getLogger();
		
		Literal minBidLiteral = JasonUtil.extractSingleBelief( "min", 1, agent );
		double min = JasonUtil.extractNumberFromTerm( minBidLiteral.getTerm( 0 ) );
		
		Literal maxBidLiteral = JasonUtil.extractSingleBelief( "max", 1, agent );
		double max = JasonUtil.extractNumberFromTerm( maxBidLiteral.getTerm( 0 ) );
		
		Random random = new Random();
		double randomValue = random.nextDouble();
		
		double bid = (randomValue * (max - min)) + min;
		logger.info( "My bid is: " + bid );
		
		un.unifies( args[0], new NumberTermImpl( bid ) );
		
		return true;
	}
}
