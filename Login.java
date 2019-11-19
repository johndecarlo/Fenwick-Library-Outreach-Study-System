/*
   Login.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This is the login page of our software
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

import com.floss.manager.*;

@SuppressWarnings("serial")
public class Login extends JDialog {

    private JTextField tfUsername; // Username text field
    private JPasswordField pfPassword; // Password password field
    private JLabel lbUsername; // Username label
    private JLabel lbPassword; // Password label
    private JLabel lbRegister; // Register label
    private JButton btnRegister; // Register button
    private JButton btnLogin; // Login button
    private JButton btnCancel; // Cancel button
    
    private static RemoteDBManager database; // Database

    /**
     * Check to see if the username and corresponding password belong to an account
     * 
     * @param username Username to check
     * @param password Corresponding password to check
     * @return True if the username and password match an account in our database,
     *         false if not
     */
    public static boolean authenticate(String username, String password) {
        database = FLOSSDriver.getManager( ); 
        String dbPassword = database.getPassword(username); 
        if (database.exists(username) && password.equals(dbPassword)) {
        	return true;
        }

        return false;
    }

    /**
     * Constructor for Login
     * 
     * @param parent The calling window to temporarily hide until done
     */
    public Login(Frame parent) {
        super(parent, "Login", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        // Username label
        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        // Username text field
        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        // Password label
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        // Password password field
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);

        // Register label
        lbRegister = new JLabel("No account?");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbRegister, cs);

        // Register button
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Register register = new Register(Login.this);
                register.setVisible(true);
                dispose();
            }
        });
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(btnRegister, cs);

        panel.setBorder(new LineBorder(Color.GRAY)); // Gray line to separate login and cancel buttons
        JPanel buttonpanel = new JPanel(); // Panel that holds the login and cancel buttons

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // If valid login
                if (Login.authenticate(getUsername(), getPassword())) {
                	String userName = getUsername( );
                    String [] fullName = FLOSSDriver.getManager( ).getName( userName ).split( " " );
                    
                    // Set up who is logging in for future use
                    FLOSSDriver.user = new Student( fullName[0], 
                    		fullName[1], userName, getPassword( ),
                    		FLOSSDriver.getManager( ).getMajor( userName ) );
                    FLOSSDriver.user.setFriends( (ArrayList<String>) FLOSSDriver.getManager().getFriendList( FLOSSDriver.user.getMasonEmail() ) );
                	
                    parent.setVisible(true);
                    dispose();
                } else { // If invalid login
                    JOptionPane.showMessageDialog(Login.this, "Invalid username or password", "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                }
            }
        });
        buttonpanel.add(btnLogin);

        // Cancel button
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonpanel.add(btnCancel);

        // Adds panels to main JDialog
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonpanel, BorderLayout.PAGE_END);

        // Finish setup and display login window
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    /**
     * Returns the trimmed user-entered username
     * 
     * @return Trimmed down user-entered username
     */
    public String getUsername() {
        return tfUsername.getText().trim();
    }

    /**
     * Returns the user-entered password
     * 
     * @return User-entered password
     */
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
}
