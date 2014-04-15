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

import com.soulwarelabs.jparley.Converter;
import com.soulwarelabs.jparley.Subroutine;
import com.soulwarelabs.jparley.utility.Manager;

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
public class StandardSubroutine implements Serializable, Subroutine {

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

    @Override
    public void execute(Connection connection) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void in(int index, Box<?> value) {
        manager.in(index, value, null, null);
    }

    @Override
    public void in(int index, Object value) {
        manager.in(index, new Box<Object>(value), null, null);
    }

    @Override
    public void in(String name, Box<?> value) {
        manager.in(name, value, null, null);
    }

    @Override
    public void in(String name, Object value) {
        manager.in(name, new Box<Object>(value), null, null);
    }

    @Override
    public void in(int index, Box<?> value, Integer type) {
        manager.in(index, value, type, null);
    }

    @Override
    public void in(int index, Object value, Integer type) {
        manager.in(index, new Box<Object>(value), type, null);
    }

    @Override
    public void in(String name, Box<?> value, Integer type) {
        manager.in(name, value, type, null);
    }

    @Override
    public void in(String name, Object value, Integer type) {
        manager.in(name, new Box<Object>(value), type, null);
    }

    @Override
    public void in(int index, Box<?> value, Converter encoder) {
        manager.in(index, value, null, encoder);
    }

    @Override
    public void in(int index, Object value, Converter encoder) {
        manager.in(index, new Box<Object>(value), null, encoder);
    }

    @Override
    public void in(String name, Box<?> value, Converter encoder) {
        manager.in(name, value, null, encoder);
    }

    @Override
    public void in(String name, Object value, Converter encoder) {
        manager.in(name, new Box<Object>(value), null, encoder);
    }

    @Override
    public void reset() {
        manager.removeAll();
    }
}
