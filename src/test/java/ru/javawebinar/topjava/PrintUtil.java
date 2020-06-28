package ru.javawebinar.topjava;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrintUtil {

    public static String formatExecutionTimeOfMethods(Map<String, Long> executionTimeOfMethods) {
        String headerMethodName = "Method name:";
        String headerMillis = "Millis:";

        int maxLengthMethodName = getMaxLength(headerMethodName, executionTimeOfMethods.keySet().stream());
        int maxLengthMillis = getMaxLength(headerMillis, executionTimeOfMethods.values().stream().map(String::valueOf));

        ArrayList<LogColumn> borderColumns = new ArrayList<>();
        borderColumns.add(new LogColumn(maxLengthMethodName));
        borderColumns.add(new LogColumn(maxLengthMillis));

        StringBuilder sb = new StringBuilder("Execution time of methods:\n");

        addRow(sb, borderColumns);
        addRow(sb,
                new LogColumn(maxLengthMethodName, headerMethodName, true),
                new LogColumn(maxLengthMillis, headerMillis, true));
        addRow(sb, borderColumns);

        List<String> methodNames = executionTimeOfMethods.keySet().stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());
        for (String methodName : methodNames) {
            addRow(sb,
                    new LogColumn(maxLengthMethodName, methodName, false),
                    new LogColumn(maxLengthMillis, String.valueOf(executionTimeOfMethods.get(methodName)), true));
        }
        addRow(sb, borderColumns);

        return sb.toString();
    }

    private static int getMaxLength(String header, Stream<String> values) {
        int maxLength = values.map(String::length).max(Integer::compareTo).orElse(0);
        maxLength = Math.max(maxLength, header.length());
        return maxLength;
    }

    private static void addRow(StringBuilder sb, LogColumn... columns) {
        addRow(sb, Arrays.asList(columns));
    }

    private static void addRow(StringBuilder sb, List<LogColumn> columns) {
        for (LogColumn column : columns) {
            sb.append(column.borderText);

            int emptyTextLengthRight = column.length - column.text.length();
            int emptyTextLengthLeft = 0;
            if (column.textAlignmentInCenter) {
                emptyTextLengthLeft = emptyTextLengthRight / 2;
                emptyTextLengthRight = emptyTextLengthRight % 2 == 0 ? emptyTextLengthLeft : emptyTextLengthLeft + 1;
            }

            // TODO java11: column.fillingEmptyText.repeat(emptyTextLengthLeft)
            sb.append(String.join("", Collections.nCopies(emptyTextLengthLeft, column.fillingEmptyText)));
            sb.append(column.text);
            // TODO java11: column.fillingEmptyText.repeat(emptyTextLengthRight)
            sb.append(String.join("", Collections.nCopies(emptyTextLengthRight, column.fillingEmptyText)));
        }

        if (columns.size() > 0) {
            sb.append(columns.get(0).borderText);
            sb.append("\n");
        }
    }


    private static class LogColumn {
        String text = "";
        int length;
        boolean textAlignmentInCenter = false;
        String borderText = "+";
        String fillingEmptyText = "-";

        public LogColumn(int length, String text, boolean textAlignmentInCenter) {
            this.text = text;
            this.length = length;
            this.textAlignmentInCenter = textAlignmentInCenter;
            this.borderText = "|";
            this.fillingEmptyText = " ";
        }

        public LogColumn(int length) {
            this.length = length;
        }
    }
}
