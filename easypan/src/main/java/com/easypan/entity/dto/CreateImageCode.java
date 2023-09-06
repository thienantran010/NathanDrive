package com.easypan.entity.dto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CreateImageCode {
    // image width
    private int width = 160;
    //image height
    private int height = 40;
    // auth code length
    private int codeCount = 4;
    // auth code distraction length
    private int lineCount = 20;
    // auth code
    private String code = null;
    // auth code image Buffer
    private BufferedImage buffImg = null;
    Random random = new Random();

    public CreateImageCode() { createImage(); }

    public CreateImageCode(int width, int height){
        this.width = width;
        this.height = height;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount){
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount, int lineCount){
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createImage();
    }

    // generate image
    private void createImage(){
        int fontWidth = width / codeCount; // font width
        int fontHeight = height - 5; // font height
        int codeY = height - 8;

        // image buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        // Graphics2D g = buffImg.createGraphics();
        // set background color
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // set font
        // Font font1 = getFont(fontHeight);
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);

        //set distraction line
        for (int i = 0; i < lineCount; i++){
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

        // add noise
        float yawpRate = 0.01f; // noise rate
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buffImg.setRGB(x, y, random.nextInt(255));
        }

        String str1 = randomStr(codeCount);// get random string
        this.code = str1;
        for (int i = 0; i < codeCount; i++){
            String strRand = str1.substring(i, i+1);
            g.setColor(getRandColor(1, 255));
            // g.drawString(a, x, y);
            // a is the thing that will be drawn, x and y are (x, y) coordination of the most left character of the drawn thing
            g.drawString(strRand, i * fontWidth + 3, codeY);
        }
    }

    // get random string
    private String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++){
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    // get random color
    private Color getRandColor(int fc, int bc) {
        // get random color within the given range
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return  new Color(r, g, b);
    }

    private void shearY(Graphics g, int w1, int h1, Color color){
        int period = random.nextInt(40) + 10; // 50

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++){
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }

    public void write(OutputStream sos) throws IOException{
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {return code.toLowerCase();}
}

















