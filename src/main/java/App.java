import java.sql.*;

public class App {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please use program arguments like:");
            System.out.println("user/password@server:port:sid table");
            return;
        }
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        String con_string = "jdbc:oracle:thin:"+args[0];
        String sql = "select * from "+ args[1].replaceAll("[ ;]","");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(con_string);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            System.out.print("\""+md.getColumnLabel(1)+"\"");
            for (int i = 2; i < cols + 1; i++) {
                System.out.print(";"+"\""+md.getColumnLabel(i)+"\"");
            }
            System.out.print("\n");

            while (rs.next()) {
                System.out.print("\""+rs.getString(1)+"\"");
                for (int i = 2; i < cols + 1; i++) {
                    System.out.print(";"+"\""+rs.getString(i)+"\"");
                }
                System.out.print("\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            releaseResourses(rs, ps, con);
        }

    }
    private static void releaseResourses(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        closeResultSet(resultSet);
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }

    private static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
        }
    }

    private static void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
        }
    }

    private static void closeConnection(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
        }
    }
}
