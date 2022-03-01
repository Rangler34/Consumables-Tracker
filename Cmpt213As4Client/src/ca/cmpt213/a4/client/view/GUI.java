package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.Consumable;
import ca.cmpt213.a4.client.control.ConsumableFactory;
import ca.cmpt213.a4.client.control.ConsumableManager;
import com.github.lgooddatepicker.components.DatePicker;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * The entire interface of the application which the user can interact with and add, remove, and view items
 * which are in the list of items.
 */
public class GUI extends JDialog implements ActionListener {


    ConsumableFactory consumableFactory = ConsumableFactory.getInstance();
    ConsumableManager consumableManager = new ConsumableManager();
    public int selectionType;


    JDialog addItem, removeItem;
    Border border;
    JLabel label, nameLabel, notesLabel, priceLabel, measurementLabel, expiryLabel, typeLabel, removeLabel;
    JPanel panel, panel2, panel3, addPanel, removePanel;
    JButton allBtn, expiredBtn, nonExpiredBtn, expiringIn7Btn, addBtn, cancelBtn, createBtn, removeBtn, returnBtn, completeRemoveBtn;
    JScrollPane scrollPane, removePane;
    JFrame frame;
    String[] consumableType = {"Food", "Drink"};
    JComboBox comboBox;
    JTextField nameField, notesField, priceField, measurementField, removeField;
    DatePicker date;
    JTextArea scrollTextArea, removeScrollArea;

    private int itemType;
    private String notes;
    private double price;
    private String itemName;
    private double amount;
    LocalDateTime expiry;
    private int removeIndex;


    /**
     * Constructs the main interface of the application. Consists of a large title, buttons so the user can choose what
     * items they want to display, buttons to add or remove an item, and a large scroll pane and text area which display
     * the items of the arraylist
     */
    public GUI() {

        border = BorderFactory.createLineBorder(Color.black, 3);

        label = new JLabel();
        label.setText(" My Consumables Tracker ");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(new Font("MV Boli", Font.BOLD, 24));
        label.setBorder(border);
        label.setBounds(0, 0, 986, 50);

        panel = new JPanel();
        panel.setBounds(0, 0, 1000, 50);
        panel.setBackground(Color.LIGHT_GRAY);
        panel.add(label);
        panel.setLayout(new BorderLayout());
        panel.setVisible(true);


        allBtn = new JButton();
        allBtn.setText("All");
        allBtn.setFocusable(false);
        allBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        allBtn.addActionListener(this);
        allBtn.setVisible(true);

        expiredBtn = new JButton();
        expiredBtn.setText("Expired");
        expiredBtn.setFocusable(false);
        expiredBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        expiredBtn.addActionListener(this);
        expiredBtn.setVisible(true);

        nonExpiredBtn = new JButton();
        nonExpiredBtn.setText("Non Expired");
        nonExpiredBtn.setFocusable(false);
        nonExpiredBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        nonExpiredBtn.addActionListener(this);
        nonExpiredBtn.setVisible(true);

        expiringIn7Btn = new JButton();
        expiringIn7Btn.setText("Expiring in 7 Days");
        expiringIn7Btn.setFocusable(false);
        expiringIn7Btn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        expiringIn7Btn.addActionListener(this);
        expiringIn7Btn.setVisible(true);


        panel2 = new JPanel();
        panel2.setBounds(0, 50, 1000, 50);
        panel2.add(allBtn);
        panel2.add(expiredBtn);
        panel2.add(nonExpiredBtn);
        panel2.add(expiringIn7Btn);
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.setVisible(true);

        addBtn = new JButton();
        addBtn.setText("Add Item");
        addBtn.setFocusable(false);
        addBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        addBtn.addActionListener(this);
        addBtn.setVisible(true);

        removeBtn = new JButton();
        removeBtn.setText("Remove Item");
        removeBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        removeBtn.setFocusable(false);
        removeBtn.addActionListener(this);
        removeBtn.setVisible(true);

        panel3 = new JPanel();
        panel3.setBounds(0, 700, 1000, 50);
        panel3.add(addBtn);
        panel3.add(removeBtn);
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel3.setVisible(true);


        scrollTextArea = new JTextArea(5, 20);
        scrollTextArea.setBounds(0, 400, 5000, 400);
        scrollTextArea.setFont(new Font("Comic Sans MS", Font.ITALIC, 18));
        scrollTextArea.setBackground(Color.LIGHT_GRAY);
        scrollTextArea.setEditable(false);
        scrollTextArea.setVisible(true);

        scrollPane = new JScrollPane(scrollTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 125, 890, 550);
        scrollPane.setVisible(true);


        frame = new JFrame();
        frame.setSize(1000, 800);
        frame.setLayout(null);
        frame.setTitle("My Consumables Tracker");
        frame.add(panel);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, "Close the window and save the items in the list", "Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        consumableManager.saveItemsInServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        frame.setResizable(false);
        frame.setVisible(true);

        scrollTextArea.selectAll();
        scrollTextArea.replaceSelection("");

        try {
            consumableManager.getConsumablesFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listItems(consumableManager.getConsumables());

    }


    /**
     * Creates the dialog for adding an item. Consists of text fields which take in user input,
     * labels to show the field type, a combo box so the user can choose between
     * creating a food or drink item. and buttons to create or cancel the creation.
     */
    public void addPopUp() {

        comboBox = new JComboBox(consumableType);
        comboBox.addActionListener(this);
        comboBox.setSize(50, 50);
        comboBox.setMaximumSize(new Dimension(300, comboBox.getPreferredSize().height));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);


        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);


