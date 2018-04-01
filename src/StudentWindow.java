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
/***************************************�����Ͷ���*****************************************/
	private Student currStuUser = Login.stuUser;//��ǰ��¼��ѧ���û�
	private Instructor instructor;
	private static Instructor selectedIstr;//��ű���ǰѧ���û�ѡΪ�Լ���ʦ�ĵ�ʦ��Ϣ
	private Student student;
	
	//����TableView�ؼ�������ʾ��ʦ��ѯ���
	Button btnQuery = new Button("��ѯ");
	private String[] selectionState = new String[3];
	private ComboBox<String> cboSelectState = new ComboBox<>();
	private TableView<Instructor> istrTable = new TableView<Instructor>();
	private static ObservableList<Instructor> istrData = null;
	TableColumn<Instructor, String> idCol = new TableColumn<Instructor, String>("����");//������,�����ݹ���������е���
	TableColumn<Instructor, String> nameCol = new TableColumn<Instructor, String>("����");	
	TableColumn<Instructor, String> sexCol = new TableColumn<Instructor, String>("�Ա�");
	TableColumn<Instructor, String> rankCol = new TableColumn<Instructor, String>("ְ��");		
	TableColumn<Instructor, String> directionCol = new TableColumn<Instructor, String>("�о�����");		
	TableColumn<Instructor, String> telephoneCol = new TableColumn<Instructor, String>("��ϵ�绰");
	TableColumn<Instructor, String> stuNumCol = new TableColumn<Instructor, String>("ѡ��ѧ����");
	TableColumn<Instructor, String> upperLimitCol = new TableColumn<Instructor, String>("ѧ��������");
	///����TableView�ؼ�������ʾѧ����ѯ��Ϣ
	private TableView<Student> stuTable = new TableView<Student>();//ѧ��TableView
	private static ObservableList<Student> stuData = null;
	TableColumn<Student, String> stuIdCol = new TableColumn<Student, String>("ѧ��");//������,�����ݹ���������е���
	TableColumn<Student, String> stuNameCol = new TableColumn<Student, String>("����");
	TableColumn<Student, String> stuSexCol = new TableColumn<Student, String>("�Ա�");
	TableColumn<Student, String> stuMajorCol = new TableColumn<Student, String>("רҵ");
	TableColumn<Student, String> stuClassCol = new TableColumn<Student, String>("�༶");
	TableColumn<Student, String> stuStateCol = new TableColumn<Student, String>("״̬");
	//ѧ����Ϣҳ��Ӧ��ʦ�����ϢLabel
	Label stuIstrState = new Label(currStuUser.getState());
	Label stuIstrID = new Label(currStuUser.getInstructorID());
	Label stuIstrName = new Label(currStuUser.getInstructor());
	//���ݿ���ر�������
	private StringBuilder istrQueryStr = new StringBuilder("select * from instructordata");
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentdata");
	private static StringBuilder stuUpdataStr = new StringBuilder("update studentdata");
//	private static Connection con;
//	private Statement sta;
	private ResultSet result; // ��Ų�ѯ���ĵ�ʦ��Ϣ��¼��
	private ResultSet stuResult; // ��Ų�ѯ����ѧ����Ϣ��¼��
//	private static StudentWindow jdbcstuWindow;
	private int numOfRecords; // ��������ݿ������صļ�¼��
	
	//����ǰѧ���û�ѡ�еĵ�ʦ��Ϣ
	private boolean isChosenOne = false;//�жϵ�ǰѧ���û��Ƿ���ѡ����һ����ʦ
	Label labelNameInfo = new Label("δѡ�� ");
	Label labelRankInfo = new Label("δѡ��");
	Label labelDirectionInfo = new Label("δѡ��");
	Label labelLimitInfo = new Label("δѡ��");
	private TabPane tabPane;
	
	public static void main(String[] args) 
	{
//		jdbcstuWindow = new StudentWindow();
		//��һ�����������ݿ�
		MySQLManager.connect();
		Application.launch(args);
		//���һ���Ͽ����ݿ�����
		MySQLManager.closeConnection();
	}
	Stage stuPrimaryStage;
