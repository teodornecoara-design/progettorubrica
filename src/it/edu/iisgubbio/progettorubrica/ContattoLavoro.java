package it.edu.iisgubbio.progettorubrica;

public class ContattoLavoro extends Contatto {
    private String azienda;
    public ContattoLavoro(String nome, String cognome, String numeroTelefono, String azienda) {
        super(nome, cognome, numeroTelefono);
        this.azienda = azienda;
    }
    @Override
    public String getExtra() { 
        return azienda; 
    }
    @Override
    public String getTipo() {
        return "lavoro";
    }
    @Override
    public String toString() {
        return "[LAVORO] " + nome.toUpperCase() + " " + cognome + " - Tel: " + numeroTelefono + " - Azienda: " + azienda;
    }
}