package internseason.scheduler.algorithm.event;

import java.util.List;

public interface AlgorithmEventListener {
    void schedulesGenerated(Integer parentHashcode, List<Integer> childHashcodes);
}
