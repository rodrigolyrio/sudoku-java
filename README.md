# Sudoku em Java (terminal)

Jogo de Sudoku 9x9 para o terminal, desenvolvido como desafio de projeto da
[DIO](https://www.dio.me) (*Criando um jogo de Sudoku em Java*), consolidando
programação orientada a objetos, coleções, classes/métodos e entrada/saída no terminal.

## Funcionalidades

| Opção | O que faz |
|---|---|
| 1 | Inicia um novo jogo a partir do tabuleiro recebido por argumentos |
| 2 | Coloca um número em uma posição (coluna e linha de 1 a 9) |
| 3 | Remove um número de uma posição |
| 4 | Mostra o tabuleiro atual |
| 5 | Mostra o status (Não iniciado / Incompleto / Completo) e se há erros |
| 6 | Limpa o jogo (com confirmação) |
| 7 | Finaliza o jogo: só conclui se estiver completo e sem erros |
| 8 | Sai |

Regras implementadas:
- Posições com **valor fixo** (dicas iniciais) não podem ser alteradas nem removidas.
- Entradas inválidas (texto, fora de 1–9) são recusadas e a pergunta se repete.
- O jogo só é finalizado quando todos os espaços estão preenchidos **e** corretos.

## Estrutura

```
src/br/com/dio/sudoku/
├── Main.java                  # menu, leitura dos argumentos e interação
├── model/
│   ├── Space.java             # célula: valor esperado, valor atual, se é fixa
│   ├── Board.java             # tabuleiro 9x9 e regras do jogo
│   └── GameStatusEnum.java    # NON_STARTED, INCOMPLETE, COMPLETE
└── ui/
    └── BoardPrinter.java      # desenho do tabuleiro no terminal
```

## Como executar

Requisito: JDK 17 ou superior.

```bash
./run.sh
```

O script compila e executa o jogo já com o tabuleiro do desafio. Para rodar manualmente:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out br.com.dio.sudoku.Main 0,0;4,false 1,0;7,false ...   # 81 argumentos
```

No Windows (PowerShell), compile com `javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`
e coloque cada argumento entre aspas, por causa do `;`.

### Formato dos argumentos

Cada um dos 81 argumentos tem o formato `coluna,linha;valorEsperado,fixo`:
- `coluna` e `linha`: de 0 a 8;
- `valorEsperado`: a solução naquela posição (1 a 9);
- `fixo`: `true` se for dica inicial, `false` se o jogador deve preenchê-la.

Exemplo: `2,0;9,true` → coluna 2, linha 0, solução 9, já preenchida.
