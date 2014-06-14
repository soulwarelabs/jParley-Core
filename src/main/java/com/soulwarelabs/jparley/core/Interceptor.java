/*
 * Project:  jParley-Core
 * Outline:  jParley framework core components
 *
 * File:     Interceptor.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.04, 14 June 2014
 * Created:  15 February 2014
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

import com.soulwarelabs.jparley.Subroutine;

/**
 * SQL execution workflow interceptor.
 *
 * @since v1.0.0
 *
 * @author Ilya Gubarev
 * @version 14 June 2014
 */
public interface Interceptor {

    /**
     * Intercepts SQL execution workflow.
     *
     * @param connection SQL database connection.
     * @param target target subroutine for interception (optional).
     * @throws SQLException if error occurs while performing interception.
     *
     * @see Connection
     *
     * @since v1.0.0
     */
    void perform(Connection connection, Subroutine target) throws SQLException;
}
