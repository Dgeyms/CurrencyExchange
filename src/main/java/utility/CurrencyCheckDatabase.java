package utility;
/*
* Проверка валюты в нахождении в базе данных
 */

import java.sql.*;

public class CurrencyCheckDatabase {
    public static boolean checkPresenceCurrency(String codeCurrency){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resSet = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.urlRelativePath);

            preparedStatement = connection.prepareStatement("SELECT Code FROM Currencies WHERE Code = ?");
            preparedStatement.setString(1, codeCurrency);
            resSet = preparedStatement.executeQuery();

            if (resSet.next()) {
               String currency = resSet.getString("Code");
                if(currency.equals(codeCurrency)){
                    return true;
                }
            }

        }catch(ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL!");
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (resSet != null) {
                    resSet.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while closing resources: " + e.getMessage());
            }
        }
        return false;
    }
}
