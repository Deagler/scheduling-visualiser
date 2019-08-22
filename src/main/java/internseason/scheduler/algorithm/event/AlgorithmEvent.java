package internseason.scheduler.algorithm.event;

import java.util.List;
import java.util.Set;

public interface AlgorithmEvent {
    void fireSchedulesGenerated(Integer parentHashcode, Set<Integer> childHashcodes);
}
