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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdministratorWindow extends Application
{
/******************************************************其他声明和定义************************************************************/
	private TabPane tabPane;
	private Instructor instructor;
	private static Instructor selectedIstr;//存放被当前学生用户选为自己导师的导师信息
	private Student student;
	private static Student selectedStu;//存放当前选中的学生
	private static Student selectedsubStu;//存放当前导师界面学生简要信息表中被选中的学生
	private static boolean isChosenOne = false;//导师界面
	private static boolean isChosenOneStu = false;//学生界面
	private static boolean isChosenOnesubStu = false;//导师界面学生简要信息表
	private static StringBuilder editState = new StringBuilder("未选");//添加学生记录时默认初始状态为“未选”
	private static String beginState = "未选";//存放选中学生时学生初始状态
	private static String changeState = "选定";//导师界面修改学生状态
/*************************************************导师界面TableView************************************************************/	
	//创建TableView控件用于显示导师查询结果
	private String[] selectionState = new String[3];
	private ComboBox<String> cboSelectState = new ComboBox<>();
	private TableView<Instructor> istrTable = new TableView<Instructor>();
	private static ObservableList<Instructor> istrData = null;
	TableColumn<Instructor, String> idCol = new TableColumn<Instructor, String>("工号");//定义列,将数据关联到表格中的列
	TableColumn<Instructor, String> nameCol = new TableColumn<Instructor, String>("姓名");	
	TableColumn<Instructor, String> sexCol = new TableColumn<Instructor, String>("性别");
	TableColumn<Instructor, String> rankCol = new TableColumn<Instructor, String>("职称");		
	TableColumn<Instructor, String> directionCol = new TableColumn<Instructor, String>("研究方向");		
	TableColumn<Instructor, String> telephoneCol = new TableColumn<Instructor, String>("联系电话");
	TableColumn<Instructor, String> passwordCol = new TableColumn<Instructor, String>("登录密码");
	TableColumn<Instructor, String> stuNumCol = new TableColumn<Instructor, String>("选定学生数");
	TableColumn<Instructor, String> upperLimitCol = new TableColumn<Instructor, String>("学生数上限");
	///创建TableView控件用于显示学生查询信息
	private TableView<Student> stuTable = new TableView<Student>();//学生TableView
	private static ObservableList<Student> stuData = null;
	TableColumn<Student, String> stuIdCol = new TableColumn<Student, String>("学号");//定义列,将数据关联到表格中的列
	TableColumn<Student, String> stuNameCol = new TableColumn<Student, String>("姓名");
	TableColumn<Student, String> stuSexCol = new TableColumn<Student, String>("性别");
	TableColumn<Student, String> stuMajorCol = new TableColumn<Student, String>("专业");
	TableColumn<Student, String> stuClassCol = new TableColumn<Student, String>("班级");
	TableColumn<Student, String> stuStateCol = new TableColumn<Student, String>("状态");

	TextField tfIstrID = new TextField("");
	TextField tfIstrName = new TextField("");
	TextField tfIstrSex = new TextField("");
	TextField tfIstrRank = new TextField("");
	TextField tfIstrDirection = new TextField("");
	TextField tfIstrPhone = new TextField("");
	TextField tfIstrPassword = new TextField("");
	TextField tfIstrUpperLimit = new TextField("");
	
	Label stuIDInfo = new Label("未选中");
	Label stuNameInfo = new Label("未选中");
	Label stuStateInfo = new Label("未选中");
/**************************************************************************************************************************/
/************************************************学生信息界面TabelView*********************************************************/
	//下拉框，用于根据学生当前选导师状态查询
	private String[] stuState = new String[4];
	private ComboBox<String> cboStuState = new ComboBox<>();
	//创建TableView控件用于显示学生查询信息
	private TableView<Student> mainStuTable = new TableView<Student>();//学生TableView
	private static ObservableList<Student> mainStuData = null;
	TableColumn<Student, String> mainStuIDCol = new TableColumn<Student, String>("学号");//定义列,将数据关联到表格中的列
	TableColumn<Student, String> mainStuNameCol = new TableColumn<Student, String>("姓名");
	TableColumn<Student, String> mainStuSexCol = new TableColumn<Student, String>("性别");
	TableColumn<Student, String> mainStuMajorCol = new TableColumn<Student, String>("专业");
	TableColumn<Student, String> mainStuClassCol = new TableColumn<Student, String>("班级");
	TableColumn<Student, String> mainStuStateCol = new TableColumn<Student, String>("状态");
	TableColumn<Student, String> mainStuPhoneCol = new TableColumn<Student, String>("手机");
	TableColumn<Student, String> mainStuPasswordCol = new TableColumn<Student, String>("密码");
	TableColumn<Student, String> mainStuIstrIDCol = new TableColumn<Student, String>("导师工号");
	TableColumn<Student, String> mainStuIstrNameCol = new TableColumn<Student, String>("导师姓名");
	
	//编辑学生信息部分
	TextField tfStuID = new TextField("");
	TextField tfStuName = new TextField("");
	TextField tfStuSex = new TextField("");
	TextField tfStuMajor = new TextField("");
	TextField tfStuClass = new TextField("");
	TextField tfStuPhone = new TextField("");
	TextField tfStuPassword = new TextField("");
	private String[] stuEditState = new String[3];
	private ComboBox<String> cboEditState = new ComboBox<>();
	TextField tfStuIstrID = new TextField("");
	TextField tfStuIstrName = new TextField("");
/**************************************************************************************************************************/
/****************************************************数据库相关声明*************************************************************/
	private StringBuilder istrQueryStr = new StringBuilder("select * from instructordata");
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentdata");
	private StringBuilder mainStuQueryStr = new StringBuilder("select * from studentdata");
	private static StringBuilder stuUpdateStr = new StringBuilder("update studentdata");
	private static StringBuilder istrUpdateStr = new StringBuilder("update studentdata");
	private ResultSet result; // 存放查询到的导师信息记录集
	private ResultSet stuResult; // 存放查询到的学生信息记录集
	private int numOfRecords; // 存放与数据库操作相关的记录数
	
	private String idStr = "";//用于存放修改学生数据时的学生信息内容
	private String nameStr = "";
	private String sexStr = "";
	private String majorStr = "";
	private String classStr = "";
	private String phoneStr = "";
	private String passwordStr = "123456";
	private String stateStr = "未选";
	private String istrIDStr = "";
	private String istrNameStr = "";
	
	private String istrEditIDStr = "";//用于存放修改导师数据时的学生信息内容
	private String istrEditNameStr = "";
	private String istrSexStr = "";
	private String istrRankStr = "";
	private String istrDirectionStr = "";
	private String istrPhoneStr = "";
	private String istrPasswordStr = "123456";
	private Integer istrUpperLimitStr = 0;
/**************************************************************************************************************************/	
	
	public static void main(String[] args) 
	{
		//第一步先连接数据库
		MySQLManager.connect();
		//运行舞台
		Application.launch(args);
		//最后一步断开数据库连接
		MySQLManager.closeConnection();
	}
	Stage stuPrimaryStage;
/**************************************************start重载***************************************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type for instructor
		selectionState[0] = "全部";
		selectionState[1] = "选满";
		selectionState[2] = "未选满";
		// Set selection type for student
		stuState[0] = "全部";
		stuState[1] = "选定";
		stuState[2] = "待定";
		stuState[3] = "未选";
		// Set selection type for student
		stuEditState[0] = "未选";
		stuEditState[1] = "待定";
		stuEditState[2] = "选定";
		// 创建标签页面板
		tabPane = new TabPane();
		// 创建学生标签
		Tab studentTab = new Tab("学生信息");
        studentTab.setContent(studentSample());
        // 创建教师标签
        Tab teacherTab = new Tab("导师信息");
        teacherTab.setContent(teacherSample());
        // 把创建的标签对象放入标签页面板
        tabPane.getTabs().addAll(studentTab, teacherTab);
        
        
        
        // 把标签页面板放入场景
		Scene scene = new Scene(tabPane, 1000, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("管理员窗口");
		primaryStage.show();
		stuPrimaryStage = primaryStage;
	}

/*****************************************************导师信息管理页节点**********************************************************/	

	@SuppressWarnings("unchecked")
	private Node teacherSample() 
	{
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(0, 20, 20 ,20));
		// 创建水平布局hBox，存放查询导师方式下拉框、按钮等组件
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 0, 0 ,0));
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		//HBox布局，用于存放学生表对应德VHbox和学生选导师操作板块
		HBox bottomHbox = new HBox();
		bottomHbox.setPadding(new Insets(10, 0, 0 ,0));
		bottomHbox.setSpacing(10);
		
		//VBox布局，用于存放显示查询到的学生信息的表
		VBox stuTableBox = new VBox();
		stuTableBox.setSpacing(10);
		stuTableBox.setAlignment(Pos.CENTER_LEFT);
		Label LabelSelectedStu = new Label("选择该导师的学生：");
		stuTableBox.getChildren().addAll(LabelSelectedStu, stuTable);
		
