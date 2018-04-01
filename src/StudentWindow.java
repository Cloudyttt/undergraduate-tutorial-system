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

public class StudentWindow extends Application
{
/***************************************声明和定义*****************************************/
	private Student currStuUser = Login.stuUser;//当前登录的学生用户
	private Instructor instructor;
	private static Instructor selectedIstr;//存放被当前学生用户选为自己导师的导师信息
	private Student student;
	
	//创建TableView控件用于显示导师查询结果
	Button btnQuery = new Button("查询");
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
	//学生信息页对应导师相关信息Label
	Label stuIstrState = new Label(currStuUser.getState());
	Label stuIstrID = new Label(currStuUser.getInstructorID());
	Label stuIstrName = new Label(currStuUser.getInstructor());
	//数据库相关变量声明
	private StringBuilder istrQueryStr = new StringBuilder("select * from instructordata");
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentdata");
	private static StringBuilder stuUpdataStr = new StringBuilder("update studentdata");
//	private static Connection con;
//	private Statement sta;
	private ResultSet result; // 存放查询到的导师信息记录集
	private ResultSet stuResult; // 存放查询到的学生信息记录集
//	private static StudentWindow jdbcstuWindow;
	private int numOfRecords; // 存放与数据库操作相关的记录数
	
	//被当前学生用户选中的导师信息
	private boolean isChosenOne = false;//判断当前学生用户是否已选中了一个导师
	Label labelNameInfo = new Label("未选中 ");
	Label labelRankInfo = new Label("未选中");
	Label labelDirectionInfo = new Label("未选中");
	Label labelLimitInfo = new Label("未选中");
	private TabPane tabPane;
	
	public static void main(String[] args) 
	{
//		jdbcstuWindow = new StudentWindow();
		//第一步先连接数据库
		MySQLManager.connect();
		Application.launch(args);
		//最后一步断开数据库连接
		MySQLManager.closeConnection();
	}
	Stage stuPrimaryStage;
/*************************************start重载*******************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type
		selectionState[0] = "全部";
		selectionState[1] = "选满";
		selectionState[2] = "未选满";
		// 创建标签页面板
		tabPane = new TabPane();
		// 创建学生标签
		Tab studentTab = new Tab("学生信息");
        studentTab.setContent(studentSample());
        // 创建教师标签
        Tab teacherTab = new Tab("教师信息");
        teacherTab.setContent(teacherSample());
        // 创建查询标签
        Tab modifyTab = new Tab("信息修改");
        modifyTab.setContent(modifyPasswordSample());
        // 把创建的标签对象放入标签页面板
        tabPane.getTabs().addAll(studentTab, teacherTab, modifyTab);
        
        
        
        // 把标签页面板放入场景
		Scene scene = new Scene(tabPane, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("学生窗口");
		primaryStage.show();
		stuPrimaryStage = primaryStage;
	}

	
/***************************************相关函数*****************************************/	
	
	
	// 创建导师标签中的内容，并返回该节点
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
		
		//VBox布局，用于存放学生选导师操作板块
		VBox stuActionBox = new VBox();
		stuActionBox.setPadding(new Insets(0, 0, 0 ,20));
		stuActionBox.setSpacing(10);
		Label LabelStuAction = new Label("选择自己的导师：");
		GridPane istrInfoPane = new GridPane();//所选导师信息
		istrInfoPane.setHgap(20);
		istrInfoPane.setVgap(20);
		Label labelIstrName = new Label("导师姓名： ");
		Label labelIstrRank = new Label("职称： ");
		Label labelIstrDirection = new Label("研究方向： ");
		Label labelIstrLimit = new Label("剩余可选人数： ");
		istrInfoPane.add(labelIstrName, 0, 0);
		istrInfoPane.add(labelIstrRank, 0, 1);
		istrInfoPane.add(labelIstrDirection, 0, 2);
		istrInfoPane.add(labelIstrLimit, 0, 3);
		istrInfoPane.add(labelNameInfo, 1, 0);
		istrInfoPane.add(labelRankInfo, 1, 1);
		istrInfoPane.add(labelDirectionInfo, 1, 2);
		istrInfoPane.add(labelLimitInfo, 1, 3);
		Button btnSelect = new Button("选择该导师");//点击按钮，选择当前导师作为当前学生导师
		btnSelect.setOnAction(e -> {
			
			//选导师前提条件：1、有选中的导师 		2、选中的导师剩余可选学生人数未满		3、学生当前状态不能为“选定”
			if(currStuUser.getState().equals("选定"))
			{
				System.out.println("当前选定导师： " + selectedIstr.getName());
				Alert alert = new Alert(Alert.AlertType.WARNING, "你已有选定的导师！", null);
				alert.showAndWait();
			}
			else if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "没有选中的导师！", null);
				alert.showAndWait();
			}
			else if((selectedIstr.getUpperLimit() - selectedIstr.getStuNum()) == 0)
			{
				System.out.println("当前导师剩余可选学生数： " + (selectedIstr.getUpperLimit() - selectedIstr.getStuNum()));
				Alert alert = new Alert(Alert.AlertType.WARNING, "当前导师所带学生已达上限！", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdataStr.replace(0, stuUpdataStr.length(), "update studentdata set instructor_id = \'" + selectedIstr.getId() + "\', instructor = \'" + selectedIstr.getName() + "\', state = '待定' where id = " + currStuUser.getId());
				System.out.println("当前数据库更新语句： " + stuUpdataStr.toString());
				updateByStu(stuUpdataStr.toString());
				stuIstrState.setText("待定");
				stuIstrID.setText(selectedIstr.getId());
				stuIstrName.setText(selectedIstr.getName());
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "选导师成功！", null);
				alert.showAndWait();
			}
		});
		istrInfoPane.add(btnSelect, 1, 4);
		stuActionBox.getChildren().addAll(LabelStuAction,istrInfoPane);
		
