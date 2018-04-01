package src;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.javafx.robot.impl.FXRobotHelper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application
{
	public static Student stuUser = new Student("111110", "王蛋", "男", "计算机", "计算机182", 
			"1580000000", "待定", "王老吉", "000001", "123456");
	public static Instructor istrUser = new Instructor("000001", "王老吉", "male", "professer", "AI", "13588880001", "123456", 0, 2);
	private String account;
	private String password;
	private String tableName;
	private String sql;
	private String accountFieldName;
	private String passFieldName;
	
	//private static Statement sta;
	private static ResultSet result; // 存放查询到的记录集
	private static Login jdbcLogin;
	private static Student student;//登录的学生信息
	private static Instructor instructor;//登录的导师信息
	public static void main(String[] args) 
	{
		jdbcLogin = new Login();
		//第一步先连接数据库
		MySQLManager.connect();
		Application.launch(args);
		//最后一步断开数据库连接
		MySQLManager.closeConnection();
	}
	//数据库查询导师信息
	public ResultSet istrQuery(String sql) 
	{
		// 创建statement对象
		try 
		{
			//sta = MySQLManager.con.createStatement();

			// 执行查询操作
			result = MySQLManager.sta.executeQuery(sql);
			// 显示结果
			while(result.next())
			{
				System.out.print(result.getString(1)+"\t");
				System.out.print(result.getString(2)+"\t");
				System.out.print(result.getString(3)+"\t");
				System.out.print(result.getString(4)+"\t");
				System.out.print(result.getString(5)+"\t");
				System.out.print(result.getString(6)+"\t");
				System.out.print(result.getString(7)+"\t");
				System.out.print(result.getString(8)+"\t");
				System.out.println(result.getString(9));
				instructor = new Instructor(result.getString(1), result.getString(2), 
						result.getString(3), result.getString(4), result.getString(5), 
						result.getString(6), result.getString(7),result.getInt(8), result.getInt(9)); 
			}

		} catch (SQLException e) {
			System.out.println("查询出错：" + e.getMessage());
		}
		return result;
	}
	//数据库查询学生信息
	public ResultSet stuQuery(String sql) 
	{
		// 创建statement对象
		try 
		{
			//sta = MySQLManager.con.createStatement();

			// 执行查询操作
			result = MySQLManager.sta.executeQuery(sql);
			// 显示结果
			while(result.next())
			{
				System.out.print(result.getString(1)+"\t");
				System.out.print(result.getString(2)+"\t");
				System.out.print(result.getString(3)+"\t");
				System.out.print(result.getString(4)+"\t");
				System.out.print(result.getString(5)+"\t");
				System.out.print(result.getString(6)+"\t");
				System.out.print(result.getString(7)+"\t");
				System.out.print(result.getString(8)+"\t");
				System.out.print(result.getString(9)+"\t");
				System.out.println(result.getString(10));
				student = new Student(result.getString(1), result.getString(2), 
							result.getString(3), result.getString(4), result.getString(5), 
							result.getString(6), result.getString(7), result.getString(8),result.getString(9), result.getString(10)); 
			}

		} catch (SQLException e) {
				System.out.println("查询出错：" + e.getMessage());
		}
		return result;
	}	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		BorderPane mainLoginPane = new BorderPane();//主布局
		
		StackPane titleStackPane = new StackPane();
		titleStackPane.setPadding(new Insets(30, 20, 20, 20));
		
		Label labelTitle = new Label("导师制管理系统");
		labelTitle.setStyle("-fx-font-weight: bold; -fx-font-size:30;");
		titleStackPane.getChildren().add(labelTitle);
		
		//登录输入框，存放用户账号和密码
		HBox centerHBox = new HBox();//用于在BorderPane布局中实现居中
		centerHBox.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox();//用于垂直方向上存放用户账号密码输入和单选框
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(0, 20, 0 ,20));
		//账号
		HBox hBoxAccount = new HBox();
		hBoxAccount.setSpacing(20);
		hBoxAccount.setAlignment(Pos.CENTER_LEFT);
		Label labelAccount = new Label("账号: ");
		TextField tfAccount = new TextField();
		tfAccount.setPrefWidth(220);
		hBoxAccount.getChildren().addAll(labelAccount, tfAccount);
		//密码
		HBox hBoxPassword = new HBox();
		hBoxPassword.setSpacing(20);
		hBoxPassword.setAlignment(Pos.CENTER_LEFT);
		Label labelPassword = new Label("密码: ");
		PasswordField tfPassword = new PasswordField();
		tfPassword.setPrefWidth(220);
		hBoxPassword.getChildren().addAll(labelPassword, tfPassword);
		//选择登录方式
		HBox hBoxUserType = new HBox();
		hBoxUserType.setSpacing(10);
		ToggleGroup group = new ToggleGroup();   
		RadioButton rbAdmin = new RadioButton("管理员");
		rbAdmin.setToggleGroup(group);
		rbAdmin.setSelected(true);
		rbAdmin.setOnAction(e -> {
			//管理员单选框监听事件
			
		});
		RadioButton rbStu = new RadioButton("学生");
		rbStu.setToggleGroup(group);
		rbStu.setOnAction(e -> {
			//学生单选框监听事件
			tableName = "studentdata";
			accountFieldName = "id";
			passFieldName = "stu_password";
		});
		RadioButton rbIstr = new RadioButton("导师");
		rbIstr.setToggleGroup(group);
		rbIstr.setOnAction(e -> {
			//导师单选框监听事件
			tableName = "instructordata";
			accountFieldName = "Istr_id";
			passFieldName = "Istr_password";
		});
		hBoxUserType.getChildren().addAll(rbAdmin, rbStu, rbIstr);
		//各控件对应的子布局加入垂直布局中
		vBox.getChildren().addAll(hBoxAccount, hBoxPassword, hBoxUserType);
		//垂直布局嵌入一个水平布局中以实现在BorderPane中水平居中效果
		centerHBox.getChildren().add(vBox);
		
		//按钮
		HBox btnHBox = new HBox();//用于存放各个按钮控件
		btnHBox.setAlignment(Pos.CENTER);
		btnHBox.setPadding(new Insets(0, 20, 60, 20));
		btnHBox.setSpacing(20);
		Button btnLogin = new Button("登录");//登录
		btnLogin.setPrefWidth(100);
		btnLogin.setStyle("-fx-font-size:16;");
		btnLogin.setOnAction(e -> {
			//登录按钮点击事件
			//获取用户输入的账户信息
			account = tfAccount.getText().toString().trim();
			password = tfPassword.getText().toString().trim();
			//判断用户输入的登录信息是否完整
			if(account.length() == 0 || password.length() == 0)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "账号密码不能为空！", null);
				alert.showAndWait();
			}
			else
			{
/************************************************************管理员登录************************************************************/				
				if(rbAdmin.isSelected())
				{
					if(tfAccount.getText().toString().trim().equals("admin") && tfPassword.getText().toString().trim().equals("123456"))
					{
						System.out.println("登陆成功");
						System.out.println("登录的管理员用户名： " + tfAccount.getText().toString().trim());
						//关闭登录窗口，加载管理员窗口界面
						primaryStage.close();
						AdministratorWindow AdminWindow = new AdministratorWindow();
						try 
						{
							AdminWindow.start(primaryStage);
						} catch (Exception e1) 
						{
							e1.printStackTrace();
						}
					}
					else
					{
						Alert alert1 = new Alert(Alert.AlertType.WARNING, "登录失败！", null);
						alert1.showAndWait();
					}
				}
				else
				{
					sql = "select * from " + tableName + " where " + accountFieldName + " = \"" + tfAccount.getText().toString() + 
							"\" and " + passFieldName + " = \"" + tfPassword.getText().toString() + "\"";
					System.out.println(sql);
					
/************************************************学生登录***********************************************************/
					if(rbStu.isSelected())
					{
						result = stuQuery(sql);
						try 
						{
							result.last();
							System.out.println("数据库查询结果条数： " + result.getRow());
							if(result.getRow()==1)
							{
								System.out.println("登陆成功");
								stuUser = new Student(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getString(9), result.getString(10));
								System.out.println("登录的用户姓名： " + stuUser.getName());
								//关闭登录窗口，加载学生窗口
								primaryStage.close();
								StudentWindow studentWindow = new StudentWindow();
								try 
								{
									studentWindow.start(primaryStage);
								} catch (Exception e1) 
								{
									e1.printStackTrace();
								}
							}
							else
							{
								Alert alert2 = new Alert(Alert.AlertType.WARNING, "登录失败", null);
								alert2.showAndWait();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					
/************************************************导师登录***********************************************************/
					else if(rbIstr.isSelected())
					{
						result = istrQuery(sql);
						try {
							result.last();
							System.out.println("数据库查询结果条数： " + result.getRow());
							if(result.getRow()==1)
							{
								System.out.println("登陆成功");
								istrUser = new Instructor(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getInt(8), result.getInt(9));
								System.out.println("登录的用户姓名： " + istrUser.getName());
								//关闭登录窗口，加载导师窗口界面
								primaryStage.close();
								InstructorWindow instructorWindow = new InstructorWindow();
								try 
								{
									instructorWindow.start(primaryStage);
								} catch (Exception e1) 
								{
									e1.printStackTrace();
								}
							}
							else
							{
								Alert alert = new Alert(Alert.AlertType.WARNING, "登录失败", null);
								alert.showAndWait();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		Button btnReset = new Button("重置");//重置
		btnReset.setPrefWidth(100);
		btnReset.setStyle("-fx-font-size:16;");
		btnReset.setOnAction(e -> {
			//重置按钮点击事件
			rbAdmin.setSelected(true);
			tfAccount.setText("");
			tfPassword.setText("");
		});
		btnHBox.getChildren().addAll(btnLogin, btnReset);
		
		//各子布局加入主布局
		mainLoginPane.setTop(titleStackPane);
		mainLoginPane.setCenter(centerHBox);
		mainLoginPane.setBottom(btnHBox);
		
		//Create a scene and place it in the stage
		Scene scene = new Scene(mainLoginPane, 500, 400);
		primaryStage.setTitle("登录");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
