package br.net.woodstock.dbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBCLient {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf("Erro nos argumentos informados, use " + DBCLient.class.getCanonicalName() + " <URL> <USER> <PASSWORD> [DRIVER_CLASS]");
		}

		String url = args[0];
		String user = args[1];
		String password = args[2];
		String driverClass = null;
		Connection connection = null;

		if (args.length == 4) {
			driverClass = args[4];
		}
		try {
			if (driverClass != null) {
				try {
					Class.forName(driverClass);
				} catch (ClassNotFoundException e) {
					System.out.printf("Erro ao carregar o Driver JDBC: %s\n", e.getMessage());
				}
			}

			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			Scanner scanner = new Scanner(System.in);
			System.out.printf("SQL > ");
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if ((line != null) && (!line.trim().isEmpty())) {
					if (("EXIT".equalsIgnoreCase(line.trim())) || ("QUIT".equalsIgnoreCase(line.trim()))) {
						break;
					} else if ("COMMIT".equalsIgnoreCase(line.trim())) {
						connection.commit();
					} else if ("ROLLBACK".equalsIgnoreCase(line.trim())) {
						connection.rollback();
					} else {
						builder.append(" ");
						builder.append(line);
						if (line.endsWith(";")) {
							try {
								Statement statement = connection.createStatement();
								ResultSet rs = statement.executeQuery(builder.toString());
								if (rs.next()) {
									ResultSetMetaData metaData = rs.getMetaData();
									int count = metaData.getColumnCount();
									System.out.printf("|");
									for (int i = 1; i <= count; i++) {
										System.out.printf("%-30s|", metaData.getColumnName(i));
									}
									System.out.printf("\n");
									for (int i = 1; i <= count; i++) {
										for (int j = 0; j < 32; j++) {
											System.out.printf("=");
										}
									}
									System.out.printf("\n");
									do {
										System.out.printf("|");
										for (int i = 1; i <= count; i++) {
											String s = rs.getString(i);
											if ((s != null) && (s.length() > 30)) {
												s = s.substring(0, 30);
											}
											System.out.printf("%-30s|", s);
										}
										System.out.printf("\n");
									} while (rs.next());
								}
							} catch (SQLException e) {
								System.out.printf("Erro ao executar a consulta: %s\n", e.getMessage());
								connection.rollback();
							}
							builder = new StringBuilder();
						}
					}
				}
				System.out.printf("SQL > ");
			}
			scanner.close();
		} catch (Exception e) {
			System.out.printf("Erro inesperado: %s\n", e.getMessage());
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					//
				}
			}
		}
	}

}
