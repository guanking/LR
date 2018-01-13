package tools;

public class LanguageFormat {
	private String content;
	private static String[][] trans = { { ".", "\\.[\\s]{2}", "¡£" },
			{ ",", ",\\s", "£¬" }, { ";", ";\\s", "£»" },
			{ "?", "\\?[\\s]{2}", "£¿" }, { "!", "![\\s]{2}", "£¡" } };

	/**
	 * @param content
	 */
	public LanguageFormat(String content, boolean toEnglish) {
		super();
		this.content = content;
		if (toEnglish) {
			transToEnglish();
		} else {
			transToChinese();
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content, boolean toEnglish) {
		this.content = content;
		if (toEnglish) {
			transToEnglish();
		} else {
			transToChinese();
		}
	}

	private void transToChinese() {

	}

	private void transToEnglish() {

	}

}
