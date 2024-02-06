package Tokenizer ;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    boolean peek(String Str);
    String consume();
    boolean consume(String Str);
}
