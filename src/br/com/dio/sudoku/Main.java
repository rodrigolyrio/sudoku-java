package br.com.dio.sudoku;

import br.com.dio.sudoku.model.Board;
import br.com.dio.sudoku.model.Space;
import br.com.dio.sudoku.ui.BoardPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Jogo de Sudoku no terminal.
 *
 * <p>Uso: {@code java br.com.dio.sudoku.Main "0,0;4,false" "1,0;7,false" ...}
 * onde cada argumento tem o formato {@code coluna,linha;valorEsperado,fixo} (índices de 0 a 8).</p>
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static Board board;

    public static void main(final String[] args) {
        final Map<String, String> positions = parseArgs(args);

        int option = -1;
        while (option != 8) {
            printMenu();
            option = readInt("Escolha uma opção: ", 1, 8);
            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.out.println("Até a próxima!");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ---------------------------------------------------------------- menu

    private static void printMenu() {
        System.out.println();
        System.out.println("========== SUDOKU ==========");
        System.out.println("1 - Iniciar um novo jogo");
        System.out.println("2 - Colocar um novo número");
        System.out.println("3 - Remover um número");
        System.out.println("4 - Visualizar jogo atual");
        System.out.println("5 - Verificar status do jogo");
        System.out.println("6 - Limpar jogo");
        System.out.println("7 - Finalizar jogo");
        System.out.println("8 - Sair");
    }

    // ------------------------------------------------------------- actions

    private static void startGame(final Map<String, String> positions) {
        if (board != null) {
            System.out.println("O jogo já foi iniciado.");
            return;
        }
        final List<List<Space>> spaces = new ArrayList<>();
        for (int col = 0; col < Board.SIZE; col++) {
            spaces.add(new ArrayList<>());
            for (int row = 0; row < Board.SIZE; row++) {
                final String config = positions.get(col + "," + row);
                final String[] parts = config.split(",");
                final int expected = Integer.parseInt(parts[0]);
                final boolean fixed = Boolean.parseBoolean(parts[1]);
                spaces.get(col).add(new Space(expected, fixed));
            }
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar!");
    }

    private static void inputNumber() {
        if (!gameStarted()) {
            return;
        }
        System.out.println("Informe a posição (1 a 9) e o número (1 a 9) que deseja colocar.");
        final int col = readInt("Coluna: ", 1, 9) - 1;
        final int row = readInt("Linha: ", 1, 9) - 1;
        final int value = readInt("Número: ", 1, 9);
        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [coluna %d, linha %d] tem um valor fixo e não pode ser alterada.%n",
                    col + 1, row + 1);
        } else {
            System.out.println("Número colocado.");
        }
    }

    private static void removeNumber() {
        if (!gameStarted()) {
            return;
        }
        System.out.println("Informe a posição (1 a 9) do número que deseja remover.");
        final int col = readInt("Coluna: ", 1, 9) - 1;
        final int row = readInt("Linha: ", 1, 9) - 1;
        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [coluna %d, linha %d] tem um valor fixo e não pode ser removida.%n",
                    col + 1, row + 1);
        } else {
            System.out.println("Número removido.");
        }
    }

    private static void showCurrentGame() {
        if (!gameStarted()) {
            return;
        }
        System.out.println(BoardPrinter.render(board));
    }

    private static void showGameStatus() {
        if (!gameStarted()) {
            return;
        }
        System.out.println("Status do jogo: " + board.getStatus().getLabel());
        System.out.println(board.hasErrors()
                ? "O jogo contém erros."
                : "O jogo não contém erros.");
    }

    private static void clearGame() {
        if (!gameStarted()) {
            return;
        }
        System.out.print("Tem certeza que deseja limpar o jogo e perder todo o progresso? (sim/não): ");
        final String answer = readLine().trim().toLowerCase();
        if (answer.equals("sim") || answer.equals("s")) {
            board.reset();
            System.out.println("Jogo limpo.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private static void finishGame() {
        if (!gameStarted()) {
            return;
        }
        if (board.gameIsFinished()) {
            System.out.println("Parabéns, você concluiu o jogo!");
            System.out.println(BoardPrinter.render(board));
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros. Revise o tabuleiro e corrija-os.");
        } else {
            System.out.println("Você ainda precisa preencher alguns espaços.");
        }
    }

    // ------------------------------------------------------------- helpers

    private static boolean gameStarted() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado (opção 1).");
            return false;
        }
        return true;
    }

    /** Converte os argumentos "col,linha;esperado,fixo" em um mapa "col,linha" -> "esperado,fixo". */
    private static Map<String, String> parseArgs(final String[] args) {
        final Map<String, String> positions = new HashMap<>();
        for (final String arg : args) {
            final String[] kv = arg.split(";");
            if (kv.length == 2 && kv[0].matches("[0-8],[0-8]") && kv[1].matches("[1-9],(true|false)")) {
                positions.put(kv[0], kv[1]);
            }
        }
        if (positions.size() != Board.SIZE * Board.SIZE) {
            System.err.println("Erro: informe 81 argumentos no formato coluna,linha;valor,fixo "
                    + "(recebidos: " + positions.size() + ").");
            System.exit(1);
        }
        return positions;
    }

    private static String readLine() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.exit(0);
            return "";
        }
    }

    /** Lê um inteiro dentro do intervalo, repetindo a pergunta até receber uma entrada válida. */
    private static int readInt(final String prompt, final int min, final int max) {
        while (true) {
            System.out.print(prompt);
            final String line = readLine().trim();
            try {
                final int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // cai na mensagem abaixo
            }
            System.out.printf("Entrada inválida. Digite um número entre %d e %d.%n", min, max);
        }
    }
}
