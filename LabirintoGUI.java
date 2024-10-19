import javax.swing.*; //interface grafica
import java.awt.*; // importa classes graficas
import java.io.BufferedReader; //ler o arquivo linha poir linha
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; //gerenciamento de listas
import java.util.List;

public class LabirintoGUI extends JPanel {
    // variáveis globais
    private static final int TAMANHO_CELULA = 100; // Tamanho de cada célula do labirinto

    private static final int[] dx = { -1, 1, 0, 0, -1, -1, 1, 1 }; // Movimentos nas 8 direções
    private static final int[] dy = { 0, 0, -1, 1, -1, 1, -1, 1 };

    private int[][] labirinto; // matriz 2d que contem as estrutura do labirinto
    private boolean[][] visitado; // controle de celulas que ja foram visitadas
    private int linhas;
    private int colunas;
    private int pivotX, pivotY; // Posição atual do pivô

    // contrutor do labirinto
    public LabirintoGUI(String arquivoCSV) throws IOException {
        carregarLabirinto(arquivoCSV);
        this.visitado = new boolean[linhas][colunas];
        this.pivotX = 0; // posição atual do ponto vermelho
        this.pivotY = 0;
        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(colunas * TAMANHO_CELULA, linhas * TAMANHO_CELULA);
        frame.add(this);
        frame.setVisible(true);
    }

    // Método paraaaa carregar o labirinto a partir de um arquivo CSV
    // cada linha ndo arquivo se transforma em um arraylist
    private void carregarLabirinto(String arquivoCSV) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(arquivoCSV));
        String linha;
        List<int[]> labirintoTemp = new ArrayList<>();

        while ((linha = br.readLine()) != null) {
            String[] valores = linha.split(",");
            int[] linhaLabirinto = new int[valores.length];
            for (int i = 0; i < valores.length; i++) {
                linhaLabirinto[i] = Integer.parseInt(valores[i].trim());
            }
            labirintoTemp.add(linhaLabirinto);
        }

        linhas = labirintoTemp.size();
        colunas = labirintoTemp.get(0).length;
        labirinto = new int[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            labirinto[i] = labirintoTemp.get(i);
        }

        br.close();
    }

    // Método para buscar o caminho usando DFS
    public boolean buscarCaminho() {
        return dfs(0, 0);
    }

    // DFS com movimentação do pivô e repaint
    private boolean dfs(int x, int y) {
        // Verifica se a posição atual está dentro dos limites e é um caminho
        if (!ehPosicaoValida(x, y) || labirinto[x][y] != 1 || visitado[x][y]) {
            return false;
        }

        visitado[x][y] = true;
        moverPivot(x, y); // Mover o pivô para a posição atual
        labirinto[x][y] = 2; // Marca o caminho no labirinto

        // Verifica se chegou à saída
        if (x == linhas - 1 && y == colunas - 1) {
            return true;
        }

        // Tenta mover nas oito direções (incluindo diagonais)
        for (int i = 0; i < 8; i++) {
            int novoX = x + dx[i];
            int novoY = y + dy[i];

            if (dfs(novoX, novoY)) {
                return true;
            }
        }

        // Se não encontrar caminho, retrocede
        moverPivot(x, y); // Mover o pivô de volta ao último ponto
        labirinto[x][y] = 1; // Desmarca o caminho
        return false;
    }

    // Método para verificar se a posição é válida
    private boolean ehPosicaoValida(int x, int y) {
        return x >= 0 && x < linhas && y >= 0 && y < colunas;
    }

    // Mover o pivô (representação visual da movimentação)
    private void moverPivot(int x, int y) {
        this.pivotX = x;
        this.pivotY = y;
        try {
            Thread.sleep(50); // Delay para visualizar a movimentação de bloco em bloco
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint(); // Reatualiza o painel
    }

    // Sobrescreve o método paintComponent para desenhar o labirinto
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                // Desenha as paredes
                if (labirinto[i][j] == 0) {
                    g.setColor(Color.BLACK);
                }
                // Desenha os caminhos
                else if (labirinto[i][j] == 1) {
                    g.setColor(Color.WHITE);
                }
                // Desenha o caminho percorrido
                else if (labirinto[i][j] == 2) {
                    g.setColor(Color.green);
                }
                g.fillRect(j * TAMANHO_CELULA, i * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
                g.setColor(Color.GRAY);
                g.drawRect(j * TAMANHO_CELULA, i * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
            }
        }

        // Desenha o pivô na posição atual
        g.setColor(Color.RED);
        g.fillOval(pivotY * TAMANHO_CELULA + 10, pivotX * TAMANHO_CELULA + 10, 30, 30);
    }

    public static void main(String[] args) {
        try {
            LabirintoGUI labirinto = new LabirintoGUI("labirinto.csv");
            if (labirinto.buscarCaminho()) {
                System.out.println("Caminho encontrado!");
            } else {
                System.out.println("Nenhum caminho encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }
}
