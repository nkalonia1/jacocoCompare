package data;

import static java.lang.String.format;

/**
 * An implementation of RExecutionData where all probe values are false. This saves
 * a little bit of memory by not needing to store a probe array.
 */
public class EmptyExecutionData implements RExecutionData {

    private ExecutionInfo _info;

    public EmptyExecutionData(ExecutionInfo info) {
        _info = info;
    }

    @Override
    public long getId() {
        return _info.getId();
    }

    @Override
    public String getName() {
        return _info.getName();
    }

    @Override
    public boolean[] getProbes() {
        return new boolean[getProbeCount()];
    }

    @Override
    public boolean probeAt(int index) {
        return false;
    }

    @Override
    public int getProbeCount() {
        return _info.getProbeCount();
    }

    @Override
    public ExecutionInfo getInfo() {
        return _info;
    }

    @Override
    public boolean hasHits() {
        return false;
    }
}
