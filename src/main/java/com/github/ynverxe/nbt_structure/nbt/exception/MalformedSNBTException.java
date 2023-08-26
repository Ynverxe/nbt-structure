package com.github.ynverxe.nbt_structure.nbt.exception;

public class MalformedSNBTException extends RuntimeException {

  public MalformedSNBTException(String message) {
    super(message);
  }

  public MalformedSNBTException(String message, Throwable cause) {
    super(message, cause);
  }
}
