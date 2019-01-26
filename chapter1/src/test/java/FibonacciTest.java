/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


class FibonacciTest {

    private final Fibonacci fibonacci = new Fibonacci();

    @Test
    void calcWithZero() {
        assertThrows(IllegalArgumentException.class, () -> fibonacci.calc(0));
    }

    @Test
    void calcWithNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> fibonacci.calc(-10));
    }

    @Test
    void performance() {
        assertTimeout(Duration.ofMillis(100),
            () -> assertEquals(new BigInteger("354224848179261915075"), fibonacci.calc(100)));
        assertTimeout(Duration.ofMillis(100),
            () -> assertEquals(new BigInteger("280571172992510140037611932413038677189525"), fibonacci.calc(200)));
    }

    @Test
    void calc() {
        assertEquals(fibonacci.calc(1), BigInteger.ONE);
        assertEquals(fibonacci.calc(2), BigInteger.ONE);
        assertEquals(fibonacci.calc(10), new BigInteger("55"));
        assertEquals(fibonacci.calc(50), new BigInteger("12586269025"));
        assertEquals(fibonacci.calc(200), new BigInteger("280571172992510140037611932413038677189525"));
    }

    @Test
    @DisplayName("access level was hacked but performance sucks\uD83D\uDE2D")
    void calcNaively() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Fibonacci.class.getDeclaredMethod("calcNaively", int.class);
        method.setAccessible(true);

        assertEquals(method.invoke(fibonacci, 10), new BigInteger("55"));
        assertEquals(method.invoke(fibonacci, 40), new BigInteger("102334155"));
        assertEquals(method.invoke(fibonacci, 42), new BigInteger("267914296"));
    }

}