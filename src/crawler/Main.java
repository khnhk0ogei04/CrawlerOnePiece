package crawler;
import java.util.Scanner;
public class Main {
	public static void main(String[] args) {
		final String URL = "https://www.nettruyenup.vn/truyen-tranh/dao-hai-tac-11402";
		Utils utils = new Utils();
		CrawlerOnePiece crawlerOnePiece = new CrawlerOnePiece(URL);
		boolean doing = true;
		while (doing) {
			utils.menu();
			int choice = utils.getIntInRange(1, 4);
			switch(choice) {
			// In ra so luong chap truyen:
			case 1: 
				try {
					crawlerOnePiece.getListChapters();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// Crawl All chap in list:
			case 2:
				for (int i = 0 ; i < crawlerOnePiece.getListChaps().size() ; i++) {
					Chapter chapter = crawlerOnePiece.getListChaps().get(i);
					crawlerOnePiece.saveFile(chapter);
					System.out.println("Successfully crawl on chapter " + chapter.getChapNumber());
					try { 
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case 3:
				int option = utils.getIntInRange(1, crawlerOnePiece.getListChaps().size());
				String tmp = option + "";
				Chapter chapter = crawlerOnePiece.getChapterByChapNumber(tmp);
				crawlerOnePiece.saveFile(chapter);
				System.out.println("Downloaded chapter");
				break;
			case 4:
				System.out.println("End program");
				doing = false;
				break;
			}
			System.out.println("");
		}
	}
}
