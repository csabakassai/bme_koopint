// Agent sample_agent in project hf4

/* Initial beliefs and rules */

my_tasks([]).

/* Initial goals */

/* Plans */





+!kqml_received(auctioneer,achive,bid(AvailableTasks, Round),_): 
					true <- 
						actions.create_bid(AvailableTasks, Task, PlusDistance, SumDistance, Tasks); 
						.send(auctioneer, tell, my_bid(Task, PlusDistance, SumDistance, Round)); 
						+bid(Round, Tasks);
						.print(Tasks).

+winner(Winner, Task, Bid, Round): .my_name(Winner) <- ?bid(Round, Tasks); -+my_tasks(Tasks); share_tasks_with_environment(Tasks) .

+winner(Winner, Task, Bid, Round): true <- .print("Winner is not me"). 

