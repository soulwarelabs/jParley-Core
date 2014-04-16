/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     StandardProcedure.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.04, 16 April 2014
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

import com.soulwarelabs.jcommons.Box;

import com.soulwarelabs.jparley.Converter;
import com.soulwarelabs.jparley.Procedure;

/**
 * Standard SQL stored procedure.
 *
 * @see Procedure
 * @see StandardSubroutine
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 16 April 2014
 */
public class StandardProcedure extends StandardSubroutine implements Procedure {

    /**
     * Creates a new instance of standard procedure.
     *
     * @param name procedure name.
     *
     * @since v1.0
     */
    public StandardProcedure(String name) {
        this(name, null, null);
    }

    /**
     * Creates a new instance of standard procedure.
     *
     * @param name procedure name.
     * @param preInterceptor SQL pre-execution interceptor (optional).
     * @param postInterceptor SQL post-execution interceptor (optional).
     *
     * @see Interceptor
     *
     * @since v1.0
     */
    public StandardProcedure(String name, Interceptor preInterceptor,
            Interceptor postInterceptor) {
        super(name, preInterceptor, postInterceptor);
    }

    @Override
    public Box<Object> out(int index, int type) {
        return output(index, type, null, null);
    }

    @Override
    public Box<Object> out(String name, int type) {
        return output(name, type, null, null);
    }

    @Override
    public Box<Object> out(int index, int type, String struct) {
        return output(index, type, struct, null);
    }

    @Override
    public Box<Object> out(String name, int type, String struct) {
        return output(name, type, struct, null);
    }

    @Override
    public Box<Object> out(int index, int type, Converter decoder) {
        return output(index, type, null, decoder);
    }

    @Override
    public Box<Object> out(String name, int type, Converter decoder) {
        return output(name, type, null, decoder);
    }

    @Override
    public Box<Object> out(int index, int type, String struct,
            Converter decoder) {
        return output(index, type, struct, decoder);
    }

    @Override
    public Box<Object> out(String name, int type, String struct,
            Converter decoder) {
        return output(name, type, struct, decoder);
    }

    @Override
    protected String createSql(String name, int parametersNumber) {
        StringBuilder parameters = new StringBuilder();
        for (int i = 1; i <= parametersNumber; i++) {
            parameters.append(i < parametersNumber ? "?," : "?");
        }
        return String.format("{call %s(%s)}", name, parameters);
    }
}
