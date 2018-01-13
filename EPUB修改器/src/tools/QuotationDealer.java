package tools;

import java.util.Stack;

public class QuotationDealer {
	public static interface QuotationDealerListener {
		/**
		 * error format:type&location
		 * 
		 * @param error
		 */
		void onErrorExiting(String error);
		/**
		 * current content dealing
		 * @param content
		 */
		void onSuccessful(String content);
	}

	private String content;
	private Stack<Character> chineseSignalQuotation = new Stack<Character>();
	private Stack<Character> chineseDoubleQuotation = new Stack<Character>();
	private int englishSignalIndex, englishDoubleIndex;
	private QuotationDealerListener listener = null;

	/**
	 * @param content
	 * @param listener
	 */
	public QuotationDealer(String content, QuotationDealerListener listener) {
		super();
		this.content = content;
		this.listener = listener;
		deal();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		deal();
	}

	public QuotationDealerListener getListener() {
		return listener;
	}

	public void setListener(QuotationDealerListener listener) {
		this.listener = listener;
	}

	private void deal() {
		englishSignalIndex = englishDoubleIndex = -1;
		int englishDoubleCount = 0;
		int englishSignalCount = 0;
		int len = content.length();
		for (int i = 0; i < len; i++) {
			switch (content.charAt(i)) {
			case '"':
				englishDoubleCount++;
				englishDoubleIndex = i;
				break;
			case '\'':
				englishSignalCount++;
				englishSignalIndex = i;
				break;
			case '“':
				chineseDoubleQuotation.push('“');
				break;
			case '”':
				if (chineseDoubleQuotation.empty()) {
					listener.onErrorExiting("”未找到与之匹配的左双引号&" + i);
					return;
				} else {
					if (chineseDoubleQuotation.peek() == '“') {
						chineseDoubleQuotation.pop();
					} else {
						listener.onErrorExiting("”未找到与之匹配的左双引号&" + i);
						return;
					}
				}
				break;
			case '‘':
				chineseSignalQuotation.push('‘');
				break;
			case '’':
				if (chineseSignalQuotation.empty()) {
					listener.onErrorExiting("’未找到与之匹配的左双引号&" + i);
					return;
				} else {
					if (chineseSignalQuotation.peek() == '‘') {
						chineseSignalQuotation.pop();
					} else {
						listener.onErrorExiting("’未找到与之匹配的左双引号&" + i);
						return;
					}
				}
				break;
			}
		}
		if (englishDoubleCount % 2 != 0) {
			listener.onErrorExiting("\"未找到与之匹配的左双引号&" + englishDoubleIndex);
			return;
		}
		if (englishSignalCount % 2 != 0) {
			listener.onErrorExiting("'未找到与之匹配的左双引号&" + englishSignalIndex);
			return;
		}
		listener.onSuccessful(this.content);
	}
}
