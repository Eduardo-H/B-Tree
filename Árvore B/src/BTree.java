
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
				
				newPage.addKey(splitHelper.getNewData());
				newPage.addChild(splitHelper.getCurrentPage(), 0);
				newPage.addChild(splitHelper.getRightChild(), 1);
				root = newPage;
			} else {
				addOnLeaf(root, null, key);
			}
		} else {
			SplitHelper splitHelper = insert(root, null, key);
			
			if (splitHelper != null) {
				if (splitHelper.getNewData() != null) {
					if (root.getnKeys() < degree) {
						if (splitHelper.getRightChild() != null) {
							System.out.println("Entrando no Split Helper do root " + key.getId());
							root.addSplitHelper(splitHelper);
						} else {
							root.addKey(splitHelper.getNewData());
						}
					} else {
						SplitHelper newSplitHelper = split(root, new SplitHelper(null, key, root));
						Page newPage = new Page(degree, false);
						
						newPage.addKey(newSplitHelper.getNewData());
						newPage.addChild(newSplitHelper.getCurrentPage(), 0);
						newPage.addChild(newSplitHelper.getRightChild(), 1);
						
						root = newPage;
					}
				}
				
				return;
			}
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
				System.out.println("Current PAGE" + currentPage);
				System.out.println("Right Child" + splitHelper.getRightChild());
				return splitHelper;
			}
		}
		
		int i;
		Boolean flag = false;
		
		for (i = 0; i < currentPage.getnKeys(); i++) {
			// Verifica se o valor atual é maior que o novo a ser inserido
			// Sees if the current key value is grater than the new key value
			if (currentPage.getKey(i).getId() > key.getId()) {
				splitHelper = insert(currentPage.getChild(i), currentPage, key);
				if (splitHelper != null) {
					currentPage = splitHelper.getCurrentPage();
				}
				flag = true;
				break;
			}
		}
		
		// Verifica se o valor a ser inserido é maior que todos os presentes na página atual
		if (!flag && !currentPage.isLeaf()) {
			splitHelper = insert(currentPage.getChild(i), currentPage, key);
			if (splitHelper != null) {
				currentPage = splitHelper.getCurrentPage();
			}
		}
		
		// Após o possível SPLIT
		
		if (splitHelper != null) {
			if (splitHelper.getNewData() != null) {
				System.out.println("Tem um valor a ser adicionado no pai (resultante do split)");
				if (currentPage.getnKeys() < degree) {
					if (splitHelper.getRightChild() != null) {
						System.out.println("A página atual não está lotada e o valor a ser adicionado tem filho a direita");
						currentPage.addSplitHelper(splitHelper);
					} else {
						System.out.println("A página atual não está lotada e o valor a ser adicionado não tem filho a direita");
						currentPage.addKey(splitHelper.getNewData());
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
				System.out.println("Entrando no split helper da esquerda " + splitHelper.getNewData().getId());
				leftPage.addSplitHelper(splitHelper);
			} else {
				leftPage.addKey(splitHelper.getNewData());
			}
		} else {
			if (splitHelper.getRightChild() != null) {
				System.out.println("Entrando no split helper da direita " + splitHelper.getNewData().getId());
				rightPage.addSplitHelper(splitHelper);
			} else {
				rightPage.addKey(splitHelper.getNewData());
			}
		}

		SplitHelper newSplitHelper = new SplitHelper(rightPage, insertOnParent, leftPage);
		
		leftPage.refreshNKeys();
		rightPage.refreshNKeys();
		
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