/**************************************************导师信息呈现*****************************************************/		
		//创建单选列表及单选列表点击事件，根据导师状态查询、筛选导师
		Label labelIstrState = new Label("导师状态：");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(selectionState);
		cboSelectState.getItems().addAll(itemsMode);
		cboSelectState.setPrefWidth(125);
		cboSelectState.setValue(selectionState[0]);
		cboQueryByStu();
		cboSelectState.setOnAction(e -> {
			if(itemsMode.indexOf(cboSelectState.getValue()) == 0)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
				cboQueryByStu();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 1)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata where stuNum = upperLimit");
				cboQueryByStu();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 2)
			{
				istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata where stuNum < upperLimit");
				cboQueryByStu();
			}
		});
		
		//根据导师姓名搜索导师功能
		Label labelSearch = new Label("搜索导师：");
		TextField tfSearch = new TextField();
		tfSearch.setPromptText("输入导师姓名...");  
		Button btnSearch = new Button("搜索");
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
									result.getString(6), result.getInt(8),result.getInt(9));
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
				stuNumCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("stuNum"));
				upperLimitCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("upperLimit"));	
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
		stuNumCol.setMinWidth(100);
		upperLimitCol.setMinWidth(100);	
		istrTable.getColumns().addAll(idCol, nameCol, sexCol, rankCol, 
						directionCol, telephoneCol, stuNumCol, upperLimitCol); 	
		istrTable.setOnMouseClicked(new MouseClickedListener());// 为导师表格注册鼠标事件
		
/**************************************************所选导师学生选择情况******************************************************/			        
		//显示所选导师当前所带学生信息
		stuTable.setEditable(true);
		stuIdCol.setMinWidth(80);	
		stuNameCol.setMinWidth(80);	
		stuSexCol.setMinWidth(60);	
		stuMajorCol.setMinWidth(110);		
		stuClassCol.setMinWidth(100);	
		stuStateCol.setMinWidth(80);
		stuTable.getColumns().addAll(stuIdCol, stuNameCol, stuSexCol, stuMajorCol, 
				stuClassCol, stuStateCol); 	
		
/**************************************************************************************************************/		
		
		//将所有查询相关控件加入HBox布局中，并将HBox布局嵌入vHBox布局中
		hBox.getChildren().addAll(labelIstrState, cboSelectState, labelSearch, tfSearch, btnSearch);
		HBox.setMargin(labelSearch, new Insets(0, 0, 0, 330));
		bottomHbox.getChildren().addAll(stuTableBox, stuActionBox);
		vBox.getChildren().addAll(hBox, istrTable, bottomHbox); 
		return vBox;
	}

