
public class KDTree2DDriver {
	
	public static void main(String[] args) {
		
		KDTree2D obje = new KDTree2D();
		
		Point2D bir = new Point2D(30, 40);
		Point2D iki = new Point2D(5, 25);
		Point2D uc = new Point2D(18, 10);
		Point2D dort = new Point2D(80, 90);
		Point2D bes = new Point2D(55, 30);
		Point2D alti = new Point2D(35, 45);
		
		obje.insert(bir);
		obje.insert(iki);
		obje.insert(uc);
		obje.insert(dort);
		obje.insert(bes);
		obje.insert(alti);
		obje.insert(new Point2D(20,35));
		obje.insert(new Point2D(40,100));
		obje.insert(new Point2D(10,9));
		obje.insert(new Point2D(20,22));
		System.out.println("toString :\n"+obje.toString());

		System.out.println(obje.search(new Point2D(20,35)).toString());
		System.out.println(obje.search(new Point2D(19,35)));

		
		System.out.println(obje.findMin(0).toString());		
		System.out.println(obje.findMin(1).toString());		
		System.out.println(obje.findMax(0).toString());		
		System.out.println(obje.findMax(1).toString());		

	}
}
