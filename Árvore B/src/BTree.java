
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
				
				root = addNewPage(splitHelper);
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
					System.out.println("Split made, current page is not full. Key to be added: " + splitHelper.getKey());
					if (splitHelper.getRightChild() != null) {
						currentPage.addSplitHelper(splitHelper);
					} else {
						currentPage.addKey(splitHelper.getKey());
					}
					
					return new SplitHelper(null, null, currentPage);
				} else {
					// Verifing if the current page is the root
					if (parent == null) {
						SplitHelper newSplitHelper = split(currentPage, splitHelper);
						
						// Creating the new page
						Page newPage = addNewPage(newSplitHelper);
						
						// Finding the page where the key of the first split is
						flag = false;
						Page auxPage = null;
						
						for (i = 0; i < newPage.getnKeys(); i++) {
							if (newPage.getKey(i).getId() > key.getId()) {
								auxPage = newPage.getChild(i);
								flag = true;
								break;
							}
						}
						
						if (!flag) {
							auxPage = newPage.getChild(i);
						}
						
						// Adding the childrens of the child
						auxPage.addChild(splitHelper.getCurrentPage(), auxPage.findKeyOnPage(splitHelper.getKey()));
						auxPage.addChild(splitHelper.getRightChild(), auxPage.findKeyOnPage(splitHelper.getKey()) + 1);
						
						// Setting the newly created page as the root
						root = newPage;
						return null;
					} else {
						SplitHelper newSplitHelper = split(currentPage, splitHelper);
						int index;						
						
						System.out.println("Split made, current page is full. Key to be added: " + splitHelper.getKey());
						
						// Adding the key from the first split and its childrens
						if (splitHelper.getKey().getId() < newSplitHelper.getRightChild().getKey(0).getId()) {
							// Going to the current page
							// newSplitHelper.getCurrentPage().addSplitHelper(splitHelper);
							// Adding the left page
							index = newSplitHelper.getCurrentPage().findKeyOnPage(splitHelper.getKey());
							newSplitHelper.getCurrentPage().addChild(splitHelper.getCurrentPage(), index);
						} else {
							// Going to the right child
							// newSplitHelper.getRightChild().addSplitHelper(splitHelper);
							// Adding the left page
							index = newSplitHelper.getRightChild().findKeyOnPage(splitHelper.getKey());
							newSplitHelper.getRightChild().addChild(splitHelper.getCurrentPage(), index);
						}
						
						
						currentPage = newSplitHelper.getCurrentPage();
						
						return newSplitHelper;
					}
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
				leftPage.addChild(currentPage.getChild(i + 1), addLeft + 1); // To be fixed
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
	
	public Page addNewPage(SplitHelper splitHelper) {
		Page newPage = new Page(degree, false);
		
		newPage.addKey(splitHelper.getKey());
		newPage.addChild(splitHelper.getCurrentPage(), 0);
		newPage.addChild(splitHelper.getRightChild(), 1);
		
		return newPage;
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
