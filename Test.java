import java.util.*;
class Test 
{
	public static void main(String[] args) 
	{
//		int tt[][][] = new int[10][10][10];
//
//		int p =0;
//		for (int i=0;i<tt.length;i++)
//		{
//			for (int j=0;j<tt.length;j++)
//			{
//				for (int k=0;k<tt.length;k++)
//				{
//					
//					tt[i][j][k] = ++p;
//				}
//			}
//		}
//
//
//		System.out.println(tt[1][9][9]);


		String tt[][][] = new String[7][3][4];


		String course[] = new String[4];

		tt[0][0][0] = "COM111";
		tt[0][0][1] = "COM222";
		tt[0][0][2] = "COM333";
		tt[0][0][3] = "COM444";
//		tt[0][0] = Arrays.toString(course);
//		course = new String[4];
		tt[0][1][0] = "COM712";
//		tt[1][0] = Arrays.toString(course);

		

		//System.out.println(Arrays.deepToString(tt));
		

		double i = 10;
		double j =  4;

		double result = i/j;

		//System.out.printf("%.2f",result);
		
		String check = tt[3][0][0];


		String n  = "COM113(ND I)";

		System.out.println(n.substring(0,7));

	}
}
