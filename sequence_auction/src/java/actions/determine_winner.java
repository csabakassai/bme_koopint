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

import java.util.Iterator;

public class determine_winner extends DefaultInternalAction {
	
	public enum AuctionMode {
		MINI_SUM,
		MINI_MAX
	}

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		// execute the internal action

		BeliefBase bb = ts.getAg().getBB();
		Literal roundLiteral = bb.getCandidateBeliefs(new PredicateIndicator("round", 1)).next();
		int round = (int) ((NumberTerm) roundLiteral.getTerm(0)).solve();
		ts.getAg().getLogger().info("Determining winner in round: " + round);

		Iterator<Literal> bids = bb.getCandidateBeliefs(new PredicateIndicator("my_bid", 4));
		
		System.out.println(bids.hasNext());
		PredicateIndicator indicator = new PredicateIndicator("auction_mode", 1);
		AuctionMode auctionMode = AuctionMode.valueOf(bb.getCandidateBeliefs(indicator).next().getTerm(0).toString());


		double min = -1;
		Term winner = null;
		Term task = null;
		while (bids.hasNext()) {
			Literal bid = bids.next();
			int roundInBid = (int) ((NumberTerm) bid.getTerm(3)).solve();
			if (roundInBid == round) {
				
				double bidValue;
				switch (auctionMode) {
					case MINI_MAX:
						bidValue = ((NumberTerm) bid.getTerm(2)).solve();
	
						break;
					case MINI_SUM:
						bidValue = ((NumberTerm) bid.getTerm(1)).solve();
						break;
					default:
						throw new IllegalArgumentException("Unsupported auction mode: " + auctionMode);
					}
				if (min < 0 || bidValue < min) {
					min = bidValue;
					winner = bid.getSources().get(0);
					task = bid.getTerm(0);
				}
			}
		}

		System.out.println("Min:" +min);
		System.out.println("Winner: " + winner);
		System.out.println("Task: " + task);
		return un.unifies(args[0], winner) &&
				un.unifies(args[1], task) &&
				un.unifies(args[2], new NumberTermImpl(min));
	}
}
