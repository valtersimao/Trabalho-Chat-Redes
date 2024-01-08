package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente implements Serializable{

    private Socket soquete;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private ArrayList<Mensagem> mensagens;

    private long id;
    private String nome;
    private boolean online;

    public Cliente(String endereco, int porta, String nome) throws Exception {
        super();
        this.soquete = new Socket(endereco, porta);
        this.saida = new ObjectOutputStream(this.soquete.getOutputStream());
        this.entrada = new ObjectInputStream(this.soquete.getInputStream());
        this.nome = nome;
    }

    public Cliente() {
    }

    public Cliente(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void enviar_mensagem(Object mensagem) throws Exception {
        System.out.println("[ENVIADO] : " +mensagem);
        this.saida.writeObject(mensagem);
    }

    public Object receber_mensagem() throws Exception {
        
        Object obj = this.entrada.readObject();
        System.out.println("[RECEBIDO] : " +obj);
        return obj;
    }

    public void finalizar() throws IOException {
        this.soquete.close();
    }

    @Override
    public String toString() {
        String on = this.online ? "ON" : "OFF";
        return this.nome + "[" + on + "]";
    }
}