/*************************************start����*******************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type
		selectionState[0] = "ȫ��";
		selectionState[1] = "ѡ��";
		selectionState[2] = "δѡ��";
		// ������ǩҳ���
		tabPane = new TabPane();
		// ����ѧ����ǩ
		Tab studentTab = new Tab("ѧ����Ϣ");
        studentTab.setContent(studentSample());
        // ������ʦ��ǩ
        Tab teacherTab = new Tab("��ʦ��Ϣ");
        teacherTab.setContent(teacherSample());
        // ������ѯ��ǩ
        Tab modifyTab = new Tab("��Ϣ�޸�");
        modifyTab.setContent(modifyPasswordSample());
        // �Ѵ����ı�ǩ��������ǩҳ���
        tabPane.getTabs().addAll(studentTab, teacherTab, modifyTab);
        
        
        
        // �ѱ�ǩҳ�����볡��
		Scene scene = new Scene(tabPane, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ѧ������");
		primaryStage.show();
		stuPrimaryStage = primaryStage;
	}

	
/***************************************��غ���*****************************************/	
	
	
	// ������ʦ��ǩ�е����ݣ������ظýڵ�
	@SuppressWarnings("unchecked")
	private Node teacherSample() 
	{
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(0, 20, 20 ,20));
		// ����ˮƽ����hBox����Ų�ѯ��ʦ��ʽ�����򡢰�ť�����
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 0, 0 ,0));
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		//HBox���֣����ڴ��ѧ�����Ӧ��VHbox��ѧ��ѡ��ʦ�������
		HBox bottomHbox = new HBox();
		bottomHbox.setPadding(new Insets(10, 0, 0 ,0));
		bottomHbox.setSpacing(10);
		
		//VBox���֣����ڴ����ʾ��ѯ����ѧ����Ϣ�ı�
		VBox stuTableBox = new VBox();
		stuTableBox.setSpacing(10);
		stuTableBox.setAlignment(Pos.CENTER_LEFT);
		Label LabelSelectedStu = new Label("ѡ��õ�ʦ��ѧ����");
		stuTableBox.getChildren().addAll(LabelSelectedStu, stuTable);
		
		//VBox���֣����ڴ��ѧ��ѡ��ʦ�������
		VBox stuActionBox = new VBox();
		stuActionBox.setPadding(new Insets(0, 0, 0 ,20));
		stuActionBox.setSpacing(10);
		Label LabelStuAction = new Label("ѡ���Լ��ĵ�ʦ��");
		GridPane istrInfoPane = new GridPane();//��ѡ��ʦ��Ϣ
		istrInfoPane.setHgap(20);
		istrInfoPane.setVgap(20);
		Label labelIstrName = new Label("��ʦ������ ");
		Label labelIstrRank = new Label("ְ�ƣ� ");
		Label labelIstrDirection = new Label("�о����� ");
		Label labelIstrLimit = new Label("ʣ���ѡ������ ");
		istrInfoPane.add(labelIstrName, 0, 0);
		istrInfoPane.add(labelIstrRank, 0, 1);
		istrInfoPane.add(labelIstrDirection, 0, 2);
		istrInfoPane.add(labelIstrLimit, 0, 3);
		istrInfoPane.add(labelNameInfo, 1, 0);
		istrInfoPane.add(labelRankInfo, 1, 1);
		istrInfoPane.add(labelDirectionInfo, 1, 2);
		istrInfoPane.add(labelLimitInfo, 1, 3);
		Button btnSelect = new Button("ѡ��õ�ʦ");//�����ť��ѡ��ǰ��ʦ��Ϊ��ǰѧ����ʦ
		btnSelect.setOnAction(e -> {
			
			//ѡ��ʦǰ��������1����ѡ�еĵ�ʦ 		2��ѡ�еĵ�ʦʣ���ѡѧ������δ��		3��ѧ����ǰ״̬����Ϊ��ѡ����
			if(currStuUser.getState().equals("ѡ��"))
			{
				System.out.println("��ǰѡ����ʦ�� " + selectedIstr.getName());
				Alert alert = new Alert(Alert.AlertType.WARNING, "������ѡ���ĵ�ʦ��", null);
				alert.showAndWait();
			}
			else if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "û��ѡ�еĵ�ʦ��", null);
				alert.showAndWait();
			}
			else if((selectedIstr.getUpperLimit() - selectedIstr.getStuNum()) == 0)
			{
				System.out.println("��ǰ��ʦʣ���ѡѧ������ " + (selectedIstr.getUpperLimit() - selectedIstr.getStuNum()));
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰ��ʦ����ѧ���Ѵ����ޣ�", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdataStr.replace(0, stuUpdataStr.length(), "update studentdata set instructor_id = \'" + selectedIstr.getId() + "\', instructor = \'" + selectedIstr.getName() + "\', state = '����' where id = " + currStuUser.getId());
				System.out.println("��ǰ���ݿ������䣺 " + stuUpdataStr.toString());
				updateByStu(stuUpdataStr.toString());
				stuIstrState.setText("����");
				stuIstrID.setText(selectedIstr.getId());
				stuIstrName.setText(selectedIstr.getName());
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѡ��ʦ�ɹ���", null);
				alert.showAndWait();
			}
		});
		istrInfoPane.add(btnSelect, 1, 4);
		stuActionBox.getChildren().addAll(LabelStuAction,istrInfoPane);
		
