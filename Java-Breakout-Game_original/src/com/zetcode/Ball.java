package com.zetcode;

import javax.swing.ImageIcon;

public class Ball extends Sprite {

    private int xdir;
    private int ydir;

    public Ball() {

        initBall();
    }

    private void initBall() {
        
        xdir = 1;
        ydir = -1;

        loadImage();
        getImageDimensions();
        resetState();
    }

    private void loadImage() {

    	var ii = new ImageIcon("resources/ball.png");
        image = ii.getImage();
    }

    void move() {

    	// อัปเดตตำแหน่งก่อน
    	x += xdir;
    	y += ydir;

    	int right = Commons.WIDTH - getImageWidth();

    	// ชนผนังซ้าย/ขวา
    	if (x <= 0) {
    	    x = 0;                 // จับให้อยู่ในขอบ
    	    xdir = Math.abs(xdir); // เด้งไปทางขวา (คงความเร็วเดิม)
    	} else if (x >= right) {
    	    x = right;
    	    xdir = -Math.abs(xdir); // เด้งไปทางซ้าย (คงความเร็วเดิม)
    	}

    	// ชนเพดาน (ด้านบน)
    	if (y <= 0) {
    	    y = 0;
    	    ydir = Math.abs(ydir);  // เด้งลง (คงความเร็วเดิม)
    	}

    	// ด้านล่างปล่อยให้ Board เช็ค game over เอง
    }

    private void resetState() {

        x = Commons.INIT_BALL_X;
        y = Commons.INIT_BALL_Y;
    }

    void setXDir(int x) {

        xdir = x;
    }

    void setYDir(int y) {

        ydir = y;
    }
    //*****************ทดสอบแก้
    int getXDir() { 
    	return xdir; 
    }

    int getYDir() {

        return ydir;
    }
}
