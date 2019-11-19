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
    private JComboBox cbMajor; // Major combobox field
    private JPasswordField pfPassword; // Password passworld field
    private JLabel lbUsername; // Username label
    private JLabel lbName; // Name label
    private JLabel lbMajor; // Major label
    private JLabel lbPassword; // Password label
    private JButton btnRegister; // Register button
    private JButton btnCancel; // Cancel button
    
    private static RemoteDBManager database;
    
    private static String[] majorsArray = {"ACCT     Accounting",
    		"AFAM     African & African American Studies",
    		"ANTH     Anthropology",
    		"ARAB     Arabic",
    		"ARTH     Art History",
    		"EDAT     Assistive Technology",
    		"ASTR     Astronomy",
    		"ATEP     Athletic Training Education Program",
    		"BENG     Bioengineering",
    		"BINF     Bioinformatics",
    		"BIOL     Biology",
    		"BUS     Business",
    		"BULE     Business Legal Studies",
    		"CHEM     Chemistry",
    		"CHIN     Chinese",
    		"CEIE     Civil and Infrastructure Engineering",
    		"CLAS     Classics",
    		"CLIM     Climate Dynamics",
    		"COS     College of Science",
    		"CVPA     College of Visual & Performing Arts",
    		"COMM     Communication",
    		"CDS     Computational and Data Sciences",
    		"GAME     Computer Game Design",
    		"CS     Computer Science",
    		"CONF     Conflict Analysis & Resolution",
    		"CRIM     Criminology",
    		"CULT     Cultural Studies",
    		"CYSE     CyberSecurity Engineering",
    		"DANC     Dance",
    		"DSGN     Design",
    		"ECED     Early Childhood Education",
    		"ECON     Economics",
    		"EDIT     Educational Instructional Technology",
    		"EDUC     Education",
    		"EDPO     Education Policy",
    		"EDPS     Education Psychology",
    		"ECE     Electrical and Computer Engineering",
    		"ELED     Elementary Education",
    		"ENGR     Engineering",
    		"ENGH     English",
    		"ENVPP     Environmental Science and Policy",
    		"FAVS     Film and Video Studies",
    		"FNAN     Finance",
    		"FRLN     Foreign Language",
    		"FRSC     Forensic Science",
    		"FREN     French",
    		"GGS     Geography and Geoinformation Science",
    		"GEOL     Geology",
    		"GERM     German",
    		"GLOA     Global Affairs",
    		"GCH     Global and Community Health",
    		"GOVT     Government",
    		"HEAL     Health",
    		"HAP     Health Administration and Policy",
    		"HHS     Health and Human Services",
    		"HEBR     Hebrew",
    		"HIST     History",
    		"HNRS     Honors Program",
    		"HNRT     Honors Program (Science/Math)",
    		"HDFS     Human Development & Family Science",
    		"IT     Information Technology",
    		"INTS     Integrative Studies",
    		"ITAL     Italian",
    		"JAPA     Japanese",
    		"KINE     Kinesiology",
    		"KORE     Korean",
    		"LATN     Latin",
    		"LING     Linguistics",
    		"MGMT     Management",
    		"MIS     Management Information Systems",
    		"MKTG     Marketing",
    		"MATH     Mathematics",
    		"ME     Mechanical Engineering",
    		"MLAB     Medical Labratory Science",
    		"MLSC     Military Science",
    		"MBUS     Minor in Business",
    		"NEUR     Neuroscience",
    		"NURS     Nursing",
    		"NUTR     Nutrition and Food Studies",
    		"OM     Operations Management",
    		"OR     Operations Research",
    		"PRLS     Parks, Recreation, and Leisure Studies",
    		"PERS     Persian",
    		"PHIL     Philosophy",
    		"PHED     Physical Education",
    		"PHYS     Physics",
    		"PORT     Portuguese",
    		"PROV     Provost",
    		"PSYC     Psychology",
    		"EDRD     Reading",
    		"RHBS     Rehabilitation Science",
    		"RELI     Religious Studies",
    		"RUSS     Russian",
    		"SOCW     Social Work",
    		"SOCI     Sociology",
    		"SWE     Software Engineering",
    		"SPAN     Spanish",
    		"EDSE     Special Education",
    		"SPMT     Sports Management",
    		"SRST     Sports and Recreation Studies",
    		"STAT     Statistics",
    		"SYST     Systems Engineering",
    		"THR     Theater",
    		"TOUR     Tourism and Events Management",
    		"TURK     Turkish",
    		"UNIV     University Studies",
    		"WMST     Women & Gender Studies"
    		};

    /**
     * Checks to see if the user-entered username already exists in our database
     * 
     * @param username User-entered username to check
     * @return True if the user-entered username does not already exist, false
     *         otherwise
     */
    public static boolean authenticate(String username) {
        database = FLOSSDriver.getManager( );
        if (!database.exists(username)) {
        	return true;
        }

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
        tfName = new JTextField(30);
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
        cbMajor = new JComboBox(majorsArray);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(cbMajor, cs);
        cbMajor.addActionListener(
                new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                      String major = (String)cbMajor.getSelectedItem();
                   }
                });

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

        panel.setBorder(new LineBorder(Color.GRAY)); // Gray line to separate register and cancel buttons
        JPanel buttonpanel = new JPanel(); // Panel that holds the register and cancel buttons

        // Register button
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Register.authenticate(getUsername())) {
                    // Add user to database
                    database.addUser(getUsername(), getName(), new String(pfPassword.getPassword()),
                    (String)cbMajor.getSelectedItem());

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