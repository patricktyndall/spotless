package data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	String filePath;
	
	public Parser(String filePath){
		this.filePath = filePath;
	}
	
	public List<Pair> parse(){
		File file = new File(filePath);
		
		List<Pair> results = new ArrayList<Pair>();

		return results; // TODO
		
	}
	
	

}
