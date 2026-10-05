package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import util.Peso;

/**
 * Regra de negócio nova (etapa de testes): cálculo do valor do frete a partir
 * do peso. É uma classe sem acesso a banco nem a interface, por isso pode ser
 * testada com JUnit de forma direta.
 *
 * Regras padrão:
 *  - até 1,00 kg: tarifa base de R$ 12,00;
 *  - acima de 1,00 kg: tarifa base + R$ 4,00 por kg excedente (proporcional);
 *  - acima de 30,00 kg ("carga pesada"): acréscimo fixo de R$ 25,00.
 * Os valores podem ser trocados pelo construtor (princípio Aberto/Fechado).
 */
public class CalculadoraDeFrete {

    public static final BigDecimal TARIFA_BASE = new BigDecimal("12.00");
    public static final BigDecimal VALOR_KG_EXCEDENTE = new BigDecimal("4.00");
    public static final BigDecimal LIMITE_FRANQUIA_KG = new BigDecimal("1.00");
    public static final BigDecimal LIMITE_CARGA_PESADA_KG = new BigDecimal("30.00");
    public static final BigDecimal ACRESCIMO_CARGA_PESADA = new BigDecimal("25.00");

    private final BigDecimal tarifaBase;
    private final BigDecimal valorKgExcedente;
    private final BigDecimal acrescimoCargaPesada;

    public CalculadoraDeFrete() {
        this(TARIFA_BASE, VALOR_KG_EXCEDENTE, ACRESCIMO_CARGA_PESADA);
    }

    public CalculadoraDeFrete(BigDecimal tarifaBase, BigDecimal valorKgExcedente, BigDecimal acrescimoCargaPesada) {
        this.tarifaBase = tarifaBase;
        this.valorKgExcedente = valorKgExcedente;
        this.acrescimoCargaPesada = acrescimoCargaPesada;
    }

    /**
     * @param peso peso em kg (validado e arredondado para 2 casas por {@link Peso})
     * @return valor do frete em reais, com 2 casas decimais
     * @throws exception.RegraDeNegocioException peso nulo, zero, negativo ou acima de 999,99
     */
    public BigDecimal calcular(BigDecimal peso) {
        BigDecimal kg = Peso.normalizar(peso);
        BigDecimal total = tarifaBase;
        if (kg.compareTo(LIMITE_FRANQUIA_KG) > 0) {
            total = total.add(kg.subtract(LIMITE_FRANQUIA_KG).multiply(valorKgExcedente));
        }
        if (ehCargaPesada(kg)) {
            total = total.add(acrescimoCargaPesada);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /** Verdadeiro para pesos estritamente acima de 30,00 kg. */
    public boolean ehCargaPesada(BigDecimal peso) {
        return Peso.normalizar(peso).compareTo(LIMITE_CARGA_PESADA_KG) > 0;
    }
}
