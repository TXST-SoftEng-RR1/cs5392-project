package edu.txstate.ctl_parser.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

import java.util.logging.Logger;

/**
 * @author Borislav Sabotinov
 *
 * A Kripke structure is defined in a .kpk file
 * A unique file extension is used to denote the special format that is to be used for defining the model.
 *
 */
public class KripkeModelParser {
    private static final Logger logger = Logger.getLogger(KripkeModelParser.class.getName());
    private JsonObject kripkeJsonObj;
    private KripkeStructure kripkeStructure;
    private static final int FROM_STATE_IDX = 0;
    private static final int TO_STATE_IDX = 1;


    public KripkeModelParser() {
        kripkeJsonObj = new JsonObject();
        kripkeStructure = new KripkeStructure();
    }

    public void loadModel(String model) {
        try {
            kripkeJsonObj = JsonParser.parseString(model).getAsJsonObject();
            loadStates();
            loadAtoms();
            loadTransitions();
            logger.info("Current model: \n" + kripkeStructure.toString());
        } catch (JsonSyntaxException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStates() {
        JsonArray states = kripkeJsonObj.getAsJsonObject("kripke-model")
                .getAsJsonArray("states");
        for (int i = 0; i < states.size(); i++) {
            State state = new State(states.get(i).getAsString());
            kripkeStructure.addState(state);
        }
    }

    private void loadAtoms() {
        JsonArray atoms = kripkeJsonObj.getAsJsonObject("kripke-model")
                .getAsJsonArray("atoms");
        for (int i = 0; i < atoms.size(); i++) {
            JsonObject obj = atoms.get(i).getAsJsonObject();

            for (String key : obj.keySet()) {
                State tmpState = kripkeStructure.getState(key);

                JsonArray atomsForSpecificState = obj.get(key).getAsJsonArray();
                for (int j = 0; j < atomsForSpecificState.size(); j++) {
                    tmpState.addAtom(atomsForSpecificState.get(j).getAsString().charAt(0));
                }

            }
        }
    }

    private void loadTransitions() {
        JsonArray transitions = kripkeJsonObj.getAsJsonObject("kripke-model")
                .getAsJsonArray("transitions");
        for (int i = 0; i < transitions.size(); i++) {
            String[] parts = transitions.get(i).getAsString().split(",");
            State fromState = kripkeStructure.getState(parts[FROM_STATE_IDX]);
            State toState = kripkeStructure.getState(parts[TO_STATE_IDX]);
            fromState.addTransition(toState);
        }
    }
}
