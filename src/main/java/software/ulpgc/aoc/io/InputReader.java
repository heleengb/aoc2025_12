package software.ulpgc.aoc.io;

import java.io.IOException;
import java.util.List;

public interface InputReader {
    List<String> readContent() throws IOException;
}