import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class GUIDemo extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/carbonfootprintdb";
    private static final String USER = "root";
    private static final String PASS = "Prathuish@123";

    public GUIDemo() {
        createGUI();
    }

    private void createGUI() {
        JFrame jfrm = new JFrame("CARBON FOOTPRINT CALCULATOR");
        jfrm.setLayout(new FlowLayout(FlowLayout.CENTER));
        jfrm.setSize(400, 300);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel jlab1 = new JLabel("WELCOME TO CARBON FOOTPRINT CALCULATOR");
        JButton jbtninsert = new JButton("INSERT ");
        JButton jbtndelete = new JButton("DELETE");
        JButton jbtnsearch = new JButton("SEARCH");
        JButton jbtndisplay = new JButton("DISPLAY");
        JButton jbtnupdate = new JButton("UPDATE");

        jbtninsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame jfrmin = new JFrame("INSERT");
                jfrmin.setLayout(new FlowLayout());
                jfrmin.setSize(400, 400);

                Box b1 = Box.createVerticalBox();

                JLabel name = new JLabel("Enter owner name:");
                JTextField jtfname = new JTextField();
                jtfname.setPreferredSize(new Dimension(250, 40));

                JLabel model = new JLabel("Enter type of vehicle:");
                JTextField jtfmodel = new JTextField();
                jtfmodel.setPreferredSize(new Dimension(250, 40));

                JLabel energy = new JLabel("Enter type of energy used:");
                JTextField jtfenergy = new JTextField();
                jtfenergy.setPreferredSize(new Dimension(250, 40));

                JLabel plate = new JLabel("Enter number plate:");
                JTextField jtfplate = new JTextField();
                jtfplate.setPreferredSize(new Dimension(250, 40));

                JLabel mileage = new JLabel("Enter mileage:");
                JTextField jtfmileage = new JTextField();
                jtfmileage.setPreferredSize(new Dimension(250, 40));

                JButton enter = new JButton("SUBMIT");
                b1.add(name);
                b1.add(jtfname);
                b1.add(model);
                b1.add(jtfmodel);
                b1.add(energy);
                b1.add(jtfenergy);
                b1.add(plate);
                b1.add(jtfplate);
                b1.add(mileage);
                b1.add(jtfmileage);
                b1.add(enter);

                jfrmin.add(b1);
                jfrmin.setVisible(true);

                enter.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent eb) {
                        if (jtfname.getText().isEmpty() ||
                                jtfmodel.getText().isEmpty() ||
                                jtfenergy.getText().isEmpty() ||
                                jtfplate.getText().isEmpty() ||
                                jtfmileage.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(jfrmin, "Please fill all the fields.", "Input Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Stop the insertion if any field is empty
                        }
                        String ownerName = jtfname.getText();
                        String transportMode = jtfmodel.getText();
                        String fuelType = jtfenergy.getText();
                        String numberPlate = jtfplate.getText();
                        double mileage = Double.parseDouble(jtfmileage.getText());
                        double carbonFootprint = mileage * 0.01;

                        // Insert data into the SQL database
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            String query = "INSERT INTO CarbonFootprint (ownerName, vehicleType, energyType, numberPlate, mileage, carbonFootprint) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, ownerName);
                            pstmt.setString(2, transportMode);
                            pstmt.setString(3, fuelType);
                            pstmt.setString(4, numberPlate);
                            pstmt.setDouble(5, mileage);
                            pstmt.setDouble(6, carbonFootprint);

                            pstmt.executeUpdate();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Record inserted successfully");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Error inserting data", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        jfrmin.dispose();

                        // Clear fields after inserting
                        jtfname.setText("");
                        jtfmodel.setText("");
                        jtfenergy.setText("");
                        jtfplate.setText("");
                        jtfmileage.setText("");
                    }
                });
            }
        });

        jbtnsearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame jfsrch = new JFrame("SEARCH");
                jfsrch.setLayout(new FlowLayout());
                jfsrch.setSize(400, 200);

                JLabel jlsrch = new JLabel("Enter owner name:");
                JTextField jtfsrch = new JTextField();
                jtfsrch.setPreferredSize(new Dimension(250, 40));
                JButton search = new JButton("SEARCH");

                Box b2 = Box.createVerticalBox();
                b2.add(jlsrch);
                b2.add(jtfsrch);
                b2.add(search);
                jfsrch.add(b2);
                jfsrch.setVisible(true);

                search.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (jtfsrch.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(jfsrch, "Please fill the name fields.", "Input Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Stop the insertion if any field is empty
                        }
                        String ownerName = jtfsrch.getText(); // Capture the owner's name from input

                        JFrame jdsp = new JFrame();
                        jdsp.setLayout(new FlowLayout());
                        jdsp.setSize(500, 500);
                        JTextArea jtxta = new JTextArea();

                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            String query = "SELECT * FROM CarbonFootprint WHERE ownerName = ?";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, ownerName); // Set the owner name in the query

                            ResultSet rs = pstmt.executeQuery(); // Execute the query and get the result set

                            boolean ownerFound = false;

                            while (rs.next()) {
                                ownerFound = true;
                                // Display the data in the text area
                                jtxta.append("Owner Name: " + rs.getString("ownerName") + "\n");
                                jtxta.append("Vehicle Type: " + rs.getString("vehicleType") + "\n");
                                jtxta.append("Energy Type: " + rs.getString("energyType") + "\n");
                                jtxta.append("Number Plate: " + rs.getString("numberPlate") + "\n");
                                jtxta.append("Mileage: " + rs.getDouble("mileage") + " miles\n");
                                jtxta.append("Carbon Footprint: " + rs.getDouble("carbonFootprint") + " tons\n");
                                jtxta.append("\n");
                            }

                            if (!ownerFound) {
                                jtxta.append("No record found for owner: " + ownerName + "\n");
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Error retrieving data", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                        jdsp.add(jtxta);
                        jdsp.setVisible(true);
                        jtfsrch.setText(""); // Clear the input field after search
                    }
                });
            }
        });

        jbtndisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame jdsp = new JFrame();
                jdsp.setLayout(new FlowLayout());
                jdsp.setSize(500, 500);

                JTextArea jtxta = new JTextArea();
                jtxta.setEditable(false);
                // scroll bar option
                JScrollPane scrollPane = new JScrollPane(jtxta);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "SELECT * FROM CarbonFootprint";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        jtxta.append("Owner Name: " + rs.getString("ownerName") + "\n");
                        jtxta.append("Vehicle Type: " + rs.getString("vehicleType") + "\n");
                        jtxta.append("Energy Type: " + rs.getString("energyType") + "\n");
                        jtxta.append("Number Plate: " + rs.getString("numberPlate") + "\n");
                        jtxta.append("Mileage: " + rs.getDouble("mileage") + " miles\n");
                        jtxta.append("Carbon Footprint: " + rs.getDouble("carbonFootprint") + " tons\n");
                        jtxta.append("\n");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GUIDemo.this, "Error retrieving data", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                jdsp.add(jtxta);
                jdsp.add(scrollPane, BorderLayout.CENTER);
                jdsp.setVisible(true);
            }
        });
        jbtndelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame jfdlt = new JFrame("DELETE");
                jfdlt.setLayout(new FlowLayout());
                jfdlt.setSize(400, 200);

                JLabel jldlt = new JLabel("Enter owner name:");
                JTextField jtfdlt = new JTextField();
                jtfdlt.setPreferredSize(new Dimension(250, 40));
                JButton delete = new JButton("DELETE");

                Box b3 = Box.createVerticalBox();
                b3.add(jldlt);
                b3.add(jtfdlt);
                b3.add(delete);
                jfdlt.add(b3);
                jfdlt.setVisible(true);

                delete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (jtfdlt.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(jfdlt, "Please fill the name fields.", "Input Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Stop the insertion if any field is empty
                        }
                        String ownerName = jtfdlt.getText();

                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            String query = "DELETE FROM CarbonFootprint WHERE ownerName = ?";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, ownerName);

                            int rowsAffected = pstmt.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(GUIDemo.this, "Record deleted successfully", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(GUIDemo.this, "No record found for owner: " + ownerName,
                                        "Not Found", JOptionPane.WARNING_MESSAGE);
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Error deleting record", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        jfdlt.dispose();
                        jtfdlt.setText(""); // Clear the input field after delete
                    }
                });
            }
        });

        jbtnupdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame jfrmin = new JFrame("UPDATE");
                jfrmin.setLayout(new FlowLayout());
                jfrmin.setSize(400, 400);

                Box b1 = Box.createVerticalBox();

                JLabel name = new JLabel("Enter owner name:");
                JTextField jtfname = new JTextField();
                jtfname.setPreferredSize(new Dimension(250, 40));

                JLabel model = new JLabel("Enter type of vehicle:");
                JTextField jtfmodel = new JTextField();
                jtfmodel.setPreferredSize(new Dimension(250, 40));

                JLabel energy = new JLabel("Enter type of energy used:");
                JTextField jtfenergy = new JTextField();
                jtfenergy.setPreferredSize(new Dimension(250, 40));

                JLabel plate = new JLabel("Enter number plate:");
                JTextField jtfplate = new JTextField();
                jtfplate.setPreferredSize(new Dimension(250, 40));

                JLabel mileage = new JLabel("Enter mileage:");
                JTextField jtfmileage = new JTextField();
                jtfmileage.setPreferredSize(new Dimension(250, 40));

                JButton enter = new JButton("SUBMIT");
                b1.add(name);
                b1.add(jtfname);
                b1.add(model);
                b1.add(jtfmodel);
                b1.add(energy);
                b1.add(jtfenergy);
                b1.add(plate);
                b1.add(jtfplate);
                b1.add(mileage);
                b1.add(jtfmileage);
                b1.add(enter);

                jfrmin.add(b1);
                jfrmin.setVisible(true);

                enter.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent eb) {
                        if (jtfname.getText().isEmpty() ||
                                jtfmodel.getText().isEmpty() ||
                                jtfenergy.getText().isEmpty() ||
                                jtfplate.getText().isEmpty() ||
                                jtfmileage.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(jfrmin, "Please fill all the fields.", "Input Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Stop the insertion if any field is empty
                        }
                        String ownerName = jtfname.getText();
                        String transportMode = jtfmodel.getText();
                        String fuelType = jtfenergy.getText();
                        String numberPlate = jtfplate.getText();
                        double mileage = Double.parseDouble(jtfmileage.getText());
                        double carbonFootprint = mileage * 0.01;

                        // Insert data into the SQL database
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            String deleteQuery = "DELETE FROM carbonFootprint WHERE ownerName = ?";
                            String insertQuery = "INSERT INTO carbonFootprint (ownerName, vehicleType, energyType, numberPlate, mileage, carbonFootprint) VALUES (?, ?, ?, ?, ?, ?)";

                            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                            deleteStmt.setString(1, ownerName);
                            deleteStmt.executeUpdate();

                            // Step 2: Prepare INSERT statement
                            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                            insertStmt.setString(1, ownerName);
                            insertStmt.setString(2, transportMode);
                            insertStmt.setString(3, fuelType);
                            insertStmt.setString(4, numberPlate);
                            insertStmt.setDouble(5, mileage);
                            insertStmt.setDouble(6, carbonFootprint);

                            // Execute the INSERT
                            insertStmt.executeUpdate();

                            // Close statements
                            deleteStmt.close();
                            insertStmt.close();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Record updated  successfully");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(GUIDemo.this, "Error upadting data", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        jfrmin.dispose();

                        // Clear fields after inserting
                        jtfname.setText("");
                        jtfmodel.setText("");
                        jtfenergy.setText("");
                        jtfplate.setText("");
                        jtfmileage.setText("");
                    }
                });
            }
        });

        // layout design of main fram
        JPanel panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        gbc.gridy = 0;// row
        gbc.gridx = 0;// column
        panel1.add(jlab1, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        panel1.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel1.add(jbtninsert, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel1.add(jbtndelete, gbc);

        gbc.gridy = 5;
        gbc.weighty = 0;
        panel1.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        gbc.gridy = 6;
        panel1.add(jbtnsearch, gbc);

        gbc.gridy = 7;
        gbc.weighty = 0;
        panel1.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        gbc.gridy = 8;
        panel1.add(jbtndisplay, gbc);

        gbc.gridy = 9;
        gbc.weighty = 0;
        panel1.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        gbc.gridy = 10;
        panel1.add(jbtnupdate, gbc);

        jfrm.add(panel1);
        jfrm.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIDemo();
            }
        });
    }
}