/**************************************************��ʦ��Ϣ����*****************************************************/		
		//������ѡ�б���ѡ�б����¼������ݵ�ʦ״̬��ѯ��ɸѡ��ʦ
		Label labelIstrState = new Label("��ʦ״̬��");
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
		
		//���ݵ�ʦ����������ʦ����
		Label labelSearch = new Label("������ʦ��");
		TextField tfSearch = new TextField();
		tfSearch.setPromptText("���뵼ʦ����...");  
		Button btnSearch = new Button("����");
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
		
		
		//��ʦTableView�ؼ�
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
		istrTable.setOnMouseClicked(new MouseClickedListener());// Ϊ��ʦ���ע������¼�
		
/**************************************************��ѡ��ʦѧ��ѡ�����******************************************************/			        
		//��ʾ��ѡ��ʦ��ǰ����ѧ����Ϣ
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
		
		//�����в�ѯ��ؿؼ�����HBox�����У�����HBox����Ƕ��vHBox������
		hBox.getChildren().addAll(labelIstrState, cboSelectState, labelSearch, tfSearch, btnSearch);
		HBox.setMargin(labelSearch, new Insets(0, 0, 0, 330));
		bottomHbox.getChildren().addAll(stuTableBox, stuActionBox);
		vBox.getChildren().addAll(hBox, istrTable, bottomHbox); 
		return vBox;
	}

