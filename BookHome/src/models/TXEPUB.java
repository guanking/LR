package models;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tools.FileOperator;
import tools.NavPointComparator;
import tools.RAR;

public class TXEPUB extends EPUB {
	public TXEPUB(String path, String tempDir) {
		super(path, tempDir);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void textsTrans() throws Exception {
		// TODO Auto-generated method stub
		if (this.images.get(imageNameDir + "footnote.png") != null) {
			File file = new File(this.path.substring(0,
					this.path.lastIndexOf('\\'))
					+ "\\note\\TX\\note.png");
			if (!file.exists()) {
				throw new Exception(file.getAbsolutePath() + "文件不存在");
			}
			Image img = ImageIO.read(file);
			ImageIO.write((RenderedImage) img, "PNG", new File(this.tempDir,
					imageNameDir + "footnote.png"));
		}
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
		this.texts.add("OEBPS/Text/Copyright.xhtml");
		FileOperator.copyFile(
				this.path.substring(0, this.path.lastIndexOf('\\'))
						+ "\\fmbq\\TX\\Copyright.xhtml", this.tempDir
						+ "/OEBPS/Text/Copyright.xhtml");
		this.texts.add("OEBPS/Text/Frontcover.xhtml");
		FileOperator.copyFile(
				this.path.substring(0, this.path.lastIndexOf('\\'))
						+ "\\fmbq\\TX\\Frontcover.xhtml", this.tempDir
						+ "/OEBPS/Text/Frontcover.xhtml");
	}

	private void transNodes(NodeList nodes) throws Exception {
		if (nodes == null)
			return;
		Node node;
		for (int i = 0; i < nodes.size(); i++) {
			node = nodes.elementAt(i);
			if (node instanceof TagNode) {
				// System.out.println(node.getClass());
				if (node instanceof ParagraphTag) {
					changeParagraphTag((ParagraphTag) node);
				} else if (node instanceof HeadingTag) {
					changeHeadingTag(((HeadingTag) node));
				} else if (node instanceof Div) {
					changeDiv((Div) node);
				} else if (node instanceof Span) {
					changeSpan((Span) node);
				} else if (node instanceof ImageTag) {
					changeImageTag((ImageTag) node);
				}
				transNodes(node.getChildren());
			} else {
				if (!node.toHtml().matches("\\s+")) {
					// System.out.println("Before:"+node.getText());
					node.setText(EPUB.toHalf(node.getText()));
					// System.out.println("After:"+node.getText());
				}
			}
		}
	}

	private static final String imageNameDir = "OEBPS/Images/";

	private void changeImageTag(ImageTag node) {
		// TODO Auto-generated method stub
		// System.out.println(node.getText());
		changeImageMode(node);
		if (node.getText()
				.trim()
				.matches(
						"img\\s+?alt\\s*?=\\s*?\"\"\\s+?src\\s*?=\\s*?\".+?\"\\s*?/$")) {
			node.removeAttribute("/");
			node.setAttribute("active", "\"true\"");
			node.setAttribute("/", null);
			// System.out.println("ChangeTo : " + node.toHtml());
		} else {
			String cls = node.getAttribute("class");
			if (cls != null && cls.equals("athena_img_center")) {
				Node p = node.getNextSibling();
				if (p == null) {
					node.removeAttribute("class");
					node.removeAttribute("/");
					node.setAttribute("active", "\"true\"");
					node.setAttribute("/", null);
				}
				TagNode tempParagraph0 = new TagNode(), tempParagraph1 = new TagNode();
				tempParagraph0.setTagName("/p");
				tempParagraph1.setTagName("p");
				tempParagraph1.setAttribute("class", "\"bodyPic\"");
				while (p != null && (p instanceof TextNode)) {
					p = p.getNextSibling();
				}
				if (p != null && (p instanceof ImageTag)) {
					if (((ImageTag) p).getAttribute("class").equals(
							"athena_img_center")) {
						node.removeAttribute("class");
						node.removeAttribute("/");
						node.setAttribute("active", "\"true\"");
						node.setAttribute("/", null);
						Node par = node.getParent();
						// System.out.println("Before : "+par.toHtml());
						// System.out.println("Before : "+par.getText());
						NodeList ns = par.getChildren();
						int index = ns.indexOf(node);
						NodeList nns = new NodeList();
						int i;
						for (i = 0; i < ns.size(); i++) {
							nns.add(ns.elementAt(i));
							if (i == index) {
								nns.add(tempParagraph0);
								nns.add(tempParagraph1);
							}
						}
						for (i = ns.size() - 1; i > index; i--) {
							ns.remove(i);
						}
						for (i = index + 1; i < nns.size(); i++) {
							// System.out.println("add node "+i+" : "+nns.elementAt(i).toHtml());
							ns.add(nns.elementAt(i));
						}
						// System.out.println("After : " + par.toHtml());
					}// end if
				}// end if p!=null instance of ImageTag
			}
		}
	}

	private void changeImageMode(ImageTag node) {
		String src = node.getAttribute("src");
		String name = src.substring(src.lastIndexOf("/") + 1);
		String alt = node.getAttribute("alt");
		if (alt != null) {
			node.setAttribute("alt", EPUB.toHalf(alt));
			// System.out.println(alt);
		}
		// System.out.println(name + " " + cls);
		if (name.equals("footnote.png")) {
			if (this.images.get(imageNameDir + name) != null) {
				this.images.put(imageNameDir + name, imageNameDir + "note.png");
				node.setAttribute("src", src.substring(0, src.lastIndexOf("/"))
						+ "/note.png");
				// System.out.println(node.toHtml());
			} else {
				System.out.println("No match key: " + imageNameDir + name);
				System.out.println(this.images.values());
			}
		} else if (node
				.getText()
				.trim()
				.matches(
						"^img\\s+?id\\s*?=\\s*?\".+?\"\\s+?src\\s*?=\\s*?\".+?\"\\s*?/$")
				|| node.getText()
						.trim()
						.matches(
								"^img\\s+?alt\\s*?=\\s*?\"\"\\s+?id\\s*?=\\s*?\".+?\"\\s+?src\\s*?=\\s*?\".+?\"\\s*?/$")) {
			if (this.images.get(imageNameDir + name) != null) {
				String temp = getFormatedName().replace("jpg", "png");
				this.images.put(imageNameDir + name, imageNameDir + temp);
				// System.out.println("Find " + node.toHtml());
				Attribute attr = null;
				Vector<Attribute> atts = node.getAttributesEx();
				System.out.println(atts.toString());
				for (int i = 1; i < atts.size(); i++) {
					if (atts.get(i).getName() != null) {
						if (atts.get(i).getName().equalsIgnoreCase("id")) {
							atts.get(i).setName("class");
							atts.get(i).setValue("s-pic");
						}
					}
				}
				node.setAttribute("src",
						src.substring(0, src.lastIndexOf('/') + 1) + temp);
				if (node.getAttribute("alt") == null) {
					node.removeAttribute("/");
					node.setAttribute("alt","\"\"");
					node.setAttribute("/",null);

				}
				// System.out.println("Change to " + node.toHtml());
			} else {
				System.out.println("No match key: " + imageNameDir + name);
				System.out.println(this.images.values());
			}
			// src = src.substring(0, src.lastIndexOf(".")) + ".png";
			// } else if (name.equals("cover.png")) {
			// System.out.println("cover.png " + imageNameDir + name);
			// if (this.images.get(imageNameDir + name) != null) {
			// this.images.put(imageNameDir + name, imageNameDir
			// + "device_phone_Frontcover.jpg");
			// node.setAttribute("src", src.substring(0, src.lastIndexOf("/"))
			// + "/device_phone_Frontcover.jpg");
			// System.out.println("Change : " + node.toHtml());
			// } else {
			// System.out.println("No match key: " + imageNameDir + name);
			// System.out.println(this.images.values());
			// }
		} else {
			if (this.images.get(imageNameDir + name) != null) {
				this.images.put(imageNameDir + name, imageNameDir
						+ (name = getFormatedName()));
				node.setAttribute("src",
						src.substring(0, src.lastIndexOf("/") + 1) + name);
			} else {
				System.out.println("No match key(format): " + imageNameDir
						+ name);
				System.out.println(this.images.values());
			}
		}
	}

	private static int countImageOrder = 0;
	protected static final String zeros = "000000000000";

	private static String getFormatedName() {
		countImageOrder++;
		countImageOrder %= 1000000;
		String name = zeros + countImageOrder;
		name = name.substring(name.length() - 6);
		return "Figure-" + name.substring(0, 4) + "-" + name.substring(4)
				+ ".jpg";
	}

	private void changeSpan(Span span) {// finished
		// TODO Auto-generated method stub
		String cls = span.getAttribute("class");
		if (cls == null)
			return;
		if (cls.equals("athena_quote")) {
			// System.out.println("find : " + span.toHtml());
			TextNode textNode = new TextNode(span.toPlainTextString());
			Node par = span.getParent(), tempNode;
			NodeList ns = par.getChildren();
			int index = ns.indexOf(span);
			int len = ns.size();
			NodeList nns = new NodeList();
			int i;
			boolean changeImage = false;
			for (i = 0; i < len; i++) {
				if (index == i) {
					nns.add(textNode);
				} else {
					if (!changeImage
							&& i > index
							&& ((tempNode = ns.elementAt(i)) instanceof ImageTag)) {
						changeImage = true;
						((ImageTag) tempNode).removeAttribute("id");
						((ImageTag) tempNode).setAttribute("class",
								"qqreader-footnote");
						nns.add(tempNode);
					} else {
						nns.add(ns.elementAt(i));
					}
				}
			}
			for (i = ns.size() - 1; i >= index; i--) {
				ns.remove(i);
			}
			for (i = index; i < nns.size(); i++) {
				ns.add(nns.elementAt(i));
			}
		}
	}

	private void changeDiv(Div node) {// finished
		// TODO Auto-generated method stub
		if (node.getAttribute("class")!=null&&node.getAttribute("class").equals("athena_img_center")) {
			// System.out.println("Find : " + node.getText());
			node.setAttribute("class", "qrbodyPic");
			// System.out.println("ChangeTo : " + node.getText());
		}
	}

	private void changeParagraphTag(ParagraphTag node) {
		// TODO Auto-generated method stub
		if (node.getText().trim().equals("p")) {
			// System.out.println("Find : " + node.getText());
			Node tempNode = node.getChildren() != null ? node.getChild(0)
					: null;
			if (tempNode != null) {
				if ((tempNode instanceof ImageTag)
						&& ((ImageTag) tempNode).getAttribute("class").equals(
								"athena_img_center")) {
					node.setAttribute("class", "\"bodyPic\"");
					return;
				}
			}
			node.setAttribute("class", "\"content\"");
			// System.out.println("ChangeTo : "+node.getText());
			return;
		}// end if
		String cls = node.getAttribute("class");
		if (cls == null)
			return;
		switch (cls) {
		case "athena_imgtitle":
			// System.out.println("Find : " + node.getText());
			node.setAttribute("class", "imgtitle");
			// System.out.println("ChangeTo : " + node.getText());
			break;
		case "athena_imgcaption":
			// System.out.println("Find : " + node.getText());
			node.setAttribute("class", "imgdescript");
			// System.out.println("ChangeTo : " + node.getText());
			break;
		}// end switch
	}

	private void changeHeadingTag(HeadingTag node) throws Exception {// finished
		// TODO Auto-generated method stub
		String id;
		if ((id = node.getAttribute("id")) == null)
			return;
		// System.out.println("Find : " + node.toHtml());
		switch (node.getTagName()) {
		case "H1":
			node.setAttribute("class", "\"firstTitle\"");
			break;
		case "H2":
			node.setAttribute("class", "\"secondTitle\"");
			break;
		case "H3":
			node.setAttribute("class", "\"thirdTitle\"");
			break;
		case "H4":
			node.setAttribute("class", "\"fourthTitle\"");
			break;
		case "H5":
			node.setAttribute("class", "\"fifthTitle\"");
			break;
		case "H6":
			node.setAttribute("class", "\"sixthTitle\"");
			break;
		default:
			throw new Exception("can't match headingTag : " + node.getTagName());
		}
		// System.out.println("ChangeTo : " + node.toHtml());
	}

	@Override
	protected void imagesTrans() throws Exception {
		// TODO Auto-generated method stub
		// System.out.println(this.images.keySet());
		// System.out.println(this.images.values());
		for (String key : this.images.keySet()) {
			if (this.images.get(key).endsWith("cover.png")) {
				// System.out.println("ImageTrans:" + key);
				File file = new File(this.tempDir, key);
				Image img = FileOperator.readImage(file);
				img = FileOperator.imResize((BufferedImage) img, 720, 1280);
				FileOperator.writeImage(file, img, "JPG");
				int index = 0;
				if ((index = key.lastIndexOf('/')) == -1) {
					throw new Exception("Image wrong format : " + key);
				}
				this.images.put(key.substring(0, index)
						+ "/device_pad_Frontcover.jpg", key.substring(0, index)
						+ "/device_pad_Frontcover.jpg");
				this.images.put(key, key.substring(0, index)
						+ "/device_phone_Frontcover.jpg");
				img = FileOperator.imResize((BufferedImage) img, 768, 1024);
				FileOperator.writeImage(
						new File(this.tempDir, key.substring(0, index)
								+ "/device_pad_Frontcover.jpg"), img, "JPG");
				img = FileOperator.imResize((BufferedImage) img, 720, 1280);
				FileOperator.writeImage(
						new File(this.tempDir, key.substring(0, index)
								+ "/device_phone_Frontcover.jpg"), img, "JPG");
				return;
			}
		}
		System.out.println("No image chanslated!");
	}

	@Override
	protected void opfTrans() throws ParserException, Exception {
		// TODO Auto-generated method stub
		NodeList ns = new Parser(FileOperator.readContent(new File(tempDir,
				this.opf))).parse(null);
		NodeList nns = new NodeList();
		TagNode node;
		String type;
		String href, name;
		for (int i = 0; i < ns.size(); i++) {
			if (ns.elementAt(i) instanceof TagNode) {
				node = (TagNode) ns.elementAt(i);
				// System.out.println(node.getTagName());
				if ((type = node.getAttribute("media-type")) != null
						&& type.startsWith("image/")) {
					href = node.getAttribute("href");
					name = href.substring(href.lastIndexOf('/') + 1);
					if ((name = this.images.get(imageNameDir + name)) != null) {
						name = name.substring(name.lastIndexOf('/') + 1);
						node.setAttribute("href",
								href.substring(0, href.lastIndexOf('/') + 1)
										+ name);
						if (name.endsWith("jpg")) {
							node.setAttribute("media-type", "image/jpeg");
						} else {
							node.setAttribute("media-type", "image/png");
						}
					} else {
						System.out.println("no match :" + imageNameDir + name);
					}
					nns.add(node);
				} else if (node.getRawTagName().equalsIgnoreCase("manifest")) {
					nns.add(node);
					node = new TagNode();
					node.setTagName("item");
					node.setAttribute("id", "\"Frontcover.xhtml\"");
					node.setAttribute("href", "\"Text/Frontcover.xhtml\"");
					node.setAttribute("media-type", "\"application/xhtml+xml\"");
					node.setAttribute("/", null);
					nns.add(node);// add fromconver.xhtml item
					node = new TagNode();
					node.setTagName("item");
					node.setAttribute("id", "\"Copyright.xhtml\"");
					node.setAttribute("href", "\"Text/Copyright.xhtml\"");
					node.setAttribute("media-type", "\"application/xhtml+xml\"");
					node.setAttribute("/", null);
					nns.add(node);// add fromconver.xhtml item
					node = new TagNode();
					node.setTagName("item");
					node.setAttribute("href",
							"\"Images/device_pad_Frontcover.jpg\"");
					node.setAttribute("id", "\"icover1\"");
					node.setAttribute("media-type", "\"image/jpeg\"");
					node.setAttribute("/", null);
					// System.out.println("opf Add : " + node.toHtml());
					nns.add(node);// add device_pad_Frontcovert.jpg item
				} else if (node.getRawTagName().equalsIgnoreCase("spine")) {
					nns.add(node);
					node = new TagNode();
					node.setTagName("itemref");
					node.setAttribute("idref", "\"Frontcover.xhtml\"");
					node.setAttribute("/", null);
					nns.add(node);// add frontcover.xhtml itemref
					node = new TagNode();
					node.setTagName("itemref");
					node.setAttribute("idref", "\"Copyright.xhtml\"");
					node.setAttribute("/", null);
					nns.add(node);// add Copyright.xhtml itemref
				} else {
					nns.add(node);
				}
			} else {
				nns.add(ns.elementAt(i));
			}
		}
		// System.out.println(this.nodesToString(nns));
		FileOperator.writeContent(new File(this.tempDir, this.opf),
				this.nodesToString(nns));
	}

	@Override
	protected void ncxTrans() throws ParserException, Exception {
		// TODO Auto-generated method stub
		NodeList nodes = new Parser(FileOperator.readContent(new File(
				this.tempDir, this.ncx))).parse(null);
		// System.out.println(this.nodesToString(nodes));
		NodeList ns = new NodeList();
		Node node;
		String tagName;
		for (int i = 0; i < nodes.size(); i++) {
			node = nodes.elementAt(i);
			if (node instanceof TagNode) {
				if ((tagName = ((TagNode) node).getTagName())
						.equals("navPoint")) {
					((TagNode) node).setAttribute("id", "navPoint-"
							+ ((TagNode) node).getAttribute("playOrder"));
					ns.add(node);
				} else if (tagName.equals("navMap")) {
					ns.add(node);
					TextNode textNode = new TextNode(EPUB.navPointFaceText);
					ns.add(textNode);
					textNode = new TextNode(EPUB.navPointCopyrightText);
					ns.add(textNode);
				} else {// end navPoint and navMap
					ns.add(node);
				}
			} else {
				ns.add(node);
			}// end if is tagnode
		}
		// System.out.println(this.nodesToString(ns));
		FileOperator.writeContent(new File(this.tempDir, this.ncx),
				sortNavPoint(this.nodesToString(ns)));
	}

	@Override
	protected void replaceStylesheet() throws Exception {
		// TODO Auto-generated method stub
		File file = new File(path);
		file = new File(file.getParent() + File.separator + "css"
				+ File.separator + "TX", "stylesheet.css");
		File file2 = new File(this.tempDir, this.stylesheet);
		FileOutputStream out = new FileOutputStream(file2);
		out.write(FileOperator.getBytesOfFile(file));
		out.close();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// String text = FileOperator
		// .readContent(new File(
		// "F:\\LRtec\\docs\\书坊代码转化工具-new\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-11.15\\OEBPS\\Text\\chapter0.xhtml"));
		// Parser parser = new Parser(text);
		// NodeList nodes = parser.parse(new NodeFilter() {
		//
		// @Override
		// public boolean accept(Node node) {
		// if (node instanceof ImageTag) {
		// ((ImageTag) node).removeAttribute("/");
		// System.out.println(node.toHtml());
		// ((ImageTag) node).setAttribute("/", null);
		// System.out.println(node.toHtml());
		//
		// return true;
		// }
		// return false;
		// }
		// });
		// String text = FileOperator
		// .readContent(new File(
		// "F:\\LRtec\\docs\\书坊代码转化工具-new\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-11.15\\OEBPS\\content.opf"));
		// Parser p = new Parser(text);
		// NodeList ns = p.parse(null);
		// for (int i = 0; i < ns.size(); i++) {
		// System.out.println(ns.elementAt(i).toHtml());
		// }
		// TagNode tag = new TagNode();
		// tag.setTagName("item");
		// tag.setAttribute("id", "\"Frontcover.xhtml\"");
		// tag.setAttribute("href", "\"Text/Fontcover.xhtml\"");
		// tag.setAttribute("media-type", "\"application/xhtml\"");
		// tag.setAttribute("/", null);
		// System.out.println(tag.toHtml());
		/*
		 * ==================================test================================
		 * ======
		 */
		// String dir = "F:\\LRtec\\callback\\测试1228\\测试1228";
		// RAR r = new RAR(dir + "\\translated", new TXEPUB(dir
		// + "\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-1221原始.epub", dir + "\\temp"));
		// r.unCompress();
		// System.out.println("extra finish!");
		// r.getEpub().translate();
		// r.compress();
		/* ==========================test tcp sort======================== */
		TXEPUB e = new TXEPUB("", "");
		System.out
				.println(e.sortNavPoint(FileOperator
						.readContent(new File(
								"F:\\LRtec\\callback\\测试1228\\测试1228\\货币信贷与人口周期影响下的中国经济转折点-洪迪华-1221原始\\OEBPS",
								"toc.ncx"))));
	}
}
