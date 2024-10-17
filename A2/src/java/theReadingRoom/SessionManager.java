package theReadingRoom;

// this class i here to help me manage the session of the currently logged-in user
public class SessionManager {
    private static SessionManager instance;
    private String username; // variable to store the name of the logged-in user
    //default constructor
    private SessionManager() {
    }

    //method to access the single intance of the class
    public static SessionManager getInstance() {
        // if the instance doesn't exist, it is created
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance; //return an instance of the class
    }
    //to retrieve the username
    public String getUsername() {
        return username;
    }
    // to set the username where needed
    public void setUsername(String username) {
        this.username = username;
    }
}
