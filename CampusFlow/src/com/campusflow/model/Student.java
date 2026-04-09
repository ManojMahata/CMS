import java.time.LocalDate;

/*
sutdent entity class
represent a student in the college management system
maps to 'students' table in database
*/

public class Student{
// private attributes (encapsulation)
	private String rollNumber;
	private String name;
	private String faculty;
	private String course;
	private int semester;
	private String section;
	private String email;
	private String phone;
	private String feeStatus;
	private LocalDate dateOfAdmission;

	// constructor - full (will be used while loading from database)
	public Student(String rollNumber, String name, String faculty, String course, int semester, String section, String email, String phone, String feeStatus, LocalDate dateOfAdmission){
		this.rollNumber = rollNumber;
		this.name = name;
		this.faculty = faculty;
		this.course = course;
		this.semester = semester;
		this.section = section;
		this.email = email;
		this.phone = phone;
		this.feeStatus = feeStatus;
		this.dateOfAdmission = dateOfAdmission;
	} // full constructor ends here

	// constructor - minimal (will be used while creating a new student)
	public Student(String rollNumber, String name, String course, int semester){
		this.rollNumber = rollNumber;
		this.name = name;
		this.course = course;
		this.semester = semester;
		this.feeStatus = "Pending"; // default value
		this.dateOfAdmission = LocalDate.now();
	}

	// getters (to read teh data)
	public String getRollNumber(){
		return rollNumber;
	}

	public String getName(){
		return name;
	}

	public String getFacutly(){
		return faculty;
	}

	public String getCourse(){
		return course;
	}

	public int getSemester(){
		return semester;
	}

	public String getSection(){
		return section;
	}

	public String getEmail(){
		return email;
	}

	public String getPhone(){
		return phone;
	}

	public String getFeeStatus(){
		return feeStatus;
	}

	public LocalDate getDateOfAdmision(){
		return dateOfAdmission;
	}
	// getters finished

	// setter (to modify data)
	public void setName(String name){
		this.name = name;
	}

	public void setFaculty(String faculty){
		this.faculty = faculty;
	}

	public void setSemester(int semester){
		this.semester = semester;
	}

	public void setSection(String section){
		this.section = section;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public void setPhoen(String phone){
		this.phone = phone;
	}

	public void setFeeStatus(String feeStatus){
		this.feeStatus = feeStatus;
	}

	// toString() - for debugging (print stadent details)
	@Override
	public String toString(){
		return "Student{" +
				"rollNumber='" + rollNumber + '\'' +
				", name= '" + name + '\'' +
				", course='" + course + '\'' +
				", semester='" + semester + '\'' +
				", feeStatus='" + feeStatus + '\'' +
				'}';
	}

}// Student class ends here
