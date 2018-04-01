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
	//数据库连接
	public final static void connect() 
	{
		try 
		{
			// 加载注册相应驱动
			Class.forName(DRIVERNAME);
			// 创建连接
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			sta = con.createStatement();
			// 显示结果
			System.out.println("数据库连接成功");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("加载驱动出错：" + e.getMessage());
		} 
		catch (SQLException e) 
		{
			System.out.println("连接数据库出错：" + e.getMessage());
		}
	}
	//查询
	public static ResultSet sqlQuery(String sql) 
	{
		ResultSet tempResult = null;
		try 
		{
			tempResult = sta.executeQuery(sql);
		} 
		catch (SQLException e) 
		{
			System.out.println("查询出错：" + e.getMessage());
		}
		return tempResult;
	}
	//数据库添增
	public static int sqlInsert(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			numOfRecords = sta.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			System.out.println("添加出错：" + e.getMessage());
		}
		return numOfRecords;
	}
	//数据库更新
	public static int sqlUpdate(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			// 执行插入操作
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// 显示结果
			System.out.println("更新的记录条数为： " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("更新出错：" + ex.getMessage());
		}
		return numOfRecords;
	}
	//数据库删除
	public static int sqlDelete(String sql) 
	{
		int numOfRecords = 0;
		try 
		{
			// 执行插入操作
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// 显示结果
			System.out.println("删除的记录条数为： " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("删除出错：" + ex.getMessage());
		}
		return numOfRecords;
	}
	//断开数据库连接
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
				System.out.println("数据库连接关闭");
			} 
			catch (SQLException e) 
			{
				System.out.println("关闭数据库出错：" + e.getMessage());
			}
		}
	}
}
