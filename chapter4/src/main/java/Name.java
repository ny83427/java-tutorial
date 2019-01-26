/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
/**
 * Name of a giving person
 */
class Name {

    private String first;

    private String middle;

    private String last;

    private String suffix;

    Name(String first, String last) {
        this.first = first;
        this.last = last;
    }

    Name(String first, String middle, String last) {
        this(first, last);
        this.middle = middle;
    }

    Name(String first, String middle, String last, String suffix) {
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.suffix = suffix;
    }

    String getFirst() {
        return first;
    }

    private static final char JOIN_SEPARATOR = ' ';

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (first != null && first.trim().length() > 0) {
            sb.append(first.trim()).append(JOIN_SEPARATOR);
        }
        if (middle != null && middle.trim().length() > 0) {
            sb.append(middle.trim()).append(JOIN_SEPARATOR);
        }
        if (last != null && last.trim().length() > 0) {
            sb.append(last.trim());
        }
        if (suffix != null && suffix.trim().length() > 0) {
            sb.append(JOIN_SEPARATOR).append(suffix.trim());
        }
        return sb.toString();
    }
}
