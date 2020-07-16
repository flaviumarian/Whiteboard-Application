import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MenuItemActionListener implements ActionListener {
    Component parent;

    private JFileChooser fileChooserSave, fileChooserOpen;
    private BufferedImage BI, newBI;
    private int [][]pixels;

    public MenuItemActionListener(Component parent) {
        this.parent = parent;


        fileChooserSave = new JFileChooser();
        fileChooserSave.setCurrentDirectory(new File("C:\\Users\\Public\\Desktop"));
        fileChooserSave.setFileFilter(new FileNameExtensionFilter("PNG", "images", "png"));

        fileChooserOpen = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("C:\\Users\\Public\\Desktop"));
        fileChooserOpen.setFileFilter(new FileNameExtensionFilter("PNG", "images", "png"));
        fileChooserOpen.setFileFilter(new FileNameExtensionFilter("JPG", "images", "jpg"));
    }


    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        @SuppressWarnings("unused")
        String cmd = item.getActionCommand();

        if(e.getActionCommand().equals("save")) {
            imageToArray();
            saveImage();
        }
        if(e.getActionCommand().equals("open")) {
            openImage();
        }
        if(e.getActionCommand().equals("new")) {
            newCanvas();

        }
    }

    private void imageToArray() {

        BI =   PaintApp.drawingBoard.exportImage();

        int width = BI.getWidth();
        int height = BI.getHeight();

        //newBI = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        pixels = new int[width][height];

        for(int i=0; i<width ; i++) {
            for(int j=0;j<height; j++) {
                pixels[i][j] = BI.getRGB(i, j);
            }
        } //all the pixels in the image
    }

    private void saveImage() {
        int returnValue = fileChooserSave.showSaveDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {

                BufferedImage backgroundImage = new BufferedImage(BI.getWidth(), BI.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = backgroundImage.createGraphics();
                g.setColor(DrawingBoard.backgroundColor);
                g.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
                g.drawImage(BI, 0, 0, null);

                ImageIO.write(backgroundImage, "png", fileChooserSave.getSelectedFile());
                JOptionPane.showMessageDialog(null, "File has been saved","File Saved",JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e) {
                JOptionPane.showMessageDialog(null, "File has NOT been saved","File not saved",JOptionPane.INFORMATION_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null, "Not saved","ERROR",JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void openImage() {
        int returnValue = fileChooserOpen.showOpenDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                newBI = ImageIO.read(fileChooserOpen.getSelectedFile());
                PaintApp.drawingBoard.importImage(newBI);
            }catch(IOException e) {
                JOptionPane.showMessageDialog(null, "File can not be opened","File not opened",JOptionPane.INFORMATION_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null, "No file selected","ERROR",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private  void newCanvas() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to save before deleting?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {
            imageToArray();
            saveImage();
            PaintApp.drawingBoard.newImage();
            PaintApp.drawingBoard.setBackground(Color.white);
        }
        else {
            PaintApp.drawingBoard.newImage();
            PaintApp.drawingBoard.setBackground(Color.white);
        }
    }
}
