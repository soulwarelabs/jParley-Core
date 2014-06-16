/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     ParametersPrinter.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.03, 16 June 2014
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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.soulwarelabs.jcommons.Box;

/**
 * SQL subroutine parameters printer.
 *
 * @see Interviewer
 *
 * @since v1.0.0
 *
 * @author Ilya Gubarev
 * @version 16 June 2014
 */
public class ParametersPrinter implements Interviewer, Serializable {

    private String prefix;
    private String linePrefix;
    private List<StringBuilder> parameters;

    /**
     * Creates a new instance of parameters printer.
     *
     * @since v1.0.0
     */
    public ParametersPrinter() {
        this("", "");
    }

    /**
     * Creates a new instance of parameters printer.
     *
     * @param prefix prefix to the text view.
     * @param linePrefix prefix to each line of the text view.
     *
     * @since v1.0.0
     */
    public ParametersPrinter(String prefix, String linePrefix) {
        this.prefix = prefix;
        this.linePrefix = linePrefix;
        this.parameters = new LinkedList<StringBuilder>();
    }

    @Override
    public void perform(Object key, Box<?> input, Box<Object> output,
            Integer type, String struct) {
        StringBuilder line = new StringBuilder().append(key).append(" = ");
        if (input == null) {
            line.append(getValueText(output));
        } else {
            if (output == null) {
                line.append(getValueText(input));
            } else {
                line.append(getValueText(input));
                line.append(" / ").append(getValueText(output));
            }
        }
        line.append(" (type: ");
        line.append(getTypeText(type));
        if (struct != null) {
            line.append(", structure: ").append(struct);
        }
        line.append(")");
        parameters.add(line);
    }

    /**
     * Gets a text view of the printer.
     *
     * @return printer text view.
     *
     * @since v1.0.0
     */
    public StringBuilder print() {
        StringBuilder result = new StringBuilder(prefix);
        for (int index = 0; index < parameters.size(); index++) {
            StringBuilder line = parameters.get(index);
            result.append(linePrefix);
            result.append(line);
            result.append(index < parameters.size() - 1 ? ",\r\n" : "");
        }
        return result;
    }

    /**
     * Resets the printer.
     *
     * @since v1.0.0
     */
    public void reset() {
        parameters.clear();
    }

    @Override
    public String toString() {
        return print().toString();
    }

    /**
     * Gets SQL type name by its code.
     *
     * @param type SQL type code.
     * @return SQL type name.
     *
     * @since v1.0.0
     */
    protected Object getTypeText(int type) {
        return type;
    }

    /**
     * Gets parameter value text representation.
     *
     * @param boxed boxed parameter value.
     * @return value text representation.
     *
     * @since v1.0.0
     */
    protected Object getValueText(Box<?> boxed) {
        Object value = boxed != null ? boxed.getValue() : null;
        if (value == null || value instanceof Number) {
            return value;
        }
        return String.format("\"%s\"", value);
    }
}
