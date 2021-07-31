package org.freehep.graphicsio.ppt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.freehep.graphicsio.AbstractVectorGraphicsIO;

public class PPTGraphics2D extends AbstractVectorGraphicsIO  {
	private OutputStream ros;
	protected PrintStream os;
	private final double r2d = 180./Math.PI;
	
	Component component;
	
	String array = "path";
	int arraynum = 0;
	
    private String getNewPathName() {
		arraynum++;
		return array+arraynum;
	}
    
    private String getCurrentPathName() {
    	return array+arraynum;
    }
    
//    @Override
//	public void scale(double sx, double sy) {
//		//super.scale(sx, sy);
//    	this.sx = sx;
//    	this.sy = sy;
//	}

    /*
     * ================================================================================
     * 1. Constructors & Factory Methods
     * ================================================================================
     */


	public PPTGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public PPTGraphics2D(OutputStream os, Component component) {
        super(component, false);
        this.component = component;
        init(os);
    }

	private void init(OutputStream os) {
		ros = os;
	}

	@Override
	public void writeHeader() throws IOException {
		os = new PrintStream(ros, true);
		os.print(
			"Sub Java2PPT()\n" +
			"Dim MyPPT\n" +
			"Dim MySlides\n" +
			"Dim MySlide\n" +
			"Dim MyShapes\n" +
			"Dim MyPath\n" +
			"Dim MyShape\n" +
			"Dim MyFill\n" +
			"Dim Color1\n" +
			"Dim Color2\n" +
			"Dim MyLine\n" +
			"Set MyPPT = Application.ActivePresentation\n" +
			"Set MySlides = MyPPT.Slides\n" + 
			"Set MySlide = MySlides.Item(1)\n" +
			"Set MyShapes = MySlide.Shapes\n"
		);
	}

	@Override
	public void writeBackground() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTrailer() throws IOException {
		os.print("End Sub\n");
	}

	@Override
	public void closeStream() throws IOException {
		os.close();
	}

	@Override
	public void writeComment(String comment) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeGraphicsSave() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeGraphicsRestore() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeImage(RenderedImage image, AffineTransform xform, Color bkg) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeString(String string, double x, double y) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeTransform(AffineTransform transform) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeClip(Shape shape) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeSetClip(Shape shape) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writePaint(Color color) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writePaint(GradientPaint paint) throws IOException {
		
	}

