/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     Interviewer.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.02, 16 April 2014
 * Created:  16 February 2014
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

/**
 * SQL subroutine parameter processor.
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 16 April 2014
 */
public interface Interviewer {

    /**
     * Processes information about SQL subroutine parameter.
     *
     * @param index parameter index.
     * @param input boxed parameter output value.
     * @param output boxed parameter output value.
     * @param type parameter SQL type code (optional).
     * @param struct parameter SQL structure name (optional).
     *
     * @see Box
     *
     * @since v1.0
     */
    void perform(int index, Box<?> input, Box<Object> output, Integer type,
            String struct);

    /**
     * Processes information about SQL subroutine parameter.
     *
     * @param name parameter name.
     * @param input boxed parameter output value.
     * @param output boxed parameter output value.
     * @param type parameter SQL type code (optional).
     * @param struct parameter SQL structure name (optional).
     *
     * @see Box
     *
     * @since v1.0
     */
    void perform(String name, Box<?> input, Box<Object> output, Integer type,
            String struct);
}
