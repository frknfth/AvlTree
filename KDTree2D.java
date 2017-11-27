import java.util.ArrayList;
import java.util.List;

public class KDTree2D {
	
	protected static class Node implements Position{		
		Point2D element;
		Node parent;
		Node left;
		Node right;		
		
		public Node(Point2D e, Node above, Node leftChild, Node rightChild) {
			element = e;
			parent = above;
			left = leftChild;
			right = rightChild;
		}
	
		public Node getParent() 	{		return parent;		}
		public Node getLeft()		{		return left;		}
		public Node getRight() 		{		return right;		}
		public Point2D getElement() {		return element;		}

		public void setParent(Node parent) 		{		this.parent = parent;	}
		public void setLeft(Node left) 			{		this.left = left;		}
		public void setRight(Node right) 		{		this.right = right;		}
		public void setElement(Point2D element) {		this.element = element;	}		
	}
	
	protected Node createNode(Point2D e, Node parent, Node left, Node right) {
		return new Node(e, parent, left, right);
	}

	public KDTree2D() {
		root = null;
		size = 0;
	}
	
	public int size() {		return size;	}	
	
	public boolean isEmpty() { return size() == 0; }
	
	protected Node validate(Position p) throws IllegalArgumentException {
		if (!(p instanceof Node))
			throw new IllegalArgumentException("Not valid position type");
		Node node = (Node) p;       									// safe cast
		if (node.getParent() == node)
			throw new IllegalArgumentException("p is no longer in the tree");
		return node;
	}
	
	private Position root() { return root; }
	
	private Position parent(Position p) { 
		Node node = validate(p);
		return node.getParent();
	}
	private Position left(Position p) 	{
		Node node = validate(p);
		return node.getLeft(); 	
	}	
	private Position right(Position p) 	{ 
		Node node = validate(p);
	    return node.getRight(); 	
	}
	
	public Position addRoot(Point2D p) throws IllegalStateException {
		if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
		root = createNode(p, null, null, null);
		size = 1;
		return root;
	}

	public Position addLeft(Position p, Point2D left)	throws IllegalArgumentException {
		Node parent = validate(p);
	    if (parent.getLeft() != null)
	      throw new IllegalArgumentException("p already has a left child");
	    Node child = createNode(left, parent, null, null);
	    parent.setLeft(child);
	    size++;
	    return child;
	}
		  
	public Position addRight(Position p, Point2D right)	throws IllegalArgumentException {
		Node parent = validate(p);
	    if (parent.getRight() != null)
	      throw new IllegalArgumentException("p already has a right child");
	    Node child = createNode(right, parent, null, null);
	    parent.setRight(child);
	    size++;
	    return child;
	}
		
	public boolean isRoot(Position p) { return p == root(); }
	
	public int depth(Position p) throws IllegalArgumentException {
		if (isRoot(p))
			return 0;
		else
			return 1 + depth(parent(p));
	}
		
	public int numChildren(Position p) {
		int count=0;
		if (left(p) != null)	count++;
		if (right(p) != null)	count++;
		return count;
	}
	
	public Iterable<Position> children(Position p) {
		List<Position> snapshot = new ArrayList<>(2);    // max capacity of 2
		if (left(p) != null)
			snapshot.add(left(p));
		if (right(p) != null)
			snapshot.add(right(p));
		return snapshot;
	}

	public Iterable<Position> positions() { return preorder(); }

	public Iterable<Position> preorder() {
		List<Position> snapshot = new ArrayList<>();
		if (!isEmpty())
			preorderSubtree(root(), snapshot);
		return snapshot;
	}
	
	private void preorderSubtree(Position p, List<Position> snapshot) {
		snapshot.add(p);
		for (Position c : children(p))
			preorderSubtree(c, snapshot);
	}
	
