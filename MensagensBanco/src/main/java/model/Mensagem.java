package model;

import java.io.Serializable;

public class Mensagem implements Serializable {

    private String nome;
    private long id;
    private long id_destinatario;
    private long id_remetente;
    private long id_conversa;
    private String texto;
    private String operacao;

    public Mensagem(String opercacao, String texto) {
        super();
        this.texto = texto;
        this.operacao = opercacao;
    }

    public Mensagem(String nome, long id, String texto) {
        this.nome = nome;
        this.id = id;
        this.texto = texto;
    }

    public Mensagem(long id, long id_destinatario, long id_remetente, String texto) {
        this.id = id;
        this.id_destinatario = id_destinatario;
        this.id_remetente = id_remetente;
        this.texto = texto;
    }

    public Mensagem(long id, long id_remetente, String texto) {
        this.id_destinatario = 0;
        this.id = id;
        this.id_remetente = id_remetente;
        this.texto = texto;
    }
    

    public long getId_conversa() {
        return id_conversa;
    }

    public void setId_conversa(long id_conversa) {
        this.id_conversa = id_conversa;
    }
    
    
    public String getOperacao() {
        return this.operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_destinatario() {
        return id_destinatario;
    }

    public void setId_destinatario(long id_destinatario) {
        this.id_destinatario = id_destinatario;
    }

    public long getId_remetente() {
        return id_remetente;
    }

    public void setId_remetente(long id_remetente) {
        this.id_remetente = id_remetente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    

    @Override
    public String toString() {
        return "[" + nome + "] = " + texto + ";";
    }

}
