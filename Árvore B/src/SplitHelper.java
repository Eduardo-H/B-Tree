
public class SplitHelper {

	private Page rightChild;
	private Data newData;
	private Page currentPage;
	

	
	public SplitHelper(Page rightChild, Data newData, Page currentPage) {
		super();
		this.rightChild = rightChild;
		this.newData = newData;
		this.currentPage = currentPage;
	}

	public Page getRightChild() {
		return rightChild;
	}
	
	public void setRightChild(Page rightChild) {
		this.rightChild = rightChild;
	}
	
	public Data getNewData() {
		return newData;
	}
	
	public void setNewData(Data newData) {
		this.newData = newData;
	}
	
	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
	}

}
