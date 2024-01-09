package configgen.util;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PackParserTest {

    @Test
    public void parseNestList() {
        t1("a", "a");
        t1("a,b", "a", "b");
        t1("QingGong(10,(13300100,13300200)), RanSe(30,3)", "QingGong(10,(13300100,13300200))", "RanSe(30,3)");
        t1("QingGong(10, (13300100, 13300200)) , RanSe(30,3) ", "QingGong(10, (13300100, 13300200))", "RanSe(30,3)");
    }

    @Test
    public void prefixWhitespace_Ignore() {
        t1("a, b, c", "a", "b", "c");
    }

    @Test
    public void surfixWhitespace_NotIgnore() {
        t1("a, b ", "a", "b ");
    }

    @Test
    public void parentheses_AsOne() {
        t1("(a,b)", "a,b");
    }

    @Test
    public void firstLayerParentheses_TakeOff() {
        t1("a,(b,c)", "a", "b,c");
    }

    @Test
    public void firstLayerParenthesesWithFunction_Keep() {
        t1("a,f(b,c)", "a", "f(b,c)");
    }

    @Test
    public void secondLayerParentheses_Keep() {
        t1("a,(b,(c,d))", "a", "b,(c,d)");
    }

    private void t1(String source, String... row) {
        List<String> a = PackParser.parsePack(source);
        assertEquals(row.length, a.size());
        int i = 0;
        for (String c : row) {
            assertEquals(c, a.get(i++));
        }
    }

    @Test
    public void parseFunction() {
//        t2("a", "a", "");
        t2("a(b,c)", "a", "b,c");
        t2("abc(b,c,d(e,f))", "abc", "b,c,d(e,f)");
        //assertThrows(Throwable.class, () -> NestListParser.parseFunction("a,b,c"));
    }

    private void t2(String source, String name, String parameters) {
        List<String> a = PackParser.parseFunction(source);
        assertEquals(2, a.size());
        assertEquals(name, a.get(0));
        assertEquals(parameters, a.get(1));
    }
}