package databaseFiles;

import loginMain.User;

import java.sql.*;

public class UserDB {
    private final ConnectDB connect = new ConnectDB();


    public void createUser(User user) throws SQLException {
        try {
            Connection connection = connect.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"Users\"(\"UserName\",\"Name\",\"Surname\",\"Password\",\"Email\",\"Role\") VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getRole().name());

            preparedStatement.execute();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showResultToUser(User user) {
        String sql = "SELECT \"Exam_Id\",\"U_username\",\"DateAndTime\",\"assessment\"  FROM \"User_Final_Result\" WHERE \"U_username\"= '" + user.getUserName() + "'";

        try {
            Connection connection = connect.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.printf("Egzamino Id %s, vartotojo UserName %s, Data ir laikas %s, ivertinimas %s%n",
                        resultSet.getInt("Exam_Id"), resultSet.getString("U_username"), resultSet.getTimestamp("DateAndTime"),
                        resultSet.getString("assessment"));
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}