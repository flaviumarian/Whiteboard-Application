import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class DrawingBoard extends Canvas implements MouseListener, MouseMotionListener {

    BufferedImage image, previousImage;
    Graphics2D g2d;

    static int lastImgHorizontal = 0, lastImgVertical = 0;
    int lastx, lasty, newX, newY, endX, endY;
    Point startDrag = null, endDrag = null;

    Stroke thickStroke, cleanStroke; // prima e pentru linie modificata in orice fel, cealalta pastreaza o variabila
    // cu linia normala
    private JFileChooser fileChooser;
    public static Color backgroundColor = Color.white;
    // boolean getNextColor = false;

    DrawingBoard() {

        this.setBackground(Color.white);
        setSize(new Dimension(1000, 960));
        image = new BufferedImage(2010, 960, BufferedImage.TYPE_INT_ARGB); // DE AICI REGLEZ CANVAS
        previousImage = null;
        g2d = image.createGraphics();
        g2d.setColor(Color.black);
        cleanStroke = g2d.getStroke();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (PaintApp.functionUsed == 0) {

            lastImgHorizontal = Integer.parseInt(PaintApp.pixelsImage1.getText());
            lastImgVertical = Integer.parseInt(PaintApp.pixelsImage2.getText());

            PaintApp.imageAdded = PaintApp.getScaledImage(PaintApp.imageAdded, lastImgHorizontal, lastImgVertical);
            final Image img = PaintApp.imageAdded;

            g2d.drawImage(img, e.getX(), e.getY(), PaintApp.drawingBoard);
            repaint();

        } // desenere imagini

        if (PaintApp.functionUsed == 2) {

            this.setBackground(PaintApp.jColorChooser1.getColor());
            backgroundColor = PaintApp.jColorChooser1.getColor(); // pastrez culoarea de fundal pentru a o seta apoi
            // imaginii inainte de salvare

            repaint();
        } // umplere ecran

        if (PaintApp.functionUsed == 3) {

            g2d.setFont(AddTextFrame.currentFont);
            g2d.setColor(PaintApp.jColorChooser1.getColor());

            String textToPrint = AddTextFrame.text.getText(); // textul din addTextFrame

            FontMetrics fm = g2d.getFontMetrics(); // ca sa iau dimensiunea fontului pe canvas

            int yPos = e.getY();
            int xPos = e.getX();

            StringTokenizer st = new StringTokenizer(textToPrint); // sa despart tot textul la nivel de cuvant
            StringBuffer oneLine = new StringBuffer(); // un stringbuffer gol in care se tot adauga cuvinte daca is ok
            // ca dimensiune si nu da afara
            StringBuffer splitWord = new StringBuffer(); // stringbuffer pentru cand cuvantul nu incape oricum si sa fie
            // desparti

            int positionX = xPos;
            while (st.hasMoreTokens())// while there are words left
            {
                positionX = xPos;
                String word = st.nextToken(); // get the next word
                if (word != null) // error checking
                {
                    if (fm.stringWidth(oneLine.toString() + word) + positionX < 1920) {// verificare daca lungimea bufferului
                        // + inca un cuvant e ok

                        oneLine.append(word + " "); // adaugam cuvantul + spatiu in buffer

                    } else {
                        if(containsWhiteSpace(oneLine.toString())) {
                            g2d.drawString(oneLine.toString(), positionX, yPos); // desenare linie curenta
                            oneLine = new StringBuffer(word + " ");// inceput linie noua
                            yPos += fm.getHeight(); // modificam pozitia pe axa y cu inaltimea textului
                            positionX += fm.stringWidth(oneLine.toString());
                        }else {
                            int wordSize = word.length();
                            int count = 0;

                            while(count != wordSize-1) {
                                if(fm.stringWidth(splitWord.toString() + word.charAt(count)) + positionX < 1920) {
                                    splitWord.append(word.charAt(count));
                                }else {
                                    g2d.drawString(splitWord.toString(), positionX, yPos);
                                    splitWord = new StringBuffer("");
                                    yPos += fm.getHeight();
                                    positionX += fm.stringWidth(splitWord.toString());
                                }
                                count++;
                            }


                        }

                    }
                }
            }
            if (oneLine.length() > 0) { // se verifica daca mai este text in buffer
                g2d.drawString(oneLine.toString(), xPos, yPos);// il desenam si pe ala
            }
            if(splitWord.length() > 0) {
                g2d.drawString(splitWord.toString(), xPos, yPos);
            }

            repaint();
        } // scriere text din clasa creata de mine cu word Wrap

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (PaintApp.functionUsed == 1 || PaintApp.functionUsed == 4 || PaintApp.functionUsed == 5
                || PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3 || PaintApp.functionUsed == 7
                || PaintApp.functionUsed == 8 || PaintApp.functionUsed == 9 || PaintApp.functionUsed == 10) {

            requestFocusInWindow();

            lastx = e.getX();
            lasty = e.getY();

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (PaintApp.functionUsed == 5) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            int px = Math.min(lastx, endX);
            int py = Math.min(lasty, endY);
            int pw = Math.abs(lastx - endX);
            int ph = Math.abs(lasty - endY);

            BufferedImage croppedImage = image.getSubimage(px, py, pw, ph);
            Graphics2D g = croppedImage.createGraphics();
            g.setBackground(PaintApp.drawingBoard.getBackground());
            System.out.println(PaintApp.drawingBoard.getBackground());

            fileChooser = new JFileChooser();

            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {

                    BufferedImage backgroundImage = new BufferedImage(croppedImage.getWidth(), croppedImage.getHeight(),
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graph = backgroundImage.createGraphics();
                    graph.setColor(backgroundColor);
                    graph.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
                    graph.drawImage(croppedImage, 0, 0, null);

                    ImageIO.write(backgroundImage, "png", fileChooser.getSelectedFile());
                    JOptionPane.showMessageDialog(null, "File has been saved", "File Saved",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException except) {
                    JOptionPane.showMessageDialog(null, "File has NOT been saved", "File not saved",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Not saved", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }

        }

        if (PaintApp.functionUsed == 7) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            g2d.setColor(PaintApp.jColorChooser1.getColor());
            g2d.setStroke(thickStroke);
            g2d.drawLine(lastx, lasty, endX, endY);

        } // desenare linie

        if (PaintApp.functionUsed == 8) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            g2d.setColor(PaintApp.jColorChooser1.getColor());
            g2d.setStroke(thickStroke);
            drawPerfectRect(lastx, lasty, endX, endY); // functie ce asigura o forma corecta a dreptunghiului
        } // desenare dreptunghi

        if (PaintApp.functionUsed == 9) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            g2d.setColor(PaintApp.jColorChooser1.getColor());
            g2d.setStroke(thickStroke);
            drawPerfectOval(lastx, lasty, endX, endY); // functie ce asigura o forma corecta a dreptunghiului
        } // desenare cerc

        if (PaintApp.functionUsed == 10) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            Point p1 = new Point(), p2 = new Point(), p3 = new Point();

            if (endX > lastx) {
                if (endY > lasty) {
                    p1.setLocation(lastx, endY);
                    p2.setLocation(lastx + (int) ((endX - lastx) / 2), lasty);
                    p3.setLocation(endX, endY);
                } // drag inspre dreapta jos
                else {
                    p1.setLocation(lastx, lasty);
                    p2.setLocation(lastx + (int) ((endX - lastx) / 2), endY);
                    p3.setLocation(endX, lasty);
                } // drag inspre dreapta sus
            } else {
                if (endY > lasty) {
                    p1.setLocation(endX, endY);
                    p2.setLocation(endX + (int) ((lastx - endX) / 2), lasty);
                    p3.setLocation(lastx, endY);
                } // drag inspre stanga jos
                else {
                    p1.setLocation(endX, lasty);
                    p2.setLocation(endX + (int) ((lastx - endX) / 2), endY);
                    p3.setLocation(lastx, lasty);
                } // drag inspre stanga sus
            }

            int[] xs = { p1.x, p2.x, p3.x };
            int[] ys = { p1.y, p2.y, p3.y };

            g2d.setStroke(thickStroke);
            g2d.setColor(PaintApp.jColorChooser1.getColor());
            if (PaintApp.fillStatus == 0)
                g2d.drawPolygon(new Polygon(xs, ys, 3));
            else
                g2d.fillPolygon(new Polygon(xs, ys, 3));

        } // desenare triunghi

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        PaintApp.mouseInfo.setText("Mouse on canvas");

        if (PaintApp.currentStroke == 1) {

            if (PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3) {
                if (PaintApp.strokePattern == 2) {
                    float dash1[] = { 10.0f };
                    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
                    thickStroke = dashed;
                    // g2d.setStroke(dashed);
                } else {
                    final float dash1[] = { 3.0f, 3.0f };
                    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
                    // g2d.setStroke(dashed);
                    thickStroke = dashed;
                }
            } else {
                thickStroke = cleanStroke;
            }
        } // grosime 1 - verificare daca e normal, despartit sau punctat

        if (PaintApp.currentStroke == 2) {

            if (PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3) {
                if (PaintApp.strokePattern == 2) {
                    float dash1[] = { 10.0f };
                    final BasicStroke dashed = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
                    // g2d.setStroke(dashed);
                    thickStroke = dashed;
                } else {
                    float dash1[] = { 3.0f, 3.0f };
                    final BasicStroke dashed = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
                    // g2d.setStroke(dashed);
                    thickStroke = dashed;
                }
            } else {
                thickStroke = new BasicStroke(4);
            }
        } // grosime 2 - verificare daca e normal, despartit sau punctat

        if (PaintApp.currentStroke == 3) {

            if (PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3) {

                if (PaintApp.strokePattern == 2) {
                    float dash1[] = { 10.0f };
                    BasicStroke dashed = new BasicStroke(6.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                            dash1, 0.0f);
                    thickStroke = dashed;
                    // g2d.setStroke(dashed);
                } else {
                    float dash1[] = { 3.0f, 3.0f };
                    BasicStroke dashed = new BasicStroke(6.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                            dash1, 0.0f);
                    thickStroke = dashed;
                    // g2d.setStroke(dashed);
                }
            } else {
                thickStroke = new BasicStroke(6);
            }
        } // grosime 3 - verificare daca e normal, despartit sau punctat

        if (PaintApp.currentStroke == 4) {
            if (PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3) {

                if (PaintApp.strokePattern == 2) {
                    float dash1[] = { 10.0f };
                    BasicStroke dashed = new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                            dash1, 0.0f);
                    thickStroke = dashed;
                    // g2d.setStroke(dashed);
                } else {
                    float dash1[] = { 3.0f, 3.0f };
                    BasicStroke dashed = new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                            dash1, 0.0f);
                    thickStroke = dashed;
                    // g2d.setStroke(dashed);
                }
            } else {
                thickStroke = new BasicStroke(10);
            }
        } // grosime 4 - verificare daca e normal, despartit sau punctat

    }

    @Override
    public void mouseExited(MouseEvent e) {
        PaintApp.mouseInfo.setText("Mouse exited the canvas");

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (PaintApp.functionUsed == 4 || PaintApp.functionUsed == 1) {

            if (PaintApp.functionUsed == 4)
                g2d.setColor(Color.white);

            if (PaintApp.functionUsed == 1 || PaintApp.strokePattern == 2 || PaintApp.strokePattern == 3)
                g2d.setColor(PaintApp.jColorChooser1.getColor());

            newX = e.getX();
            newY = e.getY();

            g2d.setStroke(thickStroke);
            g2d.drawLine(lastx, lasty, newX, newY);

            lastx = newX;
            lasty = newY;

            repaint();
        } // desenare cu creionul - linii si radiera

        if (PaintApp.functionUsed == 5 || PaintApp.functionUsed == 7 || PaintApp.functionUsed == 8
                || PaintApp.functionUsed == 9 || PaintApp.functionUsed == 10) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        } // pentru formele geometrice

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void drawPerfectRect(int x, int y, int x2, int y2) {
        int px = Math.min(x, x2);
        int py = Math.min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        if (PaintApp.fillStatus == 0)
            g2d.drawRect(px, py, pw, ph);
        else
            g2d.fillRect(px, py, pw, ph);
    }

    public void drawPerfectOval(int x, int y, int x2, int y2) {
        int px = Math.min(x, x2);
        int py = Math.min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        if (PaintApp.fillStatus == 0)
            g2d.drawOval(px, py, pw, ph);
        else
            g2d.fillOval(px, py, pw, ph);
    }

    public void setEndPoint(int x, int y) {
        endX = (x);
        endY = (y);
    }

    public BufferedImage exportImage() {
        return image;
    }

    public void importImage(BufferedImage img) {
        g2d.drawImage(img, 0, 0, null);
        repaint();
    }

    public void newImage() {
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        repaint();
    }

    public void filterImageBasedOnThreshold(BufferedImage img, int redThreshold, int blueThreshold,
                                            int greenThreshold) {

        int width = img.getWidth();
        int height = img.getHeight();

        int[][] pixels = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                pixels[i][j] = img.getRGB(i, j);

                int a = (pixels[i][j] >> 24) & 0xff;

                // get red
                int r = (pixels[i][j] & 0x00ff0000) >> 16;

                // get green
                int g = (pixels[i][j] & 0x0000ff00) >> 8;

                // get blue
                int b = pixels[i][j] & 0x000000ff;

                if (r <= redThreshold) {
                    r = 0;
                }
                if (b <= blueThreshold) {
                    b = 0;
                }
                if (g <= greenThreshold) {
                    g = 0;
                }

                a = 255;

                // set the pixel value
                pixels[i][j] = (a << 24) | (r << 16) | (g << 8) | b;

                img.setRGB(i, j, pixels[i][j]);
                // }
            }
        } // all the pixels in the image

        image = img;

        g2d.drawImage(image,  0,  0, null);
        repaint();
    }

    public boolean containsWhiteSpace(String string){
        if(string != null){
            for(int i = 0; i < string.length(); i++){
                if(Character.isWhitespace(string.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }
}
