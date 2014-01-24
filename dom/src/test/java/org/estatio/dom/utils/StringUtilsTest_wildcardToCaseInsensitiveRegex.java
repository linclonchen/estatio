/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.dom.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class StringUtilsTest_wildcardToCaseInsensitiveRegex {

    private String from;
    private String to;

    @Parameters
    public static Collection<Object[]> values() {
        return Arrays.asList(
                new Object[][]{
                    {"*abc?def*ghi", "(?i).*abc.def.*ghi"},
                    {null, null},
                }
            );
    }
    
    public StringUtilsTest_wildcardToCaseInsensitiveRegex(String from, String to) {
        this.from = from;
        this.to = to;
    }
    
    @Test
    public void nonNull() throws Exception {
        assertEquals(to, StringUtils.wildcardToCaseInsensitiveRegex(from));
    }
    
}
