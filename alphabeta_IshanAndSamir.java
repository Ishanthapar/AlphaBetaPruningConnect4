import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Authors: Ishan Thapar, 999104208; Samir Rahman, 998988726
 */
public class alphabeta_IshanAndSamir extends AIModule{

    /* A short class that allows us to pass back the score as well as where we make the move.in min/max
      game tree
   */
    private class moveAndScore implements Comparable<moveAndScore>
    {
        public int move;
        public double score;

        public moveAndScore(int move, double score)
        {

            this.move = move;
            this.score = score;
        }

        public int compareTo(moveAndScore p2)
        {
            if(this.score == p2.score)
                return 0;
            if(this.score < p2.score)
                return -1;
            else
                return 1;
        }
    }
    private static int gameHeight = 6;
    private static int gameWidth = 7;
    private static int maxDepth = 8;
    // If any potential move can win the game, give it this value.
    private static int winningScore = 10000;
    //    private static int winningScore = Integer.MAX_VALUE / 2;
    private static boolean useMaxFunction = true;
    private static boolean useMinFunction = false;


    // parentScore is a persistent value for score so don't have to recompute every time we make a move.
    private int parentScore = 0;
    private int lastMoveX = -1;
    private int lastMoveY = -1;
    private int ourPlayer;
    private int opponentPlayer;

    public void getNextMove(final GameStateModule state)
    {
        ArrayList<Integer> moves = generateMoves(state);
//        chosenMove = pickBestMove(state, moves);

        ourPlayer = state.getActivePlayer();
        opponentPlayer = ourPlayer == 1 ? 2 : 1;
//            parentScore = 0;
        // Our first move, pick col 3 by default, but if it has been picked, pick col 4.
        if (state.getCoins() < 2) {
            chosenMove = state.getHeightAt(3) == 0 ? 3 : 4;
            return;
        }

        // object that stores best move and its score
        moveAndScore nextMoveScore = minMax(state, 1, useMaxFunction, Integer.MIN_VALUE, Integer.MAX_VALUE);
//            parentScore += nextMoveScore.score;

        lastMoveX = nextMoveScore.move;
        lastMoveY = state.getHeightAt(nextMoveScore.move);
        chosenMove = nextMoveScore.move;
    }

    private ArrayList<Integer> generateMoves(final GameStateModule state)
    {
        int i;
        ArrayList<Integer> moves = new ArrayList();
        for(i = 0; i < state.getWidth(); ++i)
        {
            if(state.canMakeMove(i))
                moves.add(i); //can play column i
        }

        return moves;
    }

