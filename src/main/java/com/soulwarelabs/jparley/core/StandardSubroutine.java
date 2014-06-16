/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardSubroutine.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.10, 16 June 2014
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
import com.soulwarelabs.jparley.utility.Parameter;
import com.soulwarelabs.jparley.utility.Statement;

/**
 * Standard SQL stored subroutine.
 *
 * @see Subroutine
 *
 * @since v1.0.0
 *
 * @author Ilya Gubarev
 * @version 16 June 2014
 */
public abstract class StandardSubroutine implements Serializable, Subroutine {

    private String name;
    private Manager manager;
    private Interceptor postInterceptor;
    private Interceptor preInterceptor;

    /**
     * Creates a new instance of standard subroutine.
     *
     * @param name subroutine name.
     *
     * @since v1.0.0
     */
    public StandardSubroutine(String name) {
        this(name, null, null);
    }

    /**
     * Creates a new instance of standard subroutine.
     *
     * @param name subroutine name.
     * @param preInterceptor SQL pre-execution interceptor (optional).
     * @param postInterceptor SQL post-execution interceptor (optional).
     *
     * @see Interceptor
     *
     * @since v1.0.0
     */
    public StandardSubroutine(String name, Interceptor preInterceptor,
            Interceptor postInterceptor) {
        this.name = name;
        this.manager = new Manager();
        this.postInterceptor = postInterceptor;
        this.preInterceptor = preInterceptor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
     * @since v1.0.0
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
     * @since v1.0.0
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
     * @since v1.0.0
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
     * @since v1.0.0
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
        value = value != null ? value : new Box<Object>();
        input(index, value, null, null);
    }

    @Override
    public void in(int index, Object value) {
        input(index, new Box<Object>(value), null, null);
    }

    @Override
    public void in(String name, Box<?> value) {
        value = value != null ? value : new Box<Object>();
        input(name, value, null, null);
    }

    @Override
    public void in(String name, Object value) {
        input(name, new Box<Object>(value), null, null);
    }

    @Override
    public void in(int index, Box<?> value, Integer type) {
        value = value != null ? value : new Box<Object>();
        input(index, value, type, null);
    }

    @Override
    public void in(int index, Object value, Integer type) {
        input(index, new Box<Object>(value), type, null);
    }

    @Override
    public void in(String name, Box<?> value, Integer type) {
        value = value != null ? value : new Box<Object>();
        input(name, value, type, null);
    }

    @Override
    public void in(String name, Object value, Integer type) {
        input(name, new Box<Object>(value), type, null);
    }

    @Override
    public void in(int index, Box<?> value, Converter encoder) {
        value = value != null ? value : new Box<Object>();
        input(index, value, null, encoder);
    }

    @Override
    public void in(int index, Object value, Converter encoder) {
        input(index, new Box<Object>(value), null, encoder);
    }

    @Override
    public void in(String name, Box<?> value, Converter encoder) {
        value = value != null ? value : new Box<Object>();
        input(name, value, null, encoder);
    }

    @Override
    public void in(String name, Object value, Converter encoder) {
        input(name, new Box<Object>(value), null, encoder);
    }

    /**
     * Processes registered parameters with specified processor.
     *
     * @param interviewer parameters processor.
     *
     * @see Interviewer
     *
     * @since v1.0.0
     */
    public void interview(Interviewer interviewer) {
        for (Object key : manager.getKeys()) {
            Parameter parameter = manager.getParameter(key);
            Box<?> input = parameter.getInput();
            input = input != null ? new Box<Object>(input.getValue()) : null;
            Box<Object> output = parameter.getOutput();
            output = output != null ? new Box<Object>(output.getValue()) : null;
            String struct = parameter.getStruct();
            Integer type = parameter.getType();
            interviewer.perform(key, input, output, type, struct);
        }
    }

    /**
     * Gets a text view of the subroutine.
     *
     * @return subroutine text view.
     *
     * @since v1.0.0
     */
    public String print() {
        Interviewer interviewer = new ParametersPrinter("", "\t");
        interview(interviewer);
        return String.format("%s {\r\n%s\r\n}", getName(), interviewer);
    }

    /**
     * Removes registered subroutine parameter
     *
     * @param index parameter index.
     *
     * @since v1.0.0
     */
    public void remove(int index) {
        manager.remove(index);
    }

    /**
     * Removes registered subroutine parameter
     *
     * @param name parameter name.
     *
     * @since v1.0.0
     */
    public void remove(String name) {
        manager.remove(name);
    }

    @Override
    public void reset() {
        manager.removeAll();
    }

    @Override
    public String toString(){
        return print();
    }

    /**
     * Intercepts SQL workflow after subroutine is executed.
     *
     * @param connection SQL database connection.
     * @throws SQLException if error occurs while perform interception.
     *
     * @see #getPostInterceptor()
     *
     * @since v1.0.0
     */
    protected void after(Connection connection) throws SQLException {
        intercept(connection, getPostInterceptor());
    }

    /**
     * Intercepts SQL workflow before subroutine is executed.
     *
     * @param connection SQL database connection.
     * @throws SQLException if error occurs while perform interception.
     *
     * @see #getPreInterceptor()
     *
     * @since v1.0.0
     */
    protected void before(Connection connection) throws SQLException {
        intercept(connection, getPreInterceptor());
    }

    /**
     * Creates a callable SQL statement string for the subroutine.
     *
     * @param name subroutine name.
     * @param parametersNumber subroutine parameters number.
     * @return SQL statement string.
     *
     * @since v1.0.0
     */
    protected abstract String createSql(String name, int parametersNumber);

    /**
     * Registers a new input parameter.
     *
     * @param index parameter index.
     * @param value parameter input value (optional).
     * @param type parameter SQL type code (optional).
     * @param encoder parameter SQL data encoder (optional).
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    protected void input(int index, Box<?> value, Integer type,
            Converter encoder) {
        manager.in(index, value, type, encoder);
    }

    /**
     * Registers a new input parameter.
     *
     * @param name parameter name.
     * @param value parameter input value (optional).
     * @param type parameter SQL type code (optional).
     * @param encoder parameter SQL data encoder (optional).
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    protected void input(String name, Box<?> value, Integer type,
            Converter encoder) {
        manager.in(name, value, type, encoder);
    }

    /**
     * Registers a new output parameter.
     *
     * @param index parameter index.
     * @param type parameter SQL type code.
     * @param struct parameter SQL structure name (optional).
     * @param decoder parameter SQL data decoder (optional).
     * @return boxed output value.
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    protected Box<Object> output(int index, int type, String struct,
            Converter decoder) {
        return manager.out(index, type, struct, decoder);
    }

    /**
     * Registers a new output parameter.
     *
     * @param name parameter name.
     * @param type parameter SQL type code.
     * @param struct parameter SQL structure name (optional).
     * @param decoder parameter SQL data decoder (optional).
     * @return boxed output value.
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    protected Box<Object> output(String name, int type, String struct,
            Converter decoder) {
        return manager.out(name, type, struct, decoder);
    }

    private void intercept(Connection connection, Interceptor interceptor)
            throws SQLException {
        if (interceptor != null) {
            interceptor.perform(connection, this);
        }
    }
}
