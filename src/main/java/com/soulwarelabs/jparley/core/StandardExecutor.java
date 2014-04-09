/*
 * Copyright 2014 Soulware Labs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soulwarelabs.jparley.core;

import java.sql.Connection;
import java.sql.SQLException;

import com.soulwarelabs.jcommons.sql.ConnectionPool;

import com.soulwarelabs.jparley.Executor;
import com.soulwarelabs.jparley.Function;
import com.soulwarelabs.jparley.Subroutine;

/**
 *
 * @author gubarev
 */
public class StandardExecutor implements Executor {

    private ConnectionPool pool;

    public StandardExecutor(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public void call(Subroutine ... subroutines) throws SQLException {
        Boolean autoCommit = null;
        Connection connection = pool.getConnection();
        try {
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            for (Subroutine subroutine : subroutines) {
                subroutine.execute(connection);
            }
            connection.commit();
        } finally {
            if (autoCommit != null) {
                connection.setAutoCommit(autoCommit);
            }
            connection.close();
        }
    }

    @Override
    public Object call(Function function, Object ... parameters)
            throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                function.input(i + 2, parameters[i]);
            }
        }
        call((Subroutine) function);
        return function.getResult().getValue();
    }
}
