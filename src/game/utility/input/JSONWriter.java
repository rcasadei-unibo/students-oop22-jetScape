package game.utility.input;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import game.logics.records.Records;

public class JSONWriter implements Jsonable, Serializable {

	private static final long serialVersionUID = 1L;

	private final JsonObject json = new JsonObject();
	private final Records records;
	
	// Do not change path without updating .gitignore tracked file
	private static final String sep = File.separator;
	private static final String filePath = "res" + sep + "game" + sep + "records.json";
	private static final File file = new File(filePath);
	
    private final static Map<String,Object> recordsMap = new HashMap<>();
    private String name;
    private int age;
    
	public JSONWriter(final Records records) {
		this.records = records;
	}
	
	//TODO
	public void build() {
		Records.getKeySet().forEach(key -> {
			recordsMap.put(key, "ciao");
		});
	}
    
	/*
	public void scheduleWrite(final String key, final String value) {
		this.json.put(key, value);
	}

	public void scheduleWrite(final String key, final int value) {
		this.json.put(key, value);
	}*/
	
    /**
     * Send data to write
     */
	private void download() {
		this.name = records.getName(); 
		this.age = records.getAge(); 
	}
	
    /**
     * Get data from write
     */
	private void upload() {
		records.setName(this.name);
		records.setAge(this.age);
	}

	/**
	 * Get informations that have to be written to file
	 */
	public void read() {


	}
	
	/**
	 * Write informations passed by {@linkplain game.utility.input.JsonWriter}
	 */
	public void write() {
		this.download();
		try (
			final FileWriter fw = new FileWriter(file);
		) {
			this.toJson(fw);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
        }
        return writable.toString();
	}

	//TODO
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void toJson(final Writer writer) throws IOException {
		
		Records.getKeySet().forEach(key -> {
			json.put(key, recordsMap.get(key));
		});
		
        json.put("name", name);
        json.put("age", age);
        // json.put("position", this.getPosition());
        // json.put("skills", this.getSkills());
        // json.put("salary", this.getSalary());
        
        // Writes the object physically
        json.toJson(writer);
	}
}
