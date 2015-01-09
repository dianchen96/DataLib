package com.dc.myframe.panel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ShadePanelH extends JPanel {

	private static final long serialVersionUID = 4130628664018734365L;

	public ShadePanelH() {
        super();
        setLayout(null);
    }
	
	@Override
	protected void paintComponent(Graphics g1) {// 重写绘制组件外观
	    Graphics2D g = (Graphics2D) g1;
	    super.paintComponent(g);// 执行超类方法
	    int width = getWidth();// 获取组件大小
	    int height = getHeight();
	    // 创建填充模式对象
	    GradientPaint paint = null;
	    paint = new GradientPaint(0, 0, Color.WHITE, 0, height, Color.LIGHT_GRAY);
	    g.setPaint(paint);// 设置绘图对象的填充模式
	    g.fillRect(0, 0, width, height);// 绘制矩形填充控件界面
    }
}
