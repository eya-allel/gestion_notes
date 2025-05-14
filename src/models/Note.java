package models;

import java.time.LocalDate;

public class Note {
    private String matiere;  // "TP Java" ou "Cours Java"
    private String type;     // "TP Hebdomadaire", "Examen TP", "DS", "Examen"
    private double valeur;   // La note sur 20
    private LocalDate date;
    private String commentaire;
    
    public Note(String matiere, String type, double valeur, LocalDate date, String commentaire) {
        this.matiere = matiere;
        this.type = type;
        this.valeur = valeur;
        this.date = date;
        this.commentaire = commentaire;
    }
    
    // Getters et Setters
    public String getMatiere() {
        return matiere;
    }
    
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getValeur() {
        return valeur;
    }
    
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: %.2f/20 (%s)", matiere, type, valeur, date);
    }
}