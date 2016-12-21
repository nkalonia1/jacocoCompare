package data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nkalonia1 on 12/20/16.
 */
public class ExecDiff {
    private final int _HIT = 0, _MISSED = 1;
    private long _id;
    private String _name;
    private int _probe_count;

    private int[][] _common;
    private Set<IntExecutionData> _results

    private ExecDiff(long id, String name, int probe_count, Set<IntExecutionData> s) {
        _id = id;
        _name = name;
        _probe_count = probe_count;
        _results = new HashSet<IntExecutionData>();
    }




    public static ExecDiff diff(IntExecutionData first, IntExecutionData second, IntExecutionData... rest) {
        // Get basic info
        ExecDiff out;
        IntExecutionData root = null;
        int start = 0;
        if (first == null) {
            if (second == null) {
                for (IntExecutionData d : rest) {
                     if (d != null) {
                         root = d;
                         break;
                     }
                }
                if (root == null) {
                    throw new IllegalArgumentException("There needs to be at least one non-null argument");
                }
            } else root = second;
        } else root = first;

        // Assert compatability
        if (first != null) root.assertCompatability(first);
        if (second != null) root.assertCompatability(second);
        for (IntExecutionData d : rest) {
            if (d != null) root.assertCompatability(d);
        }

        // Create the output
        out = new ExecDiff(root.getId(), root.getName(), root.getProbes().length);
    }
}
