package internseason.scheduler.algorithm;

public enum AlgorithmType {
    A_STAR_ALGORITHM {
        @Override
        public BaseAlgorithm getInstance(){
            return new AStarAlgorithm();
        }
    },
    BASIC_ALGORITHM {
        @Override
        public BaseAlgorithm getInstance() {
            return new BasicAlgorithm();
        }
    };

    public abstract BaseAlgorithm getInstance();

}