import java.io.*;

/**
 * Script to change old opencobra hosting location to 
 * new king.nuigalway.ie location.
 * 
 * java FixPrinceLink <file-or-dir>
 */
public class FixPrinceLink {

	public static void main (String[] args) throws Exception {
		for (String arg : args) {
			if ( ! arg.startsWith("--") ) {
				fixFile(new File(arg));
			}
		}
	}


	private static void fixFile (File file) throws Exception {
	
		System.err.println ("fixing " + file.getPath());

		if ( file.isDirectory() ) {
			fixDirectory(file);
			return;
		}

		FileReader r = new FileReader(file);
		BufferedReader bf = new BufferedReader(r);

		File tmpFile = new File(file.getParent(), file.getName() + ".tmp" );
		FileWriter w = new FileWriter(tmpFile);

		String line;

		while ( (line=bf.readLine()) != null) {
			line  = line.replaceAll("prince.lcsb.uni.lu", "king.nuigalway.ie");
			w.write(line + "\n");
		}
		w.close();
		bf.close();

		// Copy modified file back over source file
		// tmpFile.renameTo(file);
		// In order to preserve file flags, will overwrite original
		// instead of renaming tmp file.
		//
		r = new FileReader(tmpFile);
		bf = new BufferedReader(r);

		// Open with option to overwrite
		w = new FileWriter (file, false);
		while ( (line=bf.readLine()) != null) {
			w.write(line + "\n");
		}
		w.close();
		bf.close();
		tmpFile.delete();
	}

	private static void fixDirectory (File dir) throws Exception {

		System.err.println ("directory: " + dir.getPath());

		if ( ! dir.isDirectory() ) {
			return;
		}

		File[] files = dir.listFiles();

		// empty dir
		if (files == null) {
			return;
		}

		for (File file : files) {
			fixFile (file);
		}
	}


}

