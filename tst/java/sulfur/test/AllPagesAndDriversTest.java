/*
This file is part of the Sulfur project by Ivan De Marino (http://ivandemarino.me).

Copyright (c) 2013, Ivan De Marino (http://ivandemarino.me)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package sulfur.test;

import sulfur.SBaseTest;
import sulfur.SPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SPageFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino
 */
public class AllPagesAndDriversTest extends SBaseTest {

    public AllPagesAndDriversTest() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.sulfur.pageconfigs");
        System.setProperty(SPageFactory.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @DataProvider(name = "allPagesByAllDrivers")
    public Iterator<Object[]> provideAllPagesByAllDrivers() {
        return makeCartesianProvider(provideConfiguredPages(), provideConfiguredDrivers());
    }

    @Test(dataProvider = "allPagesByAllDrivers")
    public void shouldLoadAllConfiguredPagesInAllConfiguredDrivers(String pageName, String driverName) {
        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("asin", "B00CV77WBU");

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("asin", "B00CV77WBU");
        queryParams.put("key2", "val2");

        // create the page (self closing)
        SPage p = createSelfDisposingPage(driverName, pageName, pathParams, queryParams);
        // open the page
        p.open();
        // validate the title
        assertTrue(p.getCurrentUrl().toLowerCase().contains("amazon.com"));
    }
}
