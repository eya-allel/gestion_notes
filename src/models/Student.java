package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private String matricule;
    private Map<String, List<Note>> notes;
    
    // Base de données simulée pour les étudiants
    private static Map<String, Student> students = new HashMap<>();
    
    public Student(String username, String password, String nom, String prenom, String matricule) {
        super(username, password, nom, prenom);
        this.matricule = matricule;
        this.notes = new HashMap<>();
    }
    
    // Méthode pour ajouter un étudiant à la "base de données"
    public static void addStudent(Student student) {
        students.put(student.getUsername(), student);
    }
    
    // Méthode pour récupérer un étudiant par son username
    public static Student getStudentByUsername(String username) {
        return students.get(username);
    }
    
    // Méthode pour vérifier si un étudiant existe
    public static boolean studentExists(String username) {
        return students.containsKey(username);
    }
    
    // Méthode pour récupérer tous les étudiants
    public static List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    // Méthode pour supprimer un étudiant
    public static void removeStudent(String username) {
        students.remove(username);
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        Student student = students.get(username);
        if (student != null) {
            return student.getPassword().equals(password);
        }
        return false;
    }
    
    // Getters et Setters
    public String getMatricule() {
        return matricule;
    }
    
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
    // Méthodes pour gérer les notes
    public void addNote(Note note) {
        String matiere = note.getMatiere();
        if (!notes.containsKey(matiere)) {
            notes.put(matiere, new ArrayList<>());
        }
        notes.get(matiere).add(note);
    }
    
    public List<Note> getNotesByMatiere(String matiere) {
        return notes.getOrDefault(matiere, new ArrayList<>());
    }
    
    public Map<String, List<Note>> getAllNotes() {
        return notes;
    }
    
    // Méthodes de calcul des notes
    public double calculateTPJavaNoteFinale() {
        List<Note> tpNotes = getNotesByMatiere("TP Java");
        if (tpNotes.isEmpty()) return 0;
        
        double totalTPHebdo = 0;
        int countTPHebdo = 0;
        double noteExamenTP = 0;
        
        for (Note note : tpNotes) {
            if (note.getType().equals("TP Hebdomadaire")) {
                totalTPHebdo += note.getValeur();
                countTPHebdo++;
            } else if (note.getType().equals("Examen TP")) {
                noteExamenTP = note.getValeur();
            }
        }
        
        double moyenneTPHebdo = countTPHebdo > 0 ? totalTPHebdo / countTPHebdo : 0;
        return (moyenneTPHebdo * 0.5) + (noteExamenTP * 0.5);
    }
    
    public double calculateCoursJavaNoteFinale() {
        List<Note> coursNotes = getNotesByMatiere("Cours Java");
        if (coursNotes.isEmpty()) return 0;
        
        double noteDS = 0;
        double noteExamen = 0;
        
        for (Note note : coursNotes) {
            if (note.getType().equals("DS")) {
                noteDS = note.getValeur();
            } else if (note.getType().equals("Examen")) {
                noteExamen = note.getValeur();
            }
        }
        
        return (noteDS * 0.4) + (noteExamen * 0.6);
    }
    
    public double calculateNoteFinale() {
        double noteTP = calculateTPJavaNoteFinale();
        double noteCours = calculateCoursJavaNoteFinale();
        
        return (noteTP * 0.5) + (noteCours * 0.5);
    }
    
    @Override
    public String toString() {
        return String.format("%s %s (%s)", prenom, nom, matricule);
    }
}