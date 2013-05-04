//package screen;
//import javax.media.opengl.GL;
//import javax.media.opengl.GL2;
//import javax.media.opengl.GLAutoDrawable;
//import javax.media.opengl.GLEventListener;
//import javax.media.opengl.GLProfile;
//import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.awt.GLCanvas;
//import javax.swing.JFrame;
//
//import java.awt.BorderLayout;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
///**
// * A minimal program that draws with JOGL in a Swing JFrame using the AWT GLCanvas.
// *
// * @author Wade Walker
// */
//public class OneTriangleSwingGLCanvas {
//
//    public static void main( String [] args ) {
//
//        final GLCanvas glcanvas = new GLCanvas();
//
//        
//        glcanvas.addGLEventListener( new GLEventListener() {
//            
//            @Override
//            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
////                OneTriangle.setup( glautodrawable.getGL().getGL2(), width, height );
//            }
//            
//            @Override
//            public void init( GLAutoDrawable glautodrawable ) {
//            }
//            
//            @Override
//            public void dispose( GLAutoDrawable glautodrawable ) {
//            }
//            
//            @Override
//            public void display( GLAutoDrawable glAD ) {
//            	GL2 g = glAD.getGL().getGL2();
//                g.glClear( GL.GL_COLOR_BUFFER_BIT );
//                g.glPushAttrib(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
//                g.glColor3d(0.5, 0.5, 0.5);
//            	g.glRecti(100, 100, 200, 200);
////                OneTriangle.render( g.getGL().getGL2(), g.getWidth(), g.getHeight() );
//            }
//        });
//
//        final JFrame jframe = new JFrame( "One Triangle Swing GLCanvas" );
//        jframe.addWindowListener( new WindowAdapter() {
//            public void windowClosing( WindowEvent windowevent ) {
//                jframe.dispose();
//                System.exit( 0 );
//            }
//        });
//
//        jframe.getContentPane().add( glcanvas );
//        jframe.setSize( 640, 480 );
//        jframe.setVisible( true );
//    }
//}