/*
 * Copyright (c) 2021 borislavsabotinov.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.txstate.ctl_parser.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KripkeModelParserTest {
    KripkeModelParser kripkeModelParser;
    String model = "{\n" +
            "\t\"kripke-model\": {\n" +
            "\t\t\"states\": [\"s1\", \"s2\", \"s3\", \"s4\"],\n" +
            "\t\t\"transitions\": [\"s1,s2\", \"s1,s3\", \"s3,s4\", \"s4,s2\", \"s2,s3\"],\n" +
            "\t\t\"atoms\": [{\"s1\": [\"p\",\"q\"]}, {\"s2\": [\"q\",\"t\",\"r\"]}, {\"s3\": []}, {\"s4\": [\"t\"]}]\n" +
            "\t}\n" +
            "}";
    JsonObject modelObj = JsonParser.parseString(model).getAsJsonObject();

    @BeforeEach
    void setUp() {
        kripkeModelParser = new KripkeModelParser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadModel_success_validKripkeStructure() {
        kripkeModelParser.loadModel(modelObj);
        assertTrue(kripkeModelParser.getKripkeStructure().toString().contains("qtr"));
    }
}