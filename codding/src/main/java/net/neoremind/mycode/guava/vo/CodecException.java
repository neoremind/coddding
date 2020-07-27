package net.neoremind.mycode.guava.vo;

/**
 * CodecException
 *
 * @author xu.zx
 */
public class CodecException extends RuntimeException {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 5196421433506179782L;

  /**
   * Creates a new instance of TimeoutException.
   */
  public CodecException() {
    super();
  }

  /**
   * Creates a new instance of TimeoutException.
   */
  public CodecException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * Creates a new instance of TimeoutException.
   */
  public CodecException(String arg0) {
    super(arg0);
  }

  /**
   * Creates a new instance of TimeoutException.
   */
  public CodecException(Throwable arg0) {
    super(arg0);
  }

}
