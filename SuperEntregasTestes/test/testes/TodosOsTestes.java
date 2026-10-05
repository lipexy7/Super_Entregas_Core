package testes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import model.UsuarioTest;
import seguranca.SenhaEmTextoPlanoTest;
import service.AutenticacaoServiceTest;
import service.CalculadoraDeFreteTabelaTest;
import service.CalculadoraDeFreteTest;
import service.EncomendaServiceTest;
import service.PontoDeControleServiceTest;
import service.RastreioServiceTest;
import util.CodigoPostalTest;
import util.ConversorTest;
import util.CpfTest;
import util.PesoTest;

/** Suíte que reúne todos os testes (Run File neste arquivo executa tudo no NetBeans). */
@RunWith(Suite.class)
@SuiteClasses({
    CalculadoraDeFreteTest.class, CalculadoraDeFreteTabelaTest.class, PesoTest.class,
    CpfTest.class, CodigoPostalTest.class, ConversorTest.class, SenhaEmTextoPlanoTest.class,
    AutenticacaoServiceTest.class, EncomendaServiceTest.class, PontoDeControleServiceTest.class,
    RastreioServiceTest.class, UsuarioTest.class
})
public class TodosOsTestes {
}
