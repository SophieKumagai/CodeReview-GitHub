import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        Scanner input = new Scanner(System.in);
        int seq, intEscolha = 1, codProduto = 0;
        String descrip, strLista;
        boolean boolVerif;
        ResultSet rs;

        System.out.println("\n\033[0;35m---------- JDBC 2024 ----------\033[0m");
        System.out.println("\nBanco: dbGerminare2\nTabela: PRODUCT");

        // Repetindo até a escolha ser 0 - SAIR
        do {
            // Verificando escolha do menu e mostrando o menu
            do {
                try {
                    boolVerif = true;
                    System.out.print("""
                                                    
                            \033[0;36mMenu\033[0m
                                    
                            [ 1 ] Inserir
                            [ 2 ] Alterar
                            [ 3 ] Excluir
                            [ 4 ] Mostrar
                            [ 5 ] Buscar por ID
                            [ 6 ] Buscar por Descrição
                            \033[0;31m[ 0 ] Sair\033[0m
                                                    
                            ESCOLHA:\s""");

                    intEscolha = input.nextInt();

                    if (intEscolha < 0 || intEscolha > 6) {
                        throw new Exception("\n\033[0;31mEscolha inválida!\033[0m");
                    }
                } catch (InputMismatchException ime) {
                    boolVerif = false;
                    System.out.println("\n\033[0;31mDigite apenas números!\033[0m");
                    input.next();
                } catch (Exception e) {
                    boolVerif = false;
                    System.out.println(e.getMessage());
                }
            } while (!boolVerif);
            // Verificando escolha do menu e mostrando o menu

            switch (intEscolha) {
                // INSERIR
                case 1 -> {
                    System.out.println("\n\033[0;35m---------- INSERIR ----------\033[0m");
                    // Pegando o próximo código da sequence
                    descrip = input.nextLine();
                    System.out.print("Digite a descrição do novo produto: ");
                    descrip = input.nextLine();

                    rs = conexao.buscarSeq();
                    if (rs != null) { // não teve erro no método
                        try {
                            if (rs.isBeforeFirst()) { // se teve algum resultado
                                if (rs.next()) { // se tiver uma próxima linha
                                    seq = rs.getInt("prodid");

                                    // Colocando na função
                                    if (conexao.inserir(seq, descrip)) {
                                        System.out.println("\n\033[0;36mProduto inserido!\033[0m");
                                    } else { // Ocorreu erro ao inserir
                                        System.out.println("\n\033[0;31mErro ao inserir!\033[0m");
                                    }

                                } // se tem alguma próxima linha para pegar a sequência

                            } else { // se obteve algum resultado para pegar a sequência
                                System.out.println("\n\033[0;31mSEM REGISTROS!\033[0m");
                            }
                        } catch (SQLException e) { // se ao pegar a próxima sequência ocorrer um erro
                            e.printStackTrace();
                        }
                    } else { // se ao pegar a próxima sequência tiver erro
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }

                // ALTERAR
                case 2 -> {
                    System.out.println("\n\033[0;35m---------- ALTERAR ----------\033[0m");
                    // Verificando o código
                    do {
                        try {
                            boolVerif = true;
                            System.out.print("Digite o código do produto que será alterado: ");

                            codProduto = input.nextInt();

                            if (codProduto < 0) {
                                throw new Exception("\n\033[0;31mEscolha inválida!\033[0m");
                            }
                        } catch (InputMismatchException ime) {
                            boolVerif = false;
                            System.out.println("\n\033[0;31mDigite apenas números!\033[0m");
                            input.next();
                        } catch (Exception e) {
                            boolVerif = false;
                            System.out.println(e.getMessage());
                        }
                    } while (!boolVerif);
                    // Verificando o código do produto

                    descrip = input.nextLine();
                    System.out.print("Digite a nova descrição do produto: ");
                    descrip = input.nextLine();

                    if (conexao.alterarDescrip(codProduto, descrip) > 0) {
                        System.out.println("\n\033[0;36mProduto alterado!\033[0m");
                    } else {
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }

                // REMOVER
                case 3 -> {
                    System.out.println("\n\033[0;35m---------- REMOVER ----------\033[0m");
                    // Verificando o código
                    do {
                        try {
                            boolVerif = true;
                            System.out.print("Digite o código do produto que será removido: ");

                            codProduto = input.nextInt();

                            if (codProduto < 0) {
                                throw new Exception("\n\033[0;31mEscolha inválida!\033[0m");
                            }
                        } catch (InputMismatchException ime) {
                            boolVerif = false;
                            System.out.println("\n\033[0;31mDigite apenas números!\033[0m");
                            input.next();
                        } catch (Exception e) {
                            boolVerif = false;
                            System.out.println(e.getMessage());
                        }
                    } while (!boolVerif);
                    // Verificando o código do produto

                    if (conexao.removerProd(codProduto) > 0) {
                        System.out.println("\n\033[0;36mProduto removido!\033[0m");
                    } else {
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }

                // MOSTRAR
                case 4 -> {
                    // Colocando na função
                    System.out.println("\n\n\033[0;35m---------- PRODUTOS ----------\033[0m");
                    rs = conexao.buscarTodos();
                    if (rs != null) { // se não tiver erro no método
                        try {
                            if (rs.isBeforeFirst()) { // se tiver um registro com os parâmetros na query
                                while (rs.next()) {
                                    strLista = ("\nCódigo do Produto: " + rs.getInt("PRODID") + "\nDescrição: "
                                            + rs.getString("DESCRIP")
                                    );

                                    System.out.println(strLista);
                                }
                            } else { // se não tiver encontrado registros
                                System.out.println("\n\033[0;31mSEM REGISTROS!\033[0m");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }

                // BUSCAR POR ID
                case 5 -> {
                    System.out.println("\n\033[0;35m---------- BUSCAR POR ID ----------\033[0m");
                    // Verificando o código
                    do {
                        try {
                            boolVerif = true;
                            System.out.print("Digite o código do produto que será buscado: ");

                            codProduto = input.nextInt();

                            if (codProduto < 0) {
                                throw new Exception("\n\033[0;31mEscolha inválida!\033[0m");
                            }
                        } catch (InputMismatchException ime) {
                            boolVerif = false;
                            System.out.println("\n\033[0;31mDigite apenas números!\033[0m");
                            input.next();
                        } catch (Exception e) {
                            boolVerif = false;
                            System.out.println(e.getMessage());
                        }
                    } while (!boolVerif);
                    // Verificando o código do produto

                    rs = conexao.buscarPorID(codProduto);
                    if (rs != null) { // não teve erro no método
                        try {
                            if (rs.isBeforeFirst()) { // se teve algum resultado
                                if (rs.next()) { // se tiver uma próxima linha
                                    System.out.println("\nCódigo do Produto: " + rs.getInt("PRODID") + "\nDescrição: "
                                            + rs.getString("DESCRIP"));
                                }
                            } else { // se obteve algum resultado para pegar a sequência
                                System.out.println("\n\033[0;31mSEM REGISTROS!\033[0m");
                            }
                        } catch (SQLException e) { // se ao pegar a próxima sequência ocorrer um erro
                            e.printStackTrace();
                        }
                    } else { // se ao pegar a próxima sequência tiver erro
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }

                // BUSCAR POR DESCRIP
                case 6 -> {
                    System.out.println("\n\033[0;35m---------- BUSCAR POR DESCRIP ----------\033[0m");
                    descrip = input.nextLine();
                    System.out.print("Digite a descrição/parte da descrição do produto/produtos que será/serão buscado/buscados: ");
                    descrip = input.nextLine();

                    rs = conexao.buscarPorDescrip(descrip);
                    if (rs != null) { // se não tiver erro no método
                        try {
                            if (rs.isBeforeFirst()) { // se tiver um registro com os parâmetros na query
                                while (rs.next()) {
                                    strLista = ("\nCódigo do Produto: " + rs.getInt("PRODID") + "\nDescrição: "
                                            + rs.getString("DESCRIP")
                                    );

                                    System.out.println(strLista);
                                }
                            } else { // se não tiver encontrado registros
                                System.out.println("\n\033[0;31mSEM REGISTROS!\033[0m");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("\n\033[0;31mERRO!\033[0m");
                    }
                }
            }

        } while (intEscolha != 0);
    }
}