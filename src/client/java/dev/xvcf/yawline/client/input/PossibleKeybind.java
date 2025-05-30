package dev.xvcf.yawline.client.input;

public class PossibleKeybind {

    private final String command;
    private final String description;

    public PossibleKeybind(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

}