/**************************************************导师信息呈现*****************************************************/		
		//创建单选列表及单选列表点击事件，根据导师状态查询、筛选导师
		Label labelIstrState = new Label("导师状态：");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(selectionState);
		cboSelectState.getItems().addAll(itemsMode);
		cboSelectState.setPrefWidth(125);
		cboSelectState.setValue(selectionState[0]);
		IstrCboQuery();
		cboSelectState.setOnAction(e -> {
			if(itemsMode.indexOf(cboSelectState.getValue()) == 0)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
				IstrCboQuery();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 1)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata where stuNum = upperLimit");
				IstrCboQuery();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 2)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata where stuNum < upperLimit");
				IstrCboQuery();
			}
		});
		
		//根据导师姓名搜索导师功能
		Label labelSearch = new Label("搜索导师：");
		TextField tfSearch = new TextField();
		tfSearch.setPromptText("输入导师姓名...");  
		Button btnSearch = new Button("搜索");
		Button btnLogout = new Button("退出");
		btnSearch.setOnAction(e -> {
			String searchName = tfSearch.getText().toString();
			if(searchName.length()!= 0)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata where Istr_name = \'" + searchName + "\'");
				System.out.println(istrQueryStr.toString());
				istrData = FXCollections.observableArrayList();
				result = MySQLManager.sqlQuery(istrQueryStr.toString());
				try 
				{
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
									result.getString(6), result.getString(7), result.getInt(8),result.getInt(9));
						istrData.add(instructor);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				istrTable.setItems(istrData);	
				idCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("id"));
				nameCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("name"));
				sexCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("sex"));
				rankCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("rank"));
				directionCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("direction"));
				telephoneCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("telephone"));
				passwordCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("password"));
				stuNumCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("stuNum"));
				upperLimitCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("upperLimit"));	
			}
		});
		btnLogout.setOnAction(e -> {
			stuPrimaryStage.close();
			Login loginWindow = new Login();
			try 
			{
				loginWindow.start(stuPrimaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		//导师TableView控件
		istrTable.setEditable(false);
		idCol.setMinWidth(100);	
		nameCol.setMinWidth(100);	
		sexCol.setMinWidth(100);	
		rankCol.setMinWidth(110);		
		directionCol.setMinWidth(100);			
		telephoneCol.setMinWidth(120);	
		passwordCol.setMinWidth(100);
		stuNumCol.setMinWidth(100);
		upperLimitCol.setMinWidth(100);	
		istrTable.getColumns().addAll(idCol, nameCol, sexCol, rankCol, 
						directionCol, telephoneCol,passwordCol, stuNumCol, upperLimitCol); 	
		istrTable.setOnMouseClicked(new MouseClickedListener());// 为导师表格注册鼠标事件
		istrTable.setPrefHeight(285);
		//存放导师TableView和导师信息编辑部分
		VBox tableViewBox = new VBox();
		//导师信息修改
		GridPane istrEditPane = new GridPane();
		istrEditPane.setHgap(10);
		istrEditPane.setVgap(5);
		Label labelIstrEdit = new Label("导师信息修改：");
		labelIstrEdit.setPadding(new Insets(20, 0, 10, 0));
		Label labelIstrID = new Label("工号");
		Label labelIstrName = new Label("姓名");
		Label labelIstrSex = new Label("性别");
		Label labelIstrRank = new Label("职称");
		Label labelIstrDirection = new Label("方向");
		Label labelIstrPhone = new Label("联系方式");
		Label labelIstrPassword = new Label("密码");
		Label labelIstrUpperLimit = new Label("学生上限");
		istrEditPane.add(labelIstrID, 0, 0);
		istrEditPane.add(labelIstrName, 1, 0);
		istrEditPane.add(labelIstrSex, 2, 0);
		istrEditPane.add(labelIstrRank, 3, 0);
		istrEditPane.add(labelIstrDirection, 4, 0);
		istrEditPane.add(labelIstrPhone, 5, 0);
		istrEditPane.add(labelIstrPassword, 6, 0);
		istrEditPane.add(labelIstrUpperLimit, 7, 0);
		tfIstrID.setPrefWidth(140);
		tfIstrName.setPrefWidth(140);
		tfIstrSex.setPrefWidth(140);
		tfIstrUpperLimit.setPrefWidth(140);
		istrEditPane.add(tfIstrID, 0, 1);
		istrEditPane.add(tfIstrName, 1, 1);
		istrEditPane.add(tfIstrSex, 2, 1);
		istrEditPane.add(tfIstrRank, 3, 1);
		istrEditPane.add(tfIstrDirection, 4, 1);
		istrEditPane.add(tfIstrPhone, 5, 1);
		istrEditPane.add(tfIstrPassword, 6, 1);
		istrEditPane.add(tfIstrUpperLimit, 7, 1);
		tfIstrID.setPromptText("输入工号..."); 
		tfIstrName.setPromptText("输入姓名..."); 
		tfIstrSex.setPromptText("输入性别..."); 
		tfIstrRank.setPromptText("输入职称..."); 
		tfIstrDirection.setPromptText("输入研究方向..."); 
		tfIstrPhone.setPromptText("输入联系方式..."); 
		tfIstrPassword.setPromptText("输入登录密码..."); 
		tfIstrUpperLimit.setPromptText("学生上限..."); 
		
		//增删改操作按钮
		HBox btnHBox = new HBox();
		btnHBox.setAlignment(Pos.CENTER_RIGHT);
		btnHBox.setSpacing(20);
		btnHBox.setPadding(new Insets(20, 0, 0, 0));
		Button btnEdit = new Button("修改信息");
		Button btnAdd = new Button("添加记录");
		Button btnDelete = new Button("删除导师");
		btnHBox.getChildren().addAll(btnEdit, btnAdd, btnDelete);
		//导师信息修改、添加、删除鼠标点击事件
		btnEdit.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的导师记录！", null);
				alert.showAndWait();
			}
			else if(isChosenOne)
			{
				istrEditIDStr = tfIstrID.getText().toString().trim();
				istrEditNameStr = tfIstrName.getText().toString().trim();
				istrSexStr = tfIstrSex.getText().toString().trim();
				istrRankStr = tfIstrRank.getText().toString().trim();
				istrDirectionStr = tfIstrDirection.getText().toString().trim();
				istrPhoneStr = tfIstrPhone.getText().toString().trim();
				istrPasswordStr = tfIstrPassword.getText().toString().trim();
				istrUpperLimitStr = Integer.parseInt(tfIstrUpperLimit.getText().toString().trim());
				if(istrEditIDStr.length() == 0 || istrEditNameStr.length() == 0 || istrPhoneStr.length() == 0)
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "导师关键信息（工号、姓名、手机号）填写不全！", null);
					alert.showAndWait();
				}
				else
				{
					istrUpdateStr.replace(0, istrUpdateStr.length(), 
							"update instructordata set Istr_name = \"" + istrEditNameStr + 
							"\", Istr_sex = \"" + istrSexStr + 
							"\", Istr_rank = \"" + istrRankStr + 
							"\", Istr_direction = \"" + istrDirectionStr + 
							"\", Istr_telephone = \"" + istrPhoneStr + 
							"\", Istr_password = \"" + istrPasswordStr + 
							"\", upperLimit = " + istrUpperLimitStr + 
					" where Istr_id = " + selectedIstr.getId());
					System.out.println("当前导师数据库更新语句： " + istrUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//重置sql语句，刷新表格
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;//重新选择表格中数据
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "导师信息修改成功！", null);
							alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "导师信息修改失败！", null);
						alert.showAndWait();
					}
				}
			}
		});
		
		btnAdd.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的导师记录！", null);
				alert.showAndWait();
			}
			else if(isChosenOne)
			{
				istrEditIDStr = tfIstrID.getText().toString().trim();
				istrEditNameStr = tfIstrName.getText().toString().trim();
				istrSexStr = tfIstrSex.getText().toString().trim();
				istrRankStr = tfIstrRank.getText().toString().trim();
				istrDirectionStr = tfIstrDirection.getText().toString().trim();
				istrPhoneStr = tfIstrPhone.getText().toString().trim();
				istrPasswordStr = tfIstrPassword.getText().toString().trim();
				istrUpperLimitStr = Integer.parseInt(tfIstrUpperLimit.getText().toString().trim());
				if(istrEditIDStr.length() == 0 || istrEditNameStr.length() == 0 || istrPhoneStr.length() == 0)
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "导师关键信息（工号、姓名、手机号）填写不全！", null);
					alert.showAndWait();
				}
				else
				{
					istrUpdateStr.replace(0, istrUpdateStr.length(), 
							"insert into instructordata values(\"" + 
									istrEditIDStr + "\", \"" + istrEditNameStr + "\",\"" + istrSexStr + "\",\"" + istrRankStr + "\",\"" + istrDirectionStr + "\",\"" + istrPhoneStr + "\",\"" + istrPasswordStr + "\", " + 0 + ", " + istrUpperLimitStr + ")");
					System.out.println("当前添加导师记录数据库更新语句： " + istrUpdateStr.toString());
					numOfRecords = MySQLManager.sqlInsert(istrUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//重置sql语句，刷新表格
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;//重新选择表格中数据
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生信息添加成功！", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "导师信息添加失败！", null);
						alert.showAndWait();
					}
				}
			}
		});
		btnDelete.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的导师记录！", null);
				alert.showAndWait();
			}
			else if(isChosenOne)
			{
				String idStr = tfIstrID.getText().toString().trim();
				istrUpdateStr.replace(0, istrUpdateStr.length(), "delete from instructordata where Istr_id = " + idStr);
				System.out.println("当前数据库删除行语句： " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlDelete(istrUpdateStr.toString());
				if(numOfRecords == 1)
				{
					stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"未选\", instructor = \"\", instructor_id = \"\" where instructor_id = \"" + idStr + "\"");
					System.out.println("当前修改学生数据库更新语句： " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					//重置sql语句，刷新表格
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					isChosenOne = false;//重新选择表格中数据
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "导师信息删除成功！", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "导师信息删除失败！", null);
					alert.showAndWait();
				}
			}
		});
		tableViewBox.getChildren().addAll(istrTable, labelIstrEdit, istrEditPane, btnHBox);
