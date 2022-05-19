package game.utility.input;

import java.io.Serializable;

import game.logics.records.Records;

//TODO
public class JSONReader extends JSONHandler implements Serializable {

    private static final long serialVersionUID = 1L;
/*
    private String name;
    private int age;
*/
    public JSONReader(final Records records) {
        super(records);
    }
/*
    /**
     * Get data from write
     *//*
    private void upload() {
        records.setName(this.name);
        records.setAge(this.age);
    }*/

    //TODO fix
	public void read() {
		// TODO Auto-generated method stub
		
	}
}
