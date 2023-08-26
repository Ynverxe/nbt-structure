package com.github.ynverxe.nbt_structure.nbt.snbt;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class CharStack implements AutoCloseable {

  private static final char SYNTAX_ESCAPE = '\\';
  private int cursor = -1;
  private CharSequence charSequence;

  public CharStack(CharSequence charSequence) {
    if (charSequence.length() == 0) {
      throw new IllegalStateException("empty char sequence");
    }

    this.charSequence = charSequence;
  }

  public char next() {
    return charSequence.charAt(checkBounds(1));
  }

  public char lookUpNext() {
    char next = next();
    cursor--;
    return next;
  }

  public boolean skipNextIfTrue(char expect) {
    boolean equals = lookUpNext() == expect;

    if (equals) skip();

    return equals;
  }

  public void expectNext(Character... characters) {
    expectNext(Arrays.asList(characters));
  }

  public void expectNext(Collection<Character> expected) {
    if (!canPeekNext()) {
      throw new IllegalStateException("Expected=" + expected + " and there's no more elements");
    }

    skipWhitespaces();

    char next = next();

    RuntimeException exception = null;

    if (!expected.contains(next)) {
      exception =
          new IllegalStateException(
              "Expected=" + expected + ", found=" + next + " at index=" + cursor);
    }

    if (exception != null) {
      back();
      throw exception;
    }
  }

  public void skip() {
    checkBounds(1);
  }

  public void back() {
    checkBounds(-1);
  }

  public char peek() {
    return charSequence.charAt(cursor);
  }

  public boolean canPeekNext() {
    return cursor + 1 < charSequence.length();
  }

  public int length() {
    return charSequence.length();
  }

  public char previous() {
    return charSequence.charAt(checkBounds(-1));
  }

  public int cursor() {
    return cursor;
  }

  public CharStack setCursor(int cursor) {
    this.cursor = cursor;
    return this;
  }

  public String peekUntilChar(char limit, boolean skipWhitespaces) {
    StringBuilder builder = new StringBuilder();

    boolean escaped = false;

    int start = cursor + 1;
    while (canPeekNext()) {
      if (skipWhitespaces) skipWhitespaces();

      char next = next();

      if (escaped) {
        if (next == limit || next == SYNTAX_ESCAPE) {
          builder.append(next);
          escaped = false;
        } else {
          throw new IllegalStateException("Invalid escape at index=" + start);
        }
      } else if (next == SYNTAX_ESCAPE) {
        escaped = true;
      } else if (next == limit) {
        back();
        return builder.toString();
      } else {
        builder.append(next);
      }
    }

    throw new IllegalStateException(
        "End of the sequence. No limit found (limit char='" + limit + "')");
  }

  public CharStack skipWhitespaces() {
    while (canPeekNext()) {
      char next = lookUpNext();

      if (Character.isWhitespace(next)) {
        next();
      } else {
        break;
      }
    }
    return this;
  }

  @Override
  public void close() throws IOException {
    if (charSequence == null) return;

    if (charSequence instanceof Closeable) {
      ((Closeable) charSequence).close();
    }

    this.charSequence = null;
    this.cursor = -1;
  }

  private int checkBounds(int direction) {
    int index = cursor + direction;
    if (index >= charSequence.length()) {
      throw new IndexOutOfBoundsException("cursor=" + index + ", length=" + charSequence.length());
    }

    if (index < 0) {
      throw new IndexOutOfBoundsException("cursor is at 0 position");
    }

    return cursor = index;
  }
}
