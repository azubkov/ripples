package com.artit.ripples.utils;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utils {
    protected Utils() {
    }

    private static final Logger LOG = Logger.getLogger(Utils.class);
    private static final Pattern JSON_PATTERN = Pattern.compile("[\\w\\p{Punct}]{1,}:[\\w\\p{Punct}]{1,}");
    private static final Pattern GUID_PATTERN = Pattern.compile("[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}");
    private static final Pattern CSS_FONT_PATTERN = Pattern.compile("font-size:[\\s]*([\\d]+)([\\D]*);");
    private static final String htmlTextPatternStr = "\\{\"" + "htmlText" + "\":\"(.*?[^\\\\]{0,1})\"\\}";
    private static final Pattern htmlTextPattern = Pattern.compile(htmlTextPatternStr);

//    TODO move to types enum
    private static final String EVENT_PREFIX = "<- 00:00:00,000 in ";
    private static final String COMMAND_PREFIX = "-> out ";
    private static final String EVENT_PATTERN = "<- \\d{2}:\\d{2}:\\d{2},\\d{3} in .*";

    public static final Pattern PORT_PATTERN = Pattern.compile("[0-9]{1,5}");

    public static String formatLongToHHMMSS(long duration) {
        duration = duration / 1000;
        return String.format("%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
    }

    public static String formatLongToSS(long duration) {
        duration = duration / 1000;
        return String.format("%02d", duration);
    }

    public static String fixStringLength(final String s, final int minimum, final int maximum, final boolean isLeftAlignment) {
        return (String) safelyProcess(s, new FunctionBody() {
            @Override
            Object doFunction() {
                return String.format("%" + (isLeftAlignment ? "-" : "") + minimum + "." + maximum + "s", s);
            }
        });
    }

    public static String fixStringLength(final String s, final int minimum, final int maximum) {
        return fixStringLength(s, minimum, maximum, true);
    }

    public static String fixStringLength(final String s, final int size) {
        return fixStringLength(s, size, size);
    }

    public static String concatHostPort(String remoteHostName, String remotePortName) {
        return remoteHostName + ":" + remotePortName;
    }

    public static String concatHostPort(String remoteHostName, int remotePortName) {
        return concatHostPort(remoteHostName, String.valueOf(remotePortName));
    }

    /**
     * checks is the line is a valid port number
     */
    public static boolean isPort(String portStr) {
        if (portStr == null) {
            return false;
        }
        Matcher matcher = PORT_PATTERN.matcher(portStr);
        return matcher.matches();
    }

    /**
     * NOT only for json-lines
     *
     * @param line line to check
     * @param key  key which existence must be checked
     * @return true, if key exist in JSON-line, false otherwise
     */
    public static boolean isKeyExist(final String line, final String key) {
//        TODO investigate about possibility of first condition 'key:' without braces
        return (line.contains(key + ":") || line.contains(key + "\":"));
    }

//    TODO must be developed using  EVENT_PREFIX
    public static String getAppName(final String line) {
        if ((line.contains(" in ") && line.indexOf(" in ") < 20)
                || (line.contains(" out ") && line.indexOf(" out ") < 20)) {
            StringTokenizer st = new StringTokenizer(line);
            String temp;
            while (st.hasMoreElements()) {
                temp = st.nextToken(" ");
                if ("in".equals(temp) || "out".equals(temp)) {
                    return st.nextToken(" ");
                }
            }
        }
        return null;
    }

    public static String getAppSmallNameFromURL(final String urlString) {
        return (String) safelyProcess(urlString, new FunctionBody() {
            @Override
            Object doFunction() {
                int ownerNameEndIndex = urlString.indexOf('?');
                if (ownerNameEndIndex < 0) {
                    /*here's no '?''*/
                    ownerNameEndIndex = urlString.length();
                }
                //take out all info after appSmallName
                String withoutParams = urlString.substring(0, ownerNameEndIndex);
                //reversing of url
                withoutParams = reverseString(withoutParams);
                //reversed
                String appSmallName = withoutParams.substring(0, withoutParams.indexOf('/'));
                appSmallName = reverseString(appSmallName);
                return appSmallName;
            }
        });
    }


    /**
     * @return null if source line null. Empty if nothing found.
     */
    public static List<KeyValue> getExecutionParametersFromURL(final String url) {
        try {
            if (url == null) {
                return null;
            }

            List<KeyValue> keyValues = new ArrayList<KeyValue>();
            List<String> params = new ArrayList<String>();

            int questionPosition = url.indexOf("?");
            if (questionPosition == -1) {
                return keyValues;
            }

            String paramsPart = url.substring(questionPosition + 1);
            int ampersandPosition = paramsPart.indexOf("&");
            while (ampersandPosition != -1) {
                params.add(paramsPart.substring(0, ampersandPosition));
                paramsPart = paramsPart.substring(ampersandPosition + 1);
                ampersandPosition = paramsPart.indexOf("&");
            }
            params.add(paramsPart);

            for (String param : params) {
                int equalSignPosition = param.indexOf("=");
                String leftSide = param.substring(0, equalSignPosition);
                String rightSide = param.substring(equalSignPosition + 1);

                KeyValue keyValue = new KeyValue(leftSide, rightSide);
                keyValues.add(keyValue);
            }

            return keyValues;
        } catch (Throwable t) {
            LOG.error("EXCEPTION at line : " + url);
            throw new RuntimeException(t);
        }
    }

    public static String getHostFromURL(final String url) {
        return (String) safelyProcess(url, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                int fromIndex = url.indexOf("//") + 2/*cause //*/;
                if (fromIndex == 1) {
                    fromIndex = 0;
                }
                int toIndex = url.indexOf(":", fromIndex);
                String remoteHostName = url.substring(fromIndex, toIndex);
                return remoteHostName;
            }
        });

    }

    /**
     * @return port from url or null if input parameter not url or damaged
     */
    public static String getPortFromURL(final String url) {
        return (String) safelyProcess(url, new FunctionBody() {

            @Override
            Object doFunction() throws Exception {
                String urlPart = url;
                int colonPosition = urlPart.indexOf(":");
                urlPart = urlPart.substring(colonPosition + 1, urlPart.length());
                colonPosition = urlPart.indexOf(":");
                urlPart = urlPart.substring(colonPosition + 1, urlPart.length());
                int slashPosition = urlPart.indexOf("/");
                if (slashPosition == -1) {
                    slashPosition = urlPart.length();
                }

                int port = Integer.parseInt(urlPart.substring(0, slashPosition));
                return String.valueOf(port);
            }
        });
    }

    /**
     * @return schemeName from url or null if input parameter not url or damaged
     */
    public static String getSchemeFromURL(final String url) {
        return (String) safelyProcess(url, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                int schemeStopIndex = url.indexOf("://");
                if (schemeStopIndex == -1) {
                    return null;
                } else {
                    return url.substring(0, schemeStopIndex);
                }
            }
        });
    }

    /**
     * "Hello World!" to "!dlroW olleH"
     */
    private static String reverseString(String source) {
        int i, len = source.length();
        StringBuffer dest = new StringBuffer(len);
        for (i = (len - 1); i >= 0; i--) {
            dest.append(source.charAt(i));
        }
        return dest.toString();
    }

    /**
     * Getting json  from incoming string
     *
     * @param line - line to get json
     * @return returns string which begins with "{" and ends with "}", else null
     */
    public static String getJSONFromLine(final String line) {
        String json;
        if ((line.contains("{")) && (line.contains("}")) && (line.indexOf("{") < line.indexOf("}"))) {
            json = line.substring(line.indexOf("{"), line.lastIndexOf("}") + 1);
        } else {
            json = "";//todo hack accordingly to sever's behavior
        }
        return json;
    }

    /**
     * @param scenarioLine the line, which type must be defined.
     * @return true if input string is command.
     */
    public static boolean isCommand(final String scenarioLine) {
        return scenarioLine.contains(COMMAND_PREFIX);
    }

    /**
     * @param scenarioLine the line, which type must be defined.
     * @return true if input string is event.
     */
    public static boolean isEvent(final String scenarioLine){
        return scenarioLine.matches(EVENT_PATTERN);
    }

    /**
     * Check whether input string is GUID
     *
     * @param input - String with GUID to test
     * @return - true if input string is GUID, false otherwise
     */
    public static boolean isGUID(final String input) {
        return (GUID_PATTERN.matcher(input).matches());
    }

    /**
     * Getting all GUIDs as Set from incoming string
     *
     * @param line - line to get GUIDs
     * @return returns Set of GUIDs,
     *         if there were no GUIDs found - returns Set with size=0
     */
    public static List<String> getGUIDSetFromLine(final String line) {
        List<String> guids = new ArrayList<String>();
        Matcher matcher = GUID_PATTERN.matcher(line);
        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                guids.add(matcher.group());
            }
        }
        return guids;
    }

    public static String wrapAsCommand(final String smallAppName, final String command) {
        return (COMMAND_PREFIX + smallAppName + " " + command);
    }

    public static String wrapAsEvent(final String smallAppName, final String command) {
        return (EVENT_PREFIX + smallAppName + " " + command);
    }

    /**
     * @param line     input line ti handle
     * @param parName  parameter to be replaced
     * @param num      number of occurrence
     * @param newValue new value of the parameter
     * @return modified string
     */
    //TODO ??? required?
    public static String replaceNumParameterWith(final String line, final String parName, final int num, final String newValue) {
        String oldValue = getValue(line, parName, num);
        String searchValue = ":" + oldValue + ",";
        String replaceValue = ":" + newValue + ",";
        if (!line.contains(searchValue)) {
            searchValue = ":" + oldValue + "}";
            replaceValue = ":" + newValue + "}";
        }
        if (!line.contains(searchValue)) {
            searchValue = ":" + oldValue + "]";
            replaceValue = ":" + newValue + "]";
        }
        return line.replace(searchValue, replaceValue);
    }

    /**
     * @param line     input line ti handle
     * @param parName  parameter to be replaced
     * @param newValue new value of the parameter
     * @return modified string
     */
    //TODO ??? required?      REFACTOR for independence of braces type (or existing)
    public static String replaceValParameterWith(final String line, final String parName, final String newValue) {
        String oldValue = getValue(line, parName);
        String searchValue = ":\"" + oldValue + "\"";
        String replaceValue = ":\"" + newValue + "\"";
        return line.replace(searchValue, replaceValue);
    }

    /**
     * @param line json line like: <b>"method":"registerApplication","destinationSessionGUID":"a9e07b60-dad3-11df-9d59-0016e68af210","sourceSessionGUID":"aa1a01f0-dad3-11df-9d59-0016e68af210"</b>
     * @return list like: <b>[method] [destinationSessionGUID] [sourceSessionGUID]</b>
     */
    public static List<String> getAllValueNames(String line) {
        try {
            String linePartFromEndMarker = line;
            String linePartToStartMarker = line;
            final String VALUE_END_MARKER = "\":";
            final String VALUE_START_MARKER = "\"";

            int endMarkerPosition;
            int markerStartPosition;
            List<String> answer = new ArrayList<String>();

            endMarkerPosition = linePartFromEndMarker.indexOf(VALUE_END_MARKER);
            while (endMarkerPosition != -1) {
                linePartToStartMarker = linePartFromEndMarker.substring(0, endMarkerPosition);
                markerStartPosition = linePartToStartMarker.lastIndexOf(VALUE_START_MARKER);
                String valueName = linePartToStartMarker.substring(markerStartPosition + VALUE_START_MARKER.length(), linePartToStartMarker.length());
                answer.add(valueName);
                linePartFromEndMarker = linePartFromEndMarker.substring(endMarkerPosition + VALUE_END_MARKER.length(), linePartFromEndMarker.length());
                endMarkerPosition = linePartFromEndMarker.indexOf(VALUE_END_MARKER);
            }

            return answer;
        } catch (Throwable t) {
            LOG.error("EXCEPTION at line : " + line);
            throw new RuntimeException(t);
        }
    }

    public static String getFileExtension(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                String filename = getFullFileNameFromPath(fullPath);
                final String extensionSeparator = ".";
                int dot = filename.lastIndexOf(extensionSeparator);
                if (dot == -1) {
                    return "";
                }
                return filename.substring(dot + 1);
            }
        });
    }

    /**
     * without extension
     */
    public static String getFileNameWithoutExtension(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                String filename = getFullFileNameFromPath(fullPath);
                final String extensionSeparator = ".";
                int dot = filename.indexOf(extensionSeparator);
                if (dot == -1) {
                    dot = filename.length();
                }
                return filename.substring(0, dot);
            }
        });
    }

    /**
     * with extension, null if not parsed
     */
    public static String getOnlyFileName(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                final String pathSeparator = System.getProperty("file.separator");
                int separatorPos = fullPath.lastIndexOf(pathSeparator);
                if (separatorPos == -1) {
                    return null;
                }
                return fullPath.substring(separatorPos + 1, fullPath.length());
            }
        });
    }

    public static String getFullFileNameFromUrl(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                int i = fullPath.lastIndexOf("/");
                if (i > 0) {
                    return fullPath.substring(i + 1);
                }
                return fullPath;
            }
        });
    }

    private static String getFullFileNameFromPath(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                int i = fullPath.lastIndexOf(System.getProperty("file.separator"));
                if (i > 0) {
                    return fullPath.substring(i + 1);
                }
                return fullPath;
            }
        });
    }


    /**
     * without last separator ('/')
     */
    public static String getFilePath(final String fullPath) {
        return (String) safelyProcess(fullPath, new FunctionBody() {
            @Override
            Object doFunction() throws Exception {
                final String pathSeparator = System.getProperty("file.separator");
                int sep = fullPath.lastIndexOf(pathSeparator);
                return fullPath.substring(0, sep);
            }
        });
    }

    /**
     * @see ScriptParser#getValueRealization
     */
    public static String getValue(String line, String name) {
        return getValue(line, name, 1);
    }

    /**
     * @see ScriptParser#getValueRealization
     */
    private static String getValue(String line, String name, int orderNumber) {
        return getValueRealization(line, name, orderNumber);
    }

    /**
     * @param orderNumber belongs [1...MAX_VALUE]
     * @return any value, that starts from nothing, { [ or " . null if not found
     */
    private static String getValueRealization(String line, String name, int orderNumber) {
        String value = getValueWithBrackets(line, name, orderNumber);
        return removeBrackets(value);
    }

    /**
     * makes apple{},"lemon"  from [apple{},"lemon"]
     */
    private static String removeBrackets(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() > 0) {
            char ch = value.charAt(0);
            if (ch == '\"' || ch == '[' || ch == '{') {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

    public static String getValueWithBrackets(String line, String name, int orderNumber) {
        if (line == null) {
            return null;
        }
        String key = "\"" + name + "\":";
        String subLine = line;
        int fromPosition = -1;
        for (int i = 0; i < orderNumber; i++) {
            fromPosition = subLine.indexOf(key);
            if (fromPosition == -1) {
                return null;
            }
            fromPosition += key.length();
            subLine = subLine.substring(fromPosition);
        }
        return getValueFromStringStart(subLine).getKey();
    }

    /**
     * returns value that starts directly at first char. E.g. <b>"value"</b> or <b>{null}</b> etc
     *
     * @return key-string with value, value - integer with value's last char position in given line; key contains braces and quotes
     */
    private static Map.Entry<String, Integer> getValueFromStringStart(final String input) {
        String subLine = input;
        char firstValueChar = subLine.charAt(0);
        if (firstValueChar == '\"') {
            subLine = subLine.substring(1);
            int eovPosition = subLine.indexOf("\"");
            if (eovPosition == -1) {
                throw new RuntimeException("String [" + input + "] is damaged. Length : " + input.length());
            }
            eovPosition++;
            String value = "\"" + subLine.substring(0, eovPosition);
            return new AbstractMap.SimpleEntry(value, eovPosition);
        } else if (firstValueChar == '{' || firstValueChar == '[') {
            char openBrace, closeBrace;
            if (firstValueChar == '{') {
                openBrace = '{';
                closeBrace = '}';
            } else {
                openBrace = '[';
                closeBrace = ']';
            }
            int openBracesCount = 0;
            int closeBracesCount = 0;
            int lastCloseBracePosition = -1;
            for (int i = 0; i < subLine.length(); i++) {
                char ch = subLine.charAt(i);
                if (ch == openBrace) {
                    openBracesCount++;
                }
                if (ch == closeBrace) {
                    closeBracesCount++;
                    if (openBracesCount == closeBracesCount) {
                        lastCloseBracePosition = i;
                        break;
                    }
                }
            }
            if (lastCloseBracePosition == -1) {
                throw new RuntimeException("String [" + subLine + "] is damaged.");
            }
            lastCloseBracePosition++;
            String value = subLine.substring(0, lastCloseBracePosition);
            return new AbstractMap.SimpleEntry(value, lastCloseBracePosition);
        } else {
//            simply word
            char[] eovChars = {',', '}', ']'};
            int mostClosest = subLine.length();
            for (char eovChar : eovChars) {
                int position = subLine.indexOf(eovChar);
                if (position == -1) {
                    continue;
                }
                mostClosest = Math.min(position, mostClosest);
            }
            String value = subLine.substring(0, mostClosest);
            return new AbstractMap.SimpleEntry(value, mostClosest);
        }
    }

    /**
     * @param input - inner string, with [] brackets
     */
    public static String[] parseArray(String input) {
        if (input == null) {
            return null;
        }
        String arrayBody = removeBrackets(input);
        List<String> result = new ArrayList<String>();
        Map.Entry<String, Integer> entry;
        while (arrayBody.length() != 0) {
            entry = getValueFromStringStart(arrayBody);
            result.add(entry.getKey());
            if (entry.getValue() == arrayBody.length()) {
                break;
            }
            arrayBody = arrayBody.substring(entry.getValue() + 1, arrayBody.length());
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * checks if the value contains another values inside
     */
    public static boolean isComplexValue(String value) {
        char[] complexChars = {',', '[', ']', '{', '}', '\"'};
        for (char complexChar : complexChars) {
            if (value.indexOf(complexChar) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses line, find all values and returns as List.
     */
    public static List<KeyValue> getAllValues(String line) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        List<String> valueNames = getAllValueNames(line);
        int[] orderNumbers = new int[valueNames.size()];

        for (String valueName : new LinkedHashSet<String>(valueNames)) {
            int valueCount = 0;
            for (int i = 0; i < valueNames.size(); i++) {
                String name = valueNames.get(i);
                if (valueName.equals(name)) {
                    valueCount++;
                    orderNumbers[i] = valueCount;
                }
            }
        }

        for (int i = 0; i < valueNames.size(); i++) {
            String valueName = valueNames.get(i);
            keyValues.add(new KeyValue(valueName, getValueRealization(line, valueName, orderNumbers[i])));
        }

        return keyValues;
    }

    /**
     * Make sure that wrapped function can produce null.<br>
     *
     * @return null if source line is "" or null, returns null if exception during parsing
     */
    private static Object safelyProcess(String line, FunctionBody functionBody) {
        if (line == null) {
            return null;
        }
        if ("".equals(line)) {
            return null;
        }

        try {
            return functionBody.doFunction();
        } catch (Throwable t) {
            LOG.error("EXCEPTION [" + t + "] at line : " + line + "\n null was returned");
            return null;
        }
    }

    private abstract static class FunctionBody {
        abstract Object doFunction() throws Exception;
    }


    public static class KeyValue {
        private String key;
        private String value;

        private KeyValue() {
        }

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "KeyValue{"
                    + "key='" + key + '\''
                    + ", value='" + value + '\''
                    + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof KeyValue)) {
                return false;
            }

            KeyValue keyValue = (KeyValue) o;

            if (key != null ? !key.equals(keyValue.key) : keyValue.key != null) {
                return false;
            }
            if (value != null ? !value.equals(keyValue.value) : keyValue.value != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }
}
