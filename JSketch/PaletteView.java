import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;


class PaletteView extends JPanel implements IView{

    private Model model;

    private JToggleButton selectTool;
    private JToggleButton eraseTool;
    private JToggleButton lineTool;
    private JToggleButton circleTool;
    private JToggleButton rectangleTool;
    private JToggleButton fillTool;
    private JToggleButton colorButton1;
    private JToggleButton colorButton2;
    private JToggleButton colorButton3;
    private JToggleButton colorButton4;
    private JToggleButton colorButton5;
    private JToggleButton colorButton6;
    private JList<Integer> thicknessList;

    class MyCellRenderer extends JLabel implements ListCellRenderer{
        public MyCellRenderer(){
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(Color.WHITE);
                setForeground(list.getForeground());
            }
            String [] thicknessIcons = {
                    "images/thickness_1.png",
                    "images/thickness_2.png",
                    "images/thickness_3.png",
                    "images/thickness_4.png",
                    "images/thickness_5.png"
            };

            setIcon(new ImageIcon(thicknessIcons[index]));
            return this;
        }
    }


    PaletteView(Model tmpmodel){
        this.model = tmpmodel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        this.setPreferredSize(new Dimension(180,100));

        //Tool bar for tools
        JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
        toolbar.setFloatable(false);
        this.add(toolbar);

        //Tool bar for colors
        JToolBar colorBar = new JToolBar(JToolBar.VERTICAL);
        colorBar.setFloatable(false);
        this.add(colorBar);

        //Tool bar for line thickness
        JToolBar lineBar = new JToolBar(JToolBar.VERTICAL);
        lineBar.setFloatable(false);
        lineBar.setPreferredSize(new Dimension(100, 180));
        lineBar.setMinimumSize(new Dimension(100,180));
        this.add(lineBar);


        //CREATING THE TOOLBAR WITH ICONS FOR SELECT, ERASE, LINE, CIRLCE, RECTANGLE AND FILL
        JPanel tools = new JPanel(new GridLayout(3,2,5,5));
        tools.setPreferredSize(new Dimension(100, 140));
        ButtonGroup toolButtons = new ButtonGroup();
        selectTool = createToolButton(new ImageIcon("images/select.png"), tool.SELECT, true);
        eraseTool = createToolButton(new ImageIcon("images/erase.png"), tool.ERASE, false);
        lineTool = createToolButton(new ImageIcon("images/line.png"), tool.LINE, false);
        circleTool = createToolButton(new ImageIcon("images/circle.png"), tool.CIRCLE, false);
        rectangleTool = createToolButton(new ImageIcon("images/rectangle.png"), tool.RECTANGLE, false);
        fillTool = createToolButton(new ImageIcon("images/fill.png"), tool.FILL, false);
        tools.add(selectTool);
        tools.add(eraseTool);
        tools.add(lineTool);
        tools.add(circleTool);
        tools.add(rectangleTool);
        tools.add(fillTool);
        toolButtons.add(selectTool);
        toolButtons.add(eraseTool);
        toolButtons.add(lineTool);
        toolButtons.add(circleTool);
        toolButtons.add(rectangleTool);
        toolButtons.add(fillTool);
        toolbar.add(tools);

        //CREATE A COLORS PANEL WITH 6 COLOR OPTIONS: BLUE, RED, ORANGE,YELLOW, GREEN AND PINK.
        JPanel colors = new JPanel(new GridLayout(3,2,5,5));
        colors.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        colors.setPreferredSize(new Dimension(100, 140));
        ButtonGroup colorButtons = new ButtonGroup();
        colorButton1 = createColorButton(Color.BLUE, true);
        colorButton2 = createColorButton(Color.RED, false);
        colorButton3 = createColorButton(Color.ORANGE, false);
        colorButton4 = createColorButton(Color.YELLOW, false);
        colorButton5 = createColorButton(Color.GREEN, false);
        colorButton6 = createColorButton(Color.PINK, false);
        colors.add(colorButton1);
        colors.add(colorButton2);
        colors.add(colorButton3);
        colors.add(colorButton4);
        colors.add(colorButton5);
        colors.add(colorButton6);
        colorButtons.add(colorButton1);
        colorButtons.add(colorButton2);
        colorButtons.add(colorButton3);
        colorButtons.add(colorButton4);
        colorButtons.add(colorButton5);
        colorButtons.add(colorButton6);
        colorBar.add(colors);

        //CREATE THE CHOOSER BUTTON AND ADD TO THE COLOR BAR.
        JButton chooserButton = new JButton("Chooser");
        chooserButton.setPreferredSize(new Dimension(100, 50));
        chooserButton.setMaximumSize(new Dimension(100, 50));
        chooserButton.setMinimumSize(new Dimension(100, 50));
        chooserButton.setAlignmentX(CENTER_ALIGNMENT);
        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setCurrentColor(JColorChooser.showDialog(null, "Pick a Color", Color.BLACK));
                if (model.getCurrentTool() == tool.SELECT){
                    model.setSelectedShapeColor(model.getCurrentColor());
                }
            }
        });
        colorBar.add(chooserButton);

        //CREATE THE THICKNESS LIST WITH 5 DIFFERENT THICKNESS OPTIONS
        Integer thickness [] = {0, 1, 2, 3, 4};
        thicknessList = new JList<Integer>(thickness);
        thicknessList.setBackground(Color.white);
        MyCellRenderer renderer = new MyCellRenderer();
        renderer.setPreferredSize(new Dimension(130, 30));
        thicknessList.setCellRenderer(renderer);
        thicknessList.setSelectedIndex(0);
        thicknessList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                int selectedIndex = list.getSelectedIndex();
                model.setCurrentThickness(selectedIndex);
                if (model.getCurrentTool() == tool.SELECT){
                    model.setSelectedShapeThickness(model.getCurrentThickness());
                }
            }
        });
        lineBar.add(thicknessList);
    }

    private  JToggleButton createToolButton(Icon icon, tool toolName, boolean selected){
        JToggleButton button = new JToggleButton(icon, selected);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.clearSelectedShape();
                model.setCurrentTool(toolName);
            }
        });
        return button;
    }

    private JToggleButton createColorButton(Color color, boolean selected){
        JToggleButton button = new JToggleButton("", selected);
        button.setBackground(color);

        if (color == Color.PINK){
            button.setIcon(new ImageIcon("images/pink.png"));
            button.setSelectedIcon(new ImageIcon("images/pink_selected.png"));
        }else if (color == Color.ORANGE){
            button.setIcon(new ImageIcon("images/orange.png"));
            button.setSelectedIcon(new ImageIcon("images/orange_selected.png"));
        }else if (color == Color.RED){
            button.setIcon(new ImageIcon("images/red.png"));
            button.setSelectedIcon(new ImageIcon("images/red_selected.png"));
        }else if (color == Color.YELLOW){
            button.setIcon(new ImageIcon("images/yellow.png"));
            button.setSelectedIcon(new ImageIcon("images/yellow_selected.png"));
        }else if (color == Color.GREEN){
            button.setIcon(new ImageIcon("images/green.png"));
            button.setSelectedIcon(new ImageIcon("images/green_selected.png"));
        }else if (color == Color.BLUE){
            button.setIcon(new ImageIcon("images/blue.png"));
            button.setSelectedIcon(new ImageIcon("images/blue_selected.png"));
        }

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setCurrentColor(color);
                if (model.getCurrentTool() == tool.SELECT){
                    model.setSelectedShapeColor(model.getCurrentColor());
                }
            }
        });
        return button;
    }

    public void updateView() {
        if (model.getSelectedShape() != null) {
            thicknessList.setSelectedIndex(model.getCurrentThickness());
            if (model.getCurrentColor().getRGB() == Color.BLUE.getRGB()){
                colorButton1.setSelected(true);
            }
            else if (model.getCurrentColor().getRGB() == Color.RED.getRGB()){
                colorButton2.setSelected(true);
            }
            else if (model.getCurrentColor().getRGB() == Color.ORANGE.getRGB()) {
                colorButton3.setSelected(true);
            }
            else if (model.getCurrentColor().getRGB() == Color.YELLOW.getRGB()) {
                colorButton4.setSelected(true);
            }
            else if (model.getCurrentColor().getRGB() == Color.BLUE.getRGB()) {
                colorButton5.setSelected(true);
            }
            else if (model.getCurrentColor().getRGB() == Color.PINK.getRGB()) {
                colorButton6.setSelected(true);
            }
        }
    }
}