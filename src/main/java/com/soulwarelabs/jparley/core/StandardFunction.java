/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardFunction.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.06, 17 June 2014
 * Created:  16 March 2014
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

import java.sql.Connection;
import java.sql.SQLException;

import com.soulwarelabs.jcommons.Box;
import com.soulwarelabs.jparley.Converter;
import com.soulwarelabs.jparley.Function;

/**
 * Standard SQL stored function.
 *
 * @see Function
 * @see StandardSubroutine
 *
 * @since v1.0.0
 *
 * @author Ilya Gubarev
 * @version 17 June 2014
 */
public class StandardFunction extends StandardSubroutine implements Function {

    /**
     * Standard function result output parameter index.
     *
     * @since v1.0.0
     */
    public static final int RESULT_INDEX = 1;

    private Converter decoder;
    private String struct;
    private int type;
    private Box<Object> value;

    /**
     * Creates a new instance of standard function.
     *
     * @param name function name.
     * @param type function result SQL type code.
     *
     * @since v1.0.0
     */
    public StandardFunction(String name, int type) {
        this(name, type, null, null);
    }

    /**
     * Creates a new instance of standard function.
     *
     * @param name function name.
     * @param type function result SQL type code.
     * @param preInterceptor SQL pre-execution interceptor (optional).
     * @param postInterceptor SQL post-execution interceptor (optional).
     *
     * @see Interceptor
     *
     * @since v1.0.0
     */
    public StandardFunction(String name, int type, Interceptor preInterceptor,
            Interceptor postInterceptor) {
        super(name, preInterceptor, postInterceptor);
        this.type = type;
        this.value = new Box<Object>();
    }

    @Override
    public Converter getDecoder() {
        return decoder;
    }

    @Override
    public void setDecoder(Converter decoder) {
        this.decoder = decoder;
    }

    @Override
    public Box<Object> getResult() {
        return value;
    }

    @Override
    public String getStruct() {
        return struct;
    }

    @Override
    public void setStruct(String struct) {
        this.struct = struct;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        value = output(RESULT_INDEX, getType(), getStruct(), getDecoder());
        super.execute(connection);
        remove(RESULT_INDEX);
    }

    @Override
    protected String createSql(String name, int parametersNumber) {
        StringBuilder parameters = new StringBuilder();
        for (int i = 1; i <= parametersNumber - 1; i++) {
            parameters.append(i < parametersNumber - 1 ? "?," : "?");
        }
        return String.format("{? = call %s(%s)}", name, parameters);
    }
}
