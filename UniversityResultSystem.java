import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Student {

    private int rollNo;
    private String name;
    private List<Integer> marks;
    private double percentage;
    private String grade;
    private double cgpa;

    public Student(int rollNo, String name, List<Integer> marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
        calculateResult();
    }

    private void calculateResult() {

        int total = marks.stream().mapToInt(Integer::intValue).sum();
        percentage = total / (double) marks.size();
        cgpa = percentage / 9.5;

        if (percentage >= 90) grade = "A+ (Distinction)";
        else if (percentage >= 75) grade = "A (First Class)";
        else if (percentage >= 60) grade = "B (Second Class)";
        else if (percentage >= 50) grade = "C (Pass)";
        else grade = "F (Fail)";
    }

    public double getPercentage() {
        return percentage;
    }

    public String toString() {
        return "Roll No: " + rollNo +
                " | Name: " + name +
                " | Percentage: " + String.format("%.2f", percentage) +
                "% | CGPA: " + String.format("%.2f", cgpa) +
                " | Grade: " + grade;
    }
}

public class UniversityResultSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<Student> students = new ArrayList<>();

        // ✅ Java Time API - Today's Date
        LocalDate examDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println("Exam Date: " + examDate.format(formatter));
        System.out.println("--------------------------------------");

        try {
            System.out.print("Enter number of students: ");
            int n = sc.nextInt();

            System.out.print("Enter number of subjects: ");
            int subjects = sc.nextInt();

            for (int i = 0; i < n; i++) {

                System.out.println("\nEnter details for Student " + (i + 1));

                System.out.print("Roll No: ");
                int roll = sc.nextInt();
                sc.nextLine();

                System.out.print("Name: ");
                String name = sc.nextLine();

                List<Integer> marks = new ArrayList<>();

                for (int j = 1; j <= subjects; j++) {
                    System.out.print("Enter marks for Subject " + j + " (0-100): ");
                    int mark = sc.nextInt();

                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException("Marks must be between 0 and 100!");
                    }

                    marks.add(mark);
                }

                students.add(new Student(roll, name, marks));
            }

            System.out.println("\n----- Merit List (Sorted by Percentage) -----");

            List<Student> sortedList = students.stream()
                    .sorted(Comparator.comparingDouble(Student::getPercentage).reversed())
                    .collect(Collectors.toList());

            int rank = 1;
            for (Student s : sortedList) {
                System.out.println("Rank " + rank++ + " → " + s);
            }

            // ✅ Topper List using Streams
            double highest = students.stream()
                    .mapToDouble(Student::getPercentage)
                    .max()
                    .orElse(0);

            List<Student> toppers = students.stream()
                    .filter(s -> s.getPercentage() == highest)
                    .collect(Collectors.toList());

            System.out.println("\n----- Topper(s) -----");
            toppers.forEach(System.out::println);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input! Please enter numeric values only.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}