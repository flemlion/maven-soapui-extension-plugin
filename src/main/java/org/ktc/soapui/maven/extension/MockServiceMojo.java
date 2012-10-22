/*
 * Copyright 2012 Thomas Bouffard (redfish4ktc)
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
 *
 */

package org.ktc.soapui.maven.extension;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.SoapUIProMockServiceRunner;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

public class MockServiceMojo extends AbstractMojo {
    private String projectFile;
    private String mockService;
    private String path;
    private String port;
    private String settingsFile;
    private boolean noBlock;
    private boolean skip;
    private String projectPassword;
    private String settingsPassword;
    private String[] globalProperties;
    private String[] projectProperties;
    private boolean saveAfterRun;
    private Properties soapuiProperties;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if ((skip) || (System.getProperty("maven.test.skip", "false").equals("true"))) {
            return;
        }
        if (projectFile == null) {
            throw new MojoExecutionException("soapui-project-file setting is required");
        }

        SoapUIProMockServiceRunner runner = new SoapUIProMockServiceRunner("soapUI Pro " + SoapUI.SOAPUI_VERSION + " Maven2 MockService Runner");

        runner.setProjectFile(projectFile);

        if (mockService != null) {
            runner.setMockService(mockService);
        }
        if (path != null) {
            runner.setPath(path);
        }
        if (port != null) {
            runner.setPort(port);
        }
        if (settingsFile != null) {
            runner.setSettingsFile(settingsFile);
        }
        runner.setBlock(!noBlock);
        runner.setSaveAfterRun(saveAfterRun);

        if (projectPassword != null) {
            runner.setProjectPassword(projectPassword);
        }
        if (settingsPassword != null) {
            runner.setSoapUISettingsPassword(settingsPassword);
        }
        if (globalProperties != null) {
            runner.setGlobalProperties(globalProperties);
        }
        if (projectProperties != null)
            runner.setProjectProperties(projectProperties);
        if (this.soapuiProperties != null && !this.soapuiProperties.isEmpty()) {
            for (Object key : this.soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }
        }
        try {
            runner.run();
        } catch (Exception e) {
            getLog().error(e.toString());
            throw new MojoFailureException(this, "SoapUI MockService(s) failed", e.getMessage());
        }
    }
}