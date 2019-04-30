/* 
   Copyright (C) 2014 Red Hat, Inc.

This file is part of IcedTea.

IcedTea is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 2.

IcedTea is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with IcedTea; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version.
*/

package net.sourceforge.jnlp.util.docprovider.formatters.formatters;

import net.sourceforge.jnlp.util.docprovider.TextsProvider;

import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManFormatter extends ReplacingTextFormatter {

    public static class KnownSections {

        public static final String NAME = "NAME";
        public static final String SYNOPSIS = "SYNOPSIS";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String OPTIONS = "CommandLineOptions";
        public static final String COMMANDS = "COMMANDS";
        public static final String EXAMPLES = "EXAMPLES";
        public static final String FILES = "FILES";
        public static final String BUGS = "BUGS";
        public static final String AUTHOR = "AUTHOR";
        public static final String SEE_ALSO = "SEE_ALSO";

    }

    private static final String SUFFIX = "1";

    @Override
    public String getNewLine() {
        return PlainTextFormatter.getLineSeparator() + ".br" + PlainTextFormatter.getLineSeparator();
    }

    @Override
    public String getBoldOpening() {
        return PlainTextFormatter.getLineSeparator() + ".B ";
    }

    @Override
    public String getBoldClosing() {
        return PlainTextFormatter.getLineSeparator();
    }

    /**
     * There is one line break less
     */
    @Override
    public String getBreakAndBold() {
        return PlainTextFormatter.getLineSeparator() + ".br" + PlainTextFormatter.getLineSeparator() + ".B ";
    }


    /**
     * There are two lines breaks less
     */
    @Override
    public String getBoldCloseNwlineBoldOpen() {
        return PlainTextFormatter.getLineSeparator() + ".br" + PlainTextFormatter.getLineSeparator() + ".B ";
    }

    @Override
    public String getTitle(String name) {
        return ".SH " + localizeTitle(name) + PlainTextFormatter.getLineSeparator() + PlainTextFormatter.getLineSeparator();
    }

    @Override
    public String getHeaders(String id, Charset encoding) {
        return ".TH " + id + " 1 \"" + getManPageDate() + "\"" + " \"" + TextsProvider.ITW + " " + getVersion() + "\"" + PlainTextFormatter.getLineSeparator();
    }

    @Override
    public String getTail() {
        return "";
    }

    private String getManPageDate() {
        final Date now = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(now);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int year = c.get(Calendar.YEAR);
        final Format formatter = new SimpleDateFormat("MMM");
        final String month = formatter.format(now);
        return day + " " + month + " " + year;
    }

    @Override
    public String getSeeAlso(String s) {
        return ".BR " + s + " (1)" + PlainTextFormatter.getLineSeparator();
    }

    @Override
    public String getFileSuffix() {
        return "." + SUFFIX;
    }

    @Override
    public String wrapParagraph(String s) {
        return PlainTextFormatter.getLineSeparator() + ".PP" + PlainTextFormatter.getLineSeparator() + s;
    }

    @Override
    public String getOption(String key, String value) {
        if (key.startsWith("-")) {
            key = "\\" + key;
        }
        if (value.startsWith("-")) {
            value = "\\" + value;
        }
        return ".TP 12" + PlainTextFormatter.getLineSeparator()
                + key + PlainTextFormatter.getLineSeparator()
                + process(value) + PlainTextFormatter.getLineSeparator();
    }
}
