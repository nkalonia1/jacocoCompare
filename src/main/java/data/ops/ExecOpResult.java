package data.ops;

import data.ExecutionInfo;

/**
 * An immutable container (except under package scope) that holds the results from some operations in IntExecOps
 */
public class ExecOpResult {
    static int _HIT = 0, _MISS = 1;

    private ExecutionInfo _info;
    int[][] _results;

    ExecOpResult(ExecutionInfo info) {
        _info = info;
        _results = new int[2][info.getProbeCount()];
    }

    public int[] getHits() {
        int[] out = new int[_results[_HIT].length];
        System.arraycopy(_results[_HIT], 0, out, 0, _results[_HIT].length);
        return out;
    }

    public int[] getMisses() {
        int[] out = new int[_results[_MISS].length];
        System.arraycopy(_results[_MISS], 0, out, 0, _results[_MISS].length);
        return out;
    }
}
