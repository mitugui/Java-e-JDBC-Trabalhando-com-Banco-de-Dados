package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private final Connection conn;

    ContaDAO(Connection connection) {
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) {

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement =  conn.prepareStatement(sql);

            preparedStatement.setInt(1, dadosDaConta.numero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
            preparedStatement.setBoolean(6, true);

            preparedStatement.execute();

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * FROM conta WHERE esta_ativa = true";

        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, cliente, saldo, estaAtiva));
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contas;
    }

    public Conta listarPorNumero(Integer numeroBusca) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        Conta conta = null;

        String sql = "SELECT * FROM conta WHERE numero = ? and esta_ativa = true;";

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, numeroBusca);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numero, cliente, saldo, estaAtiva);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conta;
    }

    public void alterar(Integer numero, BigDecimal valor) {
        PreparedStatement preparedStatement;

        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, valor);
            preparedStatement.setInt(2, numero);

            preparedStatement.execute();

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Integer numero) {
        PreparedStatement preparedStatement;

        String sql = "DELETE FROM conta WHERE numero = ?";

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, numero);

            preparedStatement.execute();

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alterarLogico(Integer numeroDaConta) {
        PreparedStatement preparedStatement;

        String sql = "UPDATE conta SET esta_ativa = false WHERE numero = ?";

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, numeroDaConta);

            preparedStatement.execute();

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
