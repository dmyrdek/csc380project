package csc380Project.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Align {

    private String paddingChar = "-";
    private int minSpacing = 5;

    protected Content content;

    public Align() {
        content = new Content();
    }

    /**
     * set the character that is used for values that don't exist.
     *
     * @param paddingChar padding char
     */
    public void setPaddingChar(String paddingChar) {
        this.paddingChar = paddingChar;
    }

    /**
     * the minimum amount of spaces between columns in the formated output.
     *
     * Must be at least 0, but a minimum of at least 1 is suggested.
     *
     * @param minSpacing minimum of spaces (must be at least 0)
     */
    public void setMinSpacing(int minSpacing) {
        if (minSpacing < 0) {
            throw new IllegalArgumentException("min spacing must be at least 0");
        }

        this.minSpacing = minSpacing;
    }

    public void addLine(int position, List<String> lineContent) {
        content.addLine(position, lineContent);
    }

    public void addLine(List<String> lineContent) {
        addLine(content.lineCount(), lineContent);
    }

    public void addLine(String... lineContent) {
        addLine(content.lineCount(), new ArrayList<>(Arrays.asList(lineContent)));
    }

    public void addLine(int position, String... lineContent) {
        addLine(position, new ArrayList<>(Arrays.asList(lineContent)));
    }

    public void addColumn(int position, List<String> columnContent) {
        content.addColumn(position, columnContent);
    }

    public void addColumn(List<String> columnContent) {
        addColumn(content.columnCount(), columnContent);
    }

    public void addColumn(String... columnContent) {
        addColumn(content.columnCount(), new ArrayList<>(Arrays.asList(columnContent)));
    }

    public void addColumn(int position, String... columnContent) {
        addColumn(position, new ArrayList<>(Arrays.asList(columnContent)));
    }

    /**
     * sets the value at the given column and row to the given content.
     *
     * If the value doesn't exist already, an IllegalArgumentException will be
     * thrown.
     *
     * @param column column
     * @param row row
     * @param value value
     */
    public void set(int column, int row, String value) {
        content.set(column, row, value);
    }

    /**
     * passes the formated string to consumer.
     *
     * To get the formated string, use toString.
     *
     * @param consumer consumer
     */
    public void output(Consumer<String> consumer) {
        consumer.accept(toString());
    }

    @Override
    public String toString() {
        List<Integer> maxContentLength = getMaxLengths();
        List<String> spaces = getSpaces(maxContentLength);

        StringBuilder out = new StringBuilder();
        for (Content.Line line : content.lines) {
            for (int columnIndex = 0; columnIndex < line.size(); columnIndex++) {
                String column = line.get(columnIndex);
                out.append(column);
                out.append(spaces.get(columnIndex)
                        .substring(0,
                                spaces.get(columnIndex).length()
                                        - column.length())); // align
            }
            out.append("\n");
        }
        return out.toString();
    }

    /**
     * returns a list of spaces of the given lengths.
     *
     * @param lengths lengths
     * @return list of spaces
     */
    private List<String> getSpaces(List<Integer> lengths) {
        List<String> spaces = new ArrayList<>();
        for (Integer length : lengths) {
            spaces.add(getSpaces(length));
        }
        return spaces;
    }

    /**
     * returns n spaces.
     *
     * @param n number of spaces
     * @return n spaces
     */
    private static String getSpaces(int n) {
        StringBuilder spaces = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    /**
     * returns a list containing the length of the longest string of each
     * column.
     *
     * @return lengths
     */
    private List<Integer> getMaxLengths() {
        List<Integer> maxContentLength = new ArrayList<>();
        for (int i = 0; i <= content.columnCount(); i++) {
            maxContentLength.add(0); // init max length with 0
        }

        for (Content.Line line : content.lines) {
            for (int columnIndex = 0; columnIndex < content.columnCount(); columnIndex++) {
                String column = line.get(columnIndex);
                if (column.length() > maxContentLength.get(columnIndex)) {
                    maxContentLength.set(columnIndex, column.length() + minSpacing);
                }
            }
        }
        return maxContentLength;
    }

    protected class Content {

        private List<Line> lines;

        public Content() {
            lines = new ArrayList<>();
        }

        /**
         * returns number of lines.
         *
         * @return number of lines
         */
        public int lineCount() {
            return lines.size();
        }

        /**
         * returns number of columns.
         *
         * @return number of columns
         */
        public int columnCount() {
            if (lines.isEmpty()) {
                return 0;
            }
            return lines.get(0).size(); // all lines are the same length
        }

        public void set(int column, int row, String value) {
            if (row < 0 || row > lineCount()) {
                throw new IllegalArgumentException("row doesn't exist");
            }
            Line line = lines.get(row);
            if (column < 0 || column > columnCount()) {
                throw new IllegalArgumentException("column doesn't exist");
            }
            line.set(column, value);
        }

        public void addColumn(int position, List<String> columnContent) {
            if (position < 0) {
                throw new IllegalArgumentException("Column position is negative");
            }

            // if the new position is outside the current content, extend short lines to position
            if (position > columnCount()) {
                for (Line line : lines) {
                    padTo(line, position);
                }
            }

            // add new lines for content of this column in case the column is too long
            while (lines.size() < columnContent.size()) {
                Line newLine = new Line(new ArrayList<>());
                padTo(newLine, position); // if the current line is new, and thus too short, pad it
                lines.add(newLine);
            }

            for (int i = 0; i < columnContent.size(); i++) {
                Line line = lines.get(i);
                line.add(position, columnContent.get(i)); // add actual content of column
            }

            if (columnContent.size() > columnCount()) {
                for (Line line : lines) {
                    padTo(line, content.columnCount());
                }
            }

            // if the column was shorter then previous once, shift the rest of the lines and insert padding char for the new column
            if (columnContent.size() < lines.size()) {
                shift(position, lines.subList(columnContent.size(), lines.size()));
            }
        }

        public void addLine(int position, List<String> lineContent) {
            if (position < 0) {
                throw new IllegalArgumentException("Line position is negative");
            }

            // add elements to this line if it is too short
            padTo(lineContent, columnCount());

            if (lineContent.size() > columnCount()) { // add elements to all other lines if this line is the new longest line
                for (Line line : lines) {
                    padTo(line, lineContent.size()); // padd to new longest line
                }
            }

            while (lines.size() < position) { // add new empty lines if this line is outside field
                List<String> emptyLineContent = new ArrayList<>();
                for (int i = 0; i < columnCount(); i++) {
                    emptyLineContent.add(paddingChar);
                }
                lines.add(new Line(emptyLineContent));
            }
            lines.add(position, new Line(lineContent)); // add actual line
        }

        /**
         * shifts all given lines at given position one entry to the right.
         *
         * The padding character will be inserted at the new position.
         *
         * @param position position
         * @param list list
         */
        private void shift(int position, List<Line> list) {
            for (Line line : list) {
                line.add(position, paddingChar);
            }
        }

        /**
         * add elements to given line until its length is the given max.
         *
         * @param line line
         * @param max max
         */
        private void padTo(List<String> line, int max) {
            while (line.size() < max) {
                line.add(paddingChar);
            }
        }

        private class Line extends ArrayList<String> {
            public Line(List<String> line) {
                super(line);
            }
        }
    }
}
