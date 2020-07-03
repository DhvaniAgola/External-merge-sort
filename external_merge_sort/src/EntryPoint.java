import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Group 3
 *
 */
public class EntryPoint {

	public static final List<File> tmpfilesList = new ArrayList<File>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File ouputfile = new File(Constant.OUTPUT_FILE);
		Constant.START_TIME = System.currentTimeMillis();
		performSortAndGenTempFiles();
		sortAndMergeTempFiles(ouputfile);
		Constant.END_TIME = System.currentTimeMillis();
		double mins = (double) (Constant.END_TIME - Constant.START_TIME) / (1000 * 60);
		System.out.println("======= Execution Time =======" + "\n");
		System.out.println("In Seconds : " + (Constant.END_TIME - Constant.START_TIME)/1000);
		System.out.println("In Minutes : " + mins);
		System.out.println("==============================");
	}

	/**
	 * 
	 */
	public static void performSortAndGenTempFiles() {
		ReaderClass.Reader s;
		try {
			s = new ReaderClass.Reader(Constant.INPUT_FILE);
			int noOfTuples = s.nextInt();
			Constant.NO_OF_TUPLES = noOfTuples;
			s.nextInt(); // Memory From File
			s.nextInt(); // Blank Space
			int countOfChunks = 0;
			int[] array=null;
			int count=0;
			int ss = numbers_IN_Block(noOfTuples);
			System.out.println(ss);
			array = new int[ss<noOfTuples?ss:(noOfTuples)];
			while (--noOfTuples>ss) {
				if (count < ss) {
					array[count++] = s.nextInt();
				} else {
					writeToFile(++countOfChunks, array);
					System.out.println("/** Data Written Successfully to tmp_" + countOfChunks + ".txt file **/");
					//array = new int[ss<noOfTuples?ss:(noOfTuples+1)];
					count=0;
					System.gc();
					array[count++] = s.nextInt();
				}
			}
			
			array = Arrays.copyOfRange(array, 0, count);
			Arrays.sort(array);
			writeToFile(++countOfChunks,array);
			
			array= null;
			System.gc();
			array = new int[noOfTuples+1];
			for(int i=0;i<=noOfTuples;i++) {
				array[i] = s.nextInt();
			}
			if (array!=null) {
				writeToFile(++countOfChunks, array);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeToFile(int countOfChunks,int[] array) throws IOException {
		System.out.println("Chunk No : " + countOfChunks + " Data (bytes) : " + array.length + " Processed !!");
		System.gc();  
		System.out.println("Preparing to write data on temp_" + countOfChunks + ".txt file");
		Arrays.sort(array);
		try(BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter(Constant.TEMP_PREFIX + countOfChunks + ".txt"));PrintWriter out = new PrintWriter(bufferedWriter)){
			
			tmpfilesList.add(new File(Constant.TEMP_PREFIX + countOfChunks + ".txt"));
			for(int i=0;i<array.length;i++) {
				out.write(String.valueOf(array[i]) + System.lineSeparator());
			}
			out.flush();
			array=null;
			System.gc();
		}
	}

	private static int numbers_IN_Block(int noOfTuples) {
		if (noOfTuples < Constant.NO_OF_INT_IN_BLOCK) {
			return Constant.NO_OF_INT_IN_BLOCK = noOfTuples;
		} else {
			return (int) (Constant.NO_OF_INT_IN_BLOCK * ((double) Constant.MAIN_MEMORY_SIZE / Constant.BLOCK_SIZE)
					* ((double) Constant.MEMORY_UTILIZATION / 100));
		}
	}

	/**
	 * 
	 * @param ouputfile
	 */
	public static void sortAndMergeTempFiles(File ouputfile) {
		try(DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(ouputfile))) {
			ArrayList<DataInputStream> readers = new ArrayList<DataInputStream>(tmpfilesList.size());
			ArrayList<Integer> intList = new ArrayList<Integer>();
			String line;
			for (File f : tmpfilesList) {
				DataInputStream din = new DataInputStream(new FileInputStream(f));
				String start_point = String.valueOf(din.readLine());
				if (start_point != null) {
					readers.add(din);
					intList.add(Integer.valueOf(start_point));
				}
			}
			
			while (!readers.isEmpty()) {
				try {
					int minIndex = intList.indexOf(Collections.min(intList));
					DataInputStream din = readers.get(minIndex);
					dataoutputstream.writeUTF(intList.get(minIndex).toString());
					dataoutputstream.writeUTF("\n");
					dataoutputstream.flush();
					if ((line = din.readLine()) != null) {
						Integer next = Integer.parseInt(line);
						intList.set(minIndex, next);
					} else {
						din.close();
						intList.remove(minIndex);
						readers.remove(minIndex);
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
