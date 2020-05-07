@file:Suppress("UNUSED_PARAMETER", "unused")
package lesson9.task1

import java.lang.IllegalArgumentException

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int
    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E
    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)
    operator fun set(cell: Cell, value: E)

    /** ------------------------------------------------------------- **/
    // fun getCell(value: E): Cell?
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {
    if ((height <= 0) || (width <= 0)) throw IllegalArgumentException()
    return MatrixImpl(height, width, e)
}

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {

    private val empty = null

    private val container = MutableList(height) { MutableList(width) { e } }

    override fun get(row: Int, column: Int): E =
            when {
                (!inside(row, column)) -> throw IndexOutOfBoundsException()
                (container[row][column] == empty) -> throw NoSuchElementException()
                else -> container[row][column]
            }

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        if (!inside(row, column)) throw IndexOutOfBoundsException()
        container[row][column] = value
    }

    override fun set(cell: Cell, value: E) {
        set(cell.row, cell.column, value)
    }

    override fun equals(other: Any?): Boolean {
        if ((other !is Matrix<*>) || (height != other.height) || (width != other.width)) return false
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (container[i][j] != other[i, j]) return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + container.hashCode()
        return result
    }

    override fun toString(): String = container.toString()

    private fun inside(row: Int, column: Int): Boolean = ((row in 0..height) && (column in 0..width))
    private fun inside(cell: Cell): Boolean = inside(cell.row, cell.column)

    /*
    override fun getCell(value: E): Cell? {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (container[i][j] == value) return Cell(i, j)
            }
        }
        return null
    }
    */
}