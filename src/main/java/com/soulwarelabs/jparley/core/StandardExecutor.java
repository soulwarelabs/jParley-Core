/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardExecutor.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.09, 16 April 2014
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
 * @version 16 April 2014
 */
public class StandardExecutor implements Executor, Serializable {

    /**
     * Executes specified SQL stored subroutines in a single transaction.
     *
     * @param connection SQL database connection.
     * @param subroutines SQL subroutines to be executed.
     * @throws SQLException if error occurs while executing the subroutines.
     *
     * @see Subroutine
     *
     * @since v1.0
     */
    public static void call(Connection connection, Subroutine ... subroutines)
            throws SQLException {
        call(null, connection, subroutines);
    }

    private static void call(StandardExecutor executor, Connection connection,
            Subroutine ... subroutines) throws SQLException {
        Boolean autoCommit = null;
        try {
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            if (executor != null) {
                executor.before(connection);
            }
            for (Subroutine subroutine : subroutines) {
                subroutine.execute(connection);
            }
            if (executor != null) {
                executor.after(connection);
            }
            connection.commit();
        } finally {
            if (connection != null) {
                if (autoCommit != null) {
                    connection.setAutoCommit(autoCommit);
                }
            }
        }
    }

    private ConnectionPool pool;
    private Interceptor postInterceptor;
    private Interceptor preInterceptor;

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
        this(pool, null, null);
    }

    public StandardExecutor(ConnectionPool pool, Interceptor preInterceptor,
            Interceptor postInterceptor) {
        this.pool = pool;
        this.postInterceptor = postInterceptor;
        this.preInterceptor = preInterceptor;
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

    /**
     * Gets SQL workflow post-execution interceptor.
     *
     * @return SQL workflow interceptor.
     *
     * @see Interceptor
     *
     * @since v1.0
     */
    public Interceptor getPostInterceptor() {
        return postInterceptor;
    }

    /**
     * Sets a new SQL workflow post-execution interceptor.
     *
     * @param postInterceptor SQL workflow interceptor.
     *
     * @see Interceptor
     *
     * @since v1.0
     */
    public void setPostInterceptor(Interceptor postInterceptor) {
        this.postInterceptor = postInterceptor;
    }

    /**
     * Gets SQL workflow pre-execution interceptor.
     *
     * @return SQL workflow interceptor.
     *
     * @see Interceptor
     *
     * @since v1.0
     */
    public Interceptor getPreInterceptor() {
        return preInterceptor;
    }

    /**
     * Sets a new SQL workflow pre-execution interceptor.
     *
     * @param preInterceptor SQL workflow interceptor.
     *
     * @see Interceptor
     *
     * @since v1.0
     */
    public void setPreInterceptor(Interceptor preInterceptor) {
        this.preInterceptor = preInterceptor;
    }

    @Override
    public void call(Subroutine ... subroutines) throws SQLException {
        Connection connection = null;
        try {
            ConnectionPool connectionPool = getPool();
            if (connectionPool == null) {
                throw new SQLException("connection pool is null");
            }
            connection = connectionPool.getConnection();
            call(this, connection, subroutines);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public Object call(Function function, Object ... parameters)
            throws SQLException {
        try {
            if (parameters != null) {
                for (Object parameter : parameters) {
                    function.in(parameter);
                }
            }
            call((Subroutine) function);
            return function.getResult();
        } finally {
            function.reset();
        }
    }

    /**
     * Intercepts SQL workflow after subroutines are executed.
     *
     * @param connection SQL database connection.
     * @throws SQLException if error occurs while perform interception.
     *
     * @see #getPostInterceptor()
     *
     * @since v1.0
     */
    protected void after(Connection connection) throws SQLException {
        intercept(connection, getPostInterceptor());
    }

    /**
     * Intercepts SQL workflow before subroutines are executed.
     *
     * @param connection SQL database connection.
     * @throws SQLException if error occurs while perform interception.
     *
     * @see #getPreInterceptor()
     *
     * @since v1.0
     */
    protected void before(Connection connection) throws SQLException {
        intercept(connection, getPreInterceptor());
    }

    private void intercept(Connection connection, Interceptor interceptor)
            throws SQLException {
        if (interceptor != null) {
            interceptor.perform(connection);
        }
    }
}
