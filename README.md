# Questionnaires

“Questionnaires” is a java party game, with fun questions and creative answers
created by Douglas Myrdek, Cedric Hansen, Brian Emigholz, and Dylan Richmond

![alt text](https://i.imgur.com/Ef0Haut.png)

---------------------------------------------------------------------------------------------------------------

TraceabilityMatrix.xlsx ---- contains the tracability matrix document
UseCases&UserStories&SystemRequirements.docx ---- contains the Use Cases, User Stories, and System Requirements

questions.txt and questions2.txt are the questions used in the game


---------------------------------------------------------------------------------------------------------------

This project will be a desktop game, whose gameplay will consist of answering questions, and voting on other players answers. This game will pit 2-16 players against each other to find who has the funniest responses to the questions given. The gameplay will be as follows. Consider a game with 4 players. In any given round, players 1&2 will get a certain random question, players 2&3 will get another random question, 3&4 another, and finally players 1&4 will get the same question. This means that for any given round, players will answer 2 distinct question, each of which will also be answered by another player. Once each player submitted both of their answers, every player will get to vote on each question that was given in the round. This means that each player will vote for each of the 4 questions that was given in the round. At the end of each voting round, all players will see a current leaderboard., The game will continue for however many rounds was decided by the host upon the games creation.
 Upon opening the application, the user will have 2 options: create game, and join game.

1.	Upon pressing the create game button, the user will be brought to a screen which gives them a few options, including setting the number of rounds, max number of players, etc.., along with a field to enter their own name, and a button to actually launch the game (make the lobby available for others to join). The host will then be brought to a waiting screen which will display which users are currently in the lobby, along with a button to start the game, bringing all users to the gameplay previously described.
2.	Upon pressing the join game button, the user will be brought to a screen which has text fields for their name, and port number, along with a button for them to "join" whatever game was created by the host in bullet point 1. Once the user joins the game, they will be brought to the waiting screen along with the host, but will not be given the button to start the game. These users will also be brought to the gameplay as previously described

At the completion of the game, a leaderboard will be shown, and a winner will be declared.

