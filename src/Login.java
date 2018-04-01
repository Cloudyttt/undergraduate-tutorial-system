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
	public static Student stuUser = new Student("111110", "����", "��", "�����", "�����182", 
			"1580000000", "����", "���ϼ�", "000001", "123456");
	public static Instructor istrUser = new Instructor("000001", "���ϼ�", "male", "professer", "AI", "13588880001", "123456", 0, 2);
	private String account;
	private String password;
	private String tableName;
	private String sql;
	private String accountFieldName;
	private String passFieldName;
	
	//private static Statement sta;
	private static ResultSet result; // ��Ų�ѯ���ļ�¼��
	private static Login jdbcLogin;
	private static Student student;//��¼��ѧ����Ϣ
	private static Instructor instructor;//��¼�ĵ�ʦ��Ϣ
	public static void main(String[] args) 
	{
		jdbcLogin = new Login();
		//��һ�����������ݿ�
		MySQLManager.connect();
		Application.launch(args);
		//���һ���Ͽ����ݿ�����
		MySQLManager.closeConnection();
	}
	//���ݿ��ѯ��ʦ��Ϣ
	public ResultSet istrQuery(String sql) 
	{
		// ����statement����
		try 
		{
			//sta = MySQLManager.con.createStatement();

			// ִ�в�ѯ����
			result = MySQLManager.sta.executeQuery(sql);
			// ��ʾ���
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
			System.out.println("��ѯ����" + e.getMessage());
		}
		return result;
	}
	//���ݿ��ѯѧ����Ϣ
	public ResultSet stuQuery(String sql) 
	{
		// ����statement����
		try 
		{
			//sta = MySQLManager.con.createStatement();

			// ִ�в�ѯ����
			result = MySQLManager.sta.executeQuery(sql);
			// ��ʾ���
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
				System.out.println("��ѯ����" + e.getMessage());
		}
		return result;
	}	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		BorderPane mainLoginPane = new BorderPane();//������
		
		StackPane titleStackPane = new StackPane();
		titleStackPane.setPadding(new Insets(30, 20, 20, 20));
		
		Label labelTitle = new Label("��ʦ�ƹ���ϵͳ");
		labelTitle.setStyle("-fx-font-weight: bold; -fx-font-size:30;");
		titleStackPane.getChildren().add(labelTitle);
		
		//��¼����򣬴���û��˺ź�����
		HBox centerHBox = new HBox();//������BorderPane������ʵ�־���
		centerHBox.setAlignment(Pos.CENTER);
		
		VBox vBox = new VBox();//���ڴ�ֱ�����ϴ���û��˺���������͵�ѡ��
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(0, 20, 0 ,20));
		//�˺�
		HBox hBoxAccount = new HBox();
		hBoxAccount.setSpacing(20);
		hBoxAccount.setAlignment(Pos.CENTER_LEFT);
		Label labelAccount = new Label("�˺�: ");
		TextField tfAccount = new TextField();
		tfAccount.setPrefWidth(220);
		hBoxAccount.getChildren().addAll(labelAccount, tfAccount);
		//����
		HBox hBoxPassword = new HBox();
		hBoxPassword.setSpacing(20);
		hBoxPassword.setAlignment(Pos.CENTER_LEFT);
		Label labelPassword = new Label("����: ");
		PasswordField tfPassword = new PasswordField();
		tfPassword.setPrefWidth(220);
		hBoxPassword.getChildren().addAll(labelPassword, tfPassword);
		//ѡ���¼��ʽ
		HBox hBoxUserType = new HBox();
		hBoxUserType.setSpacing(10);
		ToggleGroup group = new ToggleGroup();   
		RadioButton rbAdmin = new RadioButton("����Ա");
		rbAdmin.setToggleGroup(group);
		rbAdmin.setSelected(true);
		rbAdmin.setOnAction(e -> {
			//����Ա��ѡ������¼�
			
		});
		RadioButton rbStu = new RadioButton("ѧ��");
		rbStu.setToggleGroup(group);
		rbStu.setOnAction(e -> {
			//ѧ����ѡ������¼�
			tableName = "studentdata";
			accountFieldName = "id";
			passFieldName = "stu_password";
		});
		RadioButton rbIstr = new RadioButton("��ʦ");
		rbIstr.setToggleGroup(group);
		rbIstr.setOnAction(e -> {
			//��ʦ��ѡ������¼�
			tableName = "instructordata";
			accountFieldName = "Istr_id";
			passFieldName = "Istr_password";
		});
		hBoxUserType.getChildren().addAll(rbAdmin, rbStu, rbIstr);
		//���ؼ���Ӧ���Ӳ��ּ��봹ֱ������
		vBox.getChildren().addAll(hBoxAccount, hBoxPassword, hBoxUserType);
		//��ֱ����Ƕ��һ��ˮƽ��������ʵ����BorderPane��ˮƽ����Ч��
		centerHBox.getChildren().add(vBox);
		
		//��ť
		HBox btnHBox = new HBox();//���ڴ�Ÿ�����ť�ؼ�
		btnHBox.setAlignment(Pos.CENTER);
		btnHBox.setPadding(new Insets(0, 20, 60, 20));
		btnHBox.setSpacing(20);
		Button btnLogin = new Button("��¼");//��¼
		btnLogin.setPrefWidth(100);
		btnLogin.setStyle("-fx-font-size:16;");
		btnLogin.setOnAction(e -> {
			//��¼��ť����¼�
			//��ȡ�û�������˻���Ϣ
			account = tfAccount.getText().toString().trim();
			password = tfPassword.getText().toString().trim();
			//�ж��û�����ĵ�¼��Ϣ�Ƿ�����
			if(account.length() == 0 || password.length() == 0)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�˺����벻��Ϊ�գ�", null);
				alert.showAndWait();
			}
			else
			{
/************************************************************����Ա��¼************************************************************/				
				if(rbAdmin.isSelected())
				{
					if(tfAccount.getText().toString().trim().equals("admin") && tfPassword.getText().toString().trim().equals("123456"))
					{
						System.out.println("��½�ɹ�");
						System.out.println("��¼�Ĺ���Ա�û����� " + tfAccount.getText().toString().trim());
						//�رյ�¼���ڣ����ع���Ա���ڽ���
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
						Alert alert1 = new Alert(Alert.AlertType.WARNING, "��¼ʧ�ܣ�", null);
						alert1.showAndWait();
					}
				}
				else
				{
					sql = "select * from " + tableName + " where " + accountFieldName + " = \"" + tfAccount.getText().toString() + 
							"\" and " + passFieldName + " = \"" + tfPassword.getText().toString() + "\"";
					System.out.println(sql);
					
/************************************************ѧ����¼***********************************************************/
					if(rbStu.isSelected())
					{
						result = stuQuery(sql);
						try 
						{
							result.last();
							System.out.println("���ݿ��ѯ��������� " + result.getRow());
							if(result.getRow()==1)
							{
								System.out.println("��½�ɹ�");
								stuUser = new Student(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getString(9), result.getString(10));
								System.out.println("��¼���û������� " + stuUser.getName());
								//�رյ�¼���ڣ�����ѧ������
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
								Alert alert2 = new Alert(Alert.AlertType.WARNING, "��¼ʧ��", null);
								alert2.showAndWait();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					
/************************************************��ʦ��¼***********************************************************/
					else if(rbIstr.isSelected())
					{
						result = istrQuery(sql);
						try {
							result.last();
							System.out.println("���ݿ��ѯ��������� " + result.getRow());
							if(result.getRow()==1)
							{
								System.out.println("��½�ɹ�");
								istrUser = new Instructor(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getInt(8), result.getInt(9));
								System.out.println("��¼���û������� " + istrUser.getName());
								//�رյ�¼���ڣ����ص�ʦ���ڽ���
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
								Alert alert = new Alert(Alert.AlertType.WARNING, "��¼ʧ��", null);
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
		Button btnReset = new Button("����");//����
		btnReset.setPrefWidth(100);
		btnReset.setStyle("-fx-font-size:16;");
		btnReset.setOnAction(e -> {
			//���ð�ť����¼�
			rbAdmin.setSelected(true);
			tfAccount.setText("");
			tfPassword.setText("");
		});
		btnHBox.getChildren().addAll(btnLogin, btnReset);
		
		//���Ӳ��ּ���������
		mainLoginPane.setTop(titleStackPane);
		mainLoginPane.setCenter(centerHBox);
		mainLoginPane.setBottom(btnHBox);
		
		//Create a scene and place it in the stage
		Scene scene = new Scene(mainLoginPane, 500, 400);
		primaryStage.setTitle("��¼");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
