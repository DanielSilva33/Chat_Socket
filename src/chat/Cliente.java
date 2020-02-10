/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Default
 */
public class Cliente {

    public static void main(String[] args) {

        try {
            final Socket cliente = new Socket("127.0.0.1", 9999);
            //Lendo msg do servidor
            new Thread() {
                @Override
                public void run() {
                    try {
                        //leitor de String para receber os dados do cliente ou do servidor"Input para receber dados";
                        BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                        while (true) {
                            String mensagem = leitor.readLine();
                            if (mensagem == null || mensagem.isEmpty()) {
                                continue;
                            }
                            System.out.println("Server: " + mensagem);
                        }
                    } catch (IOException ex) {
                        System.out.println("Erro na leitura de dados.");
                        ex.printStackTrace();
                    }
                }
            }.start();
            //Escrevendo para o servidor
            PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader leitorT = new BufferedReader(new InputStreamReader(System.in));
            String msgT = "";
            while (true) {
                msgT = leitorT.readLine();
                if (msgT == null || msgT.length() == 0) {
                    continue;
                }
                escritor.println(msgT);
                System.out.println(msgT);
                if (msgT.equalsIgnoreCase("!SAIR")) {
                    System.exit(0);
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Endereço IP incorreto!!!");
            e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Servidor fora do ar!!!");
            ex.printStackTrace();
        }
    }

}
