/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import model.Cliente;
import model.Mensagem;

/**
 *
 * @author arthu
 */
public class TrataCliente implements Runnable {

    private Socket soquete_cliente;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private ClienteDAO clienteDAO;
    private MensagemDAO msgDAO;
    private ConversaDAO cnvDAO;
    private int id;

    public TrataCliente(Socket soquete_cliente) throws Exception {
        super();
        this.id = 0;
        this.soquete_cliente = soquete_cliente;
        this.clienteDAO = new ClienteDAO();
        this.msgDAO = new MensagemDAO();
        this.cnvDAO = new ConversaDAO();
        this.saida = new ObjectOutputStream(this.soquete_cliente.getOutputStream());
        this.entrada = new ObjectInputStream(this.soquete_cliente.getInputStream());
    }

    public void enviar_mensagem(Object mensagem) throws Exception {
        System.out.println(mensagem);
        this.saida.writeObject(mensagem);
    }

    public Object receber_mensagem() throws Exception {
        Object obj = this.entrada.readObject();
        System.out.println(obj);
        return obj;
    }

    public void finalizar() throws IOException {
        this.soquete_cliente.close();
    }

    @Override
    public void run() {
        try {
            Mensagem mensagem;
            String comand[];

            OUTER:
            do {
                mensagem = (Mensagem) receber_mensagem();
                comand = mensagem.getOperacao().split(";");
                switch (comand[0]) {
                    case "ENCERRAR" -> {
                        clienteDAO.setOnline(false, id);
                        break OUTER;
                    }

                    case "ENVIAR" -> {
                        int idConversa;
                        if((int)mensagem.getId_destinatario() != 0) {
                            idConversa = cnvDAO.select(this.id, (int)mensagem.getId_destinatario());
                        }else {
                            idConversa = 0;
                        }
                        mensagem.setId_conversa(idConversa);
                        mensagem.setId_remetente(this.id);
                        if(msgDAO.insert(mensagem)) {
                           // enviar_mensagem("OK");
                        }else {
                            //enviar_mensagem("ERRO");
                        }
                    }

                    case "ENTRAR" -> {
                        Cliente cliente = clienteDAO.selectByName(comand[1]);
                        if (cliente == null) {
                            cliente = new Cliente();
                            cliente.setNome(comand[1]);
                            cliente.setOnline(true);
                            if (clienteDAO.insert(cliente)) {
                                this.id = (int) cliente.getId();
                                enviar_mensagem("OK");
                                enviar_mensagem(clienteDAO.selectAll());
                            } else {
                                enviar_mensagem("ERRO");
                            }
                        } else {
                            if (cliente.isOnline()) {
                                enviar_mensagem("ERRO");
                            } else {
                                enviar_mensagem("OK");
                                enviar_mensagem(clienteDAO.selectAll());
                                cliente.setOnline(true);
                                clienteDAO.setOnline(true, cliente.getId());
                                this.id = (int) cliente.getId();
                            }
                        }
                    }

                    case "LISTAR" -> {
                        switch (comand[1]) {
                            case "CLIENTES" ->
                                enviar_mensagem(clienteDAO.selectAll());
                            case "MENSAGENS" -> {
                                if (comand[2].equals("GERAL")) {
                                    enviar_mensagem(msgDAO.select());
                                } else if (comand[2].equals("DIRETA")) {
                                    System.out.println(id + " " + mensagem.getId_destinatario());
                                    int id_conversa = cnvDAO.select(this.id, (int)mensagem.getId_destinatario());
                                    enviar_mensagem(msgDAO.select(id_conversa));
                                }
                            }
                            default ->
                                enviar_mensagem(null);
                        }
                    }

                    default ->
                        throw new AssertionError();
                }

            } while (!mensagem.getTexto().equals("ENCERRAR"));
            System.out.println("\u001b[32m" + soquete_cliente + " - Desconectou!");
            finalizar();
        } catch (SocketException ex) {
            if (this.id != 0) {
                System.err.println("CLIENTE DESCONECTOU A FORÇA");
                clienteDAO.setOnline(false, id);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        clienteDAO.setOnline(false, id);
    }
}
