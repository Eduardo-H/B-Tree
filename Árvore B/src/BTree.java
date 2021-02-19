
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
				SplitHelper splitHelper = split(root, new SplitHelper(null, key, root));
				Page newPage = new Page(degree, false);
				
				newPage.addKey(splitHelper.getKey());
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
	
	public SplitHelper insert(Page currentPage, Page parent, Data key) {
		SplitHelper splitHelper = null;
		
		if (currentPage.isLeaf()) {
			splitHelper = addOnLeaf(currentPage, parent, key);
			
			if (splitHelper == null) {
				return null;
			} else {
				currentPage = splitHelper.getCurrentPage();
				return splitHelper;
			}
		}
		
		int i;
		Boolean flag = false;
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			// Verifies if the current key value is grater 
			// than the new key value
			if (currentPage.getKey(i).getId() > key.getId()) {
				splitHelper = insert(currentPage.getChild(i), currentPage, key);
				if (splitHelper != null) {
					if (splitHelper.getKey() != null) {
						currentPage.addChild(splitHelper.getCurrentPage(), i);
					}
				}
				flag = true;
				break;
			}
		}
		
		// Verifies if the value to be added is greater 
		// than all existing in the current page
		if (!flag && !currentPage.isLeaf()) {
			splitHelper = insert(currentPage.getChild(i), currentPage, key);
			if (splitHelper != null) {
				if (splitHelper.getKey() != null) {
					currentPage.addChild(splitHelper.getCurrentPage(), i);
				}
			}
		}
		
		// After possible SPLIT
		
		if (splitHelper != null) {
			if (splitHelper.getKey() != null) {
				if (currentPage.getnKeys() < degree) {
					if (splitHelper.getRightChild() != null) {
						currentPage.addSplitHelper(splitHelper);
					} else {
						currentPage.addKey(splitHelper.getKey());
					}
					
					return new SplitHelper(null, null, currentPage);
				} else {
					SplitHelper newSplitHelper = split(currentPage, splitHelper);
					currentPage = newSplitHelper.getCurrentPage();
					return newSplitHelper;
				}
			}
		}
		
		return splitHelper;
	}
	
	public SplitHelper addOnLeaf(Page currentPage, Page parent, Data key) {
		if (currentPage.getnKeys() < degree) {
			currentPage.addKey(key);
			return null;
		} else {
			return split(currentPage, new SplitHelper(null, key, currentPage));
		}
	}
	
	public SplitHelper split(Page currentPage, SplitHelper splitHelper) {
		int pos = degree / 2;
		Data insertOnParent = currentPage.getKey(pos);
		
		Page leftPage = new Page(degree, currentPage.isLeaf());
		Page rightPage = new Page(degree, currentPage.isLeaf());
		
		int addLeft = 0, addRight = 0;
		
		// Verifing wich key goes to the left page 
		// and wich goes to the right page
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
		
		// Adding the new value
		if (splitHelper.getKey().getId() < rightPage.getKey(0).getId()) {
			if (splitHelper.getRightChild() != null) {
				leftPage.addSplitHelper(splitHelper);
			} else {
				leftPage.addKey(splitHelper.getKey());
			}
		} else {
			if (splitHelper.getRightChild() != null) {
				rightPage.addSplitHelper(splitHelper);
			} else {
				rightPage.addKey(splitHelper.getKey());
			}
		}

		SplitHelper newSplitHelper = new SplitHelper(rightPage, insertOnParent, leftPage);
		
		leftPage.refreshNKeys();
		rightPage.refreshNKeys();
		
		return newSplitHelper;
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
