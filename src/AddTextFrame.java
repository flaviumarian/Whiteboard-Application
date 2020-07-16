import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AddTextFrame extends JFrame {

    JPanel mainPanel;
    JLabel textInstruction, fontLabel, sizeLabel, fontStyleLabel;
    public static JTextArea text;
    JScrollPane scroll;
    String[] allFonts;
    JComboBox<String> fontOptions;
    JComboBox<String> styleOptions;
    JComboBox<Integer> sizeOptions;
    JButton addTextButton;

    public static Font currentFont = null;

    AddTextFrame(String title) {
        super(title);

        initComponents();

        setSize(new Dimension(400, 400));
        setVisible(true);
        pack();

    }//constructor


    private void changeFont() {

        styleOptions.getSelectedItem().toString();

        if(styleOptions.getSelectedIndex() == 0) {
            currentFont = new Font(fontOptions.getSelectedItem().toString(), Font.PLAIN, Integer.parseInt(sizeOptions.getSelectedItem().toString()));
        }

        if(styleOptions.getSelectedIndex() == 1) {
            currentFont = new Font(fontOptions.getSelectedItem().toString(), Font.BOLD, Integer.parseInt(sizeOptions.getSelectedItem().toString()));
        }
        if(styleOptions.getSelectedIndex() == 2) {
            currentFont = new Font(fontOptions.getSelectedItem().toString(), Font.ITALIC, Integer.parseInt(sizeOptions.getSelectedItem().toString()));
        }
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(currentFont);

    } //modificarea font-ului care va fi folosit in frame-ul principal



    private void initComponents() {

        mainPanel = new JPanel();
        fontLabel = new JLabel();
        fontOptions = new JComboBox<>();
        fontStyleLabel = new JLabel();
        styleOptions = new JComboBox<>();
        sizeLabel = new JLabel();
        sizeOptions = new JComboBox<>();
        scroll = new JScrollPane();
        text = new JTextArea();
        textInstruction = new JLabel();
        addTextButton = new JButton();


        fontLabel.setText("Font:");

        GraphicsEnvironment graphEnviron = GraphicsEnvironment.getLocalGraphicsEnvironment();
        allFonts = graphEnviron.getAvailableFontFamilyNames();

        fontOptions = new JComboBox<>(allFonts);
        fontOptions.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                if (value != null) {
                    String font = (String) value;
                    value = font;
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        fontOptions.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                changeFont();
            }
        });

        fontStyleLabel.setText("Font Style:");
        String [] styleOp = {"Normal", "Bold", "Italic"};
        styleOptions = new JComboBox<String>(styleOp);
        styleOptions.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                changeFont();
            }
        });


        sizeLabel.setText("Size:");
        Integer [] sizeValues = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72 };
        sizeOptions = new JComboBox<Integer>(sizeValues);
        sizeOptions.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                changeFont();
            }
        });


        text.setColumns(20);
        text.setRows(5);
        text.setFont(new Font(fontOptions.getSelectedItem().toString(), Font.PLAIN, Integer.parseInt(sizeOptions.getSelectedItem().toString())));
        scroll.setViewportView(text);

        textInstruction.setText("Write text:");

        addTextButton.setText("ADD TEXT");
        addTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaintApp.functionUsed = 3;
                dispose();
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(addTextButton)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(textInstruction)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(fontOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addGap(10, 10, 10)
                                                                        .addComponent(fontLabel)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(fontStyleLabel)
                                                                        .addGap(32, 32, 32)
                                                                        .addComponent(sizeLabel))
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(styleOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(sizeOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(155, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fontStyleLabel)
                                        .addComponent(sizeLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fontOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(styleOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sizeOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addComponent(textInstruction)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addTextButton)
                                .addContainerGap(70, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// creat cu NetBeans 8.2
}
