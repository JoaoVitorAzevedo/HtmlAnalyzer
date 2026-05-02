import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        // O programa recebe a URL como argumento
        if (args.length == 0) {
            // Se não passar argumento, encerramos silenciosamente para não poluir o output
            return;
        }

        String urlToAnalyze = args[0];
        String result = solve(urlToAnalyze);

        // Apenas o output esperado é impresso no console
        System.out.println(result);
    }

    private static String solve(String urlString) {
        // Estrutura para controle de profundidade
        int currentDepth = 0;
        int maxDepth = -1; // Começamos com -1 para garantir que o primeiro texto (depth 0 ou mais) seja
                           // pego
        String deepestText = "";

        // Pilha para validação de HTML malformado
        Stack<String> tagStack = new Stack<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // timeout para não travar
            connection.setReadTimeout(5000);

            // Verifica se a conexão foi bem sucedida (HTTP 200)
            int status = connection.getResponseCode();
            if (status != 200) {
                return "URL connection error";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            // O código é processado linha a linha
            while ((line = reader.readLine()) != null) {
                // Espaços iniciais de indentação devem ser ignorados
                String trimmedLine = line.trim();

                // Linhas em branco ignoradas
                if (trimmedLine.isEmpty()) {
                    continue;
                }

                // Lógica de identificação do tipo de linha
                if (isClosingTag(trimmedLine)) {
                    // Tag de Fechamento (ex: </div>)
                    currentDepth--;

                    // Validação de HTML Malformado
                    if (tagStack.isEmpty()) {
                        return "malformed HTML"; // Fechou algo que não abriu
                    }

                    String expectedTag = tagStack.pop();
                    String actualTag = extractTagName(trimmedLine);

                    if (!expectedTag.equals(actualTag)) {
                        return "malformed HTML"; // Fechou a tag errada
                    }

                } else if (isOpeningTag(trimmedLine)) {
                    // Tag de Abertura (ex: <div>)
                    currentDepth++;

                    // Empilha para validar depois
                    tagStack.push(extractTagName(trimmedLine));

                } else {
                    /*
                     * Trecho de Texto
                     * Em caso de empate na profundidade, a primeira ocorrência é mantida.
                     */
                    if (currentDepth > maxDepth) {
                        maxDepth = currentDepth;
                        deepestText = trimmedLine;
                    }
                }
            }
            reader.close();

            // Validação final: Se sobrou tag na pilha, o HTML não fechou tudo -> Malformado
            if (!tagStack.isEmpty()) {
                return "malformed HTML";
            }

            // Caso especial: HTML vazio ou sem texto
            if (deepestText.isEmpty()) {
                // Se nenhum texto for encontrado, retorna vazio.
                return deepestText;
            }

            return deepestText;

        } catch (MalformedURLException e) {
            // Erro na formação da URL
            return "URL connection error";
        } catch (IOException e) {
            // Falha de conexão ou leitura
            return "URL connection error";
        }
    }

    // código auxiliar para manter a solução mais limpa

    private static boolean isOpeningTag(String line) {
        // Começa com < mas não com </
        return line.startsWith("<") && !line.startsWith("</");
    }

    private static boolean isClosingTag(String line) {
        return line.startsWith("</");
    }

    // Extrai "div" de "<div>" ou "</div>"
    private static String extractTagName(String tagLine) {
        // Remove <, </ e >
        return tagLine.replaceAll("[<>/]", "");
    }
}