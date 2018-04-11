# <div style="text-align:center"><img src ="https://github.com/dmyrdek/csc380project/blob/master/src/main/resources/full_logo.png" /></div>

“Questionnaires” is a java party game, with fun questions and creative answers
created by Douglas Myrdek, Cedric Hansen, Brian Emigholz, and Dylan Richmond

![alt text](https://i.imgur.com/Ef0Haut.png)

---------------------------------------------------------------------------------------------------------------

TraceabilityMatrix.xlsx ---- contains the tracability matrix document
UseCases&UserStories&SystemRequirements.docx ---- contains the Use Cases, User Stories, and System Requirements

questions.txt and questions2.txt are the questions used in the game


---------------------------------------------------------------------------------------------------------------

This project will be a desktop game, whose gameplay will be similar to "Cards Against Humanity" and "Quiplash". This game will pit 4-20 players (subject to change) against each other to find who has the funniest responses to the questions. The gameplay will be as follows. Consider a game with 10 players. In any given round, players 1&2 will get a certain random question, players 3&4 will get another random question, 5 & 6 another, and so on. Once each player has typed a response to their question, every other player will get a chance to vote on the other players response. So for example, player 1 (and player 2) will vote on whether they thought player 3 or 4 had a funnier response, then on player 5 or 6's response, and so on. At the end of each voting round, players will get points based on how many votes they had, and the game will continue for however many rounds was decided by the host. The specifics of the gameplay are subject to change, but for now we will aim for this.
	In terms of the interface, we will be creating several GUI's to display the data/game in a reasonable, attractive format. Upon opening the application, the user will have 2 options: create game, and join game.

1.	Upon pressing the create game button, the user will be brought to a screen which gives them a few options, including setting the number of rounds, max number of players, etc.., along with a field to enter their own name, and a button to actually launch the game (make the lobby available for others to join). The host will then be brought to a waiting screen which will display which users are currently in the lobby, along with a button to start the game, bringing all users to the gameplay previously described.

2. 	Upon pressing the join game button, the user will be brought to a screen which has text fields for their name, and the lobby code (which for now, will likely just be the IP address, or a port number, or something along those lines), and a button for them to "join" whatever game was created by the host in bullet point 1. Once the user joins the game, they will be brought to the waiting screen along with the host, but will not be given the button to start the game. These users will also be brought to the gameplay as previously described

At the completion of the game, a leaderboard will be shown, and a winner will be declared. 
