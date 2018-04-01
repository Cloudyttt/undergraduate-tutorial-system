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
/******************************************************���������Ͷ���************************************************************/
	private TabPane tabPane;
	private Instructor instructor;
	private static Instructor selectedIstr;//��ű���ǰѧ���û�ѡΪ�Լ���ʦ�ĵ�ʦ��Ϣ
	private Student student;
	private static Student selectedStu;//��ŵ�ǰѡ�е�ѧ��
	private static Student selectedsubStu;//��ŵ�ǰ��ʦ����ѧ����Ҫ��Ϣ���б�ѡ�е�ѧ��
	private static boolean isChosenOne = false;//��ʦ����
	private static boolean isChosenOneStu = false;//ѧ������
	private static boolean isChosenOnesubStu = false;//��ʦ����ѧ����Ҫ��Ϣ��
	private static StringBuilder editState = new StringBuilder("δѡ");//���ѧ����¼ʱĬ�ϳ�ʼ״̬Ϊ��δѡ��
	private static String beginState = "δѡ";//���ѡ��ѧ��ʱѧ����ʼ״̬
	private static String changeState = "ѡ��";//��ʦ�����޸�ѧ��״̬
/*************************************************��ʦ����TableView************************************************************/	
	//����TableView�ؼ�������ʾ��ʦ��ѯ���
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
	TableColumn<Instructor, String> passwordCol = new TableColumn<Instructor, String>("��¼����");
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

	TextField tfIstrID = new TextField("");
	TextField tfIstrName = new TextField("");
	TextField tfIstrSex = new TextField("");
	TextField tfIstrRank = new TextField("");
	TextField tfIstrDirection = new TextField("");
	TextField tfIstrPhone = new TextField("");
	TextField tfIstrPassword = new TextField("");
	TextField tfIstrUpperLimit = new TextField("");
	
	Label stuIDInfo = new Label("δѡ��");
	Label stuNameInfo = new Label("δѡ��");
	Label stuStateInfo = new Label("δѡ��");
/**************************************************************************************************************************/
/************************************************ѧ����Ϣ����TabelView*********************************************************/
	//���������ڸ���ѧ����ǰѡ��ʦ״̬��ѯ
	private String[] stuState = new String[4];
	private ComboBox<String> cboStuState = new ComboBox<>();
	//����TableView�ؼ�������ʾѧ����ѯ��Ϣ
	private TableView<Student> mainStuTable = new TableView<Student>();//ѧ��TableView
	private static ObservableList<Student> mainStuData = null;
	TableColumn<Student, String> mainStuIDCol = new TableColumn<Student, String>("ѧ��");//������,�����ݹ���������е���
	TableColumn<Student, String> mainStuNameCol = new TableColumn<Student, String>("����");
	TableColumn<Student, String> mainStuSexCol = new TableColumn<Student, String>("�Ա�");
	TableColumn<Student, String> mainStuMajorCol = new TableColumn<Student, String>("רҵ");
	TableColumn<Student, String> mainStuClassCol = new TableColumn<Student, String>("�༶");
	TableColumn<Student, String> mainStuStateCol = new TableColumn<Student, String>("״̬");
	TableColumn<Student, String> mainStuPhoneCol = new TableColumn<Student, String>("�ֻ�");
	TableColumn<Student, String> mainStuPasswordCol = new TableColumn<Student, String>("����");
	TableColumn<Student, String> mainStuIstrIDCol = new TableColumn<Student, String>("��ʦ����");
	TableColumn<Student, String> mainStuIstrNameCol = new TableColumn<Student, String>("��ʦ����");
	
	//�༭ѧ����Ϣ����
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
/****************************************************���ݿ��������*************************************************************/
	private StringBuilder istrQueryStr = new StringBuilder("select * from instructordata");
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentdata");
	private StringBuilder mainStuQueryStr = new StringBuilder("select * from studentdata");
	private static StringBuilder stuUpdateStr = new StringBuilder("update studentdata");
	private static StringBuilder istrUpdateStr = new StringBuilder("update studentdata");
	private ResultSet result; // ��Ų�ѯ���ĵ�ʦ��Ϣ��¼��
	private ResultSet stuResult; // ��Ų�ѯ����ѧ����Ϣ��¼��
	private int numOfRecords; // ��������ݿ������صļ�¼��
	
	private String idStr = "";//���ڴ���޸�ѧ������ʱ��ѧ����Ϣ����
	private String nameStr = "";
	private String sexStr = "";
	private String majorStr = "";
	private String classStr = "";
	private String phoneStr = "";
	private String passwordStr = "123456";
	private String stateStr = "δѡ";
	private String istrIDStr = "";
	private String istrNameStr = "";
	
	private String istrEditIDStr = "";//���ڴ���޸ĵ�ʦ����ʱ��ѧ����Ϣ����
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
		//��һ�����������ݿ�
		MySQLManager.connect();
		//������̨
		Application.launch(args);
		//���һ���Ͽ����ݿ�����
		MySQLManager.closeConnection();
	}
	Stage stuPrimaryStage;
