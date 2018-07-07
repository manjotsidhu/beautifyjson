/*
 * Copyright 2018 "Manjot Sidhu" <manjot.techie@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.manjotsidhu.beautifyjson;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import java.util.regex.*;

/**
 *
 * @author manjotsidhu
 */
public class beautifyjson {

    String beautifyString;

    String Beautify(File file) throws IOException {
        this.beautifyString = FileUtils.readFileToString(file);
        return beautify();
    }

    String Beautify(String str) {
        this.beautifyString = str;
        return beautify();
    }

    /**
     * Beautify's string using regex and some java hacks for indentation
     *
     * @return beautified string
     */
    public String beautify() {
        this.beautifyString = replaceWithPattern(this.beautifyString, "\t", "");
        this.beautifyString = replaceWithPattern(this.beautifyString, "(?<![\\S\"])(\\s)", "");
        this.beautifyString = replaceWithPattern(this.beautifyString, "\n", "");
        this.beautifyString = replaceWithPattern(this.beautifyString, "(\\s)(?![\\S\"])", "");
        this.beautifyString = replaceWithPattern(this.beautifyString, "(?<!B)(\\{|\\}|\\[|\\])\\,?", "$0\n");
        this.beautifyString = replaceWithPattern(this.beautifyString, "(.+)(\\}\\,?|\\]\\,?)", "$1\n$2");
        this.beautifyString = replaceWithPattern(this.beautifyString, "[^\\}\\]]\\,", "$0\n");
        this.beautifyString = replaceWithPattern(this.beautifyString, ":", "$0 ");

        String fnl = "";

        Scanner sc = new Scanner(this.beautifyString);

        int i = 0;
        while (sc.hasNext()) {
            String tmp = sc.nextLine();

            if (tmp.contains("}") || tmp.contains("]")) {
                i -= 2;
            }

            String ind = new String(new char[i]).replace("\0", " ");
            fnl += ind + tmp + "\n";

            if (!tmp.contains("B[")) {
                if (tmp.contains("{") || tmp.contains("[")) {
                    i += 2;
                }
            }
        }
        return fnl;
    }

    /**
     *
     * Matches <b>string</b> with a <b>pattern</b> and replaces it with
     * <b>string</b>
     * returns the replaced string
     *
     * @param str String to be matches/replaced
     * @param ptrn Regex Pattern
     * @param replace Regex string to replace with
     * @return replaced String <b>str</b>
     */
    public String replaceWithPattern(String str, String ptrn, String replace) {

        Pattern ptn = Pattern.compile(ptrn);
        Matcher mtch = ptn.matcher(str);
        return mtch.replaceAll(replace);
    }
}
