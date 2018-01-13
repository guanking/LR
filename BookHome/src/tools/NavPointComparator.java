package tools;

import java.util.Comparator;
import java.util.LinkedList;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

public class NavPointComparator implements Comparator<LinkedList<Node>> {

	@Override
	public int compare(LinkedList<Node> o1, LinkedList<Node> o2) {
		// TODO Auto-generated method stub
		int rank1 = 0, rank2 = 0;
		if (!(o1.getFirst() instanceof TagNode)) {
			System.out.println("first of o1 is not a tagNode");
		} else {
			rank1 = Integer
					.parseInt(((TagNode) o1.getFirst()).getAttribute("playOrder"));
		}
		if (!(o2.getFirst() instanceof TagNode)) {
			System.out.println("first of o2 is not a tagNode");
		} else {
			rank2 = Integer
					.parseInt(((TagNode) o2.getFirst()).getAttribute("playOrder"));
		}
		return rank1 >rank2 ? 1 : -1;
	}

}