/**************************************************所选导师学生选择情况******************************************************/			        
		//显示所选导师当前所带学生简要信息表
		stuTable.setEditable(true);
		stuIdCol.setMinWidth(80);	
		stuNameCol.setMinWidth(80);	
		stuSexCol.setMinWidth(60);	
		stuMajorCol.setMinWidth(80);		
		stuClassCol.setMinWidth(80);	
		stuStateCol.setMinWidth(80);
		stuTableBox.setPrefHeight(200);
		stuTable.getColumns().addAll(stuIdCol, stuNameCol, stuSexCol, stuMajorCol, 
				stuClassCol, stuStateCol); 
		stuTable.setOnMouseClicked(new subStuClickedListener());// 为学生简要信息表注册鼠标事件
		
		
		//导师界面修改学生选导师状态
		VBox editStuStateBox = new VBox();
		editStuStateBox.setPadding(new Insets(0, 0, 0, 20));
		editStuStateBox.setSpacing(10);
		GridPane editStuStatePane = new GridPane();
		editStuStatePane.setHgap(0);
		editStuStatePane.setVgap(10);
		final ToggleGroup group = new ToggleGroup();  
		Label labelEditState = new Label("修改学生状态：");
		Label labelStuID = new Label("学号：");
		Label labelStuName = new Label("姓名：");
		Label labelStuState = new Label("状态：");
		editStuStatePane.add(labelStuID , 0, 0);
		editStuStatePane.add(labelStuName , 2, 0);
		editStuStatePane.add(labelStuState , 4, 0);
		
		GridPane.setMargin(stuIDInfo, new Insets(0, 20, 0, 0));
		GridPane.setMargin(stuNameInfo, new Insets(0, 20, 0, 0));
		GridPane.setMargin(stuStateInfo, new Insets(0, 20, 0, 0));
		editStuStatePane.add(stuIDInfo , 1, 0);
		editStuStatePane.add(stuNameInfo , 3, 0);
		editStuStatePane.add(stuStateInfo , 5, 0);
		
		//按钮
		GridPane stuBtnPane = new GridPane();
		RadioButton rbSelected = new RadioButton("选定");
		rbSelected.setToggleGroup(group);
		rbSelected.setSelected(true);
		RadioButton rbCancelSelect = new RadioButton("清空");
		Button btnChangeState = new Button("确认修改");
		stuBtnPane.setPadding(new Insets(10, 0, 0, 0));
		stuBtnPane.setHgap(10);
		stuBtnPane.setVgap(10);
		rbCancelSelect.setToggleGroup(group);
		stuBtnPane.add(rbSelected , 0, 0);
		stuBtnPane.add(rbCancelSelect , 1, 0);
		stuBtnPane.add(btnChangeState , 0, 1);
		rbSelected.setOnAction(e -> {
			changeState = "选定";
		});
		rbCancelSelect.setOnAction(e -> {
			changeState = "未选";
		});
		btnChangeState.setOnAction(e -> {
			if(!isChosenOnesubStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的学生记录！", null);
				alert.showAndWait();
			}
			else
			{
				if(selectedsubStu.getState().equals("选定"))
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "学生状态已选定，不能修改！", null);
					alert.showAndWait();
				}
				else if(changeState.equals("选定"))
				{
					if(selectedIstr.getUpperLimit() - selectedIstr.getStuNum() > 0)
					{
						stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"选定\" where instructor_id = \"" + selectedIstr.getId() + "\"");
						int finalSutNum = selectedIstr.getStuNum() + 1;
						istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = \""+ finalSutNum + "\" where Istr_id = \"" + selectedIstr.getId() + "\"");
						numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
						if(numOfRecords == 1)
						{
							//更新导师数据
							numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
							System.out.println("当前修改导师数据库更新语句： " + istrUpdateStr.toString());
							if(numOfRecords == 1)
							{
								System.out.println("导师数据修改成功！");
							}
							//重置sql语句，刷新表格
							stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
							stuCboQuery();
							istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
							IstrCboQuery();
							isChosenOne = false;
							isChosenOnesubStu = false;//重新选择表格中数据
							Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生状态修改成功！", null);
							alert.showAndWait();
						}
						else
						{
							Alert alert = new Alert(Alert.AlertType.WARNING, "学生状态修改失败", null);
							alert.showAndWait();
						}
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "学生状态修改失败", null);
						alert.showAndWait();
					}
				}
				else if(changeState.equals("未选"))
				{
					stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"未选\", instructor = \"\", instructor_id = \"\" where instructor_id = \"" + selectedIstr.getId() + "\"");
					System.out.println("当前修改学生数据库更新语句： " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//重置sql语句，刷新表格
						stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
						stuCboQuery();
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;
						isChosenOnesubStu = false;//重新选择表格中数据
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生状态修改成功！", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "学生状态修改失败", null);
						alert.showAndWait();
					}
				}
			}
		});
		editStuStateBox.getChildren().addAll(labelEditState, editStuStatePane, stuBtnPane);
        // 选中某个单选框时输出选中的值
