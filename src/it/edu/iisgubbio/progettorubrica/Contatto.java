package it.edu.iisgubbio.progettorubrica;

public class Contatto {
    protected String nome;
    protected String cognome;
    protected String numeroTelefono;
    public Contatto(String nome, String cognome, String numeroTelefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.numeroTelefono = numeroTelefono;
    }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getNumeroTelefono() { return numeroTelefono; } 
    public String getExtra() { 
        return ""; 
    }   
    public String getTipo() {
        return "";
    }
}