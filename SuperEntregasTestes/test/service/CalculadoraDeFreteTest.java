package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import exception.RegraDeNegocioException;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

/** Testes unitários da regra de cálculo do frete (RF06). Não usam banco nem interface. */
public class CalculadoraDeFreteTest {

    private CalculadoraDeFrete calculadora;

    @Before
    public void preparar() {
        calculadora = new CalculadoraDeFrete();
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }

    @Test
    public void pesoMinimoPagaTarifaBase() {
        assertEquals(bd("12.00"), calculadora.calcular(bd("0.01")));
    }

    @Test
    public void pesoDentroDaFranquiaPagaTarifaBase() {
        assertEquals(bd("12.00"), calculadora.calcular(bd("0.50")));
    }

    @Test
    public void exatamenteUmQuiloAindaPagaTarifaBase() {
        assertEquals(bd("12.00"), calculadora.calcular(bd("1.00")));
    }

    @Test
    public void primeiroCentesimoAcimaDaFranquiaCobraExcedente() {
        assertEquals(bd("12.04"), calculadora.calcular(bd("1.01")));
    }

    @Test
    public void pesoComExcedenteFracionado() {
        // 12,00 + (2,44 - 1,00) * 4,00 = 17,76
        assertEquals(bd("17.76"), calculadora.calcular(bd("2.44")));
    }

    @Test
    public void pesoInteiroDezQuilos() {
        // 12,00 + 9 * 4,00 = 48,00
        assertEquals(bd("48.00"), calculadora.calcular(bd("10")));
    }

    @Test
    public void pesoComTresCasasEhArredondadoAntesDoCalculo() {
        // 2,555 -> 2,56 ; 12,00 + 1,56 * 4,00 = 18,24
        assertEquals(bd("18.24"), calculadora.calcular(bd("2.555")));
    }

    @Test
    public void trintaQuilosExatosNaoEhCargaPesada() {
        // 12,00 + 29 * 4,00 = 128,00 (sem acréscimo)
        assertEquals(bd("128.00"), calculadora.calcular(bd("30.00")));
        assertFalse(calculadora.ehCargaPesada(bd("30.00")));
    }

    @Test
    public void acimaDeTrintaQuilosCobraAcrescimoDeCargaPesada() {
        // 12,00 + 29,01 * 4,00 + 25,00 = 153,04
        assertEquals(bd("153.04"), calculadora.calcular(bd("30.01")));
        assertTrue(calculadora.ehCargaPesada(bd("30.01")));
    }

    @Test
    public void pesoMaximoPermitido() {
        // 12,00 + 998,99 * 4,00 + 25,00 = 4032,96
        assertEquals(bd("4032.96"), calculadora.calcular(bd("999.99")));
    }

    @Test
    public void resultadoSempreTemDuasCasasDecimais() {
        assertEquals(2, calculadora.calcular(bd("5")).scale());
    }

    @Test
    public void tarifasPersonalizadasPeloConstrutor() {
        CalculadoraDeFrete outra = new CalculadoraDeFrete(bd("10.00"), bd("2.00"), bd("0.00"));
        // 10,00 + (5 - 1) * 2,00 = 18,00
        assertEquals(bd("18.00"), outra.calcular(bd("5")));
    }

    @Test
    public void pesoZeroELancadoComoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> calculadora.calcular(BigDecimal.ZERO));
    }

    @Test
    public void pesoNegativoELancadoComoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> calculadora.calcular(bd("-3")));
    }

    @Test
    public void pesoNuloELancadoComoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> calculadora.calcular(null));
    }

    @Test
    public void pesoAcimaDoMaximoELancadoComoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> calculadora.calcular(bd("1000")));
    }

    @Test
    public void pesoQueArredondaParaZeroELancadoComoRegraDeNegocio() {
        assertThrows(RegraDeNegocioException.class, () -> calculadora.calcular(bd("0.004")));
    }
}
