/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Default
 */
public class Server {

    public static void main(String[] args) {
        /**
         * usando socket para iniciar o chat
         *
         */
        ServerSocket servidor = null;
        try {
            /**
             * conectando no servidor e usando o tratamento de erros;
             */
            System.out.println("Servidor sendo iniciado... \n");
            servidor = new ServerSocket(9999);
            System.out.println("Servidor iniciado com sucesso!!!");

            /**
             * Esperando novos sockets para se conectar ao servidor;
             */
            while (true) {
                Socket cliente = servidor.accept();
                new Gerenciador_cliente(cliente);
            }

        } catch (IOException ex) { // tratamendo de erro caso a porta de acesso esteja inacessível;
            try { // caso der erro o servidor vai auto fechar;
                if (servidor != null) {
                    servidor.close();
                }
            } catch (IOException ex1) {
            }
            System.err.println("Erro ao iniciar o servidor");
            ex.printStackTrace();
        }
    }

}
