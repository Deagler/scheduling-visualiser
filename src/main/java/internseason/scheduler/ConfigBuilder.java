package internseason.scheduler;

public class ConfigBuilder {
    private Config config;

    public ConfigBuilder(String inputDotFile, int numberOfProcessors) {
        config = this.getDefaultConfig(inputDotFile, numberOfProcessors);
        String defaultOutputFileName = inputDotFile.replaceAll("\\.dot", "") + "-output.dot";
        // TODO: ^ Make this better.
        this.setOutputFileName(defaultOutputFileName);

    }

    public void setVisualisationEnabled(boolean visualisationEnabled) {
        config.setVisualisationEnabled(visualisationEnabled);
    }

    public void setNumberOfCores(int numberOfCores) {
       config.setNumberOfCores(numberOfCores);
    }

    public void setOutputFileName(String outputFileName) {
        config.setOutputFileName(outputFileName);
    }

    public Config build() {
        return config;
    }


    private Config getDefaultConfig(String inputDotFile, int numberOfProcessors) {
        return new Config(inputDotFile, numberOfProcessors,1,  false, "INPUT-output.dot");
    }
}
