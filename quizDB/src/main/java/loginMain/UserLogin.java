package loginMain;

import databaseFiles.AdminDB;
import databaseFiles.ConnectDB;
import databaseFiles.QuestionsDB;
import databaseFiles.UserDB;
import files.ExamTest;
import files.Role;

import java.sql.*;
import java.util.*;


public class UserLogin {
    UserDB userDB = new UserDB();
    QuestionsDB questionsDB = new QuestionsDB();
    final Map<String, User> userMap = new HashMap<>();
    Scanner sc = new Scanner(System.in);


    void startup() {
        UserLogin userLogin = new UserLogin();
        String choice;
        do {
            userLogin.menu();
            choice = sc.nextLine();
            userLogin.userChoice(sc, choice);
        } while (!choice.equals("3"));
    }

    private void menu() {
        System.out.println(" __________________________________");
        System.out.println("|  1. Registruoti nauja vartotoja  |");
        System.out.println("|  2. Prisijungti                  |");
        System.out.println("|  3. Iseiti is aplikacijos        |");
        System.out.println("|__________________________________|");
    }

    private void userChoice(Scanner sc, String choice) {
        switch (choice) {
            case "1" -> userRegistration(sc);
            case "2" -> userLogin(sc);
            case "3" -> System.out.println("Iseinama is aplikacijos");
            default -> System.out.println("Tokio pasirinkimo nera");
        }
    }

    private void userRegistration(Scanner sc) {
        User user;
        String userName;

        try {
            System.out.println("Iveskite userName");
            userName = sc.nextLine();
            System.out.println("Iveskite varda");
            String name = sc.nextLine();
            System.out.println("Iveskite pavarde");
            String userSurname = sc.nextLine();
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
            user = new User(userName, name, userSurname, password, email, Role.User);
            userMap.put(userName, user);
            userDB.createUser(user);
            System.out.printf("Sveikiname prisiregistravus %s %s%n ", name, userSurname);

        } catch (InputMismatchException e) {
            System.out.println("Neteisinga ivestis");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void userLogin(Scanner sc) {
        User users = new User();
        Connection connection = null;
        ConnectDB connectDB = new ConnectDB(connection);

        try {
            System.out.println("Iveskite userName");
            String userName = sc.nextLine();
            System.out.println("Iveskite slaptazodi");
            String password = sc.nextLine();

            connection = connectDB.connect();
            String sql = ("SELECT \"UserName\", \"Password\" ,\"Role\" FROM \"Users\" WHERE \"UserName\"= ? AND \"Password\"= ? ");
            PreparedStatement user = connection.prepareStatement(sql);

            user.setString(1, userName);
            user.setString(2, password);

            ResultSet userSet = user.executeQuery();

            while (userSet.next()) {
                if (userSet.getString("Role").equals("User")) {
                    System.out.println("Sveikiname prisijungus: " + userSet.getString("UserName"));
                    users.setUserName(userSet.getString("Username"));
                    studentMenu(users, new ExamTest());

                } else if (userSet.getString("Role").equals("Admin")) {
                    System.out.println("Sekmingai prisijungete kaip Administratorius");
                    adminMenu(sc, new ExamTest());
                } else {
                    System.out.println("Neteisingas UserName ar Slaptazodis");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Neteisinga ivestis");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void studentMenu(User user, ExamTest examTest) throws SQLException {
        boolean forward = true;
        while (forward) {
            System.out.println(" __________________________________");
            System.out.println("|  1. Pradeti testa                |");
            System.out.println("|  2. Gauti rezultatus             |");
            System.out.println("|  3. Grizti i pradini meniu       |");
            System.out.println("|__________________________________|");
            String userAction = sc.nextLine();
            switch (userAction) {
                case "1" -> questionsDB.showQuestions(user, examTest);
                case "2" -> userDB.showResultToUser(user);
                case "3" -> forward = false;
                default -> System.out.println("Tokio pasirinkimo nera");
            }
        }
    }

    private void adminMenu(Scanner sc, ExamTest examTest) throws SQLException {
        AdminDB admin = new AdminDB();
        User user = new User();
        boolean forward = true;
        while (forward) {
            System.out.println(" _____________________________________ ");
            System.out.println("|  1. Sukurti testa                   |");
            System.out.println("|  2. Pradeti testa                   |");
            System.out.println("|  3. Gauti rezultata pagal UserName  |");
            System.out.println("|  4. Parodyti visus vartotojus       |");
            System.out.println("|  5. Parodyti visus klausimus        |");
            System.out.println("|  6. Prideti vartotoja arba admina   |");
            System.out.println("|  7. Atnaujinti klausimus            |");
            System.out.println("|  8. Istrinti vartotoja              |");
            System.out.println("|  9. Gauti egzaminu sarasa           |");
            System.out.println("|  10. Istrinti klausima pagal Id     |");
            System.out.println("|  11. Istrinti egzamina              |");
            System.out.println("|  12. Gauti visu vartotoju rezultatus|");
            System.out.println("|  13. Parodyti kiek yra vartotoju    |");
            System.out.println("|  14. Iseiti is aplikacijos          |");
            System.out.println("|_____________________________________|");
            String userAction = sc.nextLine();
            switch (userAction) {
                case "1" -> admin.addTest();
                case "2" -> questionsDB.showQuestions(user, examTest);
                case "3" -> admin.showResultByUserName();
                case "4" -> admin.showAllUsers();
                case "5" -> admin.showAllQuestions();
                case "6" -> admin.addUser();
                case "7" -> admin.updateQuestions();
                case "8" -> admin.deleteUserByUserName();
                case "9" -> questionsDB.getExNameList();
                case "10" -> admin.deleteQuestion();
                case "11" -> admin.deleteExam();
                case "12" -> admin.showAllUsersResults();
                case "13" -> admin.showHowUsers();
                case "14" -> forward = false;
                default -> System.out.println("Tokio pasirinkimo nera");
            }
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
}



