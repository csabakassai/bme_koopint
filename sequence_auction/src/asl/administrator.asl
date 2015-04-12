// Agent administrator in project sequence_auction

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */


+reset: true <- .print("Killing"); .kill_agent(auctioneer); .kill_agent(robot1); .kill_agent(robot2); -+init.

+init: true <- .create_agent(robot1, "robot.asl");
				.create_agent(robot2, "robot.asl");
				.create_agent(auctioneer, "auctioneer.asl").
