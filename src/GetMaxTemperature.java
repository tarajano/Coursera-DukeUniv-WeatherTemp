
import edu.duke.*;
import java.io.*;
import org.apache.commons.csv.*;

public class GetMaxTemperature {

	/**
	 * @param args
	 */
	
	
	// check method below
	public static double averageTemperatureByHumidityInFile(CSVParser parser, double humidity){
		int count = 0;
		double summation = 0;
		for(CSVRecord record : parser){
			if(Integer.parseInt(record.get("Humidity")) >= humidity){
				count++;
				summation = summation + Double.parseDouble(record.get("TemperatureF"));
			}
		}
		if(count > 0){
			return summation / count;
		}else{
			return -1;
		}
	}
	public static CSVRecord getMinHumidityInMultipleFiles() {
		//Here
		CSVRecord min_humidity_record = null;
		CSVRecord record = null;
		DirectoryResource dr = new DirectoryResource();
		for (File file : dr.selectedFiles()){
			FileResource input_file = new FileResource(file);
			CSVParser file_parser = input_file.getCSVParser();
			record = getLowestHumidityInFile(file_parser);
			if(min_humidity_record == null){
				min_humidity_record = record;
			}else{
				min_humidity_record = getLowestOfTwoRecords(min_humidity_record, record, "Humidity"); //here
			}
		}
		return min_humidity_record;
	}
	public static CSVRecord getLowestHumidityInFile(CSVParser parser) {
		CSVRecord min_humidity_record = null;
		for(CSVRecord record : parser){
			if(min_humidity_record == null){
				min_humidity_record = record;
			}else{
				min_humidity_record = getLowestOfTwoRecords(min_humidity_record, record, "Humidity");
			}
		}
		return min_humidity_record;
	}
	public static CSVRecord getLowestOfTwoRecords(CSVRecord min_attribute_record, CSVRecord record, String attribute) {
		if(min_attribute_record.get(attribute).equalsIgnoreCase("N/A") && record.get(attribute).equalsIgnoreCase("N/A")){
			return min_attribute_record;
		}else if(min_attribute_record.get(attribute).equalsIgnoreCase("N/A")){
			return record;
		}else if(record.get(attribute).equalsIgnoreCase("N/A")){
			return min_attribute_record;
		}	
		double min_attribute = Double.parseDouble(min_attribute_record.get(attribute));
		double record_attribute = Double.parseDouble(record.get(attribute));
		if(record_attribute < -999){
			return min_attribute_record;
		}
		if(record_attribute < min_attribute){
			min_attribute_record = record;
		}
		return min_attribute_record;
	}
	public static String fileWithColdestTemperature(){
		double current_lowest_temp = 99999;
		DirectoryResource dr = new DirectoryResource();
		String coldest_file = "";
		for (File current_file : dr.selectedFiles()) {
			FileResource input_file = new FileResource(current_file);
			CSVParser parser = input_file.getCSVParser();
			CSVRecord record = getMinTempInFile(parser);
			double current_temp = Double.parseDouble(record.get("TemperatureF"));
			if(current_temp < current_lowest_temp){
				coldest_file = current_file.getName();
				current_lowest_temp = current_temp;				
			}
		}
		return coldest_file;
	}
	public static CSVRecord getLowestOfTwoTemps(CSVRecord min_temp_record, CSVRecord record) {
		double minTemp = Double.parseDouble(min_temp_record.get("TemperatureF"));
		double recordTemp = Double.parseDouble(record.get("TemperatureF"));
		if(recordTemp < -999){
			return min_temp_record;
		}
		if(recordTemp < minTemp){
			min_temp_record = record;
		}
		return min_temp_record;
	}
	public static CSVRecord getMinTempInFile(CSVParser parser) {
		CSVRecord min_temp_record = null;
		for(CSVRecord record : parser){
			if(min_temp_record == null){
				min_temp_record = record;
			}else{
				min_temp_record = getLowestOfTwoTemps(min_temp_record, record);
			}
		}
		return min_temp_record;
	}
	public static CSVRecord getMaxTempInMultipleFiles() {
		CSVRecord max_temp_record = null;
		CSVRecord record = null;
		DirectoryResource dr = new DirectoryResource();
		for (File file : dr.selectedFiles()){
			FileResource input_file = new FileResource(file);
			CSVParser file_parser = input_file.getCSVParser();
			record = getMaxTempInFile(file_parser);
			if(max_temp_record == null){
				max_temp_record = record;
			}else{
				max_temp_record = getHighestOfTwoTemps(max_temp_record, record);
			}
		}
		return max_temp_record;
	}
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
	//Tester Methods
	public static void testGetMaxTempSingleFile() {
		FileResource input_file = new FileResource("nc_weather/2015/weather-2015-01-01.csv");
		CSVParser file_parser = input_file.getCSVParser();
		CSVRecord max_temp_record = getMaxTempInFile(file_parser);
		System.out.println(max_temp_record.get("TimeEST")+" "+max_temp_record.get("TemperatureF"));	
	}
	public static void testGetMaxTempMultipleFiles() {
		CSVRecord max_temp_record = getMaxTempInMultipleFiles();
		System.out.println(max_temp_record.get("DateUTC")+" -- "+max_temp_record.get("TemperatureF"));
	}
	public static void testGetMinTempSingleFile() {
		FileResource input_file = new FileResource("nc_weather/2015/weather-2015-02-05.csv");
		CSVParser file_parser = input_file.getCSVParser();
		CSVRecord min_temp_record = getMinTempInFile(file_parser);
		System.out.println(min_temp_record.get("TimeEST")+" "+min_temp_record.get("TemperatureF"));
	}
	public static void testFileWithColdestTemperature() {
		System.out.println("File with coldest temperature: "+fileWithColdestTemperature());
	}
	public static void testGetLowestHumidityInFile() {
		FileResource fr = new FileResource();
		CSVParser parser = fr.getCSVParser();
		CSVRecord csv = getLowestHumidityInFile(parser);
		System.out.println("Lowest Humidity was: "+csv.get("Humidity")+" at "+csv.get("DateUTC"));
	}
	public static void testGetMinHumidityInMultipleFiles() {
		CSVRecord record = getMinHumidityInMultipleFiles();
		System.out.println("Lowest Humidity was: "+record.get("Humidity")+" at "+record.get("DateUTC"));
	}	
	public static void testAverageTemperatureByHumidityInFile(){
		// nc_weather/2014/weather-2014-03-20.csv // Negative test
		// nc_weather/2014/weather-2014-01-20.csv // Positive test
		FileResource fr = new FileResource("nc_weather/2014/weather-2014-03-20.csv"); 
		CSVParser parser = fr.getCSVParser();
		int humidity = 80;
		double ave_temp = averageTemperatureByHumidityInFile(parser, humidity);
		if(ave_temp != -1){
			System.out.println("When Humidity is over " + humidity + ", the average temperature is: " + ave_temp + ".");
		}else{
			System.out.println("No temperatures with that humidity.");
		}
	}
	
	
	//Main
	public static void main(String[] args) {
		// Get Max Temperature from Singles File
		//testGetMaxTempSingleFile();
		
		// Get Min Temperature from Single File
		//testGetMinTempSingleFile();
		
		// Get Max Temperature from Multiple Files
		//testGetMaxTempMultipleFiles();
		
		// Retrieving name of the file with the lowest temperature
		//testFileWithColdestTemperature();
		
		// Retrieving lowest Humidity from Single File
		//testGetLowestHumidityInFile();
		
		// Get Min Humidity from Multiple Files
		//testGetMinHumidityInMultipleFiles();
		
		//  Average temperature for records where humidity >= X value 
		testAverageTemperatureByHumidityInFile();
		
	}

}
