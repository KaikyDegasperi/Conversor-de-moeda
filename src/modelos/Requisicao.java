package modelos;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Requisicao {
    public double converterMoeda(double valor, String moedaOrigem, String moedaDestino) throws IOException, InterruptedException {
        // Obtém a taxa de conversão da moeda de origem
        RespostaApi respostaApiOrigem = obterTaxasDeConversao(moedaOrigem);
        Double taxaParaUSD = respostaApiOrigem.getConversion_rates().get("USD");

        if (moedaOrigem.equals(moedaDestino)) {
            return valor; // Se a moeda de origem é igual a moeda de destino, retorna o valor original
        }

        // Verifica se há uma taxa direta entre a moeda de origem e a moeda de destino
        Double taxaOrigemParaDestino = respostaApiOrigem.getConversion_rates().get(moedaDestino);
        if (taxaOrigemParaDestino != null) {
            return valor * taxaOrigemParaDestino;
        }

        // Se não há uma taxa direta, usa USD como intermediário
        RespostaApi respostaApiUSD = obterTaxasDeConversao("USD");
        Double taxaDeUSDParaDestino = respostaApiUSD.getConversion_rates().get(moedaDestino);

        // Converte o valor da moeda de origem para USD e depois para a moeda de destino
        double valorEmUSD = valor / taxaParaUSD;
        return valorEmUSD * taxaDeUSDParaDestino;
    }

    private RespostaApi obterTaxasDeConversao(String moeda) throws IOException, InterruptedException {
        String endereco = "https://v6.exchangerate-api.com/v6/7b2c9fbb8d501a495740e500/latest/";
        String url = endereco + moeda;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na requisição: " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), RespostaApi.class);
    }
}