/**************************************************start����***************************************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type for instructor
		selectionState[0] = "ȫ��";
		selectionState[1] = "ѡ��";
		selectionState[2] = "δѡ��";
		// Set selection type for student
		stuState[0] = "ȫ��";
		stuState[1] = "ѡ��";
		stuState[2] = "����";
		stuState[3] = "δѡ";
		// Set selection type for student
		stuEditState[0] = "δѡ";
		stuEditState[1] = "����";
		stuEditState[2] = "ѡ��";
		// ������ǩҳ���
		tabPane = new TabPane();
		// ����ѧ����ǩ
		Tab studentTab = new Tab("ѧ����Ϣ");
        studentTab.setContent(studentSample());
        // ������ʦ��ǩ
        Tab teacherTab = new Tab("��ʦ��Ϣ");
        teacherTab.setContent(teacherSample());
        // �Ѵ����ı�ǩ��������ǩҳ���
        tabPane.getTabs().addAll(studentTab, teacherTab);
        
        
        
        // �ѱ�ǩҳ�����볡��
		Scene scene = new Scene(tabPane, 1000, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("����Ա����");
		primaryStage.show();
		stuPrimaryStage = primaryStage;
	}

/*****************************************************��ʦ��Ϣ����ҳ�ڵ�**********************************************************/	

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
		
