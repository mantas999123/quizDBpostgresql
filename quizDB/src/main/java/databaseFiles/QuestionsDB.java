package databaseFiles;

import files.*;
import loginMain.User;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

import static files.AnswerId.*;

public class QuestionsDB {

    private final ConnectDB connect = new ConnectDB();
    private final List<String> answerSet = new ArrayList<>();

    public void examName(ExamTest examTest) {

        String sql = "INSERT INTO \"Exam_Name\" (\"Id\",\"Ex_Name\") VALUES (?,?)";
        try {
            Connection connection = connect.connect();
            PreparedStatement insert = connection.prepareStatement(sql);
            insert.setInt(1, examTest.getExamId());
            insert.setString(2, examTest.getExamName());

            insert.execute();
            insert.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void add(Question questions, ExamTest examTest) {

        String sql = "INSERT INTO  \"Questions\" (\"ExamId\",\"Question\", \"OptionA\", \"OptionB\", \"OptionC\", \"OptionD\"," +
                " \"CorrectAnswer\") VALUES(?,?,?,?,?,?,?) ";
        try {
            Connection connection = connect.connect();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setInt(1, examTest.getExamId());
            pstm.setString(2, questions.getQuestion());
            pstm.setString(3, questions.getOptionA());
            pstm.setString(4, questions.getOptionB());
            pstm.setString(5, questions.getOptionC());
            pstm.setString(6, questions.getOptionD());
            pstm.setString(7, questions.getCorrectAnswer());

            pstm.executeUpdate();

            System.out.println("Jusu duomenys sekmingai irasyti i DB");
            pstm.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showQuestions(User users, ExamTest examTest) {
        Setq setq = new Setq();

        ArrayList<Question> qlist = getquestions();

        Scanner scan = new Scanner(System.in);
        Collections.shuffle(qlist);// klausimai maisomi is saraso

        System.out.println("-----------------------------------------------");
        int counter = 0;
        Iterator<Question> it = qlist.iterator(); // duomenu skaitymas is saraso po viena

        Question question = null;
        for (int i = 0; i < 10 && it.hasNext(); i++) {
            question = it.next();
//            System.out.println(question.getQnsId());
            System.out.println(question.getQuestion() + "?");

            System.out.println("Pasirinkite atsakyma: ");
            System.out.println(A + "- " + question.getOptionA());
            System.out.println(B + "- " + question.getOptionB());
            System.out.println(C + "- " + question.getOptionC());
            System.out.println(D + "- " + question.getOptionD());
            System.out.println("Iveskite savo pasirinkima: ");

            String userAnswer = "";
            boolean badOption = true;
            while (badOption) {

                userAnswer = scan.nextLine().toUpperCase();
                question.setStudentAnswer(userAnswer);

                switch (userAnswer) {
                    case "A", "B", "C", "D" -> badOption = false;
                    default -> System.out.println("Pasirinkite viena is A B C D variantu");
                }
                saveAnswer(question, users);
            }
            answerSet.add(userAnswer);
            if (question.getCorrectAnswer().equals(userAnswer)) {
                counter++;
            }
        }
        setq.setExamId(examTest.getExamId());
        setq.setQuestion(answerSet);
        setq.setCounter(counter * 10 / answerSet.size());
        if (question != null) {
            writeFinalResult(users, examTest, setq, question);
        }
        showAssessment(setq);
    }


    public ArrayList<Question> getquestions() {
        Scanner scan = new Scanner(System.in);
        ArrayList<Question> arrayList = new ArrayList<>();
        ExamTest examTest = new ExamTest();

            try {
                Connection connection = connect.connect();
                getExNameList();

                System.out.println("Irasykite egzamino varda is nurodytu virsuje ^^^");
                String exName = scan.nextLine().toUpperCase();

                String getQsnSql = "SELECT  a.\"Id\",a.\"Ex_Name\", \"b\".* FROM \"Exam_Name\" AS \"a\" JOIN \"Questions\"" +
                        " AS \"b\" ON a.\"Id\" = b.\"ExamId\"  WHERE \"Ex_Name\"= '" + exName + "'";
                Statement stmt = connection.createStatement();
                ResultSet set = stmt.executeQuery(getQsnSql);

                while (set.next()) {
                    int Id = set.getInt(1);
                    String examName = set.getString(2);
                    int questionId = set.getInt(3);
                    int examId = set.getInt(4);
                    String question = set.getString(5);
                    String optionA = set.getString(6);
                    String optionB = set.getString(7);
                    String optionC = set.getString(8);
                    String optionD = set.getString(9);
                    String answer = set.getString(10);

                    Question addquestion = new Question(questionId, examId, question, optionA, optionB, optionC, optionD, answer);
                    arrayList.add(addquestion);
                    examTest.setExamId(Id);
                    examTest.setExamName(examName);

                }

            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        return arrayList;
    }


    private void showAssessment(Setq setq) {

        int result = setq.getCounter();

        if (result < 5) {
            System.out.println("Jusu rezultatas yra: " + result + " is 10");
            System.out.println("Testas neislaikytas");
        }

        if (result == 5) {
            System.out.println("Jusu rezultatas yra:  " + result + " is 10");
            System.out.println("Testas islaikytas");

        }
        if (result >= 6 && result <= 8) {
            System.out.println("Jusu rezultatas yra: " + result + " is 10");
            System.out.println("Testas islaikytas");

        }
        if (result >= 8 && result <= 10) {
            System.out.println("Jusu rezultatas yra: " + result + " is 10");
            System.out.println("Testas islaikytas");
        }
    }


    public void getExNameList() {
        String list = "Select \"Id\", \"Ex_Name\" FROM \"Exam_Name\"";
        try {
            Connection connection = connect.connect();
            Statement namelist = connection.createStatement();
            ResultSet resultSet = namelist.executeQuery(list);
            while (resultSet.next()) {
                System.out.printf("%s %s %n", resultSet.getInt("Id"), resultSet.getString("Ex_Name"));
            }
            namelist.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveAnswer(Question question, User users) {

        String insertAnswer = "INSERT INTO \"User_Answers\" ( \"Q_id\",\"Answer\",\"C_CorrectAnswer\", \"U_username\") values (?,?,?,?);";
        try (Connection connection = connect.connect(); PreparedStatement insertStatement = connection.prepareStatement(insertAnswer)) {
            insertStatement.setInt(1, question.getQnsId());
            insertStatement.setString(2, question.getStudentAnswer());
            insertStatement.setString(3, question.getCorrectAnswer());
            insertStatement.setString(4, users.getUserName());
            insertStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeFinalResult(User user, ExamTest examTest, Setq setq, Question question) {

        String insertAnswer = "INSERT INTO \"User_Final_Result\" ( \"Exam_Id\",\"U_username\",\"DateAndTime\",\"assessment\") values (?,?,?,?)";
        try (Connection connection = connect.connect(); PreparedStatement insertStatement = connection.prepareStatement(insertAnswer)) {
            insertStatement.setInt(1, question.getExamId());
            insertStatement.setString(2, user.getUserName());
            insertStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
            insertStatement.setInt(4, setq.getCounter());
            insertStatement.execute();
            insertStatement.close();
            System.out.println("Duomenys issaugoti sekmingai");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



