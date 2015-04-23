package env;

import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import actions.determine_winner.AuctionMode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AuctionEnvironment extends Environment {

	private final Map<String, Point> robots = new HashMap<String, Point>();

	private final Map<String, Point> tasks = new HashMap<String, Point>();

	private final Map<String, List<Point>> ki = Maps.newHashMap();

	public final static int environmentSize = 3*(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4;

	private AuctionView view;

	@Override
	public void init(String[] args) {
		super.init(args);

		int robot1Position = environmentSize / 4;
		robots.put("robot1", new Point(robot1Position, robot1Position));
		int robot2Position = 3 * environmentSize / 4;
		robots.put("robot2", new Point(robot2Position, robot2Position));

		initPoints();

		view = new AuctionView(this);

		addPercept("administrator", Literal.parseLiteral("init"));
		

	}

	
	private void initPoints() {
		
		addPoint();
		addPoint();
		addPoint();
		addPoint();
	}
	public void addPoint() {
		Random r = new Random();

		int taskX = Math.abs(r.nextInt() % (environmentSize - 100)) + 50;
		int taskY = Math.abs(r.nextInt() % (environmentSize - 100)) + 50;
		String taskId = "task" + tasks.size();
		tasks.put(taskId, new Point(taskX, taskY));
		System.out.println(taskX + " " + taskY);
		
	}

	@Override
	public boolean executeAction(String agName, Structure act) {

		System.out.println(act +  "[" + agName + "]");

		String functor = act.getFunctor();
		if (functor.equals("share_tasks_with_environment")) {
			
			
			List<String> taskIds = Lists.newArrayList();
			
			List<Term> task = ((ListTerm)act.getTerm(0)).getAsList();
			
			for (Term term : task) {
				taskIds.add(term.toString());
			}

			List<Point> list = ki.get(agName);
			if (list == null) {
				list = Lists.newArrayList();
				ki.put(agName, list);
			}
			list.clear();
			System.out.println(taskIds);
			for(String taskId: taskIds) {
				Point taskPosition = tasks.get(taskId);
				Preconditions.checkNotNull(taskPosition, task);
				list.add(taskPosition);
			}
			view.repaint();
			return true;
		} else {
			return super.executeAction(agName, act);
		}
	}

	public Map<String, Point> getRobots() {
		return robots;
	}

	public Map<String, Point> getTasks() {
		return tasks;
	}

	public Map<String, List<Point>> getKi() {
		return ki;
	}
	
	public void reset() {
		removePercept("auctioneer", Literal.parseLiteral("environmentReady"));
		removePercept("auctioneer", Literal.parseLiteral("auction_mode(" + AuctionMode.MINI_MAX + ")"));
		removePercept("auctioneer", Literal.parseLiteral("auction_mode(" + AuctionMode.MINI_SUM + ")"));

		for (Entry<String, Point> entry : tasks.entrySet()) {
			removePercept(Literal.parseLiteral("task(" + entry.getKey() + ", " + entry.getValue().x + ", " + entry.getValue().y + ")"));
		}
		addPercept("administrator", Literal.parseLiteral("reset"));
		tasks.clear();
		initPoints();
		ki.clear();
		view.repaint();
		
	}
	
	public void startAuction(AuctionMode auctionMode) {
		
		removePercept("administrator", Literal.parseLiteral("reset"));
		for (Entry<String, Point> entry : robots.entrySet()) {
			addPercept(entry.getKey(), Literal.parseLiteral("position(" + entry.getValue().x + ", " + entry.getValue().y + ")"));
	
		}
		
		for (Entry<String, Point> entry : tasks.entrySet()) {
			addPercept(Literal.parseLiteral("task(" + entry.getKey() + ", " + entry.getValue().x + ", " + entry.getValue().y + ")"));
		}
		addPercept("auctioneer", Literal.parseLiteral("environmentReady"));
		addPercept("auctioneer", Literal.parseLiteral("auction_mode(" + auctionMode + ")"));

	}

}
