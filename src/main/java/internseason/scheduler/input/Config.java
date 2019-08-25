package internseason.scheduler.input;

public class Config {
    private String inputDotFile;
    private int numberOfProcessors;
    private int numberOfCores;
    private boolean visualisationEnabled;
    private String outputFileName;


    public Config(String inputDotFile, int numberOfProcessors, int numberOfCores,  boolean visualisationEnabled, String outputFileName) throws CLIException {
        setInputDotFile(inputDotFile);
        setNumberOfProcessors(numberOfProcessors);
        setNumberOfCores(numberOfCores);
        setVisualisationEnabled(visualisationEnabled);
        setOutputFileName(outputFileName);
    }

    public String getInputDotFile() {
        return inputDotFile;
    }

    public int getNumberOfProcessors() {
        return numberOfProcessors;
    }

    public boolean isVisualisationEnabled() {
        return visualisationEnabled;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setInputDotFile(String inputDotFile)  {


        this.inputDotFile = inputDotFile;
    }

    public void setNumberOfProcessors(int numberOfProcessors) throws CLIException {
        if(numberOfProcessors <= 0) {
            throw new CLIException("Number of processors must be more than 0");
        }

        this.numberOfProcessors = numberOfProcessors;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) throws CLIException {
        if (numberOfCores <= 0) {
            throw new CLIException("The number of cores must be a valid positive integer");
        }
        this.numberOfCores = numberOfCores;
    }

    public void setVisualisationEnabled(boolean visualisationEnabled) {
        this.visualisationEnabled = visualisationEnabled;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public String toString() {
        return "Config{" +
                "inputDotFile='" + inputDotFile + '\'' +
                ", numberOfProcessors=" + numberOfProcessors +
                ", numberOfCores=" + numberOfCores +
                ", visualisationEnabled=" + visualisationEnabled +
                ", outputFileName='" + outputFileName + '\'' +
                '}';
    }


    public static boolean isValidInputFile(String inputFile) {
        return inputFile.endsWith(".dot");
    }
}
