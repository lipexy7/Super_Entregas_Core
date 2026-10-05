package service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Teste parametrizado: uma tabela peso -> frete esperado. Os valores esperados
 * foram calculados à mão a partir da regra (não pelo código testado).
 */
@RunWith(Parameterized.class)
public class CalculadoraDeFreteTabelaTest {

    @Parameters(name = "{0} kg -> R$ {1}")
    public static Collection<Object[]> dados() {
        return Arrays.asList(new Object[][]{
            {"0.01", "12.00"},
            {"1.00", "12.00"},
            {"1.50", "14.00"},
            {"2.00", "16.00"},
            {"5.00", "28.00"},
            {"8.67", "42.68"},
            {"15.25", "69.00"},
            {"29.99", "127.96"},
            {"30.00", "128.00"},
            {"30.01", "153.04"},
            {"50.00", "233.00"},
            {"100.00", "433.00"}
        });
    }

    private final BigDecimal peso;
    private final BigDecimal esperado;

    public CalculadoraDeFreteTabelaTest(String peso, String esperado) {
        this.peso = new BigDecimal(peso);
        this.esperado = new BigDecimal(esperado);
    }

    @Test
    public void calculaOFreteEsperado() {
        assertEquals(esperado, new CalculadoraDeFrete().calcular(peso));
    }
}
