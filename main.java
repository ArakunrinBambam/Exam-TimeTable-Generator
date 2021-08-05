import org.apache.derby.jdbc.*;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.*;
import java.sql.*;
import java.io.*;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.FontFactory;

class main extends JFrame implements ActionListener
{
	JPanel back, acpage, avpage, gpage, dcpage, dvpage,espage,lpage;

	JMenuBar mbar;
	JMenu file,sets, dept, cour, ven;
	JMenuItem addD, addC, addV, genT, addT, delC, delV, allC, allV,eset,lout;
	JComboBox jc1,jc2;
	
	JButton bt1,bt2,bt3,bt4,bt5,bt6,login,close;
	
	JPasswordField pf;
	JTextField t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,ut;
	JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14,l15,l16,l17,l18,l19,l20,title,ul,pl;

	Connection con;
	ResultSet rs,rs1;
	ResultSetMetaData rd;
	ResultSetMetaData rm;
	Statement st;
	String url = "jdbc:derby:c:/db/timetable;create=true";

	String depts[] = {"Select","Accountancy","Architectural Tech", "Businesss Administration", "Civil Engineering", "Computer Science", "Computer Engineering", "Electrical Engineering",  "Fine Art","LIS","Marketing", "Mass Communication", "Mechanical Engineering", "Public Admin", "Purchasing & Supply", "Statistics", "Science Lab Tech"};
	String lvl[] = {"Select", "ND I", "ND II", "HND I", "HND II"};
	String periods[] = {"9am - 11am", "12pm - 2pm", "3pm - 5pm"};

	String timetable[][][];
	String venuetable[][][];
	Vector rows;

	String vens[];
	String venc[];
	
	ArrayList venues;
	ArrayList courses;

	String path = "C:/TimeTableReports/";
	
	Document document;

	private Random ran = new Random();
	private Random ranp = new Random();
	

	int days = 10;
	int x = 800;
	int y = 600;



	public main() {
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e){}

		setTitle("Examination Time-Table Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(x,y);
		setLayout(null);

		back = new JPanel(null) {
			{setOpaque(false);};

			public void paintComponent(Graphics g) {
				Image img = new ImageIcon(this.getClass().getResource("img/back.png")).getImage();
				g.drawImage(img,0,0,null);
			}
		};
		back.setBounds(0,0,x,y);											
		

		lpage =  new LoginPage();
		lpage.setBounds(x/2-180,y/2-130,360,180);
		lpage.setBorder(BorderFactory.createLineBorder(new Color(0,146,63), 2, true));
		
		back.add(lpage);
		
		add(back);
		setLocationRelativeTo(null);
		this.connectDB();
//menu bar configuration
		mbar = new JMenuBar();

		file = new JMenu("File");
		sets = new JMenu("Settings");
		file.setEnabled(false);
		sets.setEnabled(false);
		ven  = new JMenu("Venue");
		sets.add(ven);

		cour  = new JMenu("Course");
		sets.add(cour);

		eset = new JMenuItem("Exam Settings");
		eset.addActionListener(main.this);
		sets.add(eset);

		addC = new JMenuItem("Add Course");
		addC.addActionListener(main.this);
		cour.add(addC);

		allC = new JMenuItem("All Courses");
		allC.addActionListener(main.this);
		cour.add(allC);

		delC = new JMenuItem("Delete Course");
		delC.addActionListener(main.this);
		cour.add(delC);

		addV = new JMenuItem("Add Venue");
		addV.addActionListener(main.this);
		ven.add(addV);
		
		allV = new JMenuItem("All Venues");
		allV.addActionListener(main.this);
		ven.add(allV);

		delV = new JMenuItem("Delete Venue");
		delV.addActionListener(main.this);
		ven.add(delV);


		genT = new JMenuItem("TimeTable Generation Page");
		genT.addActionListener(main.this);
		file.add(genT);

		lout = new JMenuItem("Logout");
		lout.addActionListener(main.this);
		file.add(lout);
		
		mbar.add(file);
		mbar.add(sets);
		
		//mbar.setEnabled(false);
		setJMenuBar(mbar);
		
		setVisible(true);
	}

//Login Page
public class LoginPage extends JPanel
{
	public LoginPage() {
		setLayout(null);
		
		setOpaque(false);
		setBackground(Color.WHITE);


		JPanel b = new JPanel(null){
			public void paintComponent(Graphics g){
				Image img = new ImageIcon(this.getClass().getResource("img/lback.png")).getImage();
				g.drawImage(img,0,0,null);
			}
		};
		
		title=new JLabel("Login Page");
		title.setBounds(0,0,370,35);
		title.setFont(new Font("Tahoma",Font.BOLD,11));
		title.setForeground(new Color(255,252,199));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(0,146,63));
		title.setOpaque(true);
		b.add(title);

		ul=new JLabel("Username:");
		ul.setFont(new Font("Tahoma",Font.BOLD,11));
		ul.setForeground(new Color(0,146,63));
		ul.setBounds(40,60,120,20);
		b.add(ul);

