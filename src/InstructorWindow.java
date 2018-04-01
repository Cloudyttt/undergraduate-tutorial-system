package src;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.StudentWindow.MouseClickedListener;

public class InstructorWindow extends Application
{
/***************************************声明和定义*****************************************/
	private Instructor currIstr = Login.istrUser;//当前登录的导师用户
	private Instructor instructor;
	private Student student;
	private static Student selectedStu;
	
	//创建TableView控件用于显示导师查询结果
	Button btnQuery = new Button("查询");
	private String[] selectionState = new String[2];
	private ComboBox<String> cboSelectState = new ComboBox<>();
	///创建TableView控件用于显示学生信息
	private TableView<Student> certainStuTable = new TableView<Student>();//学生TableView
	private static ObservableList<Student> certainStuData = null;
	TableColumn<Student, String> stuIdCol = new TableColumn<Student, String>("学号");//定义列,将数据关联到表格中的列
	TableColumn<Student, String> stuNameCol = new TableColumn<Student, String>("姓名");
	TableColumn<Student, String> stuSexCol = new TableColumn<Student, String>("性别");
	TableColumn<Student, String> stuMajorCol = new TableColumn<Student, String>("专业");
	TableColumn<Student, String> stuClassCol = new TableColumn<Student, String>("班级");
	TableColumn<Student, String> stuTelephoneCol = new TableColumn<Student, String>("联系方式");
	TableColumn<Student, String> stuStateCol = new TableColumn<Student, String>("状态");
	
	//显示被选中的学生信息
	Label certainStuID = new Label("未选中"); 
	Label certainStuName = new Label("未选中"); 
	Label certainStuSex = new Label("未选中"); 
	Label certainStuMajor = new Label("未选中"); 
	Label certainStuClass = new Label("未选中");
	Label certainStuPhone = new Label("未选中"); 
	Label certainStuState = new Label("未选中"); 
	
	//数据库相关变量声明
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'选定\'");
	private static StringBuilder stuUpdateStr = new StringBuilder("update studentdata");
	private static StringBuilder istrUpdateStr = new StringBuilder("update studentdata");
	private ResultSet result; // 存放查询到的导师信息记录集
	private ResultSet stuResult; // 存放查询到的学生信息记录集
	private static int numOfRecords; // 存放与数据库操作相关的记录数
	
	//被当前导师用户选中的学生信息
	private boolean isChosenOne = false;//判断当前导师用户是否已选中了一个学生
	private TabPane tabPane;
	