	public void insert(Point2D point) {
		if(root() == null) {
			addRoot(point);
		}
		else {
			Position nodePosition = searching(point);
			Node node = validate(nodePosition);	
			
			if(depth(node) % 2 == 0) {
				if(point.getX() < node.getElement().getX()) 
					addLeft(node, point);
				else
					addRight(node, point);
			}
			else {
				if(point.getY() < node.getElement().getY())
					addLeft(node, point);
				else
					addRight(node, point);
			}
		}
	}
	
	private Position searching(Point2D point) {		
		return searchingAux(root(), point );
	}	
	private Position searchingAux(Position localRoot , Point2D point) {		
		Node temp = validate(localRoot);
		if	 (depth(localRoot) % 2 ==  0) {
			if(point.getX() < temp.getElement().getX()) {
				if(left(localRoot) == null)
					return localRoot;
				return searchingAux(left(localRoot), point);
			}
			else { // point.getX() > temp.getElement().getX()
				if(right(localRoot) == null)
					return localRoot;
				return searchingAux(right(localRoot), point);
			}
		} 
		else { //depth(localRoot) % 2 ==  1
			if(point.getY() < temp.getElement().getY()) {
				if(left(localRoot) == null) {
					return localRoot;
				}
				return searchingAux(left(localRoot), point);
			}
			else { // point.getY() > temp.getElement().getY()
				if(right(localRoot) == null)
					return localRoot;
				return searchingAux(right(localRoot), point);
			}
		}
	}
	
	public Point2D search(Point2D point) {
		return searchAux(root(), point );
	}
	
	private Point2D searchAux(Position localRoot, Point2D point) {
		if(localRoot == null )
			return null;		
		Node temp = validate(localRoot);		
		
		if	 (depth(localRoot) % 2 ==  0) {
			if(point.getX() < temp.getElement().getX()) 		
				return searchAux(left(localRoot), point);			
			else if(point.getX() == temp.getElement().getX() && point.getY() == temp.getElement().getY())
				return point;
			else				
				return searchAux(right(localRoot), point);			
		} 
		else {
			if(point.getY() < temp.getElement().getY()) 				
				return searchAux(left(localRoot), point);			
			else if(point.getX() == temp.getElement().getX() && point.getY() == temp.getElement().getY())
				return point;
			else 
				return searchAux(right(localRoot), point);			
		}		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		Iterable<Position> list = positions();
		for(Position a : list) {
			Node temp = validate(a);
			for(int i = 0; i < depth(a) ; i++) 
				sb.append("    ");
			sb.append(temp.getElement().toString()+"\n");
		}
		return sb.toString();
	}
	
	public Point2D findMin(int d) {
		Point2D point = root().getElement();
		if(d == 0)
			point = findMinAuxX(root());
		if(d == 1)
			point = findMinAuxY(root());
		
		return point;		
	}
	
	private Point2D findMinAuxX(Position x) {
		Node node = validate(x);
		if(depth(node) % 2 == 0) {
			if(left(x) != null) {
				return findMinAuxX(left(x));
			}
			return node.getElement();			
		}
		else {
			if(left(x) != null && right(x) != null) {
				Point2D sol = findMinAuxX(left(x));
				Point2D sag = findMinAuxX(right(x));
				double minNode = Math.min(sol.getX(), sag.getX());
				double minSol = Math.min(node.getElement().getX(), sag.getX());
				double minSag = Math.min(node.getElement().getX(), sol.getX());

				if(node.getElement().getX() < minNode )
					return node.getElement();
				if(sol.getX() < minSol)
					return sol;
				if(sag.getX() < minSag)
					return sag;
			}
			if(left(x) != null && right(x) == null) {
				Point2D sol = findMinAuxX(left(x));

				if(node.getElement().getX() < sol.getX() )
					return node.getElement();
				return sol;
			}
			if(left(x) == null && right(x) != null) {
				Point2D sag = findMinAuxX(right(x));

				if(node.getElement().getX() < sag.getX() )
					return node.getElement();
				return sag;
			}
			return node.getElement();					
		}		
	}	
	private Point2D findMinAuxY(Position y ) {
        Node node = validate(y);
        
        if(depth(node) % 2 == 1) {
            if(left(y) != null) {
                return findMinAuxY(left(y));
            }
            return node.getElement();
        }
        
        else {
            if(left(y) != null && right(y) != null) {
                Point2D sol = findMinAuxY(left(y));
                Point2D sag = findMinAuxY(right(y));
                double minNode = Math.min(sol.getY(), sag.getY());
                double minSol = Math.min(node.getElement().getY(), sag.getY());
                double minSag = Math.min(node.getElement().getY(), sol.getY());

                if(node.getElement().getY() < minNode )
                    return node.getElement();
                if(sol.getY() < minSol)
                    return sol;
                if(sag.getY() < minSag)
                    return sag;
            }
            if(left(y) != null && right(y) == null) {
                Point2D sol = findMinAuxY(left(y));

                if(node.getElement().getY() < sol.getY() )
                    return node.getElement();
                return sol;
            }
            if(left(y) == null && right(y) != null) {
                Point2D sag = findMinAuxY(right(y));

                if(node.getElement().getY() < sag.getY() )
                    return node.getElement();
                return sag;
            }
            return node.getElement();
        }			
	}
	
