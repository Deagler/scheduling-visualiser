package internseason.scheduler.algorithm;


/**
 * Factory pattern used to create Concrete Algorithm objects
 */
public class AlgorithmFactory {
    public static BaseAlgorithm getAlgorithm(AlgorithmType algorithmType, int numCores) {
        BaseAlgorithm algorithm = algorithmType.getInstance();
        return algorithm;
    }
}
