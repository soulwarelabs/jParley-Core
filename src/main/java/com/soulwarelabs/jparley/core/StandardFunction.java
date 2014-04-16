/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardFunction.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.01, 16 April 2014
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

import com.soulwarelabs.jcommons.Box;

import com.soulwarelabs.jparley.Converter;
import com.soulwarelabs.jparley.Function;

/**
 * Standard SQL stored function.
 *
 * @see Function
 * @see StandardSubroutine
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 16 April 2014
 */
public class StandardFunction extends StandardSubroutine implements Function {

    private int counter;
    private Converter resultDecoder;
    private Object resultKey;
    private String structStruct;
    private int resultType;
    private Box<Object> resultValue;

    /**
     * Creates a new instance of standard function.
     *
     * @param name function name.
     *
     * @since v1.0
     */
    public StandardFunction(String name, int resultType) {
        this(name, resultType, null, null);
    }

    public StandardFunction(String name, int resultType,
            Interceptor preInterceptor, Interceptor postInterceptor) {
        super(name, preInterceptor, postInterceptor);
        this.resultKey = 1;
        this.resultType = resultType;
        this.resultValue = new Box<Object>();
        resetCounter();
    }

    @Override
    public Object getResult() {
        return resultValue.getValue();
    }

    @Override
    public void execute(Connection connection) {
        output(resultKey, resultType, structStruct, resultDecoder);
    }

    @Override
    public void in(Object value) {
        input(counter++, new Box<Object>(value), null, null);
    }

    @Override
    public void reset() {
        super.reset();
        resetCounter();
    }

    @Override
    protected String createSql(String name, int parametersNumber) {
        StringBuilder parameters = new StringBuilder();
        for (int i = 1; i <= parametersNumber - 1; i++) {
            parameters.append(i < parametersNumber - 1 ? "?," : "?");
        }
        return String.format("{? = call %s(%s)}", name, parameters);
    }

    private void resetCounter() {
        counter = 2;
    }
}