	public static void main(String[] args) 
	{
		//第一步先连接数据库
		MySQLManager.connect();
		Application.launch(args);
		//最后一步断开数据库连接
		MySQLManager.closeConnection();
	}
	Stage istrPrimaryStage;
/*************************************start重载*******************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type
		selectionState[0] = "选定";
		selectionState[1] = "待定";
		// 创建标签页面板
		tabPane = new TabPane();
		// 创建导师主页标签
		Tab studentTab = new Tab("导师信息");
        studentTab.setContent(InstrSample());
        // 创建导师信息修改标签
        Tab modifyTab = new Tab("信息修改");
        modifyTab.setContent(modifyPasswordSample());
        // 把创建的标签对象放入标签页面板
        tabPane.getTabs().addAll(studentTab, modifyTab);
        
        
        
        // 把标签页面板放入场景
		Scene scene = new Scene(tabPane, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("导师窗口");
		primaryStage.show();
		istrPrimaryStage = primaryStage;
	}

	
/*********************************************************相关函数**********************************************************/	
/*********************************************** 创建导师主页标签中的内容，并返回该节点********************************************/
	@SuppressWarnings("unchecked")
	private Node InstrSample() 
	{
		BorderPane borderPane = new BorderPane();
		System.out.println("当前登录用户姓名： " + currIstr.getName());
		
		Label basicInfoLabel = new Label("个人信息");
		basicInfoLabel.setStyle("-fx-font-size:18;");
		Label istrInfoLabel = new Label("学生选择情况");
		istrInfoLabel.setStyle("-fx-font-size:18;");
		Button btnLogout = new Button("退出");
		btnLogout.setPrefWidth(60);
		HBox.setMargin(btnLogout, new Insets(0,0,0,485));
		btnLogout.setOnAction(e -> //退出到登录界面按钮点击事件
		{
			istrPrimaryStage.close();
			Login loginWindow = new Login();
			try 
			{
				loginWindow.start(istrPrimaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		//顶部标题
		HBox topHBox = new HBox();
		topHBox.setPadding(new Insets(20, 20, 0, 20));
//		topHBox.setAlignment(Pos.TOP_CENTER);
		HBox hBox1 = new HBox();
		hBox1.setPrefWidth(300);
		hBox1.getChildren().add(basicInfoLabel);
		HBox hBox2 = new HBox();
//		hBox2.setPrefWidth(420);
		hBox2.getChildren().addAll(istrInfoLabel, btnLogout);
		topHBox.getChildren().addAll(hBox1, hBox2);
		
		//左侧导师个人信息
		HBox centerHBox = new HBox();
		centerHBox.setAlignment(Pos.CENTER);
		
		GridPane leftGridPane = new GridPane();
		leftGridPane.setPadding(new Insets(20, 0, 20, 20));
		leftGridPane.setHgap(20);
		leftGridPane.setVgap(20);
		leftGridPane.setPrefWidth(300);
		Label accountLabel = new Label("工号: ");
		Label nameLabel = new Label("姓名: ");
		Label sexLabel = new Label("性别: ");
		Label rankLabel = new Label("职称: ");
		Label directionLabel = new Label("研究方向: ");
		Label telephoneLabel = new Label("联系方式: ");
		Label stuNumLabel = new Label("选定学生数: ");
		Label upperLimitLabel = new Label("学生数上限: ");
		leftGridPane.add(accountLabel, 0, 0);
		leftGridPane.add(nameLabel, 0, 1);
		leftGridPane.add(sexLabel, 0, 2);
		leftGridPane.add(rankLabel, 0, 3);
		leftGridPane.add(directionLabel, 0, 4);
		leftGridPane.add(telephoneLabel, 0, 5);
		leftGridPane.add(stuNumLabel, 0, 6);
		leftGridPane.add(upperLimitLabel, 0, 7);
		
		Label istrAccountLabel = new Label(currIstr.getId());
		Label istrNameLabel = new Label(currIstr.getName());
		Label istrSexLabel = new Label(currIstr.getSex());
		Label istrMajorLabel = new Label(currIstr.getRank());
		Label istrClassLabel = new Label(currIstr.getDirection());
		Label istrTelephoneLabel = new Label(currIstr.getTelephone());
		Label istrStuNumLabel = new Label(currIstr.getStuNum().toString());
		Label istrUpperLimitLabel = new Label(currIstr.getUpperLimit().toString());
		leftGridPane.add(istrAccountLabel, 1, 0);
		leftGridPane.add(istrNameLabel, 1, 1);
		leftGridPane.add(istrSexLabel, 1, 2);
		leftGridPane.add(istrMajorLabel, 1, 3);
		leftGridPane.add(istrClassLabel, 1, 4);
		leftGridPane.add(istrTelephoneLabel, 1, 5);
		leftGridPane.add(istrStuNumLabel, 1, 6);
		leftGridPane.add(istrUpperLimitLabel, 1, 7);
/****************************************************右侧导师选择学生板块********************************************************/
		VBox rightBox = new VBox();//右侧主布局
		rightBox.setPadding(new Insets(20, 20, 40, 20));
		rightBox.setSpacing(20);
//		rightBox.setPrefWidth(500);
		
		//显示所选导师当前所带学生信息
		certainStuTable.setEditable(true);
		stuIdCol.setMinWidth(80);	
		stuNameCol.setMinWidth(80);	
		stuSexCol.setMinWidth(60);	
		stuMajorCol.setMinWidth(110);		
		stuClassCol.setMinWidth(100);	
		stuTelephoneCol.setMinWidth(120);
		stuStateCol.setMinWidth(80);
		certainStuTable.getColumns().addAll(stuIdCol, stuNameCol, stuSexCol, stuMajorCol, 
				stuClassCol, stuTelephoneCol, stuStateCol); 	
		certainStuTable.setOnMouseClicked(new MouseClickedListener());// 为导师表格注册鼠标事件
		
		
		//可根据学生当前状态分类呈现学生信息的下拉框
		Label labelStuState = new Label("学生状态：");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(selectionState);
		cboSelectState.getItems().addAll(itemsMode);
		cboSelectState.setPrefWidth(125);
		cboSelectState.setValue(selectionState[0]);
		cboQuery();
		cboSelectState.setOnAction(e -> {
			if(itemsMode.indexOf(cboSelectState.getValue()) == 0)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'选定\'");
				cboQuery();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 1)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'待定\'");
				cboQuery();
			}
		});
//		
//		Button btnQuery = new Button("查询");
//		btnQuery.setOnAction(e -> {//点击查询选中当前登录导师用户作为导师的学生信息
//
//		});
		//下拉框对应的HBox布局
		HBox hBoxCbo = new HBox();
		hBoxCbo.setSpacing(20);
		hBoxCbo.getChildren().addAll(labelStuState, cboSelectState);	
		//被选中的学生信息，加入一个GridPane布局中
		GridPane certainPane = new GridPane();
		certainPane.setHgap(5);
		certainPane.setVgap(10);
		Label lbCertainStuID = new Label("学生学号： "); 
		Label lbCertainStuName = new Label("学生姓名： "); 
		Label lbCertainStuSex = new Label("学生性别： "); 
		Label lbCertainStuMajor = new Label("学生专业： "); 
		Label lbCertainStuClass = new Label("学生班级： ");
		Label lbCertainStuPhone = new Label("联系方式： "); 
		Label lbCertainStuState = new Label("选择状态： "); 
		lbCertainStuMajor.setPadding(new Insets(0, 0, 0, 30));
		lbCertainStuClass.setPadding(new Insets(0, 0, 0, 30));
		lbCertainStuPhone.setPadding(new Insets(0, 0, 0, 30));
		lbCertainStuState.setPadding(new Insets(0, 0, 0, 30));
//		lbCertainStuID.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuName.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuSex.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuMajor.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuClass.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuPhone.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
//		lbCertainStuState.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
		GridPane.setMargin(lbCertainStuName, new Insets(8, 0, 0, 0));
		GridPane.setMargin(certainStuName, new Insets(8, 0, 0, 0));
		GridPane.setMargin(lbCertainStuClass, new Insets(8, 0, 0, 0));
		GridPane.setMargin(certainStuClass, new Insets(8, 0, 0, 0));
		
		certainStuID.setPrefWidth(80);
		certainStuName.setPrefWidth(80);
		certainStuSex.setPrefWidth(80);
		certainStuMajor.setPrefWidth(100);
		certainStuClass.setPrefWidth(100);
		certainStuPhone.setPrefWidth(100);
		
		certainPane.add(lbCertainStuID, 0, 0);
		certainPane.add(lbCertainStuName, 0, 1);
		certainPane.add(lbCertainStuSex, 0, 2);
		certainPane.add(lbCertainStuMajor, 2, 0);
		certainPane.add(lbCertainStuClass, 2, 1);
		certainPane.add(lbCertainStuPhone, 2, 2);
		certainPane.add(lbCertainStuState, 4, 0);

		certainPane.add(certainStuID, 1, 0);
		certainPane.add(certainStuName, 1, 1);
		certainPane.add(certainStuSex, 1, 2);
		certainPane.add(certainStuMajor, 3, 0);
		certainPane.add(certainStuClass, 3, 1);
		certainPane.add(certainStuPhone, 3, 2);
		certainPane.add(certainStuState, 5, 0);
		
		Button btnCertain = new Button("确认选择");
		GridPane.setMargin(btnCertain, new Insets(0, 5, 0, 30));
		Button btnCancel = new Button("取消选择");
		btnCertain.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
		btnCancel.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
		certainPane.add(btnCertain, 4, 2);
		certainPane.add(btnCancel, 5, 2);
		//确认学生选择按钮点击事件
		btnCertain.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的学生！", null);
				alert.showAndWait();
			}
			else if(selectedStu.getState().equals("选定"))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前选择学生已确认，请勿重复操作！", null);
				alert.showAndWait();
			}
			else if(currIstr.getUpperLimit() - currIstr.getStuNum() == 0)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "您的可带学生数已达上限！", null);
				alert.showAndWait();
			}
			else
			{
				int twoRecord = 0;//用于判断是否更新两个数据库均成功
				stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \'选定\' where id = \'" + selectedStu.getId() + "\'");
				System.out.println("当前studentdata数据库更新的语句： " + stuUpdateStr.toString());
				istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + (currIstr.getStuNum() + 1) + " where Istr_id = \'" + currIstr.getId() + "\'");
				System.out.println("当前instructordata数据库更新的语句： " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				twoRecord = twoRecord + numOfRecords;
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				twoRecord = twoRecord + numOfRecords;
				if(twoRecord == 2)
				{
					currIstr.setStuNum(currIstr.getStuNum() + 1);
					istrStuNumLabel.setText(currIstr.getStuNum().toString());
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "确认选择成功！", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "确认选择失败！", null);
					alert.showAndWait();
				}
			}
		});
		//取消学生选择点击事件
		btnCancel.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的学生！", null);
				alert.showAndWait();
			}
			else if(selectedStu.getState().equals("选定"))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前选中学生已确认，不能取消选择！", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \'未选\', instructor_id = \'\', instructor = \'\' where id = " + selectedStu.getId());
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "取消选择成功！", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "取消选择失败！", null);
					alert.showAndWait();
				}
			}
		});
