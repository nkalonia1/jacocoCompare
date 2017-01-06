package data;

/**
 * Read-Only ExecutionData interface.
 */
public interface RExecutionData {

    long getId();

    String getName();

    boolean[] getProbes();

    boolean probeAt(int index);

    int getProbeCount();

    ExecutionInfo getInfo();

    boolean hasHits();
}
