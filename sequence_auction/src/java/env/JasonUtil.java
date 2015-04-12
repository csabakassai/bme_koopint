package env;

import jason.asSyntax.ListTerm;
import jason.asSyntax.Term;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JasonUtil {
	
	
	public static List<String> convertListTermToList(ListTerm listTerm) {
		Preconditions.checkNotNull(listTerm);
		List<Term> asList = listTerm.getAsList();
		
		List<String> result = Lists.newArrayList();
		for (Term term : asList) {
			result.add(term.toString());
		}
		
		return result;
	}

}
