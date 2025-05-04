# Algoritmo do Banqueiro
## 💡 Resumo

Esse projeto consiste numa implementação do Algoritmo do Banqueiro, em que existem n threads de clientes que fazem requisições e devoluções aleatórias de forma contínua ao longo da execução do programa, seguidas de verificações do estado de segurança do sistema caso os recursos sejam fornecidos.

## 🖥️ Pré-requisitos

- **Java JDK** 11 ou superior
- Terminal / Prompt de comando


## 🛠️ Compilação e Execução

1. **Clone o repositório** (ou baixe os arquivos manualmente):
   <pre>
   git clone https://github.com/brunohreis/banker_algorithm.git
   </pre>
2. **Vá para o diretório dos arquivos de código-fonte:
   <pre>
   cd banker_algorithm/BankerAlgorithm/src/
   </pre>
2. **Compile os arquivos java**:
	<pre>
   javac *.java
   </pre>
3. **Execute o programa** passando como parâmetro (1°) a qtd de clientes e as quantidades de instâncias disponíveis de cada tipo de recurso:
	<pre>
   java Main 5 13 14 15
   </pre>
(Nesse caso, existem 5 clientes, e o recurso 0 tem 13 instâncias, o recurso 1 tem 14 instâncias e o recurso 2 tem 15 instâncias)
