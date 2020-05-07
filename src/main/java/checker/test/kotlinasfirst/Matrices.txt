@file:Suppress("UNUSED_PARAMETER")
package lesson9.task2

import lesson3.task1.factorial
import lesson9.task1.Cell
import lesson9.task1.Matrix
import lesson9.task1.createMatrix
import java.lang.IllegalStateException
import java.lang.Math.pow
import kotlin.math.*

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    if (width != other.width || height != other.height) throw IllegalArgumentException()
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 0)
    var last = 0
    var wasLayers = 0
    while (last < height * width) {
        for (i in wasLayers until width - wasLayers) {
            matrix[wasLayers, i] = last + 1
            last++
            if (last == height * width) return matrix
        }
        for (i in wasLayers + 1 until height - wasLayers - 1) {
            matrix[i, width - 1 - wasLayers] = last + 1
            last++
            if (last == height * width) return matrix
        }
        for (i in width - wasLayers - 1 downTo wasLayers) {
            matrix[height - wasLayers - 1, i] = last + 1
            last++
            if (last == height * width) return matrix
        }
        for (i in height - wasLayers - 2 downTo wasLayers + 1) {
            matrix[i, wasLayers] = last + 1
            last++
            if (last == height * width) return matrix
        }
        wasLayers++
    }
    return matrix
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> = TODO()

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> = TODO()

