package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.game2048.Game2048Initializer

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val boardValues = initializer.initialPermutation

        board.getAllCells().asSequence().forEachIndexed{ index, cell ->
            when {
                index < boardValues.size -> board[cell] = boardValues[index]
                else -> board[cell] = null
            }
        }
    }

    override fun canMove(): Boolean {
       return board.any { it == null }
    }

    override fun hasWon(): Boolean {
        var wonValue = 1
        val lastCellIndex = board.width.times(board.width) - 1

        board.getAllCells().asSequence().forEachIndexed { index, cell ->
            when {
                index < lastCellIndex && board[cell] != wonValue -> return false
                index == lastCellIndex && board[cell] != null -> return false
            }
            wonValue++
        }

        return true
    }

    override fun processMove(direction: Direction) {
        val nullPosition = board.find { it == null } ?: return

        var cellToMove : Cell?

        //get the neighbour to the empty cell by reversing the direction that we want to move in
        board.apply {
            cellToMove = nullPosition.getNeighbour(direction.reversed())
        }

        if(cellToMove == null) return

        board[nullPosition] = board[cellToMove!!]
        board[cellToMove!!] = null
    }

    override fun get(i: Int, j: Int): Int? =
        board[board.getCell(i,j)]

}

