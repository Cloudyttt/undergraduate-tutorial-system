package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class MySQLManager 
{
	public static Connection con;
	public static java.sql.Statement sta;
	public final static String DRIVERNAME = "com.mysql.jdbc.Driver";
	public final static String URL = "jdbc:mysql://127.0.0.1:3306/student?useUnicode=true&characterEncoding=utf8";
	public final static String USER = "root";
	public final static String PASSWORD = "123456";
	//���ݿ�����
	public final static void connect() 
	{
		try 
		{
			// ����ע����Ӧ����
			Class.forName(DRIVERNAME);
			// ��������
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			sta = con.createStatement();
			// ��ʾ���
			System.out.println("���ݿ����ӳɹ�");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("������������" + e.getMessage());
		} 
		catch (SQLException e) 
		{
			System.out.println("�������ݿ����" + e.getMessage());
		}
	}
	//��ѯ
	public static ResultSet sqlQuery(String sql) 
	{
		ResultSet tempResult = null;
		try 
		{
			tempResult = sta.executeQuery(sql);
		} 
		catch (SQLException e) 
		{
			System.out.println("��ѯ����" + e.getMessage());
		}
		return tempResult;
	}
	//���ݿ�����
	public static int sqlInsert(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			numOfRecords = sta.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			System.out.println("��ӳ���" + e.getMessage());
		}
		return numOfRecords;
	}
	//���ݿ����
	public static int sqlUpdate(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			// ִ�в������
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// ��ʾ���
			System.out.println("���µļ�¼����Ϊ�� " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("���³���" + ex.getMessage());
		}
		return numOfRecords;
	}
	//���ݿ�ɾ��
	public static int sqlDelete(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			// ִ�в������
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// ��ʾ���
			System.out.println("ɾ���ļ�¼����Ϊ�� " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("ɾ������" + ex.getMessage());
		}
		return numOfRecords;
	}
	//�Ͽ����ݿ�����
	public final static void closeConnection() 
	{
		if (con != null) 
		{
			try 
			{
				if (sta != null) 
				{
					sta.close();
				}
				con.close();
				System.out.println("���ݿ����ӹر�");
			} 
			catch (SQLException e) 
			{
				System.out.println("�ر����ݿ����" + e.getMessage());
			}
		}
	}
}
