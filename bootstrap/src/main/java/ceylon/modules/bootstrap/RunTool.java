/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
 * by the author tags.
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

package ceylon.modules.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;

@Summary("Executes a Ceylon program")
@Description(
"Executes the ceylon program specified as the `<module>` argument. "+
"The `<module>` may optionally include a version."
)
@RemainingSections(
"##EXAMPLE\n" +
"\n" +
"The following would execute the `com.example.foobar` module:\n" +
"\n" +
"    ceylon run com.example.foobar/1.0.0"
)
public class RunTool implements Plugin {
    
    private String moduleNameOptVersion;
    
    private String run;
    
    private List<String> repo = new ArrayList<String>(1);
    
    private boolean disableDefault;
    
    @Argument(argumentName="module", multiplicity="1")
    public void setModule(String moduleNameOptVersion) {
        this.moduleNameOptVersion = moduleNameOptVersion;
    }

    @OptionArgument(longName="run", argumentName="toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class with no parameters.")
    public void setRun(String run) {
        this.run = run;
    }

    @OptionArgument(longName="rep", argumentName="url")
    @Description("Specifies a module repository.")
    public void setRepo(List<String> repo) {
        this.repo = repo;
    }

    @Option(longName="d")
    @Description("Disables the default module repositories and source directory.")
    public void setDisableDefault(boolean disableDefault) {
        this.disableDefault = disableDefault;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ArrayList<String> argList = new ArrayList<String>();
        argList.addAll(Arrays.asList(new String[]{
                "-mp", System.getProperty("celon.runtime.repo"), 
                "ceylon.runtime",
                "+executable", "ceylon.modules.jboss.runtime.JBossRuntime"}));
        
        if (run != null) {
            argList.add("-run");
            argList.add(run);
        }
        
        if (disableDefault) {
            argList.add("-d");
        }
        
        for (String repo : this.repo) {
            argList.add("-rep");
            argList.add(repo);
        }
        
        argList.add(moduleNameOptVersion);
                
        try {
            System.out.println("Calling org.jboss.modules.Main.main with arguments " + argList);
            org.jboss.modules.Main.main(argList.toArray(new String[argList.size()]));
        } catch (Error err) {
            throw err;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);   
        }
    }

    
}