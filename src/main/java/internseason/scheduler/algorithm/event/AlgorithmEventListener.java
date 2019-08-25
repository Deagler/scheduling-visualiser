package internseason.scheduler.algorithm.event;

import java.util.List;
import java.util.Set;

public interface AlgorithmEventListener {
    void schedulesGenerated(Integer parentHashcode, Set<Integer> childHashcodes);
}
