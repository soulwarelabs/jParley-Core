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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.soulwarelabs.jcommons.Box;

/**
 *
 * @author gubarev
 */
public class ParametersPrinter implements Interviewer, Serializable {

    private List<StringBuilder> parameters;

    public ParametersPrinter() {
        parameters = new LinkedList<StringBuilder>();
    }

    @Override
    public void perform(int index, Box<?> input, Box<Object> output,
            Integer type, String struct) {
        perform(index, input, output, type, struct);
    }

    @Override
    public void perform(String name, Box<?> input, Box<Object> output,
            Integer type, String struct) {
        perform(name, input, output, type, struct);
    }

    public void perform(Object key, Box<?> input, Box<Object> output,
            Integer type, String struct) {
        StringBuilder line = new StringBuilder();
        line.append(key);
        line.append(" = ");
        line.append(input);
        line.append("/");
        line.append(output);
        line.append(" (");
        line.append(type);
        line.append("/");
        line.append(struct);
        line.append("}");
        parameters.add(line);
    }

    public StringBuilder print() {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < parameters.size(); index++) {
            StringBuilder line = parameters.get(index);
            result.append(line);
            result.append(index < parameters.size() - 1 ? ",\r\n" : "");
        }
        reset();
        return result;
    }

    public void reset() {
        parameters.clear();
    }
}
