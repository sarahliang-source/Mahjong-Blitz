package mahjong;

public class Tile {

	private String classType;
	private String value;
	
	//Tile constructor creates an empty tile object.
	public Tile() {
		classType = "?";
		value = "?";
	}
	
	//This compares the values of two tiles and returns true if the tile this method is called on is greater than the parameter tile.
	public boolean isGreaterThan(Tile tile1) {
		int thisValue  = Integer.parseInt(value);
		int otherValue = Integer.parseInt(tile1.getValue());
		if(thisValue>otherValue) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setClassType(String setClassType) {
		classType = setClassType;
	}
	
	public void setValue(String setValue) {
		value = setValue;
	}
	
	public String getClassType() {
		return classType;
	}
	
	public String getValue() {
		return value;
	}
	
	//If the tile is non-suited, this will return just the value of the tile. If the tile is suited, this will return the value and suit of the tile.
	public String toString() {
		if(classType == "Other") {
			return (value);
		} else {
			return (value + " " + classType);
		}
	}
}
