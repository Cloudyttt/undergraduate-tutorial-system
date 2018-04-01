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
/***************************************�����Ͷ���*****************************************/
	private Instructor currIstr = Login.istrUser;//��ǰ��¼�ĵ�ʦ�û�
	private Instructor instructor;
	private Student student;
	private static Student selectedStu;
	
	//����TableView�ؼ�������ʾ��ʦ��ѯ���
	Button btnQuery = new Button("��ѯ");
	private String[] selectionState = new String[2];
	private ComboBox<String> cboSelectState = new ComboBox<>();
	///����TableView�ؼ�������ʾѧ����Ϣ
	private TableView<Student> certainStuTable = new TableView<Student>();//ѧ��TableView
	private static ObservableList<Student> certainStuData = null;
	TableColumn<Student, String> stuIdCol = new TableColumn<Student, String>("ѧ��");//������,�����ݹ���������е���
	TableColumn<Student, String> stuNameCol = new TableColumn<Student, String>("����");
	TableColumn<Student, String> stuSexCol = new TableColumn<Student, String>("�Ա�");
	TableColumn<Student, String> stuMajorCol = new TableColumn<Student, String>("רҵ");
	TableColumn<Student, String> stuClassCol = new TableColumn<Student, String>("�༶");
	TableColumn<Student, String> stuTelephoneCol = new TableColumn<Student, String>("��ϵ��ʽ");
	TableColumn<Student, String> stuStateCol = new TableColumn<Student, String>("״̬");
	
	//��ʾ��ѡ�е�ѧ����Ϣ
	Label certainStuID = new Label("δѡ��"); 
	Label certainStuName = new Label("δѡ��"); 
	Label certainStuSex = new Label("δѡ��"); 
	Label certainStuMajor = new Label("δѡ��"); 
	Label certainStuClass = new Label("δѡ��");
	Label certainStuPhone = new Label("δѡ��"); 
	Label certainStuState = new Label("δѡ��"); 
	
	//���ݿ���ر�������
	private StringBuilder stuQueryStr = new StringBuilder("select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'ѡ��\'");
	private static StringBuilder stuUpdateStr = new StringBuilder("update studentdata");
	private static StringBuilder istrUpdateStr = new StringBuilder("update studentdata");
	private ResultSet result; // ��Ų�ѯ���ĵ�ʦ��Ϣ��¼��
	private ResultSet stuResult; // ��Ų�ѯ����ѧ����Ϣ��¼��
	private static int numOfRecords; // ��������ݿ������صļ�¼��
	
	//����ǰ��ʦ�û�ѡ�е�ѧ����Ϣ
	private boolean isChosenOne = false;//�жϵ�ǰ��ʦ�û��Ƿ���ѡ����һ��ѧ��
	private TabPane tabPane;
	
	public static void main(String[] args) 
	{
		//��һ�����������ݿ�
		MySQLManager.connect();
		Application.launch(args);
		//���һ���Ͽ����ݿ�����
		MySQLManager.closeConnection();
	}
	Stage istrPrimaryStage;
/*************************************start����*******************************************/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		// Set selection type
		selectionState[0] = "ѡ��";
		selectionState[1] = "����";
		// ������ǩҳ���
		tabPane = new TabPane();
		// ������ʦ��ҳ��ǩ
		Tab studentTab = new Tab("��ʦ��Ϣ");
        studentTab.setContent(InstrSample());
        // ������ʦ��Ϣ�޸ı�ǩ
        Tab modifyTab = new Tab("��Ϣ�޸�");
        modifyTab.setContent(modifyPasswordSample());
        // �Ѵ����ı�ǩ��������ǩҳ���
        tabPane.getTabs().addAll(studentTab, modifyTab);
        
        
        
        // �ѱ�ǩҳ�����볡��
		Scene scene = new Scene(tabPane, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("��ʦ����");
		primaryStage.show();
		istrPrimaryStage = primaryStage;
	}

	
