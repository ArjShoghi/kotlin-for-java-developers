package board

import board.Direction.*
import java.lang.IllegalArgumentException

fun createSquareBoard(width: Int): SquareBoard
        = SquareBoardClass(width)

fun <T> createGameBoard(width: Int): GameBoard<T>
        = GameBoardClass(width)

open class SquareBoardClass(override val width: Int) : SquareBoard {

    val cells : MutableList<MutableList<Cell>> = mutableListOf()

    init {
        for (i in 0 until width) {
            cells.add(mutableListOf())
            for (j in 0 until width) {
                cells[i].add(j, Cell(i+1,j+1))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return when {
            i == 0 || j == 0 || i > width || j > width -> null
            else -> getCell(i, j)
        }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return when {
            i == 0 || j == 0 || i > width || j > width -> throw IllegalArgumentException()
            else -> cells[i-1][j-1]
        }
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.asSequence().withIndex().flatMap { (i, cellRow) ->
            cellRow.asSequence().map { it }
        }.toList()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.filter { it <= width }.map { getCell(i,it) }.toList()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.filter{ it <= width}.map { getCell(it,j) }.toList()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            UP -> getCellOrNull(i-1 ,j)
            DOWN -> getCellOrNull(i+1 ,j)
            LEFT -> getCellOrNull(i ,j-1)
            RIGHT -> getCellOrNull(i ,j+1)
        }
    }

}

class GameBoardClass<T>(gameBoardWidth : Int) : GameBoard<T>, SquareBoardClass(gameBoardWidth) {

    val coordValueMap = mutableMapOf<Cell, T?>()

    init {
        cells.asSequence().forEach { row ->
            row.asSequence().forEach {
                coordValueMap[it] = null
            }
        }
    }

    override fun get(cell: Cell): T?
            = coordValueMap[cell]


    override fun all(predicate: (T?) -> Boolean): Boolean
            = coordValueMap.values.all(predicate)


    override fun any(predicate: (T?) -> Boolean): Boolean
            = coordValueMap.values.any(predicate)


    override fun find(predicate: (T?) -> Boolean): Cell?
            = coordValueMap.entries.first { (_,v) -> predicate.invoke(v) }.key


    override fun filter(predicate: (T?) -> Boolean): Collection<Cell>
            = coordValueMap.filter { (_, v) -> predicate.invoke(v) }.keys


    override fun set(cell: Cell, value: T?) {
        coordValueMap[cell] = value
    }


}