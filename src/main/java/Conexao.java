import java.sql.*;

public class Conexao{

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    // Método para criar e manter conexão com o banco. Retorna true caso a conexão esteja certa.
    // Retorna false caso algo tenha dado errado.
    public boolean conectar() {

        try {
            // Informar qual driver de conexão será utilizado pelo DriveManager
            Class.forName("org.postgresql.Driver");

            // Criando a conexão com o BD
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://babar.db.elephantsql.com:5432/mxzaicyi", "mxzaicyi", "Fm-KOjfBqXW-A2jO8qy6OE1yrmZNXeF8");

            return true; // retorna true caso a conexão tenha acontecido
        } catch (ClassNotFoundException cnfe) {
            // Essa classe não vamos usar só no Java, o sout não ocorre em tudo
            cnfe.printStackTrace(); // mostra o erro sem usar o sout

        } catch (SQLException SQL) {
            SQL.printStackTrace();

        }

        return false; // retorna false caso algum erro tenha ocorrido

    }

    // Método para desconectar com o banco.
    public void desconectar() {
        try {

            if (conn != null && !conn.isClosed()) {
                // Desconectando DB
                conn.close();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Método que pega a última sequencia da tabela
    public ResultSet buscarSeq() {
        if (conectar()) {
            // Instanciando o objeto prepareStatement (pstmt)
            try {
                pstmt = conn.prepareStatement("select nextval('PRODID') prodid");

                // Executando o comando SQL do objeto preparedStatement e armazenando no ResultSet
                rs = pstmt.executeQuery();

            } catch (SQLException SQL) {
                SQL.printStackTrace();
                return null;
            } finally { // Sempre vai desconectar
                desconectar();
            }
        } else {
            return null;
        }
        return rs;
    }

    // Método para inserir na tabela PRODUCT
    public boolean inserir(int prodID, String descrip) {

        if (conectar()) { // verificando se a conexão ocorreu sem erros
            try {
                // Preparando o comando que será executado
                pstmt = conn.prepareStatement("INSERT INTO PRODUCT (PRODID, DESCRIP) VALUES (?, ?)");
                // Setando o valor dos parâmetros
                pstmt.setInt(1, prodID);
                pstmt.setString(2, descrip);

                pstmt.execute(); // Executando o comando sql do preparedStatement

                return true; // retorna true caso a inserção tenha ocorrido

            } catch (SQLException e) {
                e.printStackTrace();
                return false; // retorna false caso tenha ocorrido algum erro
            } finally {
                desconectar(); // Fechando a conexão com o banco
            }
        } else {
            return false;
        }
    }

    // Método que altera a descrição do produto de acordo com o seu código
    public int alterarDescrip(int prodID, String descrip) {

        if (conectar()) { // verificando se a conexão ocorreu sem erros
            try {
                pstmt = conn.prepareStatement("UPDATE PRODUCT SET DESCRIP = ? WHERE PRODID = ?");

                // Setando os parâmetros
                pstmt.setString(1, descrip);
                pstmt.setInt(2, prodID);

                if (pstmt.executeUpdate() == 0) { // executeUpdate retorna o número de linhas afetadas
                    return 0;
                }

                return 1; // retorna 1 caso a alteração tenha sido feita

            } catch (SQLException e) {
                e.printStackTrace();
                return -1; // retorna -1 caso tenha ocorrido algum erro
            } finally {
                desconectar();
            }
        } else {
            return -1;
        }
    }

    // Método que remove o produto de acordo com o seu código
    public int removerProd( int prodID) {

        if (conectar()) {

            try {
                String remover = "DELETE FROM PRODUCT WHERE PRODID = ?";
                // Instanciando objeto preparedStatement
                pstmt = conn.prepareStatement(remover);
                // Setando o valor do parâmetro
                pstmt.setInt(1, prodID);

                if (pstmt.executeUpdate() == 0) {
                    return 0;
                }

                return 1; // retorna 1 caso a remoção tenha acontecido
            } catch (SQLException e) {
                e.printStackTrace();
                return -1; // retorna -1 caso tenha ocorrido algum erro
            } finally {
                desconectar();
            }

        } else {
            return -1;
        }
    }

    // Método que busca todos os produtos
    public ResultSet buscarTodos() {
        if (conectar()) {
            // Instanciando o objeto prepareStatement (pstmt)
            try {
                pstmt = conn.prepareStatement("SELECT * FROM PRODUCT ORDER BY PRODID");

                // Executando o comando SQL do objeto preparedStatement e armazenando no ResultSet
                rs = pstmt.executeQuery();

            } catch (SQLException SQL) {
                SQL.printStackTrace();
                return null;
            } finally { // Sempre vai desconectar
                desconectar();
            }
        } else {
            return null;
        }
        return rs;
    }

    // Método que busca o produto de acordo com o seu código
    public ResultSet buscarPorID(int prodID) {
        if (conectar()) {
            // Instanciando o objeto prepareStatement (pstmt)
            try {
                pstmt = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PRODID = ? ORDER BY PRODID");

                // Executando o comando SQL do objeto preparedStatement e armazenando no ResultSet
                pstmt.setInt(1, prodID);
                rs = pstmt.executeQuery();
            } catch (SQLException SQL) {
                SQL.printStackTrace();
                return null;
            } finally { // Sempre vai desconectar
                desconectar();
            }
        } else {
            return null;
        }

        return rs;
    }

    // Método que busca o produto de acordo com a sua descrição
    public ResultSet buscarPorDescrip(String descrip) {
        if (conectar()) {
            // Instanciando o objeto prepareStatement (pstmt)
            try {
                pstmt = conn.prepareStatement("SELECT * FROM PRODUCT WHERE UPPER(DESCRIP) LIKE UPPER(?)");
                descrip = "%" + descrip + "%";
                pstmt.setString(1, descrip);
                rs = pstmt.executeQuery();

            } catch (SQLException SQL) {
                SQL.printStackTrace();
                return null;
            } finally { // Sempre vai desconectar
                desconectar();
            }
        } else {
            return null;
        }
        return rs;
    }
}