    // If minOrMax == useMaxFunction (true) take max. else, take min
    private moveAndScore minMax(final GameStateModule state, int depth, boolean isMaxFunction, double alpha, double beta)
    {
        if(depth == maxDepth || state.isGameOver()) {
            int x = lastMoveX;
            int z = getBoardScore(state);
//            if(z == -1)
//                System.out.print("returning -1 at max depth here");
            return new moveAndScore(this.lastMoveX, getBoardScore(state));
//            return new moveAndScore(this.lastMoveX, getScoreAtMove(state));
        }

        /*
        http://stackoverflow.com/questions/9964496/alpha-beta-move-ordering
        At every depth, sort the moves based on score. By default, the TreeMap sorts least -> greatest
        For min function, you'd like to process smallest (min) moves first and see if you can prune from there.
        For max, you'd like to process largest moves (max) first and see if you can prune.
        */
        ArrayList<Integer> moves = generateMoves(state);
        PriorityQueue<moveAndScore> sortedMoveQueue = new PriorityQueue<>();

        for(int move : moves)
        {
            state.makeMove(move);
            double score = getBoardScore(state);
            sortedMoveQueue.add(new moveAndScore(move, score));
            state.unMakeMove();
        }

        ArrayList<Integer> sortedMoves = new ArrayList<Integer>();
        while(!sortedMoveQueue.isEmpty())
            sortedMoves.add(sortedMoveQueue.poll().move);

        // Reverse so it goes from greatest to least
        if(isMaxFunction)
            Collections.reverse(sortedMoves);
        // Store the move used before this temporarily to go back to.
        int tempLastMoveX = this.lastMoveX;
        int tempLastMoveY = this.lastMoveY;
        int bestMoveX = -1;
        // If max, have best score be small negative value, if min, use big positive value
        double bestScore = isMaxFunction ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        int bestScore =
        if(depth == 2)
            beta += 0;
//        for(int move : moves)
        for(int move : sortedMoves)
        {
            if(terminate){
                System.out.println("too slow terminate");
                lastMoveX = tempLastMoveX;
                lastMoveY = tempLastMoveY;
                state.unMakeMove();
                break;
            }
            lastMoveX = move;
            lastMoveY = state.getHeightAt(move);
            if(!state.canMakeMove(move))
                System.out.print("why is this move here?");
//            if(isMaxFunction == useMinFunction && move == 0 && lastMoveY == 0)
//                System.out.print("why isn't this move detected?");
            state.makeMove(move);

//            if(depth == 1 && state.getCoins() == 6)
//                System.out.print("block threat here");
            // if max right now, use min next time. if min right now, use max next time.
            boolean oppositeMinMax = isMaxFunction ? useMinFunction : useMaxFunction;
            moveAndScore moveScore =  minMax(state, depth + 1, oppositeMinMax, alpha, beta);
//            moveAndScore moveScore =  minMax(state, depth + 1, !isMaxFunction);

//            if(moveScore.move == -1) {
//                System.out.print("error in minmax");
//                System.out.print(terminate);
//            }
            if(isMaxFunction)
            {
//                if (moveScore.score + parentScore > bestScore) {
//                    System.out.println(moveScore.score);
//                    bestScore = moveScore.score + parentScore;
//                    if(bestScore > winningScore || bestScore < -winningScore) {
//                        int z = 5;
//                    }
//                    bestMoveX = lastMoveX;
//                }
                if(moveScore.score > bestScore) {
                    bestScore = moveScore.score;
//                    bestMoveX = moveScore.move;
                    bestMoveX = move;
//                    System.out.println(moveScore.move);
//                    System.out.println(moveScore.score);
                }
//
                if(bestScore >= beta)
                {
                    lastMoveX = tempLastMoveX;
                    lastMoveY = tempLastMoveY;
                    state.unMakeMove();
                    return new moveAndScore(bestMoveX, bestScore);
                }
                alpha = Math.max(alpha, bestScore);

            }
            else // min func
            {
//                if(moveScore.score + parentScore < bestScore)
//                {
//                    bestScore = moveScore.score + parentScore;
//                    bestMoveX = lastMoveX;
//                }
                if(moveScore.score < bestScore) {
                    bestScore = moveScore.score;
//                    bestMoveX = moveScore.move;
                    bestMoveX = move;
                }

                if(bestScore <= alpha) {
                    lastMoveX = tempLastMoveX;
                    lastMoveY = tempLastMoveY;
                    state.unMakeMove();
                    return new moveAndScore(bestMoveX, bestScore);
                }
                beta = Math.min(beta, bestScore);
            }

            lastMoveX = tempLastMoveX;
            lastMoveY = tempLastMoveY;
            state.unMakeMove();
        }
//        if(bestScore == -1 || bestScore == Integer.MIN_VALUE)
//            System.out.print("returning -1 or min value at end of minmax");
        return new moveAndScore(bestMoveX, bestScore);
//        return new moveAndScore(bestMoveX, bestScore * 0.95);
    }
    // Unlike previous function, gets score by looping through entire board

