package models;

public class Admin extends User {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    public Admin() {
        super(ADMIN_USERNAME, ADMIN_PASSWORD, "Administrateur", "Système");
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
    
    public static boolean isAdmin(String username) {
        return ADMIN_USERNAME.equals(username);
    }
}