		ut=new JTextField();
		ut.setBounds(130,60,180,25);
		ut.setFont(new Font("Tahoma",Font.BOLD,11));
		ut.setBorder(BorderFactory.createLineBorder(new Color(0,146,63),1));
		ut.setBackground(new Color(255,252,199));
		ut.setForeground(new Color(0,146,63));
		b.add(ut);

		pl=new JLabel("Password:");
		pl.setFont(new Font("Tahoma",Font.BOLD,11));
		pl.setForeground(new Color(0,146,63));
		pl.setBounds(40,90,120,20);
		add(pl);

		pf=new JPasswordField();
		pf.setBounds(130,90,180,25);
		pf.setFont(new Font("Tahoma",Font.BOLD,11));
		pf.setBorder(BorderFactory.createLineBorder(new Color(0,146,63),1));
		pf.setBackground(new Color(255,252,199));
		pf.setForeground(new Color(0,146,63));
		b.add(pf);

		login=new JButton("Login");
		login.setBounds(130,130,80,25);
		login.addActionListener(main.this);
		b.add(login);

		close=new JButton("Close");
		close.setBounds(220,130,80,25);
		close.addActionListener(main.this);
		b.add(close);


		b.setBounds(2,0,356,178);
		add(b);
	}

}
//login, logout and closep
public void login(String username, String password) {
		
		if (username.equals("Admin") && password.equals("pass"))
		{
			alert("Login Successful","success","success"); 
			sets.setEnabled(true);
			file.setEnabled(true);
			back.remove(lpage);
			repaint();
		}else {

			JOptionPane.showMessageDialog(null, "Incorrect Login Credentials", "Wrong Details!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void logout() {
		
		int i = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Log Out?", "Confirm!", JOptionPane.YES_NO_OPTION);

		if (i == JOptionPane.YES_OPTION)
		{
				lpage =  new LoginPage();
				lpage.setBounds(x/2-180,y/2-130,360,180);
				lpage.setBorder(BorderFactory.createLineBorder(new Color(0,146,63), 2, true));
				file.setEnabled(false);
				sets.setEnabled(false);

				back.removeAll();
				back.add(lpage);
				repaint();
		}
	
	}

	public void closeP() {

		int i = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Exit This Program?", "Confirm!", JOptionPane.YES_NO_OPTION);

		if (i == JOptionPane.YES_OPTION)
		{
			System.exit(1);
		}

	}
// Add Course
public class ACoursePage extends JPanel 
{
	public ACoursePage() 
	{
		setLayout(null);
		setOpaque(false);

		title=new JLabel("ADD NEW COURSE");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(24,29,123));
		title.setOpaque(true);
		add(title);

		l1=new JLabel("Department:");
		l1.setFont(new Font("Tahoma",Font.BOLD,11));
		l1.setForeground(new Color(24,29,123));
		l1.setBounds(40,50,120,20);
		add(l1);

		jc1 = new JComboBox(depts);
		jc1.setBounds(130,50,180,25);
		jc1.setFont(new Font("Tahoma",Font.BOLD,11));
		jc1.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),1));
		jc1.setBackground(new Color(255,255,255));
		jc1.setForeground(new Color(24,29,123));
		add(jc1);

		l7=new JLabel("Level:");
		l7.setFont(new Font("Tahoma",Font.BOLD,11));
		l7.setForeground(new Color(24,29,123));
		l7.setBounds(40,80,120,20);
		add(l7);

		jc2 = new JComboBox(lvl);
		jc2.setBounds(130,80,180,25);
		jc2.setFont(new Font("Tahoma",Font.BOLD,11));
		jc2.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),1));
		jc2.setBackground(new Color(255,255,255));
		jc2.setForeground(new Color(24,29,123));
		add(jc2);

		l2=new JLabel("Course Code:");
		l2.setFont(new Font("Tahoma",Font.BOLD,11));
		l2.setForeground(new Color(24,29,123));
		l2.setBounds(40,110,120,20);
		add(l2);

		t1=new JTextField();
		t1.setBounds(130,110,180,25);
		t1.setFont(new Font("Tahoma",Font.BOLD,11));
		t1.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),1));
		t1.setBackground(new Color(255,255,255));
		t1.setForeground(new Color(24,29,123));
		add(t1);

		l3=new JLabel("Course Title:");
		l3.setFont(new Font("Tahoma",Font.BOLD,11));
		l3.setForeground(new Color(24,29,123));
		l3.setBounds(40,140,120,20);
		add(l3);

		t2=new JTextField();
		t2.setBounds(130,140,180,25);
		t2.setFont(new Font("Tahoma",Font.BOLD,11));
		t2.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),1));
		t2.setBackground(new Color(255,255,255));
		t2.setForeground(new Color(24,29,123));
		add(t2);

		l4=new JLabel("Total Student:");
		l4.setFont(new Font("Tahoma",Font.BOLD,11));
		l4.setForeground(new Color(24,29,123));
		l4.setBounds(40,170,120,20);
		add(l4);

		t3=new JTextField();
		t3.setBounds(130,170,180,25);
		t3.setFont(new Font("Tahoma",Font.BOLD,11));
		t3.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),1));
		t3.setBackground(new Color(255,255,255));
		t3.setForeground(new Color(24,29,123));
		add(t3);
		
		bt1=new JButton("Add Course");
		bt1.setBounds(140,210,120,25);
		bt1.addActionListener(main.this);
		add(bt1);
	}
}


