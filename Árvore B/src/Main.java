import java.util.Scanner;

public class Main {
	
	public static void main(String args[]) {
		int degree, choice;
		Scanner reader = new Scanner(System.in);
		
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
					bt.insert(new Data(reader.nextInt()));
					break;
				case 2:
					System.out.print("Type the value to be searched: ");
					// bt.search(reader.nextInt());
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
