package br.gov.fazenda.sped;

import java.io.IOException;

import javax.xml.ws.Endpoint;

public class StartReceberLoteEventos {

    public static void main(String[] args) throws IOException {
        Endpoint.publish("http://localhost:8081/ws", new ReceberLoteEventosServer());
        System.out.println("Rodando servico ReceberLoteEventos!");
    }

}