/**************************************************************************************************************/		
		
		//将所有查询相关控件加入HBox布局中，并将HBox布局嵌入vHBox布局中
		hBox.getChildren().addAll(labelIstrState, cboSelectState, labelSearch, tfSearch, btnSearch, btnLogout);
		HBox.setMargin(labelSearch, new Insets(0, 0, 0, 330));
		bottomHbox.getChildren().addAll(stuTableBox, editStuStateBox);
		
		vBox.getChildren().addAll(hBox, tableViewBox, bottomHbox); 
		return vBox;
	}

/*********************************************** 创建学生标签中的内容，并返回该节点*************************************************/
	@SuppressWarnings("unchecked")
	private Node studentSample() 
	{		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(0, 20, 20 ,20));
		// 创建水平布局hBox，存放查询学生方式下拉框、按钮等组件
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 0, 0 ,0));
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		//HBox布局，用于存放当前选中学生对应的导师信息
		HBox bottomHbox = new HBox();
		bottomHbox.setPadding(new Insets(10, 0, 0 ,0));
		bottomHbox.setSpacing(10);
		
		//VBox布局，用于存放显示查询到的导师信息
		VBox stuTableBox = new VBox();
		stuTableBox.setSpacing(10);
		stuTableBox.setAlignment(Pos.CENTER_LEFT);
		Label LabelSelectedStu = new Label("该学生选择的导师：");
		stuTableBox.getChildren().add(LabelSelectedStu);
		
