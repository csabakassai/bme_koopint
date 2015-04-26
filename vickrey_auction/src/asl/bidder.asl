// Agent bidder in project vickrey_auction

/* Initial beliefs and rules */
utility(0).

/* Initial goals */


/* Plans */

+!kqml_received(auctioneer,achive,bid(Round),_): 
					true <- 
						actions.calculate_bid(Bid); 
						.send(auctioneer, tell, my_bid(Bid, Round)).
						
+winner(Winner, Prize, Round): .my_name(Winner) <- ?utility(U); -+utility(U + 0.5 - Prize); !share_utility_with_environment .


+!share_utility_with_environment: true <- ?utility(U); share(U).
