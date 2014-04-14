/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardExecutor.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.04, 14 April 2014
 * Created:  08 February 2014
 * Author:   Ilya Gubarev
 *
 * Copyright (c) 2014 Soulware Labs, Ltd.
 * Contact information is available at http://www.soulwarelabs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soulwarelabs.jparley.core;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import com.soulwarelabs.jcommons.sql.ConnectionPool;

import com.soulwarelabs.jparley.Executor;
import com.soulwarelabs.jparley.Function;
import com.soulwarelabs.jparley.Subroutine;

/**
 * Standard SQL subroutines executor.
 *
 * @see Executor
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 14 April 2014
 */
public class StandardExecutor implements Executor, Serializable {

    private ConnectionPool pool;

    /**
     * Creates a new instance of standard executor.
     *
     * @param pool SQL database connection pool.
     *
     * @see ConnectionPool
     *
     * @since v1.0
     */
    public StandardExecutor(ConnectionPool pool) {
        this.pool = pool;
    }

    /**
     * Gets SQL database connection pool.
     *
     * @return SQL database connection pool.
     *
     * @see ConnectionPool
     *
     * @since v1.0
     */
    public ConnectionPool getPool() {
        return pool;
    }

    /**
     * Sets a new SQL database connection pool.
     *
     * @param pool SQL database connection pool.
     *
     * @see ConnectionPool
     *
     * @since v1.0
     */
    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public void call(Subroutine ... subroutines) throws SQLException {
        Boolean autoCommit = null;
        Connection connection = null;
        try {
            connection = getPooledConnection();
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            for (Subroutine subroutine : subroutines) {
                subroutine.execute(connection);
            }
            connection.commit();
        } finally {
            if (connection != null) {
                try {
                    if (autoCommit != null) {
                        connection.setAutoCommit(autoCommit);
                    }
                } finally {
                    connection.close();
                }
            }
        }
    }

    @Override
    public Object call(Function function, Object ... parameters)
            throws SQLException {
        try {
            if (parameters != null) {
                for (Object parameter : parameters) {
                    function.input(parameter);
                }
            }
            call((Subroutine) function);
            return function.getResult().getValue();
        } finally {
            function.reset();
        }
    }

    private Connection getPooledConnection() throws SQLException {
        ConnectionPool connectionPool = getPool();
        if (connectionPool == null) {
            throw new SQLException("connection pool is null");
        }
        return connectionPool.getConnection();
    }
}
