import data.IntExecutionDataStore;
import org.jacoco.core.data.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by nkalonia1 on 12/30/16.
 */
public class Main {
    /**
     * Performs a File Tree walk starting at a given Path. Return a collection of all the
     * Exec files found during the walk.
     */
    public static Collection<IntExecutionDataStore> getExecs(Path p) {
        final Collection<IntExecutionDataStore> out = new LinkedList<IntExecutionDataStore>();
        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

          @Override
          public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) {
              String f_name = p.getFileName().toString();
              int i = f_name.lastIndexOf('.');
              if (i < 0) return FileVisitResult.CONTINUE;
              if (!f_name.substring(i).equals(".exec")) return FileVisitResult.CONTINUE;
              try {
                  final IntExecutionDataStore store = new IntExecutionDataStore(p);        // Create the ExecutionDataStore
                  FileInputStream in = new FileInputStream(p.toFile());             // Make a FileInputStream to read the Exec file
                  ExecutionDataReader reader = new ExecutionDataReader(in);         // Create the Exec file reader
                  reader.setExecutionDataVisitor(new IExecutionDataVisitor() {      // Set the Exec file reader to put any found data into the ExecutionDataStore
                      @Override
                      public void visitClassExecution(final ExecutionData data) {
                          store.put(data);
                      }
                  });
                  reader.setSessionInfoVisitor(new ISessionInfoVisitor() {
                      @Override
                      public void visitSessionInfo(SessionInfo info) {

                      }
                  });
                  reader.read();
                  in.close();
                  out.add(store);
              } catch (IOException ioe) {
                  System.err.println(ioe);
              }
              return FileVisitResult.CONTINUE;
          }
        };
        try {
            Files.walkFileTree(p, visitor);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
        return out;
    }

    public static void main(String[] args) {
        Path cwd = Paths.get(".");
        if (args.length > 1) cwd = Paths.get(".");
        Collection<IntExecutionDataStore> execs = getExecs(cwd);

        // Aggregate all of the stores to a single IntExecutionData without messing with the stores
        IntExecutionDataStore total = new IntExecutionDataStore();
        for (IntExecutionDataStore store : execs) {
            total.put(store);
        }

        // For each ExecutionData in the collection, check if subtracting it from the IntExecutionDataStore results in probes being unvisited
        for (IntExecutionDataStore store : execs) {
            Path source = store.getSource();
            Collection<String> result = total.subtract(store);
            total.put(store);

            System.out.println(source.subpath(0, source.getNameCount() - 3).toString() + ":");
            for (String s : result) {
                System.out.println("\t" + s);
            }
            System.out.println();
        }
    }
}
