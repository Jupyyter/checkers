package Main;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Inputs.MouseInputs;
import java.awt.Dimension;

public class Pannel extends JPanel {
    private int firstX = 100, firstY = 100, topLeftX, topLeftY, squrDim, gamePointX, gamePointY, offset,scaledWidth,scaledHeight;
    private BufferedImage checkersBorder, redSquare, blackSquare, point, highlighter, blueThing, redThing, blueKing,
            redKing, redWin, blueWin;
    private double scaling = 1.0;
    public squrInfo squrinfo[][];
    private MouseInputs mouseInputs = new MouseInputs(this);

    public Pannel() {
        addMouseListener(mouseInputs);// checking for mouse inputs when cursor in the game panel
        addMouseMotionListener(mouseInputs);
        importImgs();
        squrinfo = new squrInfo[8][8];
        for (int i = 0; i < squrinfo.length; i++) {
            for (int j = 0; j < squrinfo[i].length; j++) {
                squrinfo[i][j] = new squrInfo();
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // spawn squares
                if ((i + j) % 2 == 0) {
                    // spawn pawns
                    if (i < 3) {
                        squrinfo[i][j].red = true;
                    } else if (i > 4) {
                        squrinfo[i][j].blue = true;
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g) {// here we draw
        super.paintComponent(g);
        drawBorder(g);
        drawSquaresAndThings(g);
        if(redLose()){
            g.drawImage(blueWin, topLeftX, topLeftY, scaledWidth, scaledHeight, null);
        }
        else if(blueLose()){
            g.drawImage(redWin, topLeftX, topLeftY, scaledWidth, scaledHeight, null);
        }

    }

    private void drawBorder(Graphics g) {
        // scale checkersMapBorder to fit window
        int imgWidth = checkersBorder.getWidth();
        int imgHeight = checkersBorder.getHeight();
        double panelAspectRatio = (double) getWidth() / getHeight();
        double imgAspectRatio = (double) imgWidth / imgHeight;
        if (panelAspectRatio > imgAspectRatio) {
            // Scale based on height
            scaling = (double) getHeight() / imgHeight;
        } else {
            // Scale based on width
            scaling = (double) getWidth() / imgWidth;
        }
        scaledWidth = (int) (scaling * imgWidth);
        scaledHeight = (int) (scaling * imgHeight);
        int x = (getWidth() - scaledWidth) / 2;
        int y = (getHeight() - scaledHeight) / 2;
        topLeftX = x;
        topLeftY = y;
        g.drawImage(checkersBorder, x, y, scaledWidth, scaledHeight, null);

    }

    private void drawSquaresAndThings(Graphics g) {
        gamePointX = topLeftX + (int) (5 * scaling);
        gamePointY = topLeftY + (int) (5 * scaling);// the 0,0 of the checkers map
        squrDim = (int) (redSquare.getWidth() * scaling);// squares dimention based on resolution
        int thingDim = (int) (blueThing.getWidth() * scaling);
        offset = (int) (checkersBorder.getWidth() * scaling - (int) (10 * scaling) - (int) (squrDim * 8));
        for (int i = 0; i < 8; i++) {// spawn squares
            for (int j = 0; j < 8; j++) {
                if (squrinfo[i][j].highlight == true) {// if highlight square
                    drawSquareForEachCase(g, highlighter, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i,
                            squrDim,
                            squrDim, offset, 0);

                } else if ((i + j) % 2 == 0) {// if red square
                    drawSquareForEachCase(g, redSquare, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i,
                            squrDim,
                            squrDim, offset, 0);
                } else {// if black square
                    drawSquareForEachCase(g, blackSquare, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i,
                            squrDim,
                            squrDim, offset, 0);
                }
                squrinfo[i][j].locationX = gamePointX + squrDim * j;
                squrinfo[i][j].locationY = gamePointY + squrDim * i;
                // draw the things
                if (squrinfo[i][j].blue == true && squrinfo[i][j].red == false) {
                    if (squrinfo[i][j].king) {
                        g.drawImage(blueKing, gamePointX + thingDim * j, gamePointY + thingDim * i, thingDim, thingDim,
                                null);
                    } else {
                        g.drawImage(blueThing, gamePointX + thingDim * j, gamePointY + thingDim * i, thingDim, thingDim,
                                null);
                    }
                } else if (squrinfo[i][j].blue == false && squrinfo[i][j].red == true) {
                    if (squrinfo[i][j].king) {
                        g.drawImage(redKing, gamePointX + thingDim * j, gamePointY + thingDim * i, thingDim, thingDim,
                                null);
                    } else {
                        g.drawImage(redThing, gamePointX + thingDim * j, gamePointY + thingDim * i, thingDim, thingDim,
                                null);
                    }
                }
                if (squrinfo[i][j].possibleMove == true) {// if possible move on a square
                    g.drawImage(point, gamePointX + squrDim * j, gamePointY + squrDim * i, squrDim, squrDim,
                            null);
                }
            }
        }
    }

    private void importImgs() {
        try {
            checkersBorder = ImageIO.read(getClass().getResourceAsStream("/imgs/checkersBorder.png"));
            redSquare = ImageIO.read(getClass().getResourceAsStream("/imgs/redSquare.png"));
            blackSquare = ImageIO.read(getClass().getResourceAsStream("/imgs/blackSquare.png"));
            point = ImageIO.read(getClass().getResourceAsStream("/imgs/point.png"));
            blueThing = ImageIO.read(getClass().getResourceAsStream("/imgs/blueThing.png"));
            blueKing = ImageIO.read(getClass().getResourceAsStream("/imgs/blueKing.png"));
            redThing = ImageIO.read(getClass().getResourceAsStream("/imgs/redThing.png"));
            redKing = ImageIO.read(getClass().getResourceAsStream("/imgs/redKing.png"));
            highlighter = ImageIO.read(getClass().getResourceAsStream("/imgs/highlighter.png"));
            redWin = ImageIO.read(getClass().getResourceAsStream("/imgs/redWin.png"));
            blueWin = ImageIO.read(getClass().getResourceAsStream("/imgs/blueWin.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawSquare(Graphics g, BufferedImage image, int i, int j, int x, int y, int width, int height,
            int offsetWidth, int offsetHeight) {
        g.drawImage(image, x, y, width + offsetWidth, height + offsetHeight, null);
        squrinfo[i][j].width = width + offsetWidth;
        squrinfo[i][j].height = height + offsetHeight;
    }

    private void drawSquareForEachCase(Graphics g, BufferedImage image, int i, int j, int x, int y, int width,
            int height,
            int offsetWidth, int offsetHeight) {
        if (i == 7 && j != 7) {// down offset
            drawSquare(g, image, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i, squrDim,
                    squrDim, 0, offset);
        } else if (j == 7 && i != 7) {// right offset
            drawSquare(g, image, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i, squrDim,
                    squrDim, offset, 0);
        }
        if (j == 7 && i == 7) {// down right offset
            drawSquare(g, image, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i, squrDim,
                    squrDim, offset, offset);
        } else {// default
            drawSquare(g, image, i, j, gamePointX + squrDim * j, gamePointY + squrDim * i, squrDim,
                    squrDim, 0, 0);
        }
    }

    private boolean redLose() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squrinfo[i][j].red == true) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean blueLose() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squrinfo[i][j].blue == true) {
                    return false;
                }
            }
        }
        return true;
    }
}