package internseason.scheduler.algorithm;



public class AlgorithmFactory {
    public static BaseAlgorithm getAlgorithm(AlgorithmType algorithmType, int numCores) {
        BaseAlgorithm algorithm = algorithmType.getInstance();
        return algorithm;
    }
}
