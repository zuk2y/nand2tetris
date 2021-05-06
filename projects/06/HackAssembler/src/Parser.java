import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private List<String> commands;

    private String currentCommand;

    private String nextCommand;

    public Parser(Path inputFile) {
        try (Stream<String> stream = Files.lines(inputFile)) {
            commands = stream
                .map(i -> i.replaceAll(" ", "")) // remove space
                .map(i -> i.replaceAll("//.*$", "")) // remove comment
                .filter(i -> !(i.isBlank())) // remove blank lines
                .collect(Collectors.toList());
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        currentCommand = "";
        if (commands.isEmpty()) {
            nextCommand = "";
        } else {
            nextCommand = commands.get(0);
            commands.remove(0);
        }
    }

    public boolean hasMoreCommands() {
        return !nextCommand.isEmpty();
    }

    public void advance() {
        currentCommand = nextCommand;
        if (commands.isEmpty()) {
            nextCommand = "";
        } else {
            nextCommand = commands.get(0);
            commands.remove(0);
        }
    }

    public CommandType commandType() {
        if (Pattern.matches("^@[a-zA-Z0-9_.$:]+", currentCommand)) {
            return CommandType.A_COMMAND;
        } else if(Pattern.matches("^\\([a-zA-Z0-9_.$:]+\\)$", currentCommand)) {
            return CommandType.L_COMMAND;
        } else if(Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand) ||
            Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+$", currentCommand) ||
            Pattern.matches("[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)){
            return CommandType.C_COMMAND;
        } else {
            throw new IllegalArgumentException("command format is invalid: [" + currentCommand + "]");
        }
    }

    public String symbol() {
        if (commandType() == CommandType.A_COMMAND) {
            return currentCommand.substring(1);
        } else {
            return currentCommand.substring(1, currentCommand.length() - 1);
        }
    }

    public String comp() {
        String[] split = currentCommand.split("[=,;]");
        if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return split[1];
        } else if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+$", currentCommand)) {
            return split[1];
        } else if (Pattern.matches("[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return split[0];
        } else {
          throw new IllegalArgumentException("command format is invalid: [" + currentCommand + "]");
        }
    }

    public String dest() {
        String[] split = currentCommand.split("[=,;]");
        if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return split[0];
        } else if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+$", currentCommand)) {
            return split[0];
        } else if (Pattern.matches("[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return "null";
        } else {
          throw new IllegalArgumentException("command format is invalid: [" + currentCommand + "]");
        }
    }

    public String jump() {
        String[] split = currentCommand.split("[=,;]");
        if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return split[2];
        } else if (Pattern.matches("^[AMD]+=[AMD10+\\-!&|]+$", currentCommand)) {
            return "null";
        } else if (Pattern.matches("[AMD10+\\-!&|]+;[JGTEQGLNMP]+$", currentCommand)) {
            return split[1];
        } else {
          throw new IllegalArgumentException("command format is invalid: [" + currentCommand + "]");
        }
    }

    public enum CommandType{
        A_COMMAND,
        C_COMMAND,
        L_COMMAND;
    }
}
