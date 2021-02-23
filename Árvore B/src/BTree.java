
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
				// Needs to split
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
		
		// After the possible SPLIT
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
					// Verifing if the current page is the root
					if (parent == null) {
						SplitHelper newSplitHelper = split(currentPage, splitHelper);
						
						// Creating the new page
						Page newPage = addNewPage(newSplitHelper);
						// Finding the page where the key of the first split is
						Page auxPage = findPage(newPage, splitHelper.getKey());
						// Adding the childrens of the child
						auxPage.addChild(splitHelper.getCurrentPage(), auxPage.findKeyOnPage(splitHelper.getKey()));
						auxPage.addChild(splitHelper.getRightChild(), auxPage.findKeyOnPage(splitHelper.getKey()) + 1);
						
						// Setting the newly created page as the root
						root = newPage;
						return null;
					} else {
						SplitHelper newSplitHelper = split(currentPage, splitHelper);
						int index;
						
						// Adding the key from the first split and its childrens
						if (splitHelper.getKey().getId() < newSplitHelper.getRightChild().getKey(0).getId()) {
							// Going to the current page
							// Adding the left page
							index = newSplitHelper.getCurrentPage().findKeyOnPage(splitHelper.getKey());
							newSplitHelper.getCurrentPage().addChild(splitHelper.getCurrentPage(), index);
						} else {
							// Going to the right child
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
				leftPage.addChild(currentPage.getChild(i + 1), addLeft + 1); // Needs fixing
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
	
	public Page findPage(Page currentPage, Data key) {
		Boolean flag = false;
		Page auxPage = null;
		int i = 0;
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			if (currentPage.getKey(i).getId() > key.getId()) {
				auxPage = currentPage.getChild(i);
				flag = true;
				break;
			}
		}
		
		if (!flag) {
			auxPage = currentPage.getChild(i);
		}
		
		return auxPage;
	}
	
	public Data getData(int id) {
		if (root != null)
			return getData(root, id);
			
		return null;
	}
	
	public Data getData(Page currentPage, int id) {
		Data key = null;
		Boolean flag = false;
		int i;
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			if (currentPage.getKey(i).getId() == id) {
				return currentPage.getKey(i);
			}
			
			if (currentPage.getKey(i).getId() > id) {
				if (currentPage.getChild(i) != null) {
					key = getData(currentPage.getChild(i), id);
					break;
				}
				flag = true;
			}
		}
		
		if (!flag) {
			if (currentPage.getChild(i) != null) {
				key = getData(currentPage.getChild(i), id);
			}
		}
		
		return key;
	}
	
	public void deleteData(int id) {
		deleteData(root, null, 0, id);
	}
	
	public Page deleteData(Page currentPage, Page parent, int pos, int id) {
		Boolean flag = false;
		int i;
		
		currentPage.refreshNKeys();
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			if (currentPage.getKey(i).getId() == id) {
				if (currentPage.isLeaf()) {
					// Delete and verify nKeys
					currentPage.removeKey(id);
					currentPage.refreshNKeys();
					
					// Verifies if the number of keys is lower than the minimum number of keys of the page
					if (currentPage.getnKeys() < Math.floor(degree / 2) && parent != null) {
						Boolean redistributed = redistribute(currentPage, parent, pos, i, id);
						
						// Needs merge
						if (!redistributed) {
							merge(currentPage, parent, pos, id);
						}
					}
				} else {
					// Delete and call findRightMost function
					Data key = findRightMost(currentPage.getChild(i), currentPage.getKey(i));
					System.out.println("The right most: " + key.getId());
					currentPage.addKey(key, i);
					
					deleteData(currentPage.getChild(i), currentPage, i, id);
				}
			} else if (currentPage.getKey(i).getId() > id) {
				if (currentPage.getChild(i) != null) {
					// Moving to the left child
					deleteData(currentPage.getChild(i), currentPage, i, id);
				} else {
					return null;
				}
				
				flag = true;
			}
		}
		
		if (!flag) {
			
		}
		
		return currentPage;
	}
	
	public Data findRightMost(Page currentPage, Data currentKey) {
		Data key = null;
		
		if (currentPage.isLeaf()) {
			key = currentPage.getKey(currentPage.getnKeys() - 1);
			currentPage.addKey(currentKey, currentPage.getnKeys() -1);
		} else {
			key = findRightMost(currentPage.getChild(currentPage.getnKeys()), currentKey);
		}
		
		return key;
	}
	
	public Data findRightMost(Page currentPage) {
		Data key = null;
		
		if (currentPage.isLeaf()) {
			return currentPage.getKey(currentPage.getnKeys() - 1);
		} else {
			key = findRightMost(currentPage.getChild(currentPage.getnKeys()));
		}
		
		return key;
	}
	
	// * Merge: merges two pages in to one 
	// ** Rule: the sum between the merger page and the merged page needs to be equals to nKeys - 1
	public void merge(Page currentPage, Page parent, int pos, int id) {
		System.out.println("Entrou na função de merge!");
		
		int parentPos;
		
		if (pos - 1 >= 0) {
			parentPos = pos - 1;
		} else {
			parentPos = 0;
		}
		
		if (parent.getChild(parentPos) != null) {
			// Adicionando todos os valores da página irmã na página atual
			for (int y = 0; y < parent.getChild(parentPos).getnKeys(); y++) {
				currentPage.addKey(parent.getChild(parentPos).getKey(y));
			}
			// Adicionando o valor do pai na página atual
			currentPage.addKey(parent.getKey(parentPos));
			// Removendo o valor que foi adicionado na página atual da página pai
			// e atualizando as posições
			parent.removeKey(parent.getKey(parentPos).getId());
			
			parent.refreshNKeys();
			return;
		}
		
		parentPos += 2;
		
		if (parent.getChild(parentPos) != null) {
			// Adicionando todos os valores da página irmã na página atual
			for (int y = 0; y < parent.getChild(parentPos).getnKeys(); y++) {
				currentPage.addKey(parent.getChild(parentPos).getKey(y));
			}
			// Adicionando o valor do pai na página atual
			currentPage.addKey(parent.getKey(pos));
			// Removendo o valor que foi adicionado na página atual da página pai
			// e atualizando as posições
			parent.removeKey(parent.getKey(pos).getId());
			
			parent.refreshNKeys();
			return;
		}
	}
	
	// * Redistribution: transfers a key from a page to another
	// ** Rule: the number of keys of the page that will transfer needs to be greater than the minimum value of keys
	public Boolean redistribute(Page currentPage, Page parent, int pos, int index, int id) {
		System.out.println("Entou na função de redistribuição");
		int parentPos;
		
		if (pos - 1 >= 0) {
			parentPos = pos - 1;
		} else {
			parentPos = 0;
		}
		
		if (parent.getChild(parentPos) != null) {
			if (parent.getChild(parentPos).getnKeys() > Math.floor(degree / 2)) {
				System.out.println("Irá receber do irmão da esquerda");
				// Adicionando o valor do pai na página atual
				currentPage.addKey(parent.getKey(parentPos), index);
				
				Data rightMost = findRightMost(parent.getChild(parentPos));
				// Adicionando o valor mais a direita da página irmã à esquerda no pai
				parent.addKey(rightMost, parentPos);
				// Removendo o valor da página irmã à esquerda
				parent.getChild(parentPos).removeKey(rightMost.getId());
				
				parent.refreshNKeys();
				parent.getChild(parentPos).refreshNKeys();
				return true;
			}
		}
		
		parentPos += 2;
		
		if (parent.getChild(parentPos) != null) {
			if (parent.getChild(parentPos).getnKeys() > Math.floor(degree / 2)) {
				System.out.println("Irá receber do irmão da direita");
				// Adicionando o valor do pai na página atual
				currentPage.addKey(parent.getKey(pos));
				// Adicionando o valor mais a esquerda da página irmã à direita no pai
				parent.addKey(parent.getChild(parentPos).getKey(0), pos);
				// Removendo o valor mais a esquerda da página irmã à direita
				parent.getChild(parentPos).removeKey(parent.getKey(pos).getId());
			
				parent.refreshNKeys();
				parent.getChild(parentPos).refreshNKeys();
				return true;
			}
		}
		
		return false;
	}
	
	public void inOrder() {
		//TODO
	}
	
	@Override
	public String toString() {
		return "* Root *" + root ;
	}
}
