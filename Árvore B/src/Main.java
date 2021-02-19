import java.util.Scanner;

public class Main {
	
	public static void main(String args[]) {
		int degree, choice, id;
		Scanner reader = new Scanner(System.in);
		Data key = null;
		
		System.out.print("Type the number of keys per page: ");
		degree = reader.nextInt();
		BTree bt = new BTree(degree);
		
		while (true) {
			System.out.println("+----------------------+");
			System.out.println("|   Choose an option   |");
			System.out.println("+----------------------+");
			System.out.println("|1 - Add               |");
			System.out.println("|2 - Search            |");
			System.out.println("|3 - Remove            |");
			System.out.println("|4 - Print             |");
			System.out.println("+----------------------+");
			System.out.print("~: ");
			choice = reader.nextInt();
			
			switch (choice) {
				case 1:
					System.out.print("Type the value to be added: ");
					id = reader.nextInt();
					
					if (bt.getData(id) == null) {
						bt.insert(new Data(id));
					} else {
						System.out.println("\nThis value can't be added because it already exists in the tree.");
					}
					break;
				case 2:
					System.out.print("Type the value to be searched: ");
					key = bt.getData(reader.nextInt());
					
					if (key != null) {
						System.out.println("\nThe value " + key.getId() + " was found in the tree!");
					} else {
						System.out.println("\nValue not found in the tree.");
					}
					break;
					
				case 3:
					System.out.print("Type the value to be removed: ");
					// bt.remove(reader.nextInt());
					break;
				case 4:
					System.out.println(bt.toString()); 
					break;
				default:
					System.out.println("Invalid option.");
					break;
			}
		}
	}

}
