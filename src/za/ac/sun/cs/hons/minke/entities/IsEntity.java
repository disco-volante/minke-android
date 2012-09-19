package za.ac.sun.cs.hons.minke.entities;

public abstract class IsEntity {
	protected Long ID;

	public long getID() {
		if(ID == null){
			setID(0);
		}
		return ID;

	}

	public void setID(long ID) {
		this.ID = ID;
	}

}
