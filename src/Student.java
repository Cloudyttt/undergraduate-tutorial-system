package src;

import javafx.beans.property.SimpleStringProperty;

public class Student {
	private final SimpleStringProperty id = new SimpleStringProperty();  
	private final SimpleStringProperty name = new SimpleStringProperty();  
	private final SimpleStringProperty sex = new SimpleStringProperty();  
	private final SimpleStringProperty stuClass = new SimpleStringProperty();  
	private final SimpleStringProperty major = new SimpleStringProperty();  
	private final SimpleStringProperty telephone = new SimpleStringProperty();  
	private final SimpleStringProperty state = new SimpleStringProperty();  
	private final SimpleStringProperty instructor = new SimpleStringProperty();
	private final SimpleStringProperty instructorID = new SimpleStringProperty();
	private final SimpleStringProperty password = new  SimpleStringProperty();
	
	public Student(String id, String name, String sex, String major, String stuClass, 
			String telephone, String state, String instructor, String instructorID, String password)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setStuClass(stuClass);
		setMajor(major);
		setTelephone(telephone);
		setState(state);
		setInstructor(instructor);
		setInstructorID(instructorID);
		setPassword(password);
	}

	public Student(String id, String name, String sex, String major, String stuClass, 
			String telephone, String state, String instructor)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setStuClass(stuClass);
		setMajor(major);
		setTelephone(telephone);
		setState(state);
		setInstructor(instructor);
	}
	
	public Student(String id, String name, String sex, String major, String stuClass, String state)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setStuClass(stuClass);
		setMajor(major);
		setState(state);
	}
	
	public Student(String id, String name, String sex, String major, String stuClass, String telephone, String state)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setStuClass(stuClass);
		setMajor(major);
		setTelephone(telephone);
		setState(state);
	}
	//getter SimpleStringProperty
	public SimpleStringProperty idProperty() {
		return id;
	}
	public SimpleStringProperty nameProperty() {
		return name;
	}
	public SimpleStringProperty sexProperty() {
		return sex;
	}
	public SimpleStringProperty stuClassProperty() {
		return stuClass;
	}
	public SimpleStringProperty majorProperty() {
		return major;
	}
	public SimpleStringProperty telephoneProperty() {
		return telephone;
	}
	public SimpleStringProperty stateProperty() {
		return state;
	}
	public SimpleStringProperty instructorProperty() {
		return instructor;
	} 
	public SimpleStringProperty instructorIDProperty() {
		return instructorID;
	}
	public SimpleStringProperty passwordProperty() {
		return password;
	} 
	
	//getter String
	public String getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getSex() {
		return sex.get();
	}
	public String getStuClass() {
		return stuClass.get();
	}
	public String getMajor() {
		return major.get();
	}
	public String getTelephone() {
		return telephone.get();
	}
	public String getState() {
		return state.get();
	}
	public String getInstructor() {
		return instructor.get();
	} 
	public String getInstructorID() {
		return instructorID.get();
	}
	public String getPassword() {
		return password.get();
	} 
	
	//setter
    public void setId(String id) {  
        this.id.set(id);  
    } 
    public void setName(String name) {  
        this.name.set(name);  
    } 
    public void setSex(String sex) {  
        this.sex.set(sex);  
    } 
    public void setMajor(String major) {  
        this.major.set(major);  
    } 
    public void setStuClass(String stuClass) {  
        this.stuClass.set(stuClass);  
    } 
    public void setTelephone(String telephone) {  
        this.telephone.set(telephone);  
    } 
    public void setState(String state) {  
        this.state.set(state);  
    } 
    public void setInstructor(String instructor) {  
        this.instructor.set(instructor);  
    } 
    
    public void setInstructorID(String instructorID) {  
        this.instructorID.set(instructorID);  
    } 
    public void setPassword(String password) {  
        this.password.set(password);  
    } 
}
