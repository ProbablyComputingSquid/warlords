# warlords! 


![created by humans not ai](Created-By-Humans-Not-By-AI-Badge-black.png)
[![Athena Award Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Faward.athena.hackclub.com%2Fapi%2Fbadge)](https://award.athena.hackclub.com?utm_source=readme)


wondering how to run it? scroll down for instructions VVV


This is a local multiplayer terminal implementation of the hit card game, warlords. It's colored too, and is serving as the base for a hopefully online version of this game which will come soon(tm). Right now it's just pass-and-play. 

Warlords is a game I learned from my relatives in the land down under, Australia. 
It's somewhat like war, but multiplayer and with some skill?
The goal of the game is to get rid of all the cards in your hand, just like war. Suits do not matter, and joker trumps all.
However, the TWO is the highest card, which means the 3 is the lowest. All other cards are ordered as standard to Poker.
The player with the 3 of Clubs starts, and can play a single to a quadruple of a card.
All other players need to play higher than that card and match the amount. 
Players keep playing clockwise until nobody can play, everyone has passed, or a combination of the two. 

## Additional rules for multi-round play (more fun this way):

The winner, the "warlord" (where the game's name comes from) gets to enact tribute on the other players. The player who finished last, the "scumbag" needs to pay tribute in the form of their highest-ranking card to the warlord. The warlord, in return, gives the scumbag any card of their choice from their hand, typically the lowest-ranking one. The scumbag gets to start, regardless of who has the 3 of clubs. 


---
As far as I know, none of my friends have actually heard of this so-called "hit game".
This is probably because I live in the most free country in the world, measured by bald eagles per hamburger/foot².
this is why I am making a digitized adaption in order to share with others the fun of this classic Australian pastime.


## running the project

this projects files have bundled into a .jar file for your convenience. 
sadly i was not able to make it run in browser so you will have to download and run the file yourself.
it should be linked in the latest release on the github releases to your right-hand side. make sure to install the latest java sdk 

download the latest release from here! https://github.com/ProbablyComputingSquid/warlords/releases/tag/v1.0.0
and run `java -jar warlords.jar`

---
### how and why did I make this project?

there was a programming club challenge to make an implementation of a card game in java, so I took that and made a very bare-bones implementation of the australian card game warlords in java. for athena, i decided to fully implement it, make it a somewhat smooth user experience and to add coloring and decent-looking tui for the users. no ai was used in this project, not even code autocomplete cause i aint want no big ai training on my code as bad as it is lmao