//edit course page
public class DCoursePage extends JPanel
{
	public DCoursePage()
	{
		setLayout(null);
		setOpaque(false);

		title=new JLabel("DELETE COURSE");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(255,0,0));
		title.setOpaque(true);
		add(title);

		l1=new JLabel("Course Code:");
		l1.setFont(new Font("Tahoma",Font.BOLD,11));
		l1.setForeground(new Color(255,0,0));
		l1.setBounds(40,50,120,20);
		add(l1);

		t1=new JTextField();
		t1.setBounds(130,50,180,25);
		t1.setFont(new Font("Tahoma",Font.BOLD,11));
		t1.setBorder(BorderFactory.createLineBorder(new Color(255,0,0),1));
		t1.setBackground(new Color(255,255,255));
		t1.setForeground(new Color(255,0,0));
		add(t1);
		
		bt4=new JButton("Delete Course");
		bt4.setBounds(150,90,120,25);
		bt4.addActionListener(main.this);
		add(bt4);
	}
}


// Add Venue Page

public class AVenuePage extends JPanel
{
	public AVenuePage()
	{
		setLayout(null);
		setOpaque(false);

		title=new JLabel("ADD NEW VENUE");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(125,167,43));
		title.setOpaque(true);
		add(title);

		l5=new JLabel("Venue:");
		l5.setFont(new Font("Tahoma",Font.BOLD,11));
		l5.setForeground(new Color(125,167,43));
		l5.setBounds(50,50,120,20);
		add(l5);

		t4 = new JTextField();
		t4.setBounds(140,50,180,25);
		t4.setFont(new Font("Tahoma",Font.BOLD,11));
		t4.setBorder(BorderFactory.createLineBorder(new Color(125,167,43),1));
		t4.setBackground(new Color(255,255,255));
		t4.setForeground(new Color(125,167,43));
		add(t4);

		l6=new JLabel("Capacity:");
		l6.setFont(new Font("Tahoma",Font.BOLD,11));
		l6.setForeground(new Color(125,167,43));
		l6.setBounds(50,80,120,20);
		add(l6);

		t5=new JTextField();
		t5.setBounds(140,80,180,25);
		t5.setFont(new Font("Tahoma",Font.BOLD,11));
		t5.setBorder(BorderFactory.createLineBorder(new Color(125,167,43),1));
		t5.setBackground(new Color(255,255,255));
		t5.setForeground(new Color(125,167,43));
		add(t5);
		
		bt2=new JButton("Add Venue");
		bt2.setBounds(150,120,120,25);
		bt2.addActionListener(main.this);
		add(bt2);
	}
}

//Delete Venue Page

public class DVenuePage extends JPanel
{
	public DVenuePage()
	{
		setLayout(null);
		setOpaque(false);

		title=new JLabel("DELETE VENUE");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(217,15,87));
		title.setOpaque(true);
		add(title);

		l5=new JLabel("Venue:");
		l5.setFont(new Font("Tahoma",Font.BOLD,11));
		l5.setForeground(new Color(217,15,87));
		l5.setBounds(50,50,120,20);
		add(l5);

		t4 = new JTextField();
		t4.setBounds(140,50,180,25);
		t4.setFont(new Font("Tahoma",Font.BOLD,11));
		t4.setBorder(BorderFactory.createLineBorder(new Color(217,15,87),1));
		t4.setBackground(new Color(255,255,255));
		t4.setForeground(new Color(217,15,87));
		add(t4);
		
		bt5=new JButton("Delete Venue");
		bt5.setBounds(150,90,120,25);
		bt5.addActionListener(main.this);
		add(bt5);
	}
}


//Time Table Generation Page
public class GenerationPage extends JPanel
{
	public GenerationPage() 
	{
		setLayout(null);
		setOpaque(false);

		title=new JLabel("GENERATION PAGE");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(new Color(241,251,203));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(18,132,117));
		title.setOpaque(true);
		add(title);

		l1=new JLabel("Department:");
		l1.setFont(new Font("Tahoma",Font.BOLD,11));
		l1.setForeground(new Color(18,132,117));
		l1.setBounds(40,60,120,20);
		add(l1);

		jc1 = new JComboBox(depts);
		jc1.setBounds(130,60,180,25);
		jc1.setFont(new Font("Tahoma",Font.BOLD,11));
		jc1.setBorder(BorderFactory.createLineBorder(new Color(18,132,117),1));
		jc1.setBackground(new Color(255,255,255));
		jc1.setForeground(new Color(18,132,117));
		add(jc1);
		
		bt3=new JButton("Generate");
		bt3.setBounds(140,100,120,25);
		bt3.addActionListener(main.this);
		add(bt3);
	}
}

//Exam Settings 

public class ExamSetting extends JPanel
{
	public ExamSetting() {

		setLayout(null);
		setOpaque(false);

		title=new JLabel("EXAM SETTINGS");
		title.setBounds(0,0,400,35);
		title.setFont(new Font("Tahoma",Font.BOLD,13));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(19,214,111));
		title.setOpaque(true);
		add(title);

