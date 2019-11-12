/*
   Register.java
   CS 321 - Section 001: Team 7
   John DeCarlo, Huiying Jin, John Radecki, Joshua Yuen
   ----------------------------------------------------
   Description: This is the register page of our software
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JOptionPane;

import com.floss.manager.*;

public class Register extends JDialog {

    private JTextField tfUsername; // Username text field
    private JTextField tfName; // Name text field
    private JTextField tfMajor; // Major text field
    private JPasswordField pfPassword; // Password passworld field
    private JLabel lbUsername; // Username label
    private JLabel lbName; // Name label
    private JLabel lbMajor; // Major label
    private JLabel lbPassword; // Password label
    private JButton btnRegister; // Register button
    private JButton btnCancel; // Cancel button

    // private AWSManager database = new AWSManager(); // Database instance

    /**
     * Checks to see if the user-entered username already exists in our database
     * 
     * @param username User-entered username to check
     * @return True if the user-entered username does not already exist, false
     *         otherwise
     */
    public static boolean authenticate(String username) {
        /*
         * if (!database.exists(username)) { return true; }
         */

        if (username.equals("test")) // for testing purposes
            return true; // for testing purposes

        return false;
    }

    /**
     * Constructor for Register
     * 
     * @param parent The calling window to temporarily hide until done
     */
    public Register(JDialog parent) {
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

        // Name label
        lbName = new JLabel("Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbName, cs);

        // Name text field
        tfName = new JTextField(50);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfName, cs);

        // Major label
        lbMajor = new JLabel("Major: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbMajor, cs);

        // Major text field
        tfMajor = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(tfMajor, cs);

        // Password label
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        // Password password field
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);

        panel.setBorder(new LineBorder(Color.GRAY));
        JPanel buttonpanel = new JPanel();

        // Register button
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Register.authenticate(getUsername())) {
                    // Add user to database

                    // database.addUser(getUsername(), getName(), String(pfPassword.getPassword()),
                    // tfMajor.getText());

                    dispose();
                    setVisible(false);
                    parent.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(Register.this, "Username is already taken", "Register",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                }
            }
        });
        buttonpanel.add(btnRegister);

        // Cancel button
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
                parent.setVisible(true);
            }
        });
        buttonpanel.add(btnCancel);

        // Add panels to main JDialog
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonpanel, BorderLayout.PAGE_END);

        // Finish setup and display register window
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
     * Returns the trimmed user-entered name
     * 
     * @return Trimmed down user-entered name
     */
    public String getName() {
        return tfName.getText().trim();
    }
}