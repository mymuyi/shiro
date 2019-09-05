package chapter6;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author: yangke
 * @Date: 2019/8/28
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }
        return jdbcTemplate;
    }


    private static JdbcTemplate createJdbcTemplate() {
        DruidDataSource db = new DruidDataSource();
        db.setDriverClassName("com.mysql.jdbc.Driver");
        db.setUrl("jdbc:mysql://localhost:3306/shiro");
        db.setUsername("root");
        db.setPassword("123456");

        return new JdbcTemplate(db);
    }

}
