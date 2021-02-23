import java.io.FileReader;
import java.io.FilterReader;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {

    private List<String> commands;

    private String currentCommand;

    private String nextCommand;

    public Parser(Path inputFile){
        commands = Files.readAllLines(inputFile);
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
        if (Pattern.matches("^@.+", currentCommand)) {
            return CommandType.A_COMMAND;
        } else if(Pattern.matches(".+=.+;.+", currentCommand)){
            return CommandType.C_COMMAND;
        } else if(Pattern.matches("^\\(.+\\)$", currentCommand)) {
            return CommandType.L_COMMAND;
        } else {
            throw new IllegalArgumentException("command format is invalid: [" + currentCommand + "]");
        }
    }

    public Stirng symbol() {
        if (commandType() == CommandType.A_COMMAND) {
            return currentCommand.substring(2);
        } else {
            return currentCommand.substring(2, currentCommand.length());
        }
    }

    public String comp() {
        String[] split = currentCommand.split("[=,;]");
        return split[0];
    }

    public String dest() {
        String[] split = currentCommand.split("[=,;]");
        return split[1];
    }

    public String jump() {
        String[] split = currentCommand.split("[=,;]");
        return split[2];
    }

    public enum CommandType{
        A_COMMAND,
        C_COMMAND,
        L_COMMAND;
    }
}
