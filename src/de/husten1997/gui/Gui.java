package de.husten1997.gui;

import de.husten1997.changesettings.SettingTree;
import de.husten1997.copyinstance.CopyInstance;
import de.husten1997.main.ApplicationConfig;
import de.husten1997.main.Main;

import static de.husten1997.main.Log.setupLogger;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;

public class Gui {
    private int windowHeight, windowWidth;
    private Dimension smallDim, bigDim;

    private JFrame mainWindow;
    private JPanel oldGtnhFolder;
    private JTextField oldGtnhFolderInput;

    private JPanel newGtnhFolder;
    private JTextField newGtnhFolderInput;

    private JPanel changeSettings;

    private JPanel finalDialog;

    private static final Logger LOGGER = setupLogger( Gui.class.getName() );

    public Gui(int windowWidth, int windowHeight) {
        LOGGER.log(Level.FINE, "Setup window with dimensions W:{0}, H:{1}", new Object[]{windowWidth, windowHeight});
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.bigDim = new Dimension((int) (windowWidth * 0.95), (int) ((windowHeight - 100 )* 0.3));
        this.smallDim = new Dimension((int) (windowWidth * 0.95), 100);

        this.prepare_gui(Main.configHandler.getApplicationConfig().getOldGtnhPath(), Main.configHandler.getApplicationConfig().getNewGtnhPath());
    }



    private void prepare_gui(String oldGtnhFolderInputPath, String newGtnhFolderInputPath){
        LOGGER.log(Level.FINEST, "Preparing GUI");

        // Setup MainWindow
        mainWindow = new JFrame();
        mainWindow.setSize(this.windowWidth, this.windowHeight);
        mainWindow.setLayout(new GridLayout(4, 1));


        // Setup old GTNH Folder
        oldGtnhFolderInput = new JTextField();
        oldGtnhFolderInput.setText(oldGtnhFolderInputPath);
        oldGtnhFolder = this.build_input_panel("Old GTNH Installation", oldGtnhFolderInput);
        oldGtnhFolder.setPreferredSize(this.bigDim);


        // Setup new GTNH Folder
        newGtnhFolderInput = new JTextField();
        newGtnhFolderInput.setText(newGtnhFolderInputPath);
        newGtnhFolder = this.build_input_panel("New GTNH Installation", newGtnhFolderInput);
        newGtnhFolder.setPreferredSize(this.bigDim);



        // Setup additional Settings Panel
        changeSettings = new JPanel();
        changeSettings.setPreferredSize(this.bigDim);
        changeSettings.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Additional Settings to Change"),
                    BorderFactory.createEmptyBorder(5,5,5,5)
            )
        );

        GridBagLayout layout = new GridBagLayout();
        changeSettings.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.ipadx = (int) (parentPanel.getWidth() * 0.6);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;

        SettingTree tree = new SettingTree();
        JTree treeComponent = new JTree(tree.getSettingFiles());
        JScrollPane treeView = new JScrollPane(treeComponent);
//        treeComponent.setMinimumSize(new Dimension(500, 300));
//        treeComponent.setPreferredSize(new Dimension(500, 300));
//        treeView.setMinimumSize(new Dimension(500, 300));
//        treeView.setPreferredSize(new Dimension(500, 300));
        changeSettings.add(treeView, gbc);



        // Setup final dialog
        finalDialog = new JPanel();
        finalDialog.setPreferredSize(this.smallDim);

        JButton ButtonApprove = new JButton("Approve");
        JButton ButtonCancel = new JButton("Cancel");

        ButtonApprove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.log(Level.FINE, "Start Copying");
                String source = oldGtnhFolderInput.getText();
                String target = newGtnhFolderInput.getText();
                if ((source != null) && (target != null)) {

                    CopyInstance copyInstanceHandler = new CopyInstance(source, target);
                    copyInstanceHandler.execute();

                } else {
                    LOGGER.log(Level.FINE, "Did nothing");
                }

            }
        });

        ButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                System.exit(0);
            }
        });

        finalDialog.add(ButtonApprove);
        finalDialog.add(ButtonCancel);



        // Main Window Event Listener
        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                Main.configHandler.getApplicationConfig().setOldGtnhPath(oldGtnhFolderInput.getText());
                Main.configHandler.getApplicationConfig().setNewGtnhPath(newGtnhFolderInput.getText());
                Main.configHandler.writeConfigFile("GTNH_Updater.cfg");
                System.exit(0);
            }
        });

        // Add components to Main Window
        mainWindow.add(oldGtnhFolder);
        mainWindow.add(newGtnhFolder);
        mainWindow.add(changeSettings);
        mainWindow.add(finalDialog);
        mainWindow.setVisible(true);
    }

    private JPanel build_input_panel(String title, JTextField folderInput) {
        JPanel parentPanel = new JPanel();
        parentPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(title),
                        BorderFactory.createEmptyBorder(5,5,5,5)
                )
        );
        // parentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        // JPanel childPanel = new JPanel();
        //panel.setSize((int) (this.windowWidth * 0.9), 100);
        GridBagLayout layout = new GridBagLayout();
        parentPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        //gbc.ipadx = (int) (parentPanel.getWidth() * 0.2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(new JLabel("Folder"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        //gbc.ipadx = (int) (parentPanel.getWidth() * 0.6);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(folderInput, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton browse_button = new JButton("Browse");
        browse_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Choos the directory");
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    folderInput.setText(fileChooser.getSelectedFile().toString());
                    // File selectedFile = fileChooser.getSelectedFile();
                    //java.awt.Desktop.getDesktop().open(selectedFile);//<-- here
                }
            }
        });
        parentPanel.add(browse_button, gbc);

        //parentPanel.add(childPanel);

        return parentPanel;
    }

    public void show(){
        LOGGER.log(Level.FINEST, "Show GUI");
    }

}
