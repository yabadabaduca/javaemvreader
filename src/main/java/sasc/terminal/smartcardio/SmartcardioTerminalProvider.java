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
package sasc.terminal.smartcardio;

import java.util.List;
import sasc.terminal.CardConnection;
import sasc.terminal.Terminal;
import sasc.terminal.TerminalException;
import sasc.terminal.TerminalProvider;
import sasc.util.Log;
import sasc.util.Util;

/**
 * Reflection Wrapper
 * 
 * @author sasc
 */
public class SmartcardioTerminalProvider implements TerminalProvider {

    private static boolean isSmartcardIOAvailable = false;
    private static TerminalProvider terminalProvider = null;
    private static final String implementationClassName = SmartcardioTerminalProvider.class.getName() + "Impl";

    //Reflection stuff:
    // http://www.jroller.com/eu/entry/dealing_with_api_compatibility
    // http://wiki.forum.nokia.com/index.php/How_to_use_an_optional_API_in_Java_ME
    static {

        //Hack:
        //since the SmartcardIO classes have not been loaded yet, we can set these
        //system properties here (and not depend on the user setting them on the Command Line):
        System.setProperty("sun.security.smartcardio.t0GetResponse", "false");
        System.setProperty("sun.security.smartcardio.t1GetResponse", "false");

        //call SmartcardIO via reflection, since it might not be available on all platforms
        try {
            // this will throw an exception if "JSR-268 Java Smart Card I/O API" is missing
            Class.forName("javax.smartcardio.TerminalFactory");
            Class c = Class.forName(implementationClassName);
            // Updated: Class.newInstance() is deprecated since Java 9, use getDeclaredConstructor().newInstance() instead
            terminalProvider = (TerminalProvider) (c.getDeclaredConstructor().newInstance());
            isSmartcardIOAvailable = true;
        } catch (ClassNotFoundException ex) {
            // SmartcardIO not available on this platform - this is expected on some systems
            Log.debug("SmartcardIO not available: " + ex.getMessage());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException ex) {
            // Failed to instantiate terminal provider
            Log.debug("Failed to initialize SmartcardIO terminal provider: " + ex.getMessage());
            Log.debug(Util.getStackTrace(ex));
        }
    }
    
//    static void setTerminalProvider(String newName){
//        implementationClassName = newName;
//    }

    public static boolean isSmartcardioAvailable() {
        return isSmartcardIOAvailable;
    }

    @Override
    public List<Terminal> listTerminals() throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        return terminalProvider.listTerminals();
    }

    @Override
    public CardConnection connectAnyTerminal() throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        return terminalProvider.connectAnyTerminal();
    }
    
    @Override
    public CardConnection connectAnyTerminalWithCardPresent(String protocol) throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("Protocol cannot be null");
        }
        return terminalProvider.connectAnyTerminalWithCardPresent(protocol);
    }
    
    @Override
    public CardConnection connectAnyTerminal(String protocol) throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("Protocol cannot be null");
        }
        return terminalProvider.connectAnyTerminal(protocol);
    }

    @Override
    public CardConnection connectTerminal(String name) throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        if (name == null) {
            throw new IllegalArgumentException("Terminal name cannot be null");
        }
        return terminalProvider.connectTerminal(name);
    }

    @Override
    public CardConnection connectTerminal(int index) throws TerminalException {
        if (terminalProvider == null) {
            throw new TerminalException("SmartcardIO terminal provider not available");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Terminal index cannot be negative: " + index);
        }
        return terminalProvider.connectTerminal(index);
    }

    @Override
    public String getProviderInfo() {
        if (terminalProvider == null) {
            return "SmartcardIO not available";
        }
        return terminalProvider.getProviderInfo();
    }
}
