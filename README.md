# Quadtrees
This was an optimization I was trying to implement. I was trying to create a Mario game, but when I was doing collision
testing I was testing the collisions from the character against *every* single object in the game. This was very inefficient.
I did some searching and found out that some Game Engines use a data structure called a Quadtree. I tried to create one 
and this is what I came up with. I never did end up implementing it in the actual game.

![Quadtree](/quadtree.png)

# How to run
In order to run this clone this repository. Then make sure you have Java installed and JAVA_HOME set in your environment 
variables. Then run:

```
java TestQuadTree
```

You can move the character around with the arrow keys.
