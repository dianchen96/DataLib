//实现闪屏的方法

package com.dc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

public class Splash extends Thread{
	
	public void run() {
		SplashScreen splash = SplashScreen.getSplashScreen();
		Graphics2D g = splash.createGraphics();
		g.setColor(new Color(254,171,11));
		g.drawString("正在加载...",10,100);
		splash.update();
	}
}
