package src.mas_lab;

public class Main {
	
	public static double EvaluateRange = 5.0;
	public static int MaxCapacity = 20;
	public static double MinBoldness = 0;
	public static double MaxBoldness = 1;
	public static int MaxTurns = 50;
	
	public static int MaxFriends = 10;

	//public static Launcher launch;
	
	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Launcher.instance().InitJade(10, 30);
		//launch = new Launcher();
		//launch.InitJade(5, 3);
	}

}
