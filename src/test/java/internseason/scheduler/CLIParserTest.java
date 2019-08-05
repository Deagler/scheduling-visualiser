package internseason.scheduler;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CLIParserTest {
    private CLIParser parser;

    @Before
    public void setup() {
        parser = new CLIParser();
    }

    @Test
    public void testMissingArguments() {

        String[] args = {};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Must provide an input file and the number of processors.", e.getMessage());
        }
    }

    @Test
    public void testMissingProcessorArgument() {

        String[] args = {"input.dot"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Must provide an input file and the number of processors.", e.getMessage());
        }
    }

    @Test
    public void testInvalidInputFile() {

        String[] args = {"input", "1"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Invalid Input File, Must be a .dot file", e.getMessage());
        }

    }

    @Test
    public void testNonIntegerProcessorValue() {

        String[] args = {"input.dot", "a"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Number of processors (P) must be an integer", e.getMessage());
        }

    }

    @Test
    public void testProcessorValueIsZero() {

        String[] args = {"input.dot", "0"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Number of processors must be more than 0", e.getMessage());
        }


    }

    @Test
    public void testProcessorValueIsNegative() {

        String[] args = {"input.dot", "-1"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Number of processors must be more than 0", e.getMessage());
        }


    }


    @Test
    public void testNonIntegerCoresValue() {

        String[] args = {"input.dot", "1", "-p", "a"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("The number of cores must be a valid positive integer", e.getMessage());
        }

    }

    @Test
    public void testCoresValueIsZero() {

        String[] args = {"input.dot", "1", "-p", "0"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("The number of cores must be a valid positive integer", e.getMessage());
        }

    }

    @Test
    public void testCoresValueIsOne() {

        String[] args = {"input.dot", "1", "-p", "-1"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("The number of cores must be a valid positive integer", e.getMessage());
        }

    }

    @Test
    public void testNoCoresValue() {

        String[] args = {"input.dot", "1", "-p"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Malformed Arguments", e.getMessage());
        }

    }

    @Test
    public void testDefaultConfig() throws CLIException {

        String[] args = {"input.dot", "1"};

        Config config = parser.parse(args);

        assertEquals("input.dot", config.getInputDotFile());
        assertEquals(1, config.getNumberOfProcessors());
        assertEquals(1, config.getNumberOfCores());
        assertEquals(false, config.isVisualisationEnabled());
        assertEquals("input-output.dot", config.getOutputFileName());

    }

    @Test
    public void testVisualisationEnabled() throws CLIException {

        String[] args = {"input.dot", "1", "-v"};

        Config config = parser.parse(args);

        assertEquals("input.dot", config.getInputDotFile());
        assertEquals(1, config.getNumberOfProcessors());
        assertEquals(1, config.getNumberOfCores());
        assertEquals(true, config.isVisualisationEnabled());
        assertEquals("input-output.dot", config.getOutputFileName());

    }

    @Test
    public void testMalformedOptions() {

        String[] args = {"INPUT.dot", "1", "-o"};

        try {
            parser.parse(args);
            fail();
        } catch (CLIException e) {
            assertEquals("Malformed Arguments", e.getMessage());
        }
    }


    @Test
    public void testOutputOption() throws CLIException {


        String[] args1 = {"input.dot", "1", "-o", "output.dot"};

        Config config = parser.parse(args1);

        assertEquals("input.dot", config.getInputDotFile());
        assertEquals(1, config.getNumberOfProcessors());
        assertEquals(1, config.getNumberOfCores());
        assertEquals(false, config.isVisualisationEnabled());
        assertEquals("output.dot", config.getOutputFileName());

    }


    @Test
    public void testFullConfiguration() throws CLIException {
        String[] args = {"real.dot", "4", "-o", "output.dot", "-p", "12", "-v"};

        Config config = parser.parse(args);

        assertEquals("real.dot", config.getInputDotFile());
        assertEquals(4, config.getNumberOfProcessors());
        assertEquals(12, config.getNumberOfCores());
        assertEquals(true, config.isVisualisationEnabled());
        assertEquals("output.dot", config.getOutputFileName());
    }


}
