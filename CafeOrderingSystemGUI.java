package com.mycompany.cp2cafe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CP2Cafe {

    private static final String MENU_FILE_NAME = "menu.csv";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        ArrayList<String> menuItems = loadMenuFromCSV();
        if (menuItems.isEmpty()) {
            loadDefaultMenuItems(menuItems);
        }

        HashSet<String> menuSet = buildMenuSet(menuItems);

        JFrame frame = new JFrame("CP2 Cafe Menu Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 420);
        frame.setLayout(new BorderLayout(10, 10));

        JTextArea menuDisplayArea = new JTextArea();
        menuDisplayArea.setEditable(false);
        updateMenuDisplay(menuDisplayArea, menuItems);

        JTextField addItemField = new JTextField();
        JTextField deleteItemField = new JTextField();

        JButton addButton = new JButton("Add Menu Item");
        JButton deleteButton = new JButton("Delete Menu Item");
        JButton clearButton = new JButton("Clear Fields");

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.add(new JLabel("New Menu Item:"));
        inputPanel.add(addItemField);
        inputPanel.add(new JLabel("Menu Item to Delete:"));
        inputPanel.add(deleteItemField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(clearButton);

        frame.add(new JScrollPane(menuDisplayArea), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(functionEvent -> {
            String newItem = addItemField.getText().trim();

            if (!validateMenuItem(frame, newItem, "Please enter an item name first.")) {
                return;
            }

            if (addMenuItem(frame, menuItems, menuSet, newItem)) {
                saveMenuToCSV(menuItems);
                updateMenuDisplay(menuDisplayArea, menuItems);
                addItemField.setText("");
            }
        });

        deleteButton.addActionListener(functionEvent -> {
            String itemToDelete = deleteItemField.getText().trim();

            if (!validateMenuItem(frame, itemToDelete, "Please enter the menu item to delete.")) {
                return;
            }

            if (deleteMenuItem(frame, menuItems, menuSet, itemToDelete)) {
                saveMenuToCSV(menuItems);
                updateMenuDisplay(menuDisplayArea, menuItems);
                deleteItemField.setText("");
            }
        });

        clearButton.addActionListener(functionEvent -> {
            addItemField.setText("");
            deleteItemField.setText("");
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static boolean validateMenuItem(JFrame frame, String item, String errorMessage) {
        if (item == null || item.trim().equals("")) {
            JOptionPane.showMessageDialog(frame, errorMessage);
            return false;
        }

        if (item.contains(",")) {
            JOptionPane.showMessageDialog(frame, "Please do not use commas in menu item names.");
            return false;
        }

        return true;
    }

    public static boolean addMenuItem(JFrame frame, ArrayList<String> menuItems,
                                      HashSet<String> menuSet, String newItem) {
        String cleanedItem = newItem.trim();
        String normalizedItem = normalizeMenuItem(cleanedItem);

        if (menuSet.contains(normalizedItem)) {
            JOptionPane.showMessageDialog(frame, cleanedItem + " is already in the menu.");
            return false;
        }

        menuItems.add(cleanedItem);
        menuSet.add(normalizedItem);
        JOptionPane.showMessageDialog(frame, cleanedItem + " was added to the menu.");
        return true;
    }

    public static boolean deleteMenuItem(JFrame frame, ArrayList<String> menuItems,
                                         HashSet<String> menuSet, String itemToDelete) {
        String normalizedItem = normalizeMenuItem(itemToDelete);

        for (int i = 0; i < menuItems.size(); i++) {
            String currentItem = menuItems.get(i);

            if (normalizeMenuItem(currentItem).equals(normalizedItem)) {
                menuItems.remove(i);
                menuSet.remove(normalizedItem);
                JOptionPane.showMessageDialog(frame, currentItem + " was deleted from the menu.");
                return true;
            }
        }

        JOptionPane.showMessageDialog(frame, itemToDelete + " was not found in the menu.");
        return false;
    }

    public static ArrayList<String> loadMenuFromCSV() {
        ArrayList<String> menuItems = new ArrayList<String>();
        File menuFile = new File(MENU_FILE_NAME);

        if (!menuFile.exists()) {
            return menuItems;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(menuFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String cleanedLine = line.trim();

                if (cleanedLine.equals("") || cleanedLine.equalsIgnoreCase("Item Name")) {
                    continue;
                }

                menuItems.add(cleanedLine);
            }
        } catch (Exception exception) {
            System.out.println("Could not load menu file: " + exception.getMessage());
        }

        return menuItems;
    }

    public static void saveMenuToCSV(ArrayList<String> menuItems) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MENU_FILE_NAME))) {
            writer.println("Item Name");

            for (int i = 0; i < menuItems.size(); i++) {
                writer.println(menuItems.get(i));
            }
        } catch (Exception exception) {
            System.out.println("Could not save menu file: " + exception.getMessage());
        }
    }

    public static void updateMenuDisplay(JTextArea menuDisplayArea, ArrayList<String> menuItems) {
        StringBuilder displayText = new StringBuilder();
        displayText.append("Cafe Menu\n");
        displayText.append("==============================\n");

        if (menuItems.isEmpty()) {
            displayText.append("No menu items available.");
        } else {
            for (int i = 0; i < menuItems.size(); i++) {
                displayText.append(i + 1)
                           .append(". ")
                           .append(menuItems.get(i))
                           .append("\n");
            }
        }

        menuDisplayArea.setText(displayText.toString());
    }

    public static HashSet<String> buildMenuSet(ArrayList<String> menuItems) {
        HashSet<String> menuSet = new HashSet<String>();

        for (int i = 0; i < menuItems.size(); i++) {
            menuSet.add(normalizeMenuItem(menuItems.get(i)));
        }

        return menuSet;
    }

    public static String normalizeMenuItem(String item) {
        return item.trim().toLowerCase(Locale.ROOT);
    }

    public static void loadDefaultMenuItems(ArrayList<String> menuItems) {
        menuItems.add("Coffee");
        menuItems.add("Milk Tea");
        menuItems.add("Sandwich");
        menuItems.add("Pasta");
    }
}
