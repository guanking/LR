package tools;

public class SentencePaster {
	protected String content;

	/**
	 * @param content
	 */
	public SentencePaster(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		deal();
	}

	protected void deal() {

	}
}
