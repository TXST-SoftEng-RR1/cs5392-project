package edu.txstate.ctl_parser.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.txstate.ctl_parser.model.KripkeStructure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
    JsonObject modelObj = JsonParser.parseString(model).getAsJsonObject();;

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