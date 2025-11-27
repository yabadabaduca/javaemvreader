/*
 * Copyright 2010 sasc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sasc;

import sasc.smartcard.common.CardExplorer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import sasc.terminal.Terminal;
import sasc.terminal.TerminalAPIManager;
import sasc.terminal.TerminalException;
import sasc.terminal.TerminalProvider;
import sasc.util.Log;
import sasc.util.Util;

/**
 * Main entry point for Java EMV Reader application.
 * Handles command-line arguments and launches the appropriate mode (GUI, CLI, or emulation).
 * 
 * @author sasc
 */
public class Main {

    /**
     * Main entry point.
     * 
     * @param args Command line arguments:
     *             --help: Print help message
     *             --noGUI: Use command line version
     *             --emulate: Emulate communication with an EMV card
     *             --listTerminals: List all available terminals
     *             --verbose: Print debug messages
     */
    public static void main(String[] args) {
        if (args == null) {
            args = new String[0];
        }

        //Default values
        boolean noGUI = Boolean.getBoolean("java.awt.headless");
        boolean emulate = false;
        boolean listTerminals = false;
        boolean verbose = false;

        //Commons CLI
        //http://commons.apache.org/cli/usage.html

        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("print this message")
                .build();
        Option noGUIOption = Option.builder()
                .longOpt("noGUI")
                .desc("use command line version")
                .build();
        Option emulateOption = Option.builder()
                .longOpt("emulate")
                .desc("emulate communication with an EMV card")
                .build();
        Option listTerminalsOption = Option.builder()
                .longOpt("listTerminals")
                .desc("list all available terminals")
                .build();
        Option terminalOption = Option.builder("t")
                .longOpt("terminal")
                .hasArg()
                .argName("name")
                .desc("the name of the terminal to use")
                .build();
        Option verboseOption = Option.builder("v")
                .longOpt("verbose")
                .desc("print debug messages")
                .build();

        Options options = new Options();

        options.addOption(helpOption);
        options.addOption(noGUIOption);
        options.addOption(emulateOption);
        options.addOption(listTerminalsOption);
        options.addOption(terminalOption);
        options.addOption(verboseOption);

        // create the cmd line parser
        // Updated: GnuParser is deprecated, use DefaultParser instead
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h") || line.hasOption("help")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("javaemvreader", options, true);
                System.exit(0);
            }
            if (line.hasOption("listTerminals")) {
                listTerminals = true;
            }

            if (line.hasOption("noGUI")) {
                noGUI = true;
            }
            if (line.hasOption("emulate")) {
                emulate = true;
            }
            if (line.hasOption("v") || line.hasOption("verbose")) {
                verbose = true;
            }
        } catch (ParseException ex) {
            // oops, something went wrong
            Log.info("Parsing failed. Reason: " + ex.getMessage());
            Log.debug(Util.getStackTrace(ex));
            System.exit(-1);
        }

        if (listTerminals) {
            try{
                TerminalProvider terminalProvider = TerminalAPIManager.getProvider(TerminalAPIManager.SelectionPolicy.ANY_PROVIDER);
                if (terminalProvider == null) {
                    Log.info("No terminal provider available");
                    System.exit(-1);
                    return;
                }
                java.util.List<Terminal> terminals = terminalProvider.listTerminals();
                if (terminals == null || terminals.isEmpty()) {
                    Log.info("No terminals found");
                } else {
                    for(Terminal terminal : terminals){
                        if (terminal != null) {
                            Log.info(terminal.getTerminalInfo());
                        }
                    }
                }
                System.exit(0);

            }catch(TerminalException ex){
                Log.info("Error listing terminals: " + ex.getMessage());
                Log.debug(Util.getStackTrace(ex));
                System.exit(-1);
            }
        }

        if (emulate) {
            try{
                CardEmulatorMain.main(null);
                System.exit(0);
            }catch(TerminalException ex){
                Log.info("Error in emulation mode: " + ex.getMessage());
                Log.debug(Util.getStackTrace(ex));
                System.exit(-1);
            }
        } 

        if (noGUI) {
            //No Swing/GUI
            new CardExplorer().start();
        } else {
            // Create swing app using appframework
            // http://java.dzone.com/news/jsr-296-end-jframe
            // https://appframework.dev.java.net/
            org.jdesktop.application.Application.launch(GUI.class, args);
        }
    }
}
