package env;

import jason.NoValueException;
import jason.asSemantics.Agent;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.PredicateIndicator;
import jason.asSyntax.Term;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JasonUtil {
	
	public static List<String> convertListTermToList( ListTerm listTerm ) {
		Preconditions.checkNotNull( listTerm );
		List<Term> asList = listTerm.getAsList();
		
		List<String> result = Lists.newArrayList();
		for (Term term : asList) {
			result.add( term.toString() );
		}
		
		return result;
	}
	
	public static double extractNumberFromTerm( Term term ) {
		Preconditions.checkNotNull( term );
		Preconditions.checkArgument( term instanceof NumberTerm, term.toString()
			+ " is not a NumberTerm" );
		NumberTerm numberTerm = (NumberTerm) term;
		try {
			return numberTerm.solve();
		} catch (NoValueException e) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static Literal extractSingleBelief( String functor, int arity, Agent agent ) {
		Iterator<Literal> candidateBeliefs = agent.getBB().getCandidateBeliefs( new PredicateIndicator( functor, arity ) );
		if (!candidateBeliefs.hasNext()) {
			agent.getLogger().severe( "There is no min bid belief" );
			return null;
		} else {
			return candidateBeliefs.next();
		}
		
	}
	
}
