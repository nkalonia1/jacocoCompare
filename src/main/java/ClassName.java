/**
 * An immutable wrapper for a class' name String
 */
public class ClassName {
    private String _name;

    public ClassName(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }
}