	@Override
	protected void writePaint(TexturePaint paint) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writePaint(Paint paint) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeFont(Font font) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Graphics create() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void setPaintMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXORMode(Color c1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Shape s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fill(Shape s) {
		//writepath(s);
		buildFreeform(s);
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Graphics create(double x, double y, double width, double height) {
		//return new PPTGraphics2D(ros, component);
		return this;
	}
	
	float[] c = new float[6];
	
    private void buildFreeform(Shape original) {
        if (original == null) {
            os.print("null");
            return;
        }
        Shape s = getTransform().createTransformedShape(original);
        PathIterator i = s.getPathIterator(null);
        if (i.isDone()) return;
        
        int type, j;
        float[] c = new float[6];

        float xprev = 0f, yprev = 0f, x0 = 0f, y0 = 0f, x1,y1,x2,y2, xc=0f, yc=0f;
        
        i = s.getPathIterator(null);
        if ( i.currentSegment(c) !=  PathIterator.SEG_MOVETO) return;
        os.print(
        	"Set MyPath = MyShapes.BuildFreeform(0, " +  
        	(x0 = xprev = c[0]) + ", " +  (y0 = yprev = c[1]) + ")\n"
        );
        i.next();
        boolean closepath = false;
        boolean alreadyclosed = false;
        while (!i.isDone()) {
            type = i.currentSegment(c);
            switch (type) {
            case PathIterator.SEG_MOVETO:
            	if (closepath && (( xprev != xc ) || ( yprev != yc )) )
	            	os.print(
		                	"MyPath.AddNodes 0, 0, " +  xc + ", " + yc + "\n"
		            );
            	//if (( xprev != x0 ) || ( yprev != y0 )) 
            	if ( !alreadyclosed )
	            	os.print(
	                	"MyPath.AddNodes 0, 0, " +  x0 + ", " + y0 + "\n"
	                );
            	// no break, since movetos inside a path are actually
            	// just a lineto in PPT
            	alreadyclosed = false;
	            closepath = true;
            	xc = c[0]; yc = c[1];
            case PathIterator.SEG_LINETO:
            	os.print(
            		"MyPath.AddNodes 0, 0, " +  
            		(xprev = c[0]) + ", " + (yprev = c[1]) + "\n"
            	);
                break;
            case PathIterator.SEG_CUBICTO:
            	os.print(
                	"MyPath.AddNodes 1, 1, " +  
                	c[0] + ", " +  c[1] + ", " + c[2] + ", " + c[3] + ", " +
                	(xprev = c[4]) + ", " + (yprev = c[5]) + "\n"
                );            	
                break;
            case PathIterator.SEG_QUADTO:
            	x1 = c[0] + (xprev - c[0]) / 3f;
            	y1 = c[1] + (yprev - c[1]) / 3f;
            	x2 = c[0] + (c[2] - c[0])  / 3f;
            	y2 = c[1] + (c[3] - c[1])  / 3f;
            	os.print(
                    "MyPath.AddNodes 1, 1, " +  
                    x1 + ", " +  y1 + ", " + x2 + ", " + y2 + ", " +
                    (xprev = c[2]) + ", " + (yprev = c[3]) + "\n"
                );
            	break;
            case PathIterator.SEG_CLOSE:
            	if (closepath) {
	            	os.print(
		                "MyPath.AddNodes 0, 0, " +  xc + ", " + yc + "\n"
		            );
	            	closepath = false;
            	}
            	alreadyclosed = true;
            	if (( xprev == x0 ) && ( yprev == y0 )) break;
            	os.print(
                	"MyPath.AddNodes 0, 0, " +  x0 + ", " + y0 + "\n"
                );         	
                break;
            default:
                break;
            }
            i.next();
        }
        os.print("Set MyShape = MyPath.ConvertToShape()\n");
		os.print(fill());
		//os.print("shape.Line.Visible = False\n");
		os.print("Set MyLine = MyShape.Line\n");
		os.print("MyLine.Visible = False\n");
    }
	
	private void writepath(Shape original) {
		Shape s = getTransform().createTransformedShape(original);
	    PathIterator i = s.getPathIterator(null);
	    int type;
	    boolean itsline = true;
        while (!i.isDone()) {
        	type = i.currentSegment(c);
        	if ( type == PathIterator.SEG_CUBICTO ||
        		 type == PathIterator.SEG_QUADTO
        	) {
        		itsline = false;
        		break;
        	}
        	i.next();
        }
        if (itsline) writeline(s);
        else write(s);
	}
	
	private String fill() {
		String s = "";
		double angle;
		Paint p = getPaint();
		if ( p instanceof GradientPaint) {
			GradientPaint g = (GradientPaint) p;
			//s  = "With shape.Fill\n";
			//s += "    .TwoColorGradient 2, 1" + "\n";
			s = "Set MyFill = MyShape.Fill\n";
			s += "MyFill.TwoColorGradient 2, 1\n";
			Point2D p1, p2;
			p1 = g.getPoint1();
			p2 = g.getPoint2();
			if (p1.getX() != p2.getX()) {
				angle = 90. - Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()) * r2d;
			}
			else angle = 90.;
			s += "MyFill.GradientAngle = " + ((float) angle) + "\n";
			//s += "    .ForeColor.RGB = " + RGB(g.getColor1()) + "\n";
			//s += "    .BackColor.RGB = " + RGB(g.getColor2()) + "\n";
			//s += "End With\n";
			s += "Set Color1 = MyFill.ForeColor\n";
			s += "Set Color2 = MyFill.BackColor\n";
			s += "Color1.RGB = " + RGB(g.getColor1()) + "\n";
			s += "Color2.RGB = " + RGB(g.getColor2()) + "\n";
			return s;
		}
		if ( p instanceof Color) {
			//s +=  "shape.Fill.ForeColor.RGB = " + RGB((Color) p) + "\n";
			s = "Set MyFill = MyShape.Fill\n";
			s += "Set Color1 = MyFill.ForeColor\n";
			s += "Color1.RGB = " + RGB((Color) p) + "\n";
		}
		return s;
	}
	
	private String RGB(Color c) {
		//String s = "RGB(";
		//s = s + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")";
		//return s;
		return ""+ (c.getRed() | (c.getGreen()<<8) | (c.getBlue()<<16));
	}
	
    private void write(Shape s) {
        if (s == null) {
            os.print("null");
            return;
        }
        String name = getNewPathName();
        PathIterator i = s.getPathIterator(null);
        int type, j;
        int size = 0;
        int index = 0;
        float[] c = new float[6];
        while (!i.isDone()) {
        	type = i.currentSegment(c);
        	if (type != PathIterator.SEG_MOVETO) size += 3;
        	else size++;
        	i.next();
        }
        float xprev = 0f, yprev = 0f, x0 = 0f, y0 = 0f, x1,y1,x2,y2;
        i = s.getPathIterator(null);

        os.print("Dim "+ name + "(1 To " + size + ", 1 To 2)\n");
        
        while (!i.isDone()) {
            type = i.currentSegment(c);
            index++;
            switch (type) {
            case PathIterator.SEG_MOVETO:
                os.print(name + "(" + index + ", 1) = " + (x0 = xprev = c[0]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (y0 = yprev = c[1]) + "\n");
                break;
            case PathIterator.SEG_LINETO:
                os.print(name + "(" + index + ", 1) = " + xprev + "\n");
                os.print(name + "(" + index + ", 2) = " + yprev + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + c[0] + "\n");
                os.print(name + "(" + index + ", 2) = " + c[1] + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + (xprev = c[0]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (yprev = c[1]) + "\n");
                break;
            case PathIterator.SEG_CUBICTO:
                os.print(name + "(" + index + ", 1) = " + c[0] + "\n");
                os.print(name + "(" + index + ", 2) = " + c[1] + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + c[2] + "\n");
                os.print(name + "(" + index + ", 2) = " + c[3] + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + (xprev = c[4]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (yprev = c[5]) + "\n");            	
                break;
//                controlPoint[0] = points[0] + (lastPoint[0] - points[0]) / 3.;
//                controlPoint[1] = points[1] + (lastPoint[1] - points[1]) / 3.;
//                controlPoint[2] = points[0] + (points[2] - points[0]) / 3.;
//                controlPoint[3] = points[1] + (points[3] - points[1]) / 3.;
//                pc.cubic(controlPoint[0], controlPoint[1], controlPoint[2],
//                        controlPoint[3], points[2], points[3]);
//                lastPoint[0] = points[2];
//                lastPoint[1] = points[3];
            case PathIterator.SEG_QUADTO:
            	x1 = c[0] + (xprev - c[0]) / 3f;
            	y1 = c[1] + (yprev - c[1]) / 3f;
            	x2 = c[0] + (c[2] - c[0])  / 3f;
            	y2 = c[1] + (c[3] - c[1])  / 3f;
                os.print(name + "(" + index + ", 1) = " + x1 + "\n");
                os.print(name + "(" + index + ", 2) = " + y1 + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + x2 + "\n");
                os.print(name + "(" + index + ", 2) = " + y2 + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + (xprev = c[2]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (yprev = c[3]) + "\n");            	
                break;
            case PathIterator.SEG_CLOSE:
                os.print(name + "(" + index + ", 1) = " + xprev + "\n");
                os.print(name + "(" + index + ", 2) = " + yprev + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + x0 + "\n");
                os.print(name + "(" + index + ", 2) = " + y0 + "\n");
                index++;
                os.print(name + "(" + index + ", 1) = " + x0 + "\n");
                os.print(name + "(" + index + ", 2) = " + y0 + "\n");            	
                break;
            default:
                break;
            }
            i.next();
        }
        os.print(
           //"Set shape = myDocument.Shapes.AddCurve(SafeArrayOfPoints:=" + name + ")" + "\n"
        	"Set MyShape = MyShapes.AddCurve(" + name + ")\n"
        );
    }
    

	private void writeline(Shape s) {
        if (s == null) {
            os.print("null");
            return;
        }
        PathIterator i = s.getPathIterator(null);
        String name = getNewPathName();
        int type;
        int size = 0;
        int index = 0;
        float[] c = new float[6];
        while (!i.isDone()) {
        	size++;
        	i.next();
        }
        float xprev = 0f, yprev = 0f, x0 = 0f, y0 = 0f;
        i = s.getPathIterator(null);

        os.print("Dim "+ name + "(1 To " + size + ", 1 To 2)\n");
        
        while (!i.isDone()) {
            type = i.currentSegment(c);
            index++;
            switch (type) {
            case PathIterator.SEG_MOVETO:
                os.print(name + "(" + index + ", 1) = " + (x0 = xprev = c[0]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (y0 = yprev = c[1]) + "\n");
                break;
            case PathIterator.SEG_LINETO:
                os.print(name + "(" + index + ", 1) = " + (xprev = c[0]) + "\n");
                os.print(name + "(" + index + ", 2) = " + (yprev = c[1]) + "\n");
                break;
            case PathIterator.SEG_CLOSE:
                os.print(name + "(" + index + ", 1) = " + x0 + "\n");
                os.print(name + "(" + index + ", 2) = " + y0 + "\n");            	
                break;
            default:
                break;
            }
            i.next();
        }
        os.print(
            //"Set shape = myDocument.Shapes.AddPolyline(SafeArrayOfPoints:=" + name + ")" + "\n"
            "Set MyShape = MyShapes.AddPolyline(" + name + ")\n"
        );
    }

}
