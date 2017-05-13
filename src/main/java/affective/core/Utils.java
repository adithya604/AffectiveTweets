/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    Utils.java
 *    Copyright (C) 1999-2016 University of Waikato, Hamilton, New Zealand
 *
 */


package affective.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.python.core.*;
import org.python.util.PythonInterpreter;


import cmu.arktweetnlp.Twokenize;


/**
 * <!-- globalinfo-start -->
 * Provides static functions for String processing.
 * <!-- globalinfo-end -->
 *
 * @author Felipe Bravo-Marquez (fjb11@students.waikato.ac.nz)
 * @version $Revision: 1 $
 */

public class Utils {

    public static PythonInterpreter interpreter = new PythonInterpreter();
    public static PySystemState sys = Py.getSystemState();

    /**
     * tokenizes and normalizes the content of a tweet
     *
     * @param content the input String
     * @param toLowerCase to lowercase the content
     * @param cleanTokens normalize URLs, user mentions, and reduce repetitions of letters
     * @return a list of tokens
     */
    static public List<String> tokenize(String content, boolean toLowerCase, boolean cleanTokens) {
        sys.path.append(new PyString("/deepaffects/miniconda2/envs/tf-py27/lib/python2.7/site-packages/"));
        interpreter.execfile("/deepaffects/repos/tweetokenize/tokenize.py");
        interpreter.set("str", content);
        List<String> result = (List<String>) interpreter.eval("Tokenize().tkn()");
        return result;

    }


    /**
     * Adds a negation prefix to the tokens that follow a negation word until the next punctuation mark.
     *
     * @param tokens the list of tokens to negate
     * @param set    the set with the negated words to use
     * @return the negated tokens
     */
    static public List<String> negateTokens(List<String> tokens, Set<String> set) {
        List<String> negTokens = new ArrayList<String>();

        // flag indicating negation state
        boolean inNegation = false;

        for (String token : tokens) {

            // when we find a negation word for the first time
            if (set.contains(token) && !inNegation) {
                inNegation = true;
                negTokens.add(token);
                continue;
            }

            // if we are in a negation context with add a prefix
            if (inNegation) {
                negTokens.add("NEGTOKEN-" + token);
                // the negation context ends whend finding a punctuation match
                if (token.matches("[\\.|,|:|;|!|\\?]+"))
                    inNegation = false;
            } else {
                negTokens.add(token);
            }
        }
        return negTokens;

    }


}










