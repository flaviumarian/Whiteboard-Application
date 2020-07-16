import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class FilterThresholdFrame extends JFrame{

    JPanel mainPanel;
    JSlider redSlider, blueSlider, greenSlider;
    JLabel redLabel, blueLabel, greenLabel, redLabelValue, blueLabelValue, greenLabelValue;
    JButton generateButton;
    BufferedImage paintedImage;

    FilterThresholdFrame(String title){
        super(title);

        initComponents();

        setSize(new Dimension(400, 400));
        setVisible(true);
        pack();
    }

    public void initComponents(){

        redSlider = new JSlider();
        blueSlider = new JSlider();
        greenSlider = new JSlider();

        redLabel = new JLabel();
        blueLabel = new JLabel();
        greenLabel = new JLabel();
        redLabelValue = new JLabel();
        blueLabelValue = new JLabel();
        greenLabelValue = new JLabel();




        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        redLabel.setText("Red:");

        blueLabel.setText("Blue:");

        redSlider.setMajorTickSpacing(1);
        redSlider.setMaximum(255);
        redSlider.setPaintTicks(true);
        redSlider.setPaintTrack(false);
        redSlider.setToolTipText("");
        redSlider.setValue(0);
        redSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                redLabelValue.setText(String.valueOf(redSlider.getValue()));
            }
        });

        blueSlider.setMajorTickSpacing(1);
        blueSlider.setMaximum(255);
        blueSlider.setPaintTicks(true);
        blueSlider.setPaintTrack(false);
        blueSlider.setValue(0);
        blueSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                blueLabelValue.setText(String.valueOf(blueSlider.getValue()));
            }
        });

        greenLabel.setText("Green:");

        greenSlider.setMajorTickSpacing(1);
        greenSlider.setMaximum(255);
        greenSlider.setPaintTicks(true);
        greenSlider.setPaintTrack(false);
        greenSlider.setValue(0);
        greenSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                greenLabelValue.setText(String.valueOf(greenSlider.getValue()));
            }
        });

        redLabelValue.setText("0");
        redLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        blueLabelValue.setText("0");
        blueLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        greenLabelValue.setText("0");
        greenLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        generateButton = new JButton("FILTER");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paintedImage = PaintApp.drawingBoard.exportImage();
                BufferedImage imageToBeFiltered = new BufferedImage(paintedImage.getWidth(), paintedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                Graphics2D graph = imageToBeFiltered.createGraphics();
                graph.setColor(DrawingBoard.backgroundColor);
                graph.fillRect(0, 0, imageToBeFiltered.getWidth(), imageToBeFiltered.getHeight());
                graph.drawImage(paintedImage, 0, 0, null);//aici am imaginea efectiva a plansei de desenare

                PaintApp.drawingBoard.filterImageBasedOnThreshold(imageToBeFiltered, redSlider.getValue(), blueSlider.getValue(), greenSlider.getValue());//filtrare pe baza pragurilor

            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(blueLabel)
                                                        .addComponent(greenLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(blueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                                        .addComponent(greenSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(blueLabelValue)
                                                        .addComponent(greenLabelValue))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(redLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(redSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(redLabelValue)
                                                .addGap(0, 65, Short.MAX_VALUE))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(redLabelValue)
                                        .addComponent(redLabel)
                                        .addComponent(redSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(blueLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(blueSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(blueLabelValue, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(greenSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(greenLabel)
                                        .addComponent(greenLabelValue))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(41, Short.MAX_VALUE))
        );

    }

}
