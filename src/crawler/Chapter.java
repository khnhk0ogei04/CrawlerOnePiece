package crawler;

public class Chapter {
	private String url;
	private String chapNumber;
	public Chapter() {
		
	}
	public Chapter(String url) {
		this.url = url;
		this.chapNumber = findChapNumber(url);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getChapNumber() {
		return chapNumber;
	}
	public void setChapNumber(String chapNumber) {
		this.chapNumber = chapNumber;
	}
	private String findChapNumber(String url) {
	    String[] parts = url.split("/");
	    for (String part : parts) {
	        if (part.startsWith("chapter")) {
	            return part.substring(8);
	        }
	    }
	    return null;
	}
}