    private int getBoardScore(final GameStateModule state)
    {
        int tempScore = 0;
        int rightScore = 0;
        int upScore = 0;
        int upRightScore = 0;
        int upLeftScore = 0;
        // Look right
        for(int i = 0; i < gameWidth - 3; i++)
        {
            for(int j = 0; j < gameHeight; j++)
            {
                rightScore += scoreInDirection(state, i, j, 1, 0 );
                if(rightScore == winningScore || rightScore == -winningScore)
                    return rightScore;
            }
        }

        // Look up
        for(int i = 0; i < gameWidth; i++)
        {
            for(int j = 0; j < gameHeight - 3; j++)
            {
                upScore += scoreInDirection(state, i, j, 0, 1);
                if(upScore == winningScore || upScore == -winningScore)
                    return upScore;

            }
        }
        // up right
        for(int i = 0; i < gameWidth - 3; i++)
        {
            for(int j = 0; j < gameHeight - 3; j++)
            {
                upRightScore += scoreInDirection(state, i, j, 1, 1);
                if(upRightScore == winningScore || upRightScore == -winningScore)
                    return upRightScore;
            }
        }

        // up left
        for(int i = gameWidth - 3; i < gameWidth; i++)
        {
            for(int j = 0; j < gameHeight - 3; j++)
            {
                upLeftScore += scoreInDirection(state, i, j, 1, 1);
                if(upLeftScore == winningScore || upLeftScore == -winningScore)
                    return upLeftScore;
            }
        }

        return upLeftScore + upRightScore + upScore + rightScore;
    }

    // Gets score of the player in a certain direction - left, right, down, diagonals, also checks for wins
    private int scoreInDirection(final GameStateModule state,
                                 final int startingX,
                                 final int startingY,
                                 final int X,
                                 final int Y
    )
    {
        int ourScore = 0;
        int opponentScore = 0;
        int tileAtStart = state.getAt(startingX, startingY);

        // Iterate through 4 tiles, but stop early if our x or y value goes over the bounds.
        for(int i = 0, x = startingX, y = startingY; i < 4 && x >= 0 && x < gameWidth
                && y >= 0 && y < gameHeight; i++, x += X, y += Y)
        {
            if(startingX == 4)
            {
                // This was just here for debugging, ignore
                int p = 1;
            }
            if(state.getAt(x, y) == ourPlayer)
                ourScore++;
            else if(state.getAt(x,y) == opponentPlayer) {
                // There is an opponent tile in the way, so not worth.
                if(tileAtStart == ourPlayer)
                    return 0;
                opponentScore++;
            }
        }


        // if 4 of same tile, return the winningScore instead.
        if(opponentScore == 4)
            return -winningScore;
        return ourScore == 4 ? winningScore : ourScore;
    }

    /* Adds up # tiles in all directions from a specific move
       Ex: Make potential move at (1,1) - count how many of our tiles you can reach from 1,1
      */
    private int getScoreAtMove(final GameStateModule state)
    {
        int leftScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, -1, 0);
        int rightScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, 1, 0);
        int upLeftScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, -1, 1);
        int upRightScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, 1, 1);
        int downLeftScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, -1, -1);
        int downRightScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, 1, -1);
        int downScore = scoreInDirection(state, this.lastMoveX, this.lastMoveY, 0, -1);

        /* Because the current tile is placed by the same player, the order of the below 2 shouldnt matter -
           if max, we can only win, and if min, we cna only lose.
          */
        if(leftScore == winningScore || rightScore == winningScore || upLeftScore == winningScore ||
                upRightScore == winningScore || downLeftScore == winningScore || downRightScore == winningScore
                || downScore == winningScore)
            return winningScore;
        else if(leftScore == -winningScore || rightScore == -winningScore || upLeftScore == -winningScore ||
                upRightScore == -winningScore || downLeftScore == -winningScore || downRightScore == -winningScore
                || downScore == -winningScore)
            return -winningScore;

        // Note: Kind of bugged because we get default score of 7 because we count the same tile 7 times.
        int totalScore = leftScore + rightScore + upLeftScore + upRightScore + downLeftScore + downRightScore
                + downScore;

        return totalScore;
//        return Math.min(totalScore, winningScore);
    }

}