/**************************************************��ʦ��Ϣ����*****************************************************/		
		//������ѡ�б���ѡ�б����¼������ݵ�ʦ״̬��ѯ��ɸѡ��ʦ
		Label labelIstrState = new Label("��ʦ״̬��");
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
		
		//���ݵ�ʦ����������ʦ����
		Label labelSearch = new Label("������ʦ��");
		TextField tfSearch = new TextField();
		tfSearch.setPromptText("���뵼ʦ����...");  
		Button btnSearch = new Button("����");
		Button btnLogout = new Button("�˳�");
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
		
		//��ʦTableView�ؼ�
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
		istrTable.setOnMouseClicked(new MouseClickedListener());// Ϊ��ʦ���ע������¼�
		istrTable.setPrefHeight(285);
		//��ŵ�ʦTableView�͵�ʦ��Ϣ�༭����
		VBox tableViewBox = new VBox();
		//��ʦ��Ϣ�޸�
		GridPane istrEditPane = new GridPane();
		istrEditPane.setHgap(10);
		istrEditPane.setVgap(5);
		Label labelIstrEdit = new Label("��ʦ��Ϣ�޸ģ�");
		labelIstrEdit.setPadding(new Insets(20, 0, 10, 0));
		Label labelIstrID = new Label("����");
		Label labelIstrName = new Label("����");
		Label labelIstrSex = new Label("�Ա�");
		Label labelIstrRank = new Label("ְ��");
		Label labelIstrDirection = new Label("����");
		Label labelIstrPhone = new Label("��ϵ��ʽ");
		Label labelIstrPassword = new Label("����");
		Label labelIstrUpperLimit = new Label("ѧ������");
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
		tfIstrID.setPromptText("���빤��..."); 
		tfIstrName.setPromptText("��������..."); 
		tfIstrSex.setPromptText("�����Ա�..."); 
		tfIstrRank.setPromptText("����ְ��..."); 
		tfIstrDirection.setPromptText("�����о�����..."); 
		tfIstrPhone.setPromptText("������ϵ��ʽ..."); 
		tfIstrPassword.setPromptText("�����¼����..."); 
		tfIstrUpperLimit.setPromptText("ѧ������..."); 
		
		//��ɾ�Ĳ�����ť
		HBox btnHBox = new HBox();
		btnHBox.setAlignment(Pos.CENTER_RIGHT);
		btnHBox.setSpacing(20);
		btnHBox.setPadding(new Insets(20, 0, 0, 0));
		Button btnEdit = new Button("�޸���Ϣ");
		Button btnAdd = new Button("��Ӽ�¼");
		Button btnDelete = new Button("ɾ����ʦ");
		btnHBox.getChildren().addAll(btnEdit, btnAdd, btnDelete);
		//��ʦ��Ϣ�޸ġ���ӡ�ɾ��������¼�
		btnEdit.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�еĵ�ʦ��¼��", null);
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
					Alert alert = new Alert(Alert.AlertType.WARNING, "��ʦ�ؼ���Ϣ�����š��������ֻ��ţ���д��ȫ��", null);
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
					System.out.println("��ǰ��ʦ���ݿ������䣺 " + istrUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//����sql��䣬ˢ�±��
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;//����ѡ����������
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "��ʦ��Ϣ�޸ĳɹ���", null);
							alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "��ʦ��Ϣ�޸�ʧ�ܣ�", null);
						alert.showAndWait();
					}
				}
			}
		});
		
		btnAdd.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�еĵ�ʦ��¼��", null);
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
					Alert alert = new Alert(Alert.AlertType.WARNING, "��ʦ�ؼ���Ϣ�����š��������ֻ��ţ���д��ȫ��", null);
					alert.showAndWait();
				}
				else
				{
					istrUpdateStr.replace(0, istrUpdateStr.length(), 
							"insert into instructordata values(\"" + 
									istrEditIDStr + "\", \"" + istrEditNameStr + "\",\"" + istrSexStr + "\",\"" + istrRankStr + "\",\"" + istrDirectionStr + "\",\"" + istrPhoneStr + "\",\"" + istrPasswordStr + "\", " + 0 + ", " + istrUpperLimitStr + ")");
					System.out.println("��ǰ��ӵ�ʦ��¼���ݿ������䣺 " + istrUpdateStr.toString());
					numOfRecords = MySQLManager.sqlInsert(istrUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//����sql��䣬ˢ�±��
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;//����ѡ����������
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ����Ϣ��ӳɹ���", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "��ʦ��Ϣ���ʧ�ܣ�", null);
						alert.showAndWait();
					}
				}
			}
		});
		btnDelete.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�еĵ�ʦ��¼��", null);
				alert.showAndWait();
			}
			else if(isChosenOne)
			{
				String idStr = tfIstrID.getText().toString().trim();
				istrUpdateStr.replace(0, istrUpdateStr.length(), "delete from instructordata where Istr_id = " + idStr);
				System.out.println("��ǰ���ݿ�ɾ������䣺 " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlDelete(istrUpdateStr.toString());
				if(numOfRecords == 1)
				{
					stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"δѡ\", instructor = \"\", instructor_id = \"\" where instructor_id = \"" + idStr + "\"");
					System.out.println("��ǰ�޸�ѧ�����ݿ������䣺 " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					//����sql��䣬ˢ�±��
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					isChosenOne = false;//����ѡ����������
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "��ʦ��Ϣɾ���ɹ���", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "��ʦ��Ϣɾ��ʧ�ܣ�", null);
					alert.showAndWait();
				}
			}
		});
		tableViewBox.getChildren().addAll(istrTable, labelIstrEdit, istrEditPane, btnHBox);
