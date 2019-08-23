package internseason.scheduler.algorithm.event;

import java.util.List;

public interface AlgorithmEvent {
    void fireSchedulesGenerated(Integer parentHashcode, List<Integer> childHashcodes);
}
