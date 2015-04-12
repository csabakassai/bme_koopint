// Internal action code for project hf4

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.PredicateIndicator;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;
import jason.bb.BeliefBase;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import env.JasonUtil;

public class available_tasks extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		// execute the internal action
		ts.getAg().getLogger().info("executing internal action 'actions.available_tasks'");
		BeliefBase bb = ts.getAg().getBB();
		
		
		Iterator<Literal> allTasksLiteral = bb.getCandidateBeliefs(new PredicateIndicator("task", 3));
		
		Set<String> allTasks = Sets.newHashSet();
		
		while (allTasksLiteral.hasNext()) {
			Literal literal = allTasksLiteral.next();
			allTasks.add(literal.getTerm(0).toString());
		}

		
		Literal allocatedTasksLiteral = bb.getCandidateBeliefs(new PredicateIndicator("allocated_tasks", 1)).next();
		List<String> allocatedTasks = JasonUtil.convertListTermToList((ListTerm)allocatedTasksLiteral.getTerm(0));
		
		HashSet<String> allocatedTasksSet = Sets.newHashSet(allocatedTasks);
		
		SetView<String> difference = Sets.difference(allTasks, allocatedTasksSet);
		
		ListTermImpl availableTasks = new ListTermImpl();
		for (String taskId : difference) {
			availableTasks.add(new StringTermImpl(taskId));
		}
		
		return un.unifies(args[0], availableTasks) && !difference.isEmpty();
	}
}