/*************************************************************************************************************************/
		
		rightBox.getChildren().addAll(hBoxCbo, certainStuTable, certainPane);
		centerHBox.getChildren().addAll(leftGridPane, rightBox);
		borderPane.setTop(topHBox);
		borderPane.setCenter(centerHBox);
		return borderPane;
	}
	// 创建信息修改标签中的内容，并返回该节点
	private Node modifyPasswordSample() 
	{
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(20, 20, 20, 20));
		vBox.setSpacing(20);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		
		Label oldPassLabel = new Label("旧密码: ");
		Label newPassLabel = new Label("新密码: ");
		Label confirmPassLabel = new Label("确认新密码: ");
		TextField oldPassword  = new TextField();
		PasswordField newPassword = new PasswordField();
		PasswordField confirmPassword = new PasswordField();
		Button btnModify = new Button("修改密码");
		btnModify.setStyle("-fx-font-size:18;");
		btnModify.setOnAction(e -> {
			if(!currIstr.getPassword().trim().equals(oldPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "旧密码输入错误!", null);
				alert.showAndWait();
			}
			else if(oldPassword.getText().toString().trim().equals(newPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "新密码不能与旧密码相同!", null);
				alert.showAndWait();
			}
			else if(!newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "两次新密码输入不一致！", null);
				alert.showAndWait();
			}
			else
			{
				istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set Istr_password = \'" + newPassword.getText().toString().trim() + "\' where Istr_id = " + currIstr.getId());
				System.out.println("当前修改密码数据库更新语句： " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
				if(numOfRecords > 0)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "密码修改成功！", null);
					alert.showAndWait();
					//修改密码成功后需要重新登录，自动跳转到登录界面
					istrPrimaryStage.close();
					Login loginWindow = new Login();
					try 
					{
						loginWindow.start(istrPrimaryStage);
					} 
					catch (Exception e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		gridPane.add(oldPassLabel, 0, 0);
		gridPane.add(oldPassword, 1, 0);
		gridPane.add(newPassLabel, 0, 1);
		gridPane.add(newPassword, 1, 1);
		gridPane.add(confirmPassLabel, 0, 2);
		gridPane.add(confirmPassword, 1, 2);
		gridPane.add(btnModify, 1, 3);
		GridPane.setHalignment(btnModify, HPos.LEFT);
		
		vBox.getChildren().addAll(gridPane);
		
		return vBox;
	}	
/***********************************************TableView点击事件**********************************************************/
	//导师Tablview点击事件
	public class MouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// 得到导师选择的记录
			int selectedRow = certainStuTable.getSelectionModel().getSelectedIndex();
			// 如果确实选取了某条记录
			if(selectedRow!=-1)
			{
				isChosenOne = true;//当前学生用户已经选中一个导师
				// 获取选择的记录
				selectedStu = certainStuData.get(selectedRow);
				certainStuID.setText(selectedStu.getId());
				certainStuName.setText(selectedStu.getName());
				certainStuSex.setText(selectedStu.getSex());
				certainStuMajor.setText(selectedStu.getMajor());
				certainStuClass.setText(selectedStu.getStuClass());
				certainStuPhone.setText(selectedStu.getTelephone());
				certainStuState.setText(selectedStu.getState());
				System.out.println("被选中学生名字： " + selectedStu.getName());
			}
		}
	}	
	
	//下拉框选择查询
	public void cboQuery()
	{
		System.out.println(stuQueryStr.toString());
		certainStuData = FXCollections.observableArrayList();
		stuResult = MySQLManager.sqlQuery(stuQueryStr.toString());
		try 
		{
			while(stuResult.next())
			{
				System.out.print(stuResult.getString(1)+"\t");
				System.out.print(stuResult.getString(2)+"\t");
				System.out.print(stuResult.getString(3)+"\t");
				System.out.print(stuResult.getString(4)+"\t");
				System.out.print(stuResult.getString(5)+"\t");
				System.out.print(stuResult.getString(6)+"\t");
				System.out.print(stuResult.getString(7)+"\t");
				System.out.print(stuResult.getString(8)+"\t");
				System.out.print(stuResult.getString(9)+"\t");
				System.out.println(stuResult.getString(10));
				student = new Student(stuResult.getString(1), stuResult.getString(2), 
						stuResult.getString(3), stuResult.getString(4),stuResult.getString(5), 
						stuResult.getString(6), stuResult.getString(7));
				certainStuData.add(student);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		certainStuTable.setItems(certainStuData);	
		stuIdCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
		stuNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
		stuSexCol.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
		stuMajorCol.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
		stuClassCol.setCellValueFactory(new PropertyValueFactory<Student, String>("stuClass"));
		stuTelephoneCol.setCellValueFactory(new PropertyValueFactory<Student, String>("telephone"));
		stuStateCol.setCellValueFactory(new PropertyValueFactory<Student, String>("state"));
	}

	
}
