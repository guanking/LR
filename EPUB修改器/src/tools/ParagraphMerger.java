package tools;

public class ParagraphMerger {
	private String content;

	/**
	 * @param content
	 */
	public ParagraphMerger(String content) {
		super();
		this.content = content;
		merge();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		merge();
	}
	public void merge(){
		content=content.replaceAll("</p>[\t\n\\s]*?<p[\\s\\S]*?>","");
	}
}
