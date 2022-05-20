package game.utility.input;

import java.io.Serializable;

import game.logics.records.Records;

/**
 * This class reads information from a JSON file.
 */
public class JSONReader extends JSONHandler implements Serializable {

    private static final long serialVersionUID = 1L;
/*
    private String name;
    private int age;
*/
    /**
     * Builds a {@link JSONReader}.
    *
    * @param records records to send read data
    */
    public JSONReader(final Records records) {
        super(records);
    }

    /**
     * Send read data to {@link JSONHandler}.
     */
    private void upload() {
        //TODO add proper setters

        //records.setName(this.name);
        //records.setAge(this.age);
    }

    /**
     * Read informations written by {@link JSONWriter}.
     */
    //TODO write method
    public void read() {

        //TODO read from JSON

       this.upload();
    }
}
