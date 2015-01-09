//负责在登录界面模拟鼠标

package com.dc;

import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;


public class LoginRobot extends Thread {

	public void run() {
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(MainFrame.Login.getX()+225, MainFrame.Login.getY()+35);
			MainFrame.pic.setIcon(new ImageIcon(MainFrame.class.getResource("/image/Upper_dt_C.jpg")));
			Thread.sleep(200);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
