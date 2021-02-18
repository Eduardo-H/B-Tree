
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
			if (root.getnKeys() == degree) {
				// Split function
				SplitHelper splitHelper = split(root, null, new SplitHelper(null, key, root));
				Page newPage = new Page(degree, false);
				
				newPage.addKey(splitHelper.getNewData());
				newPage.addChild(splitHelper.getCurrentPage(), 0);
				newPage.addChild(splitHelper.getRightChild(), 1);
				root = newPage;
			} else {
				addOnLeaf(root, null, key);
			}
		} else {
			insert(root, null, key);
		}
	}
	
	public Page insert(Page currentPage, Page parent, Data key) {
		if (currentPage.isLeaf()) {
			System.out.println(currentPage);
			SplitHelper splitHelper = addOnLeaf(currentPage, parent, key);
			
			if (splitHelper == null) {
				return null;
			} else {
				if (currentPage.getnKeys() < degree) {
					currentPage.addSplitHelper(splitHelper);
				} else {
					parent.addSplitHelper(splitHelper);
					parent.addChild(splitHelper.getCurrentPage(), parent.findKey(splitHelper.getNewData()));
					return null;
				}
			}
		}
		
		int i;
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			// Verifica se o valor atual é maior que o novo a ser inserido
			// Sees if the current key value is grater than the new key value
			if (currentPage.getKey(i).getId() > key.getId()) {
				insert(currentPage.getChild(i), currentPage, key);
				break;
			}
		}
		
		// Verifica se o valor a ser inserido é maior que todos os presentes na página atual
		if (i == currentPage.getnKeys() && !currentPage.isLeaf()) {
			insert(currentPage.getChild(i), currentPage, key);
		} else if (i == currentPage.getnKeys() && currentPage.isLeaf()) {
			SplitHelper newSplitHelper = split(currentPage, parent, new SplitHelper(null, key, currentPage));
		}
		
		return currentPage;
	}
	
	public SplitHelper addOnLeaf(Page currentPage, Page parent, Data key) {
		if (currentPage.getnKeys() < degree) {
			currentPage.addKey(key);
			return null;
		} else {
			return split(currentPage, parent, new SplitHelper(null, key, currentPage));
		}
	}
	
	public SplitHelper split(Page currentPage, Page parent, SplitHelper splitHelper) {
		int pos = degree / 2;
		System.out.println(currentPage.getKey(pos));
		Data insertOnParent = currentPage.getKey(pos);
		
		Page leftPage = new Page(degree, currentPage.isLeaf());
		Page rightPage = new Page(degree, currentPage.isLeaf());
		
		int addLeft = 0, addRight = 0;
		
		// Verificando qual chave vai para o filho da esquerda e qual vai para o da direita
		for (int i = 0; i < currentPage.getnKeys(); i++) {
			if (currentPage.getKey(i).getId() < insertOnParent.getId()) {
				leftPage.addKey(currentPage.getKey(i));
				leftPage.addChild(currentPage.getChild(i), addLeft);
				addLeft++;
			} else if (currentPage.getKey(i).getId() > insertOnParent.getId()) {
				rightPage.addKey(currentPage.getKey(i));
				rightPage.addChild(currentPage.getChild(i), addRight);
				addRight++;
			}
		}
		
		// Adicionando o novo valor
		if (splitHelper.getNewData().getId() < rightPage.getKey(0).getId()) {
			if (splitHelper.getRightChild() != null) {
				leftPage.addSplitHelper(splitHelper);
			} else {
				leftPage.addKey(splitHelper.getNewData());
			}
		} else {
			if (splitHelper.getRightChild() != null) {
				rightPage.addSplitHelper(splitHelper);
			} else {
				rightPage.addKey(splitHelper.getNewData());
			}
		}

		SplitHelper newSplitHelper = new SplitHelper(rightPage, insertOnParent, leftPage);
		
		return newSplitHelper;
	}
	
	public boolean isFull(Page page) {
		if (page.getnKeys() == degree) {
			return true;
		}
		
		return false;
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
	
	@Override
	public String toString() {
		return "* Root *" + root ;
	}
}
