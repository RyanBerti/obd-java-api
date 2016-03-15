package com.github.pires.obd.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Container for multiple {@link com.github.pires.obd.commands.ObdMultiCommand} instances.
 *
 * @author pires
 * @version $Id: $Id
 */
public class ObdMultiCommand {

    private ArrayList<ObdCommand> commands;

    /**
     * Default ctor.
     */
    public ObdMultiCommand() {
        this.commands = new ArrayList<>();
    }

    /**
     * Add ObdCommand to list of ObdCommands.
     *
     * @param command a {@link com.github.pires.obd.commands.ObdCommand} object.
     */
    public void add(ObdCommand command) {
        this.commands.add(command);
    }

    /**
     * Removes ObdCommand from the list of ObdCommands.
     *
     * @param command a {@link com.github.pires.obd.commands.ObdCommand} object.
     */
    public void remove(ObdCommand command) {
        this.commands.remove(command);
    }

    /**
     * Iterate all commands, send them and read response.
     *
     * @param in  a {@link java.io.InputStream} object.
     * @param out a {@link java.io.OutputStream} object.
     * @throws java.io.IOException            if any.
     * @throws java.lang.InterruptedException if any.
     */
    public void sendCommands(InputStream in, OutputStream out)
            throws IOException, InterruptedException {
        for (ObdCommand command : commands)
            command.run(in, out);
    }

    /**
     * Loop through the ObdCommands associated with this ObdMultiCommand
     * and use the setConvertRawData setter to update the ObdCommand state
     *
     * @param convertRawData a boolean
     */
    public void setConvertRawData(boolean convertRawData) {
        for (ObdCommand command : commands)
            command.setConvertRawData(convertRawData);
    }

    /**
     * <p>getRawResult.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRawResult() {
        StringBuilder res = new StringBuilder();
        for (ObdCommand command : commands)
            res.append(command.getResult()).append(",");

        return res.toString();
    }

    /**
     * Take a comma separated set of raw results generated by this ObdMultiCommand
     * and convert it to a comma separated set of formatted results using the
     * underlying ObdCommand's convertRawData methods; number of fields in rawResult
     * should be equal to number of ObdCommands in this ObdMultiCommand
     *
     * @param rawResult a comma separated list of raw results
     * @return a comma separated list of formatted results
     */
    public String convertRawResultToFormattedResult(String rawResult) {
        StringBuilder res = new StringBuilder();

        int i=0;
        String[] rawResults = rawResult.split(",");
        for (String result : rawResults) {
            ObdCommand cmd = commands.get(i++);
            cmd.convertRawData(result);
            res.append(cmd.getFormattedResult()).append(",");
        }

        return res.toString();
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormattedResult() {
        StringBuilder res = new StringBuilder();
        for (ObdCommand command : commands)
            res.append(command.getFormattedResult()).append(",");

        return res.toString();
    }

    /**
     * Get the number of ObdCommands associated with this ObdMultiCommand
     *
     * @return int
     */

    public int getNumberCommands() {
        return commands.size();
    }


}
