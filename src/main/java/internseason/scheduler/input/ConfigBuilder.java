package internseason.scheduler.input;


public class ConfigBuilder {
    private Config config;

    public ConfigBuilder(String inputDotFile, int numberOfProcessors) throws CLIException {
        config = this.getDefaultConfig(inputDotFile, numberOfProcessors);
        String inputFileWithoutExtension = inputDotFile.substring(0, inputDotFile.length() -4);
        String defaultOutputFileName = inputFileWithoutExtension + "-output.dot";

        this.setOutputFileName(defaultOutputFileName);

    }

    public void setVisualisationEnabled(boolean visualisationEnabled) {
        config.setVisualisationEnabled(visualisationEnabled);
    }

    public void setNumberOfCores(int numberOfCores) throws CLIException {
       config.setNumberOfCores(numberOfCores);
    }

    public void setOutputFileName(String outputFileName) {
        config.setOutputFileName(outputFileName);
    }

    public Config build() {
        return config;
    }


    private Config getDefaultConfig(String inputDotFile, int numberOfProcessors) throws CLIException {
        return new Config(inputDotFile, numberOfProcessors,1,  false, "INPUT-output.dot");
    }
}