	public Point2D findMax(int d) {
		Point2D point = root().getElement();
		if(d == 0)
			point = findMaxAuxX(root());
		if(d == 1)
			point = findMaxAuxY(root());
		
		return point;		
	}
	
	private Point2D findMaxAuxX(Position x) {
		Node node = validate(x);
		if(depth(node) % 2 == 0) {
			if(right(x) != null) {
				return findMaxAuxX(right(x));
			}
			return node.getElement();			
		}
		else {
			if(left(x) != null && right(x) != null) {
				Point2D sol = findMaxAuxX(left(x));
				Point2D sag = findMaxAuxX(right(x));
				double maxNode = Math.max(sol.getX(), sag.getX());
				double maxSol = Math.max(node.getElement().getX(), sag.getX());
				double maxSag = Math.max(node.getElement().getX(), sol.getX());

				if(node.getElement().getX() > maxNode )
					return node.getElement();
				if(sol.getX() > maxSol)
					return sol;
				if(sag.getX() > maxSag)
					return sag;
			}
			if(left(x) != null && right(x) == null) {
				Point2D sol = findMaxAuxX(left(x));

				if(node.getElement().getX() > sol.getX() )
					return node.getElement();
				return sol;
			}
			if(left(x) == null && right(x) != null) {
				Point2D sag = findMaxAuxX(right(x));

				if(node.getElement().getX() > sag.getX() )
					return node.getElement();
				return sag;
			}
			return node.getElement();					
		}		
	}	
	
	private Point2D findMaxAuxY(Position y ) {
        Node node = validate(y);
		if(depth(node) % 2 == 1) {
			if(right(y) != null) 	
				return findMaxAuxY(right(y));			
			return node.getElement();			
		}		
		if(left(y) != null && right(y) != null) {
			Point2D sol = findMaxAuxY(left(y));
			Point2D sag = findMaxAuxY(right(y));
			double maxNode = Math.max(sol.getY(), sag.getY());
			double maxSol = Math.max(node.getElement().getY(), sag.getY());
			double maxSag = Math.max(node.getElement().getY(), sol.getY());
			
			if(node.getElement().getY() > maxNode )	
				return node.getElement();
			if(sol.getY() > maxSol)		
				return sol;		
			if(sag.getY() > maxSag)		
				return sag;
		}
		if(left(y) != null && right(y) == null) {
			Point2D sol = findMaxAuxY(left(y));
			if(node.getElement().getY() > sol.getY() )	
				return node.getElement();
			return sol;
		}
		if(left(y) == null && right(y) != null) {
			Point2D sag = findMaxAuxY(right(y));
			if(node.getElement().getY() > sag.getY() )	
				return node.getElement();
			return sag;
		}
		return node.getElement();	       
	}
	
	private Node root;
	private int size;
}
