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

package edu.txstate.ctl_parser.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.txstate.ctl_parser.util.KripkeModelParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class KripkeStructureTest {

    KripkeModelParser kripkeModelParser;
    String model1 = "{\n" +
            "\t\"kripke-model\": {\n" +
            "\t\t\"states\": [\"s1\", \"s2\", \"s3\", \"s4\"],\n" +
            "\t\t\"transitions\": [\"s1,s2\", \"s1,s3\", \"s3,s4\", \"s4,s2\", \"s2,s3\"],\n" +
            "\t\t\"atoms\": [{\"s1\": [\"p\",\"q\"]}, {\"s2\": [\"q\",\"t\",\"r\"]}, {\"s3\": []}, {\"s4\": [\"t\"]}]\n" +
            "\t}\n" +
            "}";
    JsonObject modelObj = JsonParser.parseString(model1).getAsJsonObject();

    @BeforeEach
    void setUp() {
        kripkeModelParser = new KripkeModelParser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateFormula() {
    }

    @Test
    void printMarkings() {
    }

    /**
     * Check E[t U p] against the defined model, where
     * s1 contains atoms pq, transitions to s2 and s3
     * s2 contains atoms qtr, NO transitions
     * s3 contains no atoms, transitions to s4
     * s4 contains atom t, transitions to s2
     * Result should be FALSE, E[t U p] does NOT hold for
     * state s2.
     */
    @Test
    void validateFormula_doesNotHold_basicCTL() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "E[t U p]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertFalse(kripkeStructure.validateFormula(formulaStream, state));
    }

    /**
     * Check E[t U q] against the defined model, where
     * s1 contains atoms pq, transitions to s2 and s3
     * s2 contains atoms qtr, NO transitions
     * s3 contains no atoms, transitions to s4
     * s4 contains atom t, transitions to s2
     * Result should be TRUE, E[t U q] does hold for
     * state s2.
     */
    @Test
    void validateFormula_holds_untilCTL() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "E[t U q]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    /**
     * Check EXr against the defined model, where
     * s1 contains atoms pq, transitions to s2 and s3
     * s2 contains atoms qtr, NO transitions
     * s3 contains no atoms, transitions to s4
     * s4 contains atom t, transitions to s2
     * Result should be TRUE, EXr does hold for
     * state s1.
     */
    @Test
    void validateFormula_holds_basicCTL() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "EXr";
        String state = "s1";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }


    /**
     * This test is about ensuring we prevent this error from occurring:
     * ERROR: Second call to constructor of static parser.
     * You must either use ReInit() or set the JavaCC option STATIC to false
     * during parser generation.
     * <p>
     * KripkeStructure's verifyFormula() method was adjusted to ensure ReInit()
     * is called by checking if isJj_initialized_once() returns true. The latter
     * is a new getter introduced in CTLParser to access the state of the jj_initialized_once
     * boolean variable.
     * <p>
     * This approach allows us to use a single parser, keep static = true in the OPTIONS, and
     * utilize the parser via successive calls. Note that we need to clear the InputStream.
     */
    @Test
    void validateFormula_holds_invokeCTLParserTwice() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "EXr";
        String state = "s1";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        kripkeStructure.validateFormula(formulaStream, state);
        formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_test72_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "EX(AFp | EFr)";
        String state = "s4";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_test61_false() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "AGq";
        String state = "s4";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertFalse(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_test10_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "EG(r->t)";
        String state = "s1";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_test25_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "(~AXq)";
        String state = "s1";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_complexTest_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "A[p U (A[q U r] -> r)]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_complexTest_false() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "A[p U (A[q U r] -> p)]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertFalse(kripkeStructure.validateFormula(formulaStream, state));
    }

    /**
     * equivalent to test 74 A[pUA[qUr]] but simplified
     */
    @Test
    void model1_customUntil_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "A[p U r]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }

    @Test
    void model1_complexUntil_true() {
        KripkeStructure kripkeStructure = kripkeModelParser.loadModel(modelObj);
        String formula = "E[A[q U r] U t]";
        String state = "s2";
        InputStream formulaStream = new ByteArrayInputStream(formula.getBytes());
        assertTrue(kripkeStructure.validateFormula(formulaStream, state));
    }
}