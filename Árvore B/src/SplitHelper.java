
public class SplitHelper {

	private Page rightChild;
	private Data key;
	private Page currentPage;
	
	public SplitHelper(Page rightChild, Data key, Page currentPage) {
		super();
		this.rightChild = rightChild;
		this.key = key;
		this.currentPage = currentPage;
	}

	public Page getRightChild() {
		return rightChild;
	}
	
	public void setRightChild(Page rightChild) {
		this.rightChild = rightChild;
	}
	
	public Data getKey() {
		return key;
	}
	
	public void setKey(Data key) {
		this.key = key;
	}
	
	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
	}

}
