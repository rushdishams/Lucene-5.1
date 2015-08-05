package org.apache.lucene.demo;

import javax.swing.JEditorPane; 
import javax.swing.JFrame; 
import javax.swing.JScrollPane;

public class ParagraphDisplay extends JFrame { 
	public void displayContent(String content) { 
		try { 

			JFrame frame = new JFrame( );
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			
			JEditorPane editorPane = new JEditorPane("text/html",content); 
			
			JScrollPane scrollPane = new JScrollPane(editorPane);
			frame.getContentPane().add(scrollPane);
			
			frame.pack( );
			frame.setVisible( true );
			frame.setSize(600, 600);
			
		} catch(Exception e) { 
			e.printStackTrace(); 
			System.out.println("Some problem has occured when rendering outptu"); 
		} 
	} 
}
