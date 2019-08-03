package internseason.scheduler;

public class Config {
    private String inputDotFile;
    private int numberOfProcessors;
    private int numberOfCores;
    private boolean visualisationEnabled;
    private String outputFileName;


    public Config(String inputDotFile, int numberOfProcessors, int numberOfCores,  boolean visualisationEnabled, String outputFileName) {
        this.inputDotFile = inputDotFile;
        this.numberOfProcessors = numberOfProcessors;
        this.numberOfCores = numberOfCores;
        this.visualisationEnabled = visualisationEnabled;
        this.outputFileName = outputFileName;
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

    public void setInputDotFile(String inputDotFile) {
        this.inputDotFile = inputDotFile;
    }

    public void setNumberOfProcessors(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) {
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
}
