package offline;

import java.io.BufferedReader;
import java.io.FileReader;

import com.mongodb.MongoClient;

public class WordCount {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient();
		// MongoDatabase db = mongoClient.getDatabase(DBUtil.DB_NAME);
		// The name of the file to open.
		// Windows is different : C:\\Documents\\ratings_Musical_Instruments.csv
		String fileName = "C:\\Users\\lei_d\\Downloads\\speech.txt";

		String line = null;
		StringBuilder sb = new StringBuilder();

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				// String[] values = line.split(" ");
				sb.append(line).append("\n");

			}
			System.out.println("Import Done!");

			mongoClient.close();
			bufferedReader.close();

			String document = sb.toString();

			// map and reduce

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
