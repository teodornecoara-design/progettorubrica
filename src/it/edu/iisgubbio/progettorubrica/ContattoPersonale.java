package it.edu.iisgubbio.progettorubrica;

public class ContattoPersonale extends Contatto {
    private String email;
    public ContattoPersonale(String nome, String cognome, String numeroTelefono, String email) {
        super(nome, cognome, numeroTelefono);
        this.email = email;
    }
    @Override
    public String getExtra() { 
        return email; 
    }
    @Override
    public String getTipo() {
        return "personale";
    }
    @Override
    public String toString() {
        return "[PERSONALE] " + nome.toUpperCase() + " " + cognome + " - Tel: " + numeroTelefono + " - Email: " + email;
    }
}