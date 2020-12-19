
public class BTree {
	
	private Page root;
	private int degree;
	
	
	public BTree(int degree) {
		super();
		this.degree = degree;
	}
	
	public void insert(Data key) {
		if (root == null) {
			root = new Page(degree, true);
			root.addKey(key);
			return;
		}
		if (root.isLeaf()) {
			addOnLeaf(root, key);
		}else {
			//TODO: navegar até a folha onde vai ser adiconado
			//chamar addOnLeaf
		}
	}
	
	public void addOnLeaf(Page page, Data key) {
		if(page.getnKeys()<degree) {
			page.addKey(key);
			return;
		}
		else {
			page = split(page, key);
			return;
		}
	}
	
	public Page split(Page currentPage, Data key) {
		Page p0 = new Page(degree, false);
		Page p1 = currentPage;
		Page p2 = new Page(degree, p1.isLeaf());
		
		//TODO
		
		
		p0.addChild(p1, 0);
		p0.addChild(p2, 1);
		
		//TODO
		
		return p0;
	}
	
	public void getData(int id) {
		
	}
	
	public void deleteData(int id) {
		
	}
	
	public Data findRightMost(Page page) {
		//TODO: 
		return null;
	}
	
	public void merge() {
		//TODO
	}
	
	public void redistribute() {
		//TODO
	}
	
	public void inOrder() {
		//TODO
	}

}