/*********************************************** ����ѧ����ǩ�е����ݣ������ظýڵ�*************************************************/
	private Node studentSample() 
	{
		BorderPane borderPane = new BorderPane();
		System.out.println("��ǰ��¼�û������� " + currStuUser.getName());
		GridPane leftVBox = new GridPane();
		GridPane rightVBox = new GridPane();
		
		Label basicInfoLabel = new Label("������Ϣ");
		basicInfoLabel.setStyle("-fx-font-size:18;");
		Label istrInfoLabel = new Label("ѡ��ʦ���");
		istrInfoLabel.setStyle("-fx-font-size:18;");
		Button btnLogout = new Button("�˳�");
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
		//��������
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
		
		//���ѧ��������Ϣ
		HBox centerHBox = new HBox();
		centerHBox.setAlignment(Pos.CENTER);
		
		GridPane leftGridPane = new GridPane();
		leftGridPane.setPadding(new Insets(20, 0, 20, 20));
		leftGridPane.setHgap(20);
		leftGridPane.setVgap(20);
		leftGridPane.setPrefWidth(500);
		Label accountLabel = new Label("ѧ��: ");
		Label nameLabel = new Label("����: ");
		Label sexLabel = new Label("�Ա�: ");
		Label majorLabel = new Label("רҵ: ");
		Label classLabel = new Label("�༶: ");
		Label telephoneLabel = new Label("�ֻ�: ");
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
		
		
		//�Ҳ�ѧ����ʦѡ�����
		GridPane rightGridPane = new GridPane();
		rightGridPane.setPadding(new Insets(20, 20, 20, 0));
		rightGridPane.setHgap(20);
		rightGridPane.setVgap(20);
		rightGridPane.setPrefWidth(500);
		Label istrState = new Label("ѡ��״̬�� ");
		Label istrID = new Label("��ʦ���ţ� ");
		Label istrName = new Label("��ʦ������ ");
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
	// ������Ϣ�޸ı�ǩ�е����ݣ������ظýڵ�
	private Node modifyPasswordSample() 
	{
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(20, 20, 20, 20));
		vBox.setSpacing(20);
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		
		Label oldPassLabel = new Label("������: ");
		Label newPassLabel = new Label("������: ");
		Label confirmPassLabel = new Label("ȷ��������: ");
		TextField oldPassword  = new TextField();
		PasswordField newPassword = new PasswordField();
		PasswordField confirmPassword = new PasswordField();
		Button btnModify = new Button("�޸�����");
		btnModify.setStyle("-fx-font-size:18;");
		btnModify.setOnAction(e -> {
			if(!currStuUser.getPassword().equals(oldPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�������������!", null);
				alert.showAndWait();
			}
			else if(oldPassword.getText().toString().equals(newPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�����벻�����������ͬ!", null);
				alert.showAndWait();
			}
			else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�������������벻һ�£�", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdataStr.replace(0, stuUpdataStr.length(), "update studentdata set stu_password = \'" + newPassword.getText().toString() + "\' where id = " + currStuUser.getId());
				System.out.println("��ǰ�޸��������ݿ������䣺 " + stuUpdataStr.toString());
				updateByStu(stuUpdataStr.toString());
				Alert alert = new Alert(Alert.AlertType.WARNING, "�����޸ĳɹ���", null);
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
	//��ʦTablview����¼�
	public class MouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// �õ��û�ѡ��ļ�¼
			int selectedRow = istrTable.getSelectionModel().getSelectedIndex();
			
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1)
			{
				// ��ȡѡ��ļ�¼
				isChosenOne = true;//��ǰѧ���û��Ѿ�ѡ��һ����ʦ
				instructor = istrData.get(selectedRow);
				selectedIstr = istrData.get(selectedRow);
				labelNameInfo.setText(selectedIstr.getName());
				labelRankInfo.setText(selectedIstr.getRank());
				labelDirectionInfo.setText(selectedIstr.getDirection());
				labelLimitInfo.setText(Integer.toString(selectedIstr.getUpperLimit() - selectedIstr.getStuNum()));
				System.out.println("��ѡ�е�ʦ���֣� " + selectedIstr.getName());
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
				//����ѯ����ѧ����Ϣ������ѧ������
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
	
	//���ݿ����
	public void updateByStu(String sql) 
	{
		try 
		{
			// ִ�в������
			numOfRecords = MySQLManager.sta.executeUpdate(sql);
			// ��ʾ���
			System.out.println("���µļ�¼����Ϊ�� " + numOfRecords);
		} catch (SQLException ex) {
			System.out.println("���³���" + ex.getMessage());
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