/*********************************************** 创建学生标签中的内容，并返回该节点*************************************************/
	private Node studentSample() 
	{
		BorderPane borderPane = new BorderPane();
		System.out.println("当前登录用户姓名： " + currStuUser.getName());
		GridPane leftVBox = new GridPane();
		GridPane rightVBox = new GridPane();
		
		Label basicInfoLabel = new Label("基本信息");
		basicInfoLabel.setStyle("-fx-font-size:18;");
		Label istrInfoLabel = new Label("选导师情况");
		istrInfoLabel.setStyle("-fx-font-size:18;");
		Button btnLogout = new Button("退出");
		btnLogout.setPrefWidth(60);
		HBox.setMargin(btnLogout, new Insets(0,0,0,320));
		btnLogout.setOnAction(e -> {
			stuPrimaryStage.close();
			Login loginWindow = new Login();
			try {
				loginWindow.start(stuPrimaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		//顶部标题
		HBox topHBox = new HBox();
		topHBox.setPadding(new Insets(20, 20, 20, 20));
//		topHBox.setAlignment(Pos.TOP_CENTER);
		HBox hBox1 = new HBox();
		hBox1.setPrefWidth(480);
		hBox1.getChildren().add(basicInfoLabel);
		HBox hBox2 = new HBox();
		//hBox2.setPrefWidth(420);
		hBox2.getChildren().addAll(istrInfoLabel, btnLogout);
		topHBox.getChildren().addAll(hBox1, hBox2);
		
		//左侧学生基本信息
		HBox centerHBox = new HBox();
		centerHBox.setAlignment(Pos.CENTER);
		
		GridPane leftGridPane = new GridPane();
		leftGridPane.setPadding(new Insets(20, 0, 20, 20));
		leftGridPane.setHgap(20);
		leftGridPane.setVgap(20);
		leftGridPane.setPrefWidth(500);
		Label accountLabel = new Label("学号: ");
		Label nameLabel = new Label("姓名: ");
		Label sexLabel = new Label("性别: ");
		Label majorLabel = new Label("专业: ");
		Label classLabel = new Label("班级: ");
		Label telephoneLabel = new Label("手机: ");
		leftGridPane.add(accountLabel, 0, 0);
		leftGridPane.add(nameLabel, 0, 1);
		leftGridPane.add(sexLabel, 0, 2);
		leftGridPane.add(majorLabel, 0, 3);
		leftGridPane.add(classLabel, 0, 4);
		leftGridPane.add(telephoneLabel, 0, 5);
		
		Label stuAccountLabel = new Label(currStuUser.getId());
		Label stuNameLabel = new Label(currStuUser.getName());
		Label stuSexLabel = new Label(currStuUser.getSex());
		Label stuMajorLabel = new Label(currStuUser.getMajor());
		Label stuClassLabel = new Label(currStuUser.getStuClass());
		Label stuTelephoneLabel = new Label(currStuUser.getTelephone());
		leftGridPane.add(stuAccountLabel, 1, 0);
		leftGridPane.add(stuNameLabel, 1, 1);
		leftGridPane.add(stuSexLabel, 1, 2);
		leftGridPane.add(stuMajorLabel, 1, 3);
		leftGridPane.add(stuClassLabel, 1, 4);
		leftGridPane.add(stuTelephoneLabel, 1, 5);
		
		
		//右侧学生导师选择情况
		GridPane rightGridPane = new GridPane();
		rightGridPane.setPadding(new Insets(20, 20, 20, 0));
		rightGridPane.setHgap(20);
		rightGridPane.setVgap(20);
		rightGridPane.setPrefWidth(500);
		Label istrState = new Label("选择状态： ");
		Label istrID = new Label("导师工号： ");
		Label istrName = new Label("导师姓名： ");
		rightGridPane.add(istrState, 0, 0);
		rightGridPane.add(istrID, 0, 1);
		rightGridPane.add(istrName, 0, 2);
//		Label stuIstrState = new Label(currStuUser.getState());
//		Label stuIstrID = new Label(currStuUser.getInstructorID());
//		Label stuIstrName = new Label(currStuUser.getInstructor());
		rightGridPane.add(stuIstrState, 1, 0);
		rightGridPane.add(stuIstrID, 1, 1);
		rightGridPane.add(stuIstrName, 1, 2);
		centerHBox.getChildren().addAll(leftGridPane, rightGridPane);
		
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
			if(!currStuUser.getPassword().equals(oldPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "旧密码输入错误!", null);
				alert.showAndWait();
			}
			else if(oldPassword.getText().toString().equals(newPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "新密码不能与旧密码相同!", null);
				alert.showAndWait();
			}
			else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "两次新密码输入不一致！", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdataStr.replace(0, stuUpdataStr.length(), "update studentdata set stu_password = \'" + newPassword.getText().toString() + "\' where id = " + currStuUser.getId());
				System.out.println("当前修改密码数据库更新语句： " + stuUpdataStr.toString());
				updateByStu(stuUpdataStr.toString());
				Alert alert = new Alert(Alert.AlertType.WARNING, "密码修改成功！", null);
				alert.showAndWait();
				stuPrimaryStage.close();
				Login loginWindow = new Login();
				try {
					loginWindow.start(stuPrimaryStage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
				labelNameInfo.setText(selectedIstr.getName());
				labelRankInfo.setText(selectedIstr.getRank());
				labelDirectionInfo.setText(selectedIstr.getDirection());
				labelLimitInfo.setText(Integer.toString(selectedIstr.getUpperLimit() - selectedIstr.getStuNum()));
				System.out.println("被选中导师名字： " + selectedIstr.getName());
			  	stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where instructor_id = " + instructor.getId());
				System.out.println(stuQueryStr.toString());
				stuResult = MySQLManager.sqlQuery(stuQueryStr.toString());
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
	
	//数据库更新
	public void updateByStu(String sql) 
	{
		try 
		{
			// 执行插入操作
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// 显示结果
			System.out.println("更新的记录条数为： " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("更新出错：" + ex.getMessage());
		}
	}
	
	public void cboQueryByStu()
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
							result.getString(6), result.getInt(8),result.getInt(9));
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
		stuNumCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("stuNum"));
		upperLimitCol.setCellValueFactory(new PropertyValueFactory<Instructor,String>("upperLimit"));	
	}
	
}
