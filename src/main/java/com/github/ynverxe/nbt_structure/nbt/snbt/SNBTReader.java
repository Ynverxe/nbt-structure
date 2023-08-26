package com.github.ynverxe.nbt_structure.nbt.snbt;

import static com.github.ynverxe.nbt_structure.nbt.NBTConstants.*;

import com.github.ynverxe.nbt_structure.nbt.*;
import com.github.ynverxe.nbt_structure.nbt.exception.MalformedSNBTException;
import com.github.ynverxe.nbt_structure.nbt.exception.MalformedSNBTNumberException;
import com.github.ynverxe.nbt_structure.nbt.text.CharStack;
import java.util.*;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public final class SNBTReader implements AutoCloseable {

  private static final Map<Character, Function<String, Number>> NUMBER_CONVERTERS = new HashMap<>();
  private static final Map<Character, Class<?>> ARRAY_NUMBER_CLASSIFIERS = new HashMap<>();

  static {
    Function<String, Number> byteConverter = Byte::parseByte;
    NUMBER_CONVERTERS.put('b', byteConverter);

    Function<String, Number> shortConverter = Short::parseShort;
    NUMBER_CONVERTERS.put('s', shortConverter);

    Function<String, Number> longConverter = Long::parseLong;
    NUMBER_CONVERTERS.put('l', longConverter);

    Function<String, Number> floatConverter = Float::parseFloat;
    NUMBER_CONVERTERS.put('f', floatConverter);

    Function<String, Number> doubleConverter = Double::parseDouble;
    NUMBER_CONVERTERS.put('d', doubleConverter);

    ARRAY_NUMBER_CLASSIFIERS.put('B', Byte.class);
    ARRAY_NUMBER_CLASSIFIERS.put('I', Integer.class);
    ARRAY_NUMBER_CLASSIFIERS.put('L', Long.class);
  }

  private CharStack stack;

  SNBTReader(CharSequence text) {
    this.stack = new CharStack(text);
  }

  NBT<?> resolve() {
    NBT<?> nbt;
    char start = stack.skipWhitespaces().lookUpNext();

    if (start == COMPOUND_OPENER) {
      nbt = parseCompound();
    } else if (start == LIST_OPENER) {
      nbt = parseListOrArray();
    } else {
      nbt = parseNBTValue();
    }

    return nbt;
  }

  private NBTCompound parseCompound() {
    NBTCompound compound = new NBTCompound();

    if (expectOpenerAndCloser(COMPOUND_OPENER, COMPOUND_CLOSER)) {
      return compound;
    }

    peekUntilCloser(() -> {
      Tag<?> tag = parseTag();

      compound.writeTag(tag);
    }, COMPOUND_CLOSER);

    return compound;
  }

  private String parseKey() {
    char next = stack.skipWhitespaces().lookUpNext();

    if (next == SINGLE_QUOTE || next == DOUBLE_QUOTE) {
      return parseString();
    }

    return parseUnquotedKey();
  }

  private String parseUnquotedKey() {
    int start = stack.cursor() + 1;
    String content = stack.peekUntilChar(KEY_VALUE_SEPARATOR, true);

    if (!UNQUOTED_KEY_PATTERN.matcher(content).matches()) {
      throw new IllegalStateException("Invalid unquoted key at index=" + start);
    }

    return content;
  }

  private NBT<?> parseNBTValue() {
    Object value = null;

    char next = stack.lookUpNext();

    if (next == SINGLE_QUOTE || next == DOUBLE_QUOTE) {
      return new NBT<>(parseString());
    }

    return new NBT<>(parseNumber());
  }

  private String parseString() {
    char usedQuote = stack.next();

    String parsed = stack.peekUntilChar(usedQuote, false);
    stack.skip(); // last quote

    return parsed;
  }

  private Number parseNumber() {
    StringBuilder content = new StringBuilder();

    Character numberClassifier = null;

    boolean decimal = false;

    int start = stack.cursor() + 1;

    while (stack.canPeekNext()) {
      char next = stack.next();

      if (Character.isDigit(next)) {
        content.append(next);
      } else if (next == DECIMAL_SEPARATOR) {
        if (decimal) {
          throw new MalformedSNBTNumberException(
              "Double decimal separator found at index=" + start);
        }

        decimal = true;
        content.append(next);
      } else {
        if (content.length() == 0) {
          throw new MalformedSNBTNumberException("Malformed number at index=" + start);
        }

        if (Character.isLetter(next)) {
          numberClassifier = next;
        } else { // a char that is not a letter, maybe a ','
          stack.back();
        }

        break;
      }
    }

    String built = content.toString();
    if (numberClassifier == null) {
      if (decimal) return Double.parseDouble(built);

      return Integer.parseInt(built);
    }

    Function<String, Number> converter =
        NUMBER_CONVERTERS.get(Character.toLowerCase(numberClassifier));

    if (converter == null)
      throw new MalformedSNBTNumberException(
          "Invalid number classifier char '" + numberClassifier + "' at index=" + start);

    return converter.apply(built);
  }

  private NBT<?> parseListOrArray() {
    if (expectOpenerAndCloser(LIST_OPENER, LIST_CLOSER)) {
      return new NBTList();
    }

    char next = stack.lookUpNext();

    if (ARRAY_NUMBER_CLASSIFIERS.containsKey(next)) {
      return parseArray();
    }

    return parseList();
  }

  private NBTList parseList() {
    NBTList nbtList = new NBTList();

    peekUntilCloser(() -> {
      NBT<?> value = resolve();

      nbtList.add(value);
    }, LIST_CLOSER);

    return nbtList;
  }

  private NBT<?> parseArray() {
    Class<?> arrayType = ARRAY_NUMBER_CLASSIFIERS.get(Character.toUpperCase(stack.next()));

    stack.expectNext(SEMICOLON);

    List<Number> numbers = new ArrayList<>();

    peekUntilCloser(() -> {
      Number number = parseNumber();

      if (!arrayType.isInstance(number)) {
        throw new MalformedSNBTException(
            String.format(
                "Expected number type=%s but found=%s at index=%s",
                arrayType.getSimpleName(), number.getClass().getSimpleName(), stack.cursor()));
      }

      numbers.add(number);
    }, LIST_CLOSER);

    return NBT.guessArrayByContent(numbers.toArray());
  }

  private Tag<?> parseTag() {
    String key = parseKey();

    stack.skipWhitespaces().expectNext(KEY_VALUE_SEPARATOR);

    NBT<?> value = resolve();
    return new Tag<>(key, value);
  }

  private void peekUntilCloser(Runnable runnable, char closer) {
    while (stack.canPeekNext() && stack.peek() != closer) {
      stack.skipWhitespaces();
      runnable.run();
      expectNextElementOrCloser(closer);
    }
  }

  private void expectNextElementOrCloser(char closer) {
    stack.skipWhitespaces().expectNext(VALUE_SEPARATOR, closer);
  }

  private boolean expectOpenerAndCloser(char opener, char closer) {
    stack.skipWhitespaces().expectNext(opener);

    return stack.skipNextIfTrue(closer);
  }

  public static @NotNull NBT<?> readSNBT(@NotNull CharSequence snbt) {
    SNBTReader reader = new SNBTReader(snbt);

    return reader.resolve();
  }

  @Override
  public void close() throws Exception {
    stack.close();
    stack = null;
  }
}