package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Student;

import java.util.List;

public class DashboardController {
    
    @FXML
    private Label totalStudentsLabel;
    
    @FXML
    private Label tpAbove10Label;
    
    @FXML
    private Label coursAbove10Label;
    
    @FXML
    private Label avgTPLabel;
    
    @FXML
    private Label avgCoursLabel;
    
    @FXML
    private Button refreshButton;
    
    public void initialize() {
        refreshStats();
    }
    
    @FXML
    private void refreshStats() {
        List<Student> students = Student.getAllStudents();
        
        // Nombre total d'étudiants
        int totalStudents = students.size();
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        
        // Nombre d'étudiants avec moyenne TP > 10
        int tpAbove10 = 0;
        // Nombre d'étudiants avec moyenne Cours > 10
        int coursAbove10 = 0;
        
        // Moyennes générales
        double totalTP = 0;
        double totalCours = 0;
        
        for (Student student : students) {
            double tpNote = student.calculateTPJavaNoteFinale();
            double coursNote = student.calculateCoursJavaNoteFinale();
            
            if (tpNote > 10) {
                tpAbove10++;
            }
            
            if (coursNote > 10) {
                coursAbove10++;
            }
            
            totalTP += tpNote;
            totalCours += coursNote;
        }
        
        tpAbove10Label.setText(String.valueOf(tpAbove10));
        coursAbove10Label.setText(String.valueOf(coursAbove10));
        
        // Calculer les moyennes générales
        double avgTP = totalStudents > 0 ? totalTP / totalStudents : 0;
        double avgCours = totalStudents > 0 ? totalCours / totalStudents : 0;
        
        avgTPLabel.setText(String.format("%.2f", avgTP));
        avgCoursLabel.setText(String.format("%.2f", avgCours));
    }
}