// Agent auctioneer in project vickrey_auction

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+environmentReady: true <- !startauction .
+!startauction : true <- .print("Starting auction...");
							-+round(1);
							!proposeBid.
							
+!proposeBid: round(R) & roundLimit(Limit) & R <= Limit<-  .broadcast(achive, bid(R)).

+!proposeBid: true <- .print("Limit reached").

+my_bid(_, _): 
	round(R) & my_bid(Bid1, R)[source(bidder1)] & my_bid(Bid2, R)[source(bidder2)] & my_bid(Bid3, R)[source(bidder3)] 
		<- actions.determine_winner(Winner, Prize);
		.broadcast(tell, winner(Winner, Prize, R));
		-+round(R+1);
		!proposeBid.
