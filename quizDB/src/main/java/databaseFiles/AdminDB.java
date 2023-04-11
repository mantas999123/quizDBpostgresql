package databaseFiles;

import files.ExamTest;
import files.Question;
import files.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminDB {
    private final QuestionsDB questionsDB = new QuestionsDB();
    private final Scanner sc = new Scanner(System.in);
    private final ConnectDB connect = new ConnectDB();


    public void deleteQuestion() {
        System.out.println("Iveskite klausimo Id kuri norite istrinti: ");
        String id = sc.nextLine().trim();
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            statement.executeQuery("DELETE FROM \"Questions\" WHERE \"Id\" = '" + id + "'");
            statement.close();
            System.out.println("Klausimas istrintas");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteExam() {
        System.out.println("Iveskite Egzamino pavadinima kuri norite istrinti: ");
        String exName = sc.nextLine().trim();
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            statement.executeQuery("DELETE  FROM \"Exam_Name\"  WHERE \"Ex_Name\"= '" + exName + "'");

            statement.close();
            System.out.println("Egzaminas istrintas");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void showAllUsers() {
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT \"UserName\", \"Name\", \"Surname\", \"Role\" FROM \"Users\" WHERE \"Role\"='User'");

            while (resultSet.next()) {
                System.out.printf("Vartotojo UserName: %s Vartotojo Vardas: %s Vartotojo Pavarde: %s%n", resultSet.getString("UserName"), resultSet.getString("Name"), resultSet.getString("Surname"));
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void showAllQuestions() {
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"Questions\"");
            while (resultSet.next()) {

                System.out.printf("Id= %s, ExamId= %s, Question= %s%n", resultSet.getInt("Id"), resultSet.getInt("ExamId"), resultSet.getString("Question"));
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUserByUserName() {
        System.out.println("Iveskite vartotojo UserName kuri norite istrinti: ");
        String userName = sc.nextLine().trim();
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            statement.executeQuery("DELETE FROM \"Users\" WHERE \"UserName\" = '" + userName + "'");

            statement.close();

            System.out.println("Vartotojas istrintas:");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void examName(ExamTest examTest) {
        System.out.println("Iveskite egzamino pavadinima:");
        examTest.setExamName(sc.nextLine());

        System.out.println("Iveskite egzamino Id:");
        examTest.setExamId(Integer.parseInt(sc.nextLine()));
        questionsDB.examName(examTest);

    }

    public void addTest() {

        ExamTest examTest = new ExamTest();
        examName(examTest);

        System.out.println("Kiek klausimu bus egzamine?");
        int testSize = Integer.parseInt(sc.nextLine());
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            System.out.println("Iveskite klausima " + (i + 1));
            Question question = new Question();
            question.setQuestion(sc.nextLine());
            System.out.println(question.getQuestion());
            System.out.println("Nustatykite atsakyma: A");
            question.setOptionA(sc.nextLine());
            System.out.println("Nustatykite atsakyma: B");
            question.setOptionB(sc.nextLine());
            System.out.println("Nustatykite atsakyma: C");
            question.setOptionC(sc.nextLine());
            System.out.println("Nustatykite atsakyma: D");
            question.setOptionD(sc.nextLine());
            System.out.println("Nustatykite teisinga atsakyma");
            question.setCorrectAnswer(sc.nextLine());
            questions.add(question);
            examTest.setQuestions(questions);
            questionsDB.add(question, examTest);
        }
    }

    public void showResultByUserName() {
        System.out.println("Iveskite vartotojo varda, kad butu pateikti rezultatai");
        String res = sc.nextLine();

        String sql = "SELECT \"Id\", \"Exam_Id\", \"U_username\", \"DateAndTime\", assessment\n" +
                "FROM \"User_Final_Result\"\n" +
                "WHERE \"U_username\"='" + res + "'";
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.printf("Egzamino Id %s, vartotojo UserName %s, Data ir laikas %s, ivertinimas %s%n",
                        resultSet.getInt("Exam_Id"), resultSet.getString("U_username"), resultSet.getTimestamp("DateAndTime"),
                        resultSet.getString("assessment"));
            }
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser() {
        System.out.println("Iveskte UserName");
        String userName = sc.nextLine();
        System.out.println("Iveskte varda");
        String name = sc.nextLine();
        System.out.println("Iveskite pavarde");
        String surname = sc.nextLine();
        System.out.println("Iveskite slaptazodi");

        boolean passwordLength;
        String password;
        do {
            password = sc.nextLine();
            passwordLength = passwordLength(password);
        } while (!passwordLength);
        String repeatPassword;
        do {
            System.out.println("Pakartokite slaptazodi");
            repeatPassword = sc.nextLine();
        } while (!repeatPassword.equals(password));
        System.out.println("Iveskite elektronini pasta: ");
        String email = sc.nextLine();
        System.out.printf("Pasirinkite role %s or %s", Role.Admin, Role.User);
        String select = sc.nextLine();

        try {
            Connection connection = connect.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"Users\"(\"UserName\",\"Name\",\"Surname\",\"Password\",\"Email\",\"Role\") VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, select);

            preparedStatement.execute();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean passwordLength(String password) {
        boolean passwordLength = true;
        if (password.length() < 5) {
            passwordLength = false;
            System.out.println("Jusu slaptazodis per trumpas, pasirinkite kita slaptazodi");
        }
        if (password.equals(password.toLowerCase())) {
            passwordLength = false;
            System.out.println("Jusu slaptazodyje yra tik mazosios raides, pasirinkite kita slaptazodi");
        }
        if (password.equals(password.toUpperCase())) {
            passwordLength = false;
            System.out.println("Jusu slaptazodi sudaro tik didziosios raides, pasirinkite kita slaptazodi");
        }
        return passwordLength;
    }

    public void updateQuestions() {
        showAllQuestions();

        System.out.println("Iveskite Klausimo Id kuri norite atnaujinti");
        int qId = Integer.parseInt(sc.nextLine());
        System.out.println("Iveskite nauja klausima kuri norite atnaujinti");
        String question = sc.nextLine();
        System.out.println("Iveskite nauja atsakyma A kuri norite atnaujinti");
        String optionA = sc.nextLine();
        System.out.println("Iveskite nauja atsakyma B kuri norite atnaujinti");
        String optionB = sc.nextLine();
        System.out.println("Iveskite nauja atsakyma C kuri norite atnaujinti");
        String optionC = sc.nextLine();
        System.out.println("Iveskite nauja atsakyma D kuri norite atnaujinti");
        String optionD = sc.nextLine();
        System.out.println("Iveskite nauja teisinga atsakyma kuri norite atnaujinti");
        String correctAnsw = sc.nextLine();

        String sql = "UPDATE \"Questions\" SET \"Question\"=?, \"OptionA\"=?, \"OptionB\"=?, \"OptionC\"=?, \"OptionD\"=?, \"CorrectAnswer\"=? WHERE \"Id\" = ?";

        try {
            Connection connection = connect.connect();
            PreparedStatement update = connection.prepareStatement(sql);
            update.setString(1, question);
            update.setString(2, optionA);
            update.setString(3, optionB);
            update.setString(4, optionC);
            update.setString(5, optionD);
            update.setString(6, correctAnsw);
            update.setInt(7, qId);

            update.executeUpdate();
            update.close();
            System.out.println("Duomenys issaugoti sekmingai");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showAllUsersResults() {

        String sql = "select \"Exam_Id\", \"U_username\", \"DateAndTime\", \"assessment\" FROM \"User_Final_Result\"\n" +
                "where \"assessment\" = \"assessment\"  order  by \"U_username\" asc, \"assessment\" desc ;";

        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.printf("ExamId-%s, UserName-%s, Data ir laikas-%s, ivertininmas-%s%n", resultSet.getInt("Exam_Id"),
                        resultSet.getString("U_username"), resultSet.getDate("DateAndTime"),
                        resultSet.getInt("assessment"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showHowUsers() {

        String sql = "select count(*)  FROM \"Users\"\n" +
                "where \"Role\" = 'User'";
        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            resultSet.next();
            int count = resultSet.getInt(1);
            System.out.println("Is viso vartotoju: " + count);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}




