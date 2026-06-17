

        if (newItem.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please enter an item name first.");
        if (!validateMenuItem(frame, newItem, "Please enter an item name first.")) {
            return;

        addMenuItem(frame, menuItems, menuSet, newItem);
        saveMenuToCSV(menuItems);
        if (addMenuItem(frame, menuItems, menuSet, newItem)) {
            saveMenuToCSV(menuItems);
        }
    }

        if (itemToDelete.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please enter the menu item to delete.");
        if (!validateMenuItem(frame, itemToDelete, "Please enter the menu item to delete.")) {
            return;

        deleteMenuItem(frame, menuItems, menuSet, itemToDelete);
        saveMenuToCSV(menuItems);
        if (deleteMenuItem(frame, menuItems, menuSet, itemToDelete)) {
            saveMenuToCSV(menuItems);
        }
    }

    public static void addMenuItem(JFrame frame, ArrayList<String> menuItems,
                                   HashSet<String> menuSet, String newItem) {
    public static boolean validateMenuItem(JFrame frame, String item, String errorMessage) {
        if (item == null || item.trim().equals("")) {
            JOptionPane.showMessageDialog(frame, errorMessage);
            return false;
        }

        return true;
    }

    public static boolean addMenuItem(JFrame frame, ArrayList<String> menuItems,
                                      HashSet<String> menuSet, String newItem) {
        String normalizedItem = normalizeMenuItem(newItem);
            );
            return false;
        } else {
            );
            return true;
        }

    public static void deleteMenuItem(JFrame frame, ArrayList<String> menuItems,
                                      HashSet<String> menuSet, String itemToDelete) {
    public static boolean deleteMenuItem(JFrame frame, ArrayList<String> menuItems,
                                         HashSet<String> menuSet, String itemToDelete) {
        String normalizedItem = normalizeMenuItem(itemToDelete);
            );
            return true;
        } else {
            );
            return false;
        }
