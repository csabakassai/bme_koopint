// Internal action code for project hf4

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.PredicateIndicator;
import jason.asSyntax.Term;
import jason.bb.BeliefBase;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import env.JasonUtil;

public class determine_winner extends DefaultInternalAction {
	
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
		// execute the internal action
		
		BeliefBase bb = ts.getAg().getBB();
		Literal roundLiteral = bb.getCandidateBeliefs( new PredicateIndicator( "round", 1 ) ).next();
		int round = (int) ((NumberTerm) roundLiteral.getTerm( 0 )).solve();
		
		Iterator<Literal> bids = bb.getCandidateBeliefs( new PredicateIndicator( "my_bid", 2 ) );
		
		List<Bid> bidList = Lists.newArrayList();
		while (bids.hasNext()) {
			Literal bid = bids.next();
			int roundInBid = (int) ((NumberTerm) bid.getTerm( 1 )).solve();
			if (roundInBid == round) {
				
				double bidValue = JasonUtil.extractNumberFromTerm( bid.getTerm( 0 ) );
				Term agent = bid.getSources().get( 0 );
				bidList.add( new Bid( agent, bidValue ) );
			}
		}
		
		Collections.sort( bidList );
		int maxIndex = bidList.size() - 1;
		int prizeIndex = maxIndex - 1;
		double prize = bidList.get( prizeIndex ).bidValue;
		
		Bid winnerBid = bidList.get( maxIndex );
		Term winner = winnerBid.agent;
		
		ts.getAg().getLogger().info( "Winner is " + winner + " with prize " + prize + " in round " + round );
		return un.unifies( args[0], winner ) &&
			un.unifies( args[1], new NumberTermImpl( prize ) );
	}
	
	private class Bid implements Comparable<Bid> {
		
		private final Term agent;
		
		private final Double bidValue;
		
		private Bid(Term agent, Double bidValue) {
			this.agent = agent;
			this.bidValue = bidValue;
		}
		
		public int compareTo( Bid o ) {
			return bidValue.compareTo( o.bidValue );
		}
		
	}
}
