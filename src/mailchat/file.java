/*****************
 Mailchat File
 By JIN Lei
 13120017 
 
 ***************/
package mailchat;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class file {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
	}

	// private static String path = "txt/";
	private static String filenameTemp;

	public static boolean creatTxtFile(String name) throws IOException {
		boolean mark = false;
		filenameTemp = name + ".txt";
		File filename = new File(filenameTemp);
		if (!filename.exists()) {
			filename.createNewFile();
			mark = true;
		}
		return mark;
	}

	public static boolean writeTxtFile(String newStr) throws IOException {
	
		boolean mark = false;
		String temp = "";

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
		
			filenameTemp = "test.txt";
			File file = new File(filenameTemp);
		
			StringBuffer buf = new StringBuffer();
			System.out.println(filenameTemp);
			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			// buf.append(newStr);
			pw.println(newStr);
			System.out.println("*********" + "\n" + newStr);
			pw.flush();
			mark = true;
		} catch (IOException e1) {
			throw e1;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}

		}
		return mark;
	}

	public void readData1() {
		try {
			FileReader read = new FileReader(filenameTemp);
			BufferedReader br = new BufferedReader(read);
			String row;
			while ((row = br.readLine()) != null) {
				System.out.println(row);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readDate() {
		String strs = "";
		try {
			FileReader read = new FileReader(new File(filenameTemp));
			StringBuffer sb = new StringBuffer();
			Scanner cinScanner = new Scanner(read);

			while (cinScanner.hasNext()) {
				strs = strs.concat(cinScanner.nextLine() + '\n');
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strs;
	}

}