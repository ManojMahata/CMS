import java.time.LocalDate;

public class TestStudent {
	public static void main(String[] args){
		// create a student object to call Student consturctor
		Student s1 = new Student("BCA001", "Manoj Mahata", "Humanities and Social Scinece", "BCA", 5, "A", "manojmahata10000@gmail.com", "9806421179", "Pending", LocalDate.of(2026,05,9));

		// lets print the details of the student
		System.out.println(s1);
		System.out.println("Student Name: " + s1.getName());
		System.out.println("Roll Number: " + s1.getRollNumber());

		// test setter
		s1.setFeeStatus("Paid");
		System.out.println("Updated Fee Status: " + s1.getFeeStatus());

	}
}