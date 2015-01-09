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
	protected void paintComponent(Graphics g1) {// ��д����������
	    Graphics2D g = (Graphics2D) g1;
	    super.paintComponent(g);// ִ�г��෽��
	    int width = getWidth();// ��ȡ�����С
	    int height = getHeight();
	    // �������ģʽ����
	    GradientPaint paint = null;
	    paint = new GradientPaint(0, 0, Color.WHITE, 0, height, Color.LIGHT_GRAY);
	    g.setPaint(paint);// ���û�ͼ��������ģʽ
	    g.fillRect(0, 0, width, height);// ���ƾ������ؼ�����
    }
}
