// Agent actioneer in project hf4

/* Initial beliefs and rules */

allocated_tasks([]).

/* Initial goals */



/* Plans */
+environmentReady: true <- !taskAllocation .
+!taskAllocation : true <- .print("Starting task allocation...");
							-+round(1);
							!proposeBid.
							
+!proposeBid: round(R) & actions.available_tasks(Tasks)<- .print(Tasks); .broadcast(achive, bid(Tasks, R)).

+!proposeBid: true <- .print("No tasks left").

+my_bid(_, _, _, _): 
	round(R) & my_bid(Task1, PlusDistance1,SumDistance1, R)[source(robot1)] & my_bid(Task2, PlusDistance2, SumDistance2, R)[source(robot2)] 
		<- actions.determine_winner(Winner, Task, Bid);
		.broadcast(tell, winner(Winner, Task, Bid, R));
		?allocated_tasks(AllocatedTasks);
		.concat(AllocatedTasks, [Task], NewAllocatedTasks);
		-+allocated_tasks(NewAllocatedTasks);
		-+round(R+1);
		!proposeBid.
		
		




