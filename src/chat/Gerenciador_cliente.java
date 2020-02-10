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
import java.util.HashMap;
import java.util.Map;

public class Gerenciador_cliente extends Thread { //extends "Thread", para fazer varias ações de uma so vez, 
    //para rodar diferentes metodos ao mesmo tempo;

    private final Socket cliente;
    private String nomeCliente;
    private BufferedReader leitor;
    private PrintWriter escritor;
    private static final Map<String, Gerenciador_cliente> clientes = new HashMap<String, Gerenciador_cliente>(); //lista de clientes conectados usando key value;

    public Gerenciador_cliente(Socket cliente) {
        this.cliente = cliente;
        start();//chamando metodo start(); trazido do Thread;
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            //leitor de String para receber os dados do cliente "Input para receber dados";
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            // escritor de dados para escrever para o cliente "OutPut para mandar dados"
            escritor = new PrintWriter(cliente.getOutputStream(), true);

            efetuarLogin();
            String msg;
            while (true) { // mandando msg para outros clientes conctados ao servidor;
                msg = leitor.readLine();
                if (msg.equalsIgnoreCase(Comandos.sair)) { //metodo para dar comando de fechar o cliente;
                    this.cliente.close();

                } else if (msg.startsWith(Comandos.msg)) { //metodo para mandar msg para outros clientes conectados no servidor;
                    String nomeDestinatario = msg.substring(Comandos.msg.length(), msg.length()); // pegando o nome apartir da 3 casa;
                    System.out.println("Enviando para o: " + nomeDestinatario);
                    Gerenciador_cliente destinatario = clientes.get(nomeDestinatario);
                    if (destinatario == null) { //caso o nome estaja incorreto;
                        escritor.println("Nome incorreto!!!");
                    } else {//Para enviar a msg entre os clientes;
//                        escritor.println("Escreva a mensagem para " + destinatario.getNomeCliente());
                        destinatario.getEscritor().println(this.nomeCliente + " disse: " + leitor.readLine());
                    }
                    // para listar os clientes logados no server
                } else if (msg.equals(Comandos.listaUsuarios)) {
                    atualizarListaUsuarios(this);

                } else {
                    escritor.println(this.nomeCliente + ", you: " + msg);
                }
            }

        } catch (IOException ex) {
            System.out.println("Cliente fechado!!!");
            // ex.printStackTrace();
        }

    }

    public PrintWriter getEscritor() {
        return escritor;
    }

//    public BufferedReader getLeitor() {
//        return leitor;
//    }
    public String getNomeCliente() {
        return nomeCliente;
    }

    private void atualizarListaUsuarios(Gerenciador_cliente gerenciadorDeClientes) {
        StringBuffer str = new StringBuffer();
        for (String c : clientes.keySet()) {
            str.append(c);
            str.append(",\n");
        }
        str.delete(str.length() - 1, str.length());
        gerenciadorDeClientes.getEscritor().println(Comandos.listaUsuarios);
        gerenciadorDeClientes.getEscritor().println(str.toString());
    }

    private void efetuarLogin() throws IOException {
        escritor.println(Comandos.login);
        String msg = leitor.readLine();
        this.nomeCliente = msg.toLowerCase().replaceAll(",", "");
        escritor.println(Comandos.login_aceito);
        escritor.println("Bem-Vindo: " + this.nomeCliente);
        clientes.put(this.nomeCliente, this); //para usar o metodo key, value; localizando os clientes com o seu nome como uma key e o value sendo ele mesmo;
        for (String cliente : clientes.keySet()) {
            atualizarListaUsuarios(clientes.get(cliente));
        }

    }

}
