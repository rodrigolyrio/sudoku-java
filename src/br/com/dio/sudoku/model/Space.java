package br.com.dio.sudoku.model;

/**
 * Representa uma célula (espaço) do tabuleiro.
 *
 * <ul>
 *   <li>{@code expected}: valor correto da solução;</li>
 *   <li>{@code actual}: valor atualmente preenchido (null = vazio);</li>
 *   <li>{@code fixed}: se true, é uma dica inicial e não pode ser alterada.</li>
 * </ul>
 */
public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(final int expected, final boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            this.actual = expected;
        }
    }

    public Integer getActual() {
        return actual;
    }

    /** Altera o valor atual. Ignorado (sem erro) se o espaço for fixo. */
    public void setActual(final Integer actual) {
        if (fixed) {
            return;
        }
        this.actual = actual;
    }

    public void clearSpace() {
        setActual(null);
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public boolean isEmpty() {
        return actual == null;
    }

    /** Retorna true se há valor preenchido e ele difere da solução. */
    public boolean isWrong() {
        return actual != null && actual != expected;
    }
}
