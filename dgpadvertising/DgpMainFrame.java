import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.JDatePanel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.util.Properties;

public class DgpMainFrame extends JFrame {

    private ClientDao clientDao = new ClientDaoImpl();
    private ProjectDao projectDao = new ProjectDaoImpl();

    private DefaultTableModel clientTableModel;
    private JTable clientTable;

    private DefaultTableModel projectTableModel;
    private JTable projectTable;
    private JComboBox<Client> clientDropdown;

    // ---------- DESIGN SYSTEM ----------
    private static final Color NAVY = new Color(24, 32, 45);
    private static final Color NAVY_LIGHT = new Color(35, 45, 61);
    private static final Color ACCENT = new Color(37, 99, 235);
    private static final Color ACCENT_DARK = new Color(29, 78, 216);
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color BORDER = new Color(229, 231, 235);
    private static final Color SUCCESS = new Color(22, 163, 74);
    private static final Color DANGER = new Color(220, 38, 38);

    private static final Font TITLE_FONT =
            new Font("SansSerif", Font.BOLD, 24);

    private static final Font SECTION_FONT =
            new Font("SansSerif", Font.BOLD, 18);

    private static final Font BODY_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    private static final Font BUTTON_FONT =
            new Font("SansSerif", Font.BOLD, 12);

    public DgpMainFrame() {
        setTitle("DGP Advertising - Client & Project Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);

        applyLookAndFeel();
        setContentPane(createMainPanel());
    }

    private void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {
        }

        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.background", CARD);
        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("TextArea.font", BODY_FONT);
        UIManager.put("ComboBox.font", BODY_FONT);
        UIManager.put("Table.font", BODY_FONT);
        UIManager.put(
                "TableHeader.font",
                new Font("SansSerif", Font.BOLD, 12)
        );
    }

    // ---------- MAIN LAYOUT ----------

