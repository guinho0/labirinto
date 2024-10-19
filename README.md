
#Resolvedor de Labirinto
##Descrição
###Este programa em Java resolve labirintos representados por matrizes de 0's (paredes) e 1's (caminhos) utilizando Busca em Profundidade (DFS) com backtracking. O labirinto é lido de um arquivo CSV e o programa encontra e exibe o caminho da entrada até a saída.

#Requisitos
##Java Development Kit (JDK 8+)
#Como Usar
##Compilar
###No terminal, navegue até o diretório onde o arquivo Labirinto.java está salvo.
#Compile o programa:
###-javac Labirinto.java
Executar
##Execute o programa passando o arquivo CSV como argumento:
###-java Labirinto labirinto.csv
#Exemplo de Saída:
X 0 0 0 0 
X X X 0 0 
0 0 X 0 0 
0 0 X X X 
0 0 0 0 X 
Caminho encontrado.
##Se não houver caminho:
Nenhum caminho encontrado.

