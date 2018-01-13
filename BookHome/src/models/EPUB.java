package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tools.NavPointComparator;

public abstract class EPUB {
	protected static final String navPointFaceText = "<navPoint id=\"navPoint-1\" playOrder=\"1\">\n"
			+ "<navLabel>\n"
			+ "<text>封面</text>\n"
			+ "</navLabel>\n"
			+ "<content src=\"Text/Frontcover.xhtml\"/>\n" + "\n</navPoint>";
	protected static final String navPointCopyrightText = "<navPoint id=\"navPoint-2\" playOrder=\"2\">\n"
			+ "<navLabel>\n"
			+ "<text>版权信息</text>\n"
			+ "</navLabel>\n"
			+ "<content src=\"Text/Frontcover.xhtml\"/>\n" + "\n</navPoint>";
	protected String path;
	protected String container;
	protected String stylesheet;
	protected String opf;
	protected String ncx;
	protected String mimetype;
	protected ArrayList<String> texts;
	protected Hashtable<String, String> images;
	protected String tempDir;

	public EPUB(String path, String tempDir) {
		this.path = path;
		this.texts = new ArrayList<String>();
		this.images = new Hashtable<String, String>();
		this.tempDir = tempDir;
	}

	public void reset(String path) {
		this.path = path;
		this.texts.clear();
		this.images.clear();
	}

	public void translate() throws Exception {
		textsTrans();
		imagesTrans();
		opfTrans();
		ncxTrans();
		replaceStylesheet();
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	public String getOpf() {
		return opf;
	}

	public void setOpf(String opf) {
		this.opf = opf;
	}

	public String getNcx() {
		return ncx;
	}

	public void setNcx(String ncx) {
		this.ncx = ncx;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public ArrayList<String> getTexts() {
		return texts;
	}

	public void setTexts(ArrayList<String> texts) {
		this.texts = texts;
	}

	public Hashtable<String, String> getImages() {
		return images;
	}

	public void setImages(Hashtable<String, String> images) {
		this.images = images;
	}

	protected String nodesToString(NodeList nodes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nodes.size(); i++) {
			sb.append(nodes.elementAt(i).toHtml());
		}
		return sb.toString();
	}

	public static String toHalf(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			if (a[i] >= 65281 && a[i] <= 65374)
				a[i] = (char) (a[i] - 65248);
			else if (a[i] == 12288)
				a[i] = 32;
		}
		StringBuffer sb = new StringBuffer(a.length);
		for (int i = 0; i < a.length; i++) {
			if (Character.isDigit(a[i]) && i + 2 < a.length && a[i + 1] == '·'
					&& Character.isDigit(a[i + 2])) {
				sb.append(a[i++]);
				sb.append('.');
				sb.append(a[++i]);
				continue;
			}
			if (Character.isLowerCase(a[i]) && i + 1 < a.length
					&& Character.isUpperCase(a[i + 1])) {
				sb.append(a[i++]);
				sb.append(' ');
				sb.append(a[i]);
				continue;
			}
			sb.append(a[i]);
		}
		return sb.toString().replace("———", "——");
	}

	protected String sortNavPoint(String content) throws Exception {
		NodeList ns = new Parser(content).parse(null);
		StringBuffer sb = new StringBuffer();
		Stack<Node> stack = new Stack<Node>();
		int index;
		Node node;
		for (index = 0; index < ns.size(); index++) {
			sb.append(ns.elementAt(index).toHtml());
			if (((node = ns.elementAt(index)) instanceof TagNode)
					&& ((TagNode) node).getTagName().equalsIgnoreCase("navMap"))
				break;
		}
		TagNode tagNode;
		if (index == ns.size()) {
			throw new Exception(this.path + ":未找到navMap标签");
		}
		ArrayList<LinkedList<Node>> sortNodes = new ArrayList<LinkedList<Node>>();
		LinkedList<Node> tempNodes = null;
		String tagName;
		for (index++; index < ns.size(); index++) {
			if (ns.elementAt(index) instanceof TagNode) {
				tagNode = (TagNode) ns.elementAt(index);
				tagName = tagNode.getRawTagName();
				if (tagName.equals("/navMap")) {
					break;
				} else if (tagName.equals("/navPoint")) {
					tempNodes = new LinkedList<Node>();
					stack.push(ns.elementAt(index));
					while (!stack.empty()) {
						node = stack.pop();
						tempNodes.addFirst(node);
						if (node instanceof TagNode) {
							if (((TagNode) node).getRawTagName().equals(
									"navPoint")) {
								break;
							}
						}
					}
					sortNodes.add(tempNodes);
					continue;
				}
			}
			stack.push(ns.elementAt(index));
		}
		Collections.sort(sortNodes, new NavPointComparator());
		for (LinkedList<Node> ele : sortNodes) {
			for (Node ele1 : ele) {
				sb.append(ele1.toHtml());
			}
			sb.append((new TextNode("\n")).toHtml());
		}
		if (index == ns.size()) {
			throw new Exception(this.path + ":</navMap>标签为最后一个标签");
		}
		for (; index < ns.size(); index++) {
			sb.append(ns.elementAt(index).toHtml());
		}
		return sb.toString();
	}

	protected abstract void textsTrans() throws Exception;

	protected abstract void imagesTrans() throws Exception;

	protected abstract void opfTrans() throws ParserException, Exception;

	protected abstract void ncxTrans() throws ParserException, Exception;

	protected abstract void replaceStylesheet() throws Exception;
}