		l5=new JLabel("No of Days:");
		l5.setFont(new Font("Tahoma",Font.BOLD,11));
		l5.setForeground(new Color(19,214,111));
		l5.setBounds(50,50,120,20);
		add(l5);

		t6 = new JTextField();
		t6.setBounds(140,50,180,25);
		t6.setFont(new Font("Tahoma",Font.BOLD,11));
		t6.setBorder(BorderFactory.createLineBorder(new Color(19,214,111),1));
		t6.setBackground(new Color(255,255,255));
		t6.setForeground(new Color(19,214,111));
		add(t6);

		t6.setText(String.valueOf(days));
		
		bt6=new JButton("Update");
		bt6.setBounds(150,120,120,25);
		bt6.addActionListener(main.this);
		add(bt6);
			
	
	}
}

//Click events 
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==addC)
		{
			acpage = new ACoursePage();
			acpage.setBounds(200,150,400,250);
			acpage.setBorder(BorderFactory.createLineBorder(new Color(24,29,123),2));
			back.removeAll();
			back.add(acpage);
			repaint();
		}else if (e.getSource()==addV)
		{
			avpage = new AVenuePage();
			avpage.setBounds(200,170,400,160);
			avpage.setBorder(BorderFactory.createLineBorder(new Color(125,167,43),2));
			back.removeAll();
			back.add(avpage);
			repaint();
		} else if (e.getSource()==delC)
		{
			dcpage = new DCoursePage();
			dcpage.setBounds(200,180,400,125);
			dcpage.setBorder(BorderFactory.createLineBorder(new Color(255,0,0),2));
			back.removeAll();
			back.add(dcpage);
			repaint();
		} else if (e.getSource()==delV)
		{
			dvpage = new DVenuePage();
			dvpage.setBounds(200,170,400,125);
			dvpage.setBorder(BorderFactory.createLineBorder(new Color(217,15,43),2));
			back.removeAll();
			back.add(dvpage);
			repaint();
		} else if (e.getSource()==eset)
		{
			espage = new ExamSetting();
			espage.setBounds(200,170,400,160);
			espage.setBorder(BorderFactory.createLineBorder(new Color(19,214,111),2));
			back.removeAll();
			back.add(espage);
			repaint();
		} else if (e.getSource()==genT)
		{
			gpage = new GenerationPage();
			gpage.setBounds(200,170,400,160);
			gpage.setBorder(BorderFactory.createLineBorder(new Color(18,132,117),2));
			back.removeAll();
			back.add(gpage);
			repaint();
		}else if (e.getSource()==bt1)
		{
			String dept = (String)jc1.getSelectedItem();
			String lvl = (String)jc2.getSelectedItem();
			String cc = t1.getText();
			String ct = t2.getText();
			String ts = t3.getText();
		
			if (dept.equals("Select") || lvl.equals("Select") || cc.equals("") || ct.equals("") || ts.equals(""))
			{
				JOptionPane.showMessageDialog(this,"All Fields Are Compulsory","Missing Field(s)",JOptionPane.ERROR_MESSAGE);
			}else {
				this.addCourse(dept,lvl,cc,ct,ts);
			}

		}else if (e.getSource()==bt2)
		{
			String name = t4.getText();
			String cap = t5.getText();

			if (name.equals("") || cap.equals(""))
			{
				alert("All Fields are Compulsory", "Missing Field(s)", "error");
			}else {
				this.addVenue(name, cap);
			}
		}else if (e.getSource()==bt3)
		{
			String dept = (String)jc1.getSelectedItem();

			if (dept.equals("Select"))
			{
				alert("Please Select Department","Dept??","error");
			}else {
				
				generate(dept);
			}
		}else if (e.getSource()==bt4)
		{
			String cc = t1.getText();

			if (cc.equals(""))
			{
				alert("Please Supply the Course Code","Course??","error");
			}else {
				
				deletec(cc);
			}
		}else if (e.getSource()==bt5)
		{
			String vn = t4.getText();

			if (vn.equals(""))
			{
				alert("Please Supply the Venue Name","Venue??","error");
			}else {
				
				deletev(vn);
			}
		}else if (e.getSource()==bt6)
		{
			String nofd = t6.getText();
			
			if (nofd.equals(""))
			{
				alert("Please Supply the number of Days","??","error");
			}else {
				
				days = Integer.parseInt(nofd);
				alert("No of Days updated successfully","Success","success");
			}
		}else if (e.getSource()==allC)
		{
			genCpdf();

		}else if (e.getSource()==allV)
		{
			
			genVpdf();
		}else if (e.getSource()==login)
		{
			String un = ut.getText();
			String up = pf.getText();

			if (un.equals("") || up.equals(""))
			{
				alert("Please Supply Your Credentials","Missing Detials","error");
			}else {
				login(un,up);
			}
		}else if (e.getSource()==close)
		{
			closeP();
		}else if (e.getSource()==lout)
		{
			logout();
		}

	}
	
	public void deletec(String cc)
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
		
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLCOURSES where COURSE_CODE='"+cc+"'");

			if (rs.next())
			{
				int rss = st.executeUpdate("DELETE from TBLCOURSES where COURSE_CODE='"+cc+"'");

				if (rss != -1)
				{
					alert("Course Deleted Successfully!","Success","success");
					t1.setText("");
				}else {
					alert("Error Occured! Try Again Later","Oops!","error");
				}
			}else {
				
				alert("Invalid Course Code! Course does not Exists!","Course??","error");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void deletev(String vn)
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
		
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLVENUES where VENUE_NAME='"+vn+"'");

			if (rs.next())
			{
				int rss = st.executeUpdate("DELETE from TBLVENUES where VENUE_NAME='"+vn+"'");

				if (rss != -1)
				{
					alert("Venue Deleted Successfully!","Success","success");
					t4.setText("");
				}else {
					alert("Error Occured! Try Again Later","Oops!","error");
				}
			}else {
				
				alert("Invalid Venue Name! Venue does not Exists!","Venue??","error");
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public void addCourse(String dept, String lvl, String cc, String ct, String ts) {

		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
		
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLCOURSES where COURSE_CODE='"+cc+"'");

			if (rs.next())
			{
				alert("Course Already Exists","Error","error");
			}else {


				PreparedStatement ps = con.prepareStatement("INSERT INTO TBLCOURSES (DEPARTMENT, LEVEL, COURSE_CODE, COURSE_TITLE, TOTAL_STUDENT) values (?,?,?,?,?)");
				ps.setString(1, dept);
				ps.setString(2, lvl);
				ps.setString(3, cc);
				ps.setString(4, ct);
				ps.setString(5, ts);
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null,"Course Added Successfully:","Success!",JOptionPane.INFORMATION_MESSAGE);
				this.clearCourse();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void addVenue(String name, String capacity) {

		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
		
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLVENUES where VENUE_NAME='"+name+"'");

			if (rs.next())
			{
				alert("Venue Already Exists", "Error", "error");

			}else {

				PreparedStatement ps = con.prepareStatement("INSERT INTO TBLVENUES (VENUE_NAME, CAPACITY) values (?,?)");
				ps.setString(1, name);
				ps.setInt(2, Integer.parseInt(capacity));
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null,"Venue Added Successfully","Success!",JOptionPane.INFORMATION_MESSAGE);
				this.clearVenue();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	//Generate Timetable method
	public void generate(String dept){

		courses = new ArrayList();
		venues = new ArrayList();
		System.out.println("The Available Exam days is "+days+" days");
		System.out.println("The Available Exam periods is "+periods.length+" Periods");
		
		timetable = new String[days][periods.length][lvl.length];
		venuetable= new String[days][periods.length][lvl.length];
		getCourses(dept);
		getVenues();
		
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
			Statement st = con.createStatement();
			
			
			for (int i=1;i<lvl.length;i++)
			{
				int total = getTlevel(i);
				System.out.println(lvl[i]+" has "+total+" Courses");
 
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
						System.out.println(rs.getString("COURSE_CODE")+"-"+totals+" Counter: "+count);
						int d, p;
						String check = "";
						gen:
						do
						{
							d = ran.nextInt(days);
							p = ranp.nextInt(periods.length);
							check = timetable[d][p][i-1];
							int c = 0;
							for (int g=0;g<periods.length;g++)
							{
								
								if (timetable[d][g][i-1]!=null)
								{
									c+=1;
								}
								
							}
							
							//check if a course is more than once in a day and allow is the department course is greater than days 
							if ((c > 0) && (total <= (days-1)))
							{
								check = "";
							}

						}
						while (check!=null);
						
						//search for venues
						lblven:
						for (int l=0;l<venues.size();l++)
						{

							if (Integer.parseInt(((ArrayList)venues.get(l)).get(1).toString()) >= totals)
							{
								String name =(String) ((ArrayList)venues.get(l)).get(0);
								int cap = Integer.parseInt(((ArrayList)venues.get(l)).get(1).toString());
								//System.out.println(((ArrayList)venues.get(l)).get(0)+" has "+((ArrayList)venues.get(l)).get(1)+" Capacity");
								//venuetable[d][p][i-1] = 
								
								int tc_cap =0;

								//iterate throught the period and day to be sure no level is using the current venue
								for (int m=1;m<lvl.length;m++)
								{
									
									if (venuetable[d][p][m-1]==name)
									{
										String ctoken = timetable[d][p][m-1];
										
										//student registered for the current course
										int nofs = nofs(ctoken.substring(0,6));

										tc_cap+=nofs;
										
									}

								}
								
								//if the venue has been used
								if (tc_cap > 0)
								{	
									//venue capacity minus the used 
									int rem = cap - tc_cap;
									
									if (rem >= totals)
									{
										venuetable[d][p][i-1] = name;
										break lblven;
									}

								}else {
									
									venuetable[d][p][i-1] = name;
									break lblven;
								}
							
							}
						}
								
							
			
						
						timetable[d][p][i-1] = rs.getString("COURSE_CODE")+"("+lvl[i]+")";

						//+"("+rs.getString("COURSE_TITLE")+")"
						count+=1;
						

						
					}
					while (rs.next());
					
				}
				


			}

			int totalv = 0;
			for (int i=0;i<days;i++)
			{
				for (int j=0;j<periods.length;j++)
				{
					for (int k=1;k<lvl.length;k++)
					{
						System.out.print(timetable[i][j][k-1]);
						if (timetable[i][j][k-1] != null)
						{
							totalv+=1;
						}
					}
					System.out.print("\t");
				}
				System.out.println();
//				
//				for (int j=0;j<periods.length;j++)
//				{
//					for (int k=1;k<lvl.length;k++)
//					{
//						System.out.print(venuetable[i][j][k-1]);
//						
//					}
//					System.out.print("\t");
//				}
//				System.out.println();
			}
			System.out.println(totalv);
			
			if (courses.size()> 0)
			{
				genPdf(dept);
			}
			
		}
		catch (Exception e){e.printStackTrace();}
	}

	//generate pdf 
	public void genPdf(String dept) {
		
		document = new Document();
		document.setPageSize(PageSize.A4.rotate());

		try
		{
			checkDir();
			PdfWriter.getInstance(document, new FileOutputStream(path+dept+"_Time_Table"+".pdf"));

			
			document.open();

			
			PdfPTable table = new PdfPTable(2);

			PdfPTable tableh  = new PdfPTable(periods.length+1);

			PdfPTable tableh1 = new PdfPTable(1);
			
			PdfPTable data[][] = new PdfPTable[days][periods.length] ;
			PdfPTable datav[][] = new PdfPTable[days][periods.length] ;
			
		
			tableh1.setSpacingBefore(10.0f);
			
			tableh1.setSpacingAfter(18.0f);

			table.setSpacingBefore(5.0f);
			
			table.setSpacingAfter(18.0f);
											

			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//data.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			tableh1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			
			tableh1.addCell(new Phrase(dept.toUpperCase()+" TIMETABLE",FontFactory.getFont(FontFactory.HELVETICA,16)));

								

//								tableh.setTotalWidth(new float[] {100,150});
//								tableh.setLockedWidth(true);

			tableh.addCell(new Phrase("DAYS\\PERIODS",FontFactory.getFont(FontFactory.HELVETICA,10)));
			for (int i=0;i<periods.length;i++)
			{
				tableh.addCell(new Phrase(periods[i],FontFactory.getFont(FontFactory.HELVETICA,10)));
			}
			
			for (int i=0;i<days;i++)
			{	
				int d=i+1;
				tableh.addCell(new Phrase("Day "+d+" Courses",FontFactory.getFont(FontFactory.HELVETICA,10)));
				for (int j=0;j<periods.length;j++)
				{
					data[i][j]= new PdfPTable(lvl.length-1);
					data[i][j].getDefaultCell().setBorder(Rectangle.NO_BORDER);
					data[i][j].setWidthPercentage(100);
					for (int k=1;k<lvl.length;k++)
					{
						
						data[i][j].addCell(new Phrase(timetable[i][j][k-1],FontFactory.getFont(FontFactory.HELVETICA,8)));
					}
					PdfPCell cell = new PdfPCell(new Phrase("Cell 1"));
					cell.addElement(data[i][j]);
					tableh.addCell(cell);
				}

				tableh.addCell(new Phrase("Venues",FontFactory.getFont(FontFactory.HELVETICA,10)));
				for (int j=0;j<periods.length;j++)
				{
					datav[i][j]= new PdfPTable(lvl.length-1);
					datav[i][j].getDefaultCell().setBorder(Rectangle.NO_BORDER);
					datav[i][j].setWidthPercentage(100);
					for (int k=1;k<lvl.length;k++)
					{
						
						datav[i][j].addCell(new Phrase(venuetable[i][j][k-1],FontFactory.getFont(FontFactory.HELVETICA,8)));
					}
					PdfPCell cellv = new PdfPCell(new Phrase("Cell 1"));
					cellv.addElement(datav[i][j]);
					tableh.addCell(cellv);
				}
			}
			
		
			
			//tableh.setTotalWidth(new float [] {1,3,3,3});
			tableh.setWidths(new float [] {1,3,3,3});
			tableh.setWidthPercentage(100);

			//tableh.setLockedWidth(true); 

			
		

		


			

			
		
			document.add(tableh1);
			//document.add(table);
			document.add(tableh);
		

			document.close();

			JOptionPane.showMessageDialog(null, "The List Has Been Generated Successfully");

			JOptionPane.showMessageDialog(null, "Please Wait While the List is being Open");

			
			File openFile = new File(path+dept+"_Time_Table"+".pdf");
					
			boolean isDesktop;
			
			isDesktop = Desktop.isDesktopSupported();
				if (isDesktop)
				{	
					Desktop.getDesktop().open(openFile);
				}else 
					System.out.print("AWT is not supported on yours system");

			//Runtime.getRuntime().exec("Explorer" + (String) new File("Report.pdf").getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		


	}
	//generate list of all courses 
	public void genCpdf() {
		
		document = new Document();
		document.setPageSize(PageSize.A4.rotate());

		try
		{
			checkDir();
			PdfWriter.getInstance(document, new FileOutputStream(path+"All_Registered_Courses"+".pdf"));

			
			document.open();

			
			PdfPTable table = new PdfPTable(2);

			PdfPTable tableh  = new PdfPTable(5);

			PdfPTable tableh1 = new PdfPTable(1);
			
			
			
		
			tableh1.setSpacingBefore(10.0f);
			
			tableh1.setSpacingAfter(18.0f);

			table.setSpacingBefore(5.0f);
			
			table.setSpacingAfter(18.0f);
											

			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//data.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			tableh1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			
			tableh1.addCell(new Phrase("ALL REGISTERED COURSES",FontFactory.getFont(FontFactory.HELVETICA,16)));

								

//								tableh.setTotalWidth(new float[] {100,150});
//								tableh.setLockedWidth(true);

//DEPARTMENT VARCHAR(100), LEVEL VARCHAR(7), COURSE_CODE VARCHAR(10), COURSE_TITLE VARCHAR(100), TOTAL_STUDENT VARCHAR(100)

			tableh.addCell(new Phrase("DEPARTMENT",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("LEVEL",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("COURSE CODE",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("COURSE TITLE",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("TOTAL STUDENT",FontFactory.getFont(FontFactory.HELVETICA,10)));
			
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLCOURSES ORDER BY DEPARTMENT");
			
			while (rs.next())
			{
				tableh.addCell(new Phrase(rs.getString("DEPARTMENT"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(rs.getString("LEVEL"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(rs.getString("COURSE_CODE"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(rs.getString("COURSE_TITLE"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(""+rs.getInt("TOTAL_STUDENT"),FontFactory.getFont(FontFactory.HELVETICA,8)));
			}
			
			
		
			
			//tableh.setTotalWidth(new float [] {1,3,3,3});
			//tableh.setWidths(new float [] {1,3,3,3});
			tableh.setWidthPercentage(100);

			//tableh.setLockedWidth(true); 

			
		

		


			

			
		
			document.add(tableh1);
			//document.add(table);
			document.add(tableh);
		

			document.close();

			JOptionPane.showMessageDialog(null, "The List Has Been Generated Successfully");

			JOptionPane.showMessageDialog(null, "Please Wait While the List is being Open");

			
			File openFile = new File(path+"All_Registered_Courses"+".pdf");
					
			boolean isDesktop;
			
			isDesktop = Desktop.isDesktopSupported();
				if (isDesktop)
				{	
					Desktop.getDesktop().open(openFile);
				}else 
					System.out.print("AWT is not supported on yours system");

			//Runtime.getRuntime().exec("Explorer" + (String) new File("Report.pdf").getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		


	
	}

	//genreate list of venues 
	public void genVpdf()
	{
		document = new Document();
		document.setPageSize(PageSize.A4);

		try
		{
			checkDir();
			PdfWriter.getInstance(document, new FileOutputStream(path+"All_Registered_Venues"+".pdf"));

			
			document.open();

			
			PdfPTable table = new PdfPTable(2);

			PdfPTable tableh  = new PdfPTable(3);

			PdfPTable tableh1 = new PdfPTable(1);
			
			
			
		
			tableh1.setSpacingBefore(10.0f);
			
			tableh1.setSpacingAfter(18.0f);

			table.setSpacingBefore(5.0f);
			
			table.setSpacingAfter(18.0f);
											

			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			//data.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			tableh1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			tableh1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			tableh1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			
			tableh1.addCell(new Phrase("ALL REGISTERED VENUES",FontFactory.getFont(FontFactory.HELVETICA,16)));

								


			tableh.addCell(new Phrase("S/N",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("VENUE NAME",FontFactory.getFont(FontFactory.HELVETICA,10)));
			tableh.addCell(new Phrase("CAPACITY",FontFactory.getFont(FontFactory.HELVETICA,10)));
			
			
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLVENUES ORDER BY VENUE_NAME");
			int count = 1;
			while (rs.next())
			{
				tableh.addCell(new Phrase(count+"",FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(rs.getString("VENUE_NAME"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				tableh.addCell(new Phrase(""+rs.getInt("CAPACITY"),FontFactory.getFont(FontFactory.HELVETICA,8)));
				count+=1;
			}
			
			
		
			
			//tableh.setTotalWidth(new float [] {1,3,3,3});
			//tableh.setWidths(new float [] {1,3,3,3});
			tableh.setWidthPercentage(100);

			//tableh.setLockedWidth(true); 

			
		

		


			

			
		
			document.add(tableh1);
			//document.add(table);
			document.add(tableh);
		

			document.close();

			JOptionPane.showMessageDialog(null, "The List Has Been Generated Successfully");

			JOptionPane.showMessageDialog(null, "Please Wait While the List is being Open");

			
			File openFile = new File(path+"All_Registered_Venues"+".pdf");
					
			boolean isDesktop;
			
			isDesktop = Desktop.isDesktopSupported();
				if (isDesktop)
				{	
					Desktop.getDesktop().open(openFile);
				}else 
					System.out.print("AWT is not supported on yours system");

			//Runtime.getRuntime().exec("Explorer" + (String) new File("Report.pdf").getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		


		
	}
	

	//get student taking a particular course
	public int nofs(String course)
	{
		int num = 0;
		for (int i=0;i<courses.size();i++)
		{
			String c =(String)((ArrayList)courses.get(i)).get(0);

			if (c.equals(course))
			{
				num = Integer.parseInt((String)((ArrayList)courses.get(i)).get(1));			
			}
		}

		return num;
	}

	//method to prepare venue and capacity array
	public void getPrepareVenue()
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLVENUES");

			


		}
		catch (Exception e){e.printStackTrace();}
	}
	// method to get available class and there courses
	public void getClasses(){
		
		for (int j=1;j<lvl.length;j++)
		{
			String level = lvl[j];
			int counter = 0;
			for (int i=0;i < courses.size();i++)
			{
				String l = (String)((ArrayList)courses.get(i)).get(2);

				if (l.equals(level))
				{
					counter+=1;
				}
				
			}

			System.out.println(level+" has "+counter);
		}
			
		
	}

//method to get total courses registered for a particular level
	public int getTlevel(int index){
		
		String level = lvl[index];
		int counter = 0;
			for (int i=0;i < courses.size();i++)
			{
				String l = (String)((ArrayList)courses.get(i)).get(2);

				if (l.equals(level))
				{
					counter+=1;
				}
				
			}
		
		return counter;
	}



//methods to prepare coures;
	public void getCourses(String dept) 
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLCOURSES where DEPARTMENT='"+dept+"'");
			
			if (!rs.next())
			{
				alert("No Courses Registered for "+dept+" Department","Info","error");
			}else {
				int counter = 0;
				do
				{
					courses.add(new ArrayList());
					((ArrayList)courses.get(counter)).add(rs.getString("COURSE_CODE"));
					((ArrayList)courses.get(counter)).add(rs.getString("TOTAL_STUDENT"));
					((ArrayList)courses.get(counter)).add(rs.getString("LEVEL"));

					counter++;
				}
				while (rs.next());
			}

		}
		catch (Exception e){e.printStackTrace();}
	}

	public void getVenues()
	{

		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("Select * from TBLVENUES ORDER BY CAPACITY");
			
			if (!rs.next())
			{
				alert("No Available Venue","Info","error");
			}else {
				int counter = 0;
				do
				{
					venues.add(new ArrayList());
					((ArrayList)venues.get(counter)).add(rs.getString("VENUE_NAME"));
					((ArrayList)venues.get(counter)).add(rs.getString("CAPACITY"));

					counter++;
				}
				while (rs.next());
			}

		}
		catch (Exception e){e.printStackTrace();}
	}

	//error message 
	public void alert(String message, String title, String type)
	{
		if (type.equals("error"))
		{
			JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
		}else if (type.equals("success"))
		{
			JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}


	//clear course fields after save 
	public void clearCourse(){
		
		jc1.setSelectedIndex(0);
		jc2.setSelectedIndex(0);
		t1.setText("");
		t2.setText("");
		t3.setText(""); 
	}
	//set up timetable folder
	public void checkDir() {
	
	File f = new File(path);

	if (!f.isDirectory())
	{
		System.out.println("Setting up timetable report folder");
		f.mkdir();
	}
}

	//clear venue fields after 
	public void clearVenue() {
		t4.setText("");
		t5.setText("");
		
	}

	//prepared the database 
	public void connectDB() {
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection(url);
			if (con != null)
			{
				System.out.println("connected to database successfull");


				Statement sta = con.createStatement();
				
				DatabaseMetaData dbmd = con.getMetaData();

				ResultSet rs = dbmd.getTables(null,"APP","TBLCOURSES", null);
				ResultSet rs1 = dbmd.getTables(null,"APP", "TBLVENUES", null);
				
				if (!rs.next())
				{
					System.out.println("Database Table is not Found, Table will now be Created");

					int cr = sta.executeUpdate("CREATE TABLE TBLCOURSES(ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), DEPARTMENT VARCHAR(100), LEVEL VARCHAR(7), COURSE_CODE VARCHAR(10), COURSE_TITLE VARCHAR(100), TOTAL_STUDENT VARCHAR(100))");
					if (cr!=-1)
					{
						System.out.println("Course Table Created Successfully");
					}else {
						System.out.println("Unable to Create Course Table");
					} 
					
				}else {
					System.out.println("Course Database table is okay");
				}

				if (!rs1.next())
				{
					System.out.println("Database Venue Table is not Found, Table will now be Created");

					int crr = sta.executeUpdate("CREATE TABLE TBLVENUES(ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), VENUE_NAME VARCHAR(100), CAPACITY INT)");
					if (crr!=-1)
					{
						System.out.println("Venue Table Created Successfully");
					}else {
						System.out.println("Unable to Create Venue Table");
					} 
					
				}else {
					System.out.println("Venue Database table is okay");
				}

			}
		}
		catch (Exception se)
		{
			se.printStackTrace();
			//System.err.println("Unable to connect to database for the following reasons:" +se.getMessage());
		}
	}



	public static void main(String[] args) 
	{
		new main();
	}
}
 