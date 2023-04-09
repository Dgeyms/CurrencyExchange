package utility;

import params.ParamsCurrency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectParamsCurrency {
    public ParamsCurrency selectCurrencyParams(int getIdCurrency) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resSet = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(UrlDatabase.urlRelativePath);
            System.out.println("Connect YES");

            String sql = "SELECT * FROM Currencies WHERE ID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getIdCurrency);

            resSet = preparedStatement.executeQuery();
            int id = resSet.getInt(1);
            String code = resSet.getString(2);
            String fullName = resSet.getString(3);
            String sing = resSet.getString(4);

            return new ParamsCurrency(id, code, fullName, sing);
        } catch (Exception e) {
            System.out.println("Connect No");
        } finally {
            try {
                if (resSet != null) {
                    resSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing resources: " + e.getMessage());
            }

        }
        return null;
    }
}
