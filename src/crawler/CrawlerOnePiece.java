package crawler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerOnePiece {
	ArrayList<Chapter> listChapters = new ArrayList<>();
	private String url;
	
	public CrawlerOnePiece() {
		
	}
	public CrawlerOnePiece(String url) {
		this.url = url;
		try {
		listChapters = getAllChapters(url);
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	
	public ArrayList<Chapter> getListChaps(){
		return listChapters;
	}
	
	public void getListChapters() throws IOException{
		listChapters = getAllChapters(url);
		printList(listChapters);
	}
	
	public Chapter getChapterByChapNumber(String ChapNumber) {
		for (int i = 0 ; i < listChapters.size() ; i++) {
			if (listChapters.get(i).getChapNumber().equalsIgnoreCase(ChapNumber)) {
				return listChapters.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<Chapter> getAllChapters(String urls) throws IOException {
	    ArrayList<Chapter> list_chapters = new ArrayList<>();
	    Document doc = Jsoup.connect(urls).get();
	    Elements chapterItems = doc.select("li.row");
	    for (Element chapterItem : chapterItems) {
	        Elements linkElement = chapterItem.select("div.chapter a");
	        if (linkElement.isEmpty()) {
	            continue;
	        } 
	        String link = linkElement.first().attr("href");
	        Chapter chapter = new Chapter(link);
	        list_chapters.add(chapter);
	    }

	    return list_chapters;
	}
	
	private void printList(ArrayList<Chapter> listChap) {
		System.out.println("Numbers of chapters: " + listChap.size());
		System.out.println("List Chaps : ");
		for (int i = 0 ; i < listChap.size() ; i++) {
			System.out.println("Chapter: " + listChap.get(i).getChapNumber());
		}
	}
	
	private ArrayList<String> getImageLinkOnPage(String urls) throws IOException{
	    ArrayList<String> imgLinks = new ArrayList<String>();
	    Document doc = Jsoup.connect(urls).get();
	    Elements images = doc.select("div.page-chapter img.lozad");
	    
	    for (Element image : images) {
	        String imageUrl = image.attr("data-src");
	        imgLinks.add(imageUrl);
	    }
	    return imgLinks;
	}
	
	private void saveImg(String src, String name, String dir) {
	    try {
	        HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(src))
	                .build();

	        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(dir, name)));

	        if (response.statusCode() != 200) {
	            throw new IOException("Failed to download file: " + response.statusCode());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveFile(Chapter chap) {
	    String dir = "resources/Truyen/Chapter" + chap.getChapNumber(); 
	    File directory = new File(dir);
	    if (!directory.exists()) {
	        directory.mkdirs();  // If the directory does not exist, create it
	    } else {
	        for (File file : directory.listFiles()) {
	            file.delete();
	        }
	    }
	    try {
	        ArrayList<String> list_img = getImageLinkOnPage(chap.getUrl());
	        for (int i = 0; i < list_img.size(); i++) {
	            String name = i + ".jpg";
	            saveImg(list_img.get(i), name, dir);
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error", "Error to save file !", JOptionPane.ERROR_MESSAGE);
	    }
	}

/*	public static void main(String[] args) {
	    String url = "https://www.nettruyenup.vn/truyen-tranh/dao-hai-tac-11402";
	    CrawlerOnePiece crawler = new CrawlerOnePiece(url);
	    try {
	        ArrayList<Chapter> chapters = crawler.getAllChapters(url);
	        if (!chapters.isEmpty()) {
	            Chapter chapter = chapters.get(12);
	            ArrayList<String> imageLinks = crawler.getImageLinkOnPage(chapter.getUrl());
	            for (String imageLink : imageLinks) {
	                System.out.println(imageLink);
	            }
	            crawler.saveFile(chapter);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	} */

}
