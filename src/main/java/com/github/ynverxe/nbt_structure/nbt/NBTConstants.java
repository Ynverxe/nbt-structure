package com.github.ynverxe.nbt_structure.nbt;

import java.util.regex.Pattern;

public class NBTConstants {

  public static final Pattern UNQUOTED_KEY_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-\\.\\+]+\\w");
  public static final char COMPOUND_OPENER = '{';
  public static final char COMPOUND_CLOSER = '}';
  public static final char LIST_OPENER = '[';
  public static final char LIST_CLOSER = ']';
  public static final char DOUBLE_QUOTE = '"';
  public static final char SINGLE_QUOTE = '\'';
  public static final char KEY_VALUE_SEPARATOR = ':';
  public static final char VALUE_SEPARATOR = ',';
  public static final char DECIMAL_SEPARATOR = '.';
  public static final char SEMICOLON = ';';

}