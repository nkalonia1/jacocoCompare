package data;

import org.jacoco.core.data.ExecutionData;

import static java.lang.String.format;

/**
 * An alternate version of ExecutionData where probes are stored as ints instead of booleans
 */
public class IntExecutionData {
    private ExecutionInfo _exec_info;
    private int[] _probes;

    public IntExecutionData(ExecutionData data) {
        boolean[] probes = data.getProbes();
        _probes = new int[probes.length];
        for (int i = 0; i < probes.length; i++) {
            _probes[i] = probes[i] ? 1 : 0;
        }
        _exec_info = new ExecutionInfo(data.getId(), data.getName(), probes.length);
    }

    public IntExecutionData(ExecutionInfo info) {
        _exec_info = info;
        _probes = new int[_exec_info.getProbeCount()];
    }


    /**
     * The add functions add the values of an (Int)ExecutionData to the current data.
     * The functions return a boolean stating whether the addition caused more instructions to be covered.
     **/
    public boolean add(IntExecutionData other) {
        assertCompatability(other);
        boolean changed = false;
        for (int i = 0; i < _exec_info.getProbeCount(); i++) {
            _probes[i] += other._probes[i];
            if (_probes[i] == other._probes[i]) changed = true;
        }
        return changed;
    }
    
    public boolean add(ExecutionData other) {
        assertCompatability(other);
        boolean changed = false;
        boolean[] probes = other.getProbes();
        for (int i = 0; i < _exec_info.getProbeCount(); i++) {
            _probes[i] += probes[i] ? 1 : 0;
            if (probes[i] && _probes[i] == 1) changed = true;
        }
        return changed;
    }


    /**
     * The subtract functions subtract the values of an (Int)ExecutionData from the current data.
     * The functions return a boolean stating whether the subtraction caused less instructions to be covered.
     **/
    public boolean subtract(IntExecutionData other) {
        assertCompatability(other);
        boolean changed = false;
        for (int i = 0; i < _exec_info.getProbeCount(); i++) {
            if (_probes[i] > 0) {
                if (other._probes[i] >= _probes[i]) {
                    changed = true;
                    _probes[i] = 0;
                } else {
                    _probes[i] -= other._probes[i];
                }
            } else {
                _probes[i] = 0;
            }
        }
        return changed;
    }

    public boolean subtract(ExecutionData other) {
        assertCompatability(other);
        boolean changed = false;
        boolean[] probes = other.getProbes();
        for (int i = 0; i < _exec_info.getProbeCount(); i++) {
            if (_probes[i] == 1 && probes[i]) {
                changed = true;
                _probes[i] = 0;
                continue;
            }
            _probes[i] = _probes[i] == 0 ? 0 : _probes[0] - (probes[i] ? 1 : 0);
        }
        return changed;
    }

    public void assertCompatability(ExecutionInfo other_info) throws IllegalStateException {
        if (_exec_info.getId() != other_info.getId()) {
            throw new IllegalStateException(format(
                    "Different ids (%016x and %016x).", _exec_info.getId(),
                    other_info.getId()));
        }
        if (!_exec_info.getName().equals(other_info.getName())) {
            throw new IllegalStateException(format(
                    "Different class names %s and %s for id %016x.", _exec_info.getName(),
                    other_info.getName(), other_info.getId()));
        }
        if (_exec_info.getProbeCount() != other_info.getProbeCount()) {
            throw new IllegalStateException(format(
                    "Incompatible execution data for class %s with id %016x.",
                    other_info.getName(), other_info.getId()));
        }
        
    }

    public void assertCompatability(ExecutionData other) throws IllegalStateException {
        assertCompatability(new ExecutionInfo(other.getId(), other.getName(), other.getProbes().length));
    }

    public void assertCompatability(IntExecutionData other) throws IllegalStateException {
        assertCompatability(other.getInfo());
    }

    public void reset() {
        for (int i = 0; i < _probes.length; i++) { _probes[i] = 0; }
    }

    public ExecutionInfo getInfo() {
        return _exec_info;
    }

    public int[] getProbes() {
        int[] out = new int[_probes.length];
        System.arraycopy(_probes, 0, out, 0, _probes.length);
        return out;
    }

    public ExecutionData toExecutionData() {
        boolean[] probes = new boolean[_probes.length];
        for (int i = 0; i < _probes.length; i++) {
            probes[i] = _probes[i] > 0;
        }
        return new ExecutionData(_exec_info.getId(), _exec_info.getName(), probes);
    }

    /**
     * Compares this class to another IntExecutionData. Returns a new IntExecutionData that represents the
     * intersection of these two objects. This function will not modify either object.
     * To be more specific the output contains the minumum value of any probe in the two IntExecutionData.
     */
    public IntExecutionData intersect(IntExecutionData other) throws IllegalStateException {
        assertCompatability(other);
        IntExecutionData out = new IntExecutionData(_exec_info);
        for (int i = 0; i < _probes.length; i++) {
            out._probes[i] = Math.min(_probes[i], other._probes[i]);
        }
        return out;
    }

}
