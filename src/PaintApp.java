import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class PaintApp extends JFrame {

    JPanel mainPanel, optionsPanel, imagePanel, toolsPanel, penOptionsPanel, shapePanel, colorPanel;
    JLabel basicLabel, clickToAddPictureLabel, labelImage1, labelImage2, labelImage3, toolsLabel,
            labelStroke, labelPen, labelFill;
    public static JLabel mouseInfo;
    GridBagConstraints gbc = new GridBagConstraints();

    static JColorChooser jColorChooser1;

    Font font = new Font("open sans", Font.PLAIN, 24);

    public static DrawingBoard drawingBoard;

    JButton openFileButton, setSizeButton, greyscaleButton, resetToNormalButton, negativeButton, cropButton, thresholdButton, toolButtons[],
            shapeButtons[];
    ButtonGroup radioButtonGroup, radioButtonGroup1;
    JRadioButton continous, nonContinuous, dotted, fill, notFill;

    BufferedImage buttonIcon = null;
    JFileChooser openFileChooser;

    public static JTextField pixelsImage1, pixelsImage2;
    public static BufferedImage imageAdded = null, originalImage = null;

    JComboBox<ImageIcon> stroke;

    static int functionUsed = 1; // de aici aleg pentru DrawingBoard componenta: 0/image; 1/pen; 2/fill; 3/text;
    // 4/erase; 5/crop 7/line; 8/rectangle; 9/circle; 10/triangle
    static int currentStroke = 1;

    static int strokePattern = 1; // modelul liniei: 1/normala; 2/linie rarefiata; 3/linie punctata

    static int fillStatus = 0; // 0 not fill; 1 fill

    PaintApp(String title) {
        super(title);

        initComponents();
        createWindowOptions(); // optiunile pentru File - Edit
        addImageButton();
        addPenOptionsButtons();

        jColorChooser1.setColor(Color.black); // incepe creionul cu culoarea negru, ca in paint-ul adevarat

        this.add(mainPanel);
        pack();
        setVisible(true);
    }

    private void initComponents() {

        // panelul mare
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // panelul cu optiunile
        optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createTitledBorder(""));
        optionsPanel.setLayout(new GridBagLayout());

        // panelul cu canvas
        imagePanel = new JPanel();
        imagePanel.setLayout(new WrapLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder(null, "Image", TitledBorder.CENTER, TitledBorder.BOTTOM,
                font, Color.black));

        addToolButtons();

        // panel cu optiuni PEN
        penOptionsPanel = new JPanel();
        penOptionsPanel.setBorder(BorderFactory.createTitledBorder(null, "Pen and Shape Options", TitledBorder.CENTER,
                TitledBorder.BOTTOM, font, Color.black));

        addShapeButtons();

        // panel cu culori
        colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        colorPanel.setBorder(BorderFactory.createTitledBorder(null, "Color", TitledBorder.CENTER, TitledBorder.BOTTOM,
                font, Color.black));
        jColorChooser1 = new JColorChooser();
        colorPanel.add(jColorChooser1);

        // adaugare panel-uri unul in altu si apoi in cel mare
        optionsPanel.add(imagePanel, createGbc(0, 0));
        optionsPanel.add(toolsPanel, createGbc(1, 0));
        optionsPanel.add(penOptionsPanel, createGbc(2, 0));
        optionsPanel.add(shapePanel, createGbc(3, 0));
        optionsPanel.add(colorPanel, createGbc(4, 0));

        drawingBoard = new DrawingBoard(); // canvas ul
        mainPanel.add(optionsPanel);
        mainPanel.add(drawingBoard);

        pixelsImage1 = new JTextField("100", 5);
        pixelsImage2 = new JTextField("100", 5);

        mouseInfo = new JLabel("default");
        mainPanel.add(mouseInfo, BorderLayout.SOUTH);// label in partea de jos stanga a ecranului unde va aparea
        // miscarea mouse-ului
    }

    private void addImageButton() {

        openFileButton = new JButton("Add image");
        imagePanel.add(openFileButton);
        setSizeButton = new JButton("Set size");
        greyscaleButton = new JButton("Greyscale image");
        resetToNormalButton = new JButton("Original Photo");
        negativeButton = new JButton("Negative image");

        labelImage1 = new JLabel("");
        labelImage2 = new JLabel("");
        labelImage3 = new JLabel("");
        clickToAddPictureLabel = new JLabel("");

        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int returnValue = openFileChooser.showOpenDialog(openFileButton);

                if (returnValue == JFileChooser.APPROVE_OPTION) {

                    File selectedFile = openFileChooser.getSelectedFile();

                    try {
                        imageAdded = ImageIO.read(selectedFile);
                        originalImage = deepCopy(imageAdded);

                        labelImage1.setText("Select size for the image: ");
                        imagePanel.add(labelImage1);
                        labelImage2.setText("horizontal: ");
                        imagePanel.add(labelImage2);
                        imagePanel.add(pixelsImage1);
                        labelImage3.setText("vertical: ");
                        imagePanel.add(labelImage3);
                        imagePanel.add(pixelsImage2);

                        imagePanel.add(setSizeButton);
                        imagePanel.add(resetToNormalButton);
                        imagePanel.add(greyscaleButton);
                        imagePanel.add(negativeButton);

                        clickToAddPictureLabel.setText("");
                        imagePanel.add(clickToAddPictureLabel);

                        setSizeButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                clickToAddPictureLabel.setText("Click where you want to place the image");
                                imagePanel.add(clickToAddPictureLabel);
                                imagePanel.revalidate();

                                flipRadioButtons(false);
                                functionUsed = 0;
                            }
                        });
                        resetToNormalButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clickToAddPictureLabel.setText("Click where you want to place the image");
                                imagePanel.add(clickToAddPictureLabel);
                                imagePanel.revalidate();

                                imageAdded = deepCopy(originalImage);
                                flipRadioButtons(false);
                                functionUsed = 0;
                            }
                        });
                        greyscaleButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                clickToAddPictureLabel.setText("Click where you want to place the image");
                                imagePanel.add(clickToAddPictureLabel);
                                imagePanel.revalidate();

                                imageAdded = deepCopy(originalImage);
                                imageAdded = convertToGrayscale(imageAdded);
                                flipRadioButtons(false);
                                functionUsed = 0;
                            }
                        });
                        negativeButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clickToAddPictureLabel.setText("Click where you want to place the image");
                                imagePanel.add(clickToAddPictureLabel);
                                imagePanel.revalidate();

                                imageAdded = deepCopy(originalImage);
                                imageAdded = convertToNegative(imageAdded);
                                flipRadioButtons(false);
                                functionUsed = 0;
                            }
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file chosen!","ERROR",JOptionPane.INFORMATION_MESSAGE);
                }

                imagePanel.revalidate();
            }
        });

        openFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(
                new File("D:\\1. Facultate\\III\\Fist semester\\Software Engineering\\1.TEME TXT\\Lab 9"));
        openFileChooser.setFileFilter(new FileNameExtensionFilter("PNG images", "png"));
        openFileChooser.setFileFilter(new FileNameExtensionFilter("JPG images", "jpg"));

    }// panel-ul cu imagini

    private void addToolButtons() {

        toolButtons = new JButton[4];

        // panel cu tools
        toolsPanel = new JPanel();
        toolsPanel.setBorder(BorderFactory.createTitledBorder(null, "Tools", TitledBorder.CENTER, TitledBorder.BOTTOM,
                font, Color.black));
        toolsPanel.setLayout(new GridLayout(4, 1));

        toolButtons[0] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("pencil.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea pencil.png
        toolButtons[0].setBorder(BorderFactory.createEmptyBorder());
        toolButtons[0].setContentAreaFilled(false);
        toolButtons[0] = new JButton(new ImageIcon(buttonIcon));
        toolButtons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                flipRadioButtonsFill(false);
                functionUsed = 1;
            }
        });

        toolButtons[1] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("bucket.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea bucket.png

        toolButtons[1].setBorder(BorderFactory.createEmptyBorder());
        toolButtons[1].setContentAreaFilled(false);
        toolButtons[1] = new JButton(new ImageIcon(buttonIcon));
        toolButtons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(false);
                flipRadioButtonsFill(false);
                functionUsed = 2;
            }
        });

        toolButtons[2] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("enterText.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea enterText.png

        toolButtons[2].setBorder(BorderFactory.createEmptyBorder());
        toolButtons[2].setContentAreaFilled(false);
        toolButtons[2] = new JButton(new ImageIcon(buttonIcon));
        toolButtons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unused")
                AddTextFrame textFrame = new AddTextFrame("Create text");
                flipRadioButtonsFill(false);
                flipRadioButtons(false);
            }
        });

        toolButtons[3] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("eraser.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea eraser.png

        toolButtons[3].setBorder(BorderFactory.createEmptyBorder());
        toolButtons[3].setContentAreaFilled(false);
        toolButtons[3] = new JButton(new ImageIcon(buttonIcon));
        toolButtons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(false);
                flipRadioButtonsFill(false);
                strokePattern = 1;
                functionUsed = 4;
            }
        });


        toolsPanel.add(toolButtons[0]);
        toolsPanel.add(toolButtons[1]);
        toolsPanel.add(toolButtons[2]);
        toolsPanel.add(toolButtons[3]);
    }// toolsPanel

    private void addPenOptionsButtons() {

        radioButtonGroup = new ButtonGroup();
        radioButtonGroup1 = new ButtonGroup();
        labelStroke = new JLabel();
        labelPen = new JLabel();
        labelFill = new JLabel();


        continous = new JRadioButton();
        nonContinuous = new JRadioButton();
        dotted = new JRadioButton();
        fill = new JRadioButton();
        notFill = new JRadioButton();

        Image[] comboImages = new Image[4];
        ImageIcon[] icons = new ImageIcon[4];
        int count = 0;
        try {
            comboImages[count] = ImageIO.read(new File("smallStroke.png"));
            count++;
            comboImages[count] = ImageIO.read(new File("mediumStroke.png"));
            count++;
            comboImages[count] = ImageIO.read(new File("mediumLargeStroke.png"));
            count++;
            comboImages[count] = ImageIO.read(new File("largeStroke.png"));
            count++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {
            icons[i] = new ImageIcon(comboImages[i]);
        }
        stroke = new JComboBox<ImageIcon>(icons);
        stroke.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (stroke.getSelectedIndex() == 0)
                    currentStroke = 1;
                if (stroke.getSelectedIndex() == 1)
                    currentStroke = 2;
                if (stroke.getSelectedIndex() == 2)
                    currentStroke = 3;
                if (stroke.getSelectedIndex() == 3)
                    currentStroke = 4;
            }
        });

        labelStroke.setText("Stroke:");
        labelPen.setText("Line model:");
        labelFill.setText("Fill:");

        radioButtonGroup.add(continous);
        continous.setText("Continuous");
        continous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fill.getModel().setEnabled(true);
                strokePattern = 1;
            }
        });

        radioButtonGroup.add(nonContinuous);
        nonContinuous.setText("Non Continuous");
        nonContinuous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fill.getModel().setEnabled(false);
                strokePattern = 2;
            }

        });

        radioButtonGroup.add(dotted);
        dotted.setText("Dotted");
        dotted.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fill.getModel().setEnabled(false);
                strokePattern = 3;
            }
        });

        radioButtonGroup1.add(fill);
        fill.setText("Fill");
        fill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(false);
                fillStatus = 1;
            }
        });

        radioButtonGroup1.add(notFill);
        notFill.setText("Not filled");
        notFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                fillStatus = 0;
            }
        });


        cropButton = new JButton("Crop");
        cropButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                functionUsed = 5;
            }
        });

        thresholdButton = new JButton("Threshold Filter");
        thresholdButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unused")
                FilterThresholdFrame thresholdFrame= new FilterThresholdFrame("Threshold filter");
            }
        });


        continous.setSelected(true);
        notFill.setSelected(true);
        flipRadioButtonsFill(false);

        GroupLayout layout = new javax.swing.GroupLayout(penOptionsPanel);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(labelPen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(continous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGap(144, 144, 144)
                                                                .addComponent(labelFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addGap(26, 26, 26)
                                                                .addComponent(labelStroke, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(thresholdButton, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                .addComponent(cropButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(stroke, 0, 147, Short.MAX_VALUE)))
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addGap(18, 18, 18))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(nonContinuous, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(dotted, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(183, 183, 183)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(notFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(38, 38, 38)))
                                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelStroke)
                                        .addComponent(stroke, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cropButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(thresholdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPen)
                                        .addComponent(continous)
                                        .addComponent(labelFill)
                                        .addComponent(notFill))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nonContinuous)
                                        .addComponent(fill))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dotted)
                                .addGap(19, 19, 19))
        );

        penOptionsPanel.setLayout(layout);

    }

    private void flipRadioButtons(boolean option) {
        if (option == true) {
            nonContinuous.getModel().setEnabled(true);
            dotted.getModel().setEnabled(true);
        } else {
            continous.setSelected(true);
            nonContinuous.getModel().setEnabled(false);
            dotted.getModel().setEnabled(false);
        }
    }

    private void flipRadioButtonsFill(boolean option) {
        if (option == true) {
            fill.getModel().setEnabled(true);
            notFill.getModel().setEnabled(true);
        } else {
            fill.getModel().setEnabled(false);
            notFill.getModel().setEnabled(false);
        }
    }

    private void addShapeButtons() {
        shapeButtons = new JButton[4];

        // panel cu forme
        shapePanel = new JPanel();
        shapePanel.setBorder(BorderFactory.createTitledBorder(null, "Shapes", TitledBorder.CENTER, TitledBorder.BOTTOM,
                font, Color.black));
        shapePanel.setLayout(new GridLayout(4, 1));

        shapeButtons[0] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("line.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea line.png
        shapeButtons[0].setBorder(BorderFactory.createEmptyBorder());
        shapeButtons[0].setContentAreaFilled(false);
        shapeButtons[0] = new JButton(new ImageIcon(buttonIcon));
        shapeButtons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                flipRadioButtonsFill(false);
                functionUsed = 7;
            }
        });

        shapeButtons[1] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("rectangle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea rectangle.png
        shapeButtons[1].setBorder(BorderFactory.createEmptyBorder());
        shapeButtons[1].setContentAreaFilled(false);
        shapeButtons[1] = new JButton(new ImageIcon(buttonIcon));
        shapeButtons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                flipRadioButtonsFill(true);
                continous.setSelected(true);
                strokePattern = 1;
                functionUsed = 8;
            }
        });

        shapeButtons[2] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("circle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea circle.png
        shapeButtons[2].setBorder(BorderFactory.createEmptyBorder());
        shapeButtons[2].setContentAreaFilled(false);
        shapeButtons[2] = new JButton(new ImageIcon(buttonIcon));
        shapeButtons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                flipRadioButtonsFill(true);
                continous.setSelected(true);
                strokePattern = 1;
                functionUsed = 9;
            }
        });

        shapeButtons[3] = new JButton("");
        try {
            buttonIcon = ImageIO.read(new File("triangle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } // se ia imaginea triangle.png
        shapeButtons[3].setBorder(BorderFactory.createEmptyBorder());
        shapeButtons[3].setContentAreaFilled(false);
        shapeButtons[3] = new JButton(new ImageIcon(buttonIcon));
        shapeButtons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flipRadioButtons(true);
                flipRadioButtonsFill(true);
                continous.setSelected(true);
                strokePattern = 1;
                functionUsed = 10;
            }
        });

        shapePanel.add(shapeButtons[0]);
        shapePanel.add(shapeButtons[1]);
        shapePanel.add(shapeButtons[2]);
        shapePanel.add(shapeButtons[3]);
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }//singura modalitate de a copia bufferedImage in alta fara a se modifica ambele in moduri incontrolabile

    private BufferedImage convertToGrayscale(BufferedImage img) {

        BufferedImage toBeReturned = img;

        // get image width and height
        int width = toBeReturned.getWidth();
        int height = toBeReturned.getHeight();

        // convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = toBeReturned.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;

                // replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                toBeReturned.setRGB(x, y, p);
            }
        }

        return toBeReturned;
    }// greyscale image

    private BufferedImage convertToNegative(BufferedImage img) {
        BufferedImage toBeReturned = img;

        // get image width and height
        int width = toBeReturned.getWidth();
        int height = toBeReturned.getHeight();
        // convert to negative
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = toBeReturned.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                // subtract RGB from 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                // set new RGB value
                p = (a << 24) | (r << 16) | (g << 8) | b;
                toBeReturned.setRGB(x, y, p);
            }
        }
        // write image

        return toBeReturned;
    }// negative image

    private GridBagConstraints createGbc(int x, int y) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = x;
        gbc.gridy = y;

        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(0, 0, 0, 2);
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }// grid bag constraints pentru crearea tabloului din partea de sus

    public void createWindowOptions() {
        ActionListener listener = new MenuItemActionListener(mainPanel);

        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.add(menuItem("New", listener, "new", 'N', KeyEvent.VK_N));
        file.add(menuItem("Open...", listener, "open", 'O', KeyEvent.VK_O));
        file.add(menuItem("Save", listener, "save", 'S', KeyEvent.VK_S));
        // file.add(menuItem("Save As...", listener, "saveas", 'A', KeyEvent.VK_A));

//		JMenu edit = new JMenu("Edit");
//		edit.setMnemonic('E');
//		edit.add(menuItem("Cut", listener, "cut", 0, KeyEvent.VK_X));
//		edit.add(menuItem("Copy", listener, "copy", 'C', KeyEvent.VK_C));
//		edit.add(menuItem("Paste", listener, "paste", 0, KeyEvent.VK_V));
//		edit.add(menuItem("Undo", listener, "undo", 0, KeyEvent.VK_Z));
//		edit.add(menuItem("Redo", listener, "redo", 0, KeyEvent.VK_Y));

        // Create a menubar and add these panes to it.
        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        //menubar.add(edit);

        // Add menubar to the main window. Note special method to add menubars
        this.setJMenuBar(menubar);

    }// creare meniuri bara de optiuni

    public static JMenuItem menuItem(String label, ActionListener listener, String command, int mnemonic,
                                     int acceleratorKey) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(listener);
        item.setActionCommand(command);
        if (mnemonic != 0)
            item.setMnemonic((char) mnemonic);
        if (acceleratorKey != 0)
            item.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey, java.awt.Event.CTRL_MASK));
        return item;
    } // pentru comenzile de pe meniu - algoritm de pe net

    public static BufferedImage getScaledImage(Image Img, int wt, int ht) {
        BufferedImage resizedImg = new BufferedImage(wt, ht, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(Img, 0, 0, wt, ht, null);
        g2.dispose();

        return resizedImg;
    } // modificarea dimensiunii imaginii - algoritm de pe net
}
