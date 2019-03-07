import org.apache.commons.dbcp2.BasicDataSource;
import org.nutz.dao.entity.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class TemplatePatternDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplatePatternDemo.class);

    public static void main(String[] args) {
        assert getTableName(ActionLog.class).equals("action_logs");
        assert getTableName(Contest.class).equals("contests");

        query(Problem.class).forEach(System.out::println);
        query(ActionLog.class).forEach(System.out::println);
        query(Contest.class).forEach(System.out::println);
    }

    interface ResultSetReader<T> {
        List<T> execute(ResultSet rs) throws SQLException;
    }

    private static BasicDataSource DATA_SOURCE;

    private static <T> List<T> query(String sql, ResultSetReader<T> reader) {
        List<T> list = new ArrayList<>();
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        try {
            String jdbcUrl = "jdbc:sqlite:db/demo.db";
            if (DATA_SOURCE == null) {
                DATA_SOURCE = new BasicDataSource();
                DATA_SOURCE.setDriverClassName("org.sqlite.JDBC");
                DATA_SOURCE.setUrl(jdbcUrl);
            }
            conn = DATA_SOURCE.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return reader.execute(rs);
        } catch (SQLException e) {
            LOGGER.error("Failed to execute statement {}:", sql, e);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return list;
    }

    private static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to close {}:", closeable.getClass(), e);
        }
    }

    private static <T> List<T> query(Class<T> clazz) {
        String sql = "select * from " + getTableName(clazz);
        return query(sql, clazz);
    }

    private static <T> String getTableName(Class<T> clazz) {
        String tableName = clazz.getAnnotation(Table.class).value();
        if (!tableName.isEmpty())
            return tableName;

        tableName = clazz.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(tableName.charAt(0));
        for (int i = 1; i < tableName.length(); i++) {
            char c = tableName.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                sb.append("_");
            }
            sb.append(c);
        }
        sb.append("s");
        return sb.toString().toLowerCase();
    }

    private static <T> List<T> query(String sql, Class<T> clazz) {
        return query(sql, rs -> {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] objects = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> pt = parameterTypes[i];
                    Object obj = null;
                    if (pt == String.class) {
                        obj = rs.getString(i + 1);
                    } else if (pt == Date.class) {
                        obj = rs.getDate(i + 1);
                    } else if (pt == int.class) {
                        obj = rs.getInt(i + 1);
                    }
                    objects[i] = obj;
                }
                try {
                    T t = (T) constructor.newInstance(objects);
                    list.add(t);
                } catch (Exception e) {
                    LOGGER.error("Failed to create instance of {} via reflection:", clazz, e);
                }
            }
            return list;
        });
    }

}