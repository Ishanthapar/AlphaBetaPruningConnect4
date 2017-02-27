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
    private HashMap<GameStateModule, Integer> gameStateMoves;
    private HashMap<Integer, GameStateModule> gameStateValues;

    //ArrayList<GameStateModule> children;

    public void getNextMove(GameStateModule game)
    {
        gameStateMoves = new HashMap<>();
        gameStateValues = new HashMap<>();
        currentGameState = game.copy();
        //currentPlayer = game.getActivePlayer();
        //children = new ArrayList<GameStateModule>();

        //chosenMove = 0;
        chosenMove = minimax(currentGameState.getActivePlayer(), 0, currentGameState);
    }

    private int evaluateBoard(GameStateModule game)
    {
        int player = 0;
        int row;
        int column;
        int result = 0;
        int [][] board = new int[7][6];

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                board[i][j] = game.getAt(i, j);
                //System.out.print(board[i][j] + " ");
            }
            //System.out.println();
        }
        //System.out.println("\n");

        for (int i = 6; i >= 0; i--)
        {
            for (int j = 5; j >= 0; j--)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

        System.out.println(board[6][0]);



        return 0;
    }

    private int minimax(int turn, int depth, GameStateModule currentState)
    {
        int bestValue;
        int nodeValue;
        int bestMove;

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
                nodeValue = minimax(1, depth + 1, myChildren.get(i));
                gameStateValues.put(nodeValue, myChildren.get(i));
                bestValue = Math.min(bestValue, nodeValue);
            }
        }
        bestMove = gameStateMoves.get(gameStateValues.get(bestValue));
        return bestMove;
    }

    private ArrayList<GameStateModule> saveChildren(GameStateModule currentState)
    {
        int numRows = currentState.getHeight();
        int numColumns = currentState.getWidth();
        ArrayList<GameStateModule> children;
        children = new ArrayList<GameStateModule>();

        for (int i = 0; i < numColumns; i++)
        {
            if(currentState.canMakeMove(i))
            {
                currentState.makeMove(i);
                children.add(currentState);
                gameStateMoves.put(currentState, i);
                currentState.unMakeMove();
            }
        }
        return children;
    }
}
