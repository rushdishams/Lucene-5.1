package org.apache.lucene.demo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
 
public class LuceneHighlighter{
    public static String highlight(String pText, String pQuery) throws Exception{	
    	
    	
    	SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style='background-color: yellow'>", "</span>");
    	Analyzer analyzer = new StandardAnalyzer();
    	QueryParser parser = new QueryParser("contents", analyzer);
	
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(parser.parse(pQuery)));
        highlighter.setTextFragmenter(new NullFragmenter());
 
        String text = highlighter.getBestFragment(analyzer, "", pText); 
 
        if (text != null){
            return text;
        }
        return pText;    
    }
 
    public static void main(String[] args){

        	File file = new File ("filesToIndex/AntioxidantReport.txt");
        	String text = null;

        	try {
				text = FileUtils.readFileToString(file);
			} catch (IOException e) {
				System.out.println("Error reading file");
			}
        	
            try {
            	File outputfile = new File("1.html");
            	String output = highlight(text, "antioxidant");
            	FileUtils.write(outputfile, output);
				System.out.println(highlight(text, "management"));
            	//highlight(text, "management");
			} catch (Exception e) {
				System.out.println("Error highlighting text");
			}
       
    }// end main

    
}//end class