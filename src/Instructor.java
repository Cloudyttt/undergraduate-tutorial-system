package src;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Instructor 
{
	private final SimpleStringProperty id = new SimpleStringProperty();  
	private final SimpleStringProperty name = new SimpleStringProperty();  
	private final SimpleStringProperty sex = new SimpleStringProperty();  
	private final SimpleStringProperty rank = new SimpleStringProperty();  
	private final SimpleStringProperty direction = new SimpleStringProperty();  
	private final SimpleStringProperty telephone = new SimpleStringProperty();  
	private final SimpleStringProperty password = new  SimpleStringProperty(); 
	private final SimpleIntegerProperty stuNum = new  SimpleIntegerProperty();
	private final SimpleIntegerProperty upperLimit = new SimpleIntegerProperty(); 	
	
	
	public Instructor(String id, String name, String sex, String rank, 
			String direction, String telephone, String password, Integer stuNum, Integer upperLimit)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setRank(rank);
		setDirection( direction);
		setTelephone(telephone);
		setPassword(password);
		setUpperLimit(upperLimit);
		setStuNum(stuNum);
	}
	public Instructor(String id, String name, String sex, String rank, 
			String direction, String telephone, Integer stuNum, Integer upperLimit)
	{
		setId(id);
		setName(name);
		setSex(sex);
		setRank(rank);
		setDirection( direction);
		setTelephone(telephone);
		setUpperLimit(upperLimit);
		setStuNum(stuNum);
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
	public SimpleStringProperty stuRankProperty() {
		return rank;
	}
	public SimpleStringProperty  directionProperty() {
		return  direction;
	}
	public SimpleStringProperty telephoneProperty() {
		return telephone;
	}
	public SimpleIntegerProperty upperLimitProperty() {
		return upperLimit;
	}
	public SimpleStringProperty passwordProperty() {
		return password;
	} 
	public SimpleIntegerProperty stuNumProperty() {
		return stuNum;
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
	public String getRank() {
		return rank.get();
	}
	public String getDirection() {
		return  direction.get();
	}
	public String getTelephone() {
		return telephone.get();
	}
	public Integer getUpperLimit() {
		return upperLimit.get();
	}
	public String getPassword() {
		return password.get();
	} 
	public Integer getStuNum() {
		return stuNum.get();
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
    public void setDirection(String direction) {  
        this.direction.set(direction);  
    } 
    public void setRank(String rank) {  
        this.rank.set(rank);  
    } 
    public void setTelephone(String telephone) {  
        this.telephone.set(telephone);  
    } 
    public void setUpperLimit(Integer upperLimit) {  
        this.upperLimit.set(upperLimit);  
    } 
    public void setPassword(String password) {  
        this.password.set(password);  
    } 
    public void setStuNum(Integer stuNum) {  
        this.stuNum.set(stuNum);  
    }
}
