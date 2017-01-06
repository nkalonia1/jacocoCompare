package data;

import org.jacoco.core.data.ExecutionData;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * New ExecutionData. It is similar to JaCoCo's ExecutionData and contains some
 * additional operational methods. This class can be converted to and from an
 * ExecutionData object without any loss of information.
 */
public class NExecutionData implements RExecutionData, Cloneable {

    private final long _id;
    private final String _name;
    private final boolean[] _probes;

    public static NExecutionData create(ExecutionData data) {
        return new NExecutionData(data.getId(), data.getName(), data.getProbes());
    }

    public NExecutionData(final long id, final String name, final boolean[] probes) {
        _id = id;
        _name = name;
        _probes = probes;
    }

    public NExecutionData(final long id, final String name, final int probe_count) {
        _id = id;
        _name = name;
        _probes = new boolean[probe_count];
    }

    public NExecutionData(final ExecutionInfo info) {
        this(info.getId(), info.getName(), info.getProbeCount());
    }

    @Override
    public long getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean[] getProbes() {
        boolean[] out =  new boolean[_probes.length];
        System.arraycopy(_probes, 0, out, 0, _probes.length);
        return out;
    }

    @Override
    public boolean probeAt(int index) {
        return _probes[index];
    }

    @Override
    public int getProbeCount() {
        return _probes.length;
    }

    @Override
    public ExecutionInfo getInfo() {
        return new ExecutionInfo(_id, _name, _probes.length);
    }

    @Override
    public boolean hasHits() {
        for (boolean p : _probes) {
            if (p) return true;
        }
        return false;
    }

    public void reset() {
        Arrays.fill(_probes, false);
    }

    /**
     * "Adds" a RExecutionData object to this. Essentially performs:
     * A or B
     * Where A is this' probes and B is the given parameter's probes.
     */
    public void add(final RExecutionData other) {
        assertCompatibility(other);
        for (int i = 0; i < _probes.length; i++) {
            _probes[i] = _probes[i] || other.probeAt(i);
        }
    }

    /**
     * "Subtracts" a RExecutionData object from this. Essentially performs:
     * A and not B
     * Where A is this' probes and B is the given parameter's probes.
     */
    public void sub(final RExecutionData other) {
        assertCompatibility(other);
        for (int i = 0; i < _probes.length; i++) {
            _probes[i] = _probes[i] && !other.probeAt(i);
        }
    }

    /**
     * "Intersects" a RExecutionData with this. Essentially performs:
     * A and B
     * Where A is this' probes and B is the given parameter's probes.
     */
    public void intersect(final RExecutionData other) {
        assertCompatibility(other);
        for (int i = 0; i < _probes.length; i++) {
            _probes[i] = _probes[i] && other.probeAt(i);
        }
    }

    private void assertCompatibility(long id, String name, int probe_count) throws IllegalStateException {
        if (_id != id) {
            throw new IllegalStateException(format(
                    "Different ids (%016x and %016x).", _id, id));
        }
        if (!_name.equals(name)) {
            throw new IllegalStateException(format(
                    "Different class names %s and %s for id %016x.", _name, name, id));
        }
        if (_probes.length != probe_count) {
            throw new IllegalStateException(format(
                    "Incompatible execution data for class %s with id %016x.", name, id));
        }
    }

    /**
     * Asserts that this NExecutionData is compatible with the provided ExecutionInfo.
     * This method checks if the ExecutionInfo matches the information contained in this
     * NExecutionData and throws an IllegalStateException if there is a mismatch. The
     * purpose of this function is to catch the (very, very unlikely) event of an id
     * collision. All functions that involve multiple ExecutionData should
     * initially call this method.
     */
    public void assertCompatibility(ExecutionInfo info) throws IllegalStateException {
        assertCompatibility(info.getId(), info.getName(), info.getProbeCount());
    }

    /**
     * Asserts that this NExecutionData is compatible with the provided RExecutionData.
     * This method checks if both ExecutionData contain the same information and
     * throws an IllegalStateException in the case of a mismatch. The purpose of this
     * method is to catch the (very, very unlikely) event of an id collision. All
     * functions that involve multiple ExecutionData should initially call this
     * method.
     */
    public void assertCompatibility(RExecutionData data) throws IllegalStateException {
        assertCompatibility(data.getId(), data.getName(), data.getProbeCount());
    }

    /**
     * Creates an ExecutionData version of this object. Any modifications to the
     * returned ExecutionData will be reflected in this object.
     */
    public ExecutionData toExecutionData() {
        return new ExecutionData(_id, _name, _probes);
    }

    @Override
    public String toString() {
        return String.format("ExecutionData[name=%s, id=%016x]", _name, _id);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new NExecutionData(_id, _name, _probes);
    }
}
