/**
 * @author Group 3
 *
 */
public class Constant {

	/**
	 * CONFIGURATION PARAMS
	 * Main Memory : 2 MB = 1572864 bytes
	 * Records : 1000000
	 */
	
	public static int NO_OF_TUPLES;
	public static final int INT_BYTES = Integer.BYTES;
	public static int NO_OF_INT_IN_BLOCK = 800;
	public static final int BLOCK_SIZE = 4000;
	public static final int MAIN_MEMORY_SIZE = 5000000;
	public static final int MEMORY_UTILIZATION = 80;
	
	/**
	 * INPUT , OUTPUT , TEMP FILES
	 */
	public static final String OUTPUT_FILE = "output.txt";
//	public static final String INPUT_FILE = "smallInput.txt";
	public static final String INPUT_FILE = "input_1_1M_5mb.txt";
	public static final String TEMP_PREFIX = "tmp_";

	/**
	 * START TIME , END TIME 
	 */
	public static long START_TIME;
	public static long END_TIME;
}