    private JPanel createMainPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createSidebar(), BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);

        content.add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBackground(BG);
        tabs.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 16, 16, 16
                )
        );

        tabs.addTab("Clients", createClientsPanel());
        tabs.addTab("Projects", createProjectsPanel());

        content.add(tabs, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        return root;
    }

    // ---------- SIDEBAR ----------

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());

        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(
                new EmptyBorder(24, 18, 20, 18)
        );

        JPanel brand = new JPanel();
        brand.setOpaque(false);

        brand.setLayout(
                new BoxLayout(
                        brand,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo = new JLabel("DGP");
        logo.setForeground(Color.WHITE);
        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("ADVERTISING");
        subtitle.setForeground(
                new Color(174, 184, 198)
        );
        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(logo);
        brand.add(Box.createVerticalStrut(1));
        brand.add(subtitle);

        brand.add(
                Box.createVerticalStrut(30)
        );

        JLabel section = new JLabel("WORKSPACE");
        section.setForeground(
                new Color(130, 142, 160)
        );
        section.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(section);
        brand.add(
                Box.createVerticalStrut(8)
        );

        JButton clients =
                createNavButton("Clients");

        JButton projects =
                createNavButton("Projects");

        clients.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        projects.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        brand.add(clients);
        brand.add(
                Box.createVerticalStrut(4)
        );
        brand.add(projects);

        JPanel nav = new JPanel();
        nav.setOpaque(false);

        nav.setLayout(
                new BoxLayout(
                        nav,
                        BoxLayout.Y_AXIS
                )
        );

        nav.add(brand);

        clients.addActionListener(
                e -> selectTab(0)
        );

        projects.addActionListener(
                e -> selectTab(1)
        );

        sidebar.add(
                nav,
                BorderLayout.NORTH
        );

        JLabel footer =
                new JLabel(
                        "Client & Project Manager"
                );

        footer.setForeground(
                new Color(120, 131, 148)
        );

        footer.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        sidebar.add(
                footer,
                BorderLayout.SOUTH
        );

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton button =
                new JButton("  " + text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.BLACK
        );

        button.setBackground(NAVY);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setBorder(
                new EmptyBorder(
                        11, 12, 11, 12
                )
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        return button;
    }

    private void selectTab(int index) {
        Container content =
                getContentPane();

        if (content instanceof JPanel root) {

            Component center =
                    ((BorderLayout) root.getLayout())
                            .getLayoutComponent(
                                    BorderLayout.CENTER
                            );

            if (center instanceof JPanel panel) {

                Component tabsComponent =
                        ((BorderLayout) panel.getLayout())
                                .getLayoutComponent(
                                        BorderLayout.CENTER
                                );

                if (tabsComponent instanceof JTabbedPane tabs) {
                    tabs.setSelectedIndex(index);
                }
            }
        }
    }

    // ---------- HEADER ----------

    private JPanel createHeaderPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(BG);

        panel.setBorder(
                new EmptyBorder(
                        24, 24, 14, 24
                )
        );

        JPanel titles = new JPanel();

        titles.setOpaque(false);

        titles.setLayout(
                new BoxLayout(
                        titles,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Client & Project Manager"
                );

        title.setFont(TITLE_FONT);
        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "V2.0"
                );

        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(MUTED);

        titles.add(title);

        titles.add(
                Box.createVerticalStrut(4)
        );

        titles.add(subtitle);

        panel.add(
                titles,
                BorderLayout.WEST
        );

        return panel;
    }

    // ---------- CARD ----------

    private JPanel cardPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                16, 16, 16, 16
                        )
                )
        );

        return panel;
    }

    // ---------- BUTTONS ----------

    private JButton createButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(text);

        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(background);
        button.setBorder(
                new EmptyBorder(
                        9, 14, 9, 14
                )
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                8, 13, 8, 13
                        )
                )
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // ---------- TABLE STYLING ----------

    private void styleTable(JTable table) {

        table.setFont(BODY_FONT);
        table.setRowHeight(34);

        table.setShowGrid(false);

        table.setIntercellSpacing(
                new Dimension(0, 0)
        );

        table.setSelectionBackground(
                new Color(219, 234, 254)
        );

        table.setSelectionForeground(TEXT);

        table.setFillsViewportHeight(true);

        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        table.getTableHeader().setForeground(
                Color.BLACK
        );

        table.getTableHeader().setBackground(
                new Color(249, 250, 251)
        );

        table.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, BORDER
                )
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        DefaultTableCellRenderer left =
                new DefaultTableCellRenderer();

        left.setBorder(
                new EmptyBorder(
                        0, 8, 0, 8
                )
        );

        left.setVerticalAlignment(
                SwingConstants.CENTER
        );

        table.setDefaultRenderer(
                Object.class,
                left
        );
    }

    // ---------- CLIENTS TAB ----------

    private JPanel createClientsPanel() {

        JPanel outer =
                new JPanel(
                        new BorderLayout()
                );

        outer.setBackground(BG);

        outer.setBorder(
                new EmptyBorder(
                        0, 0, 0, 0
                )
        );

        JPanel card = cardPanel();

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);

        JLabel title =
                new JLabel("Clients");

        title.setFont(SECTION_FONT);
        title.setForeground(TEXT);

        JLabel description =
                new JLabel(
                        "Manage your advertising clients and contact information."
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        description.setForeground(MUTED);

        JPanel headingText =
                new JPanel();

        headingText.setOpaque(false);

        headingText.setLayout(
                new BoxLayout(
                        headingText,
                        BoxLayout.Y_AXIS
                )
        );

        headingText.add(title);

        headingText.add(
                Box.createVerticalStrut(3)
        );

        headingText.add(description);

        heading.add(
                headingText,
                BorderLayout.WEST
        );

        JButton add =
                createButton(
                        "+ Add Client",
                        ACCENT
                );

        add.addActionListener(
                this::onAddClient
        );

        heading.add(
                add,
                BorderLayout.EAST
        );

        card.add(
                heading,
                BorderLayout.NORTH
        );

        clientTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Client ID",
                                "Name",
                                "Contact",
                                "Phone",
                                "Email",
                                "Address",
                                "Status"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        clientTable =
                new JTable(
                        clientTableModel
                );

        styleTable(clientTable);

        JScrollPane scroll =
                new JScrollPane(
                        clientTable
                );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scroll.getViewport()
                .setBackground(Color.WHITE);

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout()
                );

        tablePanel.setBackground(Color.WHITE);

        tablePanel.setBorder(
                new EmptyBorder(
                        16, 0, 0, 0
                )
        );

        tablePanel.add(
                scroll,
                BorderLayout.CENTER
        );

        card.add(
                tablePanel,
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton refresh =
                createSecondaryButton(
                        "Refresh"
                );

        JButton edit =
                createSecondaryButton(
                        "Edit Client"
                );

        JButton viewNotes =
                createSecondaryButton(
                        "View Notes"
                );

        JButton export =
                createButton(
                        "Export CSV",
                        ACCENT
                );

        refresh.addActionListener(
                e -> loadClients()
        );

        edit.addActionListener(
                this::onEditClient
        );

        viewNotes.addActionListener(
                this::onViewClientNotes
        );

        export.addActionListener(
                this::onExportCSV
        );

        buttons.add(refresh);
        buttons.add(viewNotes);
        buttons.add(edit);
        buttons.add(export);

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.setOpaque(false);

        bottom.setBorder(
                new EmptyBorder(
                        14, 0, 0, 0
                )
        );

        bottom.add(
                buttons,
                BorderLayout.EAST
        );

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        outer.add(
                card,
                BorderLayout.CENTER
        );

        loadClients();

        return outer;
    }

    private void loadClients() {

        clientTableModel.setRowCount(0);

        for (Client c :
                clientDao.getAllClients()) {

            clientTableModel.addRow(
                    new Object[]{
                            c.getClientId(),
                            c.getName(),
                            c.getContactPerson(),
                            c.getPhone(),
                            c.getEmail(),
                            c.getAddress(),
                            c.getStatus()
                    }
            );
        }
    }

    // ---------- PROJECTS TAB ----------

    private JPanel createProjectsPanel() {
    JPanel outer = new JPanel(new BorderLayout());
    outer.setBackground(BG);
    outer.setBorder(new EmptyBorder(0, 8, 8, 8));

    JPanel card = cardPanel();

    // ---------- TOP BAR ----------
    JPanel top = new JPanel(new BorderLayout());
    top.setOpaque(false);
    top.setBorder(new EmptyBorder(0, 0, 14, 0));

    JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );

        JLabel projectTitle = new JLabel("Projects");
        projectTitle.setFont(SECTION_FONT);
        projectTitle.setForeground(TEXT);

        JLabel projectDescription = new JLabel(
                "Track project details, documents, costs, and status"
        );
        projectDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        projectDescription.setForeground(MUTED);

        titlePanel.add(projectTitle);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(projectDescription);

        top.add(titlePanel, BorderLayout.WEST);

    // ---------- CLIENT DROPDOWN ----------
    JPanel selector = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    selector.setOpaque(false);

    JLabel clientLabel = new JLabel("Client:");
    clientLabel.setFont(BODY_FONT);

    clientDropdown = new JComboBox<>();
    clientDropdown.setFont(BODY_FONT);
    clientDropdown.setPreferredSize(new Dimension(220, 36));

    // "All Clients" option
    clientDropdown.addItem(null);

    // Load all clients into dropdown
    for (Client c : clientDao.getAllClients()) {
        clientDropdown.addItem(c);
    }

    // Display client names instead of Java object references
    clientDropdown.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus
            );

            if (value == null) {
                setText("All Clients");
            } else if (value instanceof Client) {
                setText(((Client) value).getName());
            }

            return this;
        }
    });

    // Automatically update projects when client changes
    clientDropdown.addActionListener(e -> loadProjectsForSelectedClient());

    selector.add(clientLabel);
    selector.add(clientDropdown);

    top.add(selector, BorderLayout.EAST);

    card.add(top, BorderLayout.NORTH);

    // ---------- PROJECT TABLE ----------
    projectTableModel = new DefaultTableModel(
        new Object[]{
            "ID",
            "Client",
            "Project",
            "Location",
            "Start Date",
            "End Date",
            "PO #",
            "Invoice #",
            "DR #",
            "Status",
            "Cost (₱)"
        },
        0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    projectTable = new JTable(projectTableModel);
    styleTable(projectTable);

    card.add(
        new JScrollPane(projectTable),
        BorderLayout.CENTER
    );

    // ---------- BUTTONS ----------
    JPanel buttons = new JPanel(
        new FlowLayout(FlowLayout.LEFT, 8, 14)
    );
    buttons.setOpaque(false);

    JButton add = createButton("+  Add Project", ACCENT);
    JButton edit = createSecondaryButton("Edit");
    JButton del = createSecondaryButton("Delete");
    JButton notes = createSecondaryButton("Show Notes");
    JButton specsBtn = createSecondaryButton("Show Specs");

    JComboBox<String> sortBox = new JComboBox<>(
        new String[]{
                        "Sort by Date",
                        "Start Date: Earliest → Latest",
                        "Start Date: Latest → Earliest",
                        "End Date: Earliest → Latest",
                        "End Date: Latest → Earliest"
                }
        );

sortBox.setFont(BODY_FONT);
sortBox.setPreferredSize(new Dimension(220, 34));

    add.addActionListener(this::onAddProject);
    edit.addActionListener(this::onEditProject);
    del.addActionListener(this::onDeleteProject);
    notes.addActionListener(this::onShowNotes);
    specsBtn.addActionListener(this::onShowSpecs);
    sortBox.addActionListener(e -> sortProjects((String) sortBox.getSelectedItem()));

    buttons.add(add);
    buttons.add(edit);
    buttons.add(del);
    buttons.add(notes);
    buttons.add(specsBtn);
    buttons.add(sortBox);

    card.add(buttons, BorderLayout.SOUTH);

    outer.add(card, BorderLayout.CENTER);

    // Initially show all projects
    loadProjectsForSelectedClient();

    return outer;
}

    // ---------- LOAD ALL PROJECTS ----------

    private void loadAllProjects() {

        projectTableModel.setRowCount(0);

        for (Client c :
                clientDao.getAllClients()) {

            List<Project> projects =
                    projectDao.getProjectsByClient(
                            c.getClientId()
                    );

            for (Project p : projects) {

                addProjectRow(
                        c,
                        p
                );
            }
        }
    }

    private void addProjectRow(
            Client c,
            Project p
    ) {

        projectTableModel.addRow(
                new Object[]{
                        p.getProjectId(),
                        c.getName(),
                        p.getProjectName(),
                        p.getLocation(),
                        p.getDateStarted(),
                        p.getDateCompleted(),
                        p.getPoNumber(),
                        p.getSalesInvoice(),
                        p.getDrNumber(),
                        p.getStatus(),
                        p.getTotalCost()
                }
        );
    }

    private void sortProjects(String option) {

        if (option == null || option.equals("Sort by Date")) {
                return;
        }

        int column;

        if (option.startsWith("Start Date")) {
                column = 4;
        } else {
                column = 5;
        }

        boolean ascending =
                option.contains("Earliest → Latest");

        projectTable.getRowSorter().setSortKeys(
                java.util.List.of(
                        new javax.swing.RowSorter.SortKey(
                                column,
                                ascending
                                        ? javax.swing.SortOrder.ASCENDING
                                        : javax.swing.SortOrder.DESCENDING
                        )
                )
        );
        }
    // ---------- PROJECT FILTER ----------
        private void loadProjectsForSelectedClient() {

    projectTableModel.setRowCount(0);

        Client selectedClient =
                (Client) clientDropdown.getSelectedItem();

        // Show all projects
        if (selectedClient == null) {

                for (Client c : clientDao.getAllClients()) {

                for (Project p :
                        projectDao.getProjectsByClient(
                                c.getClientId())) {

                        addProjectRow(c, p);
                }
                }

                return;
        }

        // Show only selected client's projects
        for (Project p :
                projectDao.getProjectsByClient(
                        selectedClient.getClientId())) {

                addProjectRow(selectedClient, p);
        }
        }

        //helper

    // ---------- CLIENT ADD ----------

   private void onAddClient(ActionEvent e) {

        Client c = showClientForm(null);

        if (c != null) {

                clientDao.addClient(c);

                // Refresh clients table
                loadClients();

                // Add the new client to the Projects dropdown
                if (clientDropdown != null) {
                clientDropdown.addItem(c);
                }
        }
        }

    // ---------- CLIENT EDIT ----------

    private void onEditClient(
            ActionEvent e
    ) {

        int row =
                clientTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a client first.",
                    "No Client Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                clientTable.convertRowIndexToModel(
                        row
                );

        int clientId =
                (int) clientTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        Client existing =
                clientDao.getClientById(
                        clientId
                );

        Client updated =
                showClientForm(existing);

        if (updated != null) {

            updated.setClientId(
                    clientId
            );

            clientDao.updateClient(
                    updated
            );

            loadClients();
        }
    }

    // ---------- CLIENT FORM ----------

    private Client showClientForm(
            Client existing
    ) {

        JTextField name =
                new JTextField();

        JTextField contact =
                new JTextField();

        JTextField phone =
                new JTextField();

        JTextField email =
                new JTextField();

        JTextField address =
                new JTextField();

        JTextArea notes =
                new JTextArea(4, 25);

        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "ACTIVE",
                                "INACTIVE"
                        }
                );

        if (existing != null) {

            name.setText(
                    existing.getName()
            );

            contact.setText(
                    existing.getContactPerson()
            );

            phone.setText(
                    existing.getPhone()
            );

            email.setText(
                    existing.getEmail()
            );

            address.setText(
                    existing.getAddress()
            );

            notes.setText(
                    existing.getNotes()
            );

            statusBox.setSelectedItem(
                    existing.getStatus()
            );
        }

        JPanel panel =
                createFormPanel();

        addFormRow(
                panel,
                "Client Name",
                name
        );

        addFormRow(
                panel,
                "Contact Person",
                contact
        );

        addFormRow(
                panel,
                "Phone",
                phone
        );

        addFormRow(
                panel,
                "Email",
                email
        );

        addFormRow(
                panel,
                "Address",
                address
        );

        addFormRow(
                panel,
                "Status",
                statusBox
        );

        addFormRow(
                panel,
                "Notes",
                new JScrollPane(notes)
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        existing == null
                                ? "Add Client"
                                : "Edit Client",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        Client c =
                new Client();

        c.setName(
                name.getText().trim()
        );

        c.setContactPerson(
                contact.getText().trim()
        );

        c.setPhone(
                phone.getText().trim()
        );

        c.setEmail(
                email.getText().trim()
        );

        c.setAddress(
                address.getText().trim()
        );

        c.setNotes(
                notes.getText().trim()
        );

        c.setStatus(
                (String) statusBox.getSelectedItem()
        );

        return c;
    }

    // ---------- CLIENT NOTES ----------

    private void onViewClientNotes(
            ActionEvent e
    ) {

        int row =
                clientTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a client first.",
                    "No Client Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                clientTable.convertRowIndexToModel(
                        row
                );

        int clientId =
                (int) clientTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        Client c =
                clientDao.getClientById(
                        clientId
                );

        JTextArea area =
                new JTextArea(
                        c == null
                                ? ""
                                : c.getNotes()
                );

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(BODY_FONT);

        JScrollPane scroll =
                new JScrollPane(area);

        scroll.setPreferredSize(
                new Dimension(
                        500,
                        250
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Client Notes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ---------- DATE VALIDATION ----------

    private LocalDate parseDateSafe(
            String text
    ) {

        try {

            return LocalDate.parse(
                    text.trim()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n"
                            + "Please use YYYY-MM-DD "
                            + "(e.g. 2025-12-01)",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    // ---------- ADD PROJECT ----------

    private void onAddProject(ActionEvent e) {

        System.out.println("=== ADD PROJECT CLICKED ===");

        Client selectedClient =
                (Client) clientDropdown.getSelectedItem();

        if (selectedClient == null) {

                System.out.println("NO CLIENT SELECTED");

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a client first.",
                        "Select Client",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
        }

        System.out.println(
                "Selected client: "
                + selectedClient.getName()
                + " (ID: "
                + selectedClient.getClientId()
                + ")"
        );

        Project p = showProjectForm(null);

        if (p == null) {
                System.out.println("Project form was cancelled.");
                return;
        }

        System.out.println(
                "Project created: "
                + p.getProjectName()
        );

        p.setClientId(
                selectedClient.getClientId()
        );

        System.out.println(
                "Saving project with client ID: "
                + p.getClientId()
        );

        projectDao.addProject(p);

        System.out.println("Project DAO add completed.");

        loadProjectsForSelectedClient();

        System.out.println(
                "Projects table refreshed."
        );
        }

    // ---------- EDIT PROJECT ----------

    private void onEditProject(
            ActionEvent e
    ) {

        int row =
                projectTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a project first.",
                    "No Project Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                projectTable.convertRowIndexToModel(
                        row
                );

        int projectId =
                (int) projectTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        Project existing =
                projectDao.getProjectById(
                        projectId
                );

        if (existing == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Project not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Project updated =
                showProjectForm(
                        existing
                );

        if (updated != null) {

            updated.setProjectId(
                    projectId
            );

            updated.setClientId(
                    existing.getClientId()
            );

            projectDao.updateProject(
                    updated
            );

            loadProjectsForSelectedClient();
        }
    }

    // ---------- DELETE PROJECT ----------

    private void onDeleteProject(
            ActionEvent e
    ) {

        int row =
                projectTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a project first.",
                    "No Project Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                projectTable.convertRowIndexToModel(
                        row
                );

        int projectId =
                (int) projectTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete this project?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION) {

            projectDao.deleteProject(
                    projectId
            );

            loadProjectsForSelectedClient();
        }
    }

    // ---------- EXPORT CSV ----------

    private void onExportCSV(
            ActionEvent e
    ) {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Export Clients and Projects"
        );

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        java.io.File file =
                chooser.getSelectedFile();

        if (!file.getName()
                .toLowerCase()
                .endsWith(".csv")) {

            file =
                    new java.io.File(
                            file.getAbsolutePath()
                                    + ".csv"
                    );
        }

        try (
                java.io.PrintWriter pw =
                        new java.io.PrintWriter(file)
        ) {

            pw.println(
                    "Client ID,Client Name,"
                            + "Project ID,Project Name,"
                            + "Location,Start Date,"
                            + "End Date,PO,Invoice,DR,"
                            + "Status,Cost,Notes,Specs"
            );

            for (Client c :
                    clientDao.getAllClients()) {

                List<Project> projects =
                        projectDao.getProjectsByClient(
                                c.getClientId()
                        );

                if (projects.isEmpty()) {

                    pw.printf(
                            "%d,\"%s\",,,,,,,,,\n",
                            c.getClientId(),
                            escapeCsv(
                                    c.getName()
                            )
                    );

                } else {

                    for (Project p :
                            projects) {

                        pw.printf(
                                "%d,\"%s\",%d,\"%s\","
                                        + "\"%s\",%s,%s,%s,%s,%s,"
                                        + "\"%s\",%.2f,\"%s\",\"%s\"\n",

                                c.getClientId(),

                                escapeCsv(
                                        c.getName()
                                ),

                                p.getProjectId(),

                                escapeCsv(
                                        p.getProjectName()
                                ),

                                escapeCsv(
                                        p.getLocation()
                                ),

                                p.getDateStarted(),

                                p.getDateCompleted(),

                                p.getPoNumber(),

                                p.getSalesInvoice(),

                                p.getDrNumber(),

                                escapeCsv(
                                        p.getStatus()
                                ),

                                p.getTotalCost(),

                                escapeCsv(
                                        p.getNotes()
                                ),

                                escapeCsv(
                                        p.getSpecs()
                                )
                        );
                    }
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Export successful:\n"
                            + file.getAbsolutePath(),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Export failed: "
                            + ex.getMessage(),
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- PROJECT FORM ----------

    private Project showProjectForm(
            Project existing
    ) {

        JTextField nameField =
                new JTextField();

        JTextField locationField =
                new JTextField();

        UtilDateModel startDateModel = new UtilDateModel();
        UtilDateModel endDateModel = new UtilDateModel();

        Properties dateProperties = new Properties();
        dateProperties.put("text.today", "Today");
        dateProperties.put("text.month", "Month");
        dateProperties.put("text.year", "Year");

        JDatePanelImpl startDatePanel =
                new JDatePanelImpl(
                        startDateModel,
                        dateProperties
                );

        JDatePickerImpl startDatePicker =
                new JDatePickerImpl(
                        startDatePanel,
                        new DateLabelFormatter()
                );

        JDatePanelImpl endDatePanel =
                new JDatePanelImpl(
                        endDateModel,
                        dateProperties
                );

        JDatePickerImpl endDatePicker =
                new JDatePickerImpl(
                        endDatePanel,
                        new DateLabelFormatter()
                );

        JTextField poField =
                new JTextField();

        JTextField invoiceField =
                new JTextField();

        JTextField drField =
                new JTextField();

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "----",
                                "ONGOING",
                                "COMPLETED",
                                "CANCELLED"
                        }
                );

        JTextField costField =
                new JTextField();

        JTextArea notesArea =
                new JTextArea(4, 25);

        JTextArea specsArea =
                new JTextArea(4, 25);

        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        specsArea.setLineWrap(true);
        specsArea.setWrapStyleWord(true);

        if (existing != null) {

            nameField.setText(
                    existing.getProjectName()
            );

            locationField.setText(
                    existing.getLocation()
            );

           if (existing.getDateStarted() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();

                cal.set(
                        existing.getDateStarted().getYear(),
                        existing.getDateStarted().getMonthValue() - 1,
                        existing.getDateStarted().getDayOfMonth()
                );

                startDateModel.setValue(cal.getTime());
                }

                if (existing.getDateCompleted() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();

                cal.set(
                        existing.getDateCompleted().getYear(),
                        existing.getDateCompleted().getMonthValue() - 1,
                        existing.getDateCompleted().getDayOfMonth()
                );

                endDateModel.setValue(cal.getTime());
                }

            if (existing.getPoNumber()
                    != null) {

                poField.setText(
                        existing.getPoNumber()
                                .toString()
                );
            }

            if (existing.getSalesInvoice()
                    != null) {

                invoiceField.setText(
                        existing.getSalesInvoice()
                                .toString()
                );
            }

            if (existing.getDrNumber()
                    != null) {

                drField.setText(
                        existing.getDrNumber()
                                .toString()
                );
            }

            statusBox.setSelectedItem(
                    existing.getStatus()
            );

            costField.setText(
                    String.valueOf(
                            existing.getTotalCost()
                    )
            );

            notesArea.setText(
                    existing.getNotes()
            );

            specsArea.setText(
                    existing.getSpecs()
            );
        }

        JPanel panel =
                createFormPanel();

        addFormRow(
                panel,
                "Project Name",
                nameField
        );

        addFormRow(
                panel,
                "Location",
                locationField
        );

       addFormRow(
        panel,
        "Start Date",
        startDatePicker
        );

        addFormRow(
                panel,
                "End Date",
                endDatePicker
        );

        addFormRow(
                panel,
                "PO Number",
                poField
        );

        addFormRow(
                panel,
                "Sales Invoice",
                invoiceField
        );

        addFormRow(
                panel,
                "DR Number",
                drField
        );

        addFormRow(
                panel,
                "Status",
                statusBox
        );

        addFormRow(
                panel,
                "Total Cost",
                costField
        );

        addFormRow(
                panel,
                "Notes",
                new JScrollPane(notesArea)
        );

        addFormRow(
                panel,
                "Specs",
                new JScrollPane(specsArea)
        );

        JScrollPane formScroll =
                new JScrollPane(panel);

        formScroll.setBorder(null);

        formScroll.setPreferredSize(
                new Dimension(
                        650,
                        500
                )
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        formScroll,
                        existing == null
                                ? "Add Project"
                                : "Edit Project",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        Project p =
                new Project();

        // KEEP CLIENT ID ON EDIT
        if (existing != null) {

                p.setClientId(
                        existing.getClientId()
                );
                }

                p.setProjectName(
                        nameField.getText().trim()
                );

                p.setLocation(
                        locationField.getText().trim()
                );

                p.setStatus(
                        (String) statusBox.getSelectedItem()
                );

                p.setNotes(
                        notesArea.getText().trim()
                );

                p.setSpecs(
                        specsArea.getText().trim()
                );

                if (startDateModel.getValue() != null) {

        java.util.Date date =
                (java.util.Date) startDateModel.getValue();

        LocalDate d =
                date.toInstant()
                        .atZone(
                                java.time.ZoneId.systemDefault()
                        )
                        .toLocalDate();

        p.setDateStarted(d);

        } else {

        p.setDateStarted(null);
        }


        if (endDateModel.getValue() != null) {

        java.util.Date date =
                (java.util.Date) endDateModel.getValue();

        LocalDate d =
                date.toInstant()
                        .atZone(
                                java.time.ZoneId.systemDefault()
                        )
                        .toLocalDate();

        p.setDateCompleted(d);

        } else {

        p.setDateCompleted(null);
        }

        try {

            if (!poField.getText()
                    .isBlank()) {

                p.setPoNumber(
                        Integer.parseInt(
                                poField.getText()
                                        .trim()
                        )
                );
            }

            if (!invoiceField.getText()
                    .isBlank()) {

                p.setSalesInvoice(
                        Integer.parseInt(
                                invoiceField.getText()
                                        .trim()
                        )
                );
            }

            if (!drField.getText()
                    .isBlank()) {

                p.setDrNumber(
                        Integer.parseInt(
                                drField.getText()
                                        .trim()
                        )
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "PO, Invoice, and DR numbers "
                            + "must be valid whole numbers.",
                    "Invalid Number",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        try {

            if (costField.getText()
                    .isBlank()) {

                p.setTotalCost(0);

            } else {

                p.setTotalCost(
                        Double.parseDouble(
                                costField.getText()
                                        .trim()
                        )
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Total cost must be a valid number.",
                    "Invalid Cost",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return p;
    }

    // ---------- FORM HELPERS ----------

    private static class DateLabelFormatter
                extends JFormattedTextField.AbstractFormatter {

        private final java.text.SimpleDateFormat dateFormat =
                new java.text.SimpleDateFormat("MMM dd, yyyy");

        @Override
        public Object stringToValue(String text)
                throws java.text.ParseException {

                return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value)
                throws java.text.ParseException {

                if (value == null) {
                return "";
                }

                if (value instanceof java.util.Calendar) {
                return dateFormat.format(
                        ((java.util.Calendar) value).getTime()
                );
                }

                if (value instanceof java.util.Date) {
                return dateFormat.format(value);
                }

                return "";
        }
        }

    private JPanel createFormPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(CARD);

        panel.setBorder(
                new EmptyBorder(
                        8, 8, 8, 8
                )
        );

        return panel;
    }

    private void addFormRow(
            JPanel panel,
            String label,
            Component component
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6, 6, 6, 6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.NORTHWEST;

        gbc.gridx = 0;
        gbc.gridy =
                panel.getComponentCount();

        gbc.weightx = 0;

        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        labelComponent.setForeground(
                TEXT
        );

        panel.add(
                labelComponent,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        if (component instanceof JTextArea) {
            gbc.fill =
                    GridBagConstraints.BOTH;
        }

        panel.add(
                component,
                gbc
        );
    }

    // ---------- PROJECT SPECS ----------

    private void onShowSpecs(
            ActionEvent e
    ) {

        int row =
                projectTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a project first.",
                    "No Project Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                projectTable.convertRowIndexToModel(
                        row
                );

        int projectId =
                (int) projectTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        Project p =
                projectDao.getProjectById(
                        projectId
                );

        if (p == null) {
            return;
        }

        JTextArea area =
                new JTextArea(
                        p.getSpecs()
                );

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(BODY_FONT);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Project Specifications",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ---------- PROJECT NOTES ----------

    private void onShowNotes(
            ActionEvent e
    ) {

        int row =
                projectTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a project first.",
                    "No Project Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                projectTable.convertRowIndexToModel(
                        row
                );

        int projectId =
                (int) projectTableModel
                        .getValueAt(
                                modelRow,
                                0
                        );

        Project p =
                projectDao.getProjectById(
                        projectId
                );

        if (p == null) {
            return;
        }

        JTextArea area =
                new JTextArea(
                        p.getNotes()
                );

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(BODY_FONT);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Project Notes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ---------- CSV HELPER ----------

    private String escapeCsv(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.replace(
                "\"",
                "\"\""
        );
    }

    // ---------- MAIN ----------

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new DgpMainFrame()
                        .setVisible(true)
        );
    }
}