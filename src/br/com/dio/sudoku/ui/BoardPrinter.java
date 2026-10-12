package br.com.dio.sudoku.ui;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.Space;

/** Responsável apenas por desenhar o tabuleiro no terminal. */
public final class BoardPrinter {

    private static final String SEPARATOR = "+-------+-------+-------+";

    private BoardPrinter() {
    }

    public static String render(final Board board) {
        final StringBuilder sb = new StringBuilder();
        sb.append("    1 2 3   4 5 6   7 8 9  (colunas)\n");
        for (int row = 0; row < Board.SIZE; row++) {
            if (row % 3 == 0) {
                sb.append("  ").append(SEPARATOR).append('\n');
            }
            sb.append(row + 1).append(' ');
            for (int col = 0; col < Board.SIZE; col++) {
                if (col % 3 == 0) {
                    sb.append("| ");
                }
                sb.append(cell(board.getSpaces().get(col).get(row))).append(' ');
            }
            sb.append("|\n");
        }
        sb.append("  ").append(SEPARATOR).append('\n');
        sb.append("(linhas)");
        return sb.toString();
    }

    private static String cell(final Space space) {
        return space.isEmpty() ? " " : String.valueOf(space.getActual());
    }
}
