/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardSubroutine.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.03, 15 April 2014
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
import com.soulwarelabs.jcommons.Optional;

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
 * @version 15 April 2014
 */
public abstract class StandardSubroutine implements Serializable, Subroutine {

    private String name;
    private Manager manager;

    public StandardSubroutine(String name) {
        this.name = name;
        this.manager = new Manager();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintedView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        before(connection);
        Statement statement = createStatement(getName(), manager.getTotal());
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
    public void in(int index, @Optional Object value) {
        input(index, new Box<Object>(value), null, null);
    }

    @Override
    public void in(String name, Box<?> value) {
        input(name, value, null, null);
    }

    @Override
    public void in(String name, @Optional Object value) {
        input(name, new Box<Object>(value), null, null);
    }

    @Override
    public void in(int index, Box<?> value, @Optional Integer type) {
        input(index, value, type, null);
    }

    @Override
    public void in(int index, @Optional Object value, @Optional Integer type) {
        input(index, new Box<Object>(value), type, null);
    }

    @Override
    public void in(String name, Box<?> value, @Optional Integer type) {
        input(name, value, type, null);
    }

    @Override
    public void in(String name, @Optional Object value, @Optional Integer type) {
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

    @Override
    public void reset() {
        manager.removeAll();
    }

    protected void after(Connection connection) throws SQLException {

    }

    protected void before(Connection connection) throws SQLException {

    }

    protected abstract String createSql(String name, int parametersNumber);

    protected void input(Object key, Box<?> value, @Optional Integer type,
            @Optional Converter encoder) {
        manager.in(key, value, type, encoder);
    }

    protected Box<Object> output(Object key, int type, @Optional String struct,
            @Optional Converter decoder) {
        return manager.out(key, type, struct, decoder);
    }

    private Statement createStatement(String name, int parametersNumber)
            throws SQLException {
        throw new UnsupportedOperationException();
    }
}
