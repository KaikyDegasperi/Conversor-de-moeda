import modelos.Requisicao;

import java.io.IOException;
import java.util.Scanner;

import modelos.RespostaApi;


public class Main {
    public static void main(String[] args) {
        Requisicao requisicao = new Requisicao();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Digite o valor a ser convertido: ");
            double valor = scanner.nextDouble();
            scanner.nextLine(); // Limpa o buffer

            System.out.print("Digite a moeda original (ex: USD): ");
            String moedaOrigem = scanner.nextLine();

            System.out.print("Digite a moeda desejada (ex: BRL): ");
            String moedaDestino = scanner.nextLine();

            double valorConvertido = requisicao.converterMoeda(valor, moedaOrigem, moedaDestino);
            System.out.println("Valor convertido: " + valorConvertido + " " + moedaDestino);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Erro ao fazer a requisição: " + e.getMessage());
        }
    }
}