/**************************************************��ѡ��ʦѧ��ѡ�����******************************************************/			        
		//��ʾ��ѡ��ʦ��ǰ����ѧ����Ҫ��Ϣ��
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
		stuTable.setOnMouseClicked(new subStuClickedListener());// Ϊѧ����Ҫ��Ϣ��ע������¼�
		
		
		//��ʦ�����޸�ѧ��ѡ��ʦ״̬
		VBox editStuStateBox = new VBox();
		editStuStateBox.setPadding(new Insets(0, 0, 0, 20));
		editStuStateBox.setSpacing(10);
		GridPane editStuStatePane = new GridPane();
		editStuStatePane.setHgap(0);
		editStuStatePane.setVgap(10);
		final ToggleGroup group = new ToggleGroup();  
		Label labelEditState = new Label("�޸�ѧ��״̬��");
		Label labelStuID = new Label("ѧ�ţ�");
		Label labelStuName = new Label("������");
		Label labelStuState = new Label("״̬��");
		editStuStatePane.add(labelStuID , 0, 0);
		editStuStatePane.add(labelStuName , 2, 0);
		editStuStatePane.add(labelStuState , 4, 0);
		
		GridPane.setMargin(stuIDInfo, new Insets(0, 20, 0, 0));
		GridPane.setMargin(stuNameInfo, new Insets(0, 20, 0, 0));
		GridPane.setMargin(stuStateInfo, new Insets(0, 20, 0, 0));
		editStuStatePane.add(stuIDInfo , 1, 0);
		editStuStatePane.add(stuNameInfo , 3, 0);
		editStuStatePane.add(stuStateInfo , 5, 0);
		
		//��ť
		GridPane stuBtnPane = new GridPane();
		RadioButton rbSelected = new RadioButton("ѡ��");
		rbSelected.setToggleGroup(group);
		rbSelected.setSelected(true);
		RadioButton rbCancelSelect = new RadioButton("���");
		Button btnChangeState = new Button("ȷ���޸�");
		stuBtnPane.setPadding(new Insets(10, 0, 0, 0));
		stuBtnPane.setHgap(10);
		stuBtnPane.setVgap(10);
		rbCancelSelect.setToggleGroup(group);
		stuBtnPane.add(rbSelected , 0, 0);
		stuBtnPane.add(rbCancelSelect , 1, 0);
		stuBtnPane.add(btnChangeState , 0, 1);
		rbSelected.setOnAction(e -> {
			changeState = "ѡ��";
		});
		rbCancelSelect.setOnAction(e -> {
			changeState = "δѡ";
		});
		btnChangeState.setOnAction(e -> {
			if(!isChosenOnesubStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�е�ѧ����¼��", null);
				alert.showAndWait();
			}
			else
			{
				if(selectedsubStu.getState().equals("ѡ��"))
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ��״̬��ѡ���������޸ģ�", null);
					alert.showAndWait();
				}
				else if(changeState.equals("ѡ��"))
				{
					if(selectedIstr.getUpperLimit() - selectedIstr.getStuNum() > 0)
					{
						stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"ѡ��\" where instructor_id = \"" + selectedIstr.getId() + "\"");
						int finalSutNum = selectedIstr.getStuNum() + 1;
						istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = \""+ finalSutNum + "\" where Istr_id = \"" + selectedIstr.getId() + "\"");
						numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
						if(numOfRecords == 1)
						{
							//���µ�ʦ����
							numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
							System.out.println("��ǰ�޸ĵ�ʦ���ݿ������䣺 " + istrUpdateStr.toString());
							if(numOfRecords == 1)
							{
								System.out.println("��ʦ�����޸ĳɹ���");
							}
							//����sql��䣬ˢ�±��
							stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
							stuCboQuery();
							istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
							IstrCboQuery();
							isChosenOne = false;
							isChosenOnesubStu = false;//����ѡ����������
							Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ��״̬�޸ĳɹ���", null);
							alert.showAndWait();
						}
						else
						{
							Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ��״̬�޸�ʧ��", null);
							alert.showAndWait();
						}
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ��״̬�޸�ʧ��", null);
						alert.showAndWait();
					}
				}
				else if(changeState.equals("δѡ"))
				{
					stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \"δѡ\", instructor = \"\", instructor_id = \"\" where instructor_id = \"" + selectedIstr.getId() + "\"");
					System.out.println("��ǰ�޸�ѧ�����ݿ������䣺 " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//����sql��䣬ˢ�±��
						stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
						stuCboQuery();
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						isChosenOne = false;
						isChosenOnesubStu = false;//����ѡ����������
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ��״̬�޸ĳɹ���", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ��״̬�޸�ʧ��", null);
						alert.showAndWait();
					}
				}
			}
		});
		editStuStateBox.getChildren().addAll(labelEditState, editStuStatePane, stuBtnPane);
        // ѡ��ĳ����ѡ��ʱ���ѡ�е�ֵ
/**************************************************************************************************************/		
		
		//�����в�ѯ��ؿؼ�����HBox�����У�����HBox����Ƕ��vHBox������
		hBox.getChildren().addAll(labelIstrState, cboSelectState, labelSearch, tfSearch, btnSearch, btnLogout);
		HBox.setMargin(labelSearch, new Insets(0, 0, 0, 330));
		bottomHbox.getChildren().addAll(stuTableBox, editStuStateBox);
		
		vBox.getChildren().addAll(hBox, tableViewBox, bottomHbox); 
		return vBox;
	}

/*********************************************** ����ѧ����ǩ�е����ݣ������ظýڵ�*************************************************/
	@SuppressWarnings("unchecked")
	private Node studentSample() 
	{		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(0, 20, 20 ,20));
		// ����ˮƽ����hBox����Ų�ѯѧ����ʽ�����򡢰�ť�����
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 0, 0 ,0));
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		//HBox���֣����ڴ�ŵ�ǰѡ��ѧ����Ӧ�ĵ�ʦ��Ϣ
		HBox bottomHbox = new HBox();
		bottomHbox.setPadding(new Insets(10, 0, 0 ,0));
		bottomHbox.setSpacing(10);
		
		//VBox���֣����ڴ����ʾ��ѯ���ĵ�ʦ��Ϣ
		VBox stuTableBox = new VBox();
		stuTableBox.setSpacing(10);
		stuTableBox.setAlignment(Pos.CENTER_LEFT);
		Label LabelSelectedStu = new Label("��ѧ��ѡ��ĵ�ʦ��");
		stuTableBox.getChildren().add(LabelSelectedStu);
		
