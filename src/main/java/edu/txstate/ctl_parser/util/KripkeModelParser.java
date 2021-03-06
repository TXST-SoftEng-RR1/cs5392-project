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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    private static final int FROM_STATE_IDX = 0;
    private static final int TO_STATE_IDX = 1;

    private JsonObject kripkeJsonObj;
    private KripkeStructure kripkeStructure;

    public KripkeModelParser() {
        kripkeJsonObj = new JsonObject();
        kripkeStructure = new KripkeStructure();
    }

    public KripkeStructure loadModel(JsonObject model) {
        try {
            kripkeJsonObj = model;
            loadStates();
            loadAtoms();
            loadTransitions();
        } catch (JsonSyntaxException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
        return kripkeStructure;
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

    public KripkeStructure getKripkeStructure() {
        return kripkeStructure;
    }

    public void setKripkeStructure(KripkeStructure kripkeStructure) {
        this.kripkeStructure = kripkeStructure;
    }
}
