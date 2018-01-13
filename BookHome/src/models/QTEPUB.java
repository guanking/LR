package models;

import java.io.File;
import java.util.LinkedList;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tools.FileOperator;
import views.MyFrame;

public class QTEPUB extends EPUB {
	protected LinkedList<LinkedList<String>> rules;
	protected LinkedList<NodeList> transNode = new LinkedList<NodeList>();

	public QTEPUB(String path, String tempDir,
			LinkedList<LinkedList<String>> rules) throws ParserException {
		// TODO Auto-generated constructor stub
		super(path, tempDir);
		this.rules = rules;
		for (int index = 1; index < rules.get(MyFrame.TAG).size(); index += 2) {
			transNode.addLast((new Parser(rules.get(MyFrame.TAG).get(index))
					.parse(null)));
		}
	}

	@Override
	protected void textsTrans() throws Exception {
		// TODO Auto-generated method stub
		String content;
		Parser parser;
		NodeList nodes;
		File file;
		for (String fileName : this.texts) {
			System.out.println("Trans " + fileName + "......");
			file = new File(this.tempDir, fileName);
			content = FileOperator.readContent(file);
			parser = new Parser(content);
			nodes = parser.parse(null);
			transNodes(nodes);
			FileOperator.writeContent(file, nodesToString(nodes));
		}
	}

	private void transNodes(NodeList nodes) {
		// TODO Auto-generated method stub
		if (nodes == null)
			return;
		Node node;
		for (int i = 0; i < nodes.size(); i++) {
			node = nodes.elementAt(i);
			if (node instanceof TagNode) {
				transTagNode((TagNode) node);
				transNodes(node.getChildren());
			} else if (node instanceof TextNode) {
				transTextNode((TextNode) node);
			}
		}

	}

	protected void transTagNode(TagNode node) {
		String text = "<" + node.getText() + ">";
		for (int index = 0; index < this.rules.get(MyFrame.TAG).size(); index += 2) {
			if (text.equals(this.rules.get(MyFrame.TAG).get(index))) {
				node.setAttributesEx(((TagNode) this.transNode.get(index / 2)
						.elementAt(0)).getAttributesEx());
				return;
			}
		}
	}

	protected void transTextNode(TextNode node) {
		String text = node.getText();
		String src, des;
		int begin = 0, len;
		for (int index = 0; index < this.rules.get(MyFrame.TEXT).size(); index += 2) {
			src = this.rules.get(MyFrame.TEXT).get(index);
			des = this.rules.get(MyFrame.TEXT).get(index + 1);
			len = des.length();
			while ((begin = text.indexOf(src, begin)) != -1) {
				text = text.replace(src, des);
				begin += len;
			}
		}
		node.setText(text);
	}

	@Override
	protected void imagesTrans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void opfTrans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void ncxTrans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void replaceStylesheet() {
		// TODO Auto-generated method stub

	}

	// public static void main(String[] args) throws ParserException {
	// // TODO Auto-generated method stub
	// String text =
	// "<p class=\"xxx\"><p class=\"xxx\"><p class=\"xxx\"><p class=\"xxx\"><p class=\"xxx\">";
	// Parser parser = new Parser(text);
	// NodeList node = parser.parse(null);
	// TagNode n = null;
	// for (int i = 0; i < node.size(); i++) {
	// n = (TagNode) node.elementAt(i);
	// System.out.println(n.toHtml() + " " + n.getAttributesEx());
	// Div div = new Div();
	// div.setAttributesEx(n.getAttributesEx());
	// System.out.println(div.toHtml() + " " + div.getClass());
	// }
	//
	// System.out.println(node.elementAt(0).getClass()
	// .equals(node.elementAt(1).getClass()));
	// }

}
