// Internal action code for project hf4

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.PredicateIndicator;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import jason.bb.BeliefBase;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import env.JasonUtil;

public class create_bid extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		// execute the internal action
		ts.getAg().getLogger().info("Bidding...");

		

		BeliefBase bb = ts.getAg().getBB();

		Literal positionLiteral = bb.getCandidateBeliefs(new PredicateIndicator("position", 2)).next();

		int positionX = (int) ((NumberTerm) positionLiteral.getTerm(0)).solve();
		int positionY = (int) ((NumberTerm) positionLiteral.getTerm(1)).solve();
		Point robotPosition = new Point(positionX, positionY);

		Iterator<Literal> tasks = bb.getCandidateBeliefs(new PredicateIndicator("task", 3));
		
		Map<String, Point> taskPositions = Maps.newHashMap();
		
		while (tasks.hasNext()) {
			Literal literal = tasks.next();
			int x = (int) ((NumberTerm) literal.getTerm(1)).solve();
			int y = (int) ((NumberTerm) literal.getTerm(2)).solve();
			Point taskPosition = new Point(x, y);
			taskPositions.put(literal.getTerm(0).toString(), taskPosition);
		}

		
		Literal myTasksLiteral = bb.getCandidateBeliefs(new PredicateIndicator("my_tasks", 1)).next();
		List<String> myTasks = JasonUtil.convertListTermToList((ListTerm)myTasksLiteral.getTerm(0));
		
		double currentPath = calculatePath(robotPosition, myTasks, taskPositions);
		
		List<Term> availableTasksTerms = ((ListTerm) args[0]).getAsList();

		Set<String> availableTasks = Sets.newHashSet();

		for (Term term : availableTasksTerms) {
			String taskId = ((StringTerm) term).getString();
			availableTasks.add(taskId);
		}
		
		
		double bestPath = -1;
		String bestTask = null;
		List<String> bestBid = null;
		for (String availableTask : availableTasks) {
			
			PathCalculatingResponse response = calculateBestPath(robotPosition, myTasks, availableTask, taskPositions);
			double calculateBestPath = response.getDistance();
			
			
			if(bestPath == -1 || calculateBestPath < bestPath) {
				bestTask = availableTask;
				bestPath = calculateBestPath;
				bestBid = response.getTasks();
			}
		}
		
		double delta = bestPath - currentPath;

		boolean task = un.unifies(args[1], new Atom(bestTask));

		boolean deltaDistance = un.unifies(args[2], new NumberTermImpl(delta));
		
		boolean sumDistance = un.unifies(args[3], new NumberTermImpl(bestPath));
		
		ListTermImpl biddedTaskTerm = new ListTermImpl();
		for (String biddedTask : bestBid) {
			
			biddedTaskTerm.add(new Atom(biddedTask));
		}
		
		
		boolean biddedTasks = un.unifies(args[4], biddedTaskTerm);
		
		return task && deltaDistance && sumDistance && biddedTasks ;

	}
	
	private PathCalculatingResponse calculateBestPath(Point robotPosition, List<String> currentPath, String newTask, Map<String, Point> taskPositions) {
		
		double bestPathLength = -1;
		List<String> bestPath = null;
		for(int i = 0; i<=currentPath.size(); i++) {
			ArrayList<String> tempPath = Lists.newArrayList(currentPath);
			tempPath.add(i, newTask);
			
			double newDistance = calculatePath(robotPosition, tempPath, taskPositions);
			
			if(bestPathLength == -1 || bestPathLength > newDistance) {
				bestPathLength = newDistance;
				bestPath = tempPath;
			}
		}
		
		return new PathCalculatingResponse(bestPath, bestPathLength);
		
	}
	
	private double calculatePath(Point robotPosition, List<String> tasks, Map<String, Point> taskPositions) {
		double currentPath = 0;

		Point currentPosition = robotPosition;

		for (String string : tasks) {
			Point taskPosition = taskPositions.get(string);
			Preconditions.checkNotNull(taskPosition, string);
			double distance = currentPosition.distance(taskPosition);
			currentPath = currentPath + distance;
			currentPosition = taskPosition;
		}
		
		return currentPath;
	}
	
	private class PathCalculatingResponse {
		
		private List<String> tasks;
		private double distance;
		
		
		public PathCalculatingResponse(List<String> tasks, double distance) {
			super();
			this.tasks = tasks;
			this.distance = distance;
		}
		public List<String> getTasks() {
			return tasks;
		}
		public void setTasks(List<String> tasks) {
			this.tasks = tasks;
		}
		public double getDistance() {
			return distance;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		
		
	}

}
