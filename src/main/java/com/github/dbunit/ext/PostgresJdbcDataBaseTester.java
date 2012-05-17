/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext;

import org.dbunit.JdbcDatabaseTester;

/**
 * 描述：
 * Created by JarnTang at 12-5-14 上午12:16
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class PostgresJdbcDataBaseTester extends JdbcDatabaseTester{

    public PostgresJdbcDataBaseTester(String driverClass, String connectionUrl) throws ClassNotFoundException {
        super(driverClass, connectionUrl);
    }

    public PostgresJdbcDataBaseTester(String driverClass, String connectionUrl, String username, String password) throws ClassNotFoundException {
        super(driverClass, connectionUrl, username, password);
    }

    public PostgresJdbcDataBaseTester(String driverClass, String connectionUrl, String username, String password, String schema) throws ClassNotFoundException {
        super(driverClass, connectionUrl, username, password, schema);
    }

}
