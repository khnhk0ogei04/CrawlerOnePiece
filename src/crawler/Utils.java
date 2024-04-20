package crawler;
import java.util.Scanner;

import javafx.scene.control.Dialog;
public class Utils {
	Scanner sc = new Scanner(System.in);
	public static void menu() {
		System.out.println("-----MENU-----");
		System.out.println("1. Get list chap");
		System.out.println("2. Get all chap");
		System.out.println("3. Get chap by chapID");
		System.out.println("4. Exit");
	}
	public int getIntInRange(int min , int max) {
        int choose  = 0;
        while (true) {
            try {
                System.out.println("Nhap vao gia tri: ");
                choose = Integer.parseInt(sc.nextLine());
                if(choose >= min && choose <= max){
                    return choose;
                }
            } catch (Exception e) {
                System.out.println("Nhap sai, hay nhap lai");
            }
        }
	}
	public boolean checkIntInput(String query) {
		int result = -1;
		if (query.charAt(0) < '0' || query.charAt(0) > '9') return false;
		try {
			result = Integer.parseInt(query);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
}
	
