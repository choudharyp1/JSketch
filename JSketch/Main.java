import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args) {
        //New model
        Model model = new Model();

        //New palette view
        PaletteView palette = new PaletteView(model);
        model.addView(palette);

        //New Canvas view
        CanvasView canvasView = new CanvasView(model);
        model.addView(canvasView);

        //This is the main frame of the program.
        JFrame frame = new JFrame("JSketch");

        //Adding Menu Bar with the File and View options.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File"); //File menu
        JMenu viewMenu = new JMenu("View"); //View menu
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        //Add a New item in File Menu.
        JMenuItem newDrawing = new JMenuItem("New");
        newDrawing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setNewDrawing(); //set new drawing.
            }
        });
        fileMenu.add(newDrawing);

        //File chooser.
        JFileChooser chooser = new JFileChooser();

        //Add a Load item in File Menu
        JMenuItem loadDrawing = new JMenuItem("Load");
        loadDrawing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JSketch Drawings (.jsk)", "jsk");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(frame); //opens the chooser window
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();
                    String fileName = file.getName();
                    if(!fileName.endsWith(".jsk")){
                        JOptionPane.showMessageDialog(null, "Not a JSketch Drawing file, please choose a .jsk file");
                    }else {
                        try {
                            FileInputStream fileStream = new FileInputStream(file);
                            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                            @SuppressWarnings("unchecked")	// Suppresses the unchecked cast in the following lin
                            ArrayList<MyShape> result = (ArrayList<MyShape>) objectStream.readObject();
                            model.loadSavedDrawing(result);
                            objectStream.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        fileMenu.add(loadDrawing);

        //Add a Save item in the File Menu
        JMenuItem saveDrawing = new JMenuItem("Save");
        saveDrawing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    if(!filePath.endsWith(".jsk")) {
                        file = new File(filePath + ".jsk");
                    }
                    try {
                        FileOutputStream fileStream = new FileOutputStream(file);
                        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                        objectStream.writeObject(model.getListOfShapes());
                        objectStream.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(saveDrawing);

        //Adding menu items to the View Menu.
        //First create a button group.
        ButtonGroup group = new ButtonGroup();

        //Create a fullSizeImage menu item and add to View menu.
        JRadioButtonMenuItem fullSizeImage = new JRadioButtonMenuItem("Full Size");
        fullSizeImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Set canvas layout to full size.
                CardLayout cl = (CardLayout)(canvasView.getLayout());
                cl.show(canvasView, "Full Size");
            }
        });
        fullSizeImage.setSelected(true);
        group.add(fullSizeImage);
        viewMenu.add(fullSizeImage);

        //Create a fit to window menu item and add to View menu.
        JRadioButtonMenuItem fit_to_windowImage = new JRadioButtonMenuItem("Fit to Window");
        fit_to_windowImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Set canvas layout to fit to window.
                CardLayout cl = (CardLayout)(canvasView.getLayout());
                cl.show(canvasView, "Fit to Window");
            }
        });
        group.add(fit_to_windowImage);
        viewMenu.add(fit_to_windowImage);

        //Add the menu bar to the frame.
        frame.setJMenuBar(menuBar);


        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //Add the palette and canvas Views to the contentPane.
        contentPane.add(palette);
        contentPane.add(canvasView);

        frame.setMinimumSize(new Dimension(750, 650));
        frame.setPreferredSize(new Dimension(1000, 650));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setVisible(true);


        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getID() == KeyEvent.KEY_PRESSED) {
                            model.clearSelectedShape();
                        }
                        return false;
                    }
                });
    }
}