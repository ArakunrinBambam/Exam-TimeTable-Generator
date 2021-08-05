public void generate1(String dept) {
		
		System.out.println("The Available Exam days is "+days+" days");
		System.out.println("The Available Exam periods is "+periods.length+" Periods");
		
		timetable = new String[days][periods.length][4];
		
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
			Statement st = con.createStatement();
			
//			for (int i=1;i<=days;i++)
//			{
//				System.out.println("Days "+i);
//
//			}
			for (int i=1;i<lvl.length;i++)
			{
				System.out.println(lvl[i]);
 
				ResultSet rs = st.executeQuery("Select * from TBLCOURSES where DEPARTMENT='"+dept+"' and LEVEL='"+lvl[i]+"'");

				if (!rs.next())
				{
					//alert("No Courses Registered for "+lvl[i]+" in "+dept+" Department","Info","error");
					System.out.println("No Courses Registered for "+lvl[i]+" in "+dept+" Department");;
				}else {
					int count = 0;
					do
					{
						int totals = Integer.parseInt(rs.getString("TOTAL_STUDENT"));
						//System.out.println(rs.getString("COURSE_CODE")+"-"+totals+" Counter: "+count);
						timetable[count][0][i-1] = rs.getString("COURSE_CODE")+"("+lvl[i]+")";
						count+=1;

						
					}
					while (rs.next());
				}



			}

			//System.out.println(Arrays.deepToString(timetable));

			for (int i=0;i<days;i++)
			{
				for (int j=0;j<periods.length;j++)
				{
					for (int k=1;k<lvl.length;k++)
					{
						System.out.print(timetable[i][j][k-1]);
					}
					System.out.print("\t");
				}
				System.out.println();
			}
			

//			for (int i=1;i<=days;i++)
//			{
//				System.out.println("DAY "+i);
//				System.out.println();
//
//				for (int j=0;j<periods.length;j++)
//				{
//					int p = j+1;
//					System.out.println("Period "+p);
//					System.out.println();
//					
//					
//				}
//			}
			
			

		}
		catch (Exception e){e.printStackTrace();}

	}

	public void generatEe(String dept)
	{
		venues = new ArrayList();
		courses = new ArrayList();
		
		System.out.println("The Available Exam days is "+days+" days");
		System.out.println("The Available Exam periods is "+periods.length+" Periods");
		
		getCourses(dept);
		getVenues();
		System.out.println("The Available courses is "+courses.size());
		System.out.println("The Available Venues is "+venues.size());
		getClasses();
		int cc = 0;
		mainloop:
		for (int i=0;i<days;i++)
		{
			int d =i+1;
			System.out.println("Day "+d);
			for (int j=0;j<periods.length;j++)
			{
				int p =  j+1;
				System.out.print("\t");
				System.out.println("PERIOD "+p);
				System.out.print("\t");
				System.out.println((String)((ArrayList)courses.get(cc)).get(0));
				System.out.println();
				cc = cc + 1;

				if (cc == courses.size())
				{
					break mainloop;
				}
				

			}
		}

//		for (int i=0;i < courses.size();i++)
//		{
//			for (int j=0;j<((ArrayList)courses.get(i)).size();j++)
//			{
//				System.out.print( (String)((ArrayList)courses.get(i)).get(j) + " ");
//			}
//			System.out.println();
//		}
//
//		System.out.println((String) ((ArrayList)courses.get(5)).get(2));
//		
//		System.out.println( "ND I has "+Collections.frequency(courses, "COM121 89 ND I")+" Courses");
		
		
	}