/**************************************************导师信息呈现*****************************************************/		
		//创建单选列表及单选列表点击事件，根据导师状态查询、筛选导师
		Label labelStuState = new Label("学生状态：");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(stuState);
		cboStuState.getItems().addAll(itemsMode);
		cboStuState.setPrefWidth(125);
		cboStuState.setValue(stuState[0]);
		stuCboQuery();
		cboStuState.setOnAction(e -> {
			if(itemsMode.indexOf(cboStuState.getValue()) == 0)
			{
				System.out.println("下拉条选中的值： " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 1)
			{
				System.out.println("下拉条选中的值： " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'选定\'");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 2)
			{
				System.out.println("下拉条选中的值： " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'待定\'");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 3)
			{
				System.out.println("下拉条选中的值： " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'未选\'");
				stuCboQuery();
			}
		});
		
		//根据导师姓名搜索导师功能
		Label labelStuSearch = new Label("搜索学生：");
		TextField tfStuSearch = new TextField();
		tfStuSearch.setPromptText("输入学生姓名...");  
		Button btnStuSearch = new Button("搜索");
		btnStuSearch.setOnAction(e -> {
			String searchName = tfStuSearch.getText().toString();
			if(searchName.length()!= 0)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where name = \'" + searchName + "\'");
				System.out.println(stuQueryStr.toString());
				stuCboQuery();
			}
		});

		//学生TableView控件
		mainStuTable.setEditable(false);
		mainStuIDCol.setMinWidth(100);	
		mainStuNameCol.setMinWidth(100);	
		mainStuSexCol.setMinWidth(60);	
		mainStuMajorCol.setMinWidth(100);		
		mainStuClassCol.setMinWidth(100);			
		mainStuPhoneCol.setMinWidth(120);	
		mainStuPasswordCol.setMinWidth(100);
		mainStuStateCol.setMinWidth(60);
		mainStuIstrIDCol.setMinWidth(100);	
		mainStuIstrNameCol.setMinWidth(100);	
		mainStuTable.getColumns().addAll(mainStuIDCol, mainStuNameCol, mainStuSexCol, 
				mainStuMajorCol, mainStuClassCol, mainStuPhoneCol, mainStuPasswordCol,mainStuStateCol, mainStuIstrIDCol, mainStuIstrNameCol); 	
		//mainStuTable.setOnMouseClicked(new MouseClickedListener());// 为导师表格注册鼠标事件
		mainStuTable.setPrefHeight(285);
		mainStuTable.setOnMouseClicked(new stuMouseClickedListener());// 为学生表格注册鼠标事件
		//存放学生TableView和导师信息编辑部分
		VBox tableViewBox = new VBox();
		
		//学生个人信息修改
		GridPane stuInfoPane = new GridPane();
		stuInfoPane.setHgap(10);
		stuInfoPane.setVgap(5);
		Label labelStuEdit = new Label("学生个人信息：");
		labelStuEdit.setPadding(new Insets(20, 0, 10, 0));
		Label labelStuID = new Label("学号");
		Label labelStuName = new Label("姓名");
		Label labelStuSex = new Label("性别");
		Label labelStuMajor = new Label("专业");
		Label labelStuClass = new Label("班级");
		Label labelStuPhone = new Label("联系方式");
		Label labelStuPassword = new Label("密码");


		stuInfoPane.add(labelStuID, 0, 0);
		stuInfoPane.add(labelStuName, 1, 0);
		stuInfoPane.add(labelStuSex, 2, 0);
		stuInfoPane.add(labelStuMajor, 3, 0);
		stuInfoPane.add(labelStuClass, 4, 0);
		stuInfoPane.add(labelStuPhone, 5, 0);
		stuInfoPane.add(labelStuPassword, 6, 0);

		stuInfoPane.add(tfStuID, 0, 1);
		stuInfoPane.add(tfStuName, 1, 1);
		stuInfoPane.add(tfStuSex, 2, 1);
		stuInfoPane.add(tfStuMajor, 3, 1);
		stuInfoPane.add(tfStuClass, 4, 1);
		stuInfoPane.add(tfStuPhone, 5, 1);
		stuInfoPane.add(tfStuPassword, 6, 1);

		
		tfStuID.setPromptText("输入学号..."); 
		tfStuName.setPromptText("输入姓名..."); 
		tfStuSex.setPromptText("输入性别..."); 
		tfStuMajor.setPromptText("输入专业..."); 
		tfStuClass.setPromptText("输入班级..."); 
		tfStuPhone.setPromptText("输入手机号..."); 
		tfStuPassword.setPromptText("输入登录密码...");
		tfStuID.setPrefWidth(140);
		tfStuName.setPrefWidth(140);
		tfStuSex.setPrefWidth(100);
		tfStuMajor.setPrefWidth(140);
		tfStuClass.setPrefWidth(140);
		tfStuPhone.setPrefWidth(160);
		tfStuPassword.setPrefWidth(160);
		//学生导师信息修改
		GridPane stuIstrInfoPane = new GridPane();
		stuIstrInfoPane.setHgap(10);
		stuIstrInfoPane.setVgap(5);
		Label labelIstrOfStu = new Label("学生导师信息：");
		labelIstrOfStu.setPadding(new Insets(20, 0, 0, 0));
		Label labelEditState = new Label("选择状态");
		Label labelStuIstrID = new Label("导师工号");
		Label labelStuIstrName = new Label("导师姓名");
		stuIstrInfoPane.add(labelEditState, 0, 0);
		stuIstrInfoPane.add(labelStuIstrID, 1, 0);
		stuIstrInfoPane.add(labelStuIstrName, 2, 0);
		//学生选择状态选择
		ObservableList<String> stateType = FXCollections.observableArrayList(stuEditState);
		cboEditState.getItems().addAll(stateType);
		cboEditState.setPrefWidth(100);
		cboEditState.setValue(stuEditState[0]);
		cboEditState.setOnAction(e -> {
			if(stateType.indexOf(cboEditState.getValue()) == 0)
			{
				editState.replace(0, editState.length(), "未选");
			}
			else if(stateType.indexOf(cboEditState.getValue()) == 1)
			{
				editState.replace(0, editState.length(), "待定");
			}
			else if(stateType.indexOf(cboEditState.getValue()) == 2)
			{
				editState.replace(0, editState.length(), "选定");
			}
		});
		stuIstrInfoPane.add(cboEditState, 0, 1);
		stuIstrInfoPane.add(tfStuIstrID, 1, 1);
		stuIstrInfoPane.add(tfStuIstrName, 2, 1);
		tfStuIstrID.setPrefWidth(160);
		tfStuIstrName.setPrefWidth(160);
		tfStuIstrID.setPromptText("输入导师工号..."); 
		tfStuIstrName.setPromptText("输入导师姓名..."); 
		
		//增删改操作按钮
		HBox btnHBox = new HBox();
		btnHBox.setAlignment(Pos.CENTER_LEFT);
		btnHBox.setSpacing(20);
		btnHBox.setPadding(new Insets(20, 0, 0, 0));
		Button btnEdit = new Button("修改信息");
		Button btnAdd = new Button("添加记录");
		Button btnDelete = new Button("删除学生");
		
		//学生信息修改按钮点击事件
		btnEdit.setOnAction(e->{//更新数据库
			if(!isChosenOneStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的学生记录！", null);
				alert.showAndWait();
			}
			else if(isChosenOneStu)
			{
				int availabelnum = availableStuNum(selectedStu.getInstructorID());
				
				idStr = tfStuID.getText().toString().trim();
				nameStr = tfStuName.getText().toString().trim();
				sexStr = tfStuSex.getText().toString().trim();
				majorStr = tfStuMajor.getText().toString().trim();
				classStr = tfStuClass.getText().toString().trim();
				phoneStr = tfStuPhone.getText().toString().trim();
				passwordStr = tfStuPassword.getText().toString().trim();
				stateStr = cboEditState.getValue().toString().trim();

				if(stateStr.equals("未选"))
				{
					istrIDStr = "";
					istrNameStr = "";
				}
				else if(tfStuIstrID.getText().toString().trim().equals("") || tfStuIstrID.getText().toString().trim().equals(""))
				{
					stateStr = "未选";
				}
				else
				{
					istrIDStr = tfStuIstrID.getText().toString().trim();
					istrNameStr = tfStuIstrName.getText().toString().trim();
				}
				stuUpdateStr.replace(0, stuUpdateStr.length(), 
						"update studentdata set name = \"" + nameStr + 
						"\", sex = \"" + sexStr + 
						"\", major = \"" + majorStr + 
						"\", class = \"" + classStr + 
						"\", telephone = \"" + phoneStr + 
						"\", state = \"" + stateStr + 
						"\", instructor = \"" + istrNameStr + 
						"\", instructor_id = \"" + istrIDStr + 
						"\", stu_password = \"" + passwordStr + 
				"\" where id = " + selectedStu.getId());

				if(isUpdatePermitted(beginState, stateStr, availabelnum))
				{
					System.out.println("当前学生数据库更新语句： " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//修改导师数据库
						int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, stateStr);
						istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
						System.out.println("当前修改导师数据库更新语句： " + istrUpdateStr.toString());
						numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
						//重置sql语句，刷新表格
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
						stuCboQuery();
						isChosenOneStu = false;//重新选择表格中数据
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生信息修改成功！", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "学生信息修改失败！", null);
						alert.showAndWait();
					}
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "指定导师不可再选", null);
					alert.showAndWait();
				}
			}
		});
		btnAdd.setOnAction(e->{//添加记录
			int availabelnum = 0;
			idStr = tfStuID.getText().toString().trim();
			nameStr = tfStuName.getText().toString().trim();
			sexStr = tfStuSex.getText().toString().trim();
			majorStr = tfStuMajor.getText().toString().trim();
			classStr = tfStuClass.getText().toString().trim();
			phoneStr = tfStuPhone.getText().toString().trim();
			passwordStr = tfStuPassword.getText().toString().trim();
			stateStr = cboEditState.getValue().toString().trim();
			istrIDStr = tfStuIstrID.getText().toString().trim();
			istrNameStr = tfStuIstrName.getText().toString().trim();
			if(stateStr.equals("未选"))
			{
				istrIDStr = "";
				istrNameStr = "";
				availabelnum = 1;
			}
			else
			{
				availabelnum = availableStuNum(istrIDStr);
			}
			
			if(idStr.length() == 0 || nameStr.length() == 0 || phoneStr.length() == 0)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "学生关键信息（学号、姓名、手机号）填写不全！", null);
				alert.showAndWait();
			}
			else if(isUpdatePermitted(beginState, stateStr, availabelnum))
			{
				stuUpdateStr.replace(0, stuUpdateStr.length(), 
						"insert into studentdata values(\"" + 
								idStr + "\", \"" + nameStr + "\",\"" + sexStr + "\",\"" + majorStr + "\",\"" + classStr + "\",\"" + phoneStr + "\",\"" + stateStr + "\",\"" + istrNameStr + "\",\""+ istrIDStr + "\",\"" + passwordStr
						+ "\")");
				System.out.println("当前添加学生记录数据库更新语句： " + stuUpdateStr.toString());
				numOfRecords = MySQLManager.sqlInsert(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					//修改导师数据库
					int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, stateStr);
					istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
					System.out.println("当前修改导师数据库更新语句： " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					//重置sql语句，刷新表格
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					isChosenOneStu = false;//重新选择表格中数据
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生信息添加成功！", null);
					alert.showAndWait();
				}
			}
		});
		btnDelete.setOnAction(e->{//删除记录
			if(!isChosenOneStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前没有选中的学生记录！", null);
				alert.showAndWait();
			}
			else if(isChosenOneStu)
			{
				String idStr = tfStuID.getText().toString().trim();
				stuUpdateStr.replace(0, stuUpdateStr.length(), "delete from studentdata where id = " + idStr);
				System.out.println("当前数据库删除记录语句： " + stuUpdateStr.toString());
				numOfRecords = MySQLManager.sqlDelete(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, "未选");
					istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
					System.out.println("当前修改导师数据库更新语句： " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					//重置sql语句，刷新表格
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					isChosenOneStu = false;//重新选择表格中数据
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "学生信息删除成功！", null);
					alert.showAndWait();
				}
			}
		});
		//各个布局的整合
		btnHBox.getChildren().addAll(btnEdit, btnAdd, btnDelete);
		hBox.getChildren().addAll(labelStuState, cboStuState, labelStuSearch, tfStuSearch, btnStuSearch);
		HBox.setMargin(labelStuSearch, new Insets(0, 0, 0, 330));
		tableViewBox.getChildren().addAll(mainStuTable, labelStuEdit, stuInfoPane, labelIstrOfStu,stuIstrInfoPane, btnHBox);
		vBox.getChildren().addAll(hBox, tableViewBox, bottomHbox);
		return vBox;
	}
	//导师界面学生简要信息表点击事件
	public class subStuClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// 得到用户选择的记录
			int selectedRow = stuTable.getSelectionModel().getSelectedIndex();
			System.out.println("被选中的学生记录： 第" + selectedRow + "条");
			// 如果确实选取了某条记录
			if(selectedRow!=-1)
			{
				// 获取选择的记录
				isChosenOnesubStu = true;//当前已经选中一个学生
				//System.out.println("学生表： " + mainStuData);
				student = stuData.get(selectedRow);
				selectedsubStu = stuData.get(selectedRow);
				System.out.println("被选中的学生名字： " + selectedsubStu.getName());
				
				stuIDInfo.setText(selectedsubStu.getId());
				stuNameInfo.setText(selectedsubStu.getName());
				stuStateInfo.setText(selectedsubStu.getState());
			}
		}
	}	
	//学生信息修改界面，学生tableView点击事件
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// 得到用户选择的记录
			int selectedRow = mainStuTable.getSelectionModel().getSelectedIndex();
			System.out.println("被选中记录： 第" + selectedRow + "条");
			// 如果确实选取了某条记录
			if(selectedRow!=-1)
			{
				// 获取选择的记录
				isChosenOneStu = true;//当前已经选中一个学生
				//System.out.println("学生表： " + mainStuData);
				student = mainStuData.get(selectedRow);
				selectedStu = mainStuData.get(selectedRow);
				System.out.println("被选中的学生名字： " + selectedStu.getName());
				
				tfStuID.setText(selectedStu.getId().toString());
				tfStuName.setText(selectedStu.getName().toString());
				tfStuSex.setText(selectedStu.getSex().toString());
				tfStuMajor.setText(selectedStu.getMajor());
				tfStuClass.setText(selectedStu.getStuClass());
				tfStuPhone.setText(selectedStu.getTelephone().toString());
				tfStuPassword.setText(selectedStu.getPassword().toString());
				if(selectedStu.getState().toString().equals("未选"))
				{
					cboEditState.setValue(stuEditState[0]);
					beginState = "未选";
					System.out.println("选中时初始状态： " + beginState);
				}
				else if(selectedStu.getState().toString().equals("待定"))
				{
					cboEditState.setValue(stuEditState[1]);
					beginState = "待定";
					System.out.println("选中时初始状态： " + beginState);
				}
				else if(selectedStu.getState().toString().equals("选定"))
				{
					cboEditState.setValue(stuEditState[2]);
					beginState = "选定";
					System.out.println("选中时初始状态： " + beginState);
				}
				tfStuIstrID.setText(selectedStu.getInstructorID());
				tfStuIstrName.setText(selectedStu.getInstructor());
			}
		}
	}	
	//导师Tablview点击事件
	public class MouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// 得到用户选择的记录
			int selectedRow = istrTable.getSelectionModel().getSelectedIndex();
			
			// 如果确实选取了某条记录
			if(selectedRow!=-1)
			{
				// 获取选择的记录
				isChosenOne = true;//当前学生用户已经选中一个导师
				instructor = istrData.get(selectedRow);
				selectedIstr = istrData.get(selectedRow);
				System.out.println("被选中导师名字： " + selectedIstr.getName());
			  	stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where instructor_id = " + instructor.getId());
				System.out.println(stuQueryStr.toString());
				stuResult = MySQLManager.sqlQuery(stuQueryStr.toString());
				

				tfIstrID.setText(selectedIstr.getId());
				tfIstrName.setText(selectedIstr.getName());
				tfIstrSex.setText(selectedIstr.getSex());
				tfIstrRank.setText(selectedIstr.getRank());
				tfIstrDirection.setText(selectedIstr.getDirection());
				tfIstrPhone.setText(selectedIstr.getTelephone());
				tfIstrPassword.setText(selectedIstr.getPassword());
				tfIstrUpperLimit.setText(Integer.toString(selectedIstr.getUpperLimit()));
				stuIDInfo.setText("未选中");
				stuNameInfo.setText("未选中");
				stuStateInfo.setText("未选中");
				isChosenOnesubStu = false;//当前已经选中一个学生
				
				stuData = FXCollections.observableArrayList();
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
								stuResult.getString(3), stuResult.getString(4), stuResult.getString(5), stuResult.getString(7));
						stuData.add(student);
					}
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//将查询到的学生信息关联到学生表中
				stuTable.setItems(stuData);
				
				stuIdCol.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
				stuNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
				stuSexCol.setCellValueFactory(new PropertyValueFactory<Student,String>("sex"));
				stuMajorCol.setCellValueFactory(new PropertyValueFactory<Student,String>("major"));
				stuClassCol.setCellValueFactory(new PropertyValueFactory<Student,String>("stuClass"));
				stuStateCol.setCellValueFactory(new PropertyValueFactory<Student,String>("state"));
			}
		}
	}	
	//改变学生下拉框时数据库查询函数
	public void stuCboQuery()
	{
		System.out.println(stuQueryStr.toString());
		mainStuData = FXCollections.observableArrayList();
		result = MySQLManager.sqlQuery(stuQueryStr.toString());
		try 
		{
			while(result.next())
			{
				System.out.print(result.getString(1)+"\t");//学号
				System.out.print(result.getString(2)+"\t");//姓名
				System.out.print(result.getString(3)+"\t");//性别
				System.out.print(result.getString(4)+"\t");//专业
				System.out.print(result.getString(5)+"\t");//班级
				System.out.print(result.getString(6)+"\t");//手机号
				System.out.print(result.getString(7)+"\t");//状态
				System.out.print(result.getString(8)+"\t");//导师名字
				System.out.print(result.getString(9)+"\t");//导师ID
				System.out.println(result.getString(10));//密码
				student = new Student(result.getString(1), result.getString(2), 
							result.getString(3), result.getString(4), result.getString(5), 
							result.getString(6),result.getString(7), result.getString(8),result.getString(9),result.getString(10));
				mainStuData.add(student);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mainStuTable.setItems(mainStuData);	
		mainStuIDCol.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
		mainStuNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
		mainStuSexCol.setCellValueFactory(new PropertyValueFactory<Student,String>("sex"));
		mainStuMajorCol.setCellValueFactory(new PropertyValueFactory<Student,String>("major"));
		mainStuClassCol.setCellValueFactory(new PropertyValueFactory<Student,String>("stuClass"));
		mainStuPhoneCol.setCellValueFactory(new PropertyValueFactory<Student,String>("telephone"));
		mainStuStateCol.setCellValueFactory(new PropertyValueFactory<Student,String>("state"));
		mainStuIstrIDCol.setCellValueFactory(new PropertyValueFactory<Student,String>("instructorID"));
		mainStuIstrNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("instructor"));	
		mainStuPasswordCol.setCellValueFactory(new PropertyValueFactory<Student,String>("password"));
	}
	//改变导师下拉框时数据库查询函数
	public void IstrCboQuery()
	{
		System.out.println(istrQueryStr.toString());
		istrData = FXCollections.observableArrayList();
		result = MySQLManager.sqlQuery(istrQueryStr.toString());
		try {
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
							result.getString(6),result.getString(7), result.getInt(8),result.getInt(9));
				istrData.add(instructor);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		istrTable.setItems(istrData);	
		idCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("name"));
		sexCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("sex"));
		rankCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("rank"));
		directionCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("direction"));
		telephoneCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("telephone"));
		passwordCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("password"));
		stuNumCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("stuNum"));
		upperLimitCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("upperLimit"));	
	}
	//根据导师工号，查询该导师剩余可提供的学生人数
	public int availableStuNum(String istrID)
	{
		int num = 0;
		String sql = "select * from instructordata where Istr_id = \"" + istrID + "\"";
		System.out.println("availableStuNum()函数数据库查询语句： " + sql);
		ResultSet sqlresult = MySQLManager.sqlQuery(sql);
		try {
			while(sqlresult.next())
			{
				num = sqlresult.getInt(9) - sqlresult.getInt(8);
				System.out.println("上限： " + sqlresult.getInt(9) + "   已选定：" + sqlresult.getInt(8));
				System.out.println("剩余可用名额：  " + num);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	//根据导师工号，查询该导师已选定的学生人数
	public int certainStuNum(String istrID)
	{
		int num = 0;
		String sql = "select * from instructordata where Istr_id = \"" + istrID + "\"";
		System.out.println("availableStuNum()函数数据库查询语句： " + sql);
		ResultSet sqlresult = MySQLManager.sqlQuery(sql);
		try {
			while(sqlresult.next())
			{
				num =sqlresult.getInt(8);
				System.out.println("已选定名额：  " + num);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	//根据修改前学生状态、想要修改成的学生状态和当前导师剩余可选学生人数判断修改和插入操作是否被允许
	public boolean isUpdatePermitted(String firstState, String lastState, int availabelNum)
	{
		int first = 0, last = 0, num = 0;
		if(firstState.equals("选定"))
		{
			first = 1;
		}
		else if(firstState.equals("待定") || firstState.equals("未选"))
		{
			first = 0;
		}
		
		if(lastState.equals("选定"))
		{
			last = 1;
		}
		else if(lastState.equals("待定") || lastState.equals("未选"))
		{
			last = 0;
		}
		num = first - last;
		
		//如果修改后多出了一个名额
		if(num == 1)
		{
			System.out.println("修改后多出了 1 个名额！");
			return true;
		}
		//如果修改后没有多出一个名额，根据可用名额数 + 消耗名额数结果作为可否进行加、更新德判断结果
		else
		{
			System.out.println("原来可用名额 " + availabelNum +" 个名额！");
			System.out.println("修改消耗了 " + num +" 个名额！");
			return availabelNum + num >= 0;
		}
	}
	//学生数据库操作之后导师已选定学生数变化数
	public int numOfChangedStuNum(String firstState, String lastState)
	{
		int first = 0, last = 0, num = 0;
		if(firstState.equals("选定"))
		{
			first = 1;
		}
		else if(firstState.equals("待定") || firstState.equals("未选"))
		{
			first = 0;
		}
		
		if(lastState.equals("选定"))
		{
			last = 1;
		}
		else if(lastState.equals("待定") || lastState.equals("未选"))
		{
			last = 0;
		}
		num = first - last;
		return num;
	}
}

