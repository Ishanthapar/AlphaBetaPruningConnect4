import java.util.ArrayList;
import java.util.HashMap;

/**
 * Authors: Ishan Thapar, 999104208; Samir Rahman, 998988726
 * AI class that implements Minimax algorithm
 */
public class MinimaxAI extends AIModule
{
    private int maxDepth = 1;
    private int currentPlayer;
    private GameStateModule currentGameState;
    private GameStateModule gameState;
    private HashMap<GameStateModule, Integer> gameStateMoves;
    private HashMap<Integer, GameStateModule> gameStateValues;
    //private ArrayList<GameStateModule> children;
    int [][] board;

    //ArrayList<GameStateModule> children;

    public void getNextMove(GameStateModule game)
    {
        board = new int[game.getHeight()][game.getWidth()];
        gameStateMoves = new HashMap<>();
        gameStateValues = new HashMap<>();
        currentGameState = game.copy();
        gameState = game.copy();
        //currentPlayer = game.getActivePlayer();
        //children = new ArrayList<GameStateModule>();

        //chosenMove = 0;
        chosenMove = minimax(currentGameState.getActivePlayer(), 0, currentGameState);
    }

    private int evaluateBoard(final GameStateModule game)
    {
        int player = 0;
        int row;
        int column;
        int result = 0;
        int threat;
        int player1_score;
        int player2_score;
        int score;
        int [][] board = new int[game.getHeight()][game.getWidth()];

        for (int i = 0; i < game.getHeight(); i++)
        {
            for (int j = 0; j < game.getWidth(); j++)
            {
                board[i][j] = game.getAt(j, i);
                //System.out.print(board[i][j] + " ");
            }
            //System.out.println();
        }
        //System.out.println("\n");

        for (int i = game.getHeight()-1; i >= 0; i--)
        {
            for (int j = 0; j < game.getWidth(); j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

        //System.out.println(board[6][0]);

        for (int i = 0; i < game.getHeight(); i++)
        {
            for (int j = 0; j < game.getWidth(); j++)
            {

            }
        }

        return 0;
    }

    private int minimax(final int turn, final int depth, final GameStateModule currentState)
    {
        //currentGameState = currentState.copy();
        //System.out.println("Printing current state of board passed to minimax");
        //printBoard(currentState);

        int bestValue;
        int nodeValue;
        int bestMove;
        //ArrayList<GameStateModule> myChildren = new ArrayList<>();

        if (terminate)
        {
            System.out.println("Terminate Called");
            return 0;
        }

        if (depth == maxDepth || currentState.isGameOver())
        {
            return evaluateBoard(currentState);
        }

        if (turn == 1)
        {
            bestValue = -Integer.MAX_VALUE;
            ArrayList<GameStateModule> myChildren = new ArrayList<GameStateModule>();
            myChildren = saveChildren(currentState);
            for (int i = 0; i < myChildren.size(); i++)
            {
                //System.out.println("Printing Board");
                //printBoard(myChildren.get(i));
                //System.out.println("Done Printing Board");
                nodeValue = minimax(2, depth + 1, myChildren.get(i));
                gameStateValues.put(nodeValue, myChildren.get(i));
                bestValue = Math.max(bestValue, nodeValue);
            }
        } else
        {
            bestValue = Integer.MAX_VALUE;
            ArrayList<GameStateModule> myChildren = new ArrayList<GameStateModule>();
            myChildren = saveChildren(currentState);
            for (int i = 0; i < myChildren.size(); i++)
            {
                //System.out.println("Printing Board");
                //printBoard(myChildren.get(i));
                //System.out.println("Done Printing Board");
                nodeValue = minimax(1, depth + 1, myChildren.get(i));
                gameStateValues.put(nodeValue, myChildren.get(i));
                bestValue = Math.min(bestValue, nodeValue);
            }
        }
        bestMove = gameStateMoves.get(gameStateValues.get(bestValue));
        return bestMove;
    }

    private ArrayList<GameStateModule> saveChildren(final GameStateModule currentState)
    {
        int numColumns = currentState.getWidth();
        ArrayList<GameStateModule> children;
        children = new ArrayList<GameStateModule>();
        GameStateModule state;

        for (int i = 0; i < numColumns; i++)
        {
            state = currentState.copy();
            //printBoard(state);
            if(state.canMakeMove(i))
            {
                state.makeMove(i);
                //currentState.makeMove(i);
                children.add(state);
                //printBoard(children.get(i));
                gameStateMoves.put(state, i);
                //currentState.unMakeMove();
                //currentState.unMakeMove();
            }
        }
        return children;
    }

    private void printBoard(final GameStateModule currentState)
    {

        //int [][] board = new int[currentState.getHeight()][currentState.getWidth()];

        /*for (int i = 0; i < currentState.getHeight(); i++)
        {
            for (int j = 0; j < currentState.getWidth(); j++)
            {
                board[i][j] = currentState.getAt(j, i);
                //System.out.print(board[i][j] + " ");
            }
            //System.out.println();
        }*/

        for (int i = currentState.getHeight()-1; i >= 0; i--)
        {
            for (int j = 0; j < currentState.getWidth(); j++)
            {
                System.out.print(currentState.getAt(j,i) + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

        //System.out.println(board[6][0]);
    }
}
