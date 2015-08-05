package org.apache.lucene.demo;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;








import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JRadioButton;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFilesGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static boolean create;
	private static String indexPath = "C:/Users/rushdi.shams/eclipse/workspace/Lucene-5.1/index/";
	private static String docsPath = "";
	private static File docDirectory;
	private static JButton btnDirectoryToIndex;
	private static JTextArea textArea;
	private static JRadioButton rdbtnUpdate; 
	private static JRadioButton rdbtnCreate;
	private static ButtonGroup group;
	private static JFileChooser chooser;
	private static JButton btnIndex;
	private static JTextArea progress;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexFilesGUI window = new IndexFilesGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IndexFilesGUI() {
		initialize();
	}

	public static class btnDirecotyAction extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;

		public void actionPerformed (ActionEvent e){
			chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = chooser.showOpenDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				docDirectory = chooser.getSelectedFile();
				//		        	System.out.println(docDirectory.toString());
				textArea.setText(docDirectory.getName());
				docsPath = docDirectory.toString();
				indexPath = docsPath;
				//System.out.println(docsPath);
			}
		}
	}

	public static class btnIndexAction extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;

		public void actionPerformed (ActionEvent e){

			if (group.getSelection().getActionCommand().equals("0")){
				create = false;
			}
			else{
				create = true;
			}

			System.out.println(create);

			Date start = new Date();
			try {
				System.out.println("Indexing to directory '" + indexPath + "'...");

				Directory dir = FSDirectory.open(Paths.get(indexPath));
				Analyzer analyzer = new StandardAnalyzer();
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

				if (create) {
					// Create a new index in the directory, removing any
					// previously indexed documents:
					iwc.setOpenMode(OpenMode.CREATE);
				} else {
					// Add new documents to an existing index:
					iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
				}

				// Optional: for better indexing performance, if you
				// are indexing many documents, increase the RAM
				// buffer.  But if you do this, increase the max heap
				// size to the JVM (eg add -Xmx512m or -Xmx1g):
				//
				// iwc.setRAMBufferSizeMB(256.0);

				IndexWriter writer = new IndexWriter(dir, iwc);
				final Path docDir = Paths.get(docsPath);
				indexDocs(writer, docDir);

				// NOTE: if you want to maximize search performance,
				// you can optionally call forceMerge here.  This can be
				// a terribly costly operation, so generally it's only
				// worth it when your index is relatively static (ie
				// you're done adding documents to it):
				//
				// writer.forceMerge(1);

				writer.close();

				Date end = new Date();
				System.out.println(end.getTime() - start.getTime() + " total milliseconds");

			} catch (IOException e1) {
				System.out.println(" caught a " + e1.getClass() +
						"\n with message: " + e1.getMessage());
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnDirectoryToIndex = new JButton("Directory to Index");

		btnDirectoryToIndex.setBounds(10, 24, 137, 23);
		frame.getContentPane().add(btnDirectoryToIndex);

		textArea = new JTextArea();

		textArea.setBounds(179, 24, 220, 53);
		frame.getContentPane().add(textArea);

		rdbtnUpdate = new JRadioButton("Update");
		rdbtnUpdate.setSelected(false);


		rdbtnCreate = new JRadioButton("Create");
		rdbtnCreate.setSelected(true);


		rdbtnUpdate.setBounds(10, 54, 109, 23);
		frame.getContentPane().add(rdbtnUpdate);

		rdbtnCreate.setBounds(10, 87, 109, 23);
		frame.getContentPane().add(rdbtnCreate);


		group = new ButtonGroup();
		group.add(rdbtnUpdate);
		group.add(rdbtnCreate);

		rdbtnUpdate.setActionCommand("0");
		rdbtnCreate.setActionCommand("1");


		btnIndex = new JButton("Index");

		btnIndex.setBounds(154, 122, 89, 23);
		frame.getContentPane().add(btnIndex);

		progress = new JTextArea();
		progress.setBounds(10, 156, 389, 94);
		frame.getContentPane().add(progress);

		btnDirectoryToIndex.addActionListener(new btnDirecotyAction()) ;

		/*rdbtnUpdate.addActionListener(new rdbtnUpdateAction());
		rdbtnCreate.addActionListener(new rdbtnCreateAction());*/


		btnIndex.addActionListener(new btnIndexAction()); 

	}
	/**
	 * Indexes the given file using the given writer, or if a directory is given,
	 * recurses over files and directories found under the given directory.
	 * 
	 * NOTE: This method indexes one document per input file.  This is slow.  For good
	 * throughput, put multiple documents into your input file(s).  An example of this is
	 * in the benchmark module, which can create "line doc" files, one document per line,
	 * using the
	 * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 *  
	 * @param writer Writer to the index where the given file/dir info will be stored
	 * @param path The file to index, or the directory to recurse into to find files to index
	 * @throws IOException If there is a low-level I/O error
	 */
	static void indexDocs(final IndexWriter writer, Path path) throws IOException {
		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
					} catch (IOException ignore) {
						// don't index files that can't be read.
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		}
	}

	/** Indexes a single document */
	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		if (file.toString().endsWith(".txt")){
			try (InputStream stream = Files.newInputStream(file)) {
				// make a new, empty document
				Document doc = new Document();

				// Add the path of the file as a field named "path".  Use a
				// field that is indexed (i.e. searchable), but don't tokenize 
				// the field into separate words and don't index term frequency
				// or positional information:
				Field pathField = new StringField("path", file.toString(), Field.Store.YES);
				doc.add(pathField);

				// Add the last modified date of the file a field named "modified".
				// Use a LongField that is indexed (i.e. efficiently filterable with
				// NumericRangeFilter).  This indexes to milli-second resolution, which
				// is often too fine.  You could instead create a number based on
				// year/month/day/hour/minutes/seconds, down the resolution you require.
				// For example the long value 2011021714 would mean
				// February 17, 2011, 2-3 PM.
				doc.add(new LongField("modified", lastModified, Field.Store.NO));

				// Add the contents of the file to a field named "contents".  Specify a Reader,
				// so that the text of the file is tokenized and indexed, but not stored.
				// Note that FileReader expects the file to be in UTF-8 encoding.
				// If that's not the case searching for special characters will fail.
				doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

				if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					// New index, so we just add the document (no old document can be there):
					System.out.println("adding " + file);
					progress.setText("adding " + file);
					writer.addDocument(doc);
				} else {
					// Existing index (an old copy of this document may have been indexed) so 
					// we use updateDocument instead to replace the old one matching the exact 
					// path, if present:
					System.out.println("updating " + file);
					progress.setText("updating " + file);
					writer.updateDocument(new Term("path", file.toString()), doc);
				}
			}
		}
	}
}