/**
 * Средняя
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> = TODO()

/**
 * Сложная
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean = TODO()

/**
 * Средняя
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> = TODO()

/**
 * Средняя
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes = TODO()

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> = TODO()

/**
 * Сложная
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> = TODO()

/**
 * Простая
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> = TODO(this.toString())

/**
 * Средняя
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> = TODO(this.toString())

/**
 * Сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun getCell(matrix: Matrix<Int>, value: Int): Cell? {
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            if (matrix[i, j] == value) return Cell(i, j)
        }
    }
    return null
}
// Как реализовать данную функцию как метод в Matrix<E>, если в котоеде выдает Build Error
// с Class is not abstract and does not implement abstract member fun getCell(): Cell?

fun checkPossibility(matrix: Matrix<Int>, move: Int): Boolean {
    val cell = getCell(matrix, 0) ?: throw IllegalStateException()
    val moveCell = getCell(matrix, move) ?: throw IllegalStateException()
    return when {
        (cell.row == moveCell.row) && (abs(cell.column - moveCell.column) == 1) -> true
        (cell.column == moveCell.column) && (abs(cell.row - moveCell.row) == 1) -> true
        else -> false
    }
}

fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    val allowedCells = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    if (!moves.all { allowedCells.contains(it) }) throw IllegalStateException()
    var cellZero = getCell(matrix, 0) ?: throw IllegalStateException()
    for (it in moves) {
        if (!checkPossibility(matrix, it)) throw IllegalStateException()
        val cell = getCell(matrix, it) ?: throw IllegalStateException()
        matrix[cellZero] = it
        matrix[cell] = 0
        cellZero = cell
    }
    return matrix
}

/**
 * Очень сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
fun newMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val buffer = createMatrix(matrix.height, matrix.width, 0)
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            buffer[i, j] = matrix[i, j]
        }
    }
    return buffer
}

fun newMove(matrix: Matrix<Int>, move: Int): Matrix<Int> {
    val cell = getCell(matrix, move) ?: throw IllegalStateException()
    val cellZero = getCell(matrix, 0) ?: throw IllegalStateException()
    val newMatrix = newMatrix(matrix)
    newMatrix[cellZero] = move
    newMatrix[cell] = 0
    return newMatrix
}

fun possibleWays(matrix: Matrix<Int>): List<Int> {
    val c = getCell(matrix, 0) ?: throw IllegalStateException()
    val x = c.column
    val y = c.row
    val buffer= mutableListOf<Int>()
    if (y - 1 in 0..3) buffer.add(matrix[y - 1, x])
    if (y + 1 in 0..3) buffer.add(matrix[y + 1, x])
    if (x - 1 in 0..3) buffer.add(matrix[y, x - 1])
    if (x + 1 in 0..3) buffer.add(matrix[y, x + 1])
    return buffer
}

fun asFar(matrix: Matrix<Int>, rightR: Matrix<Int>, rightL: Matrix<Int>): Double {
    var asCloseR = 0.0
    var asCloseL = 0.0
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            if (matrix[i, j] != 0) {
                var cell = getCell(rightR, matrix[i, j]) ?: throw IllegalArgumentException()
                asCloseR += abs(i - cell.row) + abs(j - cell.column)
                cell = getCell(rightL, matrix[i, j]) ?: throw IllegalArgumentException()
                asCloseL += abs(i - cell.row) + abs(j - cell.column)
            }
        }
    }
    var removeEdges = 0
    if ((matrix[0, 0] != rightR[0, 0]) && (matrix[1, 0] == rightR[1, 0]) && (matrix[0, 1] == rightR[0, 1]))
        removeEdges += 2
    if ((matrix[0, 3] != rightR[0, 3]) && (matrix[0, 2] == rightR[0, 2]) && (matrix[1, 3] == rightR[1, 3]))
        removeEdges += 2
    if ((matrix[3, 0] != rightR[3, 0]) && ((matrix[3, 1] == rightR[3, 1]) || (matrix[3, 1] == rightL[3, 1]))
            && (matrix[2, 0] == rightR[2, 0])) removeEdges += 2
    var edgeFirst = false
    var edgeSecond = false
    var edgeThrid = false
    for (row in 0 until 4) {
        var currentRow = 0
        var prev = 4 * row + 1
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if ((matrix[row, i] != 0) && (matrix[row, i] == rightR[row, j])) {
                    if (prev > matrix[row, i]) {
                        currentRow++
                        if ((row == 3) && ((i == 1) || (i == 2))) edgeThrid = true
                        if ((row == 0) && ((i == 3) || (i == 2))) edgeSecond = true
                        if ((row == 0) && ((i == 1) || (i == 2))) edgeFirst = true
                    }
                    prev = matrix[row, i]
                }
            }
        }
        val buffer = factorial(currentRow + 1) / factorial(currentRow - 1)
        if (buffer != -1.0) {
            asCloseR += buffer
            asCloseL += buffer
        }
    }
    for (column in 0 until 4) {
        var currentColumn = 0
        var prev = column + 1
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if ((matrix[i, column] != 0) && (matrix[i, column] == rightR[j, column])) {
                    if (prev > matrix[i, column]) {
                        currentColumn++
                        if (((i == 3) || (i == 2)) && (column == 0)) edgeThrid = true
                        if (((i == 1) || (i == 2)) && (column == 3)) edgeSecond = true
                        if (((i == 1) || (i == 2)) && (column == 0)) edgeFirst = true
                    }
                    prev = matrix[i, column]
                }
            }
        }
        val buffer = factorial(currentColumn + 1) / factorial(currentColumn - 1)
        if (buffer != -1.0) {
            asCloseR += buffer
            asCloseL += buffer
        }
    }
    if (edgeFirst) removeEdges -= 2
    if (edgeSecond) removeEdges -= 2
    if (edgeThrid) removeEdges -= 2
   /* for (i in 0 until 4) {
        if ((matrix[3, 3] == 0) && ((matrix[3, i] == 12) || (matrix[i, 3] == 15))) {
            removeEdges += 2
            break
        }
    } */
    // if (removeEdges < 0) removeEdges = 0
    return min(asCloseR, asCloseL) + removeEdges
}

fun getIndexOfClosestCell(list: List<Double>): Int {
    var min = Double.MAX_VALUE
    var minIndex = 0
    for (i in 0 until list.size) {
        val buffer = list[i]
        if (buffer <= min) {
            min = buffer
            minIndex = i
        }
    }
    return minIndex
}

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {

    /** Создание правильной расстановки **/
    val zeroStartMatrix = createMatrix(4, 4, 0)
    val zeroStartWay = mutableListOf(1, 2, 6, 5, 4, 8, 9, 10, 5, 4, 8, 1, 2, 8, 4, 6, 8, 2, 1, 9, 10, 5, 6, 4,
            5, 10, 9, 5, 4, 8, 3, 7, 8, 6, 11, 8, 6, 4, 2, 3, 4, 6, 7, 4, 3, 2, 6, 7, 8, 11, 10, 13, 12,
            9, 13, 10, 14, 12, 10, 13, 9, 10, 13, 14, 12, 15, 11, 12, 14, 13, 10, 9, 13, 10, 9, 13, 5, 6,
            10, 9, 15, 14, 7, 10, 6, 5, 9, 15, 14, 7, 10, 6, 15, 10, 7, 14, 10, 15, 6, 7, 14, 10, 15, 14,
            10, 11, 12, 8, 7, 10, 11, 15, 14, 11, 10, 7, 8, 10, 11, 6, 7, 11, 10, 12, 15, 10, 11, 7, 6, 14,
            10, 15, 12, 11, 14, 10, 15, 14, 11, 12)
    val rightR = createMatrix(4, 4, 0)
    val rightL = createMatrix(4, 4, 0)
    var was = 0
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            zeroStartMatrix[i, j] = was
            was++
        }
    }
    was = 1
    for (i in 0 until 3) {
        for (j in 0 until 4) {
            rightR[i, j] = was
            rightL[i, j] = was
            was++
        }
    }
    rightR[3, 0] = 13
    rightR[3, 3] = 0
    rightL[3, 0] = 13
    rightL[3, 3] = 0
    rightR[3, 1] = 14
    rightL[3, 1] = 15
    rightR[3, 2] = 15
    rightL[3, 2] = 14
    if ((matrix == rightL) || (matrix == rightR)) return emptyList()
    if (matrix == zeroStartMatrix) return zeroStartWay

    /** Поиск решения алгоритмом A* по графу возможный расстановок
     *  contourCell - ячейки контура графа
     *  contourWay - путь до каждой ячейки контура
     *  contourWeight - вес каждой ячейки контура
     *  closedCell - уже пройденные возможные расстановки
     *
     *  Поиск наименьшего значения weight
     *  Поиск соседних ходов, проверка на пройденность
     *  Расширение контура
     */

    val contourCell = mutableListOf(matrix)
    val contourWay = mutableListOf(listOf<Int>())
    val contourWeight = mutableListOf(0.0)
    val closedCell = mutableSetOf(matrix.hashCode())

    while (true) {
        val minWeightIndex = getIndexOfClosestCell(contourWeight)
        val minWeightCell = contourCell[minWeightIndex]
        val minWeightWay = contourWay[minWeightIndex]
        closedCell.add(minWeightCell.hashCode())
        contourCell.removeAt(minWeightIndex)
        contourWay.removeAt(minWeightIndex)
        contourWeight.removeAt(minWeightIndex)
        if (minWeightWay.size > 1000) continue
        val moves =  possibleWays(minWeightCell)
        for (it in moves) {
            val bufferMatrix = newMove(minWeightCell, it)
            if (!closedCell.contains(bufferMatrix.hashCode()) && !contourCell.contains(bufferMatrix)) {
                val bufferWay = minWeightWay.toMutableList()
                bufferWay.add(it)
                if ((bufferMatrix == rightL) || (bufferMatrix == rightR)) {
                    return bufferWay
                }
                contourCell.add(bufferMatrix)
                contourWay.add(bufferWay)
                //contourWeight.add(bufferWay.size + asFar(bufferMatrix, rightR, rightL))
                contourWeight.add(asFar(bufferMatrix, rightR, rightL))
            }
        }
        if (contourCell.size % 200 == 0) {
            println("${contourWay.last()} | ")
            println(contourCell[getIndexOfClosestCell(contourWeight)])
       }
    }
}