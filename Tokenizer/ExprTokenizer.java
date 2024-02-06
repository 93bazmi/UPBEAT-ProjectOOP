package Tokenizer;

public class ExprTokenizer implements Tokenizer {
    private final String src;
    private String next;
    private String prev;
    private int pos;

    public  ExprTokenizer(String src){
        this.src = src;
        pos = 0 ;
        computeNext();
    }


    public boolean hasNextToken(){
        return next != null ;
    }

    //peek : Returns the next token
    public String peek() {
        if (next == null){
            throw new TokenException.NoMoreToken(prev);
        }
        return next ;
    }
    public boolean peek(String str){
        if(!hasNextToken()){
            return false ;
        } else  {
            return  next.equals(str);
        }
    }

    //consume effects : removes the next token
    public  String consume(){
        if(hasNextToken())
            throw new TokenException.NoMoreToken(prev);
        String result = next ;
        computeNext();
        return result;
    }

    public boolean consume(String str){
        if(!hasNextToken()){
            throw new TokenException.NoMoreToken(prev);
        }else {
            if (next.equals(str)){
                computeNext();
                return true ;
            } else {
                return false ;
            }
        }
    }

    private void CommentLineCal() { //ทำจนกว่าจะเจอ \n
        while (pos<src.length() && src.charAt(pos) != '\n') {
            pos++;
        }
    }
    private boolean ignoreCommentChar(char c){ //return true เมื่อเจอช่องว่าง,#,"
        return Character.isWhitespace(c) || c == '#' || c == '"' ;
    }

    private void  computeNext(){
        if(src == null) return;
        StringBuilder s = new StringBuilder();
        while (pos<src.length() && ignoreCommentChar(src.charAt(pos))){
            if(src.charAt(pos) == '#') {
                CommentLineCal();
            } else {
                pos++ ;
            }
        }
        if (pos == src.length()){ //no more token
            prev = next;
            next = null;
            return;
        }
        String Operand = "()+-*/^{}=";
        char c = src.charAt(pos);
        if(Character.isLetter(c)){ //ตรวจสอบว่าอักขระ c เป็นตัวอักษรหรือไม่ โดยใช้เมธอด Character.isLetter(c)
            while (pos<src.length() && Character.isLetter(src.charAt(pos))){
                s.append(src.charAt(pos));
                pos++;
            }
        } else if(Operand.contains(String.valueOf(c))){ //ตรวจสอบว่าตัวอักษรที่ตำแหน่ง pos ใน src มีอยู่ใน Operand('+', '-', '*', '/') หรือไม่
            s.append(src.charAt(pos));
            pos++;
        } else {
            throw new TokenException.ExceptChar(c) ;
        }
        prev = next;
        next = s.toString();
    }

}