/*********************************************************��غ���**********************************************************/	
/*********************************************** ������ʦ��ҳ��ǩ�е����ݣ������ظýڵ�********************************************/
	@SuppressWarnings("unchecked")
	private Node InstrSample() 
	{
		BorderPane borderPane = new BorderPane();
		System.out.println("��ǰ��¼�û������� " + currIstr.getName());
		
		Label basicInfoLabel = new Label("������Ϣ");
		basicInfoLabel.setStyle("-fx-font-size:18;");
		Label istrInfoLabel = new Label("ѧ��ѡ�����");
		istrInfoLabel.setStyle("-fx-font-size:18;");
		Button btnLogout = new Button("�˳�");
		btnLogout.setPrefWidth(60);
		HBox.setMargin(btnLogout, new Insets(0,0,0,485));
		btnLogout.setOnAction(e -> //�˳�����¼���水ť����¼�
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
		//��������
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
		
		//��ർʦ������Ϣ
		HBox centerHBox = new HBox();
		centerHBox.setAlignment(Pos.CENTER);
		
		GridPane leftGridPane = new GridPane();
		leftGridPane.setPadding(new Insets(20, 0, 20, 20));
		leftGridPane.setHgap(20);
		leftGridPane.setVgap(20);
		leftGridPane.setPrefWidth(300);
		Label accountLabel = new Label("����: ");
		Label nameLabel = new Label("����: ");
		Label sexLabel = new Label("�Ա�: ");
		Label rankLabel = new Label("ְ��: ");
		Label directionLabel = new Label("�о�����: ");
		Label telephoneLabel = new Label("��ϵ��ʽ: ");
		Label stuNumLabel = new Label("ѡ��ѧ����: ");
		Label upperLimitLabel = new Label("ѧ��������: ");
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
/****************************************************�Ҳർʦѡ��ѧ�����********************************************************/
		VBox rightBox = new VBox();//�Ҳ�������
		rightBox.setPadding(new Insets(20, 20, 40, 20));
		rightBox.setSpacing(20);
//		rightBox.setPrefWidth(500);
		
		//��ʾ��ѡ��ʦ��ǰ����ѧ����Ϣ
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
		certainStuTable.setOnMouseClicked(new MouseClickedListener());// Ϊ��ʦ���ע������¼�
		
		
		//�ɸ���ѧ����ǰ״̬�������ѧ����Ϣ��������
		Label labelStuState = new Label("ѧ��״̬��");
		ObservableList<String> itemsMode = FXCollections.observableArrayList(selectionState);
		cboSelectState.getItems().addAll(itemsMode);
		cboSelectState.setPrefWidth(125);
		cboSelectState.setValue(selectionState[0]);
		cboQuery();
		cboSelectState.setOnAction(e -> {
			if(itemsMode.indexOf(cboSelectState.getValue()) == 0)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'ѡ��\'");
				cboQuery();
			}
			else if(itemsMode.indexOf(cboSelectState.getValue()) == 1)
			{
				stuQueryStr.replace(0, stuQueryStr.length(), "select * from studentData where instructor_id = \'" + currIstr.getId() + "\' and state=\'����\'");
				cboQuery();
			}
		});
//		
//		Button btnQuery = new Button("��ѯ");
//		btnQuery.setOnAction(e -> {//�����ѯѡ�е�ǰ��¼��ʦ�û���Ϊ��ʦ��ѧ����Ϣ
//
//		});
		//�������Ӧ��HBox����
		HBox hBoxCbo = new HBox();
		hBoxCbo.setSpacing(20);
		hBoxCbo.getChildren().addAll(labelStuState, cboSelectState);	
		//��ѡ�е�ѧ����Ϣ������һ��GridPane������
		GridPane certainPane = new GridPane();
		certainPane.setHgap(5);
		certainPane.setVgap(10);
		Label lbCertainStuID = new Label("ѧ��ѧ�ţ� "); 
		Label lbCertainStuName = new Label("ѧ�������� "); 
		Label lbCertainStuSex = new Label("ѧ���Ա� "); 
		Label lbCertainStuMajor = new Label("ѧ��רҵ�� "); 
		Label lbCertainStuClass = new Label("ѧ���༶�� ");
		Label lbCertainStuPhone = new Label("��ϵ��ʽ�� "); 
		Label lbCertainStuState = new Label("ѡ��״̬�� "); 
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
		
		Button btnCertain = new Button("ȷ��ѡ��");
		GridPane.setMargin(btnCertain, new Insets(0, 5, 0, 30));
		Button btnCancel = new Button("ȡ��ѡ��");
		btnCertain.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
		btnCancel.setStyle("-fx-font-weight: bold; -fx-font-size:16;");
		certainPane.add(btnCertain, 4, 2);
		certainPane.add(btnCancel, 5, 2);
		//ȷ��ѧ��ѡ��ť����¼�
		btnCertain.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�е�ѧ����", null);
				alert.showAndWait();
			}
			else if(selectedStu.getState().equals("ѡ��"))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰѡ��ѧ����ȷ�ϣ������ظ�������", null);
				alert.showAndWait();
			}
			else if(currIstr.getUpperLimit() - currIstr.getStuNum() == 0)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "���Ŀɴ�ѧ�����Ѵ����ޣ�", null);
				alert.showAndWait();
			}
			else
			{
				int twoRecord = 0;//�����ж��Ƿ�����������ݿ���ɹ�
				stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \'ѡ��\' where id = \'" + selectedStu.getId() + "\'");
				System.out.println("��ǰstudentdata���ݿ���µ���䣺 " + stuUpdateStr.toString());
				istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set stuNum = " + (currIstr.getStuNum() + 1) + " where Istr_id = \'" + currIstr.getId() + "\'");
				System.out.println("��ǰinstructordata���ݿ���µ���䣺 " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				twoRecord = twoRecord + numOfRecords;
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				twoRecord = twoRecord + numOfRecords;
				if(twoRecord == 2)
				{
					currIstr.setStuNum(currIstr.getStuNum() + 1);
					istrStuNumLabel.setText(currIstr.getStuNum().toString());
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "ȷ��ѡ��ɹ���", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "ȷ��ѡ��ʧ�ܣ�", null);
					alert.showAndWait();
				}
			}
		});
		//ȡ��ѧ��ѡ�����¼�
		btnCancel.setOnAction(e -> {
			if(!isChosenOne)
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰû��ѡ�е�ѧ����", null);
				alert.showAndWait();
			}
			else if(selectedStu.getState().equals("ѡ��"))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "��ǰѡ��ѧ����ȷ�ϣ�����ȡ��ѡ��", null);
				alert.showAndWait();
			}
			else
			{
				stuUpdateStr.replace(0, stuUpdateStr.length(), "update studentdata set state = \'δѡ\', instructor_id = \'\', instructor = \'\' where id = " + selectedStu.getId());
				numOfRecords = MySQLManager.sqlUpdate(stuUpdateStr.toString());
				if(numOfRecords == 1)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "ȡ��ѡ��ɹ���", null);
					alert.showAndWait();
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.WARNING, "ȡ��ѡ��ʧ�ܣ�", null);
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
			if(!currIstr.getPassword().trim().equals(oldPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�������������!", null);
				alert.showAndWait();
			}
			else if(oldPassword.getText().toString().trim().equals(newPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�����벻�����������ͬ!", null);
				alert.showAndWait();
			}
			else if(!newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING, "�������������벻һ�£�", null);
				alert.showAndWait();
			}
			else
			{
				istrUpdateStr.replace(0, istrUpdateStr.length(), "update instructordata set Istr_password = \'" + newPassword.getText().toString().trim() + "\' where Istr_id = " + currIstr.getId());
				System.out.println("��ǰ�޸��������ݿ������䣺 " + istrUpdateStr.toString());
				numOfRecords = MySQLManager.sqlUpdate(istrUpdateStr.toString());
				if(numOfRecords > 0)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "�����޸ĳɹ���", null);
					alert.showAndWait();
					//�޸�����ɹ�����Ҫ���µ�¼���Զ���ת����¼����
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
/***********************************************TableView����¼�**********************************************************/
	//��ʦTablview����¼�
	public class MouseClickedListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent arg0) 
		{
			// �õ���ʦѡ��ļ�¼
			int selectedRow = certainStuTable.getSelectionModel().getSelectedIndex();
			// ���ȷʵѡȡ��ĳ����¼
			if(selectedRow!=-1)
			{
				isChosenOne = true;//��ǰѧ���û��Ѿ�ѡ��һ����ʦ
				// ��ȡѡ��ļ�¼
				selectedStu = certainStuData.get(selectedRow);
				certainStuID.setText(selectedStu.getId());
				certainStuName.setText(selectedStu.getName());
				certainStuSex.setText(selectedStu.getSex());
				certainStuMajor.setText(selectedStu.getMajor());
				certainStuClass.setText(selectedStu.getStuClass());
				certainStuPhone.setText(selectedStu.getTelephone());
				certainStuState.setText(selectedStu.getState());
				System.out.println("��ѡ��ѧ�����֣� " + selectedStu.getName());
			}
		}
	}	
	
	//������ѡ���ѯ
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
