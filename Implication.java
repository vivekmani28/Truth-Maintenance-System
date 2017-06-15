
public class Implication {
	String parent;
	String child;
	boolean isAnd;
	int parentIndex;
	String otherParent;

	String rawString;
	
	public Implication(String parent, String child, boolean isAnd, int parentIndex, String rawString) {
		this.parent = parent;
		this.child = child;
		this.isAnd = isAnd;
		this.parentIndex = parentIndex;
		this.rawString = rawString;
		this.otherParent = null;
	}
	public Implication(String parent, String child, boolean isAnd, int parentIndex, String rawString,String otherParent) {
		this.parent = parent;
		this.child = child;
		this.isAnd = isAnd;
		this.parentIndex = parentIndex;
		this.rawString = rawString;
		this.otherParent = otherParent;
	}
	public String getotherParent() {
		return otherParent;
	}

	public void setotherParent(String otherParent) {
		this.otherParent = otherParent;
	}
	
	public String getRawString() {
		return rawString;
	}

	public void setRawString(String rawString) {
		this.rawString = rawString;
	}

	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public boolean isAnd() {
		return isAnd;
	}
	public void setAnd(boolean isAnd) {
		this.isAnd = isAnd;
	}
	public int getParentIndex() {
		return parentIndex;
	}
	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}
}
