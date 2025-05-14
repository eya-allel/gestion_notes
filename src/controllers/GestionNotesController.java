package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.Note;
import models.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionNotesController {
    
    @FXML
    private ComboBox<String> studentComboBox;
    
    @FXML
    private ComboBox<String> matiereComboBox;
    
    @FXML
    private ComboBox<String> typeNoteComboBox;
    
    @FXML
    private TextField noteField;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TextArea commentaireArea;
    
    @FXML
    private Button addNoteButton;
    
    @FXML
    private Button modifyNoteButton;
    
    @FXML
    private Button deleteNoteButton;
    
    @FXML
    private ListView<Note> notesListView;
    
    private ObservableList<Note> notesList = FXCollections.observableArrayList();
    
    public void initialize() {
        // Initialiser les ComboBox
        refreshStudentComboBox();
        
        matiereComboBox.setItems(FXCollections.observableArrayList("TP Java", "Cours Java"));
        
        // Les types de notes dépendent de la matière sélectionnée
        matiereComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.equals("TP Java")) {
                    typeNoteComboBox.setItems(FXCollections.observableArrayList("TP Hebdomadaire", "Examen TP"));
                } else if (newVal.equals("Cours Java")) {
                    typeNoteComboBox.setItems(FXCollections.observableArrayList("DS", "Examen"));
                }
            }
        });
        
        // Initialiser la date à aujourd'hui
        datePicker.setValue(LocalDate.now());
        
        // Configurer la ListView
        notesListView.setItems(notesList);
        
        // Ajouter un listener pour la sélection d'étudiant
        studentComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStudentNotes(newVal);
            }
        });
        
        // Ajouter un listener pour la sélection de note dans la ListView
        notesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                matiereComboBox.setValue(newVal.getMatiere());
                if (newVal.getMatiere().equals("TP Java")) {
                    typeNoteComboBox.setItems(FXCollections.observableArrayList("TP Hebdomadaire", "Examen TP"));
                } else if (newVal.getMatiere().equals("Cours Java")) {
                    typeNoteComboBox.setItems(FXCollections.observableArrayList("DS", "Examen"));
                }
                typeNoteComboBox.setValue(newVal.getType());
                noteField.setText(String.valueOf(newVal.getValeur()));
                datePicker.setValue(newVal.getDate());
                commentaireArea.setText(newVal.getCommentaire());
            }
        });
    }
    
    private void refreshStudentComboBox() {
        List<String> studentNames = new ArrayList<>();
        for (Student student : Student.getAllStudents()) {
            studentNames.add(student.getUsername());
        }
        studentComboBox.setItems(FXCollections.observableArrayList(studentNames));
    }
    
    private void loadStudentNotes(String username) {
        Student student = Student.getStudentByUsername(username);
        if (student != null) {
            notesList.clear();
            
            // Ajouter toutes les notes de l'étudiant à la liste
            for (List<Note> notes : student.getAllNotes().values()) {
                notesList.addAll(notes);
            }
        }
    }
    
    @FXML
    private void handleAddNote(ActionEvent event) {
        String selectedStudent = studentComboBox.getValue();
        String selectedMatiere = matiereComboBox.getValue();
        String selectedType = typeNoteComboBox.getValue();
        String noteText = noteField.getText();
        LocalDate selectedDate = datePicker.getValue();
        String commentaire = commentaireArea.getText();
        
        // Validation des champs
        if (selectedStudent == null || selectedMatiere == null || selectedType == null || 
            noteText.isEmpty() || selectedDate == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        try {
            double noteValue = Double.parseDouble(noteText);
            if (noteValue < 0 || noteValue > 20) {
                showAlert(AlertType.ERROR, "Erreur", "La note doit être comprise entre 0 et 20.");
                return;
            }
            
            // Créer la nouvelle note
            Note newNote = new Note(selectedMatiere, selectedType, noteValue, selectedDate, commentaire);
            
            // Ajouter la note à l'étudiant
            Student student = Student.getStudentByUsername(selectedStudent);
            if (student != null) {
                student.addNote(newNote);
                
                // Rafraîchir la liste des notes
                loadStudentNotes(selectedStudent);
                
                showAlert(AlertType.INFORMATION, "Succès", "La note a été ajoutée avec succès.");
                
                // Réinitialiser les champs
                resetFields();
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer une valeur numérique valide pour la note.");
        }
    }
    
    @FXML
    private void handleModifyNote(ActionEvent event) {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez sélectionner une note à modifier.");
            return;
        }
        
        String selectedMatiere = matiereComboBox.getValue();
        String selectedType = typeNoteComboBox.getValue();
        String noteText = noteField.getText();
        LocalDate selectedDate = datePicker.getValue();
        String commentaire = commentaireArea.getText();
        
        // Validation des champs
        if (selectedMatiere == null || selectedType == null || 
            noteText.isEmpty() || selectedDate == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        try {
            double noteValue = Double.parseDouble(noteText);
            if (noteValue < 0 || noteValue > 20) {
                showAlert(AlertType.ERROR, "Erreur", "La note doit être comprise entre 0 et 20.");
                return;
            }
            
            // Modifier la note
            selectedNote.setMatiere(selectedMatiere);
            selectedNote.setType(selectedType);
            selectedNote.setValeur(noteValue);
            selectedNote.setDate(selectedDate);
            selectedNote.setCommentaire(commentaire);
            
            // Rafraîchir la liste des notes
            notesListView.refresh();
            
            showAlert(AlertType.INFORMATION, "Succès", "La note a été modifiée avec succès.");
            
            // Réinitialiser les champs
            resetFields();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer une valeur numérique valide pour la note.");
        }
    }
    
    @FXML
    private void handleDeleteNote(ActionEvent event) {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez sélectionner une note à supprimer.");
            return;
        }
        
        // Confirmation de suppression
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette note ?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            // Supprimer la note de la liste
            notesList.remove(selectedNote);
            
            // Dans une vraie application, il faudrait aussi supprimer la note de l'étudiant
            // et mettre à jour la base de données
            
            showAlert(AlertType.INFORMATION, "Succès", "La note a été supprimée avec succès.");
            
            // Réinitialiser les champs
            resetFields();
        }
    }
    
    private void resetFields() {
        matiereComboBox.getSelectionModel().clearSelection();
        typeNoteComboBox.getSelectionModel().clearSelection();
        noteField.clear();
        datePicker.setValue(LocalDate.now());
        commentaireArea.clear();
        notesListView.getSelectionModel().clearSelection();
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}