        notesField = new JTextField();
        notesField.setMaximumSize(new Dimension(300, 30));
        notesField.setAlignmentX(Component.LEFT_ALIGNMENT);


        priceField = new JTextField();
        priceField.setMaximumSize(new Dimension(300, 30));
        priceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent charClicked) {
                priceField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.');
            }
        });


        measurementField = new JTextField();
        measurementField.setMaximumSize(new Dimension(300, 30));
        measurementField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent charClicked) {
                measurementField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.');
            }
        });
        measurementField.setAlignmentX(Component.LEFT_ALIGNMENT);


        nameLabel = new JLabel();
        nameLabel.setText("Name");
        nameLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBounds(0, 50, 50, 50);

        notesLabel = new JLabel();
        notesLabel.setText("Notes");
        notesLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        notesLabel.setBounds(0, 100, 50, 50);
        notesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        priceLabel = new JLabel();
        priceLabel.setText("Price");
        priceLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        priceLabel.setBounds(0, 150, 50, 50);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        measurementLabel = new JLabel();
        measurementLabel.setText("Weight");
        measurementLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        measurementLabel.setBounds(0, 200, 50, 50);
        measurementLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        expiryLabel = new JLabel();
        expiryLabel.setText("Expiry Date");
        expiryLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        expiryLabel.setBounds(0, 250, 50, 50);
        expiryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        date = new DatePicker();
        date.setMaximumSize(new Dimension(300, 30));
        date.setAlignmentX(Component.LEFT_ALIGNMENT);

        typeLabel = new JLabel();
        typeLabel.setText("Type");
        typeLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        typeLabel.setBounds(0, 0, 50, 50);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        createBtn = new JButton();
        createBtn.setText("Create");
        createBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        createBtn.setFocusable(false);
        createBtn.addActionListener(this);
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        cancelBtn = new JButton();
        cancelBtn.setText("Cancel");
        cancelBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(this);
        cancelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
        addPanel.add(typeLabel);
        addPanel.add(comboBox);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(nameLabel);
        addPanel.add(nameField);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(notesLabel);
        addPanel.add(notesField);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(priceLabel);
        addPanel.add(priceField);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(measurementLabel);
        addPanel.add(measurementField);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(expiryLabel);
        addPanel.add(date);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.add(createBtn);
        addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addPanel.add(cancelBtn);
        addPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addPanel.setVisible(true);


        addItem = new JDialog();
        addItem.setTitle("Add Item");
        addItem.setSize(500, 500);
        addItem.setVisible(true);
        addItem.add(addPanel);

    }


    /**
     * Creates the dialog for removing an item. Consists of two buttons, a text field, a text label
     * a scroll pane and a text area.
     */
    public void removeItem() {

        removeScrollArea = new JTextArea(5, 20);
        removeScrollArea.setBounds(0, 200, 500, 500);
        removeScrollArea.setFont(new Font("Comic Sans MS", Font.ITALIC, 18));
        removeScrollArea.setBackground(Color.LIGHT_GRAY);
        removeScrollArea.setEditable(false);
        removeScrollArea.setVisible(true);

        removePane = new JScrollPane(removeScrollArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        removePane.setBounds(50, 150, 200, 200);
        removePane.setVisible(true);


        removeField = new JTextField();
        removeField.setMaximumSize(new Dimension(200, 30));
        removeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent charClicked) {
                removeField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.');
            }
        });
        removeField.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeLabel = new JLabel();
        removeLabel.setText("Choose what index you want to remove");
        removeLabel.setFont(new Font("MV Boli", Font.BOLD, 14));
        removeLabel.setBounds(0, 10, 100, 100);
        removeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        returnBtn = new JButton();
        returnBtn.setText("Cancel Removing");
        returnBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        returnBtn.setFocusable(false);
        returnBtn.addActionListener(this);
        returnBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        completeRemoveBtn = new JButton();
        completeRemoveBtn.setText("Remove selected");
        completeRemoveBtn.setFont(new Font("Cooper Black", Font.BOLD, 12));
        completeRemoveBtn.setFocusable(false);
        completeRemoveBtn.addActionListener(this);
        completeRemoveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        removePanel = new JPanel();
        removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.PAGE_AXIS));
        removePanel.add(removeLabel);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        removePanel.add(removeField);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        removePanel.add(removePane);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        removePanel.add(completeRemoveBtn);
        removePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        removePanel.add(returnBtn);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));


        removeItem = new JDialog();
        removeItem.setTitle("Removing Item");
        removeItem.setSize(500, 500);
        removeItem.add(removePanel);
        removeItem.setVisible(true);

        removeScrollArea.selectAll();
        removeScrollArea.replaceSelection("");
        repeatListItems(consumableManager.getConsumables());

    }


    /**
     * Performs an action based off the items action event (buttons and combo box)
     *
     * @param e takes in the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addBtn) {
            addPopUp();
            dispose();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(false);
        } else if (e.getSource() == allBtn) {
            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listItems(consumableManager.getConsumables());
        } else if (e.getSource() == expiredBtn) {
            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listExpired(consumableManager.getConsumables());
        } else if (e.getSource() == nonExpiredBtn) {
            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listNonExpired(consumableManager.getConsumables());
        } else if (e.getSource() == expiringIn7Btn) {
            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listExpiringIn7(consumableManager.getConsumables());
        } else if (e.getActionCommand().equals("comboBoxChanged")) {

            if (comboBox.getSelectedItem() == "Food") {
                measurementLabel.setText("Weight");
            }
            if (comboBox.getSelectedItem() == "Drink") {
                measurementLabel.setText("Volume");
            }

        } else if (e.getSource() == createBtn) {

            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addItem, "Name can not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (date.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addItem, "Date can not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (priceField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addItem, "Price can not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (measurementField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addItem, "Measurement can not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            itemName = nameField.getText();
            notes = notesField.getText();
            price = Double.parseDouble(priceField.getText());
            amount = Double.parseDouble(measurementField.getText());


            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            LocalDate localDate = LocalDate.parse(date.getText(), format);
            expiry = localDate.atStartOfDay();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            expiry.format(formatter);


            if (comboBox.getSelectedItem() == "Food") {
                itemType = 1;
                selectionType = 1;
            } else {
                itemType = 2;
                selectionType = 2;
            }

            addConsumable(consumableManager.getConsumables());

            addItem.setVisible(false);
            frame.setVisible(true);
            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listItems(consumableManager.getConsumables());

        } else if (e.getSource() == cancelBtn) {
            addItem.setVisible(false);
            frame.setVisible(true);
        } else if (e.getSource() == removeBtn) {
            dispose();
            frame.setVisible(false);
            removeItem();
        } else if (e.getSource() == completeRemoveBtn) {

            if (removeField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addItem, "Index can not be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (Integer.parseInt(removeField.getText()) < 0) {
                JOptionPane.showMessageDialog(addItem, "Index can not be negative", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (Integer.parseInt(removeField.getText()) == 0) {
                JOptionPane.showMessageDialog(addItem, "Index can not be zero", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (Integer.parseInt(removeField.getText()) > consumableManager.getConsumables().size()) {
                JOptionPane.showMessageDialog(addItem, "Index can not be greater than amount of items", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            removeScrollArea.selectAll();
            removeScrollArea.replaceSelection("");
            repeatListItems(consumableManager.getConsumables());

            removeIndex = Integer.parseInt(removeField.getText());

            try {
                consumableManager.removeItem(removeIndex);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            consumableManager.getConsumables().clear();
            try {
                consumableManager.getConsumablesFromServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            scrollTextArea.selectAll();
            scrollTextArea.replaceSelection("");
            listItems(consumableManager.getConsumables());

            removeItem.setVisible(false);
            frame.setVisible(true);
        } else if (e.getSource() == returnBtn) {
            removeItem.setVisible(false);
            frame.setVisible(true);
        }

    }


    /**
     * Adds an item to the arraylist
     *
     * @param consumables the arraylist of items
     */
    public void addConsumable(ArrayList<Consumable> consumables) {

        Consumable item = consumableFactory.getConsumable(itemType, itemName, notes, price, amount, expiry);
        consumables.add(item);
        String consumableItem = new JSONObject()
                .put("type", itemType)
                .put("name", itemName)
                .put("notes", notes)
                .put("price", price)
                .put("size", amount)
                .put("expiryDate", expiry)

                .toString();
        try {
            consumableManager.addConsumableToServer(itemType, consumableItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Lists the items in the arraylist in the remove screen
     *
     * @param consumables the array list of items
     */
    public void repeatListItems(ArrayList<Consumable> consumables) {
        if (consumables.size() == 0) {
            removeScrollArea.append("No items to show. \n");
        } else {
            int displayIndex = 1;

            Collections.sort(consumables);
            for (Consumable items : consumables) {
                removeScrollArea.append("Item #" + displayIndex + "\n");
                if (items.checkType() == 1) {
                    removeScrollArea.append("This is a food item \n");

                } else {
                    removeScrollArea.append("This is a drink item \n");
                }
                removeScrollArea.append(items.toString() + "\n");
                if (items.daysUntilExpiry() > 0) {
                    removeScrollArea.append("This item will expire in " + items.daysUntilExpiry() + " day(s). \n");
                }
                if (items.daysUntilExpiry() < 0) {
                    removeScrollArea.append("This item expired " + (-1 * items.daysUntilExpiry()) + " day(s) ago. \n");
                }
                if (items.daysUntilExpiry() == 0) {
                    removeScrollArea.append("This item will expire today. \n");
                }
                removeScrollArea.append("\n");
                displayIndex++;
            }
        }
    }


    /**
     * Lists all the items in the array list
     *
     * @param consumables the arraylist of items
     */
    public void listItems(ArrayList<Consumable> consumables) {

        if (consumables.size() == 0) {
            scrollTextArea.append("No items to show. \n");
        } else {
            int displayIndex = 1;

            Collections.sort(consumables);
            for (Consumable items : consumables) {
                scrollTextArea.append("Item #" + displayIndex + "\n");
                if (items.checkType() == 1) {
                    scrollTextArea.append("This is a food item \n");

                } else {
                    scrollTextArea.append("This is a drink item \n");
                }
                scrollTextArea.append(items.toString() + "\n");
                if (items.daysUntilExpiry() > 0) {
                    scrollTextArea.append("This item will expire in " + items.daysUntilExpiry() + " day(s). \n");
                }
                if (items.daysUntilExpiry() < 0) {
                    scrollTextArea.append("This item expired " + (-1 * items.daysUntilExpiry()) + " day(s) ago. \n");
                }
                if (items.daysUntilExpiry() == 0) {
                    scrollTextArea.append("This item will expire today. \n");
                }
                scrollTextArea.append("\n");
                displayIndex++;
            }
        }
    }


    /**
     * Lists the expired items in the arraylist
     *
     * @param consumables the arraylist of items
     */
    public void listExpired(ArrayList<Consumable> consumables) {

        int displayIndex = 1;
        int expiredCounter = 0;


        Collections.sort(consumables);
        for (Consumable items : consumables) {
            if (items.daysUntilExpiry() < 0) {
                scrollTextArea.append("Item #" + displayIndex + "\n");
                scrollTextArea.append(items.toString() + "\n");
                scrollTextArea.append("This item expired " + (-1 * items.daysUntilExpiry()) + " day(s) ago. \n");
                scrollTextArea.append("\n");
                displayIndex++;
                expiredCounter++;
            }
        }

        if (expiredCounter == 0) {
            scrollTextArea.append("No expired items. \n");
        }

    }


    /**
     * Lists the items that are not expired in the array list
     *
     * @param consumables the arraylist of items
     */
    public void listNonExpired(ArrayList<Consumable> consumables) {

        int displayIndex = 1;
        int nonExpiredCounter = 0;

        Collections.sort(consumables);
        for (Consumable items : consumables) {

            if (items.daysUntilExpiry() > 0) {
                scrollTextArea.append("Item #" + displayIndex + "\n");
                scrollTextArea.append(items.toString() + "\n");
                scrollTextArea.append("This item will expire in " + items.daysUntilExpiry() + " day(s). \n");
                scrollTextArea.append("\n");
                displayIndex++;
                nonExpiredCounter++;
            } else if (items.daysUntilExpiry() == 0) {
                scrollTextArea.append("Item #" + displayIndex + "\n");
                scrollTextArea.append(items.toString() + "\n");
                scrollTextArea.append("This item will expire today. \n");
                scrollTextArea.append("\n");
                displayIndex++;
                nonExpiredCounter++;
            }
        }

        if (nonExpiredCounter == 0) {
            scrollTextArea.append("No Non-expired items. \n");
            scrollTextArea.append("\n");
        }
    }


    /**
     * Lists the items in the array list that are expiring in 7 days
     *
     * @param consumables the arraylist of items
     */
    public void listExpiringIn7(ArrayList<Consumable> consumables) {

        int displayIndex = 1;
        int expiringIn7Counter = 0;

        Collections.sort(consumables);
        for (Consumable items : consumables) {

            if (items.daysUntilExpiry() <= 7 && items.daysUntilExpiry() >= 0) {
                scrollTextArea.append("Item #" + displayIndex + "\n");
                scrollTextArea.append(items.toString() + "\n");

                if (items.daysUntilExpiry() == 0) {
                    scrollTextArea.append("This item will expire today. \n");
                } else {
                    scrollTextArea.append("This item will expire in " + items.daysUntilExpiry() + " day(s). \n");
                }
                scrollTextArea.append("\n");
                displayIndex++;
                expiringIn7Counter++;
            }
        }

        if (expiringIn7Counter == 0) {
            scrollTextArea.append("No items expiring in 7 days. \n");
        }
    }
}
