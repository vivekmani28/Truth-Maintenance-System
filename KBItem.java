
public class KBItem {
	String value;
	int parentIndex;
	int otherParentIndex;
	
	public KBItem(String value, int parentIndex, int otherParentIndex) {
		this.value = value;
		this.parentIndex = parentIndex;
		this.otherParentIndex = otherParentIndex;
	}
	
	public int getOtherParentIndex() {
		return otherParentIndex;
	}

	public void setOtherParentIndex(int otherParentIndex) {
		this.otherParentIndex = otherParentIndex;
	}

	public KBItem(String value, int parentIndex) {
		this.value = value;
		this.parentIndex = parentIndex;
		this.otherParentIndex = -1;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getParentIndex() {
		return parentIndex;
	}
	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}
}
