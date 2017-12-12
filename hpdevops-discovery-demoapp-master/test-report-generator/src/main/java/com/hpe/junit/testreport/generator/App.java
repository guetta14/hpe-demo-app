package com.hpe.junit.testreport.generator;

import org.apache.commons.cli.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static String suite_class_name = "com.hpe.junit.testreport.generator.AppTest";
    public static String blameCommitterFile = "No File Exception";
    public static String failedTestException;

    public enum TestResult {
        FAILED, PASSED, SKIPPED
    }

    public static class TestConfig {
        public TestResult testResult;
        public int        count;

        public TestConfig(TestResult testResult, int count) {
            this.testResult = testResult;
            this.count = count;
        }
    }

    public static void main(String[] args) {

        //Assert.fail();

        final Options options = new Options();

        options
                .addOption(Option
                        .builder()
                        .argName("blameCommitterFile")
                        .longOpt("blameCommitterFile")
                        .desc("blameCommitterFile")
                        .hasArg()
                        .build())
                .addOption(Option
                        .builder()
                        .argName("help")
                        .longOpt("help")
                        .desc("show help")
                        .build())
                .addOption(Option
                        .builder()
                        .argName("execOrder")
                        .longOpt("execOrder")
                        .desc("ex: passed:10,failed:32,skipped:12,failed:12,...")
                        .hasArg()
                        .required()
                        .build())
                .addOption(Option.builder()
                        .argName("testDuration")
                        .longOpt("testDuration")
                        .desc("the duration in ms of all executed test, this will be divided for per/test values")
                        .required()
                        .hasArg()
                        .build())
                .addOption(Option.builder()
                        .argName("suiteClassName")
                        .longOpt("suiteClassName")
                        .desc("ex: com.hpe.junit.testreport.generator.AppTest")
                        .hasArg().build())
                .addOption(Option.builder().argName("resultXmlFolder")
                        .longOpt("resultXmlFolder")
                        .desc("ex: c:/filename/ (make sure to add a '/' at the end)")
                        .hasArg()
                        .build())
                .addOption(Option.builder().argName("jobBaseName")
                        .longOpt("jobBaseName")
                        .desc("ex:MegaJobAdi")
                        .hasArg()
                        .build()) ;

        // create the parser
        CommandLineParser parser = new DefaultParser();
        CommandLine line;

        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            printHelp(options);
            return;
        }

        if (line.hasOption("help")) {
            printHelp(options);
            System.exit(0);
        }

        List<TestConfig> execConfig = new ArrayList<>();

        if (line.hasOption("execOrder")) {
            String paramOrderString = line.getOptionValue("execOrder");
            String[] separated = paramOrderString.split(",");

            for (String s : separated) {
                String[] keyValString = s.split(":");
                if (keyValString.length != 2) {
                    throw new RuntimeException("Failed to parse execOrderParam");
                } else {
                    TestResult testResult = TestResult.valueOf(keyValString[0].toUpperCase());
                    int count = Integer.parseInt(keyValString[1]);
                    execConfig.add(new TestConfig(testResult, count));
                }

            }

        }

        int failedTestCount = getTotalCount(execConfig, TestResult.FAILED);
        int passedTestCount = getTotalCount(execConfig, TestResult.PASSED);
        int skippedTestCount = getTotalCount(execConfig, TestResult.SKIPPED);

        double testDuration = Double.valueOf(line.getOptionValue("testDuration"));
        double perTestDuration = testDuration / (failedTestCount + passedTestCount);
        String jobBaseName = line.getOptionValue("jobBaseName");
        String resultXmlFolder = line.getOptionValue("resultXmlFolder", "");
        String xmlFolderWithNoBackSlash = resultXmlFolder.substring(0, resultXmlFolder.length()- 1);
        String jobName = null;
        if(jobBaseName != null) {
             jobName = xmlFolderWithNoBackSlash.substring(xmlFolderWithNoBackSlash.lastIndexOf("/") + 1) + "_" + jobBaseName;
        } else {
             jobName = xmlFolderWithNoBackSlash.substring(xmlFolderWithNoBackSlash.lastIndexOf("/") + 1);
        }


        if (line.hasOption("suiteClassName")) {
            suite_class_name = line.getOptionValue("suiteClassName");
        }

        if (line.hasOption("blameCommitterFile")) {
            blameCommitterFile = line.getOptionValue("blameCommitterFile");
        }

        failedTestException = "Exception in thread \"main\" java.lang.AssertionError\n" +
                              "\tat org.junit.Assert.fail(Assert.java:86)\n" +
                              "\tat org.junit.Assert.fail(Assert.java:95)\n" +
                              "\t"+blameCommitterFile+".main(App.java:41)\n" +
                              "Failed to execute event 'Hovering <b title=\"//*[@data-aid='admin:business-rule-edit-dialog-action-tab-action']\">element</b> and click <b title=\"//*[@data-aid='admin:business-rule-edit-dialog-action-tab-action']\">this element</b>'\n";

        Integer testStartIndex = 1;

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();

            // root elements
            Element rootElement = doc.createElement("testsuite");
            doc.appendChild(rootElement);
            rootElement.setAttribute("name", suite_class_name);
            rootElement.setAttribute("tests", failedTestCount + passedTestCount + skippedTestCount + "");
            rootElement.setAttribute("failures", failedTestCount + "");
            rootElement.setAttribute("errors", 0 + "");
            rootElement.setAttribute("skipped", skippedTestCount + "");
            rootElement.setAttribute("time", testDuration + "");

            // Add empty props
            Element properties = doc.createElement("properties");
            rootElement.appendChild(properties);

            for (TestConfig testConfig : execConfig) {
                for (int i = 1; i <= testConfig.count; i++) {
                    rootElement.appendChild(createTestElement(doc, testConfig.testResult, testStartIndex, perTestDuration, jobName));
                    testStartIndex += 1;
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;

            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(resultXmlFolder + "TEST-" + suite_class_name + ".xml"));
            transformer.transform(source, result);

            // Output to console for testing
            if (resultXmlFolder != null && !resultXmlFolder.trim().isEmpty()) {
                System.out.println("File saved to location: " + resultXmlFolder + "TEST-" + suite_class_name + ".xml");
            } else {
                if (!resultXmlFolder.trim().isEmpty()) {
                    System.out.println("File saved to wd: " + "TEST-" + suite_class_name + ".xml");
                }
            }
            result = new StreamResult(System.out);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static Element createTestElement(Document doc, TestResult testResult, int testNo, double duration, String jobName) {
        Element element = doc.createElement("testcase");
        element.setAttribute("classname", suite_class_name);
        element.setAttribute("name",  "Test" + String.format("%05d", testNo) + "_" + jobName);
        element.setAttribute("time", duration + "");

        if (TestResult.FAILED == testResult) {
            Element failure = doc.createElement("failure");
            failure.setAttribute("type", "junit.framework.AssertionFailedError");
            failure.setTextContent(failedTestException);
            element.appendChild(failure);
        }

        if (TestResult.SKIPPED == testResult) {
            Element skipped = doc.createElement("skipped");
            element.appendChild(skipped);
        }

        return element;
    }

    private static int getTotalCount(List<TestConfig> configList, TestResult result) {
        int total = 0;
        for (TestConfig config : configList) {
            if (result == null || config.testResult == result) {
                total += config.count;
            }
        }
        return total;
    }

    private static void printHelp(Options options) {
        HelpFormatter help = new HelpFormatter();
        help.setWidth(140);
        help.printHelp("testreport-generator", options);
    }

}