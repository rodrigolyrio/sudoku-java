package br.com.dio.sudoku.model;

import java.util.List;

import static br.com.dio.sudoku.model.GameStatusEnum.COMPLETE;
import static br.com.dio.sudoku.model.GameStatusEnum.INCOMPLETE;
import static br.com.dio.sudoku.model.GameStatusEnum.NON_STARTED;

/**
 * Tabuleiro 9x9. As posições são acessadas como {@code spaces.get(coluna).get(linha)}.
 */
public class Board {

    public static final int SIZE = 9;

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    /**
     * NON_STARTED: nenhum espaço editável foi preenchido.
     * COMPLETE: todos preenchidos (pode ainda conter erros; use {@link #hasErrors()}).
     * INCOMPLETE: algum preenchido e algum vazio.
     */
    public GameStatusEnum getStatus() {
        boolean anyEditableFilled = spaces.stream().flatMap(List::stream)
                .anyMatch(s -> !s.isFixed() && !s.isEmpty());
        if (!anyEditableFilled) {
            return NON_STARTED;
        }
        boolean anyEmpty = spaces.stream().flatMap(List::stream).anyMatch(Space::isEmpty);
        return anyEmpty ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors() {
        return spaces.stream().flatMap(List::stream).anyMatch(Space::isWrong);
    }

    /**
     * Preenche um espaço.
     *
     * @return false se o espaço for fixo (dica inicial), true se o valor foi alterado
     */
    public boolean changeValue(final int col, final int row, final int value) {
        final Space space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    /**
     * Limpa um espaço.
     *
     * @return false se o espaço for fixo, true se foi limpo
     */
    public boolean clearValue(final int col, final int row) {
        final Space space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.clearSpace();
        return true;
    }

    /** Limpa todos os espaços editáveis. */
    public void reset() {
        spaces.forEach(column -> column.forEach(Space::clearSpace));
    }

    /** O jogo termina com sucesso quando está completo e sem erros. */
    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == COMPLETE;
    }
}