/**************************************************��ʦ��Ϣ����*****************************************************/		
		//������ѡ�б���ѡ�б����¼������ݵ�ʦ״̬��ѯ��ɸѡ��ʦ
		Label labelStuState = new Label("ѧ��״̬��");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(stuState);
		cboStuState.getItems().addAll(itemsMode);
		cboStuState.setPrefWidth(125);
		cboStuState.setValue(stuState[0]);
		stuCboQuery();
		cboStuState.setOnAction(e -> {
			if(itemsMode.indexOf(cboStuState.getValue()) == 0)
			{
				System.out.println("������ѡ�е�ֵ�� " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 1)
			{
				System.out.println("������ѡ�е�ֵ�� " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'ѡ��\'");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 2)
			{
				System.out.println("������ѡ�е�ֵ�� " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'����\'");
				stuCboQuery();
			}
			else if(itemsMode.indexOf(cboStuState.getValue()) == 3)
			{
				System.out.println("������ѡ�е�ֵ�� " + cboStuState.getValue());
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where state = \'δѡ\'");
				stuCboQuery();
			}
		});
		
		//���ݵ�ʦ����������ʦ����
		Label labelStuSearch = new Label("����ѧ����");
		TextField tfStuSearch = new TextField();
		tfStuSearch.setPromptText("����ѧ������...");  
		Button btnStuSearch = new Button("����");
		btnStuSearch.setOnAction(e -> {
			String searchName = tfStuSearch.getText().toString();
			if(searchName.length()!= 0)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata where name = \'" + searchName + "\'");
				System.out.println(stuQueryStr.toString());
				stuCboQuery();
			}
		});

		//ѧ��TableView�ؼ�
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
		//mainStuTable.setOnMouseClicked(new MouseClickedListener());// Ϊ��ʦ���ע������¼�
		mainStuTable.setPrefHeight(285);
		mainStuTable.setOnMouseClicked(new stuMouseClickedListener());// Ϊѧ�����ע������¼�
		//���ѧ��TableView�͵�ʦ��Ϣ�༭����
		VBox tableViewBox = new VBox();
		
		//ѧ��������Ϣ�޸�
		GridPane stuInfoPane = new GridPane();
		stuInfoPane.setHgap(10);
		stuInfoPane.setVgap(5);
		Label labelStuEdit = new Label("ѧ��������Ϣ��");
		labelStuEdit.setPadding(new Insets(20, 0, 10, 0));
		Label labelStuID = new Label("ѧ��");
		Label labelStuName = new Label("����");
		Label labelStuSex = new Label("�Ա�");
		Label labelStuMajor = new Label("רҵ");
		Label labelStuClass = new Label("�༶");
		Label labelStuPhone = new Label("��ϵ��ʽ");
		Label labelStuPassword = new Label("����");


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

		
		tfStuID.setPromptText("����ѧ��..."); 
		tfStuName.setPromptText("��������..."); 
		tfStuSex.setPromptText("�����Ա�..."); 
		tfStuMajor.setPromptText("����רҵ..."); 
		tfStuClass.setPromptText("����༶..."); 
		tfStuPhone.setPromptText("�����ֻ���..."); 
		tfStuPassword.setPromptText("�����¼����...");
		tfStuID.setPrefWidth(140);
		tfStuName.setPrefWidth(140);
		tfStuSex.setPrefWidth(100);
		tfStuMajor.setPrefWidth(140);
		tfStuClass.setPrefWidth(140);
		tfStuPhone.setPrefWidth(160);
		tfStuPassword.setPrefWidth(160);
		//ѧ����ʦ��Ϣ�޸�
		GridPane stuIstrInfoPane = new GridPane();
		stuIstrInfoPane.setHgap(10);
		stuIstrInfoPane.setVgap(5);
		Label labelIstrOfStu = new Label("ѧ����ʦ��Ϣ��");
		labelIstrOfStu.setPadding(new Insets(20, 0, 0, 0));
		Label labelEditState = new Label("ѡ��״̬");
		Label labelStuIstrID = new Label("��ʦ����");
		Label labelStuIstrName = new Label("��ʦ����");
		stuIstrInfoPane.add(labelEditState, 0, 0);
		stuIstrInfoPane.add(labelStuIstrID, 1, 0);
		stuIstrInfoPane.add(labelStuIstrName, 2, 0);
		//ѧ��ѡ��״̬ѡ��
		ObservableList<String> stateType = FXCollections.observableArrayList(stuEditState);
		cboEditState.getItems().addAll(stateType);
		cboEditState.setPrefWidth(100);
		cboEditState.setValue(stuEditState[0]);
		cboEditState.setOnAction(e -> {
			if(stateType.indexOf(cboEditState.getValue()) == 0)
			{
				editState.replace(0, editState.length(), "δѡ");
			}
			else if(stateType.indexOf(cboEditState.getValue()) == 1)
			{
				editState.replace(0, editState.length(), "����");
			}
			else if(stateType.indexOf(cboEditState.getValue()) == 2)
			{
				editState.replace(0, editState.length(), "ѡ��");
			}
		});
		stuIstrInfoPane.add(cboEditState, 0, 1);
		stuIstrInfoPane.add(tfStuIstrID, 1, 1);
		stuIstrInfoPane.add(tfStuIstrName, 2, 1);
		tfStuIstrID.setPrefWidth(160);
		tfStuIstrName.setPrefWidth(160);
		tfStuIstrID.setPromptText("���뵼ʦ����..."); 
		tfStuIstrName.setPromptText("���뵼ʦ����..."); 
		
		//��ɾ�Ĳ�����ť
		HBox btnHBox = new HBox();
		btnHBox.setAlignment(Pos.CENTER_LEFT);
		btnHBox.setSpacing(20);
		btnHBox.setPadding(new Insets(20, 0, 0, 0));
		Button btnEdit = new Button("�޸���Ϣ");
		Button btnAdd = new Button("��Ӽ�¼");
		Button btnDelete = new Button("ɾ��ѧ��");
		
		//ѧ����Ϣ�޸İ�ť����¼�
		btnEdit.setOnAction(e->{//�������ݿ�
			if(!isChosenOneStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�е�ѧ����¼��", null);
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

				if(stateStr.equals("δѡ"))
				{
					istrIDStr = "";
					istrNameStr = "";
				}
				else if(tfStuIstrID.getText().toString().trim().equals("") || tfStuIstrID.getText().toString().trim().equals(""))
				{
					stateStr = "δѡ";
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
					System.out.println("��ǰѧ�����ݿ������䣺 " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
					if(numOfRecords == 1)
					{
						//�޸ĵ�ʦ���ݿ�
						int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, stateStr);
						istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
						System.out.println("��ǰ�޸ĵ�ʦ���ݿ������䣺 " + istrUpdateStr.toString());
						numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
						//����sql��䣬ˢ�±��
						istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
						IstrCboQuery();
						stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
						stuCboQuery();
						isChosenOneStu = false;//����ѡ����������
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ����Ϣ�޸ĳɹ���", null);
						alert.showAndWait();
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ����Ϣ�޸�ʧ�ܣ�", null);
						alert.showAndWait();
					}
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "ָ����ʦ������ѡ", null);
					alert.showAndWait();
				}
			}
		});
		btnAdd.setOnAction(e->{//��Ӽ�¼
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
			if(stateStr.equals("δѡ"))
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
				Alert alert = new Alert(Alert.AlertType.WARNING, "ѧ���ؼ���Ϣ��ѧ�š��������ֻ��ţ���д��ȫ��", null);
				alert.showAndWait();
			}
			else if(isUpdatePermitted(beginState, stateStr, availabelnum))
			{
				stuUpdateStr.replace(0, stuUpdateStr.length(), 
						"insert into studentdata values(\"" + 
								idStr + "\", \"" + nameStr + "\",\"" + sexStr + "\",\"" + majorStr + "\",\"" + classStr + "\",\"" + phoneStr + "\",\"" + stateStr + "\",\"" + istrNameStr + "\",\""+ istrIDStr + "\",\"" + passwordStr
						+ "\")");
				System.out.println("��ǰ���ѧ����¼���ݿ������䣺 " + stuUpdateStr.toString());
				numOfRecords = MySQLManager.sqlInsert(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					//�޸ĵ�ʦ���ݿ�
					int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, stateStr);
					istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
					System.out.println("��ǰ�޸ĵ�ʦ���ݿ������䣺 " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					//����sql��䣬ˢ�±��
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					isChosenOneStu = false;//����ѡ����������
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ����Ϣ��ӳɹ���", null);
					alert.showAndWait();
				}
			}
		});
		btnDelete.setOnAction(e->{//ɾ����¼
			if(!isChosenOneStu)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�е�ѧ����¼��", null);
				alert.showAndWait();
			}
			else if(isChosenOneStu)
			{
				String idStr = tfStuID.getText().toString().trim();
				stuUpdateStr.replace(0, stuUpdateStr.length(), "delete from studentdata where id = " + idStr);
				System.out.println("��ǰ���ݿ�ɾ����¼��䣺 " + stuUpdateStr.toString());
				numOfRecords = MySQLManager.sqlDelete(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					int lastStuNum = certainStuNum(istrIDStr) - numOfChangedStuNum(beginState, "δѡ");
					istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + lastStuNum + " where Istr_id = \"" + istrIDStr + "\"");
					System.out.println("��ǰ�޸ĵ�ʦ���ݿ������䣺 " + stuUpdateStr.toString());
					numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
					//����sql��䣬ˢ�±��
					istrQueryStr.replace(0, istrQueryStr.length(), "select * from instructordata");
					IstrCboQuery();
					stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentdata");
					stuCboQuery();
					isChosenOneStu = false;//����ѡ����������
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "ѧ����Ϣɾ���ɹ���", null);
					alert.showAndWait();
				}
			}
		});
		//�������ֵ�����
		btnHBox.getChildren().addAll(btnEdit, btnAdd, btnDelete);
		hBox.getChildren().addAll(labelStuState, cboStuState, labelStuSearch, tfStuSearch, btnStuSearch);
		HBox.setMargin(labelStuSearch, new Insets(0, 0, 0, 330));
		tableViewBox.getChildren().addAll(mainStuTable, labelStuEdit, stuInfoPane, labelIstrOfStu,stuIstrInfoPane, btnHBox);
		vBox.getChildren().addAll(hBox, tableViewBox, bottomHbox);
		return vBox;
	}
	//��ʦ����ѧ����Ҫ��Ϣ�����¼�
	public class subStuClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// �õ��û�ѡ��ļ�¼
			int selectedRow = stuTable.getSelectionModel().getSelectedIndex();
			System.out.println("��ѡ�е�ѧ����¼�� ��" + selectedRow + "��");
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1)
			{
				// ��ȡѡ��ļ�¼
				isChosenOnesubStu = true;//��ǰ�Ѿ�ѡ��һ��ѧ��
				//System.out.println("ѧ���� " + mainStuData);
				student = stuData.get(selectedRow);
				selectedsubStu = stuData.get(selectedRow);
				System.out.println("��ѡ�е�ѧ�����֣� " + selectedsubStu.getName());
				
				stuIDInfo.setText(selectedsubStu.getId());
				stuNameInfo.setText(selectedsubStu.getName());
				stuStateInfo.setText(selectedsubStu.getState());
			}
		}
	}	
	//ѧ����Ϣ�޸Ľ��棬ѧ��tableView����¼�
	public class stuMouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// �õ��û�ѡ��ļ�¼
			int selectedRow = mainStuTable.getSelectionModel().getSelectedIndex();
			System.out.println("��ѡ�м�¼�� ��" + selectedRow + "��");
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1)
			{
				// ��ȡѡ��ļ�¼
				isChosenOneStu = true;//��ǰ�Ѿ�ѡ��һ��ѧ��
				//System.out.println("ѧ���� " + mainStuData);
				student = mainStuData.get(selectedRow);
				selectedStu = mainStuData.get(selectedRow);
				System.out.println("��ѡ�е�ѧ�����֣� " + selectedStu.getName());
				
				tfStuID.setText(selectedStu.getId().toString());
				tfStuName.setText(selectedStu.getName().toString());
				tfStuSex.setText(selectedStu.getSex().toString());
				tfStuMajor.setText(selectedStu.getMajor());
				tfStuClass.setText(selectedStu.getStuClass());
				tfStuPhone.setText(selectedStu.getTelephone().toString());
				tfStuPassword.setText(selectedStu.getPassword().toString());
				if(selectedStu.getState().toString().equals("δѡ"))
				{
					cboEditState.setValue(stuEditState[0]);
					beginState = "δѡ";
					System.out.println("ѡ��ʱ��ʼ״̬�� " + beginState);
				}
				else if(selectedStu.getState().toString().equals("����"))
				{
					cboEditState.setValue(stuEditState[1]);
					beginState = "����";
					System.out.println("ѡ��ʱ��ʼ״̬�� " + beginState);
				}
				else if(selectedStu.getState().toString().equals("ѡ��"))
				{
					cboEditState.setValue(stuEditState[2]);
					beginState = "ѡ��";
					System.out.println("ѡ��ʱ��ʼ״̬�� " + beginState);
				}
				tfStuIstrID.setText(selectedStu.getInstructorID());
				tfStuIstrName.setText(selectedStu.getInstructor());
			}
		}
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
				System.out.println("��ѡ�е�ʦ���֣� " + selectedIstr.getName());
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
				stuIDInfo.setText("δѡ��");
				stuNameInfo.setText("δѡ��");
				stuStateInfo.setText("δѡ��");
				isChosenOnesubStu = false;//��ǰ�Ѿ�ѡ��һ��ѧ��
				
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
	//�ı�ѧ��������ʱ���ݿ��ѯ����
	public void stuCboQuery()
	{
		System.out.println(stuQueryStr.toString());
		mainStuData = FXCollections.observableArrayList();
		result = MySQLManager.sqlQuery(stuQueryStr.toString());
		try 
		{
			while(result.next())
			{
				System.out.print(result.getString(1)+"\t");//ѧ��
				System.out.print(result.getString(2)+"\t");//����
				System.out.print(result.getString(3)+"\t");//�Ա�
				System.out.print(result.getString(4)+"\t");//רҵ
				System.out.print(result.getString(5)+"\t");//�༶
				System.out.print(result.getString(6)+"\t");//�ֻ���
				System.out.print(result.getString(7)+"\t");//״̬
				System.out.print(result.getString(8)+"\t");//��ʦ����
				System.out.print(result.getString(9)+"\t");//��ʦID
				System.out.println(result.getString(10));//����
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
	//�ı䵼ʦ������ʱ���ݿ��ѯ����
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
	//���ݵ�ʦ���ţ���ѯ�õ�ʦʣ����ṩ��ѧ������
	public int availableStuNum(String istrID)
	{
		int num = 0;
		String sql = "select * from instructordata where Istr_id = \"" + istrID + "\"";
		System.out.println("availableStuNum()�������ݿ��ѯ��䣺 " + sql);
		ResultSet sqlresult = MySQLManager.sqlQuery(sql);
		try {
			while(sqlresult.next())
			{
				num = sqlresult.getInt(9) - sqlresult.getInt(8);
				System.out.println("���ޣ� " + sqlresult.getInt(9) + "   ��ѡ����" + sqlresult.getInt(8));
				System.out.println("ʣ��������  " + num);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	//���ݵ�ʦ���ţ���ѯ�õ�ʦ��ѡ����ѧ������
	public int certainStuNum(String istrID)
	{
		int num = 0;
		String sql = "select * from instructordata where Istr_id = \"" + istrID + "\"";
		System.out.println("availableStuNum()�������ݿ��ѯ��䣺 " + sql);
		ResultSet sqlresult = MySQLManager.sqlQuery(sql);
		try {
			while(sqlresult.next())
			{
				num =sqlresult.getInt(8);
				System.out.println("��ѡ�����  " + num);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	//�����޸�ǰѧ��״̬����Ҫ�޸ĳɵ�ѧ��״̬�͵�ǰ��ʦʣ���ѡѧ�������ж��޸ĺͲ�������Ƿ�����
	public boolean isUpdatePermitted(String firstState, String lastState, int availabelNum)
	{
		int first = 0, last = 0, num = 0;
		if(firstState.equals("ѡ��"))
		{
			first = 1;
		}
		else if(firstState.equals("����") || firstState.equals("δѡ"))
		{
			first = 0;
		}
		
		if(lastState.equals("ѡ��"))
		{
			last = 1;
		}
		else if(lastState.equals("����") || lastState.equals("δѡ"))
		{
			last = 0;
		}
		num = first - last;
		
		//����޸ĺ�����һ������
		if(num == 1)
		{
			System.out.println("�޸ĺ����� 1 �����");
			return true;
		}
		//����޸ĺ�û�ж��һ��������ݿ��������� + ���������������Ϊ�ɷ���мӡ����µ��жϽ��
		else
		{
			System.out.println("ԭ���������� " + availabelNum +" �����");
			System.out.println("�޸������� " + num +" �����");
			return availabelNum + num >= 0;
		}
	}
	//ѧ�����ݿ����֮��ʦ��ѡ��ѧ�����仯��
	public int numOfChangedStuNum(String firstState, String lastState)
	{
		int first = 0, last = 0, num = 0;
		if(firstState.equals("ѡ��"))
		{
			first = 1;
		}
		else if(firstState.equals("����") || firstState.equals("δѡ"))
		{
			first = 0;
		}
		
		if(lastState.equals("ѡ��"))
		{
			last = 1;
		}
		else if(lastState.equals("����") || lastState.equals("δѡ"))
		{
			last = 0;
		}
		num = first - last;
		return num;
	}
}

