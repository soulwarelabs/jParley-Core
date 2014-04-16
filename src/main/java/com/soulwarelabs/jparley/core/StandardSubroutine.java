/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardSubroutine.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.05, 16 April 2014
 * Created:  10 March 2014
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

import com.soulwarelabs.jcommons.Box;

import com.soulwarelabs.jparley.Converter;
import com.soulwarelabs.jparley.Subroutine;
import com.soulwarelabs.jparley.utility.Manager;
import com.soulwarelabs.jparley.utility.Statement;

/**
 * Standard SQL stored subroutine.
 *
 * @see Subroutine
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 16 April 2014
 */
public abstract class StandardSubroutine implements Serializable, Subroutine {

    private String name;
    private Manager manager;
    private Interceptor postInterceptor;
    private Interceptor preInterceptor;

    public StandardSubroutine(String name) {
        this(name, null, null);
    }

    public StandardSubroutine(String name, Interceptor preInterceptor,
            Interceptor postInterceptor) {
        this.name = name;
        this.manager = new Manager();
        this.postInterceptor = postInterceptor;
        this.preInterceptor = preInterceptor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public void execute(Connection connection) throws SQLException {
        before(connection);
        String sql = createSql(getName(), manager.getTotal());
        Statement statement = new Statement(connection.prepareCall(sql));
        manager.setupAll(connection, statement);
        statement.execute();
        manager.parseAll(connection, statement);
        after(connection);
    }

    @Override
    public void in(int index, Box<?> value) {
        input(index, value, null, null);
    }

    @Override
    public void in(int index, Object value) {
        input(index, new Box<Object>(value), null, null);
    }

    @Override
    public void in(String name, Box<?> value) {
        input(name, value, null, null);
    }

    @Override
    public void in(String name, Object value) {
        input(name, new Box<Object>(value), null, null);
    }

    @Override
    public void in(int index, Box<?> value, Integer type) {
        input(index, value, type, null);
    }

    @Override
    public void in(int index, Object value, Integer type) {
        input(index, new Box<Object>(value), type, null);
    }

    @Override
    public void in(String name, Box<?> value, Integer type) {
        input(name, value, type, null);
    }

    @Override
    public void in(String name, Object value, Integer type) {
        input(name, new Box<Object>(value), type, null);
    }

    @Override
    public void in(int index, Box<?> value, Converter encoder) {
        input(index, value, null, encoder);
    }

    @Override
    public void in(int index, Object value, Converter encoder) {
        input(index, new Box<Object>(value), null, encoder);
    }

    @Override
    public void in(String name, Box<?> value, Converter encoder) {
        input(name, value, null, encoder);
    }

    @Override
    public void in(String name, Object value, Converter encoder) {
        input(name, new Box<Object>(value), null, encoder);
    }

    public String print() {
        return toString();
    }

    @Override
    public void reset() {
        manager.removeAll();
    }

    protected void after(Connection connection) throws SQLException {
        intercept(connection, getPostInterceptor());
    }

    protected void before(Connection connection) throws SQLException {
        intercept(connection, getPreInterceptor());
    }

    protected abstract String createSql(String name, int parametersNumber);

    protected void input(Object key, Box<?> value, Integer type,
            Converter encoder) {
        manager.in(key, value, type, encoder);
    }

    protected Box<Object> output(Object key, int type, String struct,
            Converter decoder) {
        return manager.out(key, type, struct, decoder);
    }

    private void intercept(Connection connection, Interceptor interceptor)
            throws SQLException {
        if (interceptor != null) {
            interceptor.perform(connection);
        }
    }
}
