package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Note;
import models.Student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StudentController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label matriculeLabel;
    
    @FXML
    private Label tpJavaLabel;
    
    @FXML
    private Label coursJavaLabel;
    
    @FXML
    private Label noteFinaleLabel;
    
    @FXML
    private TableView<Note> notesTableView;
    
    @FXML
    private TableColumn<Note, String> matiereColumn;
    
    @FXML
    private TableColumn<Note, String> typeColumn;
    
    @FXML
    private TableColumn<Note, Double> valeurColumn;
    
    @FXML
    private TableColumn<Note, String> dateColumn;
    
    @FXML
    private Button exportButton;
    
    @FXML
    private Button logoutButton;
    
    private Student currentStudent;
    private ObservableList<Note> notesList = FXCollections.observableArrayList();
    
    public void initData(Student student) {
        this.currentStudent = student;
        
        // Initialiser les labels
        welcomeLabel.setText("Bienvenue, " + student.getPrenom() + " " + student.getNom());
        matriculeLabel.setText("Matricule: " + student.getMatricule());
        
        // Calculer et afficher les notes
        double noteTPJava = student.calculateTPJavaNoteFinale();
        double noteCoursJava = student.calculateCoursJavaNoteFinale();
        double noteFinale = student.calculateNoteFinale();
        
        tpJavaLabel.setText(String.format("Note TP Java: %.2f/20", noteTPJava));
        coursJavaLabel.setText(String.format("Note Cours Java: %.2f/20", noteCoursJava));
        noteFinaleLabel.setText(String.format("Note Finale: %.2f/20", noteFinale));
        
        // Configurer la TableView
        matiereColumn.setCellValueFactory(new PropertyValueFactory<>("matiere"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        valeurColumn.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        // Formater la colonne de valeur pour afficher 2 décimales
        valeurColumn.setCellFactory(column -> new TableCell<Note, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        // Charger les notes dans la TableView
        loadNotes();
    }
    
    private void loadNotes() {
        notesList.clear();
        
        // Ajouter toutes les notes de l'étudiant à la liste
        for (List<Note> notes : currentStudent.getAllNotes().values()) {
            notesList.addAll(notes);
        }
        
        notesTableView.setItems(notesList);
    }
    
    @FXML
    private void handleExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les notes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
        fileChooser.setInitialFileName(currentStudent.getNom() + "_" + currentStudent.getPrenom() + "_notes.txt");
        
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Relevé de notes pour " + currentStudent.getPrenom() + " " + currentStudent.getNom());
                writer.println("Matricule: " + currentStudent.getMatricule());
                writer.println("----------------------------------------");
                
                writer.println("\nNotes TP Java:");
                for (Note note : currentStudent.getNotesByMatiere("TP Java")) {
                    writer.println(note.toString());
                }
                writer.println(String.format("Note finale TP Java: %.2f/20", currentStudent.calculateTPJavaNoteFinale()));
                
                writer.println("\nNotes Cours Java:");
                for (Note note : currentStudent.getNotesByMatiere("Cours Java")) {
                    writer.println(note.toString());
                }
                writer.println(String.format("Note finale Cours Java: %.2f/20", currentStudent.calculateCoursJavaNoteFinale()));
                
                writer.println("\n----------------------------------------");
                writer.println(String.format("NOTE FINALE: %.2f/20", currentStudent.calculateNoteFinale()));
                
                showAlert(AlertType.INFORMATION, "Exportation réussie", "Vos notes ont été exportées avec succès.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erreur", "Impossible d'exporter les notes.");
            }
        }
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Scene loginScene = new Scene(loginView, 600, 400);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Connexion");
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de retourner à la page de connexion.");
        }
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}