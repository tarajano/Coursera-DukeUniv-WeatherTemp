
import edu.duke.*;

import org.apache.commons.csv.*;

public class GetMaxTemperature {

	/**
	 * @param args
	 */

	public static CSVRecord getHighestOfTwoTemps(CSVRecord max_temp_record, CSVRecord record) {
		double maxTemp = Double.parseDouble(max_temp_record.get("TemperatureF"));
		double recordTemp = Double.parseDouble(record.get("TemperatureF"));
		if(recordTemp > maxTemp){
			max_temp_record = record;
		}
		return max_temp_record;
	}
	public static CSVRecord getMaxTempInFile(CSVParser parser) {
		CSVRecord max_temp_record = null;
		for(CSVRecord record : parser){
			if(max_temp_record == null){
				max_temp_record = record;
			}else{
				max_temp_record = getHighestOfTwoTemps(max_temp_record, record);
			}
		}
		return max_temp_record;
	}
	public static void main(String[] args) {
		FileResource input_file = new FileResource("nc_weather/2015/weather-2015-01-01.csv");
		CSVParser file_parser = input_file.getCSVParser();
		CSVRecord max_temp_record = getMaxTempInFile(file_parser);
		System.out.println(max_temp_record.get("TimeEST")+" "+max_temp_record.get("TemperatureF"));
	}

}
