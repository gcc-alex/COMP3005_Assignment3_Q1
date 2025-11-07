package src;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentDBApp {

    private final String url = "jdbc:postgresql://localhost:5432/assignment3";
    private final String user = "postgres";
    private final String password = "admin";

    // Retrieves and displays all records from the students table.
    public void getAllStudents() {
        String SQL = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nStudent Records:");
            System.out.println("-------------------------------");
            System.out.println("Student ID | Student Name | Student Email | Date of Enrollment");
            System.out.println("");

            while (rs.next()) {
                int student_id = rs.getInt("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                java.sql.Date enrollment_date = rs.getDate("enrollment_date");

                System.out.printf("%d | %s %s | %s | %s%n", student_id, first_name, last_name, email, enrollment_date);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Inserts a new student record into the students table.    
    public void addStudent(String first_name, String last_name, String email, Date enrollment_date) {
        String SQL = "INSERT INTO students(first_name,last_name,email,enrollment_date) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, new java.sql.Date(enrollment_date.getTime()));
            pstmt.executeUpdate();
            System.out.println("\nStudent added successfully!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // Updates the email address for a student with the specified student_id.
    public void updateStudentEmail(int student_id, String new_email) {
        String SQL = "UPDATE students SET email=? WHERE student_id=?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            pstmt.executeUpdate();
            System.out.println("\nStudent email updated!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Deletes the record of the student with the specified student_id.
    public void deleteStudent(int student_id) {
        String SQL = "DELETE FROM students WHERE student_id=?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, student_id);
            pstmt.executeUpdate();
            System.out.println("\nStudent deleted!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Main function
    public static void main(String[] args) {
        StudentDBApp dbApp = new StudentDBApp();
        Scanner scanner = new Scanner(System.in);
      
        int input = -1;
        int student_id;
        String first_name, last_name, email, enrollment_date;

        // Menu
        while (input != 0) {
            try {
                System.out.println("\nStudent Database Application:");
                System.out.println("----------------------------------");
                System.out.println("1 - Display Students");
                System.out.println("2 - Add Student");
                System.out.println("3 - Update Email");
                System.out.println("4 - Delete Student");
                System.out.println("0 - Exit");

                System.out.print("Please select an option: ");
                
                input = scanner.nextInt();
                scanner.nextLine();

                try {
                    switch(input) {
                        case 1: // Display Students
                            dbApp.getAllStudents();
                            break;

                        case 2: // Add Student
                            System.out.println("What is the student's first name? ");
                            first_name = scanner.nextLine();
                            System.out.println("What is the student's last name? ");
                            last_name = scanner.nextLine();
                            System.out.println("What is the student's email? ");
                            email = scanner.nextLine();
                            System.out.println("When did the student enroll? (YYYY-MM-DD) ");
                            enrollment_date = scanner.nextLine();
                            dbApp.addStudent(first_name, last_name, email, java.sql.Date.valueOf(enrollment_date));
                            break;

                        case 3: // Update Email
                            System.out.println("Please enter the ID of the student whose email will be updated: ");
                            student_id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Please enter the updated email: ");
                            email = scanner.nextLine();
                            dbApp.updateStudentEmail(student_id, email);
                            break;

                        case 4: // Delete Student
                            System.out.println("Please enter the ID of the student whose email will be deleted: ");
                            student_id = scanner.nextInt();
                            dbApp.deleteStudent(student_id);
                            break;

                        case 0: // Exit
                            System.out.println("Application closed successfully.");
                            System.exit(0);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("\nInvalid enrollment date. Please use YYYY-MM-DD format.");
                }

                if (input > 4 || input < 0) {
                    System.out.println("\nInvalid input. Please choose an option from 0-4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please choose an option from 0-4.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}