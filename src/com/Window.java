package com;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.ppt.PPTGraphics2D;

@SuppressWarnings("serial")
public class Window  extends JFrame {
	   private MenuInfographics6 menu;
	   public Window() {
	      super("");
	      nobardisplay = false;
	      calculateWindowsBounds();
	      setSize(virtualBounds.width, virtualBounds.height );
	      setLocation(0,0);
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      //setLocationRelativeTo(null);
	      setResizable(false);
	      setUndecorated(true);
	      setBackground(new Color(0f, 0f, 0f, 0f));
	      menu = new MyPanel();
	      getContentPane().add(menu); 
	   }
	   private Rectangle virtualBounds = new Rectangle();
	   private void calculateWindowsBounds() {
		   GraphicsEnvironment ge = GraphicsEnvironment.
		           getLocalGraphicsEnvironment();
		   GraphicsDevice[] gs =
		           ge.getScreenDevices();
		   for (int j = 0; j < gs.length; j++) {
		       GraphicsDevice gd = gs[j];
		       GraphicsConfiguration[] gc =
		           gd.getConfigurations();
		       for (int i=0; i < gc.length; i++) {
		           virtualBounds =
		               virtualBounds.union(gc[i].getBounds());
		       }
		   }
	   }
	   public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new Window().setVisible(true);
	         }
	      });
	   }
	   
	   Component myself;
	   boolean nobardisplay;
	   
	   private interface ThreadFunction {
		   void execute();
	   }
	   
	   private class MyPanel extends MenuInfographics6 implements Runnable {
		   private int clicked = -1;
		   private Color darkLeadBlue = new Color(55, 77, 100);
		   private Color lightLeadBlue = new Color(180, 190, 200);
		   private GradientPaint g0 = new GradientPaint(0f, 10f, lightLeadBlue, 0f, 40f, Color.white);
		   private GradientPaint g1 = new GradientPaint(0f, 10f, Color.white, 0f, 40f, lightLeadBlue);
		   private GradientPaint g3 = new GradientPaint(0f, 50f, lightLeadBlue, 0f, 80f, Color.white);
		   private GradientPaint g4 = new GradientPaint(0f, 50f, Color.white, 0f, 80f, lightLeadBlue);
		   
		   /* thread to execute commands asynchronously with respect to the GUI */
		   ThreadFunction exit = () -> {
			   System.exit(0);
		   };
		   ThreadFunction microvba = () -> {
			   VectorGraphics g = null;
			   try {
				   g = new PPTGraphics2D(new File("macro.txt"), myself);
			   } catch (FileNotFoundException e1) {
				   // TODO Auto-generated catch block
				   e1.printStackTrace();
				   return;
			   }
			   nobardisplay = true;
			   g.startExport();
			   myself.print((Graphics) g);
			   g.endExport();
			   nobardisplay = false;
			   stop();
		   };		   
		   Thread thread = null;
		   ThreadFunction threadfunction = null;
		   public void start() {
			   if (thread == null ) {
				   thread = new Thread(this);
				   thread.start();
			   }
		   }
		   public void stop() {
			   thread = null;
		   }
		   public void run() {
			   while (thread != null) {
				   try {
					   Thread.sleep(100);
				   } catch (InterruptedException e) {}
				   if (threadfunction != null) threadfunction.execute();
			   }
		   }
		   /* end thread */
		   
		   public MyPanel() {
			   pathexit = exitmsg();
			   pathMvba = microvba();
			   myself = this;
			   MouseAdapter mouseListener = new MouseAdapter() {
				   public void mousePressed(MouseEvent e) {
					   clicked = -1;
					   float x =  e.getX();
					   float y = e.getY();
					   if (y < 10f || y > 80f) return;
					   if (x < (dim.width-200f) || x > (dim.width-20f)) return;
					   if (y < 40f) clicked = 0;
					   else {
						   if (y > 50f) clicked = 1;
						   else return;
					   }
					   repaint();
				   }
				   public void mouseReleased(MouseEvent e) {
					   if (clicked == -1) return; 
					   repaint();
					   if (clicked == 1) threadfunction = exit;
					   else threadfunction = microvba;
					   clicked = -1;
					   start();
					   repaint();
				   }
			   };
			   addMouseListener(mouseListener);
		   }
		   Dimension dim;
		   Path2D.Float pathexit;
		   Path2D.Float pathMvba;
		   public void paintComponent(Graphics g) {
			   dim = getSize();
			   if (g == null) return;
			   super.paintComponent(g);
			   if (nobardisplay) return; 
			   Graphics2D g2 = (Graphics2D) g;
			   if (clicked == 0) g2.setPaint(g0);
			   else g2.setPaint(g1);
			   g.fillRoundRect(dim.width-200, 10, 180, 30, 12, 12);
			   if (clicked == 1) g2.setPaint(g3);
			   else g2.setPaint(g4);
			   g.fillRoundRect(dim.width-200, 50, 180, 30, 12, 12);
			   g.setColor(darkLeadBlue);
			   g.translate(dim.width-190,8);
			   g2.fill(pathMvba);
			   g.translate(0,40);
			   g2.fill(pathexit);
		   }

		   private Path2D.Float exitmsg() {
			      Path2D.Float p = new Path2D.Float();
			      p.moveTo(1.125f,21.999f);
			      p.lineTo(8.02734f,21.999f);
			      p.lineTo(8.02734f,20.1592f);
			      p.lineTo(3.31641f,20.1592f);
			      p.lineTo(3.31641f,17.8037f);
			      p.lineTo(7.21875f,17.8037f);
			      p.lineTo(7.21875f,16.0107f);
			      p.lineTo(3.31641f,16.0107f);
			      p.lineTo(3.31641f,14.0889f);
			      p.lineTo(7.71094f,14.0889f);
			      p.lineTo(7.71094f,12.249f);
			      p.lineTo(1.125f,12.249f);
			      p.lineTo(1.125f,21.999f);
			      p.moveTo(8.48145f,21.999f);
			      p.moveTo(12.4072f,16.8545f);
			      p.lineTo(10.9424f,14.6045f);
			      p.lineTo(8.68066f,14.6045f);
			      p.curveTo(9.09082f,15.1904f,9.5127f,15.7764f,9.93457f,16.3975f);
			      p.curveTo(10.3564f,16.9951f,10.7783f,17.5928f,11.2002f,18.1904f);
			      p.curveTo(10.9541f,18.4834f,10.708f,18.8115f,10.4502f,19.1631f);
			      p.curveTo(10.1924f,19.4912f,9.95801f,19.8311f,9.72363f,20.1709f);
			      p.curveTo(9.48926f,20.5225f,9.27832f,20.8506f,9.09082f,21.167f);
			      p.curveTo(8.8916f,21.4951f,8.73926f,21.7646f,8.62207f,21.999f);
			      p.lineTo(10.7549f,21.999f);
			      p.curveTo(10.8604f,21.8115f,10.9541f,21.6006f,11.0947f,21.3896f);
			      p.curveTo(11.2119f,21.1787f,11.3525f,20.9561f,11.4814f,20.7334f);
			      p.curveTo(11.5986f,20.5225f,11.7393f,20.2998f,11.8799f,20.0889f);
			      p.curveTo(12.0439f,19.8779f,12.1846f,19.6787f,12.3252f,19.4912f);
			      p.curveTo(12.4775f,19.6787f,12.6182f,19.8896f,12.7822f,20.1123f);
			      p.curveTo(12.9229f,20.3584f,13.0752f,20.5811f,13.2158f,20.8154f);
			      p.curveTo(13.3564f,21.0381f,13.4854f,21.249f,13.6025f,21.46f);
			      p.lineTo(13.8838f,21.999f);
			      p.lineTo(16.1338f,21.999f);
			      p.curveTo(15.8643f,21.4834f,15.501f,20.874f,15.0322f,20.2178f);
			      p.curveTo(14.5752f,19.5498f,14.0713f,18.8584f,13.4971f,18.1553f);
			      p.lineTo(16.04f,14.6045f);
			      p.lineTo(13.8721f,14.6045f);
			      p.lineTo(12.4072f,16.8545f);
			      p.moveTo(16.2363f,21.999f);
			      p.moveTo(19.3184f,21.999f);
			      p.lineTo(19.3184f,14.6045f);
			      p.lineTo(17.2207f,14.6045f);
			      p.lineTo(17.2207f,21.999f);
			      p.lineTo(19.3184f,21.999f);
			      p.moveTo(19.5176f,12.46f);
			      p.curveTo(19.5176f,12.085f,19.3887f,11.7686f,19.1309f,11.5576f);
			      p.curveTo(18.8965f,11.335f,18.6035f,11.2295f,18.2637f,11.2295f);
			      p.curveTo(17.9238f,11.2295f,17.6309f,11.335f,17.3848f,11.5576f);
			      p.curveTo(17.1387f,11.7686f,17.0098f,12.085f,17.0098f,12.46f);
			      p.curveTo(17.0098f,12.8467f,17.1387f,13.1514f,17.3848f,13.3623f);
			      p.curveTo(17.6309f,13.585f,17.9238f,13.7021f,18.2637f,13.7021f);
			      p.curveTo(18.6035f,13.7021f,18.8965f,13.585f,19.1309f,13.3623f);
			      p.curveTo(19.3887f,13.1514f,19.5176f,12.8467f,19.5176f,12.46f);
			      p.moveTo(20.2822f,21.999f);
			      p.moveTo(21.1963f,12.7529f);
			      p.lineTo(21.1963f,18.9756f);
			      p.curveTo(21.1963f,19.4561f,21.2432f,19.8896f,21.3252f,20.2881f);
			      p.curveTo(21.4189f,20.6748f,21.5713f,21.0146f,21.8057f,21.2959f);
			      p.curveTo(22.0283f,21.5654f,22.3213f,21.7764f,22.6963f,21.9404f);
			      p.curveTo(23.083f,22.0811f,23.54f,22.1631f,24.126f,22.1631f);
			      p.curveTo(24.5713f,22.1631f,24.9463f,22.1279f,25.2627f,22.0576f);
			      p.curveTo(25.5674f,21.9873f,25.8486f,21.8818f,26.1064f,21.7881f);
			      p.lineTo(25.8135f,20.1592f);
			      p.curveTo(25.6143f,20.2295f,25.4033f,20.2881f,25.1455f,20.335f);
			      p.curveTo(24.9229f,20.3701f,24.6885f,20.3936f,24.4541f,20.3936f);
			      p.curveTo(23.9971f,20.3936f,23.6807f,20.2646f,23.5166f,20.0068f);
			      p.curveTo(23.376f,19.7373f,23.2939f,19.3857f,23.2939f,18.9521f);
			      p.lineTo(23.2939f,16.3389f);
			      p.lineTo(25.8135f,16.3389f);
			      p.lineTo(25.8135f,14.6045f);
			      p.lineTo(23.2939f,14.6045f);
			      p.lineTo(23.2939f,12.4248f);
			      p.lineTo(21.1963f,12.7529f);
			      p.moveTo(26.4961f,21.999f);
			      return p;
			   }
			   private Path2D.Float microvba() {
			      Path2D.Float p = new Path2D.Float();
			      p.moveTo(8.35547f,12.249f);
			      p.lineTo(0.28125f,12.249f);
			      p.lineTo(0.28125f,14.124f);
			      p.lineTo(3.22266f,14.124f);
			      p.lineTo(3.22266f,21.999f);
			      p.lineTo(5.41406f,21.999f);
			      p.lineTo(5.41406f,14.124f);
			      p.lineTo(8.35547f,14.124f);
			      p.lineTo(8.35547f,12.249f);
			      p.moveTo(8.5957f,21.999f);
			      p.moveTo(14.0332f,16.4561f);
			      p.lineTo(14.3965f,14.7217f);
			      p.curveTo(14.2793f,14.6865f,14.1387f,14.6396f,13.9863f,14.6045f);
			      p.curveTo(13.8223f,14.5693f,13.6582f,14.5459f,13.5059f,14.5107f);
			      p.curveTo(13.3535f,14.499f,13.1777f,14.4873f,13.0254f,14.4639f);
			      p.curveTo(12.873f,14.4639f,12.7324f,14.4404f,12.6152f,14.4404f);
			      p.curveTo(11.9824f,14.4404f,11.4199f,14.499f,10.9043f,14.6279f);
			      p.curveTo(10.4004f,14.7451f,9.95508f,14.8623f,9.58008f,14.9912f);
			      p.lineTo(9.58008f,21.999f);
			      p.lineTo(11.6777f,21.999f);
			      p.lineTo(11.6777f,16.3389f);
			      p.curveTo(11.7715f,16.3154f,11.9121f,16.292f,12.0879f,16.2568f);
			      p.curveTo(12.2754f,16.2451f,12.4395f,16.2334f,12.5566f,16.2334f);
			      p.curveTo(12.8613f,16.2334f,13.1191f,16.2568f,13.377f,16.3154f);
			      p.curveTo(13.6348f,16.3623f,13.8574f,16.4092f,14.0332f,16.4561f);
			      p.moveTo(14.502f,21.999f);
			      p.moveTo(18.3105f,20.5342f);
			      p.curveTo(17.9355f,20.5342f,17.6191f,20.4756f,17.3965f,20.335f);
			      p.curveTo(17.1855f,20.2295f,17.0801f,19.9834f,17.0801f,19.667f);
			      p.curveTo(17.0801f,19.4912f,17.1152f,19.3506f,17.1855f,19.2451f);
			      p.curveTo(17.2559f,19.1279f,17.373f,19.0459f,17.502f,18.9756f);
			      p.curveTo(17.6191f,18.9053f,17.7832f,18.8584f,17.959f,18.8467f);
			      p.curveTo(18.1348f,18.8232f,18.3105f,18.8115f,18.5098f,18.8115f);
			      p.curveTo(18.6504f,18.8115f,18.8027f,18.8232f,18.9785f,18.835f);
			      p.curveTo(19.1426f,18.8467f,19.2715f,18.8818f,19.3535f,18.8936f);
			      p.lineTo(19.3535f,20.4756f);
			      p.curveTo(19.2363f,20.499f,19.084f,20.5107f,18.9082f,20.5225f);
			      p.curveTo(18.7207f,20.5342f,18.5215f,20.5342f,18.3105f,20.5342f);
			      p.moveTo(18.1465f,14.4053f);
			      p.curveTo(17.9004f,14.4053f,17.6543f,14.417f,17.4316f,14.4287f);
			      p.curveTo(17.1738f,14.4639f,16.9512f,14.4873f,16.7402f,14.5107f);
			      p.curveTo(16.5293f,14.5576f,16.3301f,14.6045f,16.1309f,14.6279f);
			      p.curveTo(15.9668f,14.6865f,15.8145f,14.71f,15.6855f,14.7568f);
			      p.lineTo(15.9551f,16.4443f);
			      p.curveTo(16.2012f,16.3389f,16.4941f,16.2686f,16.8457f,16.2334f);
			      p.curveTo(17.1973f,16.1748f,17.5488f,16.1514f,17.9004f,16.1514f);
			      p.curveTo(18.4395f,16.1514f,18.8145f,16.2568f,19.0254f,16.4678f);
			      p.curveTo(19.2363f,16.6787f,19.3535f,16.9834f,19.3535f,17.335f);
			      p.lineTo(19.3535f,17.5225f);
			      p.curveTo(19.2246f,17.4873f,19.0605f,17.4521f,18.8379f,17.4404f);
			      p.curveTo(18.6387f,17.417f,18.3809f,17.4053f,18.1113f,17.4053f);
			      p.curveTo(17.7129f,17.4053f,17.3262f,17.4404f,16.9512f,17.5225f);
			      p.curveTo(16.5645f,17.5928f,16.248f,17.7217f,15.9551f,17.9092f);
			      p.curveTo(15.6504f,18.085f,15.4277f,18.3311f,15.2637f,18.624f);
			      p.curveTo(15.0762f,18.917f,14.9941f,19.2803f,14.9941f,19.7256f);
			      p.curveTo(14.9941f,20.1709f,15.0645f,20.5459f,15.2168f,20.8623f);
			      p.curveTo(15.3691f,21.167f,15.5801f,21.4248f,15.8613f,21.6006f);
			      p.curveTo(16.1426f,21.7998f,16.4824f,21.9404f,16.8691f,22.0225f);
			      p.curveTo(17.2559f,22.1162f,17.6895f,22.1514f,18.1699f,22.1514f);
			      p.curveTo(18.9316f,22.1514f,19.5762f,22.1162f,20.1152f,22.0225f);
			      p.curveTo(20.6543f,21.9287f,21.0762f,21.8584f,21.3691f,21.7881f);
			      p.lineTo(21.3691f,17.4287f);
			      p.curveTo(21.3691f,16.96f,21.3105f,16.5615f,21.2168f,16.1748f);
			      p.curveTo(21.1113f,15.7998f,20.9355f,15.4834f,20.6895f,15.2139f);
			      p.curveTo(20.4434f,14.9561f,20.1035f,14.7568f,19.6934f,14.6162f);
			      p.curveTo(19.2832f,14.4756f,18.7676f,14.4053f,18.1465f,14.4053f);
			      p.moveTo(22.2422f,21.999f);
			      p.moveTo(23.2266f,14.8506f);
			      p.lineTo(23.2266f,21.999f);
			      p.lineTo(25.3242f,21.999f);
			      p.lineTo(25.3242f,16.292f);
			      p.curveTo(25.4531f,16.2686f,25.6055f,16.2568f,25.7695f,16.2334f);
			      p.curveTo(25.9453f,16.2334f,26.0977f,16.2217f,26.25f,16.2217f);
			      p.curveTo(26.7422f,16.2217f,27.0703f,16.3623f,27.2461f,16.6436f);
			      p.curveTo(27.4336f,16.9248f,27.5156f,17.4053f,27.5156f,18.0732f);
			      p.lineTo(27.5156f,21.999f);
			      p.lineTo(29.6133f,21.999f);
			      p.lineTo(29.6133f,17.8271f);
			      p.curveTo(29.6133f,17.3115f,29.5547f,16.8545f,29.4609f,16.4443f);
			      p.curveTo(29.3555f,16.0342f,29.1797f,15.6709f,28.9336f,15.3779f);
			      p.curveTo(28.6992f,15.0732f,28.3594f,14.8389f,27.9492f,14.6865f);
			      p.curveTo(27.5273f,14.5107f,27.0117f,14.4287f,26.3789f,14.4287f);
			      p.curveTo(25.7578f,14.4287f,25.1719f,14.4756f,24.6094f,14.5693f);
			      p.curveTo(24.0469f,14.6514f,23.5781f,14.7568f,23.2266f,14.8506f);
			      p.moveTo(30.4863f,21.999f);
			      p.moveTo(33.5918f,20.499f);
			      p.curveTo(33.2285f,20.499f,32.8652f,20.4521f,32.4785f,20.3701f);
			      p.curveTo(32.1035f,20.2998f,31.7402f,20.1826f,31.3887f,20.042f);
			      p.lineTo(31.0371f,21.7412f);
			      p.curveTo(31.2012f,21.8115f,31.4941f,21.917f,31.9512f,22.0225f);
			      p.curveTo(32.373f,22.1396f,32.9238f,22.1982f,33.5801f,22.1982f);
			      p.curveTo(34.5762f,22.1982f,35.3496f,22.0107f,35.9121f,21.6357f);
			      p.curveTo(36.4512f,21.249f,36.7324f,20.71f,36.7324f,19.9717f);
			      p.curveTo(36.7324f,19.667f,36.709f,19.3975f,36.6152f,19.1631f);
			      p.curveTo(36.5684f,18.917f,36.4395f,18.7061f,36.252f,18.5068f);
			      p.curveTo(36.0879f,18.3311f,35.8418f,18.1436f,35.5488f,17.9795f);
			      p.curveTo(35.2559f,17.8271f,34.8691f,17.6514f,34.4121f,17.4873f);
			      p.curveTo(34.1895f,17.4053f,34.002f,17.335f,33.8613f,17.2412f);
			      p.curveTo(33.709f,17.1943f,33.5918f,17.124f,33.5098f,17.0537f);
			      p.curveTo(33.4277f,16.9951f,33.3691f,16.9365f,33.3457f,16.8662f);
			      p.curveTo(33.3105f,16.7959f,33.2988f,16.7256f,33.2988f,16.6436f);
			      p.curveTo(33.2988f,16.2686f,33.627f,16.0928f,34.2832f,16.0928f);
			      p.curveTo(34.6348f,16.0928f,34.9629f,16.1162f,35.2441f,16.1865f);
			      p.curveTo(35.5371f,16.2568f,35.8066f,16.3271f,36.041f,16.4092f);
			      p.lineTo(36.4043f,14.7803f);
			      p.curveTo(36.1699f,14.6865f,35.8301f,14.6045f,35.4199f,14.5107f);
			      p.curveTo(35.0098f,14.4404f,34.5762f,14.4053f,34.1309f,14.4053f);
			      p.curveTo(33.2285f,14.4053f,32.5254f,14.6045f,32.0098f,15.0029f);
			      p.curveTo(31.4824f,15.4131f,31.2363f,15.9639f,31.2363f,16.6553f);
			      p.curveTo(31.2363f,17.0068f,31.2832f,17.3115f,31.3887f,17.5693f);
			      p.curveTo(31.4824f,17.8271f,31.6348f,18.0381f,31.8223f,18.2256f);
			      p.curveTo(32.0098f,18.4014f,32.2324f,18.5654f,32.5137f,18.6943f);
			      p.curveTo(32.7598f,18.8232f,33.0762f,18.9521f,33.3926f,19.0693f);
			      p.curveTo(33.8145f,19.2334f,34.1309f,19.3857f,34.3418f,19.4912f);
			      p.curveTo(34.5527f,19.6318f,34.6465f,19.7725f,34.6465f,19.9482f);
			      p.curveTo(34.6465f,20.1592f,34.5645f,20.3115f,34.4121f,20.3818f);
			      p.curveTo(34.2598f,20.4521f,33.9785f,20.499f,33.5918f,20.499f);
			      p.moveTo(37.2744f,21.999f);
			      p.moveTo(41.4697f,11.0889f);
			      p.curveTo(40.415f,11.0889f,39.6064f,11.3701f,39.0791f,11.9326f);
			      p.curveTo(38.5283f,12.4951f,38.2588f,13.2451f,38.2588f,14.2061f);
			      p.lineTo(38.2588f,21.999f);
			      p.lineTo(40.3564f,21.999f);
			      p.lineTo(40.3564f,16.3389f);
			      p.lineTo(42.9463f,16.3389f);
			      p.lineTo(42.9463f,14.6045f);
			      p.lineTo(40.3564f,14.6045f);
			      p.lineTo(40.3564f,14.2295f);
			      p.curveTo(40.3564f,14.042f,40.3682f,13.8545f,40.4268f,13.6904f);
			      p.curveTo(40.4502f,13.5146f,40.5322f,13.374f,40.626f,13.2451f);
			      p.curveTo(40.7314f,13.1279f,40.8604f,13.0225f,41.0479f,12.9639f);
			      p.curveTo(41.2119f,12.8818f,41.4229f,12.8584f,41.6807f,12.8584f);
			      p.curveTo(41.8564f,12.8584f,42.0674f,12.8818f,42.2783f,12.917f);
			      p.curveTo(42.501f,12.9639f,42.7119f,13.0225f,42.8994f,13.1045f);
			      p.lineTo(43.3096f,11.4404f);
			      p.curveTo(43.1221f,11.3701f,42.876f,11.2764f,42.5479f,11.1943f);
			      p.curveTo(42.2197f,11.124f,41.8564f,11.0889f,41.4697f,11.0889f);
			      p.moveTo(43.1807f,21.999f);
			      p.moveTo(51.0908f,18.2842f);
			      p.curveTo(51.0908f,17.71f,50.9971f,17.1709f,50.8213f,16.7021f);
			      p.curveTo(50.6338f,16.2217f,50.3994f,15.8115f,50.0713f,15.4717f);
			      p.curveTo(49.7549f,15.1318f,49.3682f,14.8623f,48.9229f,14.6865f);
			      p.curveTo(48.4775f,14.499f,47.9854f,14.4053f,47.458f,14.4053f);
			      p.curveTo(46.9307f,14.4053f,46.4385f,14.499f,45.9932f,14.6865f);
			      p.curveTo(45.5596f,14.8623f,45.1729f,15.1318f,44.8447f,15.4717f);
			      p.curveTo(44.5283f,15.8115f,44.2822f,16.2217f,44.083f,16.7021f);
			      p.curveTo(43.9072f,17.1709f,43.8135f,17.71f,43.8135f,18.2842f);
			      p.curveTo(43.8135f,18.8584f,43.8955f,19.3975f,44.083f,19.8896f);
			      p.curveTo(44.2588f,20.3701f,44.5049f,20.7803f,44.8213f,21.1318f);
			      p.curveTo(45.1494f,21.46f,45.5244f,21.7295f,45.9697f,21.917f);
			      p.curveTo(46.415f,22.0928f,46.9072f,22.1982f,47.458f,22.1982f);
			      p.curveTo(48.0088f,22.1982f,48.5127f,22.0928f,48.9463f,21.917f);
			      p.curveTo(49.4033f,21.7295f,49.7783f,21.46f,50.1064f,21.1318f);
			      p.curveTo(50.4229f,20.7803f,50.6689f,20.3701f,50.833f,19.8896f);
			      p.curveTo(50.9971f,19.3975f,51.0908f,18.8584f,51.0908f,18.2842f);
			      p.moveTo(48.9463f,18.2842f);
			      p.curveTo(48.9463f,18.9287f,48.8174f,19.4443f,48.5479f,19.8311f);
			      p.curveTo(48.3018f,20.1943f,47.9385f,20.3936f,47.458f,20.3936f);
			      p.curveTo(46.9775f,20.3936f,46.6143f,20.1943f,46.3447f,19.8311f);
			      p.curveTo(46.0752f,19.4443f,45.9463f,18.9287f,45.9463f,18.2842f);
			      p.curveTo(45.9463f,17.6396f,46.0752f,17.1357f,46.3447f,16.7725f);
			      p.curveTo(46.6143f,16.3857f,46.9775f,16.1982f,47.458f,16.1982f);
			      p.curveTo(47.9385f,16.1982f,48.3018f,16.3857f,48.5479f,16.7725f);
			      p.curveTo(48.8174f,17.1357f,48.9463f,17.6396f,48.9463f,18.2842f);
			      p.moveTo(51.6768f,21.999f);
			      p.moveTo(57.1143f,16.4561f);
			      p.lineTo(57.4775f,14.7217f);
			      p.curveTo(57.3604f,14.6865f,57.2197f,14.6396f,57.0674f,14.6045f);
			      p.curveTo(56.9033f,14.5693f,56.7393f,14.5459f,56.5869f,14.5107f);
			      p.curveTo(56.4346f,14.499f,56.2588f,14.4873f,56.1064f,14.4639f);
			      p.curveTo(55.9541f,14.4639f,55.8135f,14.4404f,55.6963f,14.4404f);
			      p.curveTo(55.0635f,14.4404f,54.501f,14.499f,53.9854f,14.6279f);
			      p.curveTo(53.4814f,14.7451f,53.0361f,14.8623f,52.6611f,14.9912f);
			      p.lineTo(52.6611f,21.999f);
			      p.lineTo(54.7588f,21.999f);
			      p.lineTo(54.7588f,16.3389f);
			      p.curveTo(54.8525f,16.3154f,54.9932f,16.292f,55.1689f,16.2568f);
			      p.curveTo(55.3564f,16.2451f,55.5205f,16.2334f,55.6377f,16.2334f);
			      p.curveTo(55.9424f,16.2334f,56.2002f,16.2568f,56.458f,16.3154f);
			      p.curveTo(56.7158f,16.3623f,56.9385f,16.4092f,57.1143f,16.4561f);
			      p.moveTo(57.583f,21.999f);
			      p.moveTo(62.6338f,18.0732f);
			      p.lineTo(62.6338f,21.999f);
			      p.lineTo(64.7314f,21.999f);
			      p.lineTo(64.7314f,17.9678f);
			      p.curveTo(64.7314f,17.6982f,64.7197f,17.4404f,64.6963f,17.1943f);
			      p.curveTo(64.6846f,16.96f,64.6494f,16.7256f,64.5791f,16.5146f);
			      p.curveTo(64.6963f,16.4326f,64.8721f,16.3623f,65.0713f,16.3037f);
			      p.curveTo(65.2822f,16.2451f,65.4346f,16.2217f,65.54f,16.2217f);
			      p.curveTo(65.9619f,16.2217f,66.2666f,16.3623f,66.4424f,16.6436f);
			      p.curveTo(66.6182f,16.9248f,66.7002f,17.4053f,66.7002f,18.0732f);
			      p.lineTo(66.7002f,21.999f);
			      p.lineTo(68.7979f,21.999f);
			      p.lineTo(68.7979f,17.8271f);
			      p.curveTo(68.7979f,17.3115f,68.751f,16.8545f,68.6689f,16.4443f);
			      p.curveTo(68.5869f,16.0342f,68.4229f,15.6709f,68.2002f,15.3779f);
			      p.curveTo(67.9775f,15.0732f,67.6846f,14.8389f,67.2861f,14.6865f);
			      p.curveTo(66.9229f,14.5107f,66.4307f,14.4287f,65.8447f,14.4287f);
			      p.curveTo(65.5166f,14.4287f,65.1533f,14.4873f,64.7549f,14.6162f);
			      p.curveTo(64.3799f,14.7451f,64.0283f,14.9092f,63.7354f,15.1201f);
			      p.curveTo(63.5244f,14.9326f,63.2432f,14.7686f,62.9033f,14.6279f);
			      p.curveTo(62.5752f,14.499f,62.1416f,14.4287f,61.6025f,14.4287f);
			      p.curveTo(61.333f,14.4287f,61.0518f,14.4404f,60.7705f,14.4639f);
			      p.curveTo(60.4893f,14.4873f,60.2314f,14.5107f,59.9502f,14.5693f);
			      p.curveTo(59.6924f,14.6045f,59.4346f,14.6514f,59.1885f,14.71f);
			      p.curveTo(58.9658f,14.7568f,58.7549f,14.8154f,58.5674f,14.8506f);
			      p.lineTo(58.5674f,21.999f);
			      p.lineTo(60.665f,21.999f);
			      p.lineTo(60.665f,16.292f);
			      p.curveTo(60.7939f,16.2686f,60.9346f,16.2568f,61.0752f,16.2334f);
			      p.curveTo(61.2158f,16.2334f,61.3564f,16.2217f,61.4736f,16.2217f);
			      p.curveTo(61.8955f,16.2217f,62.2002f,16.3623f,62.3643f,16.6436f);
			      p.curveTo(62.5518f,16.9248f,62.6338f,17.4053f,62.6338f,18.0732f);
			      p.moveTo(69.6504f,21.999f);
			      p.moveTo(75.5654f,12.7529f);
			      p.lineTo(75.5654f,18.9756f);
			      p.curveTo(75.5654f,19.4561f,75.6123f,19.8896f,75.6943f,20.2881f);
			      p.curveTo(75.7881f,20.6748f,75.9404f,21.0146f,76.1748f,21.2959f);
			      p.curveTo(76.3975f,21.5654f,76.6904f,21.7764f,77.0654f,21.9404f);
			      p.curveTo(77.4521f,22.0811f,77.9092f,22.1631f,78.4951f,22.1631f);
			      p.curveTo(78.9404f,22.1631f,79.3154f,22.1279f,79.6318f,22.0576f);
			      p.curveTo(79.9365f,21.9873f,80.2178f,21.8818f,80.4756f,21.7881f);
			      p.lineTo(80.1826f,20.1592f);
			      p.curveTo(79.9834f,20.2295f,79.7725f,20.2881f,79.5146f,20.335f);
			      p.curveTo(79.292f,20.3701f,79.0576f,20.3936f,78.8232f,20.3936f);
			      p.curveTo(78.3662f,20.3936f,78.0498f,20.2646f,77.8857f,20.0068f);
			      p.curveTo(77.7451f,19.7373f,77.6631f,19.3857f,77.6631f,18.9521f);
			      p.lineTo(77.6631f,16.3389f);
			      p.lineTo(80.1826f,16.3389f);
			      p.lineTo(80.1826f,14.6045f);
			      p.lineTo(77.6631f,14.6045f);
			      p.lineTo(77.6631f,12.4248f);
			      p.lineTo(75.5654f,12.7529f);
			      p.moveTo(80.8652f,21.999f);
			      p.moveTo(88.7754f,18.2842f);
			      p.curveTo(88.7754f,17.71f,88.6816f,17.1709f,88.5059f,16.7021f);
			      p.curveTo(88.3184f,16.2217f,88.084f,15.8115f,87.7559f,15.4717f);
			      p.curveTo(87.4395f,15.1318f,87.0527f,14.8623f,86.6074f,14.6865f);
			      p.curveTo(86.1621f,14.499f,85.6699f,14.4053f,85.1426f,14.4053f);
			      p.curveTo(84.6152f,14.4053f,84.123f,14.499f,83.6777f,14.6865f);
			      p.curveTo(83.2441f,14.8623f,82.8574f,15.1318f,82.5293f,15.4717f);
			      p.curveTo(82.2129f,15.8115f,81.9668f,16.2217f,81.7676f,16.7021f);
			      p.curveTo(81.5918f,17.1709f,81.498f,17.71f,81.498f,18.2842f);
			      p.curveTo(81.498f,18.8584f,81.5801f,19.3975f,81.7676f,19.8896f);
			      p.curveTo(81.9434f,20.3701f,82.1895f,20.7803f,82.5059f,21.1318f);
			      p.curveTo(82.834f,21.46f,83.209f,21.7295f,83.6543f,21.917f);
			      p.curveTo(84.0996f,22.0928f,84.5918f,22.1982f,85.1426f,22.1982f);
			      p.curveTo(85.6934f,22.1982f,86.1973f,22.0928f,86.6309f,21.917f);
			      p.curveTo(87.0879f,21.7295f,87.4629f,21.46f,87.791f,21.1318f);
			      p.curveTo(88.1074f,20.7803f,88.3535f,20.3701f,88.5176f,19.8896f);
			      p.curveTo(88.6816f,19.3975f,88.7754f,18.8584f,88.7754f,18.2842f);
			      p.moveTo(86.6309f,18.2842f);
			      p.curveTo(86.6309f,18.9287f,86.502f,19.4443f,86.2324f,19.8311f);
			      p.curveTo(85.9863f,20.1943f,85.623f,20.3936f,85.1426f,20.3936f);
			      p.curveTo(84.6621f,20.3936f,84.2988f,20.1943f,84.0293f,19.8311f);
			      p.curveTo(83.7598f,19.4443f,83.6309f,18.9287f,83.6309f,18.2842f);
			      p.curveTo(83.6309f,17.6396f,83.7598f,17.1357f,84.0293f,16.7725f);
			      p.curveTo(84.2988f,16.3857f,84.6621f,16.1982f,85.1426f,16.1982f);
			      p.curveTo(85.623f,16.1982f,85.9863f,16.3857f,86.2324f,16.7725f);
			      p.curveTo(86.502f,17.1357f,86.6309f,17.6396f,86.6309f,18.2842f);
			      p.moveTo(89.3613f,21.999f);
			      p.moveTo(98.0742f,12.249f);
			      p.lineTo(95.9766f,12.249f);
			      p.curveTo(95.8828f,12.8936f,95.7891f,13.6201f,95.707f,14.4287f);
			      p.curveTo(95.6367f,15.2139f,95.5664f,16.0576f,95.5078f,16.9365f);
			      p.curveTo(95.4375f,17.792f,95.3906f,18.6709f,95.3438f,19.5381f);
			      p.curveTo(95.2852f,20.4053f,95.25f,21.2256f,95.2031f,21.999f);
			      p.lineTo(97.3359f,21.999f);
			      p.curveTo(97.3711f,21.0615f,97.4063f,20.0186f,97.4531f,18.9053f);
			      p.curveTo(97.5f,17.792f,97.5703f,16.667f,97.6641f,15.5303f);
			      p.curveTo(97.8281f,15.917f,98.0156f,16.3623f,98.2266f,16.8428f);
			      p.curveTo(98.4375f,17.3115f,98.6367f,17.792f,98.8359f,18.2725f);
			      p.curveTo(99.0469f,18.7529f,99.2344f,19.21f,99.4219f,19.6318f);
			      p.curveTo(99.6094f,20.0889f,99.7734f,20.4521f,99.9023f,20.7568f);
			      p.lineTo(101.438f,20.7568f);
			      p.curveTo(101.566f,20.4521f,101.73f,20.0889f,101.918f,19.6318f);
			      p.curveTo(102.094f,19.21f,102.293f,18.7529f,102.492f,18.2725f);
			      p.curveTo(102.703f,17.792f,102.902f,17.3115f,103.113f,16.8428f);
			      p.curveTo(103.324f,16.3623f,103.5f,15.917f,103.676f,15.5303f);
			      p.curveTo(103.77f,16.667f,103.84f,17.792f,103.887f,18.9053f);
			      p.curveTo(103.922f,20.0186f,103.969f,21.0615f,103.992f,21.999f);
			      p.lineTo(106.137f,21.999f);
			      p.curveTo(106.09f,21.2256f,106.043f,20.4053f,106.008f,19.5381f);
			      p.curveTo(105.949f,18.6709f,105.891f,17.792f,105.832f,16.9365f);
			      p.curveTo(105.762f,16.0576f,105.691f,15.2139f,105.609f,14.4287f);
			      p.curveTo(105.539f,13.6201f,105.457f,12.8936f,105.363f,12.249f);
			      p.lineTo(103.359f,12.249f);
			      p.curveTo(103.195f,12.5654f,102.996f,12.9404f,102.773f,13.3857f);
			      p.curveTo(102.563f,13.8545f,102.316f,14.3467f,102.094f,14.8857f);
			      p.curveTo(101.848f,15.4014f,101.613f,15.9521f,101.379f,16.5029f);
			      p.curveTo(101.145f,17.0537f,100.934f,17.5693f,100.723f,18.0615f);
			      p.curveTo(100.512f,17.5693f,100.301f,17.0537f,100.055f,16.5029f);
			      p.curveTo(99.8203f,15.9521f,99.5859f,15.4014f,99.3398f,14.8857f);
			      p.curveTo(99.1172f,14.3467f,98.8711f,13.8545f,98.6602f,13.3857f);
			      p.curveTo(98.4375f,12.9404f,98.2383f,12.5654f,98.0742f,12.249f);
			      p.moveTo(106.916f,21.999f);
			      p.moveTo(109.998f,21.999f);
			      p.lineTo(109.998f,14.6045f);
			      p.lineTo(107.9f,14.6045f);
			      p.lineTo(107.9f,21.999f);
			      p.lineTo(109.998f,21.999f);
			      p.moveTo(110.197f,12.46f);
			      p.curveTo(110.197f,12.085f,110.068f,11.7686f,109.811f,11.5576f);
			      p.curveTo(109.576f,11.335f,109.283f,11.2295f,108.943f,11.2295f);
			      p.curveTo(108.604f,11.2295f,108.311f,11.335f,108.064f,11.5576f);
			      p.curveTo(107.818f,11.7686f,107.689f,12.085f,107.689f,12.46f);
			      p.curveTo(107.689f,12.8467f,107.818f,13.1514f,108.064f,13.3623f);
			      p.curveTo(108.311f,13.585f,108.604f,13.7021f,108.943f,13.7021f);
			      p.curveTo(109.283f,13.7021f,109.576f,13.585f,109.811f,13.3623f);
			      p.curveTo(110.068f,13.1514f,110.197f,12.8467f,110.197f,12.46f);
			      p.moveTo(110.962f,21.999f);
			      p.moveTo(111.595f,18.2959f);
			      p.curveTo(111.595f,18.8467f,111.665f,19.374f,111.806f,19.8311f);
			      p.curveTo(111.958f,20.3115f,112.181f,20.7217f,112.509f,21.0732f);
			      p.curveTo(112.813f,21.4248f,113.212f,21.6943f,113.704f,21.8818f);
			      p.curveTo(114.196f,22.0928f,114.771f,22.1982f,115.45f,22.1982f);
			      p.curveTo(115.884f,22.1982f,116.294f,22.1514f,116.657f,22.0811f);
			      p.curveTo(117.021f,22.0107f,117.337f,21.917f,117.571f,21.8115f);
			      p.lineTo(117.278f,20.1006f);
			      p.curveTo(117.044f,20.1943f,116.774f,20.2881f,116.481f,20.3232f);
			      p.curveTo(116.177f,20.3701f,115.907f,20.3936f,115.661f,20.3936f);
			      p.curveTo(114.946f,20.3936f,114.454f,20.2178f,114.173f,19.8428f);
			      p.curveTo(113.868f,19.4795f,113.728f,18.9639f,113.728f,18.2959f);
			      p.curveTo(113.728f,17.6631f,113.892f,17.1592f,114.196f,16.7842f);
			      p.curveTo(114.489f,16.3975f,114.958f,16.1982f,115.579f,16.1982f);
			      p.curveTo(115.872f,16.1982f,116.118f,16.2334f,116.364f,16.292f);
			      p.curveTo(116.599f,16.3389f,116.81f,16.4092f,117.009f,16.4795f);
			      p.lineTo(117.442f,14.8154f);
			      p.curveTo(117.138f,14.6865f,116.81f,14.5693f,116.493f,14.5107f);
			      p.curveTo(116.165f,14.4287f,115.802f,14.4053f,115.403f,14.4053f);
			      p.curveTo(114.806f,14.4053f,114.267f,14.499f,113.798f,14.71f);
			      p.curveTo(113.341f,14.9209f,112.931f,15.1904f,112.603f,15.5537f);
			      p.curveTo(112.286f,15.8936f,112.028f,16.3154f,111.864f,16.7959f);
			      p.curveTo(111.677f,17.2646f,111.595f,17.7686f,111.595f,18.2959f);
			      p.moveTo(117.961f,21.999f);
			      p.moveTo(123.398f,16.4561f);
			      p.lineTo(123.762f,14.7217f);
			      p.curveTo(123.645f,14.6865f,123.504f,14.6396f,123.352f,14.6045f);
			      p.curveTo(123.188f,14.5693f,123.023f,14.5459f,122.871f,14.5107f);
			      p.curveTo(122.719f,14.499f,122.543f,14.4873f,122.391f,14.4639f);
			      p.curveTo(122.238f,14.4639f,122.098f,14.4404f,121.98f,14.4404f);
			      p.curveTo(121.348f,14.4404f,120.785f,14.499f,120.27f,14.6279f);
			      p.curveTo(119.766f,14.7451f,119.32f,14.8623f,118.945f,14.9912f);
			      p.lineTo(118.945f,21.999f);
			      p.lineTo(121.043f,21.999f);
			      p.lineTo(121.043f,16.3389f);
			      p.curveTo(121.137f,16.3154f,121.277f,16.292f,121.453f,16.2568f);
			      p.curveTo(121.641f,16.2451f,121.805f,16.2334f,121.922f,16.2334f);
			      p.curveTo(122.227f,16.2334f,122.484f,16.2568f,122.742f,16.3154f);
			      p.curveTo(123.0f,16.3623f,123.223f,16.4092f,123.398f,16.4561f);
			      p.moveTo(123.867f,21.999f);
			      p.moveTo(131.777f,18.2842f);
			      p.curveTo(131.777f,17.71f,131.684f,17.1709f,131.508f,16.7021f);
			      p.curveTo(131.32f,16.2217f,131.086f,15.8115f,130.758f,15.4717f);
			      p.curveTo(130.441f,15.1318f,130.055f,14.8623f,129.609f,14.6865f);
			      p.curveTo(129.164f,14.499f,128.672f,14.4053f,128.145f,14.4053f);
			      p.curveTo(127.617f,14.4053f,127.125f,14.499f,126.68f,14.6865f);
			      p.curveTo(126.246f,14.8623f,125.859f,15.1318f,125.531f,15.4717f);
			      p.curveTo(125.215f,15.8115f,124.969f,16.2217f,124.77f,16.7021f);
			      p.curveTo(124.594f,17.1709f,124.5f,17.71f,124.5f,18.2842f);
			      p.curveTo(124.5f,18.8584f,124.582f,19.3975f,124.77f,19.8896f);
			      p.curveTo(124.945f,20.3701f,125.191f,20.7803f,125.508f,21.1318f);
			      p.curveTo(125.836f,21.46f,126.211f,21.7295f,126.656f,21.917f);
			      p.curveTo(127.102f,22.0928f,127.594f,22.1982f,128.145f,22.1982f);
			      p.curveTo(128.695f,22.1982f,129.199f,22.0928f,129.633f,21.917f);
			      p.curveTo(130.09f,21.7295f,130.465f,21.46f,130.793f,21.1318f);
			      p.curveTo(131.109f,20.7803f,131.355f,20.3701f,131.52f,19.8896f);
			      p.curveTo(131.684f,19.3975f,131.777f,18.8584f,131.777f,18.2842f);
			      p.moveTo(129.633f,18.2842f);
			      p.curveTo(129.633f,18.9287f,129.504f,19.4443f,129.234f,19.8311f);
			      p.curveTo(128.988f,20.1943f,128.625f,20.3936f,128.145f,20.3936f);
			      p.curveTo(127.664f,20.3936f,127.301f,20.1943f,127.031f,19.8311f);
			      p.curveTo(126.762f,19.4443f,126.633f,18.9287f,126.633f,18.2842f);
			      p.curveTo(126.633f,17.6396f,126.762f,17.1357f,127.031f,16.7725f);
			      p.curveTo(127.301f,16.3857f,127.664f,16.1982f,128.145f,16.1982f);
			      p.curveTo(128.625f,16.1982f,128.988f,16.3857f,129.234f,16.7725f);
			      p.curveTo(129.504f,17.1357f,129.633f,17.6396f,129.633f,18.2842f);
			      p.moveTo(132.363f,21.999f);
			      p.moveTo(136.383f,21.999f);
			      p.lineTo(138.492f,21.999f);
			      p.curveTo(138.879f,21.1787f,139.254f,20.335f,139.641f,19.4561f);
			      p.curveTo(140.016f,18.5654f,140.367f,17.6982f,140.707f,16.8428f);
			      p.curveTo(141.047f,15.9756f,141.352f,15.1436f,141.633f,14.3584f);
			      p.curveTo(141.914f,13.5732f,142.172f,12.8701f,142.383f,12.249f);
			      p.lineTo(140.039f,12.249f);
			      p.curveTo(139.863f,12.8115f,139.652f,13.4209f,139.43f,14.0654f);
			      p.curveTo(139.219f,14.7217f,138.984f,15.3779f,138.75f,16.0225f);
			      p.curveTo(138.527f,16.667f,138.305f,17.2881f,138.07f,17.874f);
			      p.curveTo(137.859f,18.4717f,137.66f,18.9756f,137.484f,19.4092f);
			      p.curveTo(137.297f,18.9756f,137.086f,18.4717f,136.875f,17.874f);
			      p.curveTo(136.664f,17.2881f,136.441f,16.667f,136.219f,16.0225f);
			      p.curveTo(135.973f,15.3779f,135.75f,14.7217f,135.539f,14.0654f);
			      p.curveTo(135.316f,13.4209f,135.105f,12.8115f,134.918f,12.249f);
			      p.lineTo(132.504f,12.249f);
			      p.curveTo(132.715f,12.8701f,132.949f,13.5732f,133.23f,14.3584f);
			      p.curveTo(133.535f,15.1436f,133.84f,15.9756f,134.18f,16.8428f);
			      p.curveTo(134.52f,17.6982f,134.871f,18.5654f,135.246f,19.4561f);
			      p.curveTo(135.621f,20.335f,136.008f,21.1787f,136.383f,21.999f);
			      p.moveTo(142.471f,21.999f);
			      p.moveTo(146.842f,22.1279f);
			      p.curveTo(148.318f,22.1279f,149.42f,21.8701f,150.182f,21.3545f);
			      p.curveTo(150.908f,20.8271f,151.295f,20.0771f,151.295f,19.0459f);
			      p.curveTo(151.295f,18.542f,151.178f,18.085f,150.955f,17.6865f);
			      p.curveTo(150.732f,17.2764f,150.311f,16.96f,149.689f,16.7256f);
			      p.curveTo(150.428f,16.2686f,150.814f,15.6592f,150.814f,14.8857f);
			      p.curveTo(150.814f,14.3584f,150.709f,13.9248f,150.486f,13.585f);
			      p.curveTo(150.287f,13.2217f,149.994f,12.9404f,149.619f,12.7295f);
			      p.curveTo(149.232f,12.5186f,148.799f,12.3662f,148.271f,12.2607f);
			      p.curveTo(147.756f,12.1787f,147.182f,12.1436f,146.561f,12.1436f);
			      p.curveTo(146.104f,12.1436f,145.611f,12.1553f,145.072f,12.2139f);
			      p.curveTo(144.533f,12.2373f,144.041f,12.3076f,143.596f,12.3896f);
			      p.lineTo(143.596f,21.8467f);
			      p.curveTo(144.17f,21.9756f,144.721f,22.0459f,145.26f,22.0811f);
			      p.curveTo(145.775f,22.1162f,146.314f,22.1279f,146.842f,22.1279f);
			      p.moveTo(145.729f,17.7568f);
			      p.lineTo(147.111f,17.7568f);
			      p.curveTo(147.744f,17.7568f,148.225f,17.8506f,148.564f,18.0381f);
			      p.curveTo(148.904f,18.2256f,149.068f,18.5537f,149.068f,18.999f);
			      p.curveTo(149.068f,19.5146f,148.869f,19.8662f,148.459f,20.042f);
			      p.curveTo(148.061f,20.2295f,147.533f,20.3232f,146.877f,20.3232f);
			      p.curveTo(146.643f,20.3232f,146.432f,20.3232f,146.256f,20.3115f);
			      p.curveTo(146.068f,20.2998f,145.893f,20.2881f,145.729f,20.2646f);
			      p.lineTo(145.729f,17.7568f);
			      p.moveTo(145.729f,16.0811f);
			      p.lineTo(145.729f,13.9482f);
			      p.curveTo(145.904f,13.9365f,146.104f,13.9248f,146.314f,13.9248f);
			      p.curveTo(146.525f,13.9131f,146.736f,13.9131f,146.912f,13.9131f);
			      p.curveTo(147.486f,13.9131f,147.943f,13.9951f,148.236f,14.1475f);
			      p.curveTo(148.529f,14.2998f,148.693f,14.5811f,148.693f,14.9795f);
			      p.curveTo(148.693f,15.3545f,148.553f,15.6357f,148.248f,15.8115f);
			      p.curveTo(147.955f,15.9873f,147.475f,16.0811f,146.807f,16.0811f);
			      p.lineTo(145.729f,16.0811f);
			      p.moveTo(151.878f,21.999f);
			      p.moveTo(159.53f,21.999f);
			      p.lineTo(161.874f,21.999f);
			      p.curveTo(161.511f,20.9443f,161.159f,19.9717f,160.843f,19.0928f);
			      p.curveTo(160.515f,18.2021f,160.187f,17.3584f,159.87f,16.5732f);
			      p.curveTo(159.554f,15.7764f,159.249f,15.0381f,158.944f,14.3232f);
			      p.curveTo(158.628f,13.6201f,158.323f,12.9287f,157.995f,12.249f);
			      p.lineTo(155.897f,12.249f);
			      p.curveTo(155.581f,12.9287f,155.253f,13.6201f,154.948f,14.3232f);
			      p.curveTo(154.644f,15.0381f,154.339f,15.7764f,154.011f,16.5732f);
			      p.curveTo(153.706f,17.3584f,153.378f,18.2021f,153.062f,19.0928f);
			      p.curveTo(152.733f,19.9717f,152.382f,20.9443f,152.019f,21.999f);
			      p.lineTo(154.292f,21.999f);
			      p.curveTo(154.397f,21.6592f,154.526f,21.3076f,154.632f,20.9561f);
			      p.curveTo(154.772f,20.6045f,154.89f,20.2412f,155.019f,19.8896f);
			      p.lineTo(158.815f,19.8896f);
			      p.curveTo(158.944f,20.2412f,159.05f,20.6045f,159.19f,20.9561f);
			      p.curveTo(159.308f,21.3076f,159.437f,21.6592f,159.53f,21.999f);
			      p.moveTo(156.894f,14.4639f);
			      p.curveTo(156.952f,14.6045f,157.022f,14.792f,157.128f,15.0381f);
			      p.curveTo(157.222f,15.2725f,157.339f,15.5537f,157.444f,15.8818f);
			      p.curveTo(157.585f,16.1982f,157.714f,16.5615f,157.854f,16.9365f);
			      p.curveTo(157.995f,17.3115f,158.136f,17.7217f,158.288f,18.1436f);
			      p.lineTo(155.522f,18.1436f);
			      p.curveTo(155.675f,17.7217f,155.815f,17.3115f,155.956f,16.9365f);
			      p.curveTo(156.108f,16.5615f,156.237f,16.1982f,156.366f,15.8818f);
			      p.curveTo(156.495f,15.5537f,156.589f,15.2725f,156.683f,15.0381f);
			      p.curveTo(156.788f,14.792f,156.858f,14.6045f,156.894f,14.4639f);
			      p.moveTo(161.971f,21.999f);
			      return p;
			   }
   
		   
		}
	   

	}