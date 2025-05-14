package models;

public abstract class User {
    protected String username;
    protected String password;
    protected String nom;
    protected String prenom;
    
    public User(String username, String password, String nom, String prenom) {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }
    
    // Getters et Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public abstract boolean authenticate(String username, String password);
}