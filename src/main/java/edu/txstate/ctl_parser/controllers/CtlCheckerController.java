/*
 * Copyright (c) 2020. SimplyATX.com
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

package edu.txstate.ctl_parser.controllers;

import edu.txstate.ctl_parser.util.KripkeModelParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * @author Borislav Sabotinov
 * The @RestController is a combination of @ResponseBody and @Controller annotations
 */
@RestController
public class CtlCheckerController {
    Logger logger = Logger.getLogger(CtlCheckerController.class.getName());
    @Value("${TARGET:World}")
    String message;

    @GetMapping("/hello")
    String hello() {
        return "CS5392 CTL Checker App says hello!" + message + "!";
    }

    /**
     * "The @ResponseBody annotation [...] can be put on a method and indicates that the
     * return type should be written straight to the HTTP response body (and not placed in a Model,
     * or interpreted as a view name)."
     * http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-responsebody
     * @param model
     * @return
     */
    @PostMapping(value = "/uploadModel", produces = "text/plain")
    @ResponseBody
    public String uploadModel(@RequestBody String model) {
        KripkeModelParser kripkeModelParser = new KripkeModelParser();
        kripkeModelParser.loadModel(model);
        logger.info("Model received: ");
        logger.info(model);
        return "Model received. ";
    }

    @PostMapping(value = "/uploadFormula", produces = "text/plain")
    @ResponseBody
    public String uploadFormula(@RequestBody String CTLFormula) {

        return "Formula received. ";